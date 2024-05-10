package ca.ubc.cs304.model;

import oracle.sql.DATE;

import java.sql.Date;

public class ReviewsModel {
    private final String username;
    private final int boardGameId;
    private final Date RDate;
    private final int score;
    private final String review;


    public ReviewsModel(String username, int boardGameId, Date RDate, int score, String review) {
        this.username = username;
        this.boardGameId = boardGameId;
        this.RDate = RDate;
        this.score = score;
        this.review = review;
    }

    public String getUsername(){
        return username;
    }

    public int getBoardGameId() {
        return boardGameId;
    }

    public Date getRDate() {
        return RDate;
    }

    public int getScore() {
        return score;
    }

    public String getReview() {
        return review;
    }
}