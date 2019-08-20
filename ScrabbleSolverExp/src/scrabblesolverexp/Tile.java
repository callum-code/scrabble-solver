/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabblesolverexp;

/**
 *
 * @author callumijohnston
 */

/**
 *
 * @author callumijohnston
 */
public class Tile {
    int row;
    int col;
    char letter;
    int points;
    int index;
    private boolean isEmpty;

    public Tile(int row, int col, char letter) {
        this.row = row;
        this.col = col;
        this.letter = letter;
        isEmpty = false;
    }

    public Tile(int row, int col, char letter, int points) {
        this.row = row;
        this.col = col;
        this.letter = letter;
        this.points = points;
        isEmpty = false;
    }

    public Tile(char letter, int points) {
        this.letter = letter;
        this.points = points;
        isEmpty = false;
    }

    public Tile(char letter) {
        this.letter = letter;
        isEmpty = false;
    }

    public Tile() {
        isEmpty=true;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
    public boolean isEmpty(){
        return isEmpty;
    }
    
}

