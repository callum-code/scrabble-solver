/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabblesolverexp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author callumijohnston
 */
public class Square {
    Tile tile;
    ArrayList<Character> verticalCrossCheck;
    ArrayList<Character> horizontalCrossCheck;
    int bonusAmount;
    boolean wordBonus;
    int row;
    int col;

    public Square(int row, int col) {
        this.row= row;
        this.col = col;
        bonusAmount = 1;
        wordBonus = false;
        verticalCrossCheck= new ArrayList<>();
        horizontalCrossCheck= new ArrayList<>();
    }
    
    public Square(int row, int col, Tile t) {
        tile = t;
        this.row= row;
        this.col = col;
        bonusAmount = 1;
        wordBonus = false;
        verticalCrossCheck= new ArrayList<>();
        horizontalCrossCheck= new ArrayList<>();
    }
}
