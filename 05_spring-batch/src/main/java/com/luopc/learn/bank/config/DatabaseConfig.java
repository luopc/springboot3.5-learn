package com.luopc.learn.bank.config;

//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DatabaseConfig {
//
//    @Bean
//    public DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
//                .addScript("classpath:schema.sql")
//                .build();
//    }
//}