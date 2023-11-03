package com.fastcampus.pass.job.statistics;

import com.fastcampus.pass.config.TestBatchConfig;
import com.fastcampus.pass.repository.booking.BookingEntity;
import com.fastcampus.pass.repository.booking.BookingRepository;
import com.fastcampus.pass.repository.booking.BookingStatus;
import com.fastcampus.pass.repository.pass.PassEntity;
import com.fastcampus.pass.repository.pass.PassRepository;
import com.fastcampus.pass.repository.pass.PassStatus;
import com.fastcampus.pass.repository.statistics.StatisticsRepository;
import com.fastcampus.pass.repository.user.UserEntity;
import com.fastcampus.pass.repository.user.UserRepository;
import com.fastcampus.pass.repository.user.UserStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;

import javax.batch.runtime.JobExecution;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@DisplayName("Booking record 에서 일별/주별 참석/취소/전체 기록을 생성한다.")
@SpringBootTest
@SpringBatchTest
@EnableJpaAuditing
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        TestBatchConfig.class,
        MakeStatisticsJobConfig.class,
        MakeDailyStatisticsTasklet.class,
        MakeWeeklyStatisticsTasklet.class
})
class MakeStatisticsJobConfigTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private StatisticsRepository statisticsRepository;
    @Autowired private PassRepository passRepository;
    @Autowired private UserRepository userRepository;



    @DisplayName("주어진 booking entity 들에 대해 일간, 주간 통계 csv 파일을 생성한다.")
    @Test
    public void test_makeStatisiticFile() {
        // Given
        addBookingEntity();

        // When

        // Then
    }

    private void addBookingEntity() {
        final LocalDateTime now = LocalDateTime.now();
        final String userId = "D100" + RandomStringUtils.randomNumeric(4);

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
                "name_make_statistics",
                UserStatus.ACTIVE,
                "01012345678",
                Map.of("uuid", "abcd1234")
        );
        userRepository.save(userEntity);

        List<BookingEntity> bookingList = new ArrayList<>();

        for(int i = 0; i < 10 ; i++) {
            BookingEntity booking = BookingEntity.of(
                passEntity,
                userEntity,
                BookingStatus.COMPLETED,
                    true,
                    true,
                    now.minusDays(10),
                    now.minusDays(10).plusMinutes(i)
            );
            bookingList.add(booking);
        }
        bookingRepository.saveAll(bookingList);
    }

}