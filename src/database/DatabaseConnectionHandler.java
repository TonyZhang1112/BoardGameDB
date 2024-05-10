package ca.ubc.cs304.database;

import ca.ubc.cs304.model.*;
import ca.ubc.cs304.util.PrintablePreparedStatement;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

// Class based off the same one in the CPSC 304 Sample Project https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project
/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    // Use this version of the ORACLE_URL if you are running the code off of the server
    //	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void deleteListing(int listingID){
        try {
            String choose = "SELECT DISTINCT * FROM Listing WHERE LISTINGID = ?";
            PrintablePreparedStatement pps = new PrintablePreparedStatement(connection.prepareStatement(choose), choose, false);
            pps.setInt(1,listingID);
            ResultSet listing = pps.executeQuery();
            String query = "DELETE FROM LISTING WHERE LISTINGID = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, listingID);
            if (listing.next()) {
                int rowCount = ps.executeUpdate();
                if (rowCount == 0) {
                    System.out.println(WARNING_TAG + "The Listing you are purchasing does not exist!");
                } else {
                    insertTransaction(new TransactionModel(listing.getInt("ListingID") + 100, listing.getBigDecimal("PRICE"),
                            listing.getString("USERNAME"), listing.getString("USERNAME"), new java.sql.Date(System.currentTimeMillis())));
                }

                connection.commit();

                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }
    public void deleteGES(String seriesName) {
        try {

            String uniqueDateQuery = "SELECT StartDate, EndDate FROM GAMEEVENTSERIES1 WHERE seriesName = ?";
            PrintablePreparedStatement uniqueDatePs = new PrintablePreparedStatement(connection.prepareStatement(uniqueDateQuery),
                    uniqueDateQuery, false);
            uniqueDatePs.setString(1, seriesName);
            ResultSet uniqueDateRS = uniqueDatePs.executeQuery();

            if (uniqueDateRS.next()) {
                Date startDate = uniqueDateRS.getDate("StartDate");
                Date endDate = uniqueDateRS.getDate("EndDate");


                String countQuery = "SELECT COUNT(*) FROM GAMEEVENTSERIES1 WHERE StartDate = ? AND EndDate =?";
                PrintablePreparedStatement countPS = new PrintablePreparedStatement(connection.prepareStatement(countQuery),
                        countQuery, false);

                countPS.setDate(1, startDate);
                countPS.setDate(2, endDate);


                ResultSet countRS = countPS.executeQuery();


                if (countRS.next() && countRS.getInt(1) == 1) {

                    String deleteGES2Query = "DELETE FROM GAMEEVENTSERIES2 WHERE StartDate = ? AND EndDate = ?";
                    PrintablePreparedStatement delGES2PS = new PrintablePreparedStatement(connection.prepareStatement(deleteGES2Query),
                            deleteGES2Query, false);
                    delGES2PS.setDate(1, startDate);
                    delGES2PS.setDate(2, endDate);

                    int rowCount = delGES2PS.executeUpdate();

                    if (rowCount == 0) {
                        System.out.println(WARNING_TAG + startDate + " " + endDate + " does not exist! on GES2");
                    } else {
                        System.out.println("Deleted from GES2");
                    }
                    countPS.close();
                    delGES2PS.close();
                }
            }

            String query = "DELETE FROM GAMEEVENTSERIES1 WHERE seriesName = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, seriesName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Series " + seriesName + " does not exist!");
            } else {
                System.out.println(seriesName + " was deleted from GES1");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }


    public void globalP(String tableName) {

        try {
            // get col names from given table
            System.out.println(tableName);

            String tableNameQuery = "SELECT * FROM " + tableName;
            PrintablePreparedStatement tableNamePS = new PrintablePreparedStatement(connection.prepareStatement(tableNameQuery),
                    tableNameQuery, false);

            ResultSet tableNameRS = tableNamePS.executeQuery();

            ResultSetMetaData rsmd = tableNameRS.getMetaData();
            int numberOfCol = rsmd.getColumnCount();
            ArrayList<String> colNames = new ArrayList<>();

            // build array of col names
            for (int i = 1; i<= numberOfCol; i++) {
                colNames.add(rsmd.getColumnName(i));
            }

            System.out.println("Column names that are available are: " + colNames);

            Scanner scanner = new Scanner(System.in);
            ArrayList<String> selColNames = new ArrayList<>();
            String input;

            System.out.println("Enter the column names you would like to project, enter done when complete");

            while(!(input = scanner.nextLine().trim()).equalsIgnoreCase("done")) {
                if (colNames.contains(input)) {
                    selColNames.add(input);
                } else {
                    System.out.println("Invalid column name detected, please enter valid column name");
                }
            }

            String pQuery = "SELECT " + String.join(", ", selColNames) + " FROM " + tableName;
            PrintablePreparedStatement pPs = new PrintablePreparedStatement(connection.prepareStatement(pQuery), pQuery, false);
            ResultSet pRS = pPs.executeQuery();

            while(pRS.next()) {
                for (String colName: selColNames) {
                    System.out.print(pRS.getString(colName) + " ");
                }
                System.out.println();
            }

            tableNamePS.close();
            tableNameRS.close();
            pPs.close();
            pRS.close();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }


    }

    public String[] getTableNames() {
        ArrayList<String> tables = new ArrayList<>();
        try {
            String query = "select table_name from user_tables";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tables.add(rs.getString(1));
            }

            rs.close();
            ps.close();
            return tables.toArray(new String[tables.size()]);
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return tables.toArray(new String[tables.size()]);
        }
    }

    public String[] getColumnNames(String tableName) {
        ArrayList<String> colNames = new ArrayList<>();
        try {
            String tableNameQuery = "SELECT * FROM " + tableName;
            PrintablePreparedStatement tableNamePS = new PrintablePreparedStatement(connection.prepareStatement(tableNameQuery),
                    tableNameQuery, false);

            ResultSet tableNameRS = tableNamePS.executeQuery();

            ResultSetMetaData rsmd = tableNameRS.getMetaData();
            int numberOfCol = rsmd.getColumnCount();

            // build array of col names
            for (int i = 1; i <= numberOfCol; i++) {
                colNames.add(rsmd.getColumnName(i));
            }
            return colNames.toArray(new String[colNames.size()]);
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return colNames.toArray(new String[colNames.size()]);
        }
    }

    public DefaultTableModel getSearchResult(String tableName, String[] columnsSelected) {
        String selectClause = String.join(",", columnsSelected);
        String[] columnNames = getColumnNames(tableName);
        DefaultTableModel tableModel = selectClause != "*" ? new DefaultTableModel(columnsSelected, 0) :
                new DefaultTableModel(columnNames, 0);
        try {
            String query = "SELECT " + selectClause + " FROM " + tableName;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int tupleColumns = selectClause != "*" ? columnsSelected.length : columnNames.length;
                Object[] tuple = new String[tupleColumns];
                for (int i = 1; i <= tupleColumns; i++) {
                    tuple[i-1] = rs.getString(i);
                }
                tableModel.addRow(tuple);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return tableModel;
    }

    public PublisherModel[] getPublisherInfo() {
        ArrayList<PublisherModel> result = new ArrayList<PublisherModel>();

        try {
            String query = "SELECT * FROM PUBLISHER";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PublisherModel model = new PublisherModel(rs.getString("PUBLISHERNAME"),
                        rs.getString("WEBSITE"),
                        rs.getInt("PUBLISHERID"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new PublisherModel[result.size()]);
    }

    public BGPModel[] selectBGP(String publisher) {
        ArrayList<BGPModel> result = new ArrayList<BGPModel>();

        try {
            String query = "SELECT TITLE, GENRE, PUBLISHERNAME FROM PUBLISHER NATURAL JOIN BOARDGAME WHERE PUBLISHERNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1,publisher);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BGPModel model = new BGPModel(rs.getString("PUBLISHERNAME"),
                        rs.getString("TITLE"),
                        rs.getString("GENRE"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new BGPModel[result.size()]);
    }

    public ListingModel[] getListingInfo() {
        ArrayList<ListingModel> result = new ArrayList<ListingModel>();

        try {
            String query = "SELECT * FROM LISTING";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ListingModel model = new ListingModel(rs.getInt("LISTINGID"),
                        rs.getDate("DATELISTEd"),
                        rs.getBigDecimal("PRICE"),
                        rs.getString("DESCRIPTION"),
                        rs.getInt("BOARDGAMEID"),
                        rs.getString("USERNAME"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ListingModel[result.size()]);
    }

    public ReviewsModel[] getReviewInfo() {
        ArrayList<ReviewsModel> result = new ArrayList<ReviewsModel>();

        try {
            String query = "SELECT * FROM REVIEWS";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReviewsModel model = new ReviewsModel(rs.getString("USERNAME"),
                        rs.getInt("BOARDGAMEID"),
                        rs.getDate("RDATE"),
                        rs.getInt("SCORE"),
                        rs.getString("REVIEW"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ReviewsModel[result.size()]);
    }

    public TournamentGameModel[] getTournamentInfo() {
        ArrayList<TournamentGameModel> result = new ArrayList<TournamentGameModel>();

        try {
            String query = "SELECT * FROM TOURNAMENTGAME";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TournamentGameModel model = new TournamentGameModel(
                        rs.getInt("BOARDGAMEID"),
                        rs.getString("TOURNAMENTRULES"),
                        rs.getInt("MINPARTICIPANTS"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new TournamentGameModel[result.size()]);
    }
    public TeamGameModel[] getTeamInfo() {
        ArrayList<TeamGameModel> result = new ArrayList<TeamGameModel>();

        try {
            String query = "SELECT * FROM TEAMGAME";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TeamGameModel model = new TeamGameModel(
                        rs.getInt("MINTEAMSIZE"),
                        rs.getInt("BOARDGAMEID"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new TeamGameModel[result.size()]);
    }
    public BoardGameModel[] getBGInfo() {
        ArrayList<BoardGameModel> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM BOARDGAME";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BoardGameModel model = new BoardGameModel(rs.getInt("BOARDGAMEID"),
                        rs.getString("TITLE"),
                        rs.getString("GENRE"),
                        rs.getString("DESCRIPTION"),
                        rs.getInt("PUBLISHERID"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new BoardGameModel[result.size()]);
    }

    public DefaultTableModel getBGPubInfo() {
        String[] columnNames = {"BoardGameID", "Title", "Genre", "Description", "Publisher Name", "Website"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            String query = "SELECT bg.BOARDGAMEID, bg.TITLE, bg.GENRE, " +
                    "bg.DESCRIPTION, p.PUBLISHERNAME, p.WEBSITE FROM BOARDGAME bg NATURAL JOIN PUBLISHER p";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("BOARDGAMEID"),
                    rs.getString("TITLE"),
                    rs.getString("GENRE"),
                    rs.getString("DESCRIPTION"),
                    rs.getString("PUBLISHERNAME"),
                    rs.getString("WEBSITE")
                };

                tableModel.addRow(row);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return tableModel;
    }


    public ReviewsModel[] averageScoreBG(int publisherID) {
        ArrayList<ReviewsModel> result = new ArrayList<ReviewsModel>();
        try{
            String query = "SELECT PUBLISHERID, AVG(avg) as avgr " +
                    "FROM (BOARDGAME  NATURAL JOIN (SELECT BOARDGAMEID, AVG(SCORE) as avg FROM REVIEWS GROUP BY BOARDGAMEID) ) " +
                    "WHERE PUBLISHERID = ? " +
                    "GROUP BY PUBLISHERID";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1,publisherID);
            ResultSet rs = ps.executeQuery();
            int check = 1;
            while (rs.next()){
                ReviewsModel model = new ReviewsModel(null,rs.getInt("PUBLISHERID"),null,rs.getInt("avgr"),null);
                result.add(model);
                check = 0;
            }
            if(!rs.next() && check == 1){
                ReviewsModel model = new ReviewsModel(null,0,null,0,null);
                result.add(model);
                return result.toArray(new ReviewsModel[result.size()]);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new ReviewsModel[result.size()]);
    }

    public void updatePublisher(int id, String name, String website) {
        try {
            String query = "UPDATE PUBLISHER SET PUBLISHERNAME = ?, WEBSITE = ? WHERE PUBLISHERID = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            ps.setString(2, website);
            ps.setInt(3, id);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Publisher " + id + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public ListingModel[] filterListing(List<Object> operators) {
        ArrayList<ListingModel> result = new ArrayList<ListingModel>();
        String query;
        switch ((String) operators.get(0)) {
            case "more":
                query = "SELECT * FROM LISTING WHERE PRICE > ? ";
                break;
            case "equal":
                query = "SELECT * FROM LISTING WHERE PRICE = ? ";
                break;
            default:
                query = "SELECT * FROM LISTING WHERE PRICE < ? ";
                break;
        }

        if (operators.size() > 2) {
            for (int i = 3; i <= operators.size(); i++) {
                if (i % 3 == 0) {
                    switch ((String) operators.get(i - 1)) {
                        case "AND":
                            query += "AND ";
                            break;
                        default:
                            query += "OR ";
                            break;
                    }
                } else if ((i - 1) % 3 == 0) {
                    switch ((String) operators.get(i - 1)) {
                        case "more":
                            query += "PRICE > ? ";
                            break;
                        case "equal":
                            query += "PRICE = ? ";
                            break;
                        default:
                            query += "PRICE < ? ";
                            break;
                    }
                }

            }
        }

        try {
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setBigDecimal(1, new BigDecimal((double)operators.get(1)));
            int rebalance = 1;
            for (int i = 3; i <= operators.size();i++){
                if((i-2)%3 ==0){
                    rebalance++;
                    ps.setBigDecimal(rebalance, new BigDecimal((double)operators.get(i-1)));
                }
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                ListingModel model = new ListingModel(rs.getInt("LISTINGID"),
                        rs.getDate("DATELISTED"),
                        rs.getBigDecimal("PRICE"),
                        rs.getString("DESCRIPTION"),
                        rs.getInt("BOARDGAMEID"),
                        rs.getString("USERNAME"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ListingModel[result.size()]);
    }

    public ListingModel[] filterListing2(Map<String, Object> attributeMap, ArrayList<String> cState, String priceOperator) {
        ArrayList<ListingModel> result = new ArrayList<ListingModel>();
        String query = "SELECT * FROM LISTING ";
        Map.Entry<String,Object> entry = attributeMap.entrySet().iterator().next();
        String key = entry.getKey();
        switch (key){
            case "lid":
                query += "WHERE LISTINGID = ? ";
                break;
            case  "dateListed":
                query += "WHERE DATELISTED = ? ";
                break;
            case "price":
                switch (priceOperator){
                    case "more":
                        query += "WHERE PRICE > ? ";
                        break;
                    case "equal":
                        query += "WHERE PRICE = ? ";
                        break;
                    default:
                        query += "WHERE PRICE < ? ";
                        break;
                }
                break;
            case "description":
                query += "WHERE DESCRIPTION LIKE ? ";
                break;
            case "boardGameID":
                query += "WHERE BOARDGAMEID = ? ";
                break;
            case "username":
                query += "WHERE USERNAME LIKE ? ";
                break;
            default:
                break;
        }
        if (attributeMap.size() > 1){
            if (cState.get(0).equals("and")) {
                query += "AND ";
            } else {
                query += "OR ";
            }
            int i = 0;
            for (Map.Entry<String, Object> e :
                    attributeMap.entrySet()) {
                if (i == 0){
                    i++;
                    continue;
                }
                String k = e.getKey();
                switch (k){
                    case "lid":
                        query += "LISTINGID = ? ";
                        break;
                    case  "dateListed":
                        query += "DATELISTED = ? ";
                        break;
                    case "price":
                        switch (priceOperator){
                            case "more":
                                query += "PRICE > ? ";
                                break;
                            case "equal":
                                query += "PRICE = ? ";
                                break;
                            default:
                                query += "PRICE < ? ";
                                break;
                        }
                        break;
                    case "description":
                        query += "DESCRIPTION LIKE ? ";
                        break;
                    case "boardGameID":
                        query += "BOARDGAMEID = ? ";
                        break;
                    case "username":
                        query += "USERNAME LIKE ? ";
                        break;
                    default:
                        break;
                }
                if(i<cState.size()) {
                    if (cState.get(i).equals("and")) {
                        query += "AND ";
                    } else {
                        query += "OR ";
                    }
                }
                i++;
            }
        }

        System.out.println(query);
        try {
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            int f = 1;
            for (Map.Entry<String, Object> e :
                    attributeMap.entrySet()) {
                String k = e.getKey();
                switch (k){
                    case "lid":
                        ps.setInt(f,(int)e.getValue());
                        break;
                    case  "dateListed":
                        ps.setDate(f,(Date) e.getValue());
                        break;
                    case "price":
                        ps.setBigDecimal(f, new BigDecimal((double)e.getValue()));
                        break;
                    case "description":
                        ps.setString(f,"%"+ (String) e.getValue() +"%");
                        break;
                    case "boardGameID":
                        ps.setInt(f,(int)e.getValue());
                        break;
                    case "username":
                        ps.setString(f,"%"+ (String) e.getValue() +"%");
                        break;
                    default:
                        break;
                }
                f++;

            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                ListingModel model = new ListingModel(rs.getInt("LISTINGID"),
                        rs.getDate("DATELISTED"),
                        rs.getBigDecimal("PRICE"),
                        rs.getString("DESCRIPTION"),
                        rs.getInt("BOARDGAMEID"),
                        rs.getString("USERNAME"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ListingModel[result.size()]);
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void databaseSetup(String sqlPath) {
//        dropTableIfExists("CONTAINS");
//        dropTableIfExists("FEATURED");
//        dropTableIfExists("REVIEWS");
//        dropTableIfExists("HOSTS");
//        dropTableIfExists("TEAMGAME");
//        dropTableIfExists("TOURNAMENTGAME");
//        dropTableIfExists("GAMEEVENT");
//        dropTableIfExists("TRANSACTION");
//        dropTableIfExists("USER2");
//        dropTableIfExists("GAMEEVENTSERIES2");
//        dropTableIfExists("LISTING");
//        dropTableIfExists("BOARDGAME");
//        dropTableIfExists("PUBLISHER");
//        dropTableIfExists("GAMEEVENTSERIES1");
//        dropTableIfExists("USER1");
//        dropTableIfExists("WISHLIST");

        try {
            String content = new String(Files.readAllBytes(Paths.get(sqlPath)), StandardCharsets.UTF_8);
            executeSqlScript(content);

        } catch (IOException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void executeSqlScript(String script) throws SQLException {
        String[] commands = script.split(";");

        Statement statement = connection.createStatement();
        for (String command : commands) {

            command = command.trim();

            if (!command.isEmpty()) {
                statement.addBatch(command);
            }
        }
        statement.executeBatch();
        statement.close();
    }

    private void dropTableIfExists(String s) {
        try {
            String query = "select table_name from user_tables";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString(1).toUpperCase().equals(s)) {
                    ps.execute("DROP TABLE " + s);
                    break;
                }
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void insertBoardGame(BoardGameModel bgModel) {

        try {
            String query = "INSERT INTO BOARDGAME VALUES (?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setInt(1, bgModel.getId());
            ps.setString(2, bgModel.getTitle());
            ps.setString(3, bgModel.getGenre());
            ps.setString(4, bgModel.getDescription());
            ps.setInt(5, bgModel.getPid());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertWishlist(WishListModel wishlist) throws SQLException {
        String query = "INSERT INTO Wishlist VALUES (?,?)";
        PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                query, false);
        ps.setInt(1, wishlist.getWid());
        ps.setDate(2, wishlist.getDateUpdated());

        ps.executeUpdate();
        connection.commit();

        ps.close();
    }

    public HostsPerUser[] getNumHostsForUser() {
        ArrayList<HostsPerUser> result = new ArrayList<>();
        try {
            String query = "SELECT username, count(*) FROM Hosts GROUP BY username";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HostsPerUser model = new HostsPerUser(rs.getString(1),
                        rs.getInt(2));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return result.toArray(new HostsPerUser[result.size()]);
    }

    public DefaultTableModel getNumHostsForUserFE() {
        String[] columnNames = {"Username", "Number of Sessions"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        try {
            String query = "SELECT username, count(*) FROM Hosts GROUP BY username";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getString(1),
                        rs.getInt(2)
                };
                tableModel.addRow(row);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return tableModel;
    }

    public String[] findUsersWishingGames(String[] games) {
        ArrayList<String> result = new ArrayList<>();
        String inputString = "WHERE ";
        for (int i = 0; i < games.length; i++) {
            inputString += "boardGameID="+games[i] + (i < games.length - 1 ? " OR " : "");
        }
        try {
            String query = ("SELECT username FROM USER1 WHERE NOT EXISTS ((SELECT boardGameID " +
                    "FROM BoardGame %s) MINUS (SELECT CONTAINS.boardGameID FROM CONTAINS WHERE " +
                    "CONTAINS.WISHLISTID = USER1.WISHLISTID))").formatted(inputString);
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String user = rs.getString(1);
                result.add(user);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return result.toArray(new String[result.size()]);
    }

    public DefaultTableModel findUsersWishingGamesFE(String[] ids) {
        String[] columnNames = {"Username"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        String inputString = "WHERE ";
        for (int i = 0; i < ids.length; i++) {
            inputString += "boardGameID="+ids[i] + (i < ids.length - 1 ? " OR " : "");
        }
        try {
            String query = ("SELECT username FROM USER1 WHERE NOT EXISTS ((SELECT boardGameID " +
                    "FROM BoardGame %s) MINUS (SELECT CONTAINS.boardGameID FROM CONTAINS WHERE " +
                    "CONTAINS.WISHLISTID = USER1.WISHLISTID))").formatted(inputString);
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

            Object[] row = {
                    rs.getString("USERNAME")
            };
                tableModel.addRow(row);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return tableModel;
    }

    public int insertUser(UserModel user) {
        boolean insertedIntoWishlist = false;
        try {
            //TODO: look into masking the ms in date
            insertWishlist(new WishListModel(user.getWid(), new java.sql.Date(Calendar.getInstance().getTimeInMillis())));
            insertedIntoWishlist = true;

            String query = "INSERT INTO User1 VALUES (?,?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getWid());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getPostalCode());

            ps.executeUpdate();
            connection.commit();
            ps.close();

            String query2 = "SELECT count(*) FROM USER2 WHERE address=(?) AND postalCode=(?)";
            PrintablePreparedStatement ps2 = new PrintablePreparedStatement(connection.prepareStatement(query2),
                    query2, false);
            ps2.setString(1, user.getAddress());
            ps2.setString(2, user.getPostalCode());
            int count = 1;
            ResultSet result = ps2.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
            if (count == 0) {
                String query3 = "INSERT INTO USER2 VALUES(?,?,?)";
                PrintablePreparedStatement ps3 = new PrintablePreparedStatement(connection.prepareStatement(query3),
                        query3, false);
                ps3.setString(1, user.getAddress());
                ps3.setString(2, user.getPostalCode());
                ps3.setString(3, user.getCountry());
                ps3.executeUpdate();
                connection.commit();
                ps3.close();
            }
            ps2.close();
            if (count != 0) {
                return 2;
            }
            return 1;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            if (insertedIntoWishlist) {
                try {
                    String query = "DELETE FROM WISHLIST WHERE wishlistId=(?)";
                    PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                            query, false);
                    ps.setInt(1, user.getWid());
                    ps.executeUpdate();
                    connection.commit();
                    ps.close();
                    rollbackConnection();
                } catch (SQLException e2) {
                    System.out.println(EXCEPTION_TAG + " " + e2.getMessage());
                    rollbackConnection();
                }
            }
            rollbackConnection();
            return 0;
        }
    }

    public boolean insertContains(ContainsModel item) {
        try {
            String query = "INSERT INTO CONTAINS VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setInt(1, item.getWishlistId());
            ps.setInt(2, item.getBoardGameId());

            ps.executeUpdate();
            connection.commit();

            ps.close();
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
            return false;
        }
    }

    public boolean insertTransaction(TransactionModel transaction) {
        try {
            String query = "INSERT INTO TRANSACTION VALUES (?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setInt(1, transaction.getTid());
            ps.setBigDecimal(2, transaction.getAmount());
            ps.setDate(3, transaction.getDate());
            ps.setString(4, transaction.getBuyer());
            ps.setString(5, transaction.getSeller());

            ps.executeUpdate();
            connection.commit();

            ps.close();
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
            return false;
        }
    }

    public boolean checkUserCredentials(String username, String password) {
        try {
            String query = "SELECT count(*) FROM USER1 WHERE USERNAME=(?) AND PASSWORD=(?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            int result = 0;
            while (rs.next()) {
                result = rs.getInt(1);
            }
            ps.close();
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    public DefaultTableModel showUser() {
        String[] columnNames = {"Username", "Password", "Email", "Address", "Postal Code", "Country", "Wishlist ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            String query = "SELECT * FROM USER1 natural join USER2 ORDER BY WISHLISTID";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getString("USERNAME"),
                        rs.getString("PASSWORD"),
                        rs.getString("EMAIL"),
                        rs.getString("ADDRESS"),
                        rs.getString("POSTALCODE"),
                        rs.getString("COUNTRY"),
                        rs.getInt("WISHLISTID")
                };
                tableModel.addRow(row);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return tableModel;
    }

    public DefaultTableModel showWishlistItems() {
        String[] columnNames = {"Username", "Game Title", "Board Game ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            String query = "SELECT USERNAME, TITLE, BOARDGAMEID FROM " +
                    "USER1 natural join CONTAINS natural join BOARDGAME";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getString("USERNAME"),
                        rs.getString("TITLE"),
                        rs.getInt("BOARDGAMEID"),
                };
                tableModel.addRow(row);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return tableModel;
    }

    public void showPubGx(int x) {

        try {

            String query = "SELECT PublisherName, Website, COUNT(*) AS GameCount FROM PUBLISHER, BOARDGAME " +
                    "WHERE PUBLISHER.PublisherID = BOARDGAME.PublisherID " +
                    "GROUP BY PublisherName, Website " +
                    "HAVING COUNT(*) > ?";

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setInt(1, x);
            ResultSet rs = ps.executeQuery();

            if(!rs.isBeforeFirst()) {
                System.out.println("There are no publishers with released game greater than " + x);
            } else {
                while(rs.next()){
                    String publisherName = rs.getString("PublisherName");
                    String website = rs.getString("Website");
                    int gameCount = rs.getInt("GameCount");

                    System.out.println("Publisher: " + publisherName + " - Website: "
                            + website + " - Game Count: " + gameCount);
                }
            }

        } catch(SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public ArrayList<Object> showPubGx2(int x) {
        ArrayList<Object> items = new ArrayList<>();
        try {

            String query = "SELECT PublisherName, Website, COUNT(*) AS GameCount FROM PUBLISHER, BOARDGAME " +
                    "WHERE PUBLISHER.PublisherID = BOARDGAME.PublisherID " +
                    "GROUP BY PublisherName, Website " +
                    "HAVING COUNT(*) > ?";

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setInt(1, x);
            ResultSet rs = ps.executeQuery();

            if(!rs.isBeforeFirst()) {
                System.out.println("There are no publishers with released game greater than " + x);
            } else {
                while(rs.next()){
                    String publisherName = rs.getString("PublisherName");
                    items.add(publisherName);
                    String website = rs.getString("Website");
                    items.add(website);
                    int gameCount = rs.getInt("GameCount");
                    items.add(gameCount);

                }
            }

        } catch(SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return items;
    }

    public FeaturedModel[] getFeatInfo() {

        ArrayList<FeaturedModel> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM FEATURED";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FeaturedModel model = new FeaturedModel(rs.getInt("BOARDGAMEID"),
                        rs.getInt("SESSIONNUM"),
                        rs.getString("SERIESNAME"));

                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new FeaturedModel[result.size()]);
    }

    public HostsModel[] getHostsInfo() {

        ArrayList<HostsModel> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM HOSTS";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HostsModel model = new HostsModel(rs.getString("USERNAME"),
                        rs.getInt("SESSIONNUM"),
                        rs.getString("SERIESNAME"));

                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new HostsModel[result.size()]);

    }

    public GameEventModel[] getGEInfo() {

        ArrayList<GameEventModel> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM GAMEEVENT";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                GameEventModel model = new GameEventModel(
                        rs.getInt("SESSIONNUM"),
                        rs.getString("SERIESNAME"),
                        rs.getString("LOCATION"),
                        rs.getDate("GDATE"));

                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GameEventModel[result.size()]);
    }

    public GameEventSeriesModel[] getGESInfo() {

        ArrayList<GameEventSeriesModel> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM GAMEEVENTSERIES1";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                GameEventSeriesModel model = new GameEventSeriesModel(
                        rs.getString("SERIESNAME"),
                        rs.getString("LOCATION"),
                        rs.getDate("STARTDATE"),
                        rs.getDate("ENDDATE"),
                        rs.getString("CREATORNAME")
                );

                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GameEventSeriesModel[result.size()]);
    }

    public GameEventSeriesModel2[] getGES2Info() {
        ArrayList<GameEventSeriesModel2> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM GAMEEVENTSERIES2";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                GameEventSeriesModel2 model = new GameEventSeriesModel2(
                        rs.getDate("STARTDATE"),
                        rs.getDate("ENDDATE"),
                        rs.getInt("DURATION"));

                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GameEventSeriesModel2[result.size()]);


    }

    public void insertGameEventSeries(GameEventSeriesModel gesModel) {

        try {

            if (countSeriesName(gesModel.getSeriesName()) > 0) {
                System.out.println("The Series Name already exists!");
                return;
            }

            if (!isCreatorName(gesModel.getCreatorName())) {
                System.out.println("The user name does not exists");
                return;
            }

            String query = "INSERT INTO GAMEEVENTSERIES1 VALUES (?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setString(1, gesModel.getSeriesName());
            ps.setString(2, gesModel.getLocation());
            ps.setDate(3, gesModel.getStartDate());
            ps.setDate(4, gesModel.getEndDate());
            ps.setString(5, gesModel.getCreatorName());
            ps.executeUpdate();
            ps.close();

            if(isUniqueDuration(gesModel.getStartDate(), gesModel.getEndDate())) {

                Date startDate = gesModel.getStartDate();
                Date endDate = gesModel.getEndDate();

                long durationMili = Math.abs(endDate.getTime() - startDate.getTime());
                int durationDays = (int) TimeUnit.MILLISECONDS.toDays(durationMili);

                String insertGes2Query = "INSERT INTO GAMEEVENTSERIES2 VALUES (?,?,?)";
                PrintablePreparedStatement ps2 = new PrintablePreparedStatement(connection.prepareStatement(insertGes2Query),
                        query, false);
                ps2.setDate(1, startDate);
                ps2.setDate(2,endDate);
                ps2.setInt(3, durationDays);
                ps2.executeUpdate();
                ps2.close();
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    public boolean isCreatorName(String creatorName) {
        int count = 0;

        try {
            String query = "SELECT COUNT(*) FROM GAMEEVENTSERIES1 WHERE CREATORNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setString(1, creatorName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

        return count > 0;
    }

    public boolean isUserName(String userName){
        int count = 0;
        try{
            String query = "SELECT COUNT(*) FROM USER1 WHERE USERNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
                return count > 0;
            }
        }catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return count > 0;
    }

    public boolean isNotPublisherName(String publisherName) {
        int count = 0;
        try{
            String query = "SELECT COUNT(*) FROM PUBLISHER WHERE PUBLISHERNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setString(1, publisherName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
                return !(count > 0);
            }
        }catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return !(count > 0);
    }

    public int countSeriesName(String seriesName) {

        int count = 0;

        try {
            String query = "SELECT COUNT(*) FROM GAMEEVENTSERIES1 WHERE SERIESNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setString(1, seriesName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
                return count;
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

        return count;
    }

    public int countListingID() {

        int count = 0;

        try {
            String query = "SELECT COUNT(*) FROM LISTING";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
                return count;
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

        return count;
    }

    public boolean isUniqueDuration(Date startDate, Date endDate) {

        try{

            String countQuery = "SELECT COUNT(*) FROM GAMEEVENTSERIES1 WHERE StartDate = ? AND EndDate =?";
            PrintablePreparedStatement countPS = new PrintablePreparedStatement(connection.prepareStatement(countQuery),
                    countQuery, false);

            countPS.setDate(1, startDate);
            countPS.setDate(2, endDate);
            ResultSet countRS = countPS.executeQuery();

            if (countRS.next() && countRS.getInt(1) > 1) {
                countPS.close();
                countRS.close();
                return false;
            } else {
                countPS.close();
                countRS.close();
                return true;}
        }
        catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return false;
    }

    public int getEventSessionNum(String seriesName) {

        int count = 0;

        try {
            String query = "SELECT COUNT(*) FROM GAMEEVENT WHERE SERIESNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setString(1, seriesName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
                return count;
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

        return count;

    }

    public void insertGameEvent(GameEventModel gesModel) {

        try {

            String query = "INSERT INTO GAMEEVENT VALUES (?,?,?,?)";

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);

            ps.setInt(1, gesModel.getSessionNum());
            ps.setString(2, gesModel.getSeriesName());
            ps.setString(3, gesModel.getLocation());
            ps.setDate(4, gesModel.getDate());
            ps.executeUpdate();
            ps.close();

            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    public void insertListing(ListingModel listingModel) {
        try{
            String query = "INSERT INTO LISTING VALUES (?,?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setInt(1,listingModel.getLid());
            ps.setDate(2,listingModel.getDateListed());
            ps.setBigDecimal(3,listingModel.getPrice());
            ps.setString(4,listingModel.getDescription());
            ps.setInt(5,listingModel.getBoardGameID());
            ps.setString(6,listingModel.getUsername());
            ps.executeUpdate();
            ps.close();

            connection.commit();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertTournamentGame(TournamentGameModel tournamentGameModel) {
        try{
            String query = "INSERT INTO TOURNAMENTGAME VALUES (?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setInt(1,tournamentGameModel.getId());
            ps.setString(2,tournamentGameModel.getRules());
            ps.setInt(3,tournamentGameModel.getMinParticipants());
            ps.executeUpdate();
            ps.close();

            connection.commit();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertTeamGame(TeamGameModel teamGameModel){
        try{
            String query = "INSERT INTO TEAMGAME VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setInt(1,teamGameModel.getId());
            ps.setInt(2,teamGameModel.getMinTeamSize());
            ps.executeUpdate();
            ps.close();

            connection.commit();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }


    public void insertPublisher(PublisherModel publisherModel) {
        try{
            String query = "INSERT INTO PUBLISHER VALUES (?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setInt(1,publisherModel.getId());
            ps.setString(2,publisherModel.getpublisherName());
            ps.setString(3,publisherModel.getWebsite());
            ps.executeUpdate();
            ps.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }
    public void insertReview(ReviewsModel reviewModel) {
        try{
            String query = "INSERT INTO REVIEWS VALUES (?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setString(1,reviewModel.getUsername());
            ps.setInt(2,reviewModel.getBoardGameId());
            ps.setDate(3,reviewModel.getRDate());
            ps.setInt(4,reviewModel.getScore());
            ps.setString(5,reviewModel.getReview());
            ps.executeUpdate();
            ps.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public int getTotalListings() {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) FROM LISTING";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
                return count;
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return count;
    }

    public int getTotalPublishers() {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) FROM PUBLISHER";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
                return count;
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return count;
    }

    public boolean isBoardGameID(int boardGameID) {

        try{

            String countQuery = "SELECT COUNT(*) FROM BOARDGAME WHERE BOARDGAMEID = ?";
            PrintablePreparedStatement countPS = new PrintablePreparedStatement(connection.prepareStatement(countQuery),
                    countQuery, false);

            countPS.setInt(1, boardGameID);

            ResultSet countRS = countPS.executeQuery();

            if (countRS.next()) {
                int count = countRS.getInt(1);
                return count > 0;
            }
        }
        catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return false;
    }

    public boolean isTournamentGameID(int boardGameID) {
        try{

            String countQuery = "SELECT COUNT(*) FROM TOURNAMENTGAME WHERE BOARDGAMEID = ?";
            PrintablePreparedStatement countPS = new PrintablePreparedStatement(connection.prepareStatement(countQuery),
                    countQuery, false);

            countPS.setInt(1, boardGameID);

            ResultSet countRS = countPS.executeQuery();

            if (countRS.next()) {
                int count = countRS.getInt(1);
                return count > 0;
            }
        }
        catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return false;
    }

    public boolean isTeamGameID(int boardGameID) {
        try{

            String countQuery = "SELECT COUNT(*) FROM TEAMGAME WHERE BOARDGAMEID = ?";
            PrintablePreparedStatement countPS = new PrintablePreparedStatement(connection.prepareStatement(countQuery),
                    countQuery, false);

            countPS.setInt(1, boardGameID);

            ResultSet countRS = countPS.executeQuery();

            if (countRS.next()) {
                int count = countRS.getInt(1);
                return count > 0;
            }
        }
        catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return false;
    }

    public void insertFeatured(FeaturedModel featuredModel) {
        try {

            String query = "INSERT INTO FEATURED VALUES (?,?,?)";

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);

            ps.setInt(1, featuredModel.getBoardGameId());
            ps.setInt(2, featuredModel.getSessionNum());
            ps.setString(3, featuredModel.getSeriesName());
            ps.executeUpdate();
            ps.close();

            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertHosts(HostsModel hostsModel) {
        try {

            String query = "INSERT INTO HOSTS VALUES (?,?,?)";

            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);

            ps.setString(1, hostsModel.getUsername());
            ps.setInt(2, hostsModel.getSessionNum());
            ps.setString(3, hostsModel.getSeriesName());
            ps.executeUpdate();
            ps.close();

            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }


    public boolean isPublisherID(int id) {
        int count = 0;

        try {
            String query = "SELECT COUNT(*) FROM PUBLISHER WHERE PUBLISHERID = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

        return count > 0;
    }
    public boolean isPublisherName(String name) {
        int count = 0;

        try {
            String query = "SELECT COUNT(*) FROM PUBLISHER WHERE PUBLISHERNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query),
                    query, false);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

        return count > 0;
    }


    public DefaultTableModel getGESFullInfo() {

        String[] columnNames = {"Series Name", "Location", "Start Date", "End Date", "Duration", "Creator Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            String query = "SELECT g1.SERIESNAME, g1.LOCATION, g2.STARTDATE, " +
                    "g2.ENDDATE, g2.DURATION, g1.CREATORNAME FROM GAMEEVENTSERIES1 g1 " +
                    "INNER JOIN GAMEEVENTSERIES2 g2 " +
                    "ON g1.STARTDATE = g2.STARTDATE AND g1.ENDDATE = g2.ENDDATE";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getString("SERIESNAME"),
                        rs.getString("LOCATION"),
                        rs.getDate("STARTDATE"),
                        rs.getDate("ENDDATE"),
                        rs.getInt("DURATION"),
                        rs.getString("CREATORNAME")
                };

                tableModel.addRow(row);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return tableModel;
    }
}

