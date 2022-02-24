package spaceCloser;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GraficxClass extends JPanel{

	//the number of points in the map of the game.
	final int width = 50;
	final static int hight = 25;

	//
	private int timesToPlay = 5;
	private int level = 1;
	private int mapPrecents = 0;
	private int mapCovered = 0;

	//
	private Lib[][] map = new Lib[hight][width];

	//keep all the eaten libs
	private List<Lib> eating = new ArrayList<>();

	//keeps all the colors in an array in order to get them in the right order.
	private Color[] colors = {Color.BLUE , Color.GREEN , Color.RED , Color.BLACK , Color.white};

	//
	private boolean gameOn = true;

	//board location
	private int BoardWidth = 0;
	private int BoardHight = 0;

	//player location
	private int playerX = 500;
	private int playerY = 480;

	static JLabel statusbar;
	static JLabel statusbar1;

	//keep all the obstacles in order to iterate.
	private List<Obsticale> obs = new ArrayList<>();

	private Random r = new Random();
	
	
	//actions

	private boolean firstTime = true;

	Board b;

	/**
	 * the ctor for the graphics class.
	 * @param b
	 * @param playerX
	 * @param playerY
	 * @param d
	 */
	public GraficxClass(Board b ,int playerX , int playerY , int d){

		this.obs.add(new Obsticale(r.nextInt(20) + 4, r.nextInt(40) + 4, r.nextInt(4) + 1));
		
		this.b = b;
		
		this.playerX = playerX;
		this.playerY = playerY;
		
		eating.add(new Lib(playerY / 20 , (playerX + 1) / 20, 0 , 0));

		//
		if(firstTime){
			//defind map
			for(int i = 0; i < hight ;i++){
				for(int j = 0; j < width; j++){ 
					if((i == 0) || (j == 0) || (i == hight - 1) || (j == width-1)){
						//painting the inner map in green.
						map[i][j]  = new Lib(i,j,0,0);
					}else{
						//painting the out side with blue.
						map[i][j]  = new Lib(i,j,1,1);
					}//if
				}//j loop
			}//i loop
		}

		firstTime = false;

	}// public GraficxClass

	/**
	 * the function that happens 30 times a second, and responsible for painting all the board in the right way.
	 * @param g
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//at first the function
		for(int i = 0; i < hight ;i++){
			for(int j = 0; j < width ; j++){
				if(map[i][j].getColor() != 2){
					g.setColor(colors[map[i][j].getColor()]);
					g.fillRect(map[i][j].getY() * 20,map[i][j].getX() * 20,  20  ,20);
				}else{
					g.setColor(colors[1]);
					g.fillRect(map[i][j].getY() * 20,map[i][j].getX() * 20,  20  ,20);
					g.setColor(colors[2]);
					g.fillOval(map[i][j].getY() * 20,map[i][j].getX() * 20,  20  ,20);
				}
			}
		}

		//drowPlayer
		g.setColor(colors[3]);
		g.fillRect(playerX,playerY, 20,20);

		//paint white
		if(eating.size() >= 2){

			Lib l = eating.get(eating.size() - 2);
			if(l.getColor() == 1){
				l.setColor(4);
				map[l.getX()][l.getY()].setColor(4);
				repaint();
			}
		}
		//end painting white




		//paint eating
		if(eating.size() >= 2){

			Lib l = eating.get(eating.size() - 1);
			eating.remove(eating.size() - 1);

			if(l.getColor() == 1){

				eating.add(l);

			}else if(eating.size() >= 2){

				//if size >= 3 and the first color is not green so its ether blue or wight

				if(l.getColor() == 0){

					Lib l2 = eating.get(eating.size() - 1);
					eating.remove(eating.size() - 1);

					if(l2.getColor() == 4){

						if(l.getColor() == 4){
							//this.gameOn = false;
						}
						eating.add(l2);
						eating.add(l);
						//closing algorithem...

							Lib[] temp = new Lib[eating.size()];

							for (int i = 0; i < temp.length; i++) {
								temp[i] = eating.get(eating.size() - 1);
								eating.remove(eating.size() - 1);
								map[temp[i].getX()][temp[i].getY()].setColor(0);
								repaint();
							}

							//ready to before
							for(int i = 0; i < hight ;i++){
								for(int j = 0; j < width ; j++){
									if(map[i][j].getColor() != 0){
										map[i][j].setSection(1);
									}
								}
							}

							//int n = 0;

							//setting sections according to the locations of the obstacles.
							for(Obsticale ob : obs){
								ob.fullSectors(map, ob.getX(), ob.getY() , 0);
							}

							//paints what left in one section.
							for(int i = 0; i < hight ;i++){
								for(int j = 0; j < width ; j++){
									if(map[i][j].getSection() == 1){
										map[i][j].setColor(0);
									}
								}
							}

							//updats the map coverd element.
							for(int i = 1; i < hight -1;i++){
								for(int j = 1; j < width - 1; j++){
									if(map[i][j].getColor() == 0){
										this.mapCovered++;
									}
								}
							}

							//caculate map precentage
							this.mapPrecents = ((this.mapCovered * 100 )/ 1104);

							b.getStatusbar().setText(this.mapPrecents + "%");

							if(this.mapPrecents >= 70){
								this.level++;
								b.getStatusbar1().setText("The Level is: " + this.level + "  there are more: " + this.timesToPlay + " times to play");

							}

							this.mapCovered = 0;

							repaint();

							eating.clear();
							eating.add(l);

					}else if(l2.getColor() == 0){
						eating.clear();
						eating.add(l);
					}
				}else{
					//this.gameOn = false;
				}
			}else if(eating.size() == 2){

				//if size == 3 and the first color is not green

				eating.clear();
				eating.add(l);
			}
		}
		///end of painting eating

	}

	public void paintEating(){
		if(eating.get(eating.size() - 1).color == 4){
			this.gameOn = false;
		}else if(eating.get(eating.size() - 1).color == 0){

		}
	}


	public int getMapPrecents() {
		repaint();
		return mapPrecents;
	}

	public void reset(int timesToPlay){

		this.mapCovered = 0;
		this.mapPrecents = 0;

		this.timesToPlay = timesToPlay;
		b.getStatusbar1().setText("The Level is: " + this.level + ".  there are more: " + this.timesToPlay + " times to play");
		b.getStatusbar().setText("0%");

		for(int i = 0; i < hight ;i++){
			for(int j = 0; j < width; j++){ 
				if((i == 0) || (j == 0) || (i == hight - 1) || (j == width-1)){
					map[i][j]  = new Lib(i,j,0,0);
				}else{
					map[i][j]  = new Lib(i,j,1,1);
				}//if
			}//j loop
		}//i loop

		this.eating.clear();

		this.playerX = 500;
		this.playerY = 480;

		for(Obsticale ob : obs){
			ob.setSmall(false);
			ob.setX(r.nextInt(10) + 4);
			ob.setY(r.nextInt(20) + 4);
		}

		this.gameOn = true;
	}

	public Lib resetDuring(int timesToPlay){
		
		this.timesToPlay = timesToPlay;
		b.getStatusbar1().setText("The Level is: " + this.level + ".  there are more: " + this.timesToPlay + " times to play");

		Lib l1 = null;
		
		while(!(this.eating.size() == 0)){
			l1 = eating.get(eating.size() - 1);
			eating.remove(eating.size() - 1);
			l1.setColor(1);
			l1.setSection(1);
			
		}

		playerX = l1.getY() * 20;
		playerY = l1.getX() * 20;
		
		
		l1.setColor(0);
		l1.setSection(0);
		
		this.eating.clear();
		
		eating.add(l1);
		
		for(int i = 0; i < hight ;i++){
			for(int j = 0; j < width; j++){ 
				if(map[i][j].getColor() != 0){
					map[i][j]  = new Lib(i,j,1,1);
				}//if
				
				if((i == 0) || (j == 0) || (i == hight - 1) || (j == width-1)){
					map[i][j]  = new Lib(i,j,0,0);
				}
			}//j loop
		}//i loop
		this.gameOn = true;
		
		return l1;
	}
	
	public boolean MoveObs(){

		for (Obsticale ob : obs){
			ob.setDirection(ob.getDirection());
			gameOn = gameOn && ob.MoveObs(map, colors);
		}

		return gameOn;
	}

	public boolean isObsHere(){
		
		boolean b = false;

		for(Obsticale ob : obs){
			if(ob.isObsHere(this.playerX / 20, this.playerY / 20)){
				b = true;
			}
		}
		return b;
		
	}

	public void setPlayerX(int playerX) {

		this.playerX = playerX;

		if(eating.size() >= 1){
			Lib k = eating.get(eating.size() - 1);
			eating.remove(eating.size() - 1);
			if(!(k.getColor() == 0 && map[playerY/20][playerX/20].getColor() == 0)){
				eating.add(k);
			}
		}
		eating.add(map[playerY/20][playerX/20]);
		paintEating();
	}

	public void setPlayerY(int playerY) {

		this.playerY = playerY;

		if(eating.size() >= 1){
			Lib k = eating.get(eating.size() - 1);
			eating.remove(eating.size() - 1);
			if(!(k.getColor() == 0 && map[playerY/20][playerX/20].getColor() == 0)){
				eating.add(k);
			}
		}
		eating.add(map[playerY/20][playerX/20]);
		paintEating();
	}
	
	public void levelUp(){
		this.obs.add(new Obsticale(r.nextInt(20) + 4, r.nextInt(40) + 4, r.nextInt(4) + 1));
	}

	public void printMap(){
		for(int i = 0; i < hight ;i++){
			for(int j = 0; j < width; j++){
				System.out.print(map[i][j].getColor());
			}
		}
	}

	public int getPlayerY() {
		return playerY;
	}

	public int getPlayerX() {
		return playerX;
	}


}
