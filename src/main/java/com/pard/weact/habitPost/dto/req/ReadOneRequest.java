package com.pard.weact.habitPost.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadOneRequest {
    private Long postId;
}
