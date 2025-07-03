package com.pard.weact.postPhoto.service;

import com.pard.weact.habitPost.dto.req.UploadPhotoDto;
import com.pard.weact.postPhoto.entity.PostPhoto;
import com.pard.weact.postPhoto.repo.PostPhotoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostPhotoService {

    private final PostPhotoRepo postPhotoRepo; // 이미지 정보를 DB에 저장하기 위한 JPA Repository

    @Value("${file.upload-dir}") // application.yml에서 설정한 업로드 경로 주입
    private String uploadDir;


    public Long saveImage(MultipartFile file) throws IOException {

        // 클라이언트가 업로드한 원본 파일 이름 추출 (예: dog.jpg)
        String originalFileName = file.getOriginalFilename();

        // UUID를 붙여 저장 파일 이름 생성 → 중복 방지용 (예: e8f3..._dog.jpg)
        String storedFileName = UUID.randomUUID() + "_" + originalFileName;

        // 현재 프로젝트 실행 경로 (절대 경로 기준, 예: /Users/you/dev/project)
        String basePath = System.getProperty("user.dir");

        // 실제 저장 경로 구성 (예: /Users/you/dev/project/uploads/images)
        String fullUploadDir = Paths.get(basePath,uploadDir).toString();

        // 저장 경로가 존재하지 않으면 폴더 생성
        File dir = new File(fullUploadDir);
        if (!dir.exists()) dir.mkdirs();

        // 최종 저장 경로 설정 (예: /Users/you/dev/project/uploads/images/e8f3..._dog.jpg)
        Path filePath = Paths.get(fullUploadDir, storedFileName);

        // 업로드된 파일을 지정된 경로에 실제로 저장
        file.transferTo(filePath.toFile());

        // 클라이언트가 접근 가능한 이미지 URL 생성
        // 정적 리소스 경로 설정에 따라 파일명만으로 접근 가능
        String url = "http://localhost:8080/" + storedFileName;

        // 이미지 파일 정보를 DB에 저장할 Entity 객체 생성
        PostPhoto postPhoto = PostPhoto.builder()
                .original_name(originalFileName)   // 원본 파일 이름 저장
                .unique_name(storedFileName)       // 서버에 저장된 파일 이름 저장
                .path(url)               // 접근 가능한 URL 저장
                .build();

        // DB에 이미지 정보 저장
        postPhotoRepo.save(postPhoto);

        // id를 클라이언트에 반환
        return postPhoto.getId();
    }

    public String getPhotoPathById(Long photoId) {
        return postPhotoRepo.findById(photoId)
                .map(PostPhoto::getPath)
                .orElseThrow(() -> new IllegalArgumentException("이미지 정보가 없습니다."));
    }

    public ResponseEntity<byte[]> getImageResponseById(Long photoId) {
        try {
            PostPhoto postPhoto = postPhotoRepo.findById(photoId)
                    .orElseThrow(() -> new IllegalArgumentException("사진이 존재하지 않습니다."));

            String fileName = postPhoto.getUnique_name();
            Path path = Paths.get(System.getProperty("user.dir"), uploadDir, fileName);

            byte[] imageBytes = Files.readAllBytes(path);
            String contentType = Files.probeContentType(path);

            return ResponseEntity.ok()
                    .header("Content-Type", contentType != null ? contentType : "image/jpeg")
                    .body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
