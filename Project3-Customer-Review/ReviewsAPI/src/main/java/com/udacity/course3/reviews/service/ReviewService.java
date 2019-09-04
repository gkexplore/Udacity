package com.udacity.course3.reviews.service;

import com.udacity.course3.reviews.entity.mongo.MongoComment;
import com.udacity.course3.reviews.entity.mongo.MongoReview;
import com.udacity.course3.reviews.entity.mysql.Comment;
import com.udacity.course3.reviews.entity.mysql.Review;

import java.util.List;

public interface ReviewService {

    boolean saveReview(Long productId, Review review);

    boolean saveComment(Long reviewId, Comment comment);

    List<MongoReview> findAllReviewByProductId(Long productId);

    List<MongoComment> findAllCommentsByReviewId(Long reviewId);

}
