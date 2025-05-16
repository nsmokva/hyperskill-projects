package tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Grid {
    private final int width;
    private final int height;
    private Position[][] grid;
    private Piece piece;

    public Grid(int width, int height){
        this.width = width;
        this.height = height;
        this.grid = new Position[height][width];
        for(int i = 0; i<height; i++){
            for(int j = 0; j<width; j++){
                grid[i][j] = new Position(i, j, PositionStatus.FREE);
            }
        }
    }

    public Piece getPiece(){
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setGrid(Position[][] grid){
        this.grid = grid;
    }

    public void printMovingPieceOnTheGrid(List<Integer> state){
        Collections.sort(state);
        int sCount = 0;
        for(int i = 0; i< height; i++){
            for(int j=0; j<width; j++){
                if(state.get(sCount) == (i*width+j)){
                    System.out.print("0 ");
                    if(sCount<3){
                        sCount++;
                    }
                }else{
                    System.out.print(grid[i][j].getStatus().getSign() + " ");
                }

            }
            System.out.println();
        }
        System.out.println();
    }

    public void printGrid(){
        for(int i = 0; i<height; i++){
            for(int j=0; j<width; j++){
              System.out.print(grid[i][j].getStatus().getSign() + " ");

            }
            System.out.println();
        }
        System.out.println();
    }


        public List<Integer> down(){
        for(int i = 0; i<piece.getCurrentState().size(); i++){
            piece.setCurrentState(i, piece.getCurrentState().get(i)+width);

        }
        piece.setCurrentRow();
        isDown();
        isTouchingAnotherPiece();
        return piece.getCurrentState();
    }

    public List<Integer> rotate(){
        int newStateIndex;
        if(piece.getStateIndex() == this.piece.getStates().length-1){
            newStateIndex = 0;
        }else{
            newStateIndex = piece.getStateIndex() + 1;
        }
        List<Integer> baseRotatedState = Arrays.asList(piece.getStates()[newStateIndex]);
        for(int i = 0; i<piece.getCurrentState().size(); i++){
            piece.setCurrentState(i, baseRotatedState.get(i) + (piece.getCurrentRow()+1)*width + piece.getCurrentCol());
        }
        piece.setStateIndex(newStateIndex);
        piece.setCurrentRow();
        isDown();
        isTouchingAnotherPiece();
        return piece.getCurrentState();
    }

    public List<Integer> right(){
        boolean boundry = false;
        for(Integer state : piece.getCurrentState()){
            if(state%10==9){
                boundry = true;
            }
        }
        for(int i = 0; i<piece.getCurrentState().size(); i++){
            Integer newState = null;
            if(boundry){
                newState = piece.getCurrentState().get(i)+width;
            }else{
                newState = piece.getCurrentState().get(i)+width+1;
            }
            piece.setCurrentState(i, newState);
        }
        piece.setCurrentRow();
        if(piece.getCurrentCol() == 5){
            piece.setCurrentCol(-4);
        }else{
            piece.setCurrentCol(piece.getCurrentCol()+1);
        }
        isDown();
        isTouchingAnotherPiece();
        return piece.getCurrentState();
    }

    public List<Integer> left(){
        boolean boundry = false;
        for(Integer state : piece.getCurrentState()){
            if(state%10==0){
                boundry = true;
            }
        }

        for(int i = 0; i<piece.getCurrentState().size(); i++){
            Integer newState = null;
            if(boundry){
                newState = piece.getCurrentState().get(i)+width;
            }else{
                newState = piece.getCurrentState().get(i)+width-1;
            }
            piece.setCurrentState(i, newState);
        }
        piece.setCurrentRow();
        if(piece.getCurrentCol() == -4){
            piece.setCurrentCol(5);
        }else{
            piece.setCurrentCol(piece.getCurrentCol()-1);
        }
        isDown();
        isTouchingAnotherPiece();
        return piece.getCurrentState();
    }

    public void isDown(){
        if(!piece.getBottom()){
            for(Integer state : piece.getCurrentState()){
                String stateString = String.valueOf(state);
                if(stateString.length()>1 && ("" + stateString.charAt(0)).equals(String.valueOf(height-1))){
                    piece.setBottom(true);
                    updateGrid();
                    break;
                }
            }
        }
    }

    //isPieceTouching down border or some other piece
    public void isTouchingAnotherPiece(){
        if(!piece.getBottom()){
            for(Integer state : piece.getCurrentState()) {
                int i = state / width;
                int j = state % width;
                if (grid[i + 1][j].getStatus() == PositionStatus.TAKEN) {
                    piece.setBottom(true);
                    updateGrid();
                    break;
                }
            }
        }
    }

    public void updateGrid(){
        Collections.sort(this.piece.getCurrentState());
        int sCount = 0;
        for(int i = 0; i< height; i++){
            for(int j=0; j<width; j++){
                if(this.piece.getCurrentState().get(sCount) == (i*width+j)){
                  this.grid[i][j].setStatus(PositionStatus.TAKEN);
                    if(sCount<3){
                        sCount++;
                    }
                }
            }
        }
    }


    public void breakkkk(){
        List<Integer> indexesOfFullRows = new ArrayList<>();
        for (int i = 0; i<height; i++){
            boolean rowFull = true;
            for(int j = 0; j<width; j++){
                if(grid[i][j].getStatus() == PositionStatus.FREE){
                    rowFull = false;
                    break;
                }
            }
            if(rowFull){
                indexesOfFullRows.add(i);
            }
        }

        List<Position[]> listOfRows = new ArrayList<>();
        for(int i = 0; i<height; i ++){
            listOfRows.add(grid[i]);
        }
        Collections.sort(indexesOfFullRows);
        Collections.reverse(indexesOfFullRows);
        for(Integer index : indexesOfFullRows){
            listOfRows.remove(index.intValue());
        }

        Position[][] newGr =  new Position[height][width];
        for(int i = 0; i<indexesOfFullRows.size(); i++){
            for(int j = 0; j<width; j++){
                newGr[i][j] = new Position(i, j, PositionStatus.FREE);
            }
        }

        for(int i = 0; i<listOfRows.size(); i++){
            for(int j = 0; j<width; j++){
                if(listOfRows.get(i)[j].getStatus() == PositionStatus.TAKEN){
                    newGr[i+indexesOfFullRows.size()][j] = new Position(i+indexesOfFullRows.size(), j, PositionStatus.TAKEN);
                }else{
                    newGr[i+indexesOfFullRows.size()][j] = new Position(i+indexesOfFullRows.size(), j, PositionStatus.FREE);
                }
            }
        }

        setGrid(newGr);
        printGrid();
    }

    public boolean isGameOver(){
        boolean gameOver = false;
        for(int j = 0; j<width; j++){
            boolean colFilled = true;
            for(int i = 0; i<height; i++){
                if(grid[i][j].getStatus() == PositionStatus.FREE){
                    colFilled = false;
                }
            }
            if(colFilled){
                gameOver = colFilled;
                break;
            }
        }
        return gameOver;
    }
}


