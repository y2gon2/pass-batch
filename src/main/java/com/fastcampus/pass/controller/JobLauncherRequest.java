package com.fastcampus.pass.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Properties;

@Getter
@Setter
@ToString
public class JobLauncherRequest {    // 요청 client 로 부터
    private String name;             // Job 이름과
    private Properties jobParameters;// parameter 들을 받음.

    public JobParameters getJobParameters() {
        return new JobParametersBuilder(this.jobParameters).toJobParameters();
    }
}
