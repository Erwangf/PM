
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Erwan Giry-Fouquet
 *         M1 Informatique Université Lyon 2
 */


public class Tweet {
    private String user;
    private String username;
    private Timestamp timeStamp;
    private String content;
    private int nbResponses;
    private int nbRetweets;
    private int nbLikes;



    public Tweet(String user, String username, Timestamp timeStamp, String content, int nbResponses, int nbRetweets, int nbLikes) {
        this.user = user;
        this.username = username;
        this.timeStamp = timeStamp;
        this.content = content;
        this.nbResponses = nbResponses;
        this.nbRetweets = nbRetweets;
        this.nbLikes = nbLikes;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "user='" + user + '\'' +
                ", username='" + username + '\'' +
                ", timeStamp=" + timeStamp +
                ", content='" + content + '\'' +
                ", nbResponses=" + nbResponses +
                ", nbRetweets=" + nbRetweets +
                ", nbLikes=" + nbLikes +
                '}';
    }



    /*================================================================================================*/
    //            GETTERS & SETTERS
    /*================================================================================================*/


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNbResponses() {
        return nbResponses;
    }

    public void setNbResponses(int nbResponses) {
        this.nbResponses = nbResponses;
    }

    public int getNbRetweets() {
        return nbRetweets;
    }

    public void setNbRetweets(int nbRetweets) {
        this.nbRetweets = nbRetweets;
    }

    public int getNbLikes() {
        return nbLikes;
    }

    public void setNbLikes(int nbLikes) {
        this.nbLikes = nbLikes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

}
