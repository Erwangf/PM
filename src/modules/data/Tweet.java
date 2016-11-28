package modules.data;

import java.sql.Timestamp;

import twitter4j.Status;


public class Tweet {
    private String user;
    private String tweetId;
    private String username;
    private Timestamp timeStamp;
    private String content;
    private int nbResponses;
    private int nbRetweets;
    private int nbLikes;
    private String originalTweetd;
    private String inReplyToTweetId;
    private Float note;


    /*================================================================================================*/
    //            CONSTRUCTORS
    /*================================================================================================*/


    public Tweet(Status status) {
        this.user = status.getUser().getScreenName();
        this.tweetId = Long.toString(status.getId());
        this.username = status.getUser().getName();
        this.timeStamp = new Timestamp(status.getCreatedAt().getTime());
        this.content = status.getText().replace("\"", "");
        this.nbResponses = 0;
        this.nbRetweets = status.getRetweetCount();
        this.nbLikes = status.getFavoriteCount();
    }

    public Tweet(String user, String tweetId, String username, Timestamp timeStamp, String content, int nbResponses,
                 int nbRetweets, int nbLikes) {
        super();
        this.user = user;
        this.tweetId = tweetId;
        this.username = username;
        this.timeStamp = timeStamp;
        this.content = content.replace("\"", "");
        this.nbResponses = nbResponses;
        this.nbRetweets = nbRetweets;
        this.nbLikes = nbLikes;

    }

    /*================================================================================================*/
    //            FUNCTIONS & METHODS
    /*================================================================================================*/

    @Override
    public String toString() {
        return "Tweet{" +
                "user='" + user + '\'' +
                ", tweetId='" + tweetId + '\'' +
                ", username='" + username + '\'' +
                ", timeStamp=" + timeStamp +
                ", content='" + content + '\'' +
                ", nbResponses=" + nbResponses +
                ", nbRetweets=" + nbRetweets +
                ", nbLikes=" + nbLikes +
                ", originalTweetd='" + originalTweetd + '\'' +
                ", inReplyToTweetId='" + inReplyToTweetId + '\'' +
                ", note=" + note +
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

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
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

    public String getOriginalTweetd() {
        return originalTweetd;
    }

    public void setOriginalTweetd(String originalTweetd) {
        this.originalTweetd = originalTweetd;
    }

    public String getInReplyToTweetId() {
        return inReplyToTweetId;
    }

    public void setInReplyToTweetId(String inReplyToTweetId) {
        this.inReplyToTweetId = inReplyToTweetId;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float note) {
        this.note = note;
    }
}
