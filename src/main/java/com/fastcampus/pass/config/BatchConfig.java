package com.fastcampus.pass.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

    /**
     * JobRegistry 는 context 에서 Job 을 추적할 때 유용
     * JobRegistryBeanPostProcessor 는 Application Context 가 올라오면서 Bean 등록 시,
     * 자동으로 JobRegistry 에 Job 을 등록시켜 주어야  JobRegistry 에서 뽑아 쓸 수 있음.
     * */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}
