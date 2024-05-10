package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.*;

import ca.ubc.cs304.gui.UserSigninWindow;
import ca.ubc.cs304.model.TeamGameModel;
import ca.ubc.cs304.model.TournamentGameModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

// Class based off the same one in the CPSC 304 Sample Project
// https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project
/**
 * The class is only responsible for handling terminal text inputs.
 */
public class TerminalTransactions {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private static final int INVALID_INPUT = Integer.MIN_VALUE;
    private static final int EMPTY_INPUT = 0;

    private UserSigninWindow userSigninWindow = null;
    private BufferedReader bufferedReader = null;
    private TerminalTransactionsDelegate delegate = null;

    public TerminalTransactions() {
    }

    /**
     * Sets up the database to have a branch table with two tuples so we can insert/update/delete from it.
     * Refer to the databaseSetup.sql file to determine what tuples are going to be in the table.
     */
    public void setupDatabase(TerminalTransactionsDelegate delegate) {
        this.delegate = delegate;

        bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        delegate.databaseSetup();


    }

    /**
     * Displays simple text interface
     */
    public void showMainMenu(TerminalTransactionsDelegate delegate) {

        this.delegate = delegate;

        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int choice = INVALID_INPUT;

        while (choice != 50) {
            System.out.println();
            System.out.println("Please choose one of the above 50 options: ");

            System.out.println("1. Insert Board Game"); // DONE: BGTab
            System.out.println("2. Delete branch"); // ignore
            System.out.println("3. Update branch name"); // ignore
            System.out.println("4. Show Board Game"); // DONE: BGTab
            System.out.println("6. Insert Wishlist"); // TBI userTab
            System.out.println("7: Insert User"); // TBI userTab
            System.out.println("8: Show Num of Hosted GameEvents per user"); // DONE
            System.out.println("9: Find Users Wishing X Games"); // DONE: BGTab
            System.out.println("10. Show Publisher"); // DONE: PubTab
            System.out.println("11. Update Publisher"); // DONE: PubTab
            System.out.println("12. Filter Listing by Price"); // TBI ListingTab
            System.out.println("13. Show Listing"); // DONE: ListingTab
            System.out.println("14. Purchase From Listing"); // TBI ListingTab
            System.out.println("15. Delete Game Event Series"); // TBI GETab
            System.out.println("16. Show Publishers w/ titles greater than X"); // DONE: PubTab
            System.out.println("17. Global Projection"); // indiv tab
            System.out.println("18. Insert GameEvent Series"); // TBI GES Tab
            System.out.println("19. Insert Game Event"); // TBI GE Tab
            System.out.println("20. Show Featured"); // TBI GE/GES
            System.out.println("21. Show Hosts");// TI GE/GES or user?
            System.out.println("22. Show Game Event"); // TBI GETab
            System.out.println("23. Show Game Event Series 1"); // TBI GESTab
            System.out.println("24. Show Game Event Series 2"); // TBI GESTab
            System.out.println("31. Insert into Wishlist"); // TBI Wishlist / user
            System.out.println("32. Insert Transaction"); // TBI Transaction (Listing)
            System.out.println("33. Check User Login Credentials"); // ignore
            System.out.println("40. Insert Listings"); // TBI Listing
            System.out.println("41. Insert Publishers"); // TBI PublisherT
            System.out.println("42. Insert Reviews"); // TBI user?
            System.out.println("43. Show Reviews"); // DONE: BGTab
            System.out.println("44. Show TournamentGames"); // DONE: BGTab
            System.out.println("45. Insert TournamentGames"); // DONE: BGTab
            System.out.println("46. Show Team Game"); //DONE: BGTab
            System.out.println("47. Insert Team Game"); // DONE: BGTab
            System.out.println("48. Select Publisher and BoardGame"); // DONE: PubTab
            System.out.println("49. Get average board game score for a publisher"); // Done: PubTab

            System.out.println("50. Quit");

            choice = readInteger(false);

            System.out.println(" ");

            if (choice != INVALID_INPUT) {
                switch (choice) {
                    case 1:
                        handleInsertOption();
                        break;
                    case 2:
                        //handleDeleteOption();
                        break;
                    case 3:
                        //handleUpdateOption();
                        break;
                    case 4:
                        //delegate.showBranch();
                        delegate.showBG();
                        break;
                    case 10:
                        delegate.showPublisher();
                        break;
                    case 11:
                        handleUpdatePublisher();
                        break;
                    case 12:
                        handleFilterListing();
                        break;
                    case 13:
                        delegate.showListing();
                        break;
                    case 14:
                        handleDeleteListing();
                        break;
                    case 40:
                        handleInsertListing();
                        break;
                    case 41:
                        handleInsertPublisher();
                        break;
                    case 42:
                        handleInsertReview();
                        break;
                    case 43:
                        delegate.showReview();
                        break;
                    case 44:
                        delegate.showTournamentGames();
                        break;
                    case 45:
                        handleInsertTournamentGames();
                        break;
                    case 46:
                        delegate.showTeamGames();
                        break;
                    case 47:
                        handleInsertTeamGames();
                        break;
                    case 48:
                        handleSelectBGP();
                        break;
                    case 49:
                        handleAverageScoreBG();
                        break;
                    case 25:
                        handleFilterListing2();
                        break;
                    case 50:
                        handleQuitOption();
                        break;
                    case 6:
                        handleInsertWishlist();
                        break;
                    case 7:
                        handleInsertUser();
                        break;
                    case 8:
                        handleGetNumHostsPerUser();
                        break;
                    case 9:
                        handleFindUsersWishingGames();
                        break;
                    case 15:
                        handleDeleteGES();
                        break;
                    case 16:
                        handlePubGX();
                        break;
                    case 17:
                        handleGlobalP();
                        break;
                    case 18:
                        handleInsertGES();
                        break;
                    case 19:
                        handleInsertGE();
                        break;
                    case 20:
                        delegate.handleShowFeat();
                        break;
                    case 21:
                        delegate.handleShowHosts();
                        break;
                    case 22:
                        delegate.handleShowGE();
                        break;
                    case 23:
                        delegate.handleShowGES();
                        break;
                    case 24:
                        delegate.handleShowGES2();
                        break;
                    case 31:
                        handleInsertContains();
                        break;
                    case 32:
                        handleInsertTransaction();
                        break;
                    case 33:
                        handleUserLogin();
                        break;
                    default:
                        System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
                        break;
                }
            }
        }
    }


