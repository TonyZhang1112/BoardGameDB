package ca.ubc.cs304.delegates;
import ca.ubc.cs304.model.*;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Interface based off the same one from the CPSC 304 Sample Project https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project
/**
 * This interface uses the delegation design pattern where instead of having
 * the TerminalTransactions class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the
 * controller class (in this case Bank).
 * <p>
 * TerminalTransactions calls the methods that we have listed below but
 * Bank is the actual class that will implement the methods.
 */
public interface TerminalTransactionsDelegate {
    void databaseSetup();

    //public void deleteBranch(int branchId);

    void deleteGES(String seriesName);

    void insertBoardGame(BoardGameModel boardGameModel);

    void showBG();

    void insertWishlist(WishListModel wishlist) throws SQLException;

    int insertUser(UserModel user);

    boolean insertContains(ContainsModel item);

    boolean insertTransaction(TransactionModel transaction);

    boolean checkUserCredentials(String username, String password);

    DefaultTableModel showUser();

    HostsPerUser[] getNumHostsPerUser();


    String[] findUsersWishingGames(String[] games);

    void showPubGX(int x);

    void filterListing(List<Object> operators);

    void updatePublisher(int publisherId, String name, String website);

    void showPublisher();

    void terminalTransactionsFinished();

    void globalP(String tableName);

    String[] getTableNames();

    void handleShowFeat();

    void handleShowHosts();

    void showListing();

    void handleShowGE();

    void handleShowGES();

    void handleShowGES2();

    void insertGameEventSeries(GameEventSeriesModel model);

    void deleteListing(int listingID);

    int countSeriesName(String seriesName);

    int getEventSessionNum(String seriesName);

    void insertGameEvent(GameEventModel model);

    boolean isCreatorName(String creatorName);

    boolean isBoardGameID(int boardGameID);

    void insertFeatured(FeaturedModel model);
    
    void insertHosts(HostsModel model);
    
    void insertListing(ListingModel listingModel);

    int getTotalListings();

    boolean isUserName(String userName);

    int getTotalPublishers();

    boolean isNotPublisherName(String publisherName);

    void insertPublisher(PublisherModel publisherModel);

    void insertReview(ReviewsModel reviewModel);

    void showReview();

    void showTournamentGames();

    boolean isTournamentGame(int boardGameID);

    void showTeamGames();

    boolean isTeamGame(int boardGameID);

    void selectBGP(String publisher);

    void averageScoreBG(int publisherID);

    void insertTeamGame(TeamGameModel model);

    void insertTournamentGame(TournamentGameModel model);

    boolean isPublisherID(int publisherID);

    void filterListing2(Map<String, Object> attributeMap, ArrayList<String> cState, String priceOperator);
}
