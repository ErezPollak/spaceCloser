package spaceCloser;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Board extends JFrame implements ActionListener{

	/**
	 * keeps the level of the game that the player got to, gets up every time the player closed about 70% of the space.
	 */
	private int level;

	/**
	 * the remain times that the player has to play gets down with every time the player failed to pass stage.
	 */
	private int timesToPlay = 5;

	/**
	 * keeps the location of the player.
	 */
	private  int playerX;
	private int playerY;

	/**
	 * true as long as the game keeps going.
	 */
	private boolean gameOn;

	/**
	 * makes sure every thing that depend on time happens when it supposes to.
	 */
	private Timer timer;

	/**
	 * the graphics class, responsible for the presentation layer of the game.
	 */
	private static GraphicsClass gc;

	/**
	 * show to the player the status of the game that he is in, and the times he has remaining to play.
	 */
	JLabel statusbar = new JLabel("0%");
	JLabel statusbar1;

	{
		gameOn = true;
		playerY = 480;
		playerX = 500;
		level = 1;
		statusbar1 = new JLabel("The Level is: " + level + "there is more: " + this.timesToPlay + "times to play");
	}

	/**
	 * ctor for the Board class.
	 */
	public Board(){
		//setting the bounds and the timer to have the correct values.
		setBounds(5, 5, 1015, 565);
		timer = new Timer(300, this); 


		//creates new Graphics class.
		gc = new GraphicsClass(this,playerX,playerY);

		//setting background and fonts.
		statusbar.setBackground(new Color(250,250,0));
		statusbar.setFont(new Font("Tahoma", Font.PLAIN, 32));
		gc.add(statusbar);

		//defined status statusbar
		statusbar1.setBackground(new Color(250,250,0));
		statusbar1.setFont(new Font("Tahoma", Font.PLAIN, 25));
		gc.add(statusbar1);

		//adding the graphics class as a component to the frame.
		add(gc);

		//adding the listeners of the keyboard to the frame as a component too.
		addKeyListener(new TAdapter());

		timer.start();
	}


	/**
	 * the logic that happens every round if the timer.
	 */
	public void actionPerformed(ActionEvent e) {
		//repainting the
		repaint();

		statusbar.setBounds(getWidth() / 2, 0, 100, 100);
		statusbar1.setBounds(0, 465, 1000, 100);

		//the top percentage that needs to be filled is 70% of the board.
		if(gc.getMapPresents() >= 70){
			level++;
			//gc.levelUp();
			JOptionPane.showMessageDialog(null," level number " + this.level, "" , JOptionPane.INFORMATION_MESSAGE);
			gc.reset(timesToPlay);
			this.playerX = 500;
			this.playerY = 480;
			
			double x = 300 * Math.pow(0.8, level);

			timer.setDelay((int)(x - x % 1));
		}

		//check if there is a need to stop the game.
		if((!gc.MoveObs())){
			if(timesToPlay > 0){
				this.gameOn = false;
				timer.stop();

				int n = JOptionPane.showConfirmDialog(null,"do you want to try again? ");
				if(n == 0){
					timesToPlay--;

					Lib l2 = gc.resetDuring(timesToPlay);

					this.playerX = l2.getY() * 20;
					this.playerY = l2.getX() * 20;

					gameOn = true;
					timer.start();
				}else{
					System.exit(0);
				}
			}else{
				JOptionPane.showMessageDialog(null, "GAME OVER!!!!");
				System.exit(0);
			}
		}
	}

	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			if(gameOn){

				int keycode = e.getKeyCode();

				switch (keycode) {
					case KeyEvent.VK_LEFT -> {
						if (timer.isRunning()) {
							if (playerX > 0) {
								playerX = playerX - 20;
								gc.setPlayerX(playerX);
							}
							repaint();
						}
					}
					case KeyEvent.VK_RIGHT -> {
						if (timer.isRunning()) {
							if (playerX < 980) {
								playerX = playerX + 20;
								gc.setPlayerX(playerX);
							}
							repaint();
						}
					}
					case KeyEvent.VK_DOWN -> {
						if (timer.isRunning()) {
							if (playerY < 480) {
								playerY = playerY + 20;
								gc.setPlayerY(playerY);
							}
							repaint();
						}
					}
					case KeyEvent.VK_UP -> {
						if (timer.isRunning()) {
							if (playerY > 0) {
								playerY = playerY - 20;
								gc.setPlayerY(playerY);
							}
							repaint();
						}
					}
					case KeyEvent.VK_SPACE -> {
						if (timer.isRunning()) {
							timer.stop();
						} else {
							timer.start();
						}

					}
				}

			}
		}
	}

	public static void main(String[] args) {

		int n = JOptionPane.showConfirmDialog(null, """
				hello
				welcome to Space Closer
				the game's aim is to cover as much space as you can from the board,
				 by directing the block point around the space with the arrows of the keyboard.
				you have to avoid the red points that are going to appear on the board.
				and black point must not touch the way it went before closing a space.
				the number in the top of the board mansions the number of the presents of the board that you already covered.
				you have to close 70% of the board to get to the next level,
				when you do that the red points will double their speed.
				you have 5 lives, when you lose all of them you are out
				good luck!
				ARE YOU READY??
				""", "Space Closer", JOptionPane.YES_NO_CANCEL_OPTION);
		if(n == 0){
			/**
			 * the instance of the class itself.
			 */
			Board b = new Board();
			b.setVisible(true);
		}
		else{
			System.exit(0);
		}
	}

	public JLabel getStatusbar1() {
		return statusbar1;
	}

	public JLabel getStatusbar() {
		return statusbar;
	}

}
