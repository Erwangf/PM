package core;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import modules.data.Mongo;
import modules.data.Tweet;
import modules.data.TwitterSearch;
import modules.tweetList.TweetListModule;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;

public class MainFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1200, 800);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 2, 10, 10));
		
		
		// MongoDB Base
		Mongo base = new Mongo();
		base.ConnexionMongo("ds147537.mlab.com",47537,"twitter_rumors", "Twitter", "root", "TwitterMongo2016");
		
		
		
		TweetListModule tlm = new TweetListModule();
		contentPane.add(tlm);
		tlm.setMonboBase(base);
		tlm.showLatestTweets();
		
	}

}
