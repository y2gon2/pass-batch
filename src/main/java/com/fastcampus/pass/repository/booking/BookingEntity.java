package com.fastcampus.pass.repository.booking;

import com.fastcampus.pass.repository.BaseEntity;
import com.fastcampus.pass.repository.pass.PassEntity;
import com.fastcampus.pass.repository.user.UserEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Table(name = "booking")
@Entity
public class BookingEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer booking_seq;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "passSeq", insertable = false, updatable = false)
    private PassEntity passEntity;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity userEntity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(nullable = false)
    private boolean usedPass;

    @Column(nullable = false)
    private boolean attended;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime cancelledAt;

    // endedAt 기준, yyyy-MM-HH 00:00:00
    public LocalDateTime getStatisticsAt() {
        return this.endedAt.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    protected BookingEntity() {}

    public BookingEntity(
            PassEntity passEntity,
            UserEntity userEntity,
            BookingStatus status,
            boolean usedPass,
            boolean attended,
            LocalDateTime startedAt,
            LocalDateTime endedAt,
            LocalDateTime cancelledAt
    ) {
        this.passEntity = passEntity;
        this.userEntity = userEntity;
        this.status = status;
        this.usedPass = usedPass;
        this.attended = attended;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.cancelledAt = cancelledAt;
    }

    public static BookingEntity of(
            PassEntity passEntity,
            UserEntity userEntity,
            BookingStatus status,
            boolean usedPass,
            boolean attended,
            LocalDateTime startedAt,
            LocalDateTime endedAt
    ) {
        return new BookingEntity(
                passEntity,
                userEntity,
                status,
                usedPass,
                attended,
                startedAt,
                endedAt,
                null
        );
    }
}
