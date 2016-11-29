package modules.tweetList;

import modules.data.Mongo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TweetExplorerModule extends JPanel {

    private Mongo base;
    private TweetListModule tlm;
    private int nbPages = 1;
    private final JTextArea searchArea;

    public TweetExplorerModule() {
        super();
        this.tlm = new TweetListModule();
        this.setLayout(new BorderLayout(0, 0));

        //Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout(0, 0));
        JLabel label = new JLabel("Mots clés :");
        add(label, BorderLayout.SOUTH);


        this.add(headerPanel, BorderLayout.NORTH);
        JLabel label_1 = new JLabel("Tweet Explorer");
        headerPanel.add(label_1, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        headerPanel.add(panel, BorderLayout.CENTER);
        searchArea = new JTextArea();
        searchArea.setColumns(30);
        panel.add(searchArea);
        JButton searchBtn = new JButton("Rechercher");
        panel.add(searchBtn);
        searchBtn.addActionListener(e -> launchSearch());
        searchArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    searchBtn.doClick();

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        //Center Panel
        this.add(tlm, BorderLayout.CENTER);
    }

    // =========================================================
    //     METHODS
    // =========================================================


    /**
     * Initialize la l'explorateur de Tweets avec les derniers Tweets
     */
    public void initialize() {
        if (base == null) throw new Error("Erreur, pas de base de donnée connectée");
        tlm.RemoveAllTweet();
        tlm.setUpdateFunction((t) -> {
            base.UpdateMongoItem(t);
        });
        base.GetTweetsBlocks(nbPages).forEach(tlm::AddTweet);
    }

    private void launchSearch() {
        String[] keywords = searchArea.getText().split(" ");
        if (keywords.length != 0) {
            tlm.RemoveAllTweet();
            base.GetTweetsKeyWordsArray(keywords).forEach(tlm::AddTweet);
        } else {
            initialize();
        }

    }


    // =========================================================
    //     GETTERS & SETTERS
    // =========================================================

    public Mongo getBase() {
        return base;
    }

    public void setBase(Mongo base) {
        this.base = base;
    }

    public TweetListModule getTlm() {
        return tlm;
    }
}
