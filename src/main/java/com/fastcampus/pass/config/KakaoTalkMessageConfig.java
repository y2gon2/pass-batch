package com.fastcampus.pass.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakaotalk") // application.ymal 에서 kakao.host / kakao.token 설정값을 가져와서 사용할 있도록 해줌
public class KakaoTalkMessageConfig {
    private String host;
    private String token;
}
