package modules.keywords;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//
import org.bson.Document;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import modules.data.Tweet;

import org.jfree.chart.ChartFactory;

public class Trend {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//création de l'instance mongo
		modules.data.Mongo base = new modules.data.Mongo();
			
			//connexion à la base distante
			base.ConnexionMongo("ds147537.mlab.com",47537,"twitter_rumors", "Twitter", "root", "TwitterMongo2016");
			
			
			
			//System.out.println(base.GetAllDates());
			//ArrayList<Document> datesList = new ArrayList<Document>();
			//datesList = base.GetAllDates();
			
			
			ArrayList<Document> datesList1= base.GetAllDates();  
		
			//nb de tweets
			long nbTweet = base.GetNbTweets();
			
			System.out.println(nbTweet);
			/*for (int i=2;i<10;i++){
				datesList.addAll(base.GetAllDates());
			}*/
			
			// Ici on tri la liste 
			
			ArrayList<Document> datesList = reverse (datesList1 );
			
//			for (int i = 0; i < 10; i++) {
//				System.out.println(datesList.get(i));	
//			}
			
			ArrayList<Timestamp> dates = new ArrayList<Timestamp>();
			//ArrayList<String> dateString = new ArrayList<String>();
			
			Iterator<Document> iter = datesList.iterator();
			while (iter.hasNext()) {
				Document doc = iter.next();
				//System.out.println(doc);
				Timestamp nbs = new Timestamp(Long.parseLong(doc.get("date").toString())*1000);
				
				dates.add(nbs);
				
				
				
			}
			SimpleDateFormat formatdate = new SimpleDateFormat("dd/MM/yyyy");
			
			int cum=1;
			String date_cour, date_i;
			
			date_cour = formatdate.format(dates.get(0));
			
			DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
			for (int i = 1; i < nbTweet; i++) {
				date_i = formatdate.format(dates.get(i));
				
				if (date_i.equals(date_cour)) {
					cum++;
				}
				else {
					dataset.addValue(cum,"Tweet",date_cour);
					date_cour=date_i;
					cum=1;
				}
				
				
				
				// dataset.addValue( v, "date_tweet", string);
				//System.out.println(i+ " :  "+string);	
			}
			dataset.addValue(cum,"Tweet",date_cour);
			
			
			
			// Il faut trier les dates dans l'ordre croissant
			
			
			
			// Puis les afficher grace sur le graphique
			
			
	 
		//create a chart...
		      JFreeChart lineChart = ChartFactory.createLineChart(
		 	         "Courbe de tendance", //titre
		 	         "Année",	//Titre de l'axe vertical X
		 	         "Nombre de Tweets",   //Titre de l'axe vertical Y
		 	         dataset,	//donnees
		 	         PlotOrientation.VERTICAL,	//Orientation
		 	         true,		//Légende
		 	         true,		//tooltips
		 	         true		//urls
		 	         );
	 
		//Affichage du graphique...
		ChartFrame chart=new ChartFrame("Tendance", lineChart);
		chart.pack();
		chart.setVisible(true);
		
			
			
			
		}

	static ArrayList<Document> reverse(ArrayList<Document> liste)
    {
		ArrayList<Document> result = new ArrayList<Document>();
	for(int i=liste.size()-1; i>=0; i--)
	    result.add(liste.get(i));
	return result;
    }
	
	
private static Map<Document,Integer> WordCount(ArrayList<Document> words){
		
		TreeMap<Document, Integer> result = new TreeMap<>();
		
		for (Document w : words)
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
	
	
	}


