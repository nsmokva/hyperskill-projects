package tetris;

import java.util.Arrays;
import java.util.List;

public class Piece {
    private String name;
    private Integer[][] states;
    private List <Integer> currentState;
    private int stateIndex;
    private int currentRow;
    private int currentCol;
    private boolean bottom;

    public Integer[][] getStates(){
        return this.states;
    }

    public List<Integer> getCurrentState(){
        return this.currentState;
    }

    public int getStateIndex(){
        return this.stateIndex;
    }

    public int getCurrentRow(){
        return this.currentRow;
    }

    public int getCurrentCol(){
        return this.currentCol;
    }

    public boolean getBottom(){
        return this.bottom;
    }

    public void setCurrentState(int i, Integer stateMember){
        this.currentState.set(i, stateMember);
    }

    public void setCurrentRow(){
        this.currentRow++;
    }

    public void setStateIndex(int stIn){
        this.stateIndex = stIn;
    }

    public void setCurrentCol(int curCol){
        this.currentCol = curCol;
    }

    public void setBottom(boolean bott){
        this.bottom = bott;
    }

    public Piece(String name, Integer[][] states){
        this.name = name;
        this.states = states;
        this.currentState = Arrays.asList(Arrays.copyOf(states[0], 4));
        this.stateIndex = 0;
        this.currentCol = 0;
        this.currentRow = 0;
        this.bottom = false;

    }

}
