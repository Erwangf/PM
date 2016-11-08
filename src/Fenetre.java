// import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JButton;

public class Fenetre extends JFrame {
	// private Panneau pan = new Panneau();
	JPanel pan = new JPanel(); /* Instanciation d'un objet JPanel */
	private JButton bouton = new JButton("Mon bouton");
	
	public Fenetre(){
//	    this.setTitle("Fenêtre"); /* Titre */
//	    this.setSize(400, 500); /* Taille de la fenêtre */
//	    this.setLocationRelativeTo(null); /* Position au centre */ 
//	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); /* Ferme la fenêtre lorsque l'on clique sur la croix rouge */ 
//	    
// 		pan.setBackground(Color.WHITE); /* Couleur de fond */   
//      this.setContentPane(pan); /* On prévient notre JFrame que notre JPanel sera son content pane */
//        
//      this.setContentPane(new Panneau()); /* Appel Panneau */
//	    this.setVisible(true); /* Affiche la fenêtre */ 
    
		  this.setTitle("Animation");
	      this.setSize(1000, 500);
	      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      this.setLocationRelativeTo(null);
	      //Ajout du bouton à notre content pane
	      //On définit le layout à utiliser sur le content pane
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
	      this.getContentPane().add(new JButton("4"));
	      this.getContentPane().add(new JButton("5"));
	      this.setVisible(true);
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
		  //Les coordonnées de départ de notre rond
		  int x = pan.getPosX(), y = pan.getPosY();
		  //Le booléen pour savoir si l'on recule ou non sur l'axe x
		  boolean backX = false;
		  //Le booléen pour savoir si l'on recule ou non sur l'axe y
		  boolean backY = false;

		  //Dans cet exemple, j'utilise une boucle while
		  //Vous verrez qu'elle fonctionne très bien
		  while(true){
		    //Si la coordonnée x est inférieure à 1, on avance
		    if(x < 1)
		      backX = false;

		    //Si la coordonnée x est supérieure à la taille du Panneau moins la taille du rond, on recule
		    if(x > pan.getWidth()-50)
		      backX = true;

		    //Idem pour l'axe y
		    if(y < 1)
		      backY = false;
		    if(y > pan.getHeight()-50)
		      backY = true;

		    //Si on avance, on incrémente la coordonnée
		    //backX est un booléen, donc !backX revient à écrire
		    //if (backX == false)
		    if(!backX)
		      pan.setPosX(++x);

		    //Sinon, on décrémente
		    else
		      pan.setPosX(--x);

		    //Idem pour l'axe Y
		    if(!backY)
		      pan.setPosY(++y);
		    else
		      pan.setPosY(--y);

		    //On redessine notre Panneau
		    pan.repaint(); 

		    //pause de trois millièmes de seconde
		    try {
		      Thread.sleep(1);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		  } 
		} */
		  
}
