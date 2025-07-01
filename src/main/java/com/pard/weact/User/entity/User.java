package com.pard.weact.User.entity;

import com.pard.weact.User.dto.req.CreateUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String gender;

    private String userId;

    private String pw;

    @Column(nullable = true)
    private String profilePhoto;

    // update 할때 공백인 부분 있으면 알아서 걸러서 수정해줌
    public void update(CreateUser req) {
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

}
