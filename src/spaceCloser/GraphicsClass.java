package spaceCloser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import javax.swing.JPanel;

public class GraphicsClass extends JPanel {

        //the number of points in the map of the game.
        final int width;
        final int height;

        //
        private int timesToPlay;
        private int level;
        private int mapPresents;

        private int mapCovered;
        private final Lib[][] map;

        //keep all the eaten libs
        private final List<Lib> eating;

        //keeps all the colors in an array in order to get them in the right order.
        private final Color[] COLORS_VECTOR;

        private boolean gameOn = true;

        //player location
        private int playerX;
        private int playerY;

        //keep all the obstacles in order to iterate.
        private List<Obstacle> obs;

    private final Random r;


    Board b;

    //initializations
    {
        height = 25;
        width = 50;
        COLORS_VECTOR = new Color[]{Color.BLUE, Color.GREEN, Color.RED, Color.BLACK, Color.white};
        eating = new ArrayList<>();
        map = new Lib[height][width];
        r = new Random();
        obs = new ArrayList<>();
        level = 1;
        timesToPlay = 5;
        mapPresents = 0;
        mapCovered = 0;
    }


    /**
     * the ctor for the graphics class.
     */
    public GraphicsClass(Board b, int playerX, int playerY) {

        this.obs.add(new Obstacle(r.nextInt(20) + 4, r.nextInt(40) + 4, r.nextInt(4) + 1));

        this.b = b;

        this.playerX = playerX;
        this.playerY = playerY;

        eating.add(new Lib(playerY / 20, (playerX + 1) / 20, 0, 0));

        //initialize map

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if ((i == 0) || (j == 0) || (i == height - 1) || (j == width - 1)) {
                    //painting the inner map in green.
                    map[i][j] = new Lib(i, j, 0, 0);
                } else {
                    //painting the outside with blue.
                    map[i][j] = new Lib(i, j, 1, 1);
                }//if
            }//j loop
        }//i loop


    }// public GraphicsClass

    /**
     * the function that happens 30 times a second, and responsible for painting all the board in the right way.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //at first the function
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j].getColor() != 2) {
                    g.setColor(COLORS_VECTOR[map[i][j].getColor()]);
                    g.fillRect(map[i][j].getY() * 20, map[i][j].getX() * 20, 20, 20);
                } else {
                    g.setColor(COLORS_VECTOR[1]);
                    g.fillRect(map[i][j].getY() * 20, map[i][j].getX() * 20, 20, 20);
                    g.setColor(COLORS_VECTOR[2]);
                    g.fillOval(map[i][j].getY() * 20, map[i][j].getX() * 20, 20, 20);
                }
            }
        }

        //drawPlayer
        g.setColor(COLORS_VECTOR[3]);
        g.fillRect(playerX, playerY, 20, 20);

    }

    /**
     * the function that paints what we need to paint in blue every time the circle closes.
     */
    public void paintEating() {

        //if the player went twice on the same spot.
        if (eating.get(eating.size() - 1).color == 4) {
            this.gameOn = false;
            return;
        }

        //if the color is blue it means that a loop is over and need to be closed.
        //and making sure that the two first eaten limbs are blue and white.
        if (eating.get(eating.size() - 1).color == 0
                && eating.size() > 1 &&  eating.get(eating.size() - 2).color == 4) {

            //painting the trail in blue.
            for (Lib l : eating) {
                l.setColor(0);
            }

            //setting all the green ones to section 1.
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (map[i][j].getColor() != 0) {
                        map[i][j].setSection(1);
                    }
                }
            }

            //setting sections according to the locations of the obstacles.
            for (Obstacle ob : obs) {
                ob.fullSectors(map, ob.getX(), ob.getY(), 0);
            }

            //paints what left in one section.
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (map[i][j].getSection() == 1 && map[i][j].getColor() != 0) {
                        map[i][j].setColor(0);
                        this.mapCovered++;
                    }
                }
            }


            ////calculation of the new map percentage, and updating statusbar:


            //adding the size of the eating list accept for the two blue ones in the beginning and end of the list.
            this.mapCovered += this.eating.size() - 2;

            //calculate map percentage
            this.mapPresents = (((this.mapCovered) * 100) / 1104);

            b.getStatusbar().setText(this.mapPresents + "%");

            if (this.mapPresents >= 70) {
                this.level++;
                b.getStatusbar1().setText("The Level is: " + this.level + "  there are more: " + this.timesToPlay + " times to play");
            }

            //restores only the last element in the list.
            Lib l = eating.get(eating.size() - 1);
            eating.clear();
            eating.add(l);
        }
    }


    public int getMapPresents() {
        return mapPresents;
    }

    public void reset(int timesToPlay) {

        this.obs.add(new Obstacle(r.nextInt(20) + 4, r.nextInt(40) + 4, r.nextInt(4) + 1));

        this.mapCovered = 0;
        this.mapPresents = 0;

        this.timesToPlay = timesToPlay;
        b.getStatusbar1().setText("The Level is: " + this.level + ".  there are more: " + this.timesToPlay + " times to play");
        b.getStatusbar().setText("0%");

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if ((i == 0) || (j == 0) || (i == height - 1) || (j == width - 1)) {
                    map[i][j] = new Lib(i, j, 0, 0);
                } else {
                    map[i][j] = new Lib(i, j, 1, 1);
                }//if
            }//j loop
        }//i loop

        this.eating.clear();

        this.playerX = 500;
        this.playerY = 480;

        for (Obstacle ob : obs) {
            ob.setX(r.nextInt(10) + 4);
            ob.setY(r.nextInt(20) + 4);
            ob.setDirection(r.nextInt(4) + 1);
        }

        this.gameOn = true;
    }


    /**
     * initialize all the properties for anther time of playing after game over.
     * @param timesToPlay remain times to play.
     * @return the first lib of the eating list witch is the last correct place the player was at.
     */
    public Lib resetDuring(int timesToPlay) {

        this.timesToPlay = timesToPlay;
        b.getStatusbar1().setText("The Level is: " + this.level + ".  there are more: " + this.timesToPlay + " times to play");

        Lib l1 = eating.get(0);

        for (Lib l : eating) {
            if (l.color != 0) {
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

    /**
     * moving all the obs in the required direction.
     * @return if one of the obs steps on a wight lib there is a need to stop the game.
     */
    public boolean MoveObs() {

        for (Obstacle ob : obs) {
            ob.setDirection(ob.getDirection());
            gameOn = gameOn && ob.MoveObs(map) && !(ob.getNextX() == playerY / 20 && ob.getNextY() == playerX / 20);
        }

        return gameOn;
    }

    /**
     * setting the location of the players and checking if an area was closed.
     * @param playerX the new X location of the player.
     */
    public void setPlayerX(int playerX) {

        this.playerX = playerX;

       checkAte();
    }

    /**
     * etting the location of the players and checking if an area was closed.
     * @param playerY the new Y location of the player.
     */
    public void setPlayerY(int playerY) {

        this.playerY = playerY;

        checkAte();

    }

    /**
     * checking if the player closed on an area that need to be painted as closed.
     */
    private void checkAte(){

        if (eating.size() != 0 && eating.get(eating.size() - 1).color == 1) {
            eating.get(eating.size() - 1).setColor(4);
        }
        eating.add(map[playerY / 20][playerX / 20]);

        if (map[this.playerY / 20][this.playerX / 20].color == 0 && eating.get(eating.size() - 2).color == 0) {
            eating.remove(eating.size() - 2);
        }

        paintEating();
    }


}
