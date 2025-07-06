package com.pard.weact.habitPost.dto.res;

import com.pard.weact.comment.dto.res.CommentDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResultOneDto {
    private String userName;
    private String message;
    private String imageUrl;
    private Long likeCount;
    private Boolean liked;
    private List<CommentDto> comments; // 댓글 목록
}
