/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fill.a.pix.solver;

/**
 *
 * @author Kaj75
 */
public class Square {
    private State state;
    // up to 100% for Mark
    private int certainty;
    //position
    public int x;
    public int y;

    Square(int x, int y) {
        this.x = x;
        this.y = y;
        state = State.Clear;
        certainty = 50;
    }

    public State getState() {
        return state;
    }

    public int getCertainty() {
        return certainty;
    }

    public void setState(State state) {
        this.state = state;
    }
}
