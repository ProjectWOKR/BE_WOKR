package com.slamdunk.WORK.dto.response;

import lombok.*;

@NoArgsConstructor
@Getter
public class KeyResultResponse {
    private boolean myKeyResult;
    private Long keyResultId;
    private String keyResult;
    private int progress;
    private int emotion;

    @Builder
    public KeyResultResponse(boolean myKeyResult, Long keyResultId, String keyResult, int progress, int emotion) {
        this.myKeyResult = myKeyResult;
        this.keyResultId = keyResultId;
        this.keyResult = keyResult;
        this.progress = progress;
        this.emotion = emotion;
    }
}
