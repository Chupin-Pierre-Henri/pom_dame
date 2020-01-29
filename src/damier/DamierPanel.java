package damier;

import click.ClickAction;
import jeu.Jeu;

import javax.swing.*;
import java.awt.*;


public class DamierPanel extends JPanel {
	//tableau de type damier qui permet l'afichage du damier graphique
	int[][] mat;
	//diff�rentes images
	// S=S�lectionn�	P=qui peut Prendre
	Image noir,blanc,pionN,pionB,pionBS,pionNS,pionBP;

	public DamierPanel(int[][] mat, ClickAction listener){
		//initialisation des variables
		this.mat=mat;
		//permet de g�rer la gestion des cick dans la classe ClickAction
		addMouseListener(listener);
		Toolkit kit=Toolkit.getDefaultToolkit();
		MediaTracker tracker=new MediaTracker(this);
		this.blanc=kit.getImage("src/damier/blanc.jpg");
		this.noir=kit.getImage("src/damier/noir.jpg");
		this.pionB=kit.getImage("src/damier/pionB.jpg");
		this.pionN=kit.getImage("src/damier/pionN.jpg");
		this.pionBS=kit.getImage("src/damier/pionBS.jpg");
		this.pionNS=kit.getImage("src/damier/pionNS.jpg");
		this.pionBP=kit.getImage("src/damier/pionBP.jpg");
		tracker.addImage(blanc,0);
		tracker.addImage(noir,0);
		tracker.addImage(pionB,0);
		tracker.addImage(pionN,0);
		try {tracker.waitForID(0);}
		catch(InterruptedException e){}
	}

	public void paintComponent(Graphics g){

		int i,j;
		//Pour chaque ligne
		for (i=0; i< Jeu.NBCASES; i++){
			//pour chaque colonne
			for (j=0; j< Jeu.NBCASES; j++){
				//-10 -> case blanche
				if (this.mat[i][j]==-10){
					g.drawImage(this.blanc,j* Jeu.TAILLEIM,i* Jeu.TAILLEIM,null);
				}
//				0 -> case noir
				else if(this.mat[i][j]==0){
					g.drawImage(this.noir,j* Jeu.TAILLEIM,i* Jeu.TAILLEIM,null);
				}
//				1 -> pion blanc
				else if(this.mat[i][j]==1){
					g.drawImage(this.pionB,j* Jeu.TAILLEIM,i* Jeu.TAILLEIM,null);
				}
//				-1 -> pion noir
				else if(this.mat[i][j]==-1){
					g.drawImage(this.pionN,j* Jeu.TAILLEIM,i* Jeu.TAILLEIM,null);
				}
//				2 -> pion blanc selectionn�
				else if(this.mat[i][j]==2){
					g.drawImage(this.pionBS,j* Jeu.TAILLEIM,i* Jeu.TAILLEIM,null);
				}
//				-2 -> pion noir s�lectionn�
				else if(this.mat[i][j]==-2){
					g.drawImage(this.pionNS,j* Jeu.TAILLEIM,i* Jeu.TAILLEIM,null);
				}
//				5 -> pion blanc qui peut prendre
				else if(this.mat[i][j]==5){
					g.drawImage(this.pionBP,j* Jeu.TAILLEIM,i* Jeu.TAILLEIM,null);
				}
			}
		}
		repaint();
		
	}
	//permet de r�initiliser la variable de damier
	public void rafraichir(int[][] mat){
		this.mat=mat;
	}

}
