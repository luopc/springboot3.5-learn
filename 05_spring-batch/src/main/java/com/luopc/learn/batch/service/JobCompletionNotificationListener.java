package com.luopc.learn.batch.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Service;

@Slf4j
@Setter
@Service
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(org.springframework.batch.core.JobExecution jobExecution) {
        System.out.println("Before Job");
    }

    @Override
    public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
        if (jobExecution.getStatus() == org.springframework.batch.core.BatchStatus.COMPLETED) {
            System.out.println("After Job");
        }

    }

}
