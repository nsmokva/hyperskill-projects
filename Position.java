package tetris;

public class Position {
    private PositionStatus status;
    private int x;
    private int y;

    public Position(int x, int y, PositionStatus status){
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public PositionStatus getStatus(){
        return this.status;
    }

    public void setStatus(PositionStatus status){
        this.status = status;
    }
}