    private void handlePubGX() {
        int x = INVALID_INPUT;
        while (x == INVALID_INPUT) {
            System.out.print("Please enter the min number of games for a publisher");
            x = readInteger(false);
        }

        delegate.showPubGX(x);

    }

    private void handleGlobalP() {
        String tableName = "";

        //TODO: When calling this method from UI, update it so it takes input string of tableName from the tab
        Set<String> tableNames = new HashSet<>();
        tableNames.add("BOARDGAME");
        tableNames.add("CONTAINS");
        tableNames.add("FEATURED");
        tableNames.add("GAMEEVENT");
        tableNames.add("GAMEEVENTSERIES1");
        tableNames.add("GAMEEVENTSERIES2");
        tableNames.add("HOSTS");
        tableNames.add("LISTING");
        tableNames.add("PUBLISHER");
        tableNames.add("REVIEWS");
        tableNames.add("TEAMGAME");
        tableNames.add("TOURNAMENTGAME");
        tableNames.add("TRANSACTION");
        tableNames.add("USER1");
        tableNames.add("USER2");
        tableNames.add("WISHLIST");

        System.out.println(tableNames);
        int i = 3;

        while (i > 0) {
            System.out.println("Please enter in the name of the table to project or exit to return");
            tableName = readLine();

            if (tableName.equalsIgnoreCase("exit")) {
                break;
            } else if (tableNames.contains(tableName.toUpperCase())) {
                delegate.globalP(tableName);
                break;
            } else if (!(tableNames.contains(tableName.toUpperCase()))){
                i--;
                System.out.println("Table name does not exist, please try again! You have " + i + " tries");
            }
        }
    }



//    private void handleDeleteOption() {
//        int branchId = INVALID_INPUT;
//        while (branchId == INVALID_INPUT) {
//            System.out.print("Please enter the branch ID you wish to delete: ");
//            branchId = readInteger(false);
//            if (branchId != INVALID_INPUT) {
//                delegate.deleteBranch(branchId);
//            }
//        }
//    }

