package com.pard.weact.inAppNotification.repository;

import com.pard.weact.inAppNotification.entity.InAppNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InAppNotificationRepo extends JpaRepository<InAppNotification, Long> {
    List<InAppNotification> findAllByTargetId(Long userId);

    InAppNotification findByUserInviteId(Long userInviteId);
}
