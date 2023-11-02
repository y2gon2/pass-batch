package com.fastcampus.pass.job.pass;

import com.fastcampus.pass.config.TestBatchConfig;
import com.fastcampus.pass.repository.booking.BookingEntity;
import com.fastcampus.pass.repository.booking.BookingRepository;
import com.fastcampus.pass.repository.booking.BookingStatus;
import com.fastcampus.pass.repository.pass.PassEntity;
import com.fastcampus.pass.repository.pass.PassRepository;
import com.fastcampus.pass.repository.pass.PassStatus;
import com.fastcampus.pass.repository.user.UserEntity;
import com.fastcampus.pass.repository.user.UserRepository;
import com.fastcampus.pass.repository.user.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("이용권 사용 test")
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@EnableJpaAuditing
@ContextConfiguration(classes = {UsePassesJobConfig.class, TestBatchConfig.class})
class UsePassesJobConfigTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private PassRepository passRepository;
    @Autowired private UserRepository userRepository;

    final LocalDateTime now = LocalDateTime.now();
    final String userId = "Name-usePassTest";
    final int previousCount = 10;
    final boolean usedPass = false;

    @Test
    public void test_usePassesJob() throws Exception {
        // Given
        addEntities();

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();

        // Then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals("usePassesJob", jobInstance.getJobName());

        List<PassEntity> passes = passRepository
                .findByUserId(userId)
                .orElseThrow();

        List<BookingEntity> bookings = bookingRepository
                .findByUserId(userId)
                .orElseThrow();

        assertEquals(passes.get(0).getRemainingCount(), previousCount - 1);
        assertTrue(bookings.get(0).isUsedPass());
    }

    private void addEntities() {

        PassEntity passEntity = PassEntity.of(
                1,
                userId,
                PassStatus.PROGRESSED,
                previousCount,
                now.minusDays(30),
                now.plusDays(30)
        );
        passRepository.save(passEntity);

        UserEntity userEntity = UserEntity.of(
                userId,
                "John",
                UserStatus.ACTIVE,
                "010",
                Map.of("uuid", "asdf1234")
        );
        userRepository.save(userEntity);

        BookingEntity bookingEntity = BookingEntity.of(
                passEntity,
                userEntity,
                BookingStatus.COMPLETED,
                usedPass,
                true,
                now.minusMinutes(61),
                now.minusMinutes(1)
        );
        bookingRepository.save(bookingEntity);
    }
}