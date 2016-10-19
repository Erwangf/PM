import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        /*connection.getHeaderFields().forEach(( headerName, headerValue)->{
            connection.setRequestProperty(headerName,null);
        });*/
        connection.setRequestProperty("Accept-Charset", "utf-8");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.setRequestMethod("GET");


        InputStreamReader ISR = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));
        BufferedReader rd = new BufferedReader(ISR);
        String jsonText = readAll(rd);

        ISR.close();
        rd.close();

        return new JSONObject(jsonText);
    }

    //European countries use ";" as
    //CSV separator because "," is their digit separator
    private static final String CSV_SEPARATOR = ";";

    private static void writeToCSV(ArrayList<Tweet> tweetList, String path) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            for (Tweet t : tweetList) {
                String oneLine = t.getUser() +
                        CSV_SEPARATOR +
                        t.getUsername() +
                        CSV_SEPARATOR +
                        t.getTimeStamp().getTime() +
                        CSV_SEPARATOR +
                        t.getContent() +
                        CSV_SEPARATOR +
                        t.getNbResponses() +
                        CSV_SEPARATOR +
                        t.getNbRetweets() +
                        CSV_SEPARATOR +
                        t.getNbLikes() +
                        CSV_SEPARATOR;

                bw.write(oneLine);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException ignored) {
        }
    }


    public static void main(String[] args) throws IOException, JSONException {
        if (args.length != 3) {
            System.out.println("Erreur, arguments incorrects.\nUsage : TwitterSearch critères nombre_de_tweets chemin_du_fichier_csv");
            System.out.println("Exemple :\nTwitterSearch \"cancer graviola\" 100 mytweets.csv");
            return;
        }
        String q = URLEncoder.encode(args[0], "UTF-8");
        int nMax = Integer.parseInt(args[1]);
        String path = args[2];
        System.out.println("=============== Twitter Search ===============");
        System.out.println("Query = " + q);
        System.out.println("Maximum tweets = " + nMax);

        ArrayList<Tweet> myList = getTweetsFromTwitter(nMax, q);

        System.out.println("Found " + myList.size() + " tweets.");
        System.out.println("Saving into " + path + "...");
        //when it's done, we write the list of tweets in a CSV file
        writeToCSV(myList, path);
        System.out.println("==============================================");


    }

    public static ArrayList<Tweet> getTweetsFromTwitter(int nMax, String q) throws IOException, JSONException {

        int n = 0;
        int lastLog = 0;
        String url;
        String next_position = "";
        ArrayList<Tweet> myList = new ArrayList<>();
        do {

            //default url -- look, the parameter max_position is empty
            url = "https://twitter.com/i/search/timeline?vertical=default&q=" + q + "&src=typd&include_available_features=1&include_entities=1&reset_error_state=false&max_position";

            if (n != 0) url = url + "=" + next_position; //we set the max_position parameter to next_position

            JSONObject json = readJsonFromUrl(url); //we get json object

            String items_html = (String) json.get("items_html"); //this contains a big chunk of html, to be parsed...
            next_position = (String) json.get("min_position"); //used for the next request
            Document doc = Jsoup.parse(items_html); //we parse the html chunk in a html document ( with Jsoup ! )

            Elements tweet_block = doc.select(".tweet.js-stream-tweet"); // a list of tweets

            if (tweet_block.size() == 0) break;
            // we iterate on every tweet
            for (Element e : tweet_block) {

                String id = e.attr("data-item-id");
                System.out.println(id);
                //each information is accessible via a wisely chosen selector...

                String user = e.select(".fullname.js-action-profile-name.show-popup-with-id").text();
                String username = e.select("span.username.js-action-profile-name").first().text();
                //timestamp, in unix time (see : https://en.wikipedia.org/wiki/Unix_time )
                String timeStamp_string = e.select("._timestamp").first().attr("data-time");
                Timestamp timestamp = new Timestamp(Long.parseLong(timeStamp_string));
                //content
                String content = e.select(".TweetTextSize.js-tweet-text.tweet-text").text();
                //Responses
                int nbResponses = Integer.parseInt(e.select(".ProfileTweet-action--reply").select(".ProfileTweet-actionCount").attr("data-tweet-stat-count"));
                //retweets
                int nbRetweets = Integer.parseInt(e.select(".ProfileTweet-action--retweet").select(".ProfileTweet-actionCount").attr("data-tweet-stat-count"));
                //likes
                int nbLikes = Integer.parseInt(e.select(".ProfileTweet-action--favorite").select(".ProfileTweet-actionCount").attr("data-tweet-stat-count"));

                //we put data into a Tweet class
                Tweet t = new Tweet(user, username, timestamp, content, nbResponses, nbRetweets, nbLikes);
                // and we add it to our list
                myList.add(t);
                n += 1;
                //log, 1 by 1%
                if (((double) (n - lastLog) / nMax) >= 0.01) {
                    lastLog = n;
                    System.out.println("Progress -> " + Math.round(((double) n / nMax) * 100) + "%");
                }
                if (n == nMax) break;

            }

        }
        while (n < nMax);
        return myList;
    }
}
