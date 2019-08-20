/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabblesolverexp;


import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author callumijohnston
 */
public class WordFinder {

//    Tile[] tiles = new Tile[7];
    String rack;
    Square[] board;
    Square anchor;
    static HashMap hashBrowns;
    ArrayList<String> legalMoves = new ArrayList<>();//replace with arraylist of linked list of 
    ArrayList<String> xSquares = new ArrayList<>();
    ArrayList<String> ySquares = new ArrayList<>();
    ArrayList<String> isPlayed = new ArrayList<>();
    ScrabbleBoard sb;
    int lim;

    public WordFinder(ScrabbleBoard scrabbleBoard) {
        sb = scrabbleBoard;
        WordFinder.hashBrowns = new HashMap();
        hashItOut();
    }

    public void solve(String rack, Square[] board, boolean horizontals) {
        this.rack = rack.toUpperCase();
        this.board = board;
        for (int i = 0; i < board.length; i++) {
            if (!board[i].tile.isEmpty()) {
                anchor = board[i];
                int j = i;
                while (j > 0 && board[j - 1].tile.isEmpty()) {
                    j--;
                }
                int limit = i - j;
                lim = limit;
                if (lim > 0) {
                    LeftPart("", "", "", "", Trie.root, limit, 0, horizontals, false, -1);
                }
            }
        }
    }

    void LeftPart(String isP, String x, String y, String partialWord,
            Node N, int limit, int num, boolean horizontals, boolean r, int b) {
        ExtendRight(isP, x, y, partialWord, N, anchor, 0, horizontals, r, b);
        if (limit > 0) {
            Node temp = N;
            if (temp.next != null) {
                temp = temp.next;
                do {
                    char l = temp.letter;
                    if (rack.contains(l + "") || rack.contains("_")) {
                        if (rack.contains(l + "")) {
                            rack = rack.replaceFirst(l + "", "");
                        } else {
                            rack = rack.replaceFirst("_", "");
                        }
                        int x1 = anchor.row;
                        int y1 = anchor.col;
                        int x2=x1;
                        int y2=y1;
                        if (rack.contains(l + "")) {
                            rack = rack.replaceFirst(l + "", "");
                        } else {
                            rack = rack.replaceFirst("_", "");
                            b = partialWord.length();
                        }
                        ArrayList<Character> crossCheck;
                        
                        if (horizontals) {
                            x2 -= lim-limit;
                        } else {
                            y2 -= lim-limit;
                        }
                        Square square = sb.squareBoard[x2][y2];
                        if (!horizontals) {
                            crossCheck = square.verticalCrossCheck;
                        } else {
                            crossCheck = square.horizontalCrossCheck;
                        }
                        if (crossCheck.contains(l)) {
                            LeftPart(isP + "1", x + "[" + (x1) + "]", y + "[" + (y1) + "]",
                                    partialWord + l, temp, limit - 1, num, horizontals, true, b);
                        }
                        if (x1 < 200) {
                            rack = rack + l;
                        } else {
                            rack = rack + '_';
                        }
                    }
                    temp = temp.left;
                } while (temp != null);
            }
        }
    }

    void ExtendRight(String isP, String x, String y, String partialWord,
            Node N, Square square, int num, boolean horizontals, boolean r, int b) {
        if (square.tile.isEmpty()) {
            Square sq = square;
            if (!horizontals) {
                sq = sb.squareBoard[anchor.row+1][anchor.col];
            } else {
                sq = sb.squareBoard[anchor.row+1][anchor.col];
            }
            if (N.cap&&isP.contains("1")/* && sq.tile.isEmpty()*/) {
                legalMove(isP, partialWord, x.substring(1, x.length() - 1),
                        y.substring(1, y.length() - 1), horizontals);
            }
            Node temp = N;
            if (temp.next != null) {
                temp = temp.next;
                do {
                    char l = temp.letter;
                    ArrayList<Character> crossCheck;
                    if (!horizontals) {
                        crossCheck = square.verticalCrossCheck;
                    } else {
                        crossCheck = square.horizontalCrossCheck;
                    }
                    if ((rack.contains(l + "") || rack.contains("_"))
                            && square.tile.index < 14 && crossCheck.contains(l)) {
                        for (char c : crossCheck) {
                            System.out.print(c);
                        }
                        System.out.println("");
                        int x1 = anchor.row;
                        int y1 = anchor.col;
                        if (rack.contains(l + "")) {
                            rack = rack.replaceFirst(l + "", "");
                        } else {
                            rack = rack.replaceFirst("_", "");
                            b = partialWord.length();
                        }
                        Square next = board[square.tile.index + 1];
                        if (horizontals) {
                            x1 += num;
                        } else {
                            y1 += num;
                        }
                        ExtendRight(isP + "1", x + "[" + x1 + "]", y + "[" + y1 + "]",
                                partialWord + l, temp, next, num + 1, horizontals, true, b);
                        if (x1 < 200) {
                            rack = rack + l;
                        } else {
                            rack = rack + '_';
                        }
                    }
                    temp = temp.left;
                } while (temp != null);
            }
        } else {
            char l = square.tile.getLetter();
            Node temp = N;
            if (temp.next != null && square.tile.index < 14) {
                temp = temp.next;
                while (temp != null && temp.letter != l) {
                    temp = temp.left;
                }
                if (temp != null && temp.letter == l) {
                    N = temp;
                    Square next = board[square.tile.index + 1];
//                    int x1 = (next.col);
//                    int y1 = (next.row);
                    int x1 = anchor.row;
                    int y1 = anchor.col;
                    if (horizontals) {
                        x1 += num;
                    } else {
                        y1 += num;
                    }
                    if (x1 < 14) {
                        ExtendRight(isP + "0", x + "[" + x1 + "]", y + "[" + y1 + "]",
                                partialWord + l, N, next, num + 1, horizontals, r, b);
                    }
                }
            }
        }
    }

