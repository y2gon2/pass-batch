package com.fastcampus.pass.repository.pass;

import com.fastcampus.pass.repository.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "bulk_pass")
public class BulkPassEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bulkPassSeq;

    @Column(nullable = false)
    private Integer packageSeq;

    @Column(nullable = false)
    private String userGroupId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BulkPassStatus status;

    private Integer count;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(nullable = false)
    private LocalDateTime startedAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endedAt;

    protected BulkPassEntity() {};
    private BulkPassEntity(
            Integer packageSeq,
            String userGroupId,
            BulkPassStatus status,
            Integer count,
            LocalDateTime startedAt,
            LocalDateTime endedAt
    ) {
        this.packageSeq = packageSeq;
        this.userGroupId = userGroupId;
        this.status = status;
        this.count = count;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public static BulkPassEntity of(
            Integer packageSeq,
            String userGroupId,
            BulkPassStatus status,
            Integer count,
            LocalDateTime startedAt,
            LocalDateTime endedAt
    ) {
       return new BulkPassEntity(
               packageSeq,
               userGroupId,
               status,
               count,
               startedAt,
               endedAt
        );
    }

    public static BulkPassEntity of(
            Integer packageSeq,
            String userGroupId,
            BulkPassStatus status,
            LocalDateTime startedAt
    ) {
        return new BulkPassEntity(
                packageSeq,
                userGroupId,
                status,
                null,
                startedAt,
                null
        );
    }
}

