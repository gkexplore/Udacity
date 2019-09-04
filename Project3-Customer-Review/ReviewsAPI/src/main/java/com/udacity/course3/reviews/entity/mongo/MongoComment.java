package com.udacity.course3.reviews.entity.mongo;

public class MongoComment {

    private String text;

    public MongoComment() {
    }

    public MongoComment(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



}
