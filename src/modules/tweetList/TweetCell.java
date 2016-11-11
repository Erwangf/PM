package modules.tweetList;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.View;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseWheelListener;
import java.util.Date;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.DropMode;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import com.jgoodies.forms.factories.DefaultComponentFactory;

import modules.data.Tweet;

import java.awt.SystemColor;

public class TweetCell extends JPanel{
	
	private Tweet associatedTweet;
	
	 
	

	public TweetCell(Tweet associatedTweet){
		super();
		this.associatedTweet = associatedTweet;
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout(0, 0));
        this.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(153, 204, 255));
		
        add(headerPanel, BorderLayout.NORTH);
        headerPanel.setLayout(new GridLayout(0, 2, 0, 0));
        
        JLabel userLabel = new JLabel(associatedTweet.getUser());
        userLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(userLabel);
        
        JLabel dateLabel = new JLabel((new Date(associatedTweet.getTimeStamp().getTime())).toString());
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
        add(centerPanel,BorderLayout.CENTER);
        
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(153, 204, 255));
        add(footerPanel, BorderLayout.SOUTH);
        footerPanel.setLayout(new GridLayout(0, 3, 0, 0));
        
        JLabel nbRepliesLabel = new JLabel(associatedTweet.getNbResponses()+" Replies");
        nbRepliesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(nbRepliesLabel);
        
        JLabel nbRetweetLabel = new JLabel(associatedTweet.getNbRetweets()+" Retweets");
        nbRetweetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(nbRetweetLabel);
        
        JLabel nbLikeLabel = new JLabel(associatedTweet.getNbLikes()+" Likes");
        nbLikeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(nbLikeLabel);
        
        
	}
}
