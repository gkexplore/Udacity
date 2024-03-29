package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //find all reviews by review id using jpql
    @Query("SELECT c FROM Comment c WHERE c.review.reviewId = :reviewId")
    List<Comment> findAllByReviewId(Long reviewId);

}
