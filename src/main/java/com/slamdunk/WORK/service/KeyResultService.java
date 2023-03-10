package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.request.KeyResultRequest;
import com.slamdunk.WORK.dto.response.KeyResultDetailResponse;
import com.slamdunk.WORK.dto.response.KeyResultResponse;
import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.repository.KeyResultRepository;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeyResultService {
    private final KeyResultRepository keyResultRepository;
    private final ObjectiveRepository objectiveRepository;
    private final UserKeyResultService userKeyResultService;

    //핵심결과 생성
    @Transactional
    public ResponseEntity<?> registerKeyResult(Long objectiveId, KeyResultRequest keyResultRequest, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            Optional<Objective> objectiveCheck = objectiveRepository.findById(objectiveId);
            if (objectiveCheck.isEmpty()) {
                return new ResponseEntity<>("목표가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
            } else {
                for (int i = 0; i< keyResultRequest.getKeyResultDate().size(); i++) {
                    KeyResult newKeyResult = new KeyResult(
                            objectiveCheck.get(),
                            keyResultRequest.getKeyResultDate().get(i));
                    keyResultRepository.save(newKeyResult);

                    userKeyResultService.registerUserKeyResult(newKeyResult, objectiveCheck.get(), userDetails);
                }
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>("팀장만 생성 가능합니다." ,HttpStatus.FORBIDDEN);
        }
    }

    //핵심결과 전체 조회
    public ResponseEntity<?> allKeyResult(UserDetailsImpl userDetails) {
        List<Long> keyResultId = userKeyResultService.allKeyResult(userDetails);

        List<KeyResultResponse> keyResultResponseList = new ArrayList<>();
        for (int i=0; i<keyResultId.size(); i++) {
        Optional<KeyResult> keyResult = keyResultRepository.findById(keyResultId.get(i));
            if (keyResult.isPresent()) {
                KeyResultResponse keyResultResponse = KeyResultResponse.builder()
                        .myKeyResult(userKeyResultService.checkMyKeyResult(keyResult.get().getId(), userDetails))
                        .keyResultId(keyResult.get().getId())
                        .keyResult(keyResult.get().getKeyResult())
                        .progress(keyResult.get().getProgress())
                        .emotion(keyResult.get().getEmoticon())
                        .build();

                keyResultResponseList.add(keyResultResponse);
            }
        }

        return new ResponseEntity<>(keyResultResponseList, HttpStatus.OK);
    }

    //핵심결과 상세 조회
    public ResponseEntity<?> detailKeyResult(Long keyResultId, UserDetailsImpl userDetails) {
        Optional<KeyResult> keyResult = keyResultRepository.findById(keyResultId);
        if (keyResult.isPresent()) {
            KeyResultDetailResponse keyResultDetailResponse = KeyResultDetailResponse.builder()
                    .myKeyResult(userKeyResultService.checkMyKeyResult(keyResultId, userDetails))
                    .keyResultId(keyResult.get().getId())
                    .keyResult(keyResult.get().getKeyResult())
                    .build();

            return new ResponseEntity<>(keyResultDetailResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("존재하지 않는 핵심결과입니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
