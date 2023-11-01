package com.fastcampus.pass.repository.pass;


import com.fastcampus.pass.repository.BaseEntity;
import com.fastcampus.pass.repository.packaze.PackageEntity;
import com.fastcampus.pass.repository.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Table(name = "pass")
@Entity
public class PassEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passSeq;

    @Column(nullable = false)
    private Integer packageSeq;

    @Column(nullable = false)
    private String userId;

    // table 연동은 추후에
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "packageSeq")
//    private PackageEntity packageEntity;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "userId")
//    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PassStatus status;

    private Integer remainingCount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(nullable = false)
    private LocalDateTime startedAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endedAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiredAt;

    protected PassEntity() {};

    private PassEntity(
            Integer packageSeq,
            String userId,
            PassStatus status,
            Integer remainingCount,
            LocalDateTime startedAt,
            LocalDateTime endedAt,
            LocalDateTime expiredAt
    ) {
        this.packageSeq = packageSeq;
        this.userId = userId;
        this.status = status;
        this.remainingCount = remainingCount;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.expiredAt = expiredAt;
    }

    public static PassEntity of(
            Integer packageSeq,
            String userId,
            PassStatus status,
            Integer remainingCount,
            LocalDateTime startedAt,
            LocalDateTime endedAt,
            LocalDateTime expiredAt
    ) {
      return new PassEntity(
              packageSeq,
              userId,
              status,
              remainingCount,
              startedAt,
              endedAt,
              expiredAt
      );
    }

    public static PassEntity of(
            Integer packageSeq,
            String userId,
            PassStatus status,
            Integer remainingCount,
            LocalDateTime startedAt,
            LocalDateTime endedAt
    ) {
        return new PassEntity(
                packageSeq,
                userId,
                status,
                remainingCount,
                startedAt,
                endedAt,
                null
        );
    }

}
