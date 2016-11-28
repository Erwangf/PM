package modules.keywords;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import modules.data.Tweet;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class Interface_data extends JFrame {

	private JPanel contentPane;
	private JTextField text_mots;
	private JTextField nb_tweet;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Interface_data frame = new Interface_data();
					frame.setTitle("DATA");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Interface_data() {
		//cr�ation de l'instance mongo
		modules.data.Mongo base = new modules.data.Mongo();
		//connexion � la base distante
		base.ConnexionMongo("ds147537.mlab.com", 47537, "twitter_rumors", "Twitter", "root",
				"TwitterMongo2016");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, -47, 487, 265);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Combien de tweets voulez-vous recuperer ?");
		lblNewLabel.setBounds(12, 126, 261, 22);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Veuillez saisir les mots clés :");
		lblNewLabel_1.setBounds(12, 163, 182, 16);
		contentPane.add(lblNewLabel_1);
		
		text_mots = new JTextField();
		text_mots.setBounds(192, 161, 116, 22);
		contentPane.add(text_mots);
		text_mots.setColumns(10);
		
		JComboBox Liste_nbre = new JComboBox();
		Liste_nbre.setModel(new DefaultComboBoxModel(new String[] {"10", "20", "50", "100", "200", "500", "1000", "5000", "10000"}));
		Liste_nbre.setBounds(265, 126, 68, 22);
		contentPane.add(Liste_nbre);
				
		JButton Bvalider = new JButton("Lancer la recherche");
		Bvalider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nb=Liste_nbre.getSelectedItem().toString();
				String mots_cles=text_mots.getText();		

				int i = Integer.parseInt(nb);
				//System.out.println(Liste_nbre.getSelectedItem().toString());
				
				modules.data.TwitterSearch base2 = new modules.data.TwitterSearch();
				ArrayList<Tweet> TweetList=new ArrayList<Tweet>();
				try {
					TweetList=base2.getTweetsFromTwitter(i, mots_cles);
					//base.InsertMongo(TweetList);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// + ALIMENTER LA BASE DE DONNÉES DES TWEETS RECUPERES
				/*int h=1;
				for(Tweet v : TweetList){
					System.out.println(v);
					h++;
				}*/
				
			}
		});
		Bvalider.setBounds(95, 192, 153, 25);
		contentPane.add(Bvalider);
		
		JButton Bexport = new JButton("EXPORTER");
		Bexport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					/*//cr�ation de l'instance mongo
					modules.data.Mongo base = new modules.data.Mongo();
						
						//connexion � la base distante
						base.ConnexionMongo("ds147537.mlab.com",47537,"twitter_rumors", "Twitter", "root", "TwitterMongo2016");
						*/
						ArrayList<Tweet> tweets = base.GetTweetsBlocks(1);  
						
						int i=100;
						int j=2;
						while(tweets.size()==i){
							tweets.addAll(base.GetTweetsBlocks(j));
							j++;

							i=i+100;
						} 		
				
				BufferedWriter tampon = new BufferedWriter(new FileWriter("C:/Users/hamza/Desktop/MonFichier_exp2.csv"));
				PrintWriter sortie = new PrintWriter(tampon);
				for (Tweet v : tweets){
				
				          sortie.println(v);
				}
				sortie.flush();
				sortie.close();
				
				System.out.println("FIN");
				}  catch (IOException exception) {
					exception.printStackTrace();
				}				
			}
		});
		Bexport.setBounds(361, 178, 105, 39);
		contentPane.add(Bexport);
		
		JButton Bdrop = new JButton("DROP");
		Bdrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Methode qui va effacer le contenu de la base de données
			}
		});
		Bdrop.setBounds(361, 99, 105, 39);
		contentPane.add(Bdrop);
		
		JButton Bupdate = new JButton("UPDATE");
		Bupdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			
				try {
					
					long nbre = base.GetNbTweets();
					//Object o = nbre;
					//System.out.println(o);
					//String str = (String)o;
					
					//String nbb = (nb);
					String numberAsString = Long.toString(nbre);
					nb_tweet.setText(numberAsString);
					;
				} catch (Exception e) {
					System.out.println("ERREUR "+ e);
				} 				
				
			}
		});
		Bupdate.setBounds(22, 25, 97, 39);
		contentPane.add(Bupdate);
		
		JLabel Label_nbre = new JLabel("La base de données contient");
		Label_nbre.setBounds(166, 13, 211, 25);
		contentPane.add(Label_nbre);
		
		JLabel lblNewLabel_3 = new JLabel("tweets");
		lblNewLabel_3.setBounds(389, 17, 68, 16);
		contentPane.add(lblNewLabel_3);
		
		nb_tweet = new JTextField();
		nb_tweet.setBounds(338, 14, 51, 22);
		contentPane.add(nb_tweet);
		nb_tweet.setColumns(10);
		
		
		//try {			
			long nbre0 = base.GetNbTweets();
			
			String numberAsString0 = Long.toString(nbre0);
			nb_tweet.setText(numberAsString0);
			;
		/*} catch (Exception e) {
			System.out.println("ERREUR "+ e);
		} */
		
		
	}
}
