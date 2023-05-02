package com.almostThere.domain.meeting.dto.create;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CalculateDetailRequestDto {
    private Long meetingId;
    private String filePath;
    private String fileName;
    private String storeName;
    private int price;
}
