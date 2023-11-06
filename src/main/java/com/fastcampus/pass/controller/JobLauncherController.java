package com.fastcampus.pass.controller;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("job")
public class JobLauncherController {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public JobLauncherController(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @PostMapping("/launcher")
    public ExitStatus LaunchJob(@RequestBody JobLauncherRequest request) throws Exception {
        Job job = jobRegistry.getJob(request.getName()); // client 요청 job 이름을 확인하여 job registry 로부터 job 을 가져와고
        return this.jobLauncher.run(job, request.getJobParameters()).getExitStatus(); // job launch 로 받은 paramter 를 넣고 해당 job 을 실행
    }
}
