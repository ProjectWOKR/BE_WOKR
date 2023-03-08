package com.slamdunk.WORK.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
public class ObjectiveRequest {
    private String objective;
    private LocalDate startDate;
    private LocalDate endDate;
    private int color;
}
