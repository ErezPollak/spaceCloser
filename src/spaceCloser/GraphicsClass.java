package spaceCloser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GraphicsClass extends JPanel{

	//the number of points in the map of the game.
	final int width = 50;
	final static int height = 25;

	//
	private int timesToPlay;
	private int level;
	private int mapPresents;

	{
		level = 1;
		timesToPlay = 5;
		mapPresents = 0;
	}

	private int mapCovered = 0;

	//
	private final Lib[][] map = new Lib[height][width];

	//keep all the eaten libs
	private List<Lib> eating = new ArrayList<>();

	//keeps all the colors in an array in order to get them in the right order.
	private Color[] colors = {Color.BLUE , Color.GREEN , Color.RED , Color.BLACK , Color.white};

	//
	private boolean gameOn = true;

	//board location
	private int BoardWidth = 0;
	private final int BoardHight = 0;

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
	public GraphicsClass(Board b , int playerX , int playerY , int d){

		this.obs.add(new Obsticale(r.nextInt(20) + 4, r.nextInt(40) + 4, r.nextInt(4) + 1));
		
		this.b = b;
		
		this.playerX = playerX;
		this.playerY = playerY;
		
		eating.add(new Lib(playerY / 20 , (playerX + 1) / 20, 0 , 0));

		//
		if(firstTime){
			//defind map
			for(int i = 0; i < height; i++){
				for(int j = 0; j < width; j++){ 
					if((i == 0) || (j == 0) || (i == height - 1) || (j == width-1)){
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
		for(int i = 0; i < height; i++){
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

	}

	/**
	 * the function that paints what we need to paint in blue every time the circle closes.
	 */
	public void paintEating(){
		if(eating.get(eating.size() - 1).color == 4){
			this.gameOn = false;
		}else if(eating.get(eating.size() - 1).color == 0){

			for(Lib l : eating){
				l.setColor(0);
			}

			for(int i = 0; i < height; i++){
				for(int j = 0; j < width ; j++){
					if(map[i][j].getColor() != 0){
						map[i][j].setSection(1);
					}
				}
			}

			//setting sections according to the locations of the obstacles.
			for(Obsticale ob : obs){
				ob.fullSectors(map, ob.getX(), ob.getY() , 0);
			}

			//paints what left in one section.
			for(int i = 0; i < height; i++){
				for(int j = 0; j < width ; j++){
					if(map[i][j].getSection() == 1){
						map[i][j].setColor(0);
					}
				}
			}


			//updats the map coverd element.
			for(int i = 1; i < height -1; i++){
				for(int j = 1; j < width - 1; j++){
					if(map[i][j].getColor() == 0){
						this.mapCovered++;
					}
				}
			}

			//caculate map precentage
			this.mapPresents = ((this.mapCovered * 100 )/ 1104);

			b.getStatusbar().setText(this.mapPresents + "%");

			if(this.mapPresents >= 70){
				this.level++;
				b.getStatusbar1().setText("The Level is: " + this.level + "  there are more: " + this.timesToPlay + " times to play");

			}

			this.mapCovered = 0;

			//restors only the last element in the list.
			Lib l = eating.get(eating.size() -1);
			eating.clear();
			eating.add(l);

		}
	}


	public int getMapPresents() {
		return mapPresents;
	}

	public void reset(int timesToPlay){

		this.mapCovered = 0;
		this.mapPresents = 0;

		this.timesToPlay = timesToPlay;
		b.getStatusbar1().setText("The Level is: " + this.level + ".  there are more: " + this.timesToPlay + " times to play");
		b.getStatusbar().setText("0%");

		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){ 
				if((i == 0) || (j == 0) || (i == height - 1) || (j == width-1)){
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

		Lib l1 = eating.get(0);

		for(Lib l : eating){
			if(l.color != 0){
				l.setColor(1);
				l.setSection(1);
			}
		}

		playerX = l1.getY() * 20;
		playerY = l1.getX() * 20;

		l1.setColor(0);
		l1.setSection(0);
		
		this.eating.clear();
		
		eating.add(l1);

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

		if(eating.size() != 0 && eating.get(eating.size() - 1).color == 1){
			eating.get(eating.size()-1).setColor(4);
		}
		eating.add(map[playerY/20][playerX/20]);

		if(map[playerY/20][playerX/20].color == 0 && eating.get(eating.size() - 2).color == 0){
			eating.remove(eating.size() - 2);
		}

//		eating.add(map[playerY/20][playerX/20]);
		System.out.println("before " + eating);
		paintEating();
		System.out.println("after" + eating);
	}

	public void setPlayerY(int playerY) {

		this.playerY = playerY;

		if(eating.size() != 0 &&eating.get(eating.size()-1).color == 1){
			eating.get(eating.size()-1).setColor(4);
		}
		eating.add(map[playerY/20][playerX/20]);

		if(map[playerY/20][playerX/20].color == 0 && eating.get(eating.size() - 2).color == 0){
			eating.remove(eating.size() - 2);
		}

		System.out.println("before " + eating);
		paintEating();
		System.out.println("after" + eating);
	}
	
	public void levelUp(){
		this.obs.add(new Obsticale(r.nextInt(20) + 4, r.nextInt(40) + 4, r.nextInt(4) + 1));
	}

	public void printMap(){
		for(int i = 0; i < height; i++){
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
