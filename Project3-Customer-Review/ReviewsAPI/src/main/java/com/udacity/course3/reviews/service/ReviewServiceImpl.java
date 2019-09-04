package com.udacity.course3.reviews.service;

import com.udacity.course3.reviews.entity.mongo.MongoComment;
import com.udacity.course3.reviews.entity.mongo.MongoReview;
import com.udacity.course3.reviews.entity.mysql.Comment;
import com.udacity.course3.reviews.entity.mysql.Product;
import com.udacity.course3.reviews.entity.mysql.Review;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.MongoReviewRepository;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MongoReviewRepository mongoReviewRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public boolean saveReview(Long productId, Review review) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        //check if product exist
        if(optionalProduct.isPresent()) {

            review.setProduct(optionalProduct.get());

            //save review to mysql and copy it to mongo review document
            MongoReview mongoReview = copyReview(reviewRepository.save(review));
            mongoReviewRepository.save(mongoReview);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean saveComment(Long reviewId, Comment comment) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isPresent()) {

            //save comment on mysql db
            comment.setReview(review.get());
            commentRepository.save(comment);

            //find review by id on mongo review collection and save the comment
            Optional<MongoReview> mongoReviewOptional = mongoReviewRepository.findById(reviewId+"");
            if(mongoReviewOptional.isPresent()){
               MongoReview mongoReview =  mongoReviewOptional.get();
                System.out.println(mongoReview.getId());

               /* List<MongoComment> mongoCommentList = copyComment(comment);
                System.out.println(mongoCommentList.size());
                for (MongoComment mongoComment: mongoCommentList){
                    mongoReview.addComment(mongoComment);
                }*/
               //copy comments to review
              // mongoReview.setCommentList(mongoCommentList);

               //save review to mongo

              /*  MongoComment mongoComment1 = new MongoComment();
                mongoComment1.setText("dsfsfsf");

                MongoComment mongoComment2  = new MongoComment();
                mongoComment2.setText("ghgf");*/

                mongoReview.getCommentList().add(copyComment(comment));
               mongoReviewRepository.save(mongoReview);
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<MongoReview> findAllReviewByProductId(Long productId) {
        List<Review> reviewList = reviewRepository.findAllByProductId(productId);
        return getReviewsFromMongo(reviewList);
    }

    @Override
    public List<MongoComment> findAllCommentsByReviewId(Long reviewId) {
        return mongoReviewRepository.findById(reviewId+"").get().getCommentList();
    }


    public MongoReview copyReview(Review review){
        System.out.println(review.getTitle());
        MongoReview mongoReview = new MongoReview();
        mongoReview.setId(review.getReviewId()+"");
        mongoReview.setLikeCount(review.getLikeCount());
        mongoReview.setTitle(review.getTitle());
        return mongoReview;
    }

    public MongoComment copyComment(Comment comment){
        MongoComment mongoComment = new MongoComment();
        System.out.println("inside copy comment"+ comment.getText());
        mongoComment.setText(comment.getText());
        return mongoComment;
    }

    public List<MongoReview> getReviewsFromMongo(List<Review> reviewList){
        List<MongoReview> mongoReviewList = new ArrayList<>();
        for (Review review: reviewList){
            Optional<MongoReview> reviewOptional = mongoReviewRepository.findById(review.getReviewId()+"");
            if(reviewOptional.isPresent()){
                mongoReviewList.add(reviewOptional.get());
            }
        }
        return mongoReviewList;
    }
}
