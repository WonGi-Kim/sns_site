package com.sparta.easyspring.comment.entity;

import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.comment.dto.CommentRequestDto;
import com.sparta.easyspring.commentlike.entity.CommentLike;
import com.sparta.easyspring.post.entity.Post;
import com.sparta.easyspring.timestamp.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
public class Comment extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long likes;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();

    public Comment(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.likes = 0L;
    }

    public void editComment(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void editCommentByAdmin(CommentRequestDto requestDto) {
        this.content = requestDto.getContent() + " (Admin에 의해 수정되었음)";
    }

    public void updateCommentLike(Long likes) {
        this.likes = likes;
    }
}