    private void handleDeleteGES() {
        String seriesName = "";
        //TODO: modify above for sanitization? - like if string contains ; CREATE TABLE etc?
        while (seriesName.equals("")) {
            System.out.print("Please enter the GES name you wish to delete: ");
            seriesName = readLine();
            if (!seriesName.equals("")) {
                delegate.deleteGES(seriesName);
            }
        }
    }

    private void handleDeleteListing() {
        int listingID = INVALID_INPUT;
        while (listingID == INVALID_INPUT) {
            System.out.print("Please enter the listing ID for the game you want to purchase: ");
            listingID = readInteger(false);
        }
        delegate.deleteListing(listingID);

    }

//    private void handleInsertOption() {
//        int id = INVALID_INPUT;
//        while (id == INVALID_INPUT) {
//            System.out.print("Please enter the branch ID you wish to insert: ");
//            id = readInteger(false);
//        }
//
//        String name = null;
//        while (name == null || name.length() <= 0) {
//            System.out.print("Please enter the branch name you wish to insert: ");
//            name = readLine().trim();
//        }
//
//        // branch address is allowed to be null so we don't need to repeatedly ask for the address
//        System.out.print("Please enter the branch address you wish to insert: ");
//        String address = readLine().trim();
//        if (address.length() == 0) {
//            address = null;
//        }
//
//        String city = null;
//        while (city == null || city.length() <= 0) {
//            System.out.print("Please enter the branch city you wish to insert: ");
//            city = readLine().trim();
//        }
//
//        int phoneNumber = INVALID_INPUT;
//        while (phoneNumber == INVALID_INPUT) {
//            System.out.print("Please enter the branch phone number you wish to insert: ");
//            phoneNumber = readInteger(true);
//        }
//
//        BranchModel model = new BranchModel(address,
//                city,
//                id,
//                name,
//                phoneNumber);
//        delegate.insertBranch(model);
//    }

    private void handleInsertOption() {

        boolean isTeamGame = false;
        boolean isTournamentGame = false;
        int minParticipants = INVALID_INPUT;
        String tournamentRules = null;

        int id = INVALID_INPUT;
        while (id == INVALID_INPUT || delegate.isBoardGameID(id)) {
            System.out.print("Please enter the Board Game ID you wish to insert: ");
            id = readInteger(false);
            if(delegate.isBoardGameID(id)) {
                System.out.print("Board Game ID " + id + " already exists!");
            }
        }

        String title = null;
        while (title == null || title.length() <= 0) {
            System.out.print("Please enter the board game title you wish to insert: ");
            title = readLine().trim();
        }

        // branch address is allowed to be null so we don't need to repeatedly ask for the address
        System.out.print("Please enter the board game genre you wish to insert: ");
        String genre = readLine().trim();
        if (genre.length() == 0) {
            genre = null;
        }

        String description = null;
        while (description == null || description.length() <= 0) {
            System.out.print("Please enter the board game description you wish to insert: ");
            description = readLine().trim();
        }

        int publisherID = INVALID_INPUT;
        while (publisherID == INVALID_INPUT || !delegate.isPublisherID(publisherID)) {
            System.out.print("Please enter the publisher ID for the board game you wish to insert: ");
            publisherID = readInteger(false);
            if(!delegate.isPublisherID(publisherID)) {
                System.out.print("The Publisher ID does not exist!");
            }
        }

        int minTeamSize = INVALID_INPUT;
        while (minTeamSize == INVALID_INPUT || !isTeamGame) {
            System.out.print("Is this a team game?: [Enter Y / N]");
            String response = readLine();
            if (response.equals("Y")) {
                System.out.print("Please enter the min team size:");
                minTeamSize = readInteger(false);
                isTeamGame = true;
            } else if (response.equals("N")) {
                break;
            }
        }


        while(minParticipants == INVALID_INPUT || !isTournamentGame) {
            System.out.print("Is this a tournament game?: [Enter Y / N]");
            String response = readLine();
            if (response.equals("Y")){
                System.out.print("Please enter the min participants:");
                minParticipants = readInteger(false);
                System.out.print("Please enter the tournament rules:");
                tournamentRules = readLine();
                isTournamentGame = true;
            } else if (response.equals("N")) {
                break;
            }
        }

        BoardGameModel model = new BoardGameModel(id,
                title,
                genre,
                description,
                publisherID);

        delegate.insertBoardGame(model);

        if (isTeamGame) {
            TeamGameModel model2 = new TeamGameModel(id,
                    minTeamSize
            );
            delegate.insertTeamGame(model2);
        }

        if (isTournamentGame) {
            TournamentGameModel model3 = new TournamentGameModel(id,
                    tournamentRules,
                    minParticipants);
            delegate.insertTournamentGame(model3);
        }

    }

