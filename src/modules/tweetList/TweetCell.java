package modules.tweetList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseWheelListener;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import modules.data.Tweet;

@SuppressWarnings("serial")
public class TweetCell extends JPanel{
	

	private Tweet associatedTweet;
	
	 
	

	public TweetCell(Tweet _associatedTweet){
		super();
		associatedTweet = _associatedTweet;
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
        
        JLabel dateLabel = new JLabel((new Date(associatedTweet.getTimeStamp().getTime()*1000)).toString());
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
        footerPanel.setBackground(new Color(223, 223, 223));
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
