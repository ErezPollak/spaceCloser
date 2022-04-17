package spaceCloser;

import java.util.Arrays;

public class Obstacle extends Lib {

    int[][] DIRECTION_ORDER = new int[][]{{3, 1, 2, 4}, {4, 2, 3, 1}, {3, 1, 4, 2}, {4, 2, 1, 3}};

    private int direction = 1;
    private int nextX;
    private int nextY;

    public Obstacle(int x, int y, int d) {
        super(x, y, 2, 0);
        this.direction = d;
        setDirection(this.direction);
    }

    /**
     * sets the movement on the screen to be like ethe direction is.
     *
     * @param n
     */
    public void setDirection(int n) {
        this.direction = n;
        setDirectionLite(n);
    }

    private void setDirectionLite(int nextCheckDirection) {
        switch (nextCheckDirection) {
            case 0:{
                nextX = X;
                nextY = Y;
            }break;
            case 1: {
                nextX = X + 1;
                nextY = Y - 1;
            }
            break;
            case 2: {
                nextX = X + 1;
                nextY = Y + 1;
            }
            break;

            case 3: {
                nextX = X - 1;
                nextY = Y + 1;
            }
            break;

            case 4: {
                nextX = X - 1;
                nextY = Y - 1;
            }
            break;
        }
    }


    public int getX() {
        return X;
    }

    public void setX(int x) {
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

    public int getNextY() {
        return nextY;
    }

    public int getDirection() {
        return direction;
    }

    public void setPlace() {
        X = nextX;
        Y = nextY;
    }

    /**
     * the function returns the number of obstacles in the area of the lib (x ,y).
     * this is an implementation of DFS algorithm.
     *
     * @param map
     * @param x
     * @param y
     * @param counter
     * @return
     */
    public int fullSectors(Lib[][] map, int x, int y, int counter) {

        if (map[x][y].getColor() == 0 || map[x][y].getSection() == 0) {
            return 0;
        }

        map[x][y].setSection(0);

        counter = 1 + counter
                + fullSectors(map, x - 1, y - 1, counter)
                + fullSectors(map, x, y - 1, counter)
                + fullSectors(map, x + 1, y - 1, counter)
                + fullSectors(map, x + 1, y, counter)
                + fullSectors(map, x + 1, y + 1, counter)
                + fullSectors(map, x, y + 1, counter)
                + fullSectors(map, x - 1, y + 1, counter)
                + fullSectors(map, x - 1, y, counter);

        return counter;
    }

    /**
     * taking care that the obs move the correct way.
     * @param map the map of the game.
     * @return the status of the game if it didn't reach a dead end.
     */
    public boolean MoveObs(Lib[][] map) {

        //check if the obstacles are stepping on wight lib means there is a game over.
        if(map[this.nextX][this.nextY].getColor() == 4 || map[this.X][this.Y].getColor() == 4){
            return false;
        }

        int nextCheckDirection = this.direction;
        int index = 0;
        while (map[this.nextX][this.nextY] != null && map[this.nextX][this.nextY].getColor() == 0) {
            nextCheckDirection = DIRECTION_ORDER[this.direction % 4][(index++) % DIRECTION_ORDER.length];
            if(nextCheckDirection == this.direction){
                nextCheckDirection = 0;
                break;
            }
            setDirectionLite(nextCheckDirection);
        }
        setDirection(nextCheckDirection);
        map[this.X][this.Y].setColor(1);
        map[this.nextX][this.nextY].setColor(2);
        setPlace();
        return true;
    }

    public String toString() {
        return "[X=" + X + ", Y=" + Y + ", direction=" + direction + ", nextX=" + nextX + ", nextY=" + nextY + "]";
    }


}
