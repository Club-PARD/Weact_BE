package com.pard.weact.login.auth;

import com.pard.weact.User.dto.req.LoginDto;
import com.pard.weact.User.entity.User;
import com.pard.weact.User.repo.UserRepo;
import com.pard.weact.login.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
/*

🔚 실제 흐름 연결해 보면?

    /auth/login에서 ID/PW 맞으면 → JwtUtil.generateToken()으로 토큰 발급
    이후 모든 요청은 JWT 포함 (Authorization: Bearer ~)
    JwtFilter가 토큰 검사 → 통과되면 SecurityContextHolder에 유저 정보 저장
    컨트롤러에서 @AuthenticationPrincipal이나 SecurityContextHolder로 로그인 정보 접근 가능

*/
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        User user = userRepo.findByUserId(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("아이디가 틀렸습니다"));

        if (!user.getPw().equals(dto.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다");
        }

        String token = jwtUtil.generateToken(user.getUserId());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
