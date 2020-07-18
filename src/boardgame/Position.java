/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package boardgame;

public class Position {
    private int x, y;
    //CONSTRUCTORS
    public Position(char file, int rank) {
        x = fileToInt(file);
        y = rankToInt(rank);
    }
    public Position(int arrayX, int arrayY) {
        x = arrayX;
        y = arrayY;
    }
    //METHODS
    @Override public boolean equals(Object other) {
        return other instanceof Position
            && ((Position)other).getX() == getX()
            && ((Position)other).getY() == getY();
    }
    //HELPERS
    /**
     * Translate the file of the algebraic chess move to its array equivalent.
     * (eg, the 'A' in A1 would become 0)
     */
    private static int fileToInt(char file) {
        if(!Character.isAlphabetic(file)) {
            return -1;
        }
        //if rank is an ascii capital letter
        if(file >= 97)
            return file - 97; //A = 0, B = 1, etc
        else
            return file - 65; //a = 0, b = 1, etc
    }
    /**
     * Translate the rank of the algebraic chess move to its array equivalent.
     * (eg, the '1' in A1 would become 0)
     */
    private static int rankToInt(int rank) {
        return rank-1;
    }
    //GETTERS & SETTERS
    public int  getX(){return x;}
    public int  getY(){return y;}
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}
