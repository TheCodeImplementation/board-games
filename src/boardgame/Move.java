/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package boardgame;

public class Move {
    private final   Position start, destination;
    //If sender is null, that indicates the move was not created by a user - it must have been the computer/ai.
    private         UserInterface sender;
    //CONSTRUCTORS
    public Move(Position start, Position destination) {
        this(start, destination, null);
    }
    public Move(Position start, Position destination, UserInterface ui) {
        this.start = start;
        this.destination = destination;
        sender = ui;
    }
    //METHODS
    public boolean isVertical() {
        return getX1() == getX2();
    }
    public boolean isNorth() {
        return getY2() - getY1() > 0;
    }
    public boolean isSouth() {
        return getY2() - getY1() < 0;
    }
    public boolean isDiagonal() {
        return Math.abs(getY2() - getY1()) == Math.abs(getX2() - getX1());
    }
    public boolean isStraight() {
        //Test if the move up/down/left/right, ie not diagonal by checking
        //if the Xs or the Ys are the same but not both...
        return getX1() == getX2() ^ getY1() == getY2();
    }
    /**
     * How many squared from start to destination? Only works with linear moves (diagonal or straight).
     */
    public int distance() {
        return Math.max(
                Math.abs(getY2() - getY1()),
                Math.abs(getX2() - getX1())
        );
    }
    //GETTERS
    public Position         getStart() {
        return start;
    }
    public Position         getDestination() {
        return destination;
    }
    public int              getX1() {
        return getStart().getX();
    }
    public int              getY1() {
        return getStart().getY();
    }
    public int              getX2() {
        return getDestination().getX();
    }
    public int              getY2() {
        return getDestination().getY();
    }
    public UserInterface    getSender() {
        return sender;
    }
    public void             setSender(UserInterface sender) {
        this.sender = sender;
    }
}