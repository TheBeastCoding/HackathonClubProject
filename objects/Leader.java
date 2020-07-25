package objects;

import java.io.Serializable;

public class Leader implements Serializable {
    String leaderName;
    String leaderRole;
    String leaderEmail;

    public Leader(String leaderName, String leaderRole, String leaderEmail) {
        this.leaderName = leaderName;
        this.leaderRole = leaderRole;
        this.leaderEmail = leaderEmail;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String getLeaderRole() {
        return leaderRole;
    }

    public String getLeaderEmail() {
        return leaderEmail;
    }
}
