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
    private Integer pass_seq;

    @ManyToOne(optional = false)
    @JoinColumn(name = "packageSeq")
    private PackageEntity packageEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @Column(nullable = false)
    private PassStatus status;

    private Integer remainingCount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(nullable = false)
    private LocalDateTime startedAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiredAt;
}
