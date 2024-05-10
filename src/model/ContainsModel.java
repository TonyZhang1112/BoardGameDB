package ca.ubc.cs304.model;

public class ContainsModel {
    private final int wishlistId;
    private final int boardGameId;

    public ContainsModel(int wishlistId, int boardGameId) {
        this.wishlistId = wishlistId;
        this.boardGameId = boardGameId;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public int getBoardGameId() {
        return boardGameId;
    }
}
