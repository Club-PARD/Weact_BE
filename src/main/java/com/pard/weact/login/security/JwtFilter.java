package com.pard.weact.login.security;

import com.pard.weact.User.repo.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 모든 요청 전에 JWT 토큰을 검사하여 로그인 여부를 판단하고,
 * 유효한 경우 SecurityContext에 인증 정보를 등록하는 필터입니다.
 */
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 요청 헤더에서 Authorization 추출
        String authHeader = request.getHeader("Authorization");

        // 2. "Bearer {token}" 형식일 경우만 처리
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 제거

            // 3. 토큰 유효성 검사
            if (jwtUtil.validateToken(token)) {
                // 4. 토큰에서 userId (String) 추출
                String userId = jwtUtil.getUserIdFromToken(token);

                // 5. userId로 DB에서 실제 User(Long id 포함) 조회
                var userOpt = userRepo.findByUserId(userId);
                if (userOpt.isPresent()) {
                    var user = userOpt.get();

                    // 6. 사용자 정보를 기반으로 인증 객체 생성
                    var auth = new UsernamePasswordAuthenticationToken(
                            user, null, null // 비밀번호와 권한은 사용하지 않음 -> 인증된 사용자 이지만 특별한 권한은 없다
                    );

                    // 7. 인증 상세 정보 설정 및 SecurityContext에 등록
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //위에거 요청의 IP 주소, 세션 ID, User-Agent 같은 추가 정보를 인증 객체에 붙임, 필수는 아니지만 디버깅할때 쓰임.

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    //사용자 로그인된거라고 spring security한테 알려주는거
                    // -> @AuthenticationPrincipal User user로 로그인 사용자 접근 가능
                }
            }
        }

        // 8. 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }
}
