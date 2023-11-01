package com.fastcampus.pass.repository.notification;

import com.fastcampus.pass.repository.booking.BookingEntity;
import com.fastcampus.pass.repository.booking.BookingStatus;
import com.fastcampus.pass.repository.pass.PassEntity;
import com.fastcampus.pass.repository.pass.PassStatus;
import com.fastcampus.pass.repository.user.UserEntity;
import com.fastcampus.pass.repository.user.UserStatus;
import com.fastcampus.pass.util.LocalDateTimeUtils;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationModelMapperTest {

    @Test
    public void test_toNotificationEntity() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final String userId = "C100" + RandomStringUtils.randomNumeric(4);
        final Map<String, Object> uuid = Map.of("uuid", "abcd1234");

        UserEntity userEntity = UserEntity.of(
          userId,
          "김철수",
          UserStatus.ACTIVE,
          "0123456",
          uuid
        );

        final NotificationEvent event = NotificationEvent.BEFORE_CLASS;

        PassEntity passEntity = PassEntity.of(
                1,
                userId,
                PassStatus.READY,
                1,
                now.minusDays(30),
                now.plusDays(30)
        );

        BookingEntity bookingEntity = BookingEntity.of(
                passEntity,
                userEntity,
                BookingStatus.READY,
                false,
                false,
                now.minusDays(1),
                now.plusDays(1)
        );

        // when
        final NotificationEntity notificationEntity =
                NotificationModelMapper
                        .INSTANCE
                        .toNotificationEntity(bookingEntity, event);

        // then
        assertEquals("abcd1234", notificationEntity.getUuid());
        assertEquals(event, notificationEntity.getEvent());
        assertEquals(
                String.format(
                        "안녕하세요. %s 수업 시작합니다. 수업 전 출석 체크 부탁드립니다. \uD83D\uDE0A",
                        LocalDateTimeUtils.format(now.minusDays(1))
                ),
                notificationEntity.getText()
        );
    }

}