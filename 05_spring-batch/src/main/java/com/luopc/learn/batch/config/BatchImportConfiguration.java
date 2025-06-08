package com.luopc.learn.batch.config;

import com.luopc.learn.batch.model.entity.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
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

import javax.sql.DataSource;

/**<a href="https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-5.0-Migration-Guide">Spring Batch</a>**/
@Configuration
public class BatchImportConfiguration {


    @Bean(name = "importUserJob")
    public Job importUserJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource) {
        // 定义Job
        return new JobBuilder("importUserJob", jobRepository)
                .start(csvProcessingStep(jobRepository, transactionManager, dataSource))
                .build();
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
                .names("id", "name", "age", "email")// 字段映射
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
            return Person.builder()
                    .email(person.getEmail().toLowerCase())
                    .build();
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
