package com.udacity.course3.reviews;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReviewsApplicationTests {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ReviewRepository reviewsRepository;

	@Autowired
	CommentRepository commentRepository;


	/************Test for Product Table***************/

	//Verify save and findById work fine for Product
	@Test
	public void givenProduct_whenSave_thenGetOk() {
		productRepository.save(getProducts().get(0));
		Product product1 = productRepository.findById(1L).get();
		assertEquals("sofa", product1.getProductName());
	}

	//Verify retrieving all the products from product table works fine
	@Test
	public void givenProducts_whenRetrieve_thenGetOk(){

		//persist two products
		productRepository.save(getProducts().get(0));
		productRepository.save(getProducts().get(1));

		//retrieve both the products
		List<Product> productList =  productRepository.findAll();

		//verify the findAll method works fine
		assertEquals(productList.size(), 2);

	}


	//*************************Test for Review Table************************//*

	//verify save review  and findbyid work fine
	@Test
	public void giveReview_thenSave_thenGetOK(){

		productRepository.save(getProducts().get(0));
		reviewsRepository.save(getReviews().get(0));
		reviewsRepository.save(getReviews().get(1));

		//verify save works fine
		assertTrue(reviewsRepository.findAll().size()==2);
		assertTrue(reviewsRepository.findById(getReviews().get(0).getReviewId()).get().getTitle().equals("Good"));

	}

	//verify findreviewsbyproductid works fine
	@Test
	public void givenReview_thenGetReviewsByProductId_theGetOK() throws InterruptedException {
		productRepository.save(getProducts().get(0));
		reviewsRepository.save(getReviews().get(0));
		reviewsRepository.save(getReviews().get(1));

		//verify findAllReviewByProductId works fine
		assertTrue(reviewsRepository.findAll().size()==2);
		assertTrue(reviewsRepository.findAllByProductId(getProducts().get(0).getProductId()).get(0).getTitle().equals("Good"));

	}


	//***************************Test for Comment Table********************//*

	//verify save and findbyid works fine
	@Test
	public void giveComment_thenSave_thenGetOK(){
		productRepository.save(getProducts().get(0));
		reviewsRepository.save(getReviews().get(0));
		reviewsRepository.save(getReviews().get(1));
		commentRepository.save(getComments().get(0));
		commentRepository.save(getComments().get(1));

		assertTrue(commentRepository.findAll().size()==2);
		assertTrue(commentRepository.findById(getComments().get(0).getCommentId()).get().getText().equals("comment1"));

	}

	//verify findAllByReviewId works fine
	@Test
	public void givenReviewComments_whenRetrieveByreviewId_thenGetOk(){
		productRepository.save(getProducts().get(0));
		reviewsRepository.save(getReviews().get(0));
		reviewsRepository.save(getReviews().get(1));
		commentRepository.save(getComments().get(0));
		commentRepository.save(getComments().get(1));

		assertTrue(commentRepository.findAllByReviewId(getReviews().get(0).getReviewId()).size()==2);

	}


	public List<Product> getProducts(){

		Product product = new Product();
		product.setProductId(1L);
		product.setProductName("sofa");

		Product product2 = new Product();
		product2.setProductId(2L);
		product2.setProductName("knife");

		return Arrays.asList(product, product2);
	}

	public List<Review> getReviews(){
		Review review = new Review();
		review.setReviewId(1L);
		review.setTitle("Good");
		review.setProduct(getProducts().get(0));

		Review review2 = new Review();
		review2.setReviewId(2L);
		review2.setTitle("Bad");
		review2.setProduct(getProducts().get(0));

		return Arrays.asList(review, review2);

	}

	public List<Comment> getComments(){
		Comment comment1 = new Comment();
		comment1.setCommentId(1L);
		comment1.setText("comment1");
		comment1.setReview(getReviews().get(0));

		Comment comment2 = new Comment();
		comment2.setCommentId(2L);
		comment2.setText("comment2");
		comment2.setReview(getReviews().get(0));

		return Arrays.asList(comment1, comment2);
	}



}