package com.fastcampus.pass.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Auditing 작업 (해당 project 에서는 BaseEntity)
@Configuration
public class JpaConfig {
}
