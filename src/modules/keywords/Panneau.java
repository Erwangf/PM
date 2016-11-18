package modules.keywords;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
 
public class Panneau extends JPanel { 
	private int posX = -50; /* Déclaration des variables */
	private int posY = -50;
	
	// public void paintComponent(Graphics g){
	// g.setColor(Color.red);
	// g.fillOval(posX, posY, 50, 50);
	//	}
	
	public void paintComponent(Graphics g){
	    // g.setColor(Color.white); /* couleur de fond */
	    g.fillRect(0, 0, this.getWidth(), this.getHeight()); /* toute la surface */
	    g.setColor(Color.black); /* couleur du rond */
	    g.fillOval(posX, posY, 50, 50); /* coordonées souhaitées */
	  }


	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

}

	//import java.awt.Color;
	//import java.awt.Font;
	//import java.awt.Graphics;
	//import java.awt.Graphics2D;
	//import java.awt.Image;
	//import java.io.File;
	//import java.io.IOException;
	//import java.awt.GradientPaint;
	//import javax.imageio.ImageIO;
	//import javax.swing.JPanel;
	
	// public void paintComponent(Graphics g){
    // Vous verrez cette phrase chaque fois que la méthode sera invoquée
	// System.out.println("Je suis morte !"); 
	// g.fillOval(20, 20, 75, 75); /* Trace un rond plein en commençant à dessiner sur l'axe x à 20 pixels et sur l'axe y à 20 pixels, et fais en sorte qu'il occupe 75 pixels de large et 75 pixels de haut */
    
	/* FORMES */ 
    // int x1 = this.getWidth()/4; 
    // int y1 = this.getHeight()/4;               
	  
    // g.fillOval(x1, y1, this.getWidth()/2, this.getHeight()/2); /* cercle pleins */
    // g.drawOval(x1, y1, this.getWidth()/2, this.getHeight()/2); /* cercle vide */
    // g.drawRect(10, 10, 50, 60); /* carré vide */
    // g.fillRect(65, 65, 30, 40); /* carré pleins */ 
    // g.drawRoundRect(10, 10, 30, 50, 10, 10); /* rectangle arrondi vide */
    // g.fillRoundRect(55, 65, 55, 30, 5, 5); /* rectangle arrondi pleins */
    // g.drawLine(0, 0, this.getWidth(), this.getHeight()); /* gauche vers droite */ 
    // g.drawLine(0, this.getHeight(), this.getWidth(), 0); /* droite vers gauche */ 
    
	// int x[] = {20, 30, 50, 60, 60, 50, 30, 20};
    // int y[] = {30, 20, 20, 30, 50, 60, 60, 50};
    // g.drawPolygon(x, y, 8); /* polygone vide */
    // int x2[] = {50, 60, 80, 90, 90, 80, 60, 50};
    // int y2[] = {60, 50, 50, 60, 80, 90, 90, 80};
    // g.fillPolygon(x2, y2, 8); /* polygone pleins */
	  
	// Font font = new Font("Courier", Font.BOLD, 20); /* police d'écriture */ 
	// g.setFont(font); /* fond */
	// g.setColor(Color.PINK); /* couleur d'écriture */
	// g.drawString("Morgane la Princesse, c'est vraiment la plus belle de toutes ! ", 10, 20); /* Affichage de texte */ 

	// try { CASSE !!!!
	//	    Image img = ImageIO.read(new File("images.jpg"));
	//	     // g.drawImage(img, 0, 0, this);
	//	     g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this); /* Pour une image de fond */
	//	} catch (IOException e) {
	//	      e.printStackTrace();
	//	}  
	  
	// Graphics2D g2d = (Graphics2D)g;         
	// GradientPaint gp = new GradientPaint(0, 0, Color.PINK, 0,100, Color.BLACK, true);                
	// g2d.setPaint(gp);
	// g2d.fillRect(0, 0, this.getWidth(), this.getHeight());  
	 
	// }
