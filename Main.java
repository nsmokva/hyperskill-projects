package tetris;

import java.util.*;

public class Main {

    static Integer[][] O = {{4, 14, 15, 5}};
    static Integer[][] I = {{4, 14, 24, 34}, {3, 4, 5, 6}};
    static Integer[][] S = {{5, 4, 14, 13}, {4, 14, 15, 25}};
    static Integer[][] Z = {{4, 5, 15, 16}, {5, 15, 14, 24}};
    static Integer[][] L = {{4, 14, 24, 25}, {5, 15, 14, 13}, {4, 5, 15, 25}, {6, 5, 4, 14}};
    static Integer[][] J = {{5, 15, 25, 24}, {15, 5, 4, 3}, {5, 4, 14, 24}, {4, 14, 15, 16}};
    static Integer[][] T = {{4, 14, 24, 15}, {4, 13, 14, 15}, {5, 15, 25, 14}, {4, 5, 6, 15}};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer[][]> mapp = new HashMap<>();
        mapp.put("O", O);
        mapp.put("I", I);
        mapp.put("S", S);
        mapp.put("Z", Z);
        mapp.put("L", L);
        mapp.put("J", J);
        mapp.put("T", T);


        int n = scanner.nextInt();
        int m = scanner.nextInt();

        Grid grid = new Grid(n, m);
        grid.printGrid();

        while(true){
            String command = scanner.next();
            if(grid.isGameOver()){
                grid.printGrid();
                System.out.println("Game over!");
                break;
            }else{
                if(command.matches("piece")){
                    String input = scanner.next();
                    Integer[][] states = mapp.get(input);
                    grid.printMovingPieceOnTheGrid(Arrays.asList(states[0]));
                    grid.setPiece(new Piece("piece", states));
                    grid.isTouchingAnotherPiece();
                }else if(command.matches("exit")){
                    break;
                }else if(command.matches("break")){
                    //System.out.println("breaking in Main");
                    grid.breakkkk();
                }else if(grid.getPiece().getBottom()){
                    grid.printGrid();
                   // System.out.println("piece bottom");
                }else if(command.matches("down")){
                    grid.printMovingPieceOnTheGrid(grid.down());
                }else if(command.matches("rotate")){
                    grid.printMovingPieceOnTheGrid(grid.rotate());
                }else if(command.matches("right")){
                    grid.printMovingPieceOnTheGrid(grid.right());
                }else if(command.matches("left")){
                    grid.printMovingPieceOnTheGrid(grid.left());
                }
            }
        }
    }
}
