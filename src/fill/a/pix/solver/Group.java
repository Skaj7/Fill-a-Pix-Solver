/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fill.a.pix.solver;

import java.util.List;

/**
 *
 * @author Kaj75
 */
public class Group {
    private int number;
    private List<Square> squares;
    //position
    public int x;
    public int y;

    Group(Data data) {
        number = data.getNum();
        x = data.getX();
        y = data.getY();
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public int getNumber() {
        return number;
    }

    public List<Square> getSquares() {
        return squares;
    }

    int getNumberOfMarked() {
        int marks = 0;
        for (Square square : squares) {
            if(square.getState() == State.Mark){
                marks++;
            }
        }
        return marks;
    }

    int getNumberOfBlank() {
        int blanks = 0;
        for (Square square : squares) {
            if(square.getState() == State.Blank){
                blanks++;
            }
        }
        return blanks;
    }

    int getNumberOfClear() {
        int clears = 0;
        for (Square square : squares) {
            if(square.getState() == State.Clear){
                clears++;
            }
        }
        return clears;
    }
}
