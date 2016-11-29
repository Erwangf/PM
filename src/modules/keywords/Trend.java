package modules.keywords;

import modules.data.Mongo;
import modules.data.OpinionMining;
import modules.data.Tweet;
import org.bson.Document;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

//import org.jfree.data.time.Minute;
//import org.jfree.ui.Spacer;


public class Trend extends JPanel {


    private JPanel waitingPanel;
    private boolean mensuel;
    private Mongo base;
    private ArrayList<Tweet> datesList;

    public void setBase(Mongo base) {
        this.base = base;
    }

    public Trend() {

        //Etape 0 : constructeur (et déclarations)

        super();
        mensuel = true;

        this.setLayout(new BorderLayout());


    }

    public void initialize() {

        removeAll();
        revalidate();
        repaint();
        waitingPanel = new JPanel(new BorderLayout());
        ImageIcon loading = new ImageIcon(Keywords.class.getResource("/ajax-loader.gif"));

        waitingPanel.add(new JLabel("Veuillez patienter...",loading,JLabel.CENTER),BorderLayout.CENTER);
        add(waitingPanel,BorderLayout.CENTER);
        revalidate();
        repaint();
        datesList = null;
        //Etape 1 : dataset
        //Appel de la fonction createDataset qui charge les données
        XYDataset dataset = createDataset(mensuel, 1); //rouge


        // Etape 2 : Creer le graphique
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Courbe de tendance",
                "Date",
                "Nombre de tweets par" + (mensuel ? "mois" : "jour"),
                dataset,
                false,
                true,
                false
        );



        //Paramretres
        chart.setBackgroundPaint(Color.white); //couleur de fond
        XYPlot plot = chart.getXYPlot();
        StandardXYItemRenderer sr1 = new StandardXYItemRenderer();
        sr1.setSeriesPaint(0, Color.decode("#008663"));
        plot.setRenderer(0,sr1);
        plot.setDataset(1,createDataset(mensuel,0));
        StandardXYItemRenderer sr2 = new StandardXYItemRenderer();
        sr2.setSeriesPaint(0, Color.yellow);
        plot.setRenderer(1,sr2);
        plot.setDataset(2,createDataset(mensuel,-1)); //rouge
        StandardXYItemRenderer sr3 = new StandardXYItemRenderer();
        sr3.setSeriesPaint(0, Color.red);
        plot.setRenderer(2,sr3);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        //  final DateAxis axis = (DateAxis) plot.getDomainAxis();
        //  axis.setDateFormatOverride(new SimpleDateFormat("M/yy"));


        //Etape 3 : Préparation de la fenetre
        ChartPanel chartPanel = new ChartPanel(chart){

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
        chartPanel.setMouseZoomable(true, false);
        this.setLayout(new BorderLayout());
        removeAll();
        revalidate();
        add(chartPanel,BorderLayout.CENTER);
        revalidate();
        repaint();

    }



    public static void main(final String[] args) {

        //Etape 4 : Affichage du  graphique
        JFrame frame = new JFrame("Test Trends");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Trend trend = new Trend();
        Mongo myBase = new Mongo();
        myBase.ConnexionMongoDefault();
        trend.setBase(myBase);
        trend.initialize();

       frame.setContentPane(trend);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }

    private XYDataset createDataset(boolean mensuel, int note) {


        //Reccuperation des dates et du nb de tweets
        if(datesList==null)datesList = base.GetAllTweets();
        int nbTweet = (int) base.GetNbTweets();

        //Création d'une collection de timestamp
        ArrayList<Timestamp> dates = new ArrayList<>();

        //Conversion doc.dates => timestamp(milliseconde) => timestamp(seconde)
        Tweet doc;
        Timestamp nbs;
        int i;
        for (i = nbTweet - 1; i >= 0; i--) {
            doc = datesList.get(i);
            nbs = new Timestamp((long) doc.getTimeStamp().getTime() * 1000);
            if(OpinionMining.getPrevision_v2(doc.getContent()) == note ) dates.add(nbs);
        }
        //Tri de l'arraylist pour avoir les dates dans l'ordre
        Collections.sort(dates);

//Comptage par mois
        if (mensuel) {

            //Création d'un format pour les date (dd/mm/yyyy)
            SimpleDateFormat formatdate2 = new SimpleDateFormat("MM/yyyy");

            //Partage des différents formats de la date
            SimpleDateFormat date_mois = new SimpleDateFormat("MM");
            SimpleDateFormat date_annee = new SimpleDateFormat("yyyy");

            //Creation et ajout des TimeSeries dans le dataset
            TimeSeriesCollection dataset = new TimeSeriesCollection();
            TimeSeries s = new TimeSeries("Tweets", Month.class);

            //initialisation du comptage
            int cum = 1;
            String date_cour, date_i;

            //Reccuperation de la première date
            int dateMonth = Integer.parseInt(date_mois.format(dates.get(0)));
            int dateYear = Integer.parseInt(date_annee.format(dates.get(0)));
            date_cour = formatdate2.format(dates.get(0));
            //Demarrage de la bouble for : pour chaque date,
            for (i = 1; i < dates.size(); i++) {
                //Enregistrement de la date i
                date_i = formatdate2.format(dates.get(i));
                //Si date i est égale à la date précédente (courante), on incrémente le comptage de 1
                if (date_i.equals(date_cour)) {
                    cum++;
                }
                // sinon on ajoute la date courante dans la serie, et on enregistre la date i en date courante
                else {
                    s.add(new Month(dateMonth, dateYear), cum);
                    dateMonth = Integer.parseInt(date_mois.format(dates.get(i)));
                    dateYear = Integer.parseInt(date_annee.format(dates.get(i)));
                    date_cour = date_i;
                    cum = 1;
                }
            }
            //Fin de la boucle, on ajoute la dernière date à la serie
            s.add(new Month(dateMonth, dateYear), cum);

            //Ajout de la serie au dataset
            dataset.addSeries(s);

            //On retourne le dataset
            return dataset;

        } else {
//Comptage par jour
            //Création d'un format pour les date (dd/mm/yyyy)
            SimpleDateFormat formatdate = new SimpleDateFormat("dd/MM/yyyy");

            //Partage des différents formats de la date
            SimpleDateFormat date_jour = new SimpleDateFormat("dd");
            SimpleDateFormat date_mois = new SimpleDateFormat("MM");
            SimpleDateFormat date_annee = new SimpleDateFormat("yyyy");

            //Creation et ajout des TimeSeries dans le dataset
            TimeSeriesCollection dataset = new TimeSeriesCollection();
            TimeSeries s = new TimeSeries("Tweets", Day.class);

            //initialisation du comptage
            int cum = 1;
            String date_cour, date_i;

            //Reccuperation de la première date
            int dateDay = Integer.parseInt(date_jour.format(dates.get(0)));
            int dateMonth = Integer.parseInt(date_mois.format(dates.get(0)));
            int dateYear = Integer.parseInt(date_annee.format(dates.get(0)));
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
                    s.add(new Day(dateDay, dateMonth, dateYear), cum);
                    dateDay = Integer.parseInt(date_jour.format(dates.get(i)));
                    dateMonth = Integer.parseInt(date_mois.format(dates.get(i)));
                    dateYear = Integer.parseInt(date_annee.format(dates.get(i)));
                    date_cour = date_i;
                    cum = 1;
                }
            }
            //Fin de la boucle, on ajoute la dernière date à la serie
            s.add(new Day(dateDay, dateMonth, dateYear), cum);

            //Ajout de la serie au dataset
            dataset.addSeries(s);

            //On retourne le dataset
            return dataset;

        }

    }

}