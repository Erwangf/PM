package modules.keywords;

import java.awt.BasicStroke;
import java.awt.Color;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.bson.Document;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
//import org.jfree.data.time.Minute;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
//import org.jfree.ui.Spacer;


public class Trend extends ApplicationFrame {

	public Trend(final String title) {
	
	//Etape 0 : constructeur (et déclarations)
	
        super(title);
	
	//Etape 1 : dataset
		//Appel de la fonction createDataset qui charge les données
        boolean mensuel = false;
		XYDataset dataset = createDataset(mensuel);


	// Etape 2 : Creer le graphique
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Courbe de tendance",
            "Date", 
            "Nombre de tweets par jour",
            dataset,
            false,
            true,
            false
        );
		
		//Paramretres
		 chart.setBackgroundPaint(Color.white); //couleur de fond
       final XYPlot plot = chart.getXYPlot();
       plot.setBackgroundPaint(Color.lightGray);
       plot.setDomainGridlinePaint(Color.white);
       plot.setRangeGridlinePaint(Color.white);       
     //  final DateAxis axis = (DateAxis) plot.getDomainAxis();
     //  axis.setDateFormatOverride(new SimpleDateFormat("M/yy"));
		
		
	//Etape 3 : Préparation de la fenetre
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		chartPanel.setMouseZoomable(true, false);
		setContentPane(chartPanel);
		
		
    }

    public static void main(final String[] args) {

	//Etape 4 : Affichage du  graphique
		Trend trend = new Trend("Tendance des tweets");
		trend.pack();
        RefineryUtilities.centerFrameOnScreen(trend);
        trend.setVisible(true);		
		

    }

	private XYDataset createDataset(boolean mensuel) {

	//Création instance mongo et connexion à la base
		modules.data.Mongo base = new modules.data.Mongo();
		base.ConnexionMongo("ds147537.mlab.com",47537,"twitter_rumors", "Twitter", "root", "TwitterMongo2016");
	
	//Reccuperation des dates et du nb de tweets
		ArrayList<Document> datesList= base.GetAllDates();
		int nbTweet = (int) base.GetNbTweets();
		
	//Création d'une collection de timestamp
		ArrayList<Timestamp> dates = new ArrayList<Timestamp>();
		
	//Conversion doc.dates => timestamp(milliseconde) => timestamp(seconde)
		Document doc;
		Timestamp nbs;
		int i;
		for (i = nbTweet-1; i >= 0; i--) {
			doc = datesList.get(i);
			nbs = new Timestamp(Long.parseLong(doc.get("date").toString())*1000);
			dates.add(nbs);
		}
		//Tri de l'aarylist pour avoir les dates dans l'ordre
		Collections.sort(dates);	

//Comptage par mois
	if(mensuel){
		
		//Création d'un format pour les date (dd/mm/yyyy)	
				SimpleDateFormat formatdate2 = new SimpleDateFormat("MM/yyyy");
				
			//Partage des différents formats de la date
				SimpleDateFormat date_mois = new SimpleDateFormat("MM");
				SimpleDateFormat date_annee = new SimpleDateFormat("yyyy");
			
			//Creation et ajout des TimeSeries dans le dataset
				TimeSeriesCollection dataset = new TimeSeriesCollection();
				dataset.setDomainIsPointsInTime(true);
				TimeSeries s = new TimeSeries("Tweets", Month.class);
				
			//initialisation du comptage
				int cum=1;
				String date_cour, date_i;
				int dateMonth=0, dateYear=0;
			//Reccuperation de la première date
				dateMonth = Integer.parseInt(date_mois.format(dates.get(0)));
				dateYear = Integer.parseInt(date_annee.format(dates.get(0)));
				date_cour = formatdate2.format(dates.get(0));
			//Demarrage de la bouble for : pour chaque date,
				for (i = 1; i < nbTweet; i++) {
					//Enregistrement de la date i
					date_i = formatdate2.format(dates.get(i));
					//Si date i est égale à la date précédente (courante), on incrémente le comptage de 1
					if (date_i.equals(date_cour)) {
						cum++;
					}
					// sinon on ajoute la date courante dans la serie, et on enregistre la date i en date courante
					else {
						s.add(new Month(dateMonth,dateYear),cum);
						dateMonth = Integer.parseInt(date_mois.format(dates.get(i)));
						dateYear = Integer.parseInt(date_annee.format(dates.get(i)));
						date_cour=date_i;
						cum=1;
					}
				}
				//Fin de la boucle, on ajoute la dernière date à la serie
				s.add(new Month(dateMonth,dateYear),cum);
				
				//Ajout de la serie au dataset
				dataset.addSeries(s);
				
				//On retourne le dataset
				return dataset;
		
	}else{
//Comptage par jour
	//Création d'un format pour les date (dd/mm/yyyy)	
		SimpleDateFormat formatdate = new SimpleDateFormat("dd/MM/yyyy");
		
	//Partage des différents formats de la date
		SimpleDateFormat date_jour = new SimpleDateFormat("dd");
		SimpleDateFormat date_mois = new SimpleDateFormat("MM");
		SimpleDateFormat date_annee = new SimpleDateFormat("yyyy");
	
	//Creation et ajout des TimeSeries dans le dataset
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.setDomainIsPointsInTime(true);
		TimeSeries s = new TimeSeries("Tweets", Day.class);
		
	//initialisation du comptage
		int cum=1;
		String date_cour, date_i;
		int dateDay=0, dateMonth=0, dateYear=0;
	//Reccuperation de la première date
		dateDay = Integer.parseInt(date_jour.format(dates.get(0)));
		dateMonth = Integer.parseInt(date_mois.format(dates.get(0)));
		dateYear = Integer.parseInt(date_annee.format(dates.get(0)));
		date_cour = formatdate.format(dates.get(0));
	//Demarrage de la bouble for : pour chaque date,
		for (i = 1; i < nbTweet; i++) {
			//Enregistrement de la date i
			date_i = formatdate.format(dates.get(i));
			//Si date i est égale à la date précédente (courante), on incrémente le comptage de 1
			if (date_i.equals(date_cour)) {
				cum++;
			}
			// sinon on ajoute la date courante dans la serie, et on enregistre la date i en date courante
			else {
				s.add(new Day(dateDay,dateMonth,dateYear),cum);
				dateDay = Integer.parseInt(date_jour.format(dates.get(i)));
				dateMonth = Integer.parseInt(date_mois.format(dates.get(i)));
				dateYear = Integer.parseInt(date_annee.format(dates.get(i)));
				date_cour=date_i;
				cum=1;
			}
		}
		//Fin de la boucle, on ajoute la dernière date à la serie
		s.add(new Day(dateDay,dateMonth,dateYear),cum);
		
		//Ajout de la serie au dataset
		dataset.addSeries(s);
		
		//On retourne le dataset
		return dataset;
		
	}
	
    }

	private Timestamp Timestamp(long l) {
		// TODO Auto-generated method stub
		return null;
	}
	
}