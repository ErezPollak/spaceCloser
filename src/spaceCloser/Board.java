package spaceCloser;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Stack;

import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Board extends JFrame implements ActionListener{

	private static Board b;

	private static int level = 1;

	private static int timesToPlay = 5;

	private static int playerX = 500;
	private static int playerY = 480;

	private static boolean gameOn = true;

	private Timer timer;
	private static GraficxClass gc;

	JLabel statusbar = new JLabel("0%");
	JLabel statusbar1 = new JLabel("The Level is: " + this.level + "there is more: " + this.timesToPlay + "times to play");

	public Board(){
		setBounds(5, 5, 1015, 565);
		timer = new Timer(300, this); 
		timer.start();

		gc = new GraficxClass(this,playerX,playerY,1);//1000 , 500);

		statusbar.setBackground(new Color(250,250,0));
		statusbar.setFont(new Font("Tahoma", Font.PLAIN, 32));
		gc.add(statusbar);

		//difined status statusbar
		statusbar1.setBackground(new Color(250,250,0));
		statusbar1.setFont(new Font("Tahoma", Font.PLAIN, 25));
		gc.add(statusbar1);

		add(gc);

		addKeyListener(new TAdapter());
	}

	public JLabel getStatusbar1() {
		return statusbar1;
	}

	public void setStatusbar1(JLabel statusbar1) {
		this.statusbar1 = statusbar1;
	}

	public JLabel getStatusbar() {
		return statusbar;
	}

	public void setStatusbar(JLabel statusbar) {
		this.statusbar = statusbar;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public void actionPerformed(ActionEvent e) {

		statusbar.setBounds(getWidth() / 2, 0, 100, 100);

		statusbar1.setText("The Level is: " + this.level + ".  there are more: " + this.timesToPlay + " times to play");

		statusbar1.setBounds(0, 465, 1000, 100);


		if(gc.getMapPrecents() >= 70){
			level++;
			gc.levelUp();
			JOptionPane.showMessageDialog(null," level number " + this.level, "" , 1);
			gc.reset(timesToPlay);
			this.playerX = 500;
			this.playerY = 480;
			
			double x = 300 * Math.pow(0.8, level);
			
			System.out.println((int)(x - x % 1));
			
			timer.setDelay((int)(x - x % 1));
		}

		if(gc != null && (!gc.MoveObs() || gc.isObsHere())){
			if(timesToPlay > 0){
				this.gameOn = false;
				timer.stop();

				int n = JOptionPane.showConfirmDialog(null,"do you want to try again? ");
				if(n == 0){
					timesToPlay--;

					Lib l2 = gc.resetDuring(timesToPlay);

					System.out.println("b    "+l2.getX()+"    "+l2.getY());

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
		}else{
			repaint();
		}



	}

	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			if(gameOn){

				int keycode = e.getKeyCode();

				switch (keycode) {

					case KeyEvent.VK_LEFT:{
						if(timer.isRunning()){
							if(playerX > 0){
								playerX = playerX - 20;
								gc.setPlayerX(playerX);
							}
							repaint();
						}
					}break;
	
					case KeyEvent.VK_RIGHT:{
						if(timer.isRunning()){
							if(playerX < 980){
								playerX = playerX + 20;
								gc.setPlayerX(playerX);
							}
							repaint();
						}
					}break;
	
					case KeyEvent.VK_DOWN:{
						if(timer.isRunning()){
							if(playerY < 480){
								playerY = playerY + 20;
								gc.setPlayerY(playerY);
							}
							repaint();}
					}break;
	
					case KeyEvent.VK_UP:{
						if(timer.isRunning()){
							if(playerY > 0){
								playerY = playerY - 20;
								gc.setPlayerY(playerY);
							}
							repaint();
						}
					}break;
	
					case KeyEvent.VK_SPACE:{
						if(timer.isRunning()){
							timer.stop();
						}else{
							timer.start();
						}
	
					}break;

				}

			}
		}
	}



	public static void main(String[] args) {
		int n = 0;

		n = JOptionPane.showConfirmDialog(null,"hellow"+
				"\n"+"welcame to Space Closer"+
				"\n"+"the game's aim is to cover as much space as you can from the board,"+
				"\n"+" by directing the bluck point around the space with the arrows of the keybord."+
				"\n"+"you have to avoid the red points that are going to appear on the board."+
				"\n"+"and bluck point must not touch the way it went before closing a space."+
				"\n"+"the number in the top of the board mantions the number of the precents of the board that you alredy coverd."+
				"\n"+"you have to close 70% of the board to get to the next level,"+
				"\n"+"when you do that the red points will double thier speed."+
				"\n"+"you have 5 lives, when you lose all of them you are out"+
				"\n"+"good luck!"+
				"\n"+"ARE YOU READY??"+
				"\n" , "Space Closer",1);
		if(n == 0){
			b = new Board();
			b.setVisible(true);
		}
		else{
			System.exit(0);
		}
	}

}
