DROP TABLE CONTAINS;
DROP TABLE FEATURED;
DROP TABLE REVIEWS;
DROP TABLE HOSTS;
DROP TABLE TeamGame;
DROP TABLE TournamentGame;
DROP TABLE GameEvent;
DROP TABLE Transaction;
DROP TABLE USER2;
DROP TABLE GAMEEVENTSERIES2;
DROP TABLE LISTING;
DROP TABLE BoardGame;
DROP TABLE Publisher;
DROP TABLE GAMEEVENTSERIES1;
DROP TABLE USER1;
DROP TABLE WISHLIST;

CREATE TABLE Publisher
(
    PublisherID   INTEGER PRIMARY KEY,
    PublisherName VARCHAR(50) NOT NULL UNIQUE,
    Website       VARCHAR(50)
);

CREATE TABLE BoardGame
(
    BoardGameID INTEGER PRIMARY KEY,
    Title       VARCHAR(20) NOT NULL,
    Genre       VARCHAR(10),
    Description VARCHAR(300),
    PublisherID INTEGER,
    FOREIGN KEY (PublisherID) REFERENCES Publisher
        ON DELETE SET NULL
);

CREATE TABLE TeamGame
(
    BoardGameID INTEGER PRIMARY KEY,
    MinTeamSize INTEGER NOT NULL
);
CREATE TABLE TournamentGame
(
    BoardGameID     INTEGER PRIMARY KEY,
    TournamentRules VARCHAR(300),
    MinParticipants INTEGER NOT NULL
);


CREATE TABLE Wishlist
(
    WishlistID  INTEGER PRIMARY KEY,
    DateUpdated DATE NOT NULL
);

CREATE TABLE User1
(
    Username   VARCHAR(20) PRIMARY KEY,
    Password   VARCHAR(20) NOT NULL,
    Email      VARCHAR(20) NOT NULL UNIQUE,
    WishlistID INTEGER UNIQUE,
    Address    VARCHAR(20) NOT NULL,
    PostalCode VARCHAR(6)  NOT NULL,
    FOREIGN KEY (WishlistID) REFERENCES Wishlist
        ON DELETE SET NULL
);

CREATE TABLE User2
(
    Address    VARCHAR(20),
    PostalCode VARCHAR(6),
    Country    VARCHAR(20) NOT NULL,
    PRIMARY KEY (Address, PostalCode)
);

CREATE TABLE Listing
(
    ListingID   INTEGER PRIMARY KEY,
    DateListed  DATE    NOT NULL,
    Price       DECIMAL NOT NULL,
    Description VARCHAR(300),
    BoardGameID INTEGER,
    Username    VARCHAR(20),
    FOREIGN KEY (BoardGameID) REFERENCES BoardGame
        ON DELETE CASCADE,
    FOREIGN KEY (Username) REFERENCES User1
        ON DELETE CASCADE
);

CREATE TABLE Transaction
(
    TransactionID INTEGER PRIMARY KEY,
    Amount        DECIMAL NOT NULL,
    TDate         DATE    NOT NULL,
    Buyer         VARCHAR(20),
    Seller        VARCHAR(20),
    FOREIGN KEY (Buyer) REFERENCES User1 (Username)
        ON DELETE SET NULL,
    FOREIGN KEY (Seller) REFERENCES User1 (Username)
        ON DELETE SET NULL
);

CREATE TABLE GameEventSeries1
(
    SeriesName  VARCHAR(50) PRIMARY KEY,
    Location    VARCHAR(50) NOT NULL,
    StartDate   DATE        NOT NULL,
    EndDate     DATE        NOT NULL,
    CreatorName VARCHAR(20),
    FOREIGN KEY (CreatorName) REFERENCES User1 (Username)
        ON DELETE SET NULL
);

CREATE TABLE GameEventSeries2
(
    StartDate DATE,
    EndDate   DATE,
    Duration  INTEGER,
    PRIMARY KEY (StartDate, EndDate)
);


CREATE TABLE GameEvent
(
    SessionNum INTEGER,
    SeriesName VARCHAR(50) NOT NULL,
    Location   VARCHAR(50) NOT NULL,
    GDate      DATE        NOT NULL,
    PRIMARY KEY (SessionNum, SeriesName),
    FOREIGN KEY (SeriesName) REFERENCES GameEventSeries1
        ON DELETE CASCADE
);

