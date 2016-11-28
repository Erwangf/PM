package modules.keywords;

import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.bson.Document;
import org.json.JSONException;

import modules.data.Tweet;

public class Data extends JFrame implements ActionListener{

	public  void main(String[] args) {
		// TODO Auto-generated method stub

		 JFrame fenetre = new JFrame();
         
		    //Définit un titre pour notre fenêtre
		    fenetre.setTitle("Ma première fenêtre Java");
		    //Définit sa taille : 400 pixels de large et 100 pixels de haut
		    fenetre.setSize(400, 200);
		    //Nous demandons maintenant à notre objet de se positionner au centre
		    fenetre.setLocationRelativeTo(null);
		    //Termine le processus lorsqu'on clique sur la croix rouge
		    fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
		    JButton bouton= new JButton("Faire une nouvelle recherche");
	        
		    fenetre.add(bouton);
		    
		    
	        bouton.setVisible(true);
		    
		    fenetre.setLayout(new FlowLayout ());  
		    TextArea t = new TextArea("5 lignes et 50 colonnes",3,35);  
		    TextArea t2 = new TextArea("5 lignes et 50 colonnes",3,35);     
		    bouton.addActionListener(this);
		    fenetre.add(t);  
		    fenetre.add(t2); 
		    fenetre.show();  
		    fenetre.pack();  
		    
		    go();
		    
		    //Et enfin, la rendre visible        
		    fenetre.setVisible(true);
		    
		    // Il faut mettre ca dans le bouton :
		    /*
		     * String text1=t.getText();
		    String text2=t.getText();*/
		     
		    
		    
		    //export_csv();
	}
	
	
	
	  private void go(){
		    //Cette méthode ne change pas
		  }
		 
		   public void actionPerformed(ActionEvent arg0,int para1, String para2) {
		    //Lorsque l'on clique sur le bouton, on met à jour le JLabel
			   modules.data.TwitterSearch ex2 =new modules.data.TwitterSearch();
			   
			   try {
				ex2.getTweetsFromTwitter(para1, para2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		  }      
		


	public static void export_csv2(){
		
		//cr�ation de l'instance mongo
		modules.data.Mongo base = new modules.data.Mongo();
			
			//connexion � la base distante
			base.ConnexionMongo("ds147537.mlab.com",47537,"twitter_rumors", "Twitter", "root", "TwitterMongo2016");
			
			ArrayList<Tweet> tweets = base.GetTweetsBlocks(1);  
			
			int i=100;
			int j=2;
			while(tweets.size()==i){
				tweets.addAll(base.GetTweetsBlocks(j));
				j++;

				i=i+100;
			} 
		
		modules.data.TweetExport ex =new modules.data.TweetExport();
		//ArrayList<Tweet> tn =new ArrayList<Tweet>();
		ex.writeToCSV(tweets, "C:/Users/hamza/Desktop/MonFichier.csv");
		System.out.println("FIN");
	}
	
	
	public static void export_csv(){
		try{
			//cr�ation de l'instance mongo
			modules.data.Mongo base = new modules.data.Mongo();
				
				//connexion � la base distante
				base.ConnexionMongo("ds147537.mlab.com",47537,"twitter_rumors", "Twitter", "root", "TwitterMongo2016");
				
				ArrayList<Tweet> tweets = base.GetTweetsBlocks(1);  
				
				int i=100;
				int j=2;
				while(tweets.size()==i){
					tweets.addAll(base.GetTweetsBlocks(j));
					j++;

					i=i+100;
				} 
			
		FileWriter fileWriter = new FileWriter("C:/Users/hamza/Desktop/MonFichier.csv");

		/*int cc=0;
		for (Tweet v : tweets){
			cc++;
			fileWriter.append("\r"+cc+"\r");
		fileWriter.append("\r"+v.toString()+"\r");

		System.out.println(v);
		}
		System.out.println(tweets.size());
		fileWriter.close();*/
		
		
		BufferedWriter tampon = new BufferedWriter(new FileWriter("C:/Users/hamza/Desktop/MonFichier.csv"));
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



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
