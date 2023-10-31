package com.fastcampus.pass.repository.pass;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

// ReportingPolicy.IGNORE: 일치하지 않는 필드는 무시
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE) // 매핑되지 않은 대상 필드에 대해 경고 또는 오류 메시지를 출력하지 않음.
public interface PassModelMapper {

    // PassModelMapper의 인스턴스를 생성하고 INSTANCE라는 이름으로 접근할 수 있게 함.
    // 이렇게 해서 외부에서 PassModelMapper.INSTANCE를 통해 매퍼에 접근
    PassModelMapper INSTANCE = Mappers.getMapper(PassModelMapper.class);

    // BulkPassEntity 와 userId 를 가지고 PassEntity instance 생성
    // 필드명이 같지 않거나 custom 하게 mapping 해주기 위해서는 @Mapping 을 추가.
    // 1. status 는 각 entity 에서 전혀 인과 관계가 없으므로, defaultStatus 이름으로 call 가능한
    //    별도의 method 를 생성하여, PassEntity status 값을 설정할 내용을 정의해준다.
    // 2. count field -> remainingCount field 간 mapping 되도록 설정해준다.
    @Mapping(target = "status", qualifiedByName = "defaultStatus")
    @Mapping(target = "remainingCount", source = "bulkPassEntity.count")
    PassEntity toPassEntity(BulkPassEntity bulkPassEntity, String userId);

    // BulkPassStatus 와 관계 없이 PassStatus 값을 설정
    @Named("defaultStatus")
    default PassStatus status(BulkPassStatus status) {
        return PassStatus.READY;
    }
}
