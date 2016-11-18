package modules.keywords;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.json.JSONException;

import modules.data.Tweet;
import modules.data.TwitterSearch;

import java.util.regex.*;

public class Keywords extends JFrame {

	public static void main(String[] args) {
		Map<String, Integer> mamap = new LinkedHashMap<>();
			mamap=PrintWords();
		new Keywords(mamap).setVisible(true);

	}
	
	public static Map<String, Integer> PrintWords(){
		Map<String, Integer> countsorted= new LinkedHashMap<>();
		try {
			ArrayList<Tweet> al = TwitterSearch.getTweetsFromTwitter(200, "cancer%20graviola");   
			ArrayList<String> words = WordExtractor(al);
			ArrayList<String> stopWords=GetStopWords ();
			words = FilterStopWords(words,stopWords);
			Map<String, Integer> count = new LinkedHashMap<>();
			
			count=WordCount(words);
			
			//countsorted = new LinkedHashMap<>();
			countsorted=sortByValue(count);
			afficherMap(countsorted);
			
			//return countsorted;
			
			// Tableau
			//new JTableBasiqueAvecModeleStatique().setVisible(true);

			//Map<String,Integer> map = WordCount(words);
			//PrintWordCount(count);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return countsorted ;
	}
	
	private static ArrayList<String> WordExtractor(ArrayList<Tweet> tweets)
	{
		ArrayList <String> words=new ArrayList <String> ();		
		String content;
		for (Tweet t : tweets)
		{

				content=t.getContent().replaceAll( "\\s\\p{Punct}|[,.#@>:;/!»*«¿'?)]", "");  //[\\s\\p{Punct}]     \\p{Punct}[^#@'&¿]
				content=content.toUpperCase();
				String[] mywords = content.split(" ");	
				words.addAll(new ArrayList(Arrays.asList(mywords)));
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
	
	/**
	 * En entrée une liste de mots
	 * en sortie une liste de mots filtrés (no stopwords)
	 * 
	 * @param words : la liste de mot en entrée
	 * @return
	 */
	private static ArrayList<String> FilterStopWords(ArrayList<String> words,ArrayList<String> stopwords ){
		
		ArrayList<String> result = new ArrayList<String>();
				
		for (String w : words)
		{
			if (! stopwords.contains(w))
			{
				result.add(w);
			}
		}
						
		return 	result;	
		
	}

	
	private static Map<String,Integer> WordCount(ArrayList<String> words){
		
		TreeMap<String, Integer> result = new TreeMap<>();
		
		for (String w : words)
		{
			if (! result.containsKey(w))
			{
				result.put(w, 1);
			}else{
				result.replace(w, result.get(w)+1);
			}
		}
		
		
		/*for (Map.Entry<String, Integer> entry : result.entrySet()) {
			System.out.println("Mot : " + entry.getKey() + " Count : " + entry.getValue());
		}*/	
		
		
		return result;
	}
	
	
	
	
	 private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

	        // 1. Convert Map to List of Map
	        List<Map.Entry<String, Integer>> list =
	                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

	        // 2. Sort list with Collections.sort(), provide a custom Comparator
	        //    Try switch the o1 o2 position for a different order
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
	            public int compare(Map.Entry<String, Integer> o1,
	                               Map.Entry<String, Integer> o2) {
	                return (o2.getValue()).compareTo(o1.getValue());
	            }
	        });

	        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
	        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
	        for (Map.Entry<String, Integer> entry : list) {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }

	        /*
	        //classic iterator example
	        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
	            Map.Entry<String, Integer> entry = it.next();
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }*/


	        return sortedMap;
	    }

	
	 
	 private static void afficherMap(Map<String, Integer> Map){
		 
		 for (Map.Entry<String, Integer> entry : Map.entrySet()) {
			System.out.println("Mot : " + entry.getKey() + " Count : " + entry.getValue());
		}
		 
	 }
	 
	 
//****************** INTERFACE ********************************************
	 

		    public Keywords(Map<String, Integer> Map) {  // parametre (Map<String, Integer> Map)
		        super();
		 
		        setTitle("Nombre d'occurences pour chaque mot");
		        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        
		        
		        /*for (Map.Entry<String, Integer> entry : Map.entrySet()) {
					//System.out.println("Mot : " + entry.getKey() + " Count : " + entry.getValue());
		        	String [][] donnees= { entry.getKey(),(String[])  entry.getValue()};
		        	
				}*/
		        
		       
		        int i=0;
		        int intermediaire;
		        Object[][] donneees = new Object [25][2] ;
		        //for (int i=0;i<20;i++){
	        	  for (Map.Entry<String, Integer> entry : Map.entrySet()) {
						//System.out.println("Mot : " + entry.getKey() + " Count : " + entry.getValue());
			        	//String [i][1] donneees= { entry.getKey(),  entry.getValue()};
	        		   donneees [i][0] = entry.getKey();
	        		   //intermediaire= entry.getValue();
	        		   //intermediaire=intermediaire;  // Il faut a tout prix caster cette variable en String
	        		   donneees [i][1] = entry.getValue();
	        		   i++;
	        		   if (i==25){
	        			   break;
	        		   }
					}
		       // }
		 
		        /*Object[][] donnees = 
		        {
		                {"graviola", 77},
		                {"cancer", 66},
		                {"medicament", 55},
		                {"mort", 44},
		                {"guerison", 33},
		                
		        };*/
		 
		        String[] entetes = {"Mots", "Nombre d'occurences"};
		 
		        JTable tableau = new JTable(donneees, entetes);
		 
		        //getContentPane().add(tableau.getTableHeader(), BorderLayout.NORTH);
		        //getContentPane().add(tableau, BorderLayout.CENTER);
		        
		        getContentPane().add(new JScrollPane(tableau), BorderLayout.CENTER);
		 
		        pack();
		    }	 
}





	
