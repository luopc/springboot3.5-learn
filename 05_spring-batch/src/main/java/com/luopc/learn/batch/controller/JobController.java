package com.luopc.learn.batch.controller;

import com.luopc.learn.batch.demo.FirstJobDemo;
import com.luopc.learn.batch.demo.SplitJobDemo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    private final JobLauncher jobLauncher;
    @Autowired
    private FirstJobDemo firstJobDemo;
    @Autowired
    private SplitJobDemo splitJobDemo;
    @Autowired
    @Qualifier("importUserJob")
    private Job importUserJob;

    public JobController(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @GetMapping("/start-first-job")
    public String startFirstJob() throws Exception {
        jobLauncher.run(firstJobDemo.firstJob(), new JobParameters());
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
        jobLauncher.run(importUserJob, new JobParameters());
        return "批处理作业已启动";
    }
}
