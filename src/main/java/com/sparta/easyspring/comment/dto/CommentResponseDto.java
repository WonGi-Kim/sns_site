package com.sparta.easyspring.comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.easyspring.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private Long likes;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    @QueryProjection
    public CommentResponseDto(Long id, String content, Long likes, Long userId, Long postId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.content = content;
        this.likes = likes;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.likes = comment.getLikes();
        this.userId = comment.getUser().getId();
        this.postId = comment.getPost().getId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }

}
