import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;


public class TestMongo {

	public static void main(String[] args) throws IOException, JSONException {
		if (args.length != 3) {
			System.out.println(
					"Erreur, arguments incorrects.\nUsage : TwitterSearch critères nombre_de_tweets chemin_du_fichier_csv");
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
		
		
		//création de l'instance mongo
		Mongo base = new Mongo();
	//	base.ConnexionMongo("localhost",0,"BaseTest", "Twitter");
		//connexion à la base distante
		base.ConnexionMongo("ds147537.mlab.com",47537,"twitter_rumors", "Twitter", "root", "TwitterMongo2016");
		System.out.println("1");
		//Insertion de la liste de Tweets récupérés précédemment
		base.InsertMongo(myList);
		System.out.println(base.GetNbTweets());
		System.out.println("2");
		System.out.println(base.GetAllTweets());
		System.out.println("3");
		System.out.println(base.GetAllDates());
		
		//System.out.println("Saving into " + path + "...");
		// when it's done, we write the list of tweets in a CSV file
	//	writeToCSV(myList, path);
		System.out.println("==============================================");

	}

}
