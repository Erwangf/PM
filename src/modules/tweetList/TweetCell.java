package modules.tweetList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelListener;
import java.util.Date;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.border.MatteBorder;

import modules.data.OpinionMining;
import modules.data.Tweet;

@SuppressWarnings("serial")
public class TweetCell extends JPanel {

    private final Color positiveColor = new Color(4, 175, 67);
    private final Color negativeColor = new Color(229,95,95);
    private final Color nullColor = new Color(194, 192, 176);
    private final Color neutralColor = new Color(229, 218, 91);


    private Tweet associatedTweet;
    private final JPanel headerPanel;

    public void setUpdateFunction(Consumer<Tweet> updateFunction) {
        this.updateFunction = updateFunction;
    }

    private Consumer<Tweet> updateFunction;


    private void updatePrevision(){
        if(OpinionMining.scoreIndex!=null && associatedTweet.getNote()==null){
            Color c;
            switch (OpinionMining.getPrevision_v2(associatedTweet.getContent())){
                case 1:
                    c = positiveColor;
                    break;
                case 0:
                    c = neutralColor;
                    break;
                case -1:
                    c = negativeColor;
                    break;
                default:
                    c = nullColor;

            }
            headerPanel.setBackground(c);

        }
    }


    public TweetCell(Tweet _associatedTweet) {
        super();
        associatedTweet = _associatedTweet;
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout(0, 0));
        this.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(153, 204, 255));

        add(headerPanel, BorderLayout.NORTH);
        headerPanel.setLayout(new GridLayout(0, 2, 0, 0));

        JLabel userLabel = new JLabel(associatedTweet.getUser());
        userLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(userLabel);

        JLabel dateLabel = new JLabel((new Date(associatedTweet.getTimeStamp().getTime() * 1000)).toString());
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(dateLabel);

        JLabel usernameLabel = new JLabel(associatedTweet.getUsername());
        headerPanel.add(usernameLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(204, 255, 255));
        centerPanel.setAutoscrolls(false);
        centerPanel.setLayout(new GridLayout(0, 1, 0, 0));

        JTextArea text = new JTextArea();
        text.setBackground(new Color(211, 211, 211));
        text.setWrapStyleWord(true);
        text.setColumns(1);
        text.setEnabled(true);
        text.setEditable(false);
        text.setLineWrap(true);

        text.setText(associatedTweet.getContent());
        JScrollPane scroll = new JScrollPane(text);
        for (MouseWheelListener mwl : scroll.getMouseWheelListeners()) {
            scroll.removeMouseWheelListener(mwl);
        }

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setWheelScrollingEnabled(true);
        centerPanel.add(scroll); //add the JScrollPane to the panel
        add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(223, 223, 223));
        add(footerPanel, BorderLayout.SOUTH);
        footerPanel.setLayout(new GridLayout(0, 3, 0, 0));

        JLabel nbRepliesLabel = new JLabel(associatedTweet.getNbResponses() + " Replies");
        nbRepliesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(nbRepliesLabel);

        JLabel nbRetweetLabel = new JLabel(associatedTweet.getNbRetweets() + " Retweets");
        nbRetweetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(nbRetweetLabel);

        JLabel nbLikeLabel = new JLabel(associatedTweet.getNbLikes() + " Likes");
        nbLikeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(nbLikeLabel);

        JPanel notePanel = new JPanel();
        add(notePanel, BorderLayout.EAST);
        notePanel.setLayout(new GridLayout(0, 2, 0, 0));

        JLabel lblNote = new JLabel("Note :");
        notePanel.add(lblNote);

        JRadioButton rbPlus = new JRadioButton("+");
        JRadioButton rb0 = new JRadioButton("0");
        JRadioButton rbMinus = new JRadioButton("-");
        JRadioButton rbNull = new JRadioButton("?");

        //action command ( on leur file une valeur )
        rbPlus.setActionCommand("1");
        rb0.setActionCommand("0");
        rbMinus.setActionCommand("-1");
        rbNull.setActionCommand("null");

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbPlus);
        bg.add(rb0);
        bg.add(rbMinus);
        bg.add(rbNull);
        
        JLabel label = new JLabel(" ");
        notePanel.add(label);


        notePanel.add(rbPlus);
        notePanel.add(rb0);
        notePanel.add(rbMinus);
        notePanel.add(rbNull);

        ActionListener updateTweetNote = e -> {
            if (e.getActionCommand().equals("null")) {
                associatedTweet.setNote(null);
            } else {
                associatedTweet.setNote(Float.parseFloat(e.getActionCommand()));
            }
            System.out.println(associatedTweet);
            updateFunction.accept(associatedTweet);

        };

        rbPlus.addActionListener(updateTweetNote);
        rb0.addActionListener(updateTweetNote);
        rbMinus.addActionListener(updateTweetNote);
        rbNull.addActionListener(updateTweetNote);

        if (associatedTweet.getNote() == null) {
            rbNull.setSelected(true);
        } else {
            switch (associatedTweet.getNote().intValue()) {
                case 1:
                    rbPlus.setSelected(true);
                    break;
                case 0:
                    rb0.setSelected(true);
                    break;
                case -1:
                    rbMinus.setSelected(true);
                    break;
                default:
                    rbNull.setSelected(true);
            }
        }

        updatePrevision();




    }


}
