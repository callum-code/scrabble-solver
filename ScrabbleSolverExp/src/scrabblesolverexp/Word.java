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


import java.util.ArrayList;

/**
 *
 * @author callumijohnston
 */
public class Word {

    ArrayList<Square> word;
    ArrayList<Character> rackTakes;
    int points;
    boolean horizontal;
    ScrabbleBoard board;
    int wordBonusAmount = 1;
    private int length = 0;

    public Word(ScrabbleBoard board, boolean horizontal) {
        this.board = board;
        word = new ArrayList<>();
        rackTakes = new ArrayList<>();
        this.horizontal = horizontal;
    }

    public int length() {
        return length;
    }

    public char charAt(int i) {
        return word.get(i).tile.getLetter();
    }

    public ArrayList<Square> add(char c) {
        int row= 0;
        int col =0;
        if (word.isEmpty()) {

        } else {
            row = word.get(word.size() - 1).row;
            col = word.get(word.size() - 1).col;
            if (horizontal) {
                col++;
            } else {
                row++;
            }
        }
        Square sq = new Square(row, col);
        sq.tile = new Tile(c, (int) WordFinder.hashBrowns.get(Character.toLowerCase(c)));
//        sq.bonusAmount = board.board[row][col].bonusAmount;
//        sq.wordBonus = board.board[row][col].wordBonus;
        ArrayList<Square> copy = word;
        copy.add(sq);
        rackTakes.add(c);
        int letterPoints;
        if (sq.wordBonus) {
            wordBonusAmount = sq.bonusAmount;
            letterPoints = sq.tile.points * wordBonusAmount;
        } else {
            letterPoints = sq.tile.points * wordBonusAmount
                    * sq.bonusAmount;
        }
        points += letterPoints;
        length++;
        return copy;
    }

    public ArrayList<Square> add(Square s) {
        ArrayList<Square> copy = word;
        copy.add(s);
        points += s.tile.points;
        length++;
        return copy;
    }

    public String toString() {
        String s = "";
        for (Square sq : word) {
            s += sq.tile.letter;
        }
        return s;
    }

}

