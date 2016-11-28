package modules.keywords;
// import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class Fenetre extends JFrame {
	// private Panneau pan = new Panneau();
	JPanel pan = new JPanel(); /* Instanciation d'un objet JPanel */
	private JButton bouton = new JButton("Afficher un message");
	private JButton btnCount = new JButton("compteur de tweet");
	private JButton btnEnreg = new JButton("fenetre enregistrement fichier");
	private JButton btnTwitterSearch = new JButton("twitter search");
	private JButton btnClear = new JButton("vide la BD");
	
	public Fenetre(){
//	    this.setTitle("Fen�tre"); /* Titre */
//	    this.setSize(400, 500); /* Taille de la fenêtre */
//	    this.setLocationRelativeTo(null); /* Position au centre */ 
//	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); /* Ferme la fen�tre lorsque l'on clique sur la croix rouge */ 
//	    
// 		pan.setBackground(Color.WHITE); /* Couleur de fond */   
//      this.setContentPane(pan); /* On pr�vient notre JFrame que notre JPanel sera son content pane */
//        
//      this.setContentPane(new Panneau()); /* Appel Panneau */
//	    this.setVisible(true); /* Affiche la fen�tre */ 
    
		  this.setTitle("Animation");
	      this.setSize(1000, 500);
	      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      this.setLocationRelativeTo(null);
	      //Ajout du bouton � notre content pane
	      //On d�finit le layout � utiliser sur le content pane
	      //Trois lignes sur deux colonnes
	      GridLayout gl = new GridLayout();
	      gl.setColumns(2); // 2 colonnes
	      gl.setRows(3); // 3 lignes
	      this.setLayout(gl);
	      gl.setHgap(10); //dix pixels d'espace entre les colonnes (H comme Horizontal)
	      gl.setVgap(10);
	      //On ajoute le bouton au content pane de la JFrame
	      this.getContentPane().add(new JButton("Button 1"));
	      this.getContentPane().add(new JButton("2"));
	      this.getContentPane().add(new JButton("3"));
	      this.getContentPane().add(btnEnreg);
	      this.getContentPane().add(btnCount);
	      this.getContentPane().add(bouton);
	      this.setVisible(true);
	      
	      bouton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				afficherMsg();
				
			}
			
			
		});
	      
	      btnCount.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				ftnCount();
				
			}
			
			
		});
	      
	      btnEnreg.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				ftnBteDial();
				
			}
			
			
		});
	      
	 }
	 
	 /* ANIMATION LIGNE DE CERCLE ROUGE 
	 public Fenetre(){        
	    this.setTitle("Animation");
	    this.setSize(300, 300);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    this.setContentPane(pan);
	    this.setVisible(true);
	    go();
	 } */

	 /* private void go(){
	    for(int i = -50; i < pan.getWidth(); i++){
	      int x = pan.getPosX(), y = pan.getPosY();
	      x++;
	      y++;
	      pan.setPosX(x);
	      pan.setPosY(y);
	      pan.repaint();  
	      try {
	        Thread.sleep(10); 
	      } catch (InterruptedException e) {
	        e.printStackTrace();
	      }
	    }
	  } */
	 
	 /* private void go(){
		  //Les coordonn�es de d�part de notre rond
		  int x = pan.getPosX(), y = pan.getPosY();
		  //Le bool�en pour savoir si l'on recule ou non sur l'axe x
		  boolean backX = false;
		  //Le bool�en pour savoir si l'on recule ou non sur l'axe y
		  boolean backY = false;

		  //Dans cet exemple, j'utilise une boucle while
		  //Vous verrez qu'elle fonctionne tr�s bien
		  while(true){
		    //Si la coordonn�e x est inf�rieure � 1, on avance
		    if(x < 1)
		      backX = false;

		    //Si la coordonn�e x est sup�rieure � la taille du Panneau moins la taille du rond, on recule
		    if(x > pan.getWidth()-50)
		      backX = true;

		    //Idem pour l'axe y
		    if(y < 1)
		      backY = false;
		    if(y > pan.getHeight()-50)
		      backY = true;

		    //Si on avance, on incr�mente la coordonn�e
		    //backX est un bool�en, donc !backX revient � �crire
		    //if (backX == false)
		    if(!backX)
		      pan.setPosX(++x);

		    //Sinon, on d�cr�mente
		    else
		      pan.setPosX(--x);

		    //Idem pour l'axe Y
		    if(!backY)
		      pan.setPosY(++y);
		    else
		      pan.setPosY(--y);

		    //On redessine notre Panneau
		    pan.repaint(); 

		    //pause de trois milli�mes de seconde
		    try {
		      Thread.sleep(1);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		  } 
		} */
		
	public void afficherMsg(){
		System.out.println("Vous avez cliqué sur le bouton !!");
	}
	
	public void ftnCount(){
		System.out.println("La fonction Count marche correctement !!");
		modules.data.Mongo base = new modules.data.Mongo();
		base.ConnexionMongo("ds147537.mlab.com",47537,"twitter_rumors", "Twitter", "root", "TwitterMongo2016");
		int nbTweet = (int) base.GetNbTweets();
		
		System.out.println("Il y a " + nbTweet + " tweets dans la base de données MongoDB.");
	}
	
	public void ftnBteDial(){
		JFileChooser saveFile = new JFileChooser();
		//saveFile.showSaveDialog(null);
		//saveFile.showOpenDialog(null);
		
	    JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	    		"csv files", "csv");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showSaveDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            chooser.getSelectedFile().getName());
	       System.out.println("You chose to open this path: " +
		            chooser.getSelectedFile().getParent());
	    }
	}
}
