package modules.data;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class TweetIO {
    private final static String CSV_SEPARATOR = ";";


    public static void main(String[] args) {

        ArrayList<Tweet> learnT = readFromCSV("./config/tweets101_370.csv");
        OpinionMining.buildScoreIndex_v1(learnT);
        OpinionMining.scoreIndex.forEach((k,v)->{
            System.out.println(k+":"+v);
        });
        ArrayList<Tweet> testT = readFromCSV("./config/test70.csv");
        final int[] correctPositiveResults = {0};
        final int[] correctNegativeResults = {0};
        final int[] correctNullResults = {0};
        final int[] incorrectPositiveResults = {0};
        final int[] incorrectNegativeResults = {0};
        final int[] incorrectNullResults = {0};
        testT.forEach((tweet -> {
            int prevision = 0;
            float rawPrevision = OpinionMining.getScore_v1(tweet.getContent());

            if (rawPrevision > 0.5f) {
                prevision = 1;
            } else if (rawPrevision < -0.1f) {
                prevision = -1;
            }
            System.out.println("Prévision = " + prevision + " original = " + tweet.getNote() + " raw = " + rawPrevision);
            if (prevision == tweet.getNote()) {
                switch (Math.round(tweet.getNote())) {
                    case 1:
                        correctPositiveResults[0]++;
                        break;
                    case 0:
                        correctNullResults[0]++;
                        break;
                    case -1:
                        correctNegativeResults[0]++;
                }
            } else {
                switch (Math.round(tweet.getNote())) {
                    case 1:
                        incorrectPositiveResults[0]++;
                        break;
                    case 0:
                        incorrectNullResults[0]++;
                        break;
                    case -1:
                        incorrectNegativeResults[0]++;
                }
            }

            tweet.setNote(((float) prevision));

        }));

        float nbTweetNotNull = correctNegativeResults[0]+correctPositiveResults[0]+incorrectNegativeResults[0]+incorrectPositiveResults[0];
        float nbTweetNotNullCorrect = correctPositiveResults[0]+correctNegativeResults[0];

        System.out.println("Correct Positive results : " + correctPositiveResults[0]);
        System.out.println("Correct Null results : " + correctNullResults[0]);
        System.out.println("Correct Negative results : " + correctNegativeResults[0]);
        System.out.println("Incorrect Positive results : " + incorrectPositiveResults[0]);
        System.out.println("incorrect Null results : " + incorrectNullResults[0]);
        System.out.println("incorrect Negative results : " + incorrectNegativeResults[0]);
        System.out.println("-------------------");
        System.out.println("nb tweets non nuls "+nbTweetNotNull);
        System.out.println("nb tweets non nuls corrects"+nbTweetNotNullCorrect);
        System.out.println("Taux de Précision (hors null) = " + nbTweetNotNullCorrect/nbTweetNotNull);


    }

    /**
     * This function write the content of an ArrayList of {@link Tweet} objects, at a specific path, given in parameters.
     *
     * @param tweetList The ArrayList of Tweets
     * @param path      The relative path of the result file.
     */
    public static void writeToCSV(ArrayList<Tweet> tweetList, String path) {
        try {
            // we open a BufferedWriter : it allow Java to write files, at a specific path ( given in parameters )
            OutputStream os = new FileOutputStream(path);
            os.write(239);
            os.write(187);
            os.write(191); //petit fix pour l'UTF-8
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            for (Tweet t : tweetList) {
                // for each tweet, we create one line, with all the informations
                String oneLine =
                        "\"" + t.getUser() + "\"" + CSV_SEPARATOR + "\"" + t.getInReplyToTweetId() + "\"" + CSV_SEPARATOR + "\"" + t.getUsername() + "\"" + CSV_SEPARATOR + "\"" +
                                t.getTimeStamp().getTime() + "\"" + CSV_SEPARATOR + "\"" + t.getContent() + "\"" + CSV_SEPARATOR + "\"" +
                                t.getNbResponses() + "\"" + CSV_SEPARATOR + "\"" + t.getNbRetweets() + "\"" + CSV_SEPARATOR + "\"" +
                                t.getNbLikes() + "\"" + CSV_SEPARATOR + "\"0\"";

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


    public static ArrayList<Tweet> readFromCSV(String csvFile) {
        ArrayList<Tweet> res = new ArrayList<>();
        String line = "";


        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(csvFile), "UTF8"));) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] fields = line.split("\"" + CSV_SEPARATOR + "\"");
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].replace("\"", "");
                }

                Tweet t = new Tweet(
                        fields[0], //user,
                        fields[1], //tweetID
                        fields[2], //username
                        new Timestamp(Integer.parseInt(fields[3])), //date
                        fields[4], //content
                        Integer.parseInt(fields[5]), //nbresponses
                        Integer.parseInt(fields[6]), //nbRetweets
                        Integer.parseInt(fields[7]) //nblikes
                );

                t.setNote(Float.parseFloat(fields[8]));

                res.add(t);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