    private void handleInsertGES() {

        String seriesName = null;
        while (seriesName == null || seriesName.length() <= 0) {
            System.out.print("Please enter the Game Event Series Name you wish to insert: ");
            seriesName = readLine().trim();
        }

        String location = null;
        while (location == null || location.length() <= 0) {
            System.out.print("Please enter the series location you wish to insert: ");
            location = readLine().trim();
        }

        System.out.print("Please enter the start date of your series (YYYY-MM-DD): ");

        Date startDate = getDate();

        System.out.print("Please enter the end date of your series (YYYY-MM-DD): ");
        Date endDate = getDate();

        String userName = null;
        while (userName == null || userName.length() <= 0) {
            System.out.print("Please enter the username of the series creator: ");
            userName = readLine().trim();
        }

        GameEventSeriesModel model = new GameEventSeriesModel(seriesName,
                location,
                startDate,
                endDate,
                userName);

        System.out.println(startDate);
        System.out.println(endDate);
        delegate.insertGameEventSeries(model);

    }

    private void handleInsertGE(){

        String seriesName = null;
        while (seriesName == null || seriesName.length() <= 0) {
            System.out.print("Please enter the series name of the event you wish to insert: ");
            seriesName = readLine().trim();
        }

        int x = delegate.countSeriesName(seriesName);

        if (x == 0) {
            System.out.println("The series name does not exist!");
            return;
        }

        String creatorName = null;
        while (creatorName == null || creatorName.length() <= 0) {
            System.out.print("Please enter the host name of the event you wish to insert: ");
            creatorName = readLine().trim();
        }

        if(!delegate.isCreatorName(creatorName)) {
            System.out.print("The entered user name does not exist!");
            return;
        }

        int boadGameID = INVALID_INPUT;
        while (boadGameID == INVALID_INPUT) {
            System.out.print("Please enter the Board Game ID you wish to feature: ");
            boadGameID = readInteger(false);
        }

        if(!delegate.isBoardGameID(boadGameID)) {
            System.out.print("The entered board game id does not exist!");
            return;
        }

        int sessionNum = delegate.getEventSessionNum(seriesName) + 1;

        String location = null;
        while (location == null || location.length() <= 0) {
            System.out.print("Please enter the series location you wish to insert: ");
            location = readLine().trim();
        }

        System.out.print("Please enter the date for your event (YYYY-MM-DD): ");

        Date eventDate = getDate();

        GameEventModel model = new GameEventModel(sessionNum,
                seriesName,
                location,
                eventDate);

        FeaturedModel model2 = new FeaturedModel(boadGameID,
                sessionNum,
                seriesName);

        HostsModel model3 = new HostsModel(creatorName,
                sessionNum,
                seriesName);

        delegate.insertGameEvent(model);
        delegate.insertFeatured(model2);
        delegate.insertHosts(model3);
    }


    private Date getDate() {

        int year = INVALID_INPUT;
        while (year == INVALID_INPUT || year < 1000 || year > 9999 ) {
            System.out.print("Please enter the year (format YYYY): ");
            year = readInteger(false);
            if (year < 1000 || year > 9999) {
                System.out.println("Please enter a valid year");
            }
        }

        int month = INVALID_INPUT;
        while (month == INVALID_INPUT || month < 1 || month > 12 ) {
            System.out.print("Please enter the moth (format MM): ");
            month = readInteger(false);
            if (month < 1 || month > 12) {
                System.out.println("Please enter a valid month");
            }
        }

        int day = INVALID_INPUT;
        while (day == INVALID_INPUT || day < 0 || day > 31) {
            System.out.print("Please enter the day (format DD): ");
            day = readInteger(false);
            if (day < 0 || day > 31) {
                System.out.println("Please enter a valid day");
            }
        }

        String strDate = year + "-" + month + "-" + day;

        System.out.println(strDate);

        Date date = Date.valueOf(strDate);

        return date;
    }

