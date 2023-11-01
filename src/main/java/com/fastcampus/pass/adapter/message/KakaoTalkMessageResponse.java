package com.fastcampus.pass.adapter.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class KakaoTalkMessageResponse {
    // json : successful_receiver_uuids <-> Java 객체 : successfulReceiverUuids field mapping
    // {
    //    "successful_receiver_uuids": ["uuid1", "uuid2", "uuid3"]
    // }
    // 의 Json 을 List<String> 으로 mapping
    @JsonProperty("successful_receiver_uuids")
    private List<String> successfulReceiverUuids;
}
