package modules.tweetList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

import org.json.JSONException;

import lib.Tweet;
import lib.TwitterSearch;

import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JSeparator;

public class TweetListModule extends JPanel {

    private JPanel mainList;
    private int prefSizeX = 600;
    private int prefSizeY = 400;

    public TweetListModule() {
    	
        setLayout(new BorderLayout());
        mainList = new JPanel();
        mainList.setBackground(Color.WHITE);
        JScrollPane jsp = new JScrollPane(mainList);
        GridBagLayout gbl_mainList = new GridBagLayout();
        gbl_mainList.columnWidths = new int[]{0, 0};
        gbl_mainList.rowHeights = new int[]{0, 0};
        gbl_mainList.columnWeights = new double[]{0.0, Double.MIN_VALUE};
        gbl_mainList.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        mainList.setLayout(gbl_mainList);
        
        
        add(jsp);
        
        

    }
    

    public void AddTweet(Tweet t){
    	TweetCell panel = new TweetCell(t);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5,0,5,0);
        mainList.add(panel, gbc, 0);
        

        validate();
        repaint();
    }
    
    public void RemoveAllTweet(){
    	mainList.removeAll();
    	validate();
        repaint();
    }
    
    
    

    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(prefSizeX, prefSizeY);
    }
    
    
    //exemple d'utilisation de la classe TweetListModule
    public static void main(String[] args){
    	//permet de démarrer créer l'interface graphique plus tard, quand le reste a été fait. Plus propre, évite des erreurs
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }

                //création d'une fenêtre pour afficher le module
                JFrame frame = new JFrame("Test Tweet List");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //Récupération des tweets
                ArrayList<Tweet> alt = new ArrayList<Tweet>();
                try {
					 alt = TwitterSearch.getTweetsFromTwitter(50, "cancer%20graviola");
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                //création du module
                TweetListModule tweetList = new TweetListModule();
                //remplissage avec les tweets téléchargés
                alt.forEach((t)->{
                	tweetList.AddTweet(t);
                	});
               //ajout du module à la fênetre
                frame.getContentPane().add(tweetList);
                frame.pack();
                //centrage de la fenêtre
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
