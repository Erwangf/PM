package modules.data;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class TweetExport {
    private final static String CSV_SEPARATOR = "\t";


    /**
     * This function write the content of an ArrayList of {@link Tweet} objects, at a specific path, given in parameters.
     *
     * @param tweetList The ArrayList of Tweets
     * @param path      The relative path of the result file.
     */

    public static void writeToCSV(ArrayList<Tweet> tweetList, String path) {
        try {
            // we open a BufferedWriter : it allow Java to write files, at a specific path ( given in parameters )
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));

            for (Tweet t : tweetList) {
                // for each tweet, we create one line, with all the informations
                String oneLine = t.getUser() + CSV_SEPARATOR + t.getUsername() + CSV_SEPARATOR + t.getTimeStamp().getTime() + CSV_SEPARATOR
                        + t.getContent() + CSV_SEPARATOR + t.getNbResponses() + CSV_SEPARATOR + t.getNbRetweets() + CSV_SEPARATOR
                        + t.getNbLikes() + CSV_SEPARATOR + t.getInReplyToTweetId() + CSV_SEPARATOR + t.getOriginalTweetd() + CSV_SEPARATOR;
                // and finally we write that line.
                bw.write(oneLine);
                bw.newLine();
            }
            // at the end of the procedure, we flush the stream, and close the buffer.
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
