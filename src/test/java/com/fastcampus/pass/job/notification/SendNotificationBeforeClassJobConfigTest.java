package com.fastcampus.pass.job.notification;

import com.fastcampus.pass.adapter.message.KakaoTalkMessageAdapter;
import com.fastcampus.pass.config.KakaoTalkMessageConfig;
import com.fastcampus.pass.config.TestBatchConfig;
import com.fastcampus.pass.repository.BaseEntity;
import com.fastcampus.pass.repository.booking.BookingEntity;
import com.fastcampus.pass.repository.booking.BookingRepository;
import com.fastcampus.pass.repository.booking.BookingStatus;
import com.fastcampus.pass.repository.pass.PassEntity;
import com.fastcampus.pass.repository.pass.PassRepository;
import com.fastcampus.pass.repository.pass.PassStatus;
import com.fastcampus.pass.repository.user.UserEntity;
import com.fastcampus.pass.repository.user.UserRepository;
import com.fastcampus.pass.repository.user.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("예약 알림 batch 기능 test")
@Slf4j
@SpringBatchTest
@SpringBootTest
@EnableJpaAuditing // Auditing Field 에 대한 작업이 정상적으로 test 에서 반영될 수 있도록 해줌
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        SendNotificationBeforeClassJobConfig.class,
        TestBatchConfig.class,
        SendNotificationItemWriter.class,
        KakaoTalkMessageConfig.class,
        KakaoTalkMessageAdapter.class,
})
public class SendNotificationBeforeClassJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PassRepository passRepository;
    @Autowired
    private UserRepository userRepository;


    @DisplayName("시작 10 분전인 booking record 를 찾아 notificaton record 생성")
    @Test
    public void test_addNotificationStep() throws Exception {
        // given
        addBookingEntity();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("addNotificationStep");

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

    }

    private void addBookingEntity() {
        final LocalDateTime now = LocalDateTime.now();
        final String userId = "A100" + RandomStringUtils.randomNumeric(4);

        PassEntity passEntity = PassEntity.of(
                1,
                userId,
                PassStatus.PROGRESSED,
                10,
                now.minusDays(60),
                now.minusDays(1)
        );
        passRepository.save(passEntity);

        UserEntity userEntity = UserEntity.of(
                userId,
                "김영희",
                UserStatus.ACTIVE,
                "01012345678",
                Map.of("uuid", "abcd1234")
        );
        userRepository.save(userEntity);

        BookingEntity bookingEntity = BookingEntity.of(
                passEntity,
                userEntity,
                BookingStatus.READY,
                false,
                false,
                now.plusMinutes(10),
                now.plusMinutes(60)
        );
        bookingRepository.save(bookingEntity);

    }
}