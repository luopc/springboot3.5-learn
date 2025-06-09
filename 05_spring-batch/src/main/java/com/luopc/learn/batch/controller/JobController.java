package com.luopc.learn.batch.controller;

import com.luopc.learn.batch.demo.FirstJobDemo;
import com.luopc.learn.batch.demo.SplitJobDemo;
import org.hibernate.id.IdentityGenerator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.IdGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class JobController {

    private final JobLauncher jobLauncher;

    /**
     * 通过类的方式创建Job对象，并通过JobLauncher启动作业
     */
    @Autowired
    private FirstJobDemo firstJobDemo;
    @Autowired
    private SplitJobDemo splitJobDemo;
    /**
     * 通过bean的方式创建Job对象，并通过JobLauncher启动作业
     */
    @Autowired
    @Qualifier("importUserJob")
    private Job importUserJob;
//    @Autowired
//    @Qualifier("bankReconciliationJob")
//    private Job bankReconciliationJob;

    public JobController(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @GetMapping("/start-simple-job")
    public String startSimpleJob() throws Exception {
        jobLauncher.run(firstJobDemo.simpleJob(), new JobParameters());
        return "批处理作业已启动";
    }

    @GetMapping("/start-multi-job")
    public String startMultiJob() throws Exception {
        jobLauncher.run(firstJobDemo.multiStepJob(), new JobParameters());
        return "批处理作业已启动";
    }

    @GetMapping("/start-split-job")
    public String startSplitJob() throws Exception {
        jobLauncher.run(splitJobDemo.splitJob(), new JobParameters());
        return "批处理作业已启动";
    }

    @GetMapping("/start-import-job")
    public String startImportJob() throws Exception {
        Map<String, JobParameter<?>> jobParameters = new HashMap<>();
        jobParameters.put("name", new JobParameter<>("importUserJob", String.class));
        jobParameters.put("id", new JobParameter<>(UUID.randomUUID().toString().replaceAll("-", ""), String.class));
        jobLauncher.run(importUserJob, new JobParameters(jobParameters));
        return "批处理作业已启动";
    }
}
