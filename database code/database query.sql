-- display all e board officers in programming club from florida polytechnic univeristy
select concat(u.userFirstName, " ", u.userLastName) "Student Name", uc.role "E-Board Position", u.userEmail
from USERS u, CLUB c, USER_CLUB uc
where c.clubID=uc.clubID and uc.userID=u.userID and c.clubID=1 and uc.role NOT IN ('Member', 'Other');

-- select clubs with user riley
select c.clubID, c.clubName "Club Name", c.clubDesc
from CLUB c, USER_CLUB uc, USERS u
where u.userEmail="rkiefer7196@floridapoly.edu" and u.password="ABC" and u.userID=uc.userID and uc.clubID=c.clubID;

-- select all club's events with user riley
select e.eventName, e.eventDesc, e.eventLocation, e.eventStartDate, e.eventEndDate
from CLUB c, USER_CLUB uc, USERS u, EVENT e
where u.userEmail="rkiefer7196@floridapoly.edu" and u.password="ABC" and u.userID=uc.userID and uc.clubID=c.clubID and c.clubID=e.clubID;

-- select specific club's events with user riley
select e.eventName, e.eventDesc, e.eventLocation, e.eventStartDate, e.eventEndDate
from CLUB c, USER_CLUB uc, USERS u, EVENT e
where u.userEmail="rkiefer7196@floridapoly.edu" and u.password="ABC" and u.userID=uc.userID and uc.clubID=c.clubID and c.clubID=e.clubID and uc.clubID=1;

-- select top 5 upcoming events with user riley
select e.eventName, e.eventDesc, e.eventLocation, e.eventStartDate, e.eventEndDate
from CLUB c, USER_CLUB uc, USERS u, EVENT e
where u.userEmail="rkiefer7196@floridapoly.edu" and u.password="ABC" and u.userID=uc.userID and uc.clubID=c.clubID and e.clubID=c.clubID
order by e.eventStartDate
limit 5;

