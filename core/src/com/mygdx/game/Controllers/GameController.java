package com.mygdx.game.Controllers;

import com.mygdx.game.Models.Square;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    //har per nå laget kun en liste, kan heller ha liste i liste for å lettere navigere seg opp/ned/sidelengs men har ikke det nå
    private List<Square> opBoard = new ArrayList<>();
    private List<Square> myBoard = new ArrayList<>();
    private int squaresize;
    private int numberSquaresHeight;
    private int numberSquaresWidth;

    public GameController(){
        squaresize = 32;
        numberSquaresHeight = 5;
        numberSquaresWidth = 9;
        int x = 20;
        int my_y = 50;
        int op_y = 300;
        for (int i = 0; i< numberSquaresHeight; i++){
            for (int j = 0; j< numberSquaresWidth; j++){
                myBoard.add(new Square(x, my_y, squaresize));
                opBoard.add(new Square(x,op_y,squaresize));
                x+=squaresize;

            }
            x = 20;
            my_y+=squaresize;
            op_y+=squaresize;
        }

    }

    public List<Square> getOpBoard(){
        return opBoard;
    }

    public List<Square> getMyBoard(){
        return myBoard;
    }

}