    private void handleGetNumHostsPerUser() {
        HostsPerUser[] models = delegate.getNumHostsPerUser();
        for (int i = 0; i < models.length; i++) {
            HostsPerUser model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getUsername());
            System.out.printf("%-20.20s", model.getNumHosts());
            System.out.println();
        }
    }

    private void handleFindUsersWishingGames() {
        String description = null;
        while (description == null || description.length() <= 0) {
            System.out.print("Please the gameIds (seperated by commas, no spaces): ");
            description = readLine().trim();
        }
        String[] users = delegate.findUsersWishingGames(description.split(","));

        for (int i = 0; i < users.length; i++) {
            String user = users[i];

            // simplified output formatting; truncation may occur
            System.out.println(user);
        }
    }

    private void handleInsertWishlist() {
        int id = INVALID_INPUT;
        while (id == INVALID_INPUT) {
            System.out.print("Insert WishlistId");
            id = readInteger(false);
        }
        WishListModel wishlist = new WishListModel(id, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));

        try {
            delegate.insertWishlist(wishlist);
        } catch (SQLException e) {
            System.out.println("WishlishID is already in use.");
        }
    }

    private void handleInsertUser() {
        int wid = INVALID_INPUT;
        while(wid == INVALID_INPUT) {
            System.out.print("Insert WishlistID: ");
            wid = readInteger(false);
        }
        String username = null;
        while(username == null || username.length() == 0) {
            System.out.print("Insert username: ");
            username = readLine().trim();
        }
        String email = null;
        while(email == null || email.length() == 0) {
            System.out.print("Insert email: ");
            email = readLine().trim();
        }
        int result = delegate.insertUser(new UserModel(username, "aaa", email,
                wid, "bbb", "ccc", "Canada"));
        if (result == 1 || result == 2) {
            System.out.println("User Successfully added!");
        } else {
            System.out.println("Username, email, or WishlistId already in use.");
        }
    }

    private void handleInsertContains() {
        int wid = INVALID_INPUT;
        while(wid == INVALID_INPUT) {
            System.out.print("Insert WishlistID: ");
            wid = readInteger(false);
        }
        int bgid = INVALID_INPUT;
        while(bgid == INVALID_INPUT) {
            System.out.print("Insert BoardgameID: ");
            bgid = readInteger(false);
        }

        boolean result = delegate.insertContains(new ContainsModel(wid, bgid));
        if (result) {
            System.out.println("Board game successfully added to Wishlist!");
        } else {
            System.out.println("That wishlist already contains that board game, or the Wishlist or BG doesn't exist");
        }
    }
    private void handleInsertTransaction() {
        int tid = INVALID_INPUT;
        while(tid == INVALID_INPUT) {
            System.out.print("Insert TransactionID: ");
            tid = readInteger(false);
        }
        double amount = INVALID_INPUT;
        while(amount == INVALID_INPUT) {
            System.out.print("Amount: ");
            amount = readDecimal(false);
        }
        String username1 = null;
        while(username1 == null || username1.length() == 0) {
            System.out.print("Insert buyer username: ");
            username1 = readLine().trim();
        }
        String username2 = null;
        while(username2 == null || username2.length() == 0) {
            System.out.print("Insert seller username: ");
            username2 = readLine().trim();
        }

        boolean result = delegate.insertTransaction(new TransactionModel(tid, new BigDecimal(amount), username1,
                username2, new java.sql.Date(Calendar.getInstance().getTimeInMillis())));
        if (result) {
            System.out.println("Transaction successfully added!");
        } else {
            System.out.println("Buyer or Seller doesn't exist, or TransactionID already in use.");
        }
    }

    private void handleInsertListing() {
        int lid = delegate.getTotalListings() + 1;
        double price = INVALID_INPUT;
        while (price == INVALID_INPUT){
            System.out.print("Please enter your designated price: ");
            price = readDecimal(false);
        }
        int boardGameID = INVALID_INPUT;
        while (boardGameID == INVALID_INPUT) {
            System.out.print("Please enter the Board Game ID you wish to feature: ");
            boardGameID = readInteger(false);
            if(!delegate.isBoardGameID(boardGameID)) {
                System.out.print("The entered board game id does not exist!");
                boardGameID = INVALID_INPUT;
            }
        }
        String userName = null;
        while (userName == null || userName.length() <= 0) {
            System.out.print("Who is the user making this?: ");
            userName = readLine().trim();
            if(!delegate.isUserName(userName)) {
                System.out.println("The entered user name does not exist!");
                userName = null;
            }
        }
        String description = null;
        System.out.print("What is the description: ");
        description = readLine().trim();

        ListingModel listingModel = new ListingModel(lid,new java.sql.Date(Calendar.getInstance().getTimeInMillis()),new BigDecimal(price),description,boardGameID,userName);


        delegate.insertListing(listingModel);

    }
    private void handleInsertPublisher() {
        int pid = delegate.getTotalPublishers() + 1;

        String websiteName = null;
        while (websiteName == null || websiteName.length() <= 0) {
            System.out.print("What is the website?: ");
            websiteName = readLine().trim();
        }

        String publisherName = null;
        while (publisherName == null || publisherName.length() <= 0) {
            System.out.print("What is the name of the publisher: ");
            publisherName = readLine().trim();
            if(!delegate.isNotPublisherName(publisherName)) {
                System.out.println("The entered publisher name already exists!");
                publisherName = null;
            }
        }

        PublisherModel publisherModel = new PublisherModel(publisherName,websiteName,pid);


        delegate.insertPublisher(publisherModel);
    }

    private void handleInsertReview() {
        String userName = null;
        while (userName == null || userName.length() <= 0) {
            System.out.print("Who is reviewing?: ");
            userName = readLine().trim();
            if(!delegate.isUserName(userName)) {
                System.out.println("The entered user name does not exist!");
                userName = null;
            }
        }
        int boardGameID = INVALID_INPUT;
        while (boardGameID == INVALID_INPUT) {
            System.out.print("Please enter the Board Game ID you wish to review: ");
            boardGameID = readInteger(false);
            if(!delegate.isBoardGameID(boardGameID)) {
                System.out.print("The entered board game id does not exist!");
                boardGameID = INVALID_INPUT;
            }
        }

        int score = INVALID_INPUT;
        while (score == INVALID_INPUT){
            System.out.print("Please enter your scoring from 0-10: ");
            score = readInteger(false);
            if(score < 0 || score > 10 ){
                score = INVALID_INPUT;
                System.out.println("Score is out of range!");
            }
        }
        String review = null;
        System.out.print("What is the review?: ");
        review = readLine().trim();

        ReviewsModel reviewModel = new ReviewsModel(userName,boardGameID,new java.sql.Date(Calendar.getInstance().getTimeInMillis()),score,review);
        delegate.insertReview(reviewModel);
    }

    private void handleInsertTournamentGames() {
        int boardGameID = INVALID_INPUT;
        while (boardGameID == INVALID_INPUT) {
            System.out.print("Please enter the Board Game ID you wish to insert for Tournament games: ");
            boardGameID = readInteger(false);
            if(!delegate.isBoardGameID(boardGameID)) {
                System.out.println("The entered board game id does not exist!");
                boardGameID = INVALID_INPUT;
            }
            if(delegate.isTournamentGame(boardGameID)){
                System.out.println("The entered board game id is already in the list!");
                boardGameID = INVALID_INPUT;
            }
        }

        String rules = null;
        while (rules == null || rules.length() <= 0) {
            System.out.print("What are the rules of the tournament?: ");
            rules = readLine().trim();
        }

        int minP = INVALID_INPUT;
        while (minP == INVALID_INPUT) {
            System.out.print("Please enter the min participants count: ");
            minP = readInteger(false);
        }

        TournamentGameModel tournamentGameModel = new TournamentGameModel(boardGameID,rules,minP);
        delegate.insertTournamentGame(tournamentGameModel);

    }
    private void handleInsertTeamGames() {
        int boardGameID = INVALID_INPUT;
        while (boardGameID == INVALID_INPUT) {
            System.out.print("Please enter the Board Game ID you wish to insert for Tournament games: ");
            boardGameID = readInteger(false);
            if(!delegate.isBoardGameID(boardGameID)) {
                System.out.println("The entered board game id does not exist!");
                boardGameID = INVALID_INPUT;
            }
            if(delegate.isTeamGame(boardGameID)){
                System.out.println("The entered board game id is already in the list!");
                boardGameID = INVALID_INPUT;
            }
        }

        int minT = INVALID_INPUT;
        while (minT == INVALID_INPUT) {
            System.out.print("Please enter the min participants count: ");
            minT = readInteger(false);
        }

        TeamGameModel teamGameModel = new TeamGameModel(minT,boardGameID);
        delegate.insertTeamGame(teamGameModel);
    }

    private void handleSelectBGP() {
        String publisher = null;
        while (publisher == null || publisher.length() <= 0) {
            System.out.print("What publisher name would you like to see the board games of?: ");
            publisher = readLine().trim();
        }
        delegate.selectBGP(publisher);
    }

    private void handleAverageScoreBG() {
        int publisherID = INVALID_INPUT;
        while(publisherID == INVALID_INPUT){
            System.out.print("Please enter the ID of the publisher you'd like to view: ");
            publisherID = readInteger(false);
        }

        delegate.averageScoreBG(publisherID);
    }
    private void handleUserLogin() {
        String username = null;
        while(username == null || username.length() == 0) {
            System.out.print("Enter username: ");
            username = readLine().trim();
        }
        String password = null;
        while(password == null || password.length() == 0) {
            System.out.print("Enter Password: ");
            password = readLine().trim();
        }

        boolean result = delegate.checkUserCredentials(username, password);

        if (result) {
            System.out.println("If this were the GUI, the user would be logged in and sent to the home tab.");
        } else {
            System.out.println("If this were the GUI, An error should display saying credentials were incorrect.");
        }
    }

    private void handleQuitOption() {
        System.out.println("Good Bye!");

        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                System.out.println("IOException!");
            }
        }

        delegate.terminalTransactionsFinished();
    }

    private void handleUpdatePublisher() {
        int id = INVALID_INPUT;
        while (id == INVALID_INPUT) {
            System.out.print("Please enter the publisher ID you wish to update: ");
            id = readInteger(false);
        }

        String name = null;
        while (name == null || name.length() <= 0) {
            System.out.print("Please enter the publisher name you wish to update: ");
            name = readLine().trim();
        }

        String website = null;
        while (website == null || website.length() <= 0) {
            System.out.print("Please enter the website domain you wish to update: ");
            website = readLine().trim();
        }

        delegate.updatePublisher(id, name, website);
    }

    private void handleFilterListing(){
        List<Object> operators = new ArrayList<Object>();
        String operator = "abcdefg";
        double price;
        while (!operator.equals("quit")) {
            operator = null;
            while (operator == null){
                System.out.print("Please enter the operator you wish to filter on price for Listing (more, less, or equal): ");
                operator = readLine().trim().toLowerCase();
                if(!operator.equals("more") && !operator.equals("less") && !operator.equals("equal")){
                    operator = null;
                    System.out.println(WARNING_TAG + " Invalid output, try again");
                } else {
                    operators.add(operator);
                }
            }
            price = INVALID_INPUT;
            while (price == INVALID_INPUT){
                System.out.print("Please enter your desired filtering price on Listing: ");
                price = readDecimal(false);
                if(price < 0){
                    price = INVALID_INPUT;
                    System.out.println("Can't have a price value in the negatives or letters!");
                } else {
                    operators.add((price));
                }

            }
            operator = null;
            while (operator == null) {
                System.out.print("Please enter the connective state (AND/OR) or quit: ");
                operator = readLine().trim().toLowerCase();
                if(!operator.equals("and") && !operator.equals("or") && !operator.equals("quit")){
                    operator = null;
                    System.out.println(WARNING_TAG + " Invalid output, try again");
                } else if(operator.equals("quit")) {
                    break;
                } else {
                    operators.add(operator.toUpperCase());
                }
            }

        }
        delegate.filterListing(operators);
    }

    private void handleFilterListing2(){
        String priceOperator = "";
        ArrayList<String> cState = new ArrayList<>();
        Map<String,Object> attributeMap = new LinkedHashMap<>();
        ArrayList<String> selectable = new ArrayList<String>(Arrays.asList("lid", "dateListed", "price", "description","boardGameID","username","quit"));
        String operator = "";
        while (!operator.equals("quit")){
            System.out.println("Please enter the attribute you wish to filter on for Listing (lid,dateListed,price,description, boardGameID,username) or quit: ");
            operator = readLine().trim();
            if (!selectable.contains(operator)){
                System.out.println("This attribute either doesn't exist or is already being filtered on!");
                operator = "";
                continue;
            } else if (operator.equals("price")) {
                while (priceOperator.isEmpty()){
                    System.out.print("Please enter the operator you wish to filter on price for Listing (more, less, or equal): ");
                    priceOperator = readLine().trim().toLowerCase();
                    if(!priceOperator.equals("more") && !priceOperator.equals("less") && !priceOperator.equals("equal")) {
                        priceOperator = "";
                        System.out.println(WARNING_TAG + " Invalid output, try again");
                    }
                }
                double price = INVALID_INPUT;
                while (price == INVALID_INPUT){
                    System.out.print("Please enter your desired filtering price on Listing: ");
                    price = readDecimal(false);
                    if(price < 0){
                        price = INVALID_INPUT;
                        System.out.println("Can't have a price value in the negatives or letters!");
                    } else {
                        attributeMap.put(operator,price);
                        selectable.remove(operator);
                    }
                }
            } else if (operator.equals("lid") || operator.equals("boardGameID")){
                int choice = INVALID_INPUT;
                while (choice == INVALID_INPUT){
                    System.out.print("Please enter your desired filtering ID on Listing: ");
                    choice = readInteger(false);
                    if(choice <= 0){
                        choice = INVALID_INPUT;
                        System.out.println("Invalid Input");
                    } else {
                        attributeMap.put(operator,choice);
                        selectable.remove(operator);
                    }
                }
            } else if (operator.equals("description") || operator.equals("username")) {
                String choice = "";
                while (choice.isEmpty()){
                    System.out.print("Please enter the filter on " + operator + ": ");
                    choice = readLine().trim().toLowerCase();
                    attributeMap.put(operator,choice);
                    selectable.remove(operator);
                }
            } else if (operator.equals("dateListed")){
                Date dateListed = getDate();
                attributeMap.put(operator,dateListed);
                selectable.remove(operator);
            } else{
                if(attributeMap.isEmpty()){
                    return;
                }
                continue;
            }
            String connecting = "";
            while (connecting.isEmpty()) {
                System.out.print("Please enter the connective state (AND/OR): ");
                connecting = readLine().trim().toLowerCase();
                if(!connecting.equals("and") && !connecting.equals("or")){
                    connecting= "";
                    System.out.println(WARNING_TAG + " Invalid output, try again");
                } else {
                    cState.add(connecting);
                }
            }
        }
        int i = 0;
        for (Map.Entry<String, Object> entry :
                attributeMap.entrySet()) {

            // Printing keys

            System.out.print(entry.getKey() + ": ");
            System.out.println(entry.getValue());
            System.out.println(cState.get(i));
            i++;
        }
        delegate.filterListing2(attributeMap,cState,priceOperator);
    }