    void legalMove(String isP, String w, String x, String y, boolean horizontals) {
        legalMoves.add(w);
        xSquares.add(horizontals + x);
        ySquares.add(horizontals + y);
        isPlayed.add(isP);
    }

    void crossCheck(Square[][] board) {
        for (int j = 0; j < 14; j++) {
            for (int i = 0; i < 14; i++) {
                board[i][j].verticalCrossCheck = verticalCheck(board, i, j);
                board[i][j].horizontalCrossCheck = horizontalCheck(board, i, j);
            }
        }
    }

    private ArrayList<Character> verticalCheck(Square[][] board, int i, int j) {
        ArrayList<Character> result = new ArrayList<>();
        int pre = i;
        int post = i;

        while (pre > 0 && !board[pre - 1][j].tile.isEmpty()) {
            pre--;
        }

        while (post < 14 && !board[post + 1][j].tile.isEmpty()) {
            post++;
        }
        if (pre == post) {
            for (int k = 0; k < 26; k++) {
                result.add(charVal(k));
            }
        } else {
            for (int k = 0; k < 26; k++) {
                String s = "";
                for (int l = pre; l < i; l++) {
                    s += Character.toUpperCase(board[l][j].tile.getLetter());
                }
                s += charVal(k);
                for (int l = i + 1; l <= post; l++) {
                    s += Character.toUpperCase(board[l][j].tile.getLetter());
                }
                if (Trie.displayWord(s)) {
                    result.add(charVal(k));
                }
            }
        }
        return result;
    }

    private ArrayList<Character> horizontalCheck(Square[][] board, int i, int j) {
        ArrayList<Character> result = new ArrayList<>();
        int pre = j;
        int post = j;
        while (pre > 0 && !board[i][pre - 1].tile.isEmpty()) {
            pre--;
        }
        while (post < 14 && !board[i][post + 1].tile.isEmpty()) {
            post++;
        }
        if (pre == j && post == j) {
            for (int k = 0; k < 26; k++) {
                result.add(charVal(k));
            }
        } else {
            for (int k = 0; k < 26; k++) {
                String s = "";
                for (int l = pre; l < j; l++) {
                    s += Character.toUpperCase(board[i][l].tile.getLetter());
                }
                s += charVal(k);
                for (int l = j + 1; l <= post; l++) {
                    s += Character.toUpperCase(board[i][l].tile.getLetter());
                }

                if (Trie.displayWord(s)) {
                    result.add(charVal(k));
                }
            }
        }
        return result;
    }

    public char charVal(int i) {
        return Character.toUpperCase((char) (i + 97));
    }

    public static final String ALPHABET
            = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";

    private void hashItOut() {
        hashBrowns.put('a', 1);
        hashBrowns.put('b', 3);
        hashBrowns.put('c', 3);
        hashBrowns.put('d', 2);
        hashBrowns.put('e', 1);
        hashBrowns.put('f', 4);
        hashBrowns.put('g', 2);
        hashBrowns.put('h', 4);
        hashBrowns.put('i', 1);
        hashBrowns.put('j', 8);
        hashBrowns.put('k', 5);
        hashBrowns.put('l', 1);
        hashBrowns.put('m', 3);
        hashBrowns.put('n', 1);
        hashBrowns.put('o', 1);
        hashBrowns.put('p', 3);
        hashBrowns.put('q', 10);
        hashBrowns.put('r', 1);
        hashBrowns.put('s', 1);
        hashBrowns.put('t', 1);
        hashBrowns.put('u', 1);
        hashBrowns.put('v', 4);
        hashBrowns.put('w', 4);
        hashBrowns.put('x', 8);
        hashBrowns.put('y', 4);
        hashBrowns.put('z', 10);
        hashBrowns.put('_', 0);
    }

}

