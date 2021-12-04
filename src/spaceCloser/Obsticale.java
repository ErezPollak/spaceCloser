package spaceCloser;

import java.awt.Color;

public class Obsticale extends Lib{
	//private int X;
	//private int Y;
	private int direction = 1;
	private int nextX;
	private int nextY;
	private boolean isSmall = false;
	
	public Obsticale(int x, int y , int d){
		super(x , y , 2 , 0);
		//this.X = x;
		//this.Y = y;
		this.direction = d;
		setDirection(this.direction);
	}
	
	public void setDirection(int n){
		this.direction = n;
		switch(direction){
			case 1:{
				nextX = X + 1;
				nextY = Y - 1;
			}break;
			case 2:{
				nextX = X + 1;
				nextY = Y + 1;
			}break;
		
			case 3:{
				nextX = X - 1;
				nextY = Y + 1;
				}break;
		
			case 4:{
				nextX = X - 1;
				nextY = Y - 1;
			}break;

		}
		
	}

	public  void setPlase(){
		X = nextX;
		Y = nextY;
		setDirection(direction);
	}
	
	
	public int getX() {
		return X;
	}

	public  void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getNextX() {
		return nextX;
	}

	public void setNextX(int nextx) {
		nextX = nextx;
	}

	public int getNextY() {
		return nextY;
	}

	public  void setNextY(int nexty) {
		nextY = nexty;
	}

	public int getDirection() {
		return direction;
	}

	public void setPlace(){
		X = nextX;
		Y = nextY;
	}
	
	public boolean isSmall() {
		return isSmall;
	}

	public void setSmall(boolean isSmall) {
		this.isSmall = isSmall;
	}

	public int fullSectors(Lib[][] map , int x , int y, int counter){
		
		if(map[x][y].getColor() == 0 || map[x][y].getSection() == 0){
			return 0;
		}
			map[x][y].setSection(0);
			
			counter = 1 + counter
			+fullSectors(map , x - 1, y - 1,counter)
			+fullSectors(map , x, y - 1,counter)
			+fullSectors(map , x + 1, y - 1 , counter)
			+fullSectors(map , x + 1, y , counter)
			+fullSectors(map , x + 1, y + 1 , counter)
			+fullSectors(map , x, y + 1 , counter)
			+fullSectors(map , x - 1, y + 1 , counter)
			+fullSectors(map , x - 1, y , counter);
			
			return counter;		
	}
	
	
	public boolean MoveObs(Lib[][] map, Color[] colors){
		
		boolean gameOn = true;
		
		//System.out.println("aaaaa");
		//this.direction = this.direction;
		//System.out.println(this.X + "    "+this.Y);
		
		if(map[this.nextX][this.nextY] != null && map[this.nextX][this.nextY].getColor() == 0){
			switch(this.direction){
				case 1:{
					this.direction = 4;
					nextX = X - 1;
					nextY = Y - 1;
					if(map[this.nextX][this.nextY].getColor() == 0){
						this.direction = 2;
						nextX = X + 1;
						nextY = Y + 1;
						if(map[this.nextX][this.nextY].getColor() == 0){
							this.direction = 3;
							nextX = X - 1;
							nextY = Y + 1;
						}
					}
				}break;
				case 2:{
					this.direction = 3;
					nextX = X - 1;
					nextY = Y + 1;
					if(map[this.nextX][this.nextY].getColor() == 0){
						this.direction = 1;
						nextX = X + 1;
						nextY = Y - 1;
						if(map[this.nextX][this.nextY].getColor() == 0){
							this.direction = 4;
							nextX = X - 1;
							nextY = Y - 1;
						}
					}
				}break;
				case 3:{
					this.direction = 4;
					nextX = X - 1;
					nextY = Y - 1;
					if(map[this.nextX][this.nextY].getColor() == 0){
						this.direction = 2;
						nextX = X + 1;
						nextY = Y + 1;
						if(map[this.nextX][this.nextY].getColor() == 0){
							this.direction = 1;
							nextX = X + 1;
							nextY = Y - 1;
						}
					}
				}break;
				case 4:{
					this.direction = 3;
					nextX = X - 1;
					nextY = Y + 1;
					if(map[this.nextX][this.nextY].getColor() == 0){
						this.direction = 1;
						nextX = X + 1;
						nextY = Y - 1;
						if(map[this.nextX][this.nextY].getColor() == 0){
							this.direction = 2;
							nextX = X + 1;
							nextY = Y + 1;
						}
					}
				}break;
	
			}
		}
		if(map[getNextX()][getNextY()] != null && map[getNextX()][getNextY()].getColor() == 4){
			
			gameOn = false;
			
		}
		
		map[getNextX()][getNextY()].setColor(2);
		map[getX()][getY()].setColor(1);
		//printMap();
		setPlace();
		
		return gameOn;
		
	}
	
	public boolean isObsHere(int x , int y){
		if((this.nextX == y && this.nextY == x )|| (this.X == y && this.Y == x)){
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "[X=" + X + ", Y=" + Y + ", direction=" + direction + ", nextX=" + nextX + ", nextY=" + nextY+ "]";
	}
	
	
	
}
