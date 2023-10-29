package com.fastcampus.pass.config;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@EnableJpaAuditing     // EnableAutoConfiguration 에 포함되어 있으므로 해당 설정을 주석처리함.
@EnableAutoConfiguration // Sprig Boot 에서 제공하는 자동 설정 활성화 @SpringAutoConfiguration 에 포함되어 application 실행시 자동으로 같이 실행됨.
@EnableBatchProcessing
@EntityScan("com.fastcampus.pass.repository")
@EnableJpaRepositories("com.fastcampus.pass.repository")
@EnableTransactionManagement
public class TestBatchConfig {
}
