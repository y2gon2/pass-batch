package com.fastcampus.pass.repository.packaze;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Package Repository Test")
@Slf4j
@SpringBootTest // repository 를 진행할 때는 @DataJpaTest 를 주로 진행하지만 이번 경우는 실제 동작을 보기 위해 설정함
@ActiveProfiles("test")
public class PackageRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @DisplayName("package entity insert")
    @Test
    public void test_save() {
        // given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("Body Challenge : 12 Weeks");
        packageEntity.setPeriod(84);

        // when
        packageRepository.save(packageEntity);

        // then
        assertNotNull(packageEntity.getPackageSeq());
    }

    @DisplayName("package 생성 후, 일시 기준으로 찾는다")
    @Test
    public void test_findByCreatedAtAfter() {
        // Given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity0 = PackageEntity.of(
                "3 months only for Students",
                90
        );
        packageRepository.save(packageEntity0);

        PackageEntity packageEntity1 = PackageEntity.of(
                "6 months only for Students",
                180
        );
        packageRepository.save(packageEntity1);

        // When
        final List<PackageEntity> packageEntities = packageRepository
                .findByCreatedAtAfter(
                        dateTime,
                        PageRequest.of(0, 2, Sort.by("packageSeq").descending())
                );

        // Then
        assertEquals(2, packageEntities.size());
        assertEquals(packageEntity0.getPackageSeq(), packageEntities.get(1).getPackageSeq());
    }

    @DisplayName("신규 package 생성 후, column 값을 수정")
    @Test
    public void test_updateCountAndPeriod() {
        // Given
        PackageEntity packageEntity = PackageEntity.of(
                "The Body Profile Event - 4 months",
                90
        );
        packageRepository.save(packageEntity);

        // When
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        final PackageEntity updatedPackageEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        // Then
        assertEquals(1, updatedCount); // 해당 method 작업에 의해 값의 수정이 발생한 record (행) 의 갯수를 의미
        assertEquals(30, updatedPackageEntity.getCount());
        assertEquals(120, updatedPackageEntity.getPeriod());
    }

    @DisplayName("package Entity Delete")
    @Test
    public void test_delete() {
        // Given
        PackageEntity packageEntity = PackageEntity.of(
                "To be Deleted",
                1,
                10
        );
        PackageEntity newPackageEntity = packageRepository.save(packageEntity);

        // When
        packageRepository.deleteById(newPackageEntity.getPackageSeq());

        // Then
        assertTrue(packageRepository.findById(newPackageEntity.getPackageSeq()).isEmpty());
    }
}