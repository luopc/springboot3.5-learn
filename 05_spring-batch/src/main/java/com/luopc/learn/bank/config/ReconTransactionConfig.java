package com.luopc.learn.bank.config;

import com.luopc.learn.bank.model.BankTransaction;
import com.luopc.learn.bank.model.ReconTransaction;
import com.luopc.learn.bank.processor.DataMatchingProcessor;
import com.luopc.learn.bank.processor.DiscrepancyClassifier;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.CompositeItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class ReconTransactionConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;


    @Bean
    public Step downloadStep() {
        return new StepBuilder("downloadStep", jobRepository).
                tasklet((contribution, chunkContext) -> {
                    // 实现SFTP下载逻辑
                    //sftpService.download("/bank/recon/20231001.csv");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    // 核心对账Step
    @Bean
    public Step reconStep() {
        return new StepBuilder("reconStep", jobRepository)
                .<ReconTransaction, ReconTransaction>chunk(1000, transactionManager)
                .reader(reconCompositeReader())
                .processor(reconCompositeProcessor())
                .writer(reconCompositeWriter())
                .faultTolerant()
                .skipLimit(100)
                .skip(DataIntegrityViolationException.class)
                .retryLimit(3)
                .retry(PessimisticLockingFailureException.class)
                .build();
    }

    // 组合处理器
    @Bean
    public CompositeItemProcessor<ReconTransaction, ReconTransaction> reconCompositeProcessor() {
        List<ItemProcessor<?, ?>> delegates = new ArrayList<>();
        delegates.add(new DataMatchingProcessor());
        delegates.add(new DiscrepancyClassifier());
        return new CompositeItemProcessor<>(delegates);
    }

    @Bean
    public CompositeItemReader<ReconTransaction> reconCompositeReader() {
        return new CompositeItemReader<>(List.of(bankFileReader(), internalDbReader()));
    }

    // 银行文件读取器
    @Bean
    public FlatFileItemReader<ReconTransaction> bankFileReader() {
        return new FlatFileItemReaderBuilder<ReconTransaction>()
                .name("bankFileReader")
                .resource(new FileSystemResource("recon/20231001.csv"))
                .delimited()
                .names("transactionId", "tradeTime", "amount", "bankSerialNo")
                .fieldSetMapper(fieldSet -> {
                    ReconTransaction t = new ReconTransaction();
                    t.setTransactionId(fieldSet.readString("transactionId"));
                    t.setBankSerialNo(fieldSet.readString("bankSerialNo"));
                    t.setBankAmount(fieldSet.readBigDecimal("amount"));
                    return t;
                })
                .build();
    }

    // 内部数据库读取器
    @Bean
    public JdbcCursorItemReader<ReconTransaction> internalDbReader() {
        return new JdbcCursorItemReaderBuilder<ReconTransaction>()
                .name("internalDbReader")
                .dataSource(dataSource)
                .sql("SELECT order_no, amount, status FROM transactions WHERE trade_date = ?")
                .rowMapper((rs, rowNum) -> {
                    ReconTransaction t = new ReconTransaction();
                    t.setInternalOrderNo(rs.getString("order_no"));
                    t.setSystemAmount(rs.getBigDecimal("amount"));
                    return t;
                })
                .preparedStatementSetter(ps -> ps.setString(1, "2023-10-01"))
                .build();
    }


    // 组合写入器
    @Bean
    public CompositeItemWriter<ReconTransaction> reconCompositeWriter() {
        //discrepancyDbWriter(),
        //alertMessageWriter()
        return new CompositeItemWriterBuilder<ReconTransaction>()
                .delegates(jdbcBatchItemWriter(), jdbcBatchItemWriter())
                .build();
    }

    @Bean
    public Step reportStep() {
        return new StepBuilder("reportStep", jobRepository)
                .<ReconTransaction, ReconTransaction>chunk(1000, transactionManager)
                .reader(discrepancyReader())
                .writer(excelWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<ReconTransaction> discrepancyReader() {
        return new JdbcPagingItemReaderBuilder<ReconTransaction>()
                .name("discrepancyReader")
                .dataSource(dataSource)
                .selectClause("SELECT *")
                .fromClause("FROM discrepancy_records")
                .whereClause("WHERE recon_date = '2023-10-01'")
                .sortKeys(Collections.singletonMap("transaction_id", Order.ASCENDING))
                .rowMapper(new BeanPropertyRowMapper<>(ReconTransaction.class))
                .build();
    }

    @Bean
    public FlatFileItemWriter<ReconTransaction> excelWriter() {
        return new FlatFileItemWriterBuilder<ReconTransaction>()//
                .resource(new FileSystemResource("reports/2023-10-01.csv"))
                .delimited()
                .names("id", "name", "language", "os")
                .name("customerCsvItemReader")
//                .forceSync(fs -> new BankTransaction())
                .build();
//        return new FlatFileItemReaderBuilder<ReconTransaction>()
//                .resource(new FileSystemResource("filePath"))
//                .lineMapper(new DefaultLineMapper<>() {{
//                    setLineTokenizer(new DelimitedLineTokenizer());
//                    setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
//                        setTargetType(ReconTransaction.class);
//                    }});
//                }})
//                .linesToSkip(1)
//                .strict(false) // 允许文件结尾空行
//                .saveState(false) // 禁用状态保存
//                .build();
    }

    @Bean
    public JdbcBatchItemWriter<ReconTransaction> jdbcBatchItemWriter() {
        return null;
    }
//
//    @Bean
//    public JdbcCursorItemReader<ReconTransaction> excelWriter(){
//
//    }
}
