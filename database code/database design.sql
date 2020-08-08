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

-- Quick command listing

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
