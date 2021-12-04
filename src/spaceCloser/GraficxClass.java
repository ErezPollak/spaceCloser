package spaceCloser;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GraficxClass extends JPanel{

	final static int width = 50;
	final static int hight = 25;

	private static int timesToPlay = 5;
	private static int level = 1;
	private static int mapPrecents = 0;
	private static int mapCovered = 0;

	private static Lib[][] map = new Lib[hight][width];
	private static Stack<Lib> eating = new Stack<Lib>();
	private static Color[] colors = {Color.BLUE, Color.GREEN, Color.RED,Color.BLACK , Color.white};

	private static boolean gameOn = true;

	//board location
	private int BoardWidth = 0;
	private int BoardHight = 0;

	//player location
	private static int playerX = 500;
	private static int playerY = 480;

	static JLabel statusbar;
	static JLabel statusbar1;

	private static Stack<Obsticale> obs = new Stack<Obsticale>();
	private static Stack<Obsticale> temp = new Stack<Obsticale>();
	private static Obsticale tempPop;
	private static Random r = new Random();
	
	
	//actions

	private static boolean firstTime = true; 

	Board b;
	
	public GraficxClass(Board b ,int playerX , int playerY , int d){

		this.obs.add(new Obsticale(r.nextInt(20) + 4, r.nextInt(40) + 4, r.nextInt(4) + 1));
		
		this.b = b;
		
		this.playerX = playerX;
		this.playerY = playerY;
		
		eating.add(new Lib(playerY / 20 , (playerX + 1) / 20, 0 , 0));

		if(firstTime){
			//defind map
			for(int i = 0; i < hight ;i++){
				for(int j = 0; j < width; j++){ 
					if((i == 0) || (j == 0) || (i == hight - 1) || (j == width-1)){
						map[i][j]  = new Lib(i,j,0,0);
					}else{
						map[i][j]  = new Lib(i,j,1,1);
					}//if
				}//j loop
			}//i loop
		}

		firstTime = false;

		
		
		

	}// public GraficxClass
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
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
			Lib gray1 = eating.pop();
			Lib gray2 = eating.pop();
			if(gray2.getColor() == 1){
				gray2.setColor(4);
				map[gray2.getX()][gray2.getY()].setColor(4);
				repaint();
			}
			eating.push(gray2);
			eating.push(gray1);
		}

		//end painting white
		
		
		
		//paint eating
		if(eating.size() >= 2){
			Lib l = eating.pop();
			if(l.getColor() == 1){
				eating.add(l);
			}else if(eating.size() >= 2){
				if(l.getColor() == 0){
					Lib l2 = eating.pop();
					if(l2.getColor() == 4){
							if(l.getColor() == 4){
								this.gameOn = false;
							}

							eating.add(l2);
							eating.add(l);
							//closing algorithem...

							Lib[] temp = new Lib[eating.size()];

							for (int i = 0; i < temp.length; i++) {
								temp[i] = eating.pop();
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

							int n = 0;
							
							while(!this.obs.empty()){
								tempPop = this.obs.pop();
								n = tempPop.fullSectors(map, tempPop.getX(), tempPop.getY() , 0);
								System.out.println(n);
								if(n != 0 && n <= 4 && !tempPop.isSmall()){
									tempPop.setSmall(true);
								 	b.getTimer().setDelay(b.getTimer().getDelay() * 2);
								}
								this.temp.push(tempPop);
							}
							while(!this.temp.empty()){
								this.obs.push(this.temp.pop());
							}
					
							for(int i = 0; i < hight ;i++){
								for(int j = 0; j < width ; j++){
									if(map[i][j].getSection() == 1){
										map[i][j].setColor(0);


									}
								}
							}

							for(int i = 1; i < hight -1;i++){
								for(int j = 1; j < width - 1; j++){
									if(map[i][j].getColor() == 0){
										this.mapCovered++;
									}
								}
							}



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
					this.gameOn = false;
				}
			}else if(eating.size() == 2){
				eating.clear();
				eating.add(l);
			}
		}
		///end of painting eating

	}
	public static int getMapPrecents() {
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

		while(!this.obs.empty()){
			tempPop.setSmall(false);
			tempPop = this.obs.pop();
			tempPop.setX(r.nextInt(10) + 4);
			tempPop.setY(r.nextInt(20) + 4);
			this.temp.push(tempPop);
		}
		while(!this.temp.empty()){
			this.obs.push(this.temp.pop());
		}
		
		this.gameOn = true;
	}

	public Lib resetDuring(int timesToPlay){
		
		this.timesToPlay = timesToPlay;
		b.getStatusbar1().setText("The Level is: " + this.level + ".  there are more: " + this.timesToPlay + " times to play");

		Lib l1 = null;
		
		while(!this.eating.empty()){
			l1 = eating.pop();
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
	
	public static boolean MoveObs(){
		while(!obs.empty()){
			tempPop = obs.pop();
			tempPop.setDirection(tempPop.getDirection());
			gameOn =  gameOn && tempPop.MoveObs(map,colors);
			temp.push(tempPop);
		}
		while(!temp.empty()){
			obs.push(temp.pop());
		}
		
		return gameOn;

	}

	public boolean isObsHere(){
		
		boolean b = false;
		
		while(!obs.empty()){
			tempPop = obs.pop();
			if(tempPop.isObsHere(this.playerX / 20, this.playerY / 20)){
				b = true;
			}
			temp.push(tempPop);
		}
		while(!temp.empty()){
			obs.push(temp.pop());
		}
		
		return b;
		
	}



	public static int getPlayerX() {
		return playerX;
	}
	public static void setPlayerX(int playerX) {
		GraficxClass.playerX = playerX;
		if(eating.size() >= 1){
			Lib k = eating.pop();
			if(!(k.getColor() == 0 && map[playerY/20][playerX/20].getColor() == 0)){
				eating.add(k);
			}
		}
		eating.add(map[playerY/20][playerX/20]);

	}
	public static int getPlayerY() {
		return playerY;
	}
	public static void setPlayerY(int playerY) {
		GraficxClass.playerY = playerY;
		if(eating.size() >= 1){
			Lib k = eating.pop();
			if(!(k.getColor() == 0 && map[playerY/20][playerX/20].getColor() == 0)){
				eating.add(k);
			}
		}
		eating.add(map[playerY/20][playerX/20]);


	}
	
	public static void printMap(){
		for(int i = 0; i < hight ;i++){
			for(int j = 0; j < width; j++){ 
				//System.out.print(map[i][j].getColor());
			}
		}
	}
	
	public void levelUp(){
		this.obs.add(new Obsticale(r.nextInt(20) + 4, r.nextInt(40) + 4, r.nextInt(4) + 1));
		
		while(!obs.empty()){
			tempPop = obs.pop();
			temp.push(tempPop);
		}
		while(!temp.empty()){
			obs.push(temp.pop());
		}
		
	}

}