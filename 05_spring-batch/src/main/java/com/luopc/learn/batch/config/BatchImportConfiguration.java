package com.luopc.learn.batch.config;

import com.luopc.learn.batch.model.entity.Person;
import com.luopc.learn.batch.service.JobCompletionNotificationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * <a href="https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-5.0-Migration-Guide">Spring Batch</a>
 **/

@Slf4j
@Configuration
public class BatchImportConfiguration {


    @Bean(name = "importUserJob")
    public Job importUserJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource) {
        // 定义Job
        return new JobBuilder("importUserJob", jobRepository)
                //增加校验
                .validator((jobParams) -> {
                    String name = jobParams.getString("name");
                    if (!StringUtils.hasText(name)) {
                        throw new JobParametersInvalidException("name 参数不能为空");
                    }
                })
                .listener(jobCompletionNotificationListener())
                .start(csvProcessingStep(jobRepository, transactionManager, dataSource))
                .build();
    }

    public JobCompletionNotificationListener jobCompletionNotificationListener() {
        return new JobCompletionNotificationListener();
    }

    // 定义Step
    @Bean
    public Step csvProcessingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource) {
        // 每处理100条提交一次事务
        return new StepBuilder("csvProcessing", jobRepository)
                .<Person, Person>chunk(100, transactionManager)
                .reader(userReader())
                .processor(userProcessor())
                .writer(userWriter(dataSource))
                .build();
    }

    @Bean
    public FlatFileItemReader<Person> userReader() {
        // CSV文件读取器
        return new FlatFileItemReaderBuilder<Person>()
                .name("userReader")
                .resource(new ClassPathResource("person.csv"))// 文件路径
                .delimited()
                .delimiter(",")
                .names("name", "age", "email")// 字段映射
                .targetType(Person.class)
                .linesToSkip(1) // 跳过标题行
                .build();
    }


    @Bean
    public ItemProcessor<Person, Person> userProcessor() {
        // 数据处理（示例：年龄校验）
        return person -> {
            if (person.getAge() < 0) {
                throw new IllegalArgumentException("年龄不能为负数: " + person);
            }
            String name = person.getName().toUpperCase();
            int age = person.getAge();
            String email = person.getEmail().toUpperCase();

            Person transformed = new Person(name, age, email);
            log.info("Converting ( {} ) into ( {} )", person, transformed);
            return transformed;
        };
    }


    @Bean
    public JdbcBatchItemWriter<Person> userWriter(DataSource dataSource) {
        // 数据库写入器
        return new JdbcBatchItemWriterBuilder<Person>()
                .dataSource(dataSource)
                .sql("INSERT INTO Person (name, age, email) VALUES (:name, :age, :email)")
                .beanMapped()
                .build();
    }

}
