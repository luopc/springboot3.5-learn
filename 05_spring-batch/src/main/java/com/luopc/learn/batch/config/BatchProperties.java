package com.luopc.learn.batch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "batch")
public class BatchProperties {

    private String inputFile;

}
