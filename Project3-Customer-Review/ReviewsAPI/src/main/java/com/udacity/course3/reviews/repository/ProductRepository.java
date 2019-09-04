package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.entity.mysql.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
