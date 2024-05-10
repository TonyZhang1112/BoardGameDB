package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.gui.UserSigninWindow;
import ca.ubc.cs304.model.*;
import ca.ubc.cs304.ui.LoginWindow;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Class based off the same one in the CPSC 304 Sample Project https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project
/**
 * This is the main controller class that will orchestrate everything.
 */
public class DBHub implements LoginWindowDelegate, TerminalTransactionsDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;
    private UserSigninWindow userSigninWindow = null;

    public DBHub() {
        dbHandler = new DatabaseConnectionHandler();
    }

    public void start() {
        loginWindow = new LoginWindow();
        loginWindow.showFrame(this);
    }

    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            loginWindow.dispose();
        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    public void insertBoardGame(BoardGameModel bgModel) {
        dbHandler.insertBoardGame(bgModel);
    }

    public void deleteListing(int listingID){
        dbHandler.deleteListing(listingID);
    }

    public void insertGameEventSeries(GameEventSeriesModel model) {
        dbHandler.insertGameEventSeries(model);
    }

    public int countSeriesName(String seriesName) {
        return dbHandler.countSeriesName(seriesName);
    }

    public int getEventSessionNum(String seriesName) {
        return dbHandler.getEventSessionNum(seriesName);
    }

    public int getTotalListings(){
        return dbHandler.getTotalListings();
    }
    public int getTotalPublishers(){
        return dbHandler.getTotalPublishers();
    }
    public void deleteGES(String seriesName) {
        dbHandler.deleteGES(seriesName);
    }

    public void insertListing(ListingModel listingModel){
        dbHandler.insertListing(listingModel);
    }
    public void insertTournamentGame(TournamentGameModel tournamentGameModel){
        dbHandler.insertTournamentGame(tournamentGameModel);
    }

    public void insertTeamGame(TeamGameModel teamGameModel){
        dbHandler.insertTeamGame(teamGameModel);
    }
    public void insertPublisher(PublisherModel publisherModel){
        dbHandler.insertPublisher(publisherModel);
    }

    public void insertReview(ReviewsModel reviewModel){
        dbHandler.insertReview(reviewModel);
    }

    public void insertGameEvent(GameEventModel gameEventModel) {
        dbHandler.insertGameEvent(gameEventModel);
    }

    public void insertFeatured(FeaturedModel featuredModel) {
        dbHandler.insertFeatured(featuredModel);
    }

    public void insertHosts(HostsModel hostsModel) {
        dbHandler.insertHosts(hostsModel);
    }


    @Override
    public boolean isPublisherID(int publisherID) {
        return dbHandler.isPublisherID(publisherID);
    }

    @Override
    public boolean isCreatorName(String creatorName) {
        return dbHandler.isCreatorName(creatorName);
    }
    public boolean isUserName(String userName) {
        return dbHandler.isUserName(userName);
    }
    public boolean isNotPublisherName(String publisherName){
        return dbHandler.isNotPublisherName(publisherName);
    }

    public boolean isBoardGameID(int boardGameID) {
        return dbHandler.isBoardGameID(boardGameID);
    }
    public boolean isTournamentGame(int boardGameID){
        return dbHandler.isTournamentGameID(boardGameID);
    }

    public boolean isTeamGame(int boardGameID){
        return dbHandler.isTeamGameID(boardGameID);
    }

    public void showPubGX(int x) {
        dbHandler.showPubGx(x);
    }

    public void globalP(String tableName) {
        dbHandler.globalP(tableName);
    }

    public String[] getTableNames() {
        return dbHandler.getTableNames();
    }

    public void handleShowFeat() {
        FeaturedModel[] models = dbHandler.getFeatInfo();

        for (int i = 0; i < models.length; i++) {
            FeaturedModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getBoardGameId());
            System.out.printf("%-20.20s", model.getSessionNum());
            System.out.printf("%-20.20s", model.getSeriesName());

            System.out.println();
        }

    }

    public void handleShowHosts() {
        HostsModel[] models = dbHandler.getHostsInfo();

        for (int i = 0; i < models.length; i++) {
            HostsModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getUsername());
            System.out.printf("%-20.20s", model.getSessionNum());
            System.out.printf("%-20.20s", model.getSeriesName());

            System.out.println();
        }

    }


    public void averageScoreBG(int publisherID){
        ReviewsModel[] models = dbHandler.averageScoreBG(publisherID);
        for (int i = 0; i < models.length; i++) {
            ReviewsModel model = models[i];
            if (model.getBoardGameId() == 0){
                System.out.println("The Publisher ID you input is either invalid or does not have any ratings yet");
                return;
            }
            if(i == 0){
                System.out.printf("%-20.20s", "PublisherID");
                System.out.printf("%-20.20s", "Average Score");
                System.out.println();
            }
            // simplified output formatting; truncation may occur
            System.out.printf("%-20.20s", model.getBoardGameId());
            System.out.printf("%-20.20s", model.getScore() + "/10");


            System.out.println();
        }
    }

    public void handleShowGE() {
        GameEventModel[] models = dbHandler.getGEInfo();

        for (int i = 0; i < models.length; i++) {
            GameEventModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getSessionNum());
            System.out.printf("%-20.20s", model.getSeriesName());
            System.out.printf("%-20.20s", model.getLocation());
            System.out.printf("%-20.20s", model.getDate());

            System.out.println();
        }

    }

    public void handleShowGES() {
        GameEventSeriesModel[] models = dbHandler.getGESInfo();

        for (int i = 0; i < models.length; i++) {
            GameEventSeriesModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getSeriesName());
            System.out.printf("%-20.20s", model.getLocation());
            System.out.printf("%-20.20s", model.getStartDate());
            System.out.printf("%-20.20s", model.getEndDate());
            System.out.printf("%-10.10s", model.getCreatorName());

            System.out.println();
        }

    }

    public void handleShowGES2() {
        GameEventSeriesModel2[] models = dbHandler.getGES2Info();

        for (int i = 0; i < models.length; i++) {
            GameEventSeriesModel2 model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getStartDate());
            System.out.printf("%-20.20s", model.getEndDate());
            System.out.printf("%-20.20s", model.getDuration());
            System.out.println();
        }

    }

    public void updatePublisher(int publisherId, String name, String website) {
        dbHandler.updatePublisher(publisherId, name, website);
    }

    public void showBG() {
        BoardGameModel[] models = dbHandler.getBGInfo();

        for (int i = 0; i < models.length; i++) {
            BoardGameModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getId());
            System.out.printf("%-20.20s", model.getTitle());

            if (model.getGenre() == null) {
                System.out.printf("%-20.20s", " ");
            } else {
                System.out.printf("%-20.20s", model.getGenre());
            }

            if (model.getDescription() == null) {
                System.out.printf("%-20.20s", " ");
            } else {
                System.out.printf("%-20.20s", model.getDescription());
            }

            System.out.printf("%-20.20s", model.getPid());

            System.out.println();
        }
    }

    public HostsPerUser[] getNumHostsPerUser() {
        return dbHandler.getNumHostsForUser();
    }

    public String[] findUsersWishingGames(String[] games) {
        return dbHandler.findUsersWishingGames(games);
    }

    public void showPublisher() {
        PublisherModel[] models = dbHandler.getPublisherInfo();

        for (int i = 0; i < models.length; i++) {
            PublisherModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getId());
            System.out.printf("%-20.20s", model.getpublisherName());
            if (model.getWebsite() == null) {
                System.out.printf("%-20.20s", " ");
            } else {
                System.out.printf("%-20.20s", model.getWebsite());
            }

            System.out.println();
        }
    }
    public void showListing() {
        ListingModel[] models = dbHandler.getListingInfo();

        for (int i = 0; i < models.length; i++) {
            ListingModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getLid());
            System.out.printf("%-20.20s", model.getDateListed());
            System.out.printf("%-20.20s", model.getPrice());
            if (model.getDescription() == null) {
                System.out.printf("%-20.20s", "Null ");
            } else {
                System.out.printf("%-20.20s", model.getDescription());
            }
            System.out.printf("%-20.20s", model.getBoardGameID());
            System.out.printf("%-20.20s", model.getUsername());

            System.out.println();
        }
    }

    public void selectBGP(String publisher){
        BGPModel[] models = dbHandler.selectBGP(publisher);
        for (int i = 0; i < models.length; i++) {
            BGPModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-30.30s", model.getpublisherName());
            System.out.printf("%-30.30s", model.getTitle());
            System.out.printf("%-30.30s", model.getGenre());


            System.out.println();
        }
    }

    public void showReview() {
        ReviewsModel[] models = dbHandler.getReviewInfo();

        for (int i = 0; i < models.length; i++) {
            ReviewsModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getUsername());
            System.out.printf("%-20.20s", model.getBoardGameId());
            System.out.printf("%-20.20s", model.getRDate());
            System.out.printf("%-20.20s", model.getScore());
            if (model.getReview() == null) {
                System.out.printf("%-20.20s", "Null ");
            } else {
                System.out.printf("%-20.20s", model.getReview());
            }
            System.out.println();
        }
    }

    public void showTournamentGames() {
        TournamentGameModel[] models = dbHandler.getTournamentInfo();

        for (int i = 0; i < models.length; i++) {
            TournamentGameModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.50s", model.getId());
            if (model.getRules() == null) {
                System.out.printf("%-50.50s", "Null ");
            } else {
                System.out.printf("%-50.50s", model.getRules());
            }
            System.out.printf("%-50.50s", model.getMinParticipants());
            System.out.println();
        }
    }

    public void showTeamGames(){
        TeamGameModel[] models = dbHandler.getTeamInfo();

        for (int i = 0; i < models.length; i++) {
            TeamGameModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.50s", model.getId());
            System.out.printf("%-50.50s", model.getMinTeamSize());
            System.out.println();
        }
    }

    public void filterListing(List<Object> operators){
        ListingModel[] models = dbHandler.filterListing(operators);
        for (int i = 0; i < models.length; i++){
            ListingModel model = models[i];

            System.out.printf("%-10.10s", model.getLid());
            System.out.printf("%-20.20s", model.getDateListed());
            System.out.printf("%-10.10s", model.getPrice());
            if (model.getDescription() == null) {
                System.out.printf("%-20.20s", " ");
            } else {
                System.out.printf("%-20.20s", model.getDescription());
            }
            System.out.printf("%-10.20s", model.getBoardGameID());
            System.out.printf("%-10.20s", model.getUsername());

            System.out.println();
        }
    }

    public void filterListing2(Map<String, Object> attributeMap, ArrayList<String> cState, String priceOperator){
        ListingModel[] models = dbHandler.filterListing2(attributeMap,cState,priceOperator);
        for (int i = 0; i < models.length; i++){
            ListingModel model = models[i];

            System.out.printf("%-10.10s", model.getLid());
            System.out.printf("%-20.20s", model.getDateListed());
            System.out.printf("%-10.10s", model.getPrice());
            if (model.getDescription() == null) {
                System.out.printf("%-20.20s", " ");
            } else {
                System.out.printf("%-20.20s", model.getDescription());
            }
            System.out.printf("%-10.20s", model.getBoardGameID());
            System.out.printf("%-10.20s", model.getUsername());

            System.out.println();
        }
    }

    public void insertWishlist(WishListModel wishlist) throws SQLException {
        dbHandler.insertWishlist(wishlist);
    }

    public int insertUser(UserModel user) { return dbHandler.insertUser(user); }

    public boolean insertContains(ContainsModel item) {
        return dbHandler.insertContains(item);
    }

    public boolean insertTransaction(TransactionModel transaction) {
        return dbHandler.insertTransaction(transaction);
    }

    public boolean checkUserCredentials(String username, String password) {
        return dbHandler.checkUserCredentials(username, password);
    }

    public DefaultTableModel showUser() {
        return dbHandler.showUser();
    }

    public DatabaseConnectionHandler getDbHandler(){return dbHandler;}

    /**
     * TerminalTransactionsDelegate Implementation
     * <p>
     * The TerminalTransaction instance tells us that it is done with what it's
     * doing, so we are cleaning up the connection since it's no longer needed.
     */
    public void terminalTransactionsFinished() {
        dbHandler.close();
        dbHandler = null;

        System.exit(0);
    }

    public void databaseSetup() {
        dbHandler.databaseSetup("src/sql/scripts/databaseSetup.sql");
    }

    /**
     * Main method called at launch time
     */
    public static void main(String args[]) {
        DBHub dbHub = new DBHub();
        dbHub.start();
    }
}
