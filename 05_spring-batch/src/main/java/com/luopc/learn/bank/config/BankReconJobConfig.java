package com.luopc.learn.bank.config;

import com.luopc.learn.bank.model.BankTransaction;
import com.luopc.learn.bank.model.ReconTransaction;
import com.luopc.learn.bank.model.ReconciliationResult;
import com.luopc.learn.bank.processor.BankATransactionReader;
import com.luopc.learn.bank.processor.BankBTransactionReader;
import com.luopc.learn.bank.processor.ReconciliationResultWriter;
import com.luopc.learn.bank.processor.TransactionMatchingProcessor;
import com.luopc.learn.batch.service.JobCompletionNotificationListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.CompositeItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class BankReconJobConfig {

    private static final Resource CSV = new ClassPathResource("/customers.csv");

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private BankATransactionReader bankAReader;
    @Autowired
    private BankBTransactionReader bankBReader;
    @Autowired
    private TransactionMatchingProcessor processor;
    @Autowired
    private ReconciliationResultWriter writer;
    @Autowired
    private JobCompletionNotificationListener listener;

    @Bean
    public CompositeItemReader<List<BankTransaction>> compositeReader(JdbcCursorItemReader<List<BankTransaction>> customerJdbcItemReader,
                                                                      FlatFileItemReader<List<BankTransaction>> customerCsvItemReader) {
        return new CompositeItemReader<>(List.of(customerJdbcItemReader, customerCsvItemReader));
    }


    @Bean
    FlatFileItemReader<List<BankTransaction>> customerCsvItemReader() {
        return new FlatFileItemReaderBuilder<List<BankTransaction>>()//
                .resource(CSV)
                .delimited()
                .names("id", "name", "language", "os")
                .name("customerCsvItemReader")
                .fieldSetMapper(fs -> new ArrayList<>())
                .build();
    }

    @Bean
    JdbcCursorItemReader<List<BankTransaction>> customerJdbcItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<List<BankTransaction>>()//
                .name("customerJdbcItemReader")//
                .dataSource(dataSource)//
                .sql("select id, name, language, os from customer")//
//                .rowMapper((rs, rowNum) -> new BankTransaction(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)))
                .rowMapper((rs, rowNum) -> new ArrayList<>())
                .build();
    }


    @Bean
    public Step reconciliationStep() {
        return new StepBuilder("reconciliationStep", jobRepository)
                .<List<BankTransaction>, ReconciliationResult>chunk(1, transactionManager)
                .reader(compositeReader(customerJdbcItemReader(dataSource), customerCsvItemReader()))
                .processor(processor)
                .writer(writer)
                .build();
    }


    @Bean(name = "bankReconciliationJob1")
    public Job bankReconciliationJob1() {
        return new JobBuilder("bankReconciliationJob1", jobRepository)
                .listener(listener)
                .start(reconciliationStep())
                .build();
    }



    @Bean(name = "bankReconciliationJob2")
    public Job bankReconciliationJob2() {
        return new JobBuilder("bankReconciliationJob2", jobRepository)
                .listener(listener)
                .start(reconciliationStep())
                .build();
    }
}