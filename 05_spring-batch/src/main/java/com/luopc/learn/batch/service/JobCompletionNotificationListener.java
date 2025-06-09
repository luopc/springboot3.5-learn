package com.luopc.learn.batch.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Service;

@Slf4j
@Setter
@Service
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(org.springframework.batch.core.JobExecution jobExecution) {
        String name = jobExecution.getJobParameters().getString("name");
        log.info("Before Job[{}]", name);
    }

    @Override
    public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
        String name = jobExecution.getJobParameters().getString("name");
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("作业成功完成!");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            System.out.println("作业失败!");
        }

    }

}
