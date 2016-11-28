package core;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import modules.keywords.Interface_data;
import modules.keywords.Keywords;
import modules.keywords.Trend;
import modules.tweetList.TweetExplorerModule;
import org.json.JSONException;

import modules.data.Mongo;
import modules.data.Tweet;
import modules.data.TwitterSearch;
import modules.tweetList.TweetListModule;
import modules.keywords.Keywords;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;

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
                    frame.pack();
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
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1200, 800);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu_Accueil = new JMenu("Accueil");
		menuBar.add(menu_Accueil);
		
		JMenu menuAide = new JMenu("Aide");
		menuBar.add(menuAide);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 2, 10, 10));
		
		
		// MongoDB Base
		Mongo base = new Mongo();
		base.ConnexionMongoDefault();
		
		

		//Tweet Explorer Module
		TweetExplorerModule tem = new TweetExplorerModule();
		contentPane.add(tem);
		tem.setBase(base);
		tem.initialize();

		//Keywords module

		Keywords kw = new Keywords();
		kw.setBase(base);

		contentPane.add(kw);

		//Trends module

		Trend tr = new Trend();
		tr.setBase(base);
		contentPane.add(tr);


		//Data module

        Interface_data intD = new Interface_data();
        intD.setBase(base);
        contentPane.add(intD);

        this.setVisible(true);





		//start
		tr.initialize();


        System.out.println("TR LOADED !");
        Thread _t = new Thread(new Runnable() {
            @Override
            public void run() {
                kw.initialize();
                System.out.println("LOADED !");
                pack();
            }
        });
        _t.start();





		//Module Keyword
		//Keywords kw = new Keywords();
		//contentPane.add(kw);
		//kw.setMonboBase(base);

		
		
		
		
	}

}