//    private void handleUpdateOption() {
//        int id = INVALID_INPUT;
//        while (id == INVALID_INPUT) {
//            System.out.print("Please enter the branch ID you wish to update: ");
//            id = readInteger(false);
//        }
//
//        String name = null;
//        while (name == null || name.length() <= 0) {
//            System.out.print("Please enter the branch name you wish to update: ");
//            name = readLine().trim();
//        }
//
//        delegate.updateBranch(id, name);
//    }


    private int readInteger(boolean allowEmpty) {
        String line = null;
        int input = INVALID_INPUT;
        try {
            line = bufferedReader.readLine();
            input = Integer.parseInt(line);
        } catch (IOException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        } catch (NumberFormatException e) {
            if (allowEmpty && line.length() == 0) {
                input = EMPTY_INPUT;
            } else {
                System.out.println(WARNING_TAG + " Your input was not an integer");
            }
        }
        return input;
    }
    private double readDecimal(boolean allowEmpty) {
        String line = null;
        double input = INVALID_INPUT;
        try {
            line = bufferedReader.readLine();
            input = Double.parseDouble(line);
        } catch (IOException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        } catch (NumberFormatException e) {
            if (allowEmpty && line.length() == 0) {
                input = EMPTY_INPUT;
            } else {
                System.out.println(WARNING_TAG + " Your input was not an number");
            }
        }
        return input;
    }

    private String readLine() {
        String result = null;
        try {
            result = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }
}