CREATE TABLE Contains
(
    WishlistID  INTEGER,
    BoardGameID INTEGER,
    PRIMARY KEY (WishlistID, BoardGameID),
    FOREIGN KEY (WishlistID) REFERENCES Wishlist
        ON DELETE SET NULL,
    FOREIGN KEY (BoardGameID) REFERENCES BoardGame
        ON DELETE SET NULL
);

CREATE TABLE Hosts
(
    Username   VARCHAR(20),
    SessionNum INTEGER,
    SeriesName VARCHAR(50),
    PRIMARY KEY (Username, SessionNum, SeriesName),
    FOREIGN KEY (Username) REFERENCES User1
        ON DELETE SET NULL,
    FOREIGN KEY (SessionNum, SeriesName) REFERENCES GameEvent (SessionNum, SeriesName)
        ON DELETE CASCADE
);

CREATE TABLE Featured
(
    BoardGameID INTEGER,
    SessionNum  INTEGER,
    SeriesName  VARCHAR(50),
    PRIMARY KEY (SeriesName, SessionNum),
    FOREIGN KEY (BoardGameID) REFERENCES BoardGame
        ON DELETE SET NULL,
    FOREIGN KEY (SessionNum, SeriesName) REFERENCES GameEvent
        ON DELETE CASCADE
);

CREATE TABLE Reviews
(
    Username    VARCHAR(20),
    BoardGameID INTEGER,
    RDate       DATE NOT NULL,
    Score       INTEGER NOT NULL,
    Review      VARCHAR(500),
    PRIMARY KEY (Username, BoardGameID),
    FOREIGN KEY (Username) REFERENCES User1
        ON DELETE SET NULL,
    FOREIGN KEY (BoardGameID) REFERENCES BoardGame
        ON DELETE SET NULL
);

INSERT
INTO Publisher(PublisherID, PublisherName, Website)
VALUES (1, 'GoodGameCompany', NULL);

INSERT
INTO Publisher(PublisherID, PublisherName, Website)
VALUES (2, 'OKGameCompany', 'www.we-ok.com');

INSERT
INTO Publisher(PublisherID, PublisherName, Website)
VALUES (3, 'BadGameCompany', 'wwww.we-bad.com');

INSERT
INTO Publisher(PublisherID, PublisherName, Website)
VALUES (4, 'GreatGameCompany', 'www.greatcompany.com');

INSERT
INTO Publisher(PublisherID, PublisherName, Website)
VALUES (5, 'ExcellentGameComapny', 'www.excellentgame.com');

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (1, 'Chess', 'Strategy', 'A western two-player strategy board game.', 1);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (2, 'Shogi', 'Strategy', 'An asian two-player strategy board game.', 2);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (3, 'Go’', 'Strategy', 'An asian two-player strategy board game.', 3);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (4, 'Mahjong', 'Strategy', 'A four-player strategy board game.', 4);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (5, 'Backgammon', 'Strategy', 'A two-player strategy board game.', 5);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (6, 'D&D: Curse of Straud', 'RPG', 'Role playing fantasy game.', 1);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (7, 'Team UNO’', 'Strategy', 'A western strategy board game.', 2);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (8, 'Team Chess’', 'Strategy', 'A western two-player strategy board game.', 3);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (9, 'Team Poker’', 'Strategy', 'A western two-player strategy board game.', 4);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (10, 'Call of Cthulhu', 'RPG', 'Survive in this horror RPG!.', 5);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (11, 'Tournament Chess', 'Strategy', 'A western two-player strategy board game.', 1);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (12, 'Tournament Shogi', 'Strategy', 'An asian two-player strategy board game.', 2);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (13, 'Tournament Go', 'Strategy', 'A western two-player strategy board game.', 3);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (14, 'Tournament Othello', 'Strategy', 'A western two-player strategy board game.', 4);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (15, 'Tournament Twister', 'Physical', 'A western physical board game.', 5);

INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (16, 'Ride Shelly to Mid', 'Strategy', 'A western two-player strategy board game.', 1);
INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (17, 'Run down mid', 'Strategy', 'A western two-player strategy board game.', 1);
INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (18, 'Blame Team', 'Strategy', 'A western two-player strategy board game.', 1);
INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (19, 'Bait Baron', 'Strategy', 'A western two-player strategy board game.', 1);
INSERT
INTO BoardGame(BoardGameID, Title, Genre, Description, PublisherID)
VALUES (20, 'Nexus Blitz', 'Strategy', 'A western two-player strategy board game.', 1);

INSERT
INTO TeamGame(BoardGameID, MinTeamSize)
VALUES (6, 4);

INSERT
INTO TeamGame(BoardGameID, MinTeamSize)
VALUES (7, 5);

INSERT
INTO TeamGame(BoardGameID, MinTeamSize)
VALUES (8, 6);

INSERT
INTO TeamGame(BoardGameID, MinTeamSize)
VALUES (9, 7);

INSERT
INTO TeamGame(BoardGameID, MinTeamSize)
VALUES (10, 8);

INSERT
INTO TournamentGame(BoardGameID, TournamentRules, MinParticipants)
VALUES (11, NULL, 4);

INSERT
INTO TournamentGame(BoardGameID, TournamentRules, MinParticipants)
VALUES (12, 'Must use own game piece', 8);

INSERT
INTO TournamentGame(BoardGameID, TournamentRules, MinParticipants)
VALUES (13, 'Please shower before coming', 16);

INSERT
INTO TournamentGame(BoardGameID, TournamentRules, MinParticipants)
VALUES (14, 'You can use any color of game pieces - not just black and white', 32);

INSERT
INTO TournamentGame(BoardGameID, TournamentRules, MinParticipants)
VALUES (15, 'Please be presentable', 8);

INSERT
INTO Wishlist(WishlistID, DateUpdated)
Values (1, '2023-10-20');

INSERT
INTO Wishlist(WishlistID, DateUpdated)
Values (2, '2023-10-20');

INSERT
INTO Wishlist(WishlistID, DateUpdated)
Values (3, '2023-10-20');

INSERT
INTO Wishlist(WishlistID, DateUpdated)
Values (4, '2023-10-20');

INSERT
INTO Wishlist(WishlistID, DateUpdated)
Values (5, '2023-10-20');

INSERT
INTO Wishlist(WishlistID, DateUpdated)
Values (6, '2023-10-20');

INSERT
INTO Wishlist(WishlistID, DateUpdated)
Values (7, '2023-10-20');

INSERT
INTO User1(Username, Email, Password, WishListID, Address, PostalCode)
VALUES ('NickTheScammer', 'scammer@scam.com', 'password1234', 1, '1234 Del Street',
        'A1A1A1');
INSERT
INTO User1(Username, Email, Password, WishListID, Address, PostalCode)
VALUES ('Mike', 'ppork@outlook.com', 'monkey', 2, '999 Bait Street', 'B2B2B2');

INSERT
INTO User1(Username, Email, Password, WishListID, Address, PostalCode)
VALUES ('BaldEagle', 'patriot@america.ru', 'America', 3, '98 House Street', '634000');

INSERT
INTO User1(Username, Email, Password, WishListID, Address, PostalCode)
VALUES ('MyName', 'generic@a.com', 'password', 4, '12 Up Street', 'A3A3A3');

INSERT
INTO User1(Username, Email, Password, WishListID, Address, PostalCode)
VALUES ('NickTheNiceGuy', 'scammer2@scam.com', 'password', 5, '1234 Del Street',
        'A1A1A1');

INSERT
INTO User1(Username, Email, Password, WishListID, Address, PostalCode)
VALUES ('Hi', 'hi@hi.com', 'password', 6, '12 12 Ave', 'B1B1B1');

INSERT
INTO User1(Username, Email, Password, WishListID, Address, PostalCode)
VALUES ('Bye', 'bye@bye.com', 'password', 7, '12 Bird Ave', 'B3B3B3');

INSERT
INTO User2(Address, PostalCode, Country)
VALUES ('1234 Del Street', 'A1A1A1', 'Canada');

