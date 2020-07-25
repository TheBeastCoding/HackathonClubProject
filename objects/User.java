package objects;

import java.io.Serializable;

public class User implements Serializable {
    String firstName;
    String lastName;
    String universityEmail;
    String universityName;
    Integer universityID;

    public User(String firstName, String lastName, String universityEmail, String universityName, Integer universityID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.universityEmail = universityEmail;
        this.universityName = universityName;
        this.universityID = universityID;
    }

    public Integer getUniversityID() {
        return universityID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUniversityEmail() {
        return universityEmail;
    }

    public String getUniversityName() {
        return universityName;
    }
}
