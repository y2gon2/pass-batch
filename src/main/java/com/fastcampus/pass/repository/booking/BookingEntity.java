package com.fastcampus.pass.repository.booking;

import com.fastcampus.pass.repository.pass.PassEntity;
import com.fastcampus.pass.repository.user.UserEntity;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
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
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer booking_seq;

    @ManyToOne(optional = false)
    @JoinColumn(name = "passSeq")
    private PassEntity passEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false)
    private Integer usedPass;

    @Column(nullable = false)
    private Integer attended;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endedAt;

    private LocalDateTime cancelledAt;
}
