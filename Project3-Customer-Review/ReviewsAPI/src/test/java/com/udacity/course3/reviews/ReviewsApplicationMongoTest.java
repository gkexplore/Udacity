package com.udacity.course3.reviews;

import com.udacity.course3.reviews.entity.mongo.MongoComment;
import com.udacity.course3.reviews.entity.mongo.MongoReview;
import com.udacity.course3.reviews.repository.MongoReviewRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration= {EmbeddedMongoAutoConfiguration.class})
@ActiveProfiles("test")
@Import(MongoConfiguration.class)
public class ReviewsApplicationMongoTest {

    @Autowired
    MongoReviewRepository mongoReviewRepository;

    @Before
    public void setUp(){

        //setup a review with comments
        MongoReview mongoReview = new MongoReview();
        mongoReview.setId("1");
        mongoReview.setTitle("test");
        mongoReview.setLikeCount(2);
        mongoReview.getCommentList().add(new MongoComment("comment1"));
        mongoReview.getCommentList().add(new MongoComment("comment2"));
        mongoReviewRepository.save(mongoReview);
    }

    @Test
    public void testFindReviewById(){
        //test retrieve by review id works fine
        MongoReview mongoReview = mongoReviewRepository.findById("1").get();

        //assert all the fields on the Review Document
        assertEquals(mongoReview.getTitle(), "test");
        assertEquals(mongoReview.getLikeCount(), 2);
        assertEquals(mongoReview.getCommentList().get(0).getText(), "comment1");
        assertEquals(mongoReview.getCommentList().get(1).getText(), "comment2");
    }


}
