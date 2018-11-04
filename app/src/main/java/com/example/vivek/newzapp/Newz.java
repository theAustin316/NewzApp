package com.example.vivek.newzapp;

public class Newz {

    private String mDate;
    private String mCategory;
    private String mTopic;
    private String mTrailText;
    private String mWebURL;
    private String mAuthor;

    public Newz(String date, String category, String topic, String trailText, String webURL, String author) {
        mDate = date;
        mCategory = category;
        mTopic = topic;
        mTrailText = trailText;
        mWebURL = webURL;
        mAuthor = author;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmCategory() {
        return mCategory;
    }

    public String getmTopic() {
        return mTopic;
    }

    public String getmTrailText() {
        return mTrailText;
    }

    public String getmWebURL() {
        return mWebURL;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}
