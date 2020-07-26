CREATE TABLE UNIVERSITY (
	universityID int AUTO_INCREMENT NOT NULL,
	universityName varchar(30) NOT NULL,
	universityStreet varchar(30) NOT NULL,
	universityCity varchar(15) NOT NULL,
	universityState char(2) NOT NULL,
	universityZip char(5) NOT NULL,
	PRIMARY KEY(universityID)
);

CREATE TABLE CLUB (
	clubID int AUTO_INCREMENT NOT NULL,
	clubName varchar(35) NOT NULL,
	clubDesc varchar(150) NOT NULL,
	clubWebsite varchar(25) DEFAULT "N/A",
	universityID int,
	PRIMARY KEY(clubID),
	FOREIGN KEY(universityID) REFERENCES UNIVERSITY(universityID)
);
	
CREATE TABLE EVENT (
	eventID int AUTO_INCREMENT NOT NULL,
	eventName varchar(25) NOT NULL,
	eventDesc varchar(50) NOT NULL,
	eventLocation varchar(25) NOT NULL,
	eventStartDate datetime NOT NULL,
	eventEndDate datetime NOT NULL,
	clubID int,
	PRIMARY KEY(eventID),
	FOREIGN KEY(clubID) REFERENCES CLUB(clubID)
);

CREATE TABLE USERS (
	userID int AUTO_INCREMENT NOT NULL,
	userFirstName varchar(20) 	NOT NULL,
	userLastName varchar(20) NOT NULL,
	userEmail varchar(254) NOT NULL,
	universityID int NOT NULL,
	password varchar(16) NOT NULL,
	PRIMARY KEY(userID),
	FOREIGN KEY(universityID) references UNIVERSITY(universityID)
);

CREATE TABLE USER_CLUB (
	userID int NOT NULL,
	clubID int NOT NULL,
	role enum("President", "Vice President", "Secretary", "Treasurer", "Advisor","Member","Other") NOT NULL,	
	FOREIGN KEY(userID) references USERS(userID),
	FOREIGN KEY(clubID) references CLUB(clubID)
);

-- drop tables
drop table USER_CLUB;
drop table EVENT;
drop table CLUB;
drop table users;
drop table UNIVERSITY;

-- describe tables
describe USER_CLUB;
describe USERS;
describe EVENT;
describe CLUB;
describe UNIVERSITY;

-- select *
select * from USERS;
select * from USER_CLUB;
select * from EVENT;
select * from CLUB;
select * from UNIVERSITY;

-- test data

-- insert test university
insert into UNIVERSITY
(universityName,universityStreet,universityCity,universityState,universityZip)
values
("Florida Polytechnic University","4700 Research Way","Lakeland","FL","33584");

-- save test university ID
set @var_universityID1=LAST_INSERT_ID();

-- insert test club
insert into CLUB
(clubName, clubDesc, universityID)
values
("Programming Club", "Learn to program.", @var_universityID1);

set @var_clubID1=LAST_INSERT_ID();

-- insert into leader
insert into USERS
(userLastName,userFirstName,userEmail, universityID, password)
values
("Placeres","Jose","jplaceres3559@floridapoly.edu", @var_universityID1, "abc");

set @var_leaderID1=LAST_INSERT_ID();

insert into USER_CLUB
(userID, clubID, role)
values
(@var_leaderID1, @var_clubID1, "President");

insert into USERS
(userLastName,userFirstName,userEmail, universityID, password)
values
("Crosswait","Andrew","acrosswait5466@floridapoly.edu", @var_universityID1, "abc");

set @var_leaderID2=LAST_INSERT_ID();

insert into USER_CLUB
(userID, clubID, role)
values
(@var_leaderID2, @var_clubID1, "Vice President");

insert into USERS
(userLastName,userFirstName,userEmail, universityID, password)
values
("Coy","Ruel","rcoy6832@floridapoly.edu", @var_universityID1, "abc");

set @var_leaderID3=LAST_INSERT_ID();

insert into USER_CLUB
(userID, clubID, role)
values
(@var_leaderID3, @var_clubID1, "Treasurer");

insert into USERS
(userLastName,userFirstName,userEmail, universityID, password)
values
("Lad","Mihir","mlad7113@floridapoly.edu", @var_universityID1, "abc");

set @var_leaderID4=LAST_INSERT_ID();

insert into USER_CLUB
(userID, clubID, role)
values
(@var_leaderID4, @var_clubID1, "Secretary");

