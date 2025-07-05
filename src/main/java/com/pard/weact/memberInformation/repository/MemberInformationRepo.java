package com.pard.weact.memberInformation.repository;

import com.pard.weact.memberInformation.entity.MemberInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberInformationRepo extends JpaRepository<MemberInformation, Long> {
    public MemberInformation findByUserIdAndRoomId(Long userId, Long roomId);
    public List<MemberInformation> findAllByRoomIdAndUserIdNot(Long roomId, Long userId);
}
