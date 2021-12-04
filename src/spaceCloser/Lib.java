package spaceCloser;

public class Lib {
	protected int X = 0;
	protected int Y = 0;
	protected int color = 0;
	protected int section ;
	
	
	
	public Lib(int x, int y, int color , int section) {
		this.X = x;
		this.Y = y;
		this.color = color;
		this.section = section;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getX(){
		return this.X;
	}
	
	public void setX(int x){
		this.X = x;
	}
	
	public int getY(){
		return this.Y;
	}
	
	public String toString(){
		return "X: " + this.X + " Y: "+this.Y+ " color: "+this.color + "section: " +this.section + "\n";
	}
}
