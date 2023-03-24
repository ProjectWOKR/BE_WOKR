package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.UserKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserKeyResultRepository extends JpaRepository<UserKeyResult, Long> {
    List<UserKeyResult> findAllByTeam(String team);
    Optional<UserKeyResult> findByKeyResultIdAndUserId(Long KeyResultId, Long userId);
    @Query("select ukr from UserKeyResult ukr inner join KeyResult kr on ukr.keyResult.id = kr.id where ukr.objective.id = :objectiveId AND ukr.user.id = :userId AND kr.deleteState = false")
    List<UserKeyResult> findAllByObjectiveIdAndUserId(@Param("objectiveId") Long objectiveId,@Param("userId") Long userId);
}
