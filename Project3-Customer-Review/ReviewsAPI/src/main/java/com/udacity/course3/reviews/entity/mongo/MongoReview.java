package com.udacity.course3.reviews.entity.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

//Set collection name or else default name will be used
@Document(collection = "review")
public class MongoReview {

    @Id
    private String id;
    private int likeCount;
    private String title;

    //initialize comment list
    private List<MongoComment> commentList = new ArrayList<>();

    public MongoReview(){

    }
    public MongoReview(String id, int likeCount, String title, List<MongoComment> commentList) {
        this.id = id;
        this.likeCount = likeCount;
        this.title = title;
        this.commentList = commentList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MongoComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<MongoComment> commentList) {
        this.commentList = commentList;
    }

    public void addComment(MongoComment comment){
        commentList.add(comment);
    }

    @Override
    public String toString(){
        return "title:"+this.title+", likeCount:"+this.likeCount;
    }
}
