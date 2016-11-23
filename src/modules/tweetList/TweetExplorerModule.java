package modules.tweetList;

import modules.data.Mongo;

import javax.swing.*;
import java.awt.*;

public class TweetExplorerModule extends JPanel {

    private Mongo base;
    private TweetListModule tlm;
    private int nbPages = 1;

    public TweetExplorerModule() {
        super();
        this.tlm = new TweetListModule();
        this.setLayout(new BorderLayout(0,0));

        //Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Tweet Explorer"));
        this.add(headerPanel,BorderLayout.NORTH);

        //Center Panel
        this.add(tlm,BorderLayout.CENTER);
    }

    // =========================================================
    //     METHODS
    // =========================================================


    public void initialize(){
        if(base==null) throw new Error("Erreur, pas de base de donnée connectée");
        tlm.RemoveAllTweet();
        base.GetTweetsBlocks(nbPages).forEach(tlm::AddTweet);
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
