package com.pard.weact.memberInformation.repository;

import com.pard.weact.memberInformation.entity.MemberInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberInformationRepo extends JpaRepository<MemberInformation, Long> {
    public MemberInformation findByUserIdAndRoomId(Long userId, Long roomId);
    public List<MemberInformation> findAllByRoomIdAndUserIdNot(Long roomId, Long userId);

    @Query("""
    SELECT m FROM MemberInformation m
    WHERE m.room.id = :roomId
    AND m.percent = (
        SELECT MAX(m2.percent)
        FROM MemberInformation m2
        WHERE m2.room.id = :roomId
    )
""")
    List<MemberInformation> findTopMembersByRoomId(@Param("roomId") Long roomId);

    @Query("""
    SELECT m FROM MemberInformation m
    WHERE m.room.id = :roomId
      AND m.percent = (
          SELECT MAX(m2.percent) FROM MemberInformation m2
          WHERE m2.room.id = :roomId
            AND m2.percent < (
                SELECT MAX(m3.percent) FROM MemberInformation m3
                WHERE m3.room.id = :roomId
            )
      )
""")
    List<MemberInformation> findSecondPlaceMembersByRoomId(@Param("roomId") Long roomId);

    @Query("""
    SELECT m FROM MemberInformation m
    WHERE m.room.id = :roomId
      AND m.percent = (
          SELECT MAX(m3.percent) FROM MemberInformation m3
          WHERE m3.room.id = :roomId
            AND m3.percent < (
                SELECT MAX(m2.percent) FROM MemberInformation m2
                WHERE m2.room.id = :roomId
                  AND m2.percent < (
                      SELECT MAX(m1.percent) FROM MemberInformation m1
                      WHERE m1.room.id = :roomId
                  )
            )
      )
""")
    List<MemberInformation> findThirdPlaceMembersByRoomId(@Param("roomId") Long roomId);

    List<MemberInformation> findByRoomIdOrderByPercentDesc(Long roomId);

    List<MemberInformation> findByRoomId(Long roomId);
}