INSERT
INTO User2(Address, PostalCode, Country)
VALUES ('999 Bait Street', 'B2B2B2', 'Canada');

INSERT
INTO User2(Address, PostalCode, Country)
VALUES ('98 House Street', '634000', 'Russia');

INSERT
INTO User2(Address, PostalCode, Country)
VALUES ('12 Up Street', 'A3A3A3', 'Canada');

INSERT
INTO User2(Address, PostalCode, Country)
VALUES ('12 12 Ave', 'B1B1B1', 'Canada');

INSERT
INTO User2(Address, PostalCode, Country)
VALUES ('12 Bird Ave', 'B3B3B3', 'Canada');

INSERT
INTO Listing(ListingID, DateListed, Price, Description, BoardGameID, Username)
VALUES (1, '2023-01-11', 13.00, NULL, 1, 'NickTheScammer');

INSERT
INTO Listing(ListingID, DateListed, Price, Description, BoardGameID, Username)
VALUES (2, '2023-01-11', 320.10, NULL, 7, 'Hi');

INSERT
INTO Listing(ListingID, DateListed, Price, Description, BoardGameID, Username)
VALUES (3, '2022-01-11', 23.00, NULL, 4, 'Mike');

INSERT
INTO Listing(ListingID, DateListed, Price, Description, BoardGameID, Username)
VALUES (4, '2021-01-11', 6.00, NULL, 14, 'NickTheScammer');

INSERT
INTO Listing(ListingID, DateListed, Price, Description, BoardGameID, Username)
VALUES (5, '1994-06-01', 12.00, NULL, 2, 'NickTheNiceGuy');

INSERT
INTO Transaction(TransactionID, AMOUNT, Buyer, Seller, TDate)
VALUES (1, 13.00, 'NickTheScammer', 'Bye', '2023-11-11');

INSERT
INTO Transaction(TransactionID, AMOUNT, Buyer, Seller, TDate)
VALUES (2, 320.10, 'Hi', 'BaldEagle', '2023-11-21');

INSERT
INTO Transaction(TransactionID, AMOUNT, Buyer, Seller, TDate)
VALUES (3, 23.00, 'Mike', 'Bye', '2022-11-11');

INSERT
INTO Transaction(TransactionID, AMOUNT, Buyer, Seller, TDate)
VALUES (4, 6.00, 'NickTheScammer', 'Mike', '2022-12-11');

INSERT
INTO Transaction(TransactionID, AMOUNT, Buyer, Seller, TDate)
VALUES (5, 12.00, 'NickTheNiceGuy', 'MyName', '2021-11-11');

INSERT
INTO GameEventSeries1(SeriesName, Location, StartDate, EndDate, CreatorName)
VALUES ('Faker''s Series', 'ICCS X350', '2020-10-11', '2021-10-11', 'NickTheScammer');

INSERT
INTO GameEventSeries1(SeriesName, Location, StartDate, EndDate, CreatorName)
VALUES ('Guma''s Series', 'ICCS X350', '2020-10-11', '2021-10-11', 'NickTheScammer');

INSERT
INTO GameEventSeries1(SeriesName, Location, StartDate, EndDate, CreatorName)
VALUES ('Maru''s Series', 'ICCS X360', '2022-10-11', '2023-10-11', 'Mike');

INSERT
INTO GameEventSeries1(SeriesName, Location, StartDate, EndDate, CreatorName)
VALUES ('Boxer''s Series', 'ICCS X370', '2023-10-11', '2024-10-11', 'BaldEagle');

INSERT
INTO GameEventSeries1(SeriesName, Location, StartDate, EndDate, CreatorName)
VALUES ('Beryl''s Series', 'SWING 350', '2020-10-11', '2021-10-12', 'MyName');

INSERT
INTO GameEventSeries1(SeriesName, Location, StartDate, EndDate, CreatorName)
VALUES ('Shoemaker''s Series', 'SWING 400', '2023-10-11', '2023-11-11', 'NickTheNiceGuy');

INSERT
INTO GameEventSeries2(StartDate, EndDate, Duration)
VALUES ('2020-10-11', '2021-10-11', 365);

INSERT
INTO GameEventSeries2(StartDate, EndDate, Duration)
VALUES ('2022-10-11', '2023-10-11', 365);

