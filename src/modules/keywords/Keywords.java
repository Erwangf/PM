package modules.keywords;
import java.awt.BorderLayout;
import java.io.*;
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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import modules.data.Mongo;
import modules.data.Tweet;
import modules.data.TwitterSearch;


import java.util.regex.*;

public class Keywords extends JPanel {
	
	private Mongo base;
	private  JPanel waitingPanel;
	private JTable tableau;
	private JScrollPane tabPane;

	public void setMonboBase(Mongo b){
    	this.base = b;
    }


	private static ArrayList<String> stopwords = null;

	public static void main(String[] args) {
		Mongo testbase = new Mongo();
		Keywords kw = new Keywords();
		kw.setBase(testbase);
		kw.initialize();
		kw.setVisible(true);
	}
	
	public Map<String, Integer> PrintWords(){
		Map<String, Integer> countsorted= new LinkedHashMap<>();

			
			
		
			ArrayList<Tweet> al = base.GetTweetsBlocks(1);  
			
			
			int i=100;
			int j=2;
			while(al.size()==i){
				al.addAll(base.GetTweetsBlocks(j));
				j++;
				//i=i+base.GetTweetsBlocks(j).size();
				i=i+100;
			}
			//System.out.println("Nombre de paquets : " + j);
			
			
			/*for (int i=2;i<10;i++){
				al.addAll(base.GetTweetsBlocks(i));
			}*/
			
			ArrayList<String> words = WordExtractor(al);
			ArrayList<String> stopWords=GetStopWords();
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
		//} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		return countsorted ;
	}
	
	private static ArrayList<String> WordExtractor(ArrayList<Tweet> tweets)
	{
		ArrayList <String> words=new ArrayList <String> ();		
		String content;
		for (Tweet t : tweets)
		{

				content=t.getContent().replaceAll( "\\s\\p{Punct}|[,.#@>:+;/_!*¿'?)]", "");  //[\\s\\p{Punct}]     \\p{Punct}[^#@'&�]
				content=content.toUpperCase();
				String[] mywords = content.split(" ");	
				words.addAll(new ArrayList(Arrays.asList(mywords)));
		}
		return words;		
	}

	
	public static ArrayList<String> GetStopWords()
		{
			if(stopwords!=null) return stopwords;

			InputStream monFichier = null;
			BufferedReader tampon = null;

			stopwords = new ArrayList<String>();

			try {
				//monFichier = Keywords.class.getClassLoader().getResourceAsStream("config/StopWords.csv");	//build
				monFichier = new FileInputStream("./config/StopWords.csv"); //dev
				tampon = new BufferedReader(new InputStreamReader(monFichier));


				String element;
				int c;
				String ligne = tampon.readLine();
				while (ligne != null) {
					// Lit une ligne de test.csv (ligne == null) break;
					stopwords.add(ligne.toUpperCase());

					// V�rifie la fin de fichier
					
					//System.out.println("\n"+ligne);
					ligne = tampon.readLine();
				} // Fin du while
			} catch (IOException exception) {
				exception.printStackTrace();
			} finally {
				System.out.println("Chargé "+stopwords.size()+" stopwords !");
				try {
					if (tampon != null) {
						tampon.close();
					}
					if (monFichier != null) {
						monFichier.close();
					}
				} catch(IOException exception1) {
					exception1.printStackTrace();
				}
		}
	return stopwords;
}
	
	/**
	 * En entr�e une liste de mots
	 * en sortie une liste de mots filtr�s (no stopwords)
	 * 
	 * @param words : la liste de mot en entr�e
	 * @return
	 */
	public static ArrayList<String> FilterStopWords(ArrayList<String> words,ArrayList<String> stopwords ){
		
		ArrayList<String> result = new ArrayList<String>();
			//byte temp;	
		for (String w : words)
		{
			try{
			byte[] bytes=w.getBytes("ISO8859_15");
			String t = new String( bytes , "cp1252" );
			t=t.replaceAll( "Ã", "A");
			t=t.replaceAll( "Â", "A");
			t=t.replaceAll( "Á", "A");
			t=t.replaceAll( "Í", "I");
			t=t.replaceAll( "//?", "");
			t=t.replaceAll( "Ó", "O");
			t=t.replaceAll( "Ô", "O");
			t=t.replaceAll( "Ê", "E");
			t=t.replaceAll( "È", "E");
			t=t.replaceAll( "Ú", "E");

			if (! stopwords.contains(w) && w.length()>1)
				{
					result.add(t);
				}
				
			} catch (java.io.UnsupportedEncodingException e) { 
			    // Le codage n'est pas reconnu. 
			    e.printStackTrace(); 
			}
			
			
			
			
//			if (! stopwords.contains(w))
//			{
//				result.add(t);
//			}
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


		return result;
	}
	
	
	
	
	 private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

	        // 1. Convert Map to List of Map
	        List<Map.Entry<String, Integer>> list =
					new LinkedList<>(unsortMap.entrySet());

	        // 2. Sort list with Collections.sort(), provide a custom Comparator
	        //    Try switch the o1 o2 position for a different order
	        Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

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
		 int kount=0;
		 for (Map.Entry<String, Integer> entry : Map.entrySet()) {
			 kount++;
			System.out.println("Mot : " + entry.getKey() + " Count : " + entry.getValue() + "   (" +kount+")");
			if (kount==300) break;
		}
		 
	 }
	 
	 
//****************** INTERFACE ********************************************
	 

	public Keywords(){
		super();
        setLayout(new BorderLayout());
	}

	public void setBase(Mongo m){
		this.base = m;
	}
	public void initialize(){

        waitingPanel = new JPanel(new BorderLayout());
        ImageIcon loading = new ImageIcon(Keywords.class.getResource("/ajax-loader.gif"));

        waitingPanel.add(new JLabel("Veuillez patienter...",loading,JLabel.CENTER),BorderLayout.CENTER);
        add(waitingPanel,BorderLayout.CENTER);
        revalidate();


		Map<String,Integer> myMap = PrintWords();

        int i=0;
        int intermediaire;
        Object[][] donneees = new Object [25][2] ;

    	  for (Map.Entry<String, Integer> entry : myMap.entrySet()) {

    		   donneees [i][0] = entry.getKey();
      		   donneees [i][1] = entry.getValue();
    		   i++;
    		   if (i==25){
    			   break;
    		   }
			}

        String[] entetes = {"Mots", "Nombre d'occurences"};

        JTable tableau = new JTable(donneees, entetes);
		DefaultTableModel tableModel = new DefaultTableModel(donneees, entetes) {

			@Override
			public boolean isCellEditable(int row, int column) {
				//all cells false
				return false;
			}
		};

		tableau.setModel(tableModel);
        removeAll();
        revalidate();
		add(new JScrollPane(tableau), BorderLayout.CENTER);
        revalidate();
        repaint();




		
	}
}





	
