package com.udacity.course3.reviews.entiry;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "text")
    private String text;

    @Column(name = "created_time")
    private Timestamp createdTime;


    @ManyToOne
    @JoinColumn(name="review_id", nullable=false)
    private Review review;

    public Long getCommentId() {
        return commentId;
    }

    public Comment() {
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public void setReview(Review review) {
        this.review = review;
    }

}