insert into USERS
(userLastName,userFirstName,userEmail, universityID, password)
values
("Kiefer","Riley","rkiefer7196@floridapoly.edu", @var_universityID1, "abc");

set @var_leaderID5=LAST_INSERT_ID();

insert into USER_CLUB
(userID, clubID, role)
values
(@var_leaderID5, @var_clubID1, "Member");



-- insert swim club
insert into CLUB
(clubName, clubDesc, universityID)
values
("Swim Club", "Train and compete in swim competitions", 1);

set @var_clubID2=LAST_INSERT_ID();

-- insert swim club leaders
insert into USERS
(userLastName,userFirstName,userEmail, universityID, password)
values
("Townsend","Vanessa","vtownsend7317@floridapoly.edu",1,"123");

set @var_userID6=LAST_INSERT_ID();

insert into USER_CLUB
(userID, clubID, role)
values
(@var_userID6, @var_clubID2, "President");

insert into USERS
(userLastName,userFirstName,userEmail, universityID, password)
values
("Zuniga","Isabella","izuniga@floridapoly.edu",1,"123");

set @var_userID7=LAST_INSERT_ID();

insert into USER_CLUB
(userID, clubID, role)
values
(@var_userID7, @var_clubID2, "Vice President");

insert into USERS
(userLastName,userFirstName,userEmail, universityID, password)
values
("Fowler","Gervonte","gfowler3204@floridapoly.edu",1,"123");

set @var_userID8=LAST_INSERT_ID();

insert into USER_CLUB
(userID, clubID, role)
values
(@var_userID8, @var_clubID2, "Treasurer");

insert into USERS
(userLastName,userFirstName,userEmail, universityID, password)
values
("Byerly","William","wbyerly4518@floridapoly.edu",1,"123");

set @var_userID9=LAST_INSERT_ID();

insert into USER_CLUB
(userID, clubID, role)
values
(@var_userID9, @var_clubID2, "Secretary");

-- add riley to swim club
insert into USER_CLUB
(userID, clubID, role)
values
(5, 2, "Member");


-- insert another test club
insert into CLUB
(clubName, clubDesc, universityID)
values
("The Force Institute", "Learn to use the force.", 1);

insert into USER_CLUB
(userID, clubID, role)
values
(10, 4, "President");

-- insert another test club
insert into CLUB
(clubName, clubDesc, universityID)
values
("The Hammock Club", "Learn to use a hammock.", 1);

insert into USER_CLUB
(userID, clubID, role)
values
(2, 5, "President");

-- insert test club
insert into CLUB
(clubName, clubDesc, universityID)
values
("Drumline", "Learn to play percussion.", @var_universityID1);

set @var_clubID3=LAST_INSERT_ID();

-- insert drumline club leaders
insert into USERS
(userLastName,userFirstName,userEmail, universityID, password)
values
("Kim","Joshua","jkim6465@floridapoly.edu",1,"420");

set @var_userID10=LAST_INSERT_ID();

insert into USER_CLUB
(userID, clubID, role)
values
(@var_userID10, @var_clubID3, "President");

insert into USER_CLUB
(userID, clubID, role)
values
(5, @var_clubID3, "Vice President");

-- event
insert into EVENT
(eventID, eventName, eventDesc, eventLocation, eventStartDate, eventEndDate, clubID)
values
(1, "Hackathon", "Implement new ideas.", "IST Second Floor","2020-01-01 00:00:00", "2020-01-01 23:59:58",1);

-- swim event
insert into EVENT
(eventName, eventDesc, eventLocation, eventStartDate, eventEndDate, clubID)
values
("Swim Competition", "Compete for prizes", "SDC","2020-01-02 00:00:00", "2020-01-02 23:59:58",2);

-- programing event
insert into EVENT
(eventName, eventDesc, eventLocation, eventStartDate, eventEndDate, clubID)
values
("Programming Elections", "Choose next semester's leaders", "IST-1024","2020-01-05 00:00:00", "2020-01-05 23:59:58",1);

-- drumline event
insert into EVENT
(eventName, eventDesc, eventLocation, eventStartDate, eventEndDate, clubID)
values
("Drumline Practice", "Learn Jig 2", "SDC","2020-01-03 00:00:00", "2020-01-03 23:59:58",3);

-- drumline event
insert into EVENT
(eventName, eventDesc, eventLocation, eventStartDate, eventEndDate, clubID)
values
("Drumline Performance", "Play for the wellnes center staff", "SDC","2020-01-07 00:00:00", "2020-01-07 23:59:58",3);