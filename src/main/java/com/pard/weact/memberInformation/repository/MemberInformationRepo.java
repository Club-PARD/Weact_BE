package com.pard.weact.memberInformation.repository;

import com.pard.weact.memberInformation.entity.MemberInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInformationRepo extends JpaRepository<MemberInformation, Long> {
    public MemberInformation findByUserIdAndRoomId(Long userId, Long roomId);
}
