import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;

public class Keywords {

	public static void main(String[] args) {
		try {
			ArrayList<Tweet> al = TwitterSearch.getTweetsFromTwitter(20, "cancer graviola");
			ArrayList<String> words = WordExtractor(al);
			for(String w : words){
				System.out.println(w);
			}
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	
	
	private static ArrayList<String> WordExtractor(ArrayList<Tweet> tweets)
	{
		
		ArrayList <String> words=new ArrayList <String> ();
		
		for (Tweet t : tweets)
		{
			String content=t.getContent();
			words.addAll(new ArrayList(Arrays.asList(content.split(" "))));
		}
		
		return words;
		
	}

	
	private static ArrayList<String> GetStopWords ()
		{
			FileReader monFichier = null;
			BufferedReader tampon = null;
			
			ArrayList <String> stopwords=new ArrayList <String> ();
		
			try {
				monFichier = new FileReader("./config/StopWords.csv");	// Tu n'a qu'a changer le chemin du fichier csv contenant les tweets
				tampon = new BufferedReader(monFichier);

				String element;
				int c;
				String ligne = tampon.readLine();
				while (ligne != null) {
					// Lit une ligne de test.csv (ligne == null) break;
					stopwords.add(ligne.toUpperCase());

					// Vérifie la fin de fichier
					
					//System.out.println("\n"+ligne);
					ligne = tampon.readLine();
				} // Fin du while
			} catch (IOException exception) {
				exception.printStackTrace();
			} finally {
				try {
					tampon.close();
					monFichier.close();
				} catch(IOException exception1) {
					exception1.printStackTrace();
				}
			
			
			
		}
	return stopwords;
}


	private static DeleteStopwords()
	
}



	
