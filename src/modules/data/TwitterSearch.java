package modules.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;

public class TwitterSearch {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        String r = sb.toString();
        sb.setLength(0);
        return r;
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        /*
		 * connection.getHeaderFields().forEach(( headerName, headerValue)->{ connection.setRequestProperty(headerName,null); });
		 */
        connection.setRequestProperty("Accept-Charset", "utf-8");
        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("x-overlay-request", "true");

        connection.setRequestMethod("GET");

        InputStreamReader ISR = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));
        BufferedReader rd = new BufferedReader(ISR);
        String jsonText = readAll(rd);

        ISR.close();
        rd.close();

        return new JSONObject(jsonText);
    }


    private static ArrayList<Tweet> getRetweetById(String id) {
        ArrayList<Tweet> res = new ArrayList<Tweet>();
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            ArrayList<Status> statuses = (ArrayList<Status>) (twitter.getRetweets(Long.parseLong(id)));
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                Tweet t = new Tweet((Status) status);
                t.setOriginalTweetd(id);
                res.add(t);
            }
            System.out.println("done.");
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get retweets: " + te.getMessage());
            System.exit(-1);
        }
        return res;
    }


    public static ArrayList<Tweet> getRepliesFromTweet(Tweet t) throws IOException, JSONException {
        String url = "https://twitter.com/" + t.getUsername() + "/status/" + t.getTweetId();
        JSONObject json = readJsonFromUrl(url); // we get json object
        String items_html = (String) json.get("page");
        Document doc = Jsoup.parse(items_html);
        Elements tweets_block = doc.select(".js-stream-tweet");
        ArrayList<Tweet> result = new ArrayList<Tweet>();
        for (Element e : tweets_block) {
            String id = e.attr("data-item-id");
            // user :
            String user = e.select(".fullname.js-action-profile-name.show-popup-with-id").text();
            // username :
            String username = e.select("span.username.js-action-profile-name").first().text();
            // timestamp, in Unix time (see : https://en.wikipedia.org/wiki/Unix_time )
            String timeStamp_string = e.select("._timestamp").first().attr("data-time");
            Timestamp timestamp = new Timestamp(Long.parseLong(timeStamp_string));
            // content :
            String content = e.select(".TweetTextSize.js-tweet-text.tweet-text").text();
            // Responses :
            int nbResponses = Integer.parseInt(
                    e.select(".ProfileTweet-action--reply").select(".ProfileTweet-actionCount").attr("data-tweet-stat-count"));
            // retweets :
            int nbRetweets = Integer.parseInt(
                    e.select(".ProfileTweet-action--retweet").select(".ProfileTweet-actionCount").attr("data-tweet-stat-count"));
            // likes :
            int nbLikes = Integer.parseInt(
                    e.select(".ProfileTweet-action--favorite").select(".ProfileTweet-actionCount").attr("data-tweet-stat-count"));

            // we store data into a Tweet class
            Tweet r = new Tweet(user, id, username, timestamp, content, nbResponses, nbRetweets, nbLikes);
            r.setInReplyToTweetId(t.getTweetId());
            result.add(r);
        }
        return result;


    }

     /**
     * Return an ArrayList of {@link Tweet}, fetched from a Twitter Search, with custom query.
     *
     * @param nMax : Maximum number of tweets
     * @param q    : the query
     * @return an ArrayList of Tweets
     * @throws IOException
     * @throws JSONException
     */
    public static ArrayList<Tweet> getTweetsFromTwitter(int nMax, String q, boolean getRetweets) throws IOException, JSONException {

        int n = 0;
        int lastLog = 0;
        String url;
        String next_position = "";
        ArrayList<Tweet> myList = new ArrayList<>();
        do {

            // default url -- look, the parameter max_position is empty
            url = "https://twitter.com/i/search/timeline?vertical=default&q=" + URLEncoder.encode(q, "UTF-8")
                    + "&src=typd&include_available_features=1&include_entities=1&reset_error_state=false&max_position";

            if (n != 0)
                url = url + "=" + next_position; // we set the max_position parameter to next_position
            JSONObject json = readJsonFromUrl(url); // we get json object

            // this contains a big chunk of html, to be parsed...
            String items_html = (String) json.get("items_html");

            // we will use next_position for the next request
            next_position = (String) json.get("min_position");

            // with jsoup, we parse the html chunk to a document
            Document doc = Jsoup.parse(items_html);

            // we select a list of tweets
            Elements tweet_block = doc.select(".tweet.js-stream-tweet");

            if (tweet_block.size() == 0)
                break;
            // we iterate on every tweet
            for (Element e : tweet_block) {

                String id = e.attr("data-item-id");
                // user :
                String user = e.select(".fullname.js-action-profile-name.show-popup-with-id").text();
                // username :
                String username = e.select("span.username.js-action-profile-name").first().text();
                // timestamp, in Unix time (see : https://en.wikipedia.org/wiki/Unix_time )
                String timeStamp_string = e.select("._timestamp").first().attr("data-time");
                Timestamp timestamp = new Timestamp(Long.parseLong(timeStamp_string));
                // content :
                String content = e.select(".TweetTextSize.js-tweet-text.tweet-text").text();
                // Responses :
                int nbResponses = Integer.parseInt(
                        e.select(".ProfileTweet-action--reply").select(".ProfileTweet-actionCount").attr("data-tweet-stat-count"));
                // retweets :
                int nbRetweets = Integer.parseInt(
                        e.select(".ProfileTweet-action--retweet").select(".ProfileTweet-actionCount").attr("data-tweet-stat-count"));
                // likes :
                int nbLikes = Integer.parseInt(
                        e.select(".ProfileTweet-action--favorite").select(".ProfileTweet-actionCount").attr("data-tweet-stat-count"));

                // we store data into a Tweet class
                Tweet t = new Tweet(user, id, username, timestamp, content, nbResponses, nbRetweets, nbLikes);
                // and we add it to our list
                myList.add(t);
                if (getRetweets && t.getNbRetweets() > 0) {
                    myList.addAll(getRetweetById(t.getTweetId()));
                    try {
                        Thread.sleep(5500); // twitter API limit : max 180 request in 15 minutes ==> min 5 delay between requests...
                    } catch (InterruptedException e1) {

                        e1.printStackTrace();
                    }
                }
                n += 1;
                // log, 1 by 1%
                if (((double) (n - lastLog) / nMax) >= 0.01) {
                    lastLog = n;
                    System.out.println("Progress -> " + Math.round(((double) n / nMax) * 100) + "%");

                }
                if (n == nMax)
                    break;
            }

        } while (n < nMax);
        return myList;
    }

    public static ArrayList<Tweet> getTweetsFromTwitter(int nMax, String q) throws IOException, JSONException {
        return getTweetsFromTwitter(nMax, q, false);

    }

    /*
     * Main function, for testing the class. Args must be : the query, the maximum of tweets, and the result file location.
     */
    public static void main(String[] args) throws IOException, JSONException {
        if (args.length != 4) {
            System.out.println("Erreur, arguments incorrects.\nUsage : TwitterSearch crit�res nombre_de_tweets chemin_du_fichier_csv");
            System.out.println("Exemple :\nTwitterSearch \"cancer graviola\" 100 mytweets.csv");
            return;
        }
        String q = args[0];
        int nMax = Integer.parseInt(args[1]);
        String path = args[2];
        String option = args[3];
        boolean getRetweets = option.toUpperCase().equals("ALL");

        System.out.println("=============== Twitter Search ===============");
        System.out.println("Query = " + q);
        System.out.println("Maximum tweets = " + nMax);

        ArrayList<Tweet> myList = getTweetsFromTwitter(nMax, q, getRetweets);

        System.out.println("Found " + myList.size() + " tweets.");
        System.out.println("Saving into " + path + "...");
        // when it's done, we write the list of tweets in a CSV file
        TweetIO.writeToCSV(myList, path);
        System.out.println("Done. !");
        System.out.println("==============================================");

    }

}
