package com.example.rydeldcosta.findme;

/**
 * Created by Rydel Dcosta on 4/27/2016.
 */
public interface GetReviewCallBack {
    public abstract void done();
    public abstract void done(ReviewItem[] reviewItems,int reviews);
}
