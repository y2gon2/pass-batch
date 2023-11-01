package com.fastcampus.pass.repository.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("User Repository Test")
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    @DisplayName("user entity insert (created_at 이 생성되지 않는 부분 확인용)")
    @Test
    public void test_save() {
        // Given
        String userId = "test_user";
        UserEntity user = UserEntity.of(
                userId,
                "test_name",
                UserStatus.ACTIVE,
                "010",
                Map.of("uuid", "asdf1234")
        );

        // When
        userRepository.save(user);

        // Then
        UserEntity findUser = userRepository
                        .findById(userId)
                        .orElseThrow();
        assertEquals(userId, findUser.getUserId());
    }
}