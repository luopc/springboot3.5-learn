package com.luopc.learn.batch.demo;

import com.luopc.learn.batch.service.MyDecider;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class DeciderJobDemo {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private MyDecider myDecider;

    public Job deciderJob() {
        return new JobBuilder("deciderJob", jobRepository)
                .start(step1())
                .next(myDecider)
                .from(myDecider).on("weekend").to(step2())
                .from(myDecider).on("workingDay").to(step3())
                .from(step3()).on("*").to(step4())
                .end()
                .build();
    }

    private Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("执行步骤一操作。。。");
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

    private Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("执行步骤二操作[weekend]。。。");
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

    private Step step3() {
        return new StepBuilder("step3", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("执行步骤三操作[workingDay]。。。");
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }


    private Step step4() {
        return new StepBuilder("step4", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("执行步骤四操作[workingDay]。。。");
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }
}