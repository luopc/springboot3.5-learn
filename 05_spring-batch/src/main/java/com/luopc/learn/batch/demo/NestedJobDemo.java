package com.luopc.learn.batch.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class NestedJobDemo {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;


    public Job parentJob() {
        return new JobBuilder("parentJob", jobRepository)
                .start(childJobOneStep())
                .next(childJobTwoStep())
                .build();
    }


    // 将任务转换为特殊的步骤
    private Step childJobOneStep() {
        return new JobStepBuilder(new StepBuilder("childJobOneStep", jobRepository))
                .job(childJobOne())
                .launcher(jobLauncher)
                .build();
    }

    // 将任务转换为特殊的步骤
    private Step childJobTwoStep() {
        return new JobStepBuilder(new StepBuilder("childJobTwoStep", jobRepository))
                .job(childJobTwo())
                .launcher(jobLauncher)
                .build();
    }

    // 子任务一
    private Job childJobOne() {
        return new JobBuilder("childJobOne", jobRepository)
                .start(
                        new StepBuilder("childJobOneStep", jobRepository)
                                .tasklet((stepContribution, chunkContext) -> {
                                    System.out.println("子任务一执行步骤。。。");
                                    return RepeatStatus.FINISHED;
                                }, transactionManager).build()
                ).build();
    }

    // 子任务二
    private Job childJobTwo() {
        return new JobBuilder("childJobTwo", jobRepository)
                .start(
                        new StepBuilder("childJobTwoStep", jobRepository)
                                .tasklet((stepContribution, chunkContext) -> {
                                    System.out.println("子任务二执行步骤。。。");
                                    return RepeatStatus.FINISHED;
                                }, transactionManager).build()
                ).build();
    }
}
