package com.pard.weact.User.entity;

import com.pard.weact.User.dto.req.CreateUserDto;
import com.pard.weact.UserInvite.entity.UserInvite;
import com.pard.weact.memberInformation.entity.MemberInformation;
import com.pard.weact.room.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInvite> invites;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberInformation> memberInfos;

    private String userName;

    private String gender;

    private String userId;

    private String pw;

    @Column(nullable = true)
    private String profilePhoto;

    // update 할때 공백인 부분 있으면 알아서 걸러서 수정해줌
    public void update(CreateUserDto req) {
        if (req.getUserName() != null && !req.getUserName().trim().isEmpty()) {
            this.userName = req.getUserName();
        }
        if (req.getGender() != null && !req.getGender().trim().isEmpty()) {
            this.gender = req.getGender();
        }
        if (req.getUserId() != null && !req.getUserId().trim().isEmpty()) {
            this.userId = req.getUserId();
        }
        if (req.getPw() != null && !req.getPw().trim().isEmpty()) {
            this.pw = req.getPw();
        }
    }

    public void updateProfilePhoto(String profileUrl) {
        this.profilePhoto = profileUrl;
    }
    public String getProfilePhotoOrDefault(String defaultUrl) {
        return this.profilePhoto != null ? this.profilePhoto : defaultUrl;
    }
}
