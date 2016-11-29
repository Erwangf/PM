package modules.keywords;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import modules.data.Mongo;
import org.json.JSONException;

import modules.data.Tweet;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Insets;
import java.util.function.Consumer;
import javax.swing.SwingConstants;

public class Interface_data extends JPanel {

    private JPanel contentPane;
    private JTextField text_mots;
    private JTextField nb_tweet;

    public void setCallUpdate(Consumer callUpdate) {
        this.callUpdate = callUpdate;
    }

    private Consumer callUpdate;


    public void setBase(Mongo base) {
        this.base = base;
        this.nb_tweet.setText(Long.toString(base.GetNbTweets()));
    }

    private Mongo base;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    JFrame frame = new JFrame();
                    Mongo base = new Mongo();
                    base.ConnexionMongoDefault();

                    Interface_data interf = new Interface_data();
                    frame.setContentPane(interf);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    interf.setBase(base);
                    frame.pack();
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
        super();

        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new BorderLayout(0, 0));

        JPanel topPanel = new JPanel();
        this.add(topPanel, BorderLayout.NORTH);

        JButton Bupdate = new JButton("UPDATE");
        topPanel.add(Bupdate);

        JLabel Label_nbre = new JLabel("La base de données contient");
        topPanel.add(Label_nbre);

        nb_tweet = new JTextField();
        nb_tweet.setHorizontalAlignment(SwingConstants.RIGHT);
        nb_tweet.setColumns(6);
        topPanel.add(nb_tweet);
        nb_tweet.setEditable(false);
        nb_tweet.setText("0");

        JLabel lblNewLabel_3 = new JLabel("tweets");
        topPanel.add(lblNewLabel_3);

        JPanel rightPanel = new JPanel();
        this.add(rightPanel, BorderLayout.EAST);
        rightPanel.setLayout(new GridLayout(0, 1, 0, 0));

        JPanel centerPanel = new JPanel();
        this.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JPanel center_top = new JPanel();
        centerPanel.add(center_top);

        JLabel lblNewLabel = new JLabel("Combien de tweets voulez-vous recuperer ?");
        center_top.add(lblNewLabel);

        JComboBox Liste_nbre = new JComboBox();
        center_top.add(Liste_nbre);
        Liste_nbre.setModel(new DefaultComboBoxModel(new String[]{"10", "20", "50", "100", "200", "500", "1000", "5000", "10000"}));

        JPanel center_center = new JPanel();
        centerPanel.add(center_center);
        center_center.setLayout(new BorderLayout(0, 0));

        JButton Bvalider = new JButton("Lancer la recherche");
        center_center.add(Bvalider, BorderLayout.SOUTH);

        JPanel panel = new JPanel();
        center_center.add(panel, BorderLayout.NORTH);

        JLabel lblNewLabel_1 = new JLabel("Veuillez saisir les mots clés :");
        panel.add(lblNewLabel_1);

        text_mots = new JTextField();
        panel.add(text_mots);
        text_mots.setColumns(10);

        JPanel center_bottom = new JPanel();
        centerPanel.add(center_bottom);

        JButton Bdrop = new JButton("DROP");
        center_bottom.add(Bdrop);

        JButton Bexport = new JButton("EXPORTER");
        center_bottom.add(Bexport);
        Bexport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    /*//cr�ation de l'instance mongo
					modules.data.Mongo base = new modules.data.Mongo();
						
						//connexion � la base distante
						base.ConnexionMongo("ds147537.mlab.com",47537,"twitter_rumors", "Twitter", "root", "TwitterMongo2016");
						*/
                    ArrayList<Tweet> tweets = base.GetTweetsBlocks(1);

                    int i = 100;
                    int j = 2;
                    while (tweets.size() == i) {
                        tweets.addAll(base.GetTweetsBlocks(j));
                        j++;

                        i = i + 100;
                    }
                    System.out.println("1");
                    String pathname = SaveFile();
                    System.out.println(pathname);
                    BufferedWriter tampon = new BufferedWriter(new FileWriter(pathname));
                    PrintWriter sortie = new PrintWriter(tampon);
                    System.out.println("2");
                    for (Tweet v : tweets) {

                        sortie.println(v);
                    }
                    sortie.flush();
                    sortie.close();

                    System.out.println("FIN");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
        Bdrop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Methode qui va effacer le contenu de la base de données
            }
        });
        Bvalider.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nb = Liste_nbre.getSelectedItem().toString();
                String mots_cles = text_mots.getText();

                int i = Integer.parseInt(nb);
                //System.out.println(Liste_nbre.getSelectedItem().toString());

                modules.data.TwitterSearch base2 = new modules.data.TwitterSearch();
                ArrayList<Tweet> TweetList = new ArrayList<Tweet>();
                try {
                    TweetList = base2.getTweetsFromTwitter(i, mots_cles);
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
                    callUpdate.accept(null);
                    ;
                } catch (Exception e) {
                    System.out.println("ERREUR " + e);
                }

            }
        });
        ;
		/*} catch (Exception e) {
			System.out.println("ERREUR "+ e);
		} */


    }

    public String SaveFile() {

        String pathname, path = "", name = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(null);
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            name = chooser.getSelectedFile().getName();
            path = chooser.getSelectedFile().getParent();
        }
        if (name.substring(name.length() - 4, name.length()) != ".csv") {
            name = name + ".csv";
        }
        pathname = path + "/" + name;
        return pathname;
    }

}
