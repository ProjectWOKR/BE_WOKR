package com.slamdunk.WORK.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ProgressRequest {
    private int progress;
}
