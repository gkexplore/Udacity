package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.entity.mysql.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    //find all reviews by product id using jpql
    @Query("SELECT r FROM Review r WHERE r.product.productId = :productId")
    List<Review> findAllByProductId(Long productId);


}
