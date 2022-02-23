package spaceCloser;

public class Lib {
	protected int X = 0;
	protected int Y = 0;
	protected int color = 0;
	protected int section ;

	/**
	 * every lib represents a cube on the screen.
	 * @param x
	 * @param y
	 * @param color means what color will it be painted with.
	 * 	 the colors are thus:
	 * 	 0 means blue
	 * 	 1 means green
	 * 	 2 red
	 * 	 3 white
	 * 	 4 black
	 * @param section meant for the painting algorithm, to differentiate between those who were been visited to those that were not.
	 */
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

	@Override
	public String toString() {
		return "Lib{" +
				"X=" + X +
				", Y=" + Y +
				", color=" + color +
				", section=" + section +
				'}';
	}
}