INSERT
INTO GameEventSeries2(StartDate, EndDate, Duration)
VALUES ('2023-10-11', '2024-10-11', 365);

INSERT
INTO GameEventSeries2(StartDate, EndDate, Duration)
VALUES ('2020-10-11', '2021-10-12', 366);

INSERT
INTO GameEventSeries2(StartDate, EndDate, Duration)
VALUES ('2023-10-11', '2023-11-11', 30);

INSERT
INTO GameEvent(SessionNum, SeriesName, Location, GDate)
VALUES (1, 'Faker''s Series', 'IKB 300', '2020-10-12');

INSERT
INTO GameEvent(SessionNum, SeriesName, Location, GDate)
VALUES (2, 'Faker''s Series', 'ICCS X350', '2020-10-19');

INSERT
INTO GameEvent(SessionNum, SeriesName, Location, GDate)
VALUES (3, 'Faker''s Series', '1234 University Road', '2020-11-12');

INSERT
INTO GameEvent(SessionNum, SeriesName, Location, GDate)
VALUES (4, 'Faker''s Series', 'Roger''s Arena', '2020-12-10');

INSERT
INTO GameEvent(SessionNum, SeriesName, Location, GDate)
VALUES (1, 'Shoemaker''s Series', 'SWING 400', '2023-10-18');

INSERT
INTO Contains(WishlistID, BoardGameId)
VALUES (1, 1);

INSERT
INTO Contains(WishlistID, BoardGameId)
VALUES (2, 7);

INSERT
INTO Contains(WishlistID, BoardGameId)
VALUES (3, 8);

INSERT
INTO Contains(WishlistID, BoardGameId)
VALUES (4, 9);

INSERT
INTO Contains(WishlistID, BoardGameId)
VALUES (5, 2);

INSERT
INTO Contains(WishlistID, BoardGameId)
VALUES (6, 1);

INSERT
INTO Contains(WishlistID, BoardGameId)
VALUES (2, 3);

INSERT
INTO Hosts(Username, SessionNum, SeriesName)
Values ('Mike', 1, 'Faker''s Series');

INSERT
INTO Hosts(Username, SessionNum, SeriesName)
Values ('Mike', 2, 'Faker''s Series');

INSERT
INTO Hosts(Username, SessionNum, SeriesName)
Values ('BaldEagle', 3, 'Faker''s Series');

INSERT
INTO Hosts(Username, SessionNum, SeriesName)
Values ('MyName', 4, 'Faker''s Series');

INSERT
INTO Hosts(Username, SessionNum, SeriesName)
Values ('NickTheNiceGuy', 1, 'Shoemaker''s Series');

INSERT
INTO Featured(BoardGameID, SessionNum, SeriesName)
Values (1, 1, 'Faker''s Series');

INSERT
INTO Featured(BoardGameID, SessionNum, SeriesName)
Values (2, 2, 'Faker''s Series');

INSERT
INTO Featured(BoardGameID, SessionNum, SeriesName)
Values (3, 3, 'Faker''s Series');

INSERT
INTO Featured(BoardGameID, SessionNum, SeriesName)
Values (4, 4, 'Faker''s Series');

INSERT
INTO Featured(BoardGameID, SessionNum, SeriesName)
Values (5, 1, 'Shoemaker''s Series');

INSERT
INTO Reviews(Username, BoardGameID, RDate, Score, Review)
VALUES ('Mike', 1, '2023-1-14', 0, 'Trash Game');

INSERT
INTO Reviews(Username, BoardGameID, RDate, Score, Review)
VALUES ('MyName', 1, '2023-1-16', 0, 'I lost my job over this');

INSERT
INTO Reviews(Username, BoardGameID, RDate, Score, Review)
VALUES ('NickTheScammer', 1, '2023-1-14', 10, 'I won, it is good');

INSERT
INTO Reviews(Username, BoardGameID, RDate, Score, Review)
VALUES ('Mike', 7, '2023-1-14', 6, 'It is meh');

INSERT
INTO Reviews(Username, BoardGameID, RDate, Score, Review)
VALUES ('Bye', 12, '2022-12-09', 3, NULL);

COMMIT;