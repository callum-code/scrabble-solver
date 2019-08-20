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
import java.util.StringTokenizer;

/**
 *
 * @author callumijohnston
 */
public class ScrabbleBoard {

    Square[][] squareBoard = new Square[15][15];
    WordFinder solve = new WordFinder(this);
    ArrayList<ArrayList<Integer>> xMoves = new ArrayList<>();
    ArrayList<ArrayList<Integer>> yMoves = new ArrayList<>();
    ArrayList<ArrayList<Integer>> crossSections = new ArrayList<>();
    ArrayList<String> legalMoves = new ArrayList<>();
    ArrayList<String> isPlayed = new ArrayList<>();
    ScrabblePanel panel;
    Tile[] tileRack;
    Tile[] bestMove;

    public ScrabbleBoard(ScrabblePanel panel) {
        this.panel = panel;
    }

    public Tile[] solve() {
        solve = new WordFinder(this);
        xMoves = new ArrayList<>();
        yMoves = new ArrayList<>();
        isPlayed = new ArrayList<>();
        solve.crossCheck(squareBoard);
        for (int j = 0; j < squareBoard.length; j++) {//horizontal group
            String rack = "";
            for (Tile tile : tileRack) {
                rack += tile.letter;
            }
            Square[] boardRow = new Square[squareBoard.length];
            for (int i = 0; i < squareBoard.length; i++) {
                boardRow[i] = squareBoard[i][j];
                if (!boardRow[i].tile.isEmpty()) {
                    boardRow[i].tile.letter = Character.toUpperCase(boardRow[i].tile.letter);
                }
                boardRow[i].tile.index = i;
            }
            solve.solve(rack, boardRow, true);
            for (String legalMove : solve.legalMoves) {
                legalMoves.add(legalMove);
            }
            storeLocations(true);
        }
        solve = new WordFinder(this);
        solve.crossCheck(squareBoard);
        for (int j = 0; j < squareBoard.length; j++) {//vertical group
            String rack = "";
            for (Tile tile : tileRack) {
                rack += tile.letter;
            }
            Square[] boardCol = new Square[squareBoard.length];
            for (int i = 0; i < squareBoard.length; i++) {
                boardCol[i] = squareBoard[j][i];
                if (!boardCol[i].tile.isEmpty()) {
                    boardCol[i].tile.letter = Character.toUpperCase(boardCol[i].tile.letter);
                }
                boardCol[i].tile.index = i;
            }
            solve.solve(rack, boardCol, false);
            for (String legalMove : solve.legalMoves) {
                legalMoves.add(legalMove);
            }
            storeLocations(false);
        }
        Tile[] result = null;
        int finalPoints = 0;
        String finalPlayed = "";
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
        for (int j = 0; j < legalMoves.size(); j++) {
            String legalMove = legalMoves.get(j);
            System.out.println(legalMove);
            int points = 0;
            Tile[] preResult = new Tile[legalMove.length()];
            int wordBonusAmount = 1;
            boolean isLegit = true;
            for (int i = 0; i < legalMove.length(); i++) {
                if (isPlayed.get(j).charAt(i) == '1'
                        && squareBoard[xMoves.get(j).get(i)][yMoves.get(j).get(i)].wordBonus) {
                    wordBonusAmount = squareBoard[xMoves.get(j).get(i)][yMoves.get(j).get(i)].bonusAmount;
                }
                if (!isPlayed.get(i).contains("1")) {
                    isLegit = false;
                }
            }
            for (int i = 0; i < legalMove.length(); i++) {
                preResult[i] = new Tile(legalMove.charAt(i));
                if (yMoves.get(j).get(i) < 0 || xMoves.get(j).get(i) < 0
                        || yMoves.get(j).get(i) > 14 || xMoves.get(j).get(i) > 14) {
                    points = 0;
                    System.out.println("s");
                    break;
                }
                if (isLegit) {
                    if (squareBoard[xMoves.get(j).get(i)][yMoves.get(j).get(i)].wordBonus || isPlayed.get(j).charAt(i) == '0') {
                        preResult[i].points = ((int) WordFinder.hashBrowns.get((preResult[i].letter + "").toLowerCase().charAt(0))) * wordBonusAmount;
                    } else {
                        preResult[i].points = ((int) WordFinder.hashBrowns.get((preResult[i].letter + "").toLowerCase().charAt(0))) * wordBonusAmount
                                * squareBoard[xMoves.get(j).get(i)][yMoves.get(j).get(i)].bonusAmount;
                    }
                }
//                System.out.println(preResult[i].letter + ": " + preResult[i].points);
                points += preResult[i].points;
            }

//            System.out.println("points: " + points);
            if (points > finalPoints) {
                finalPoints = points;
                result = preResult;
                x = xMoves.get(j);
                y = yMoves.get(j);
                finalPlayed = isPlayed.get(j);
            }
        }
//        System.out.println("2nd best:");
//        for (int i = 0; i < result2.length; i++) {
//            System.out.print(result2[i].letter);
//        }
//        System.out.print("\nx: ");
//        for (int i = 0; i < x.size(); i++) {
//            System.out.print(x.get(i) + ", ");
//        }
//        System.out.print("\ny: ");
//        for (int i = 0; i < y.size(); i++) {
//            System.out.print(y.get(i) + ", ");
//        }
//        System.out.println(finalPlayed);
//        System.out.println("");
        try {
            for (int i = 0; i < result.length; i++) {
                panel.addTile(x.get(i) + 1, y.get(i) + 1, result[i].getLetter());
            }
        } catch (NullPointerException npe) {
            System.out.println("No possible moves");
            return null;
        }
        panel.displayScore(x.get(x.size() - 1) + 1, y.get(x.size() - 1) + 1, finalPoints);
        for (int i = 0; i < result.length; i++) {
            if (finalPlayed.charAt(i) == '1') {
                if (panel.rack.contains(Character.toLowerCase(result[i].getLetter()))) {
                    for (int j = 0; j < panel.rack.size(); j++) {
                        if (Character.toUpperCase(panel.rack.get(j)) == result[i].getLetter()) {
                            panel.rack.remove(j);
                            break;
                        }
                    }
                }
            }
        }
//        System.out.println("");
        panel.tileRack = false;
        panel.repaint();//del?
//        System.out.println("points: " + finalPoints);
        return result;
    }

    public int checkCross(boolean horizontal, int x, int y, char c) {
        int result = 0;
        if (horizontal) {
            int pre = y;
            int post = y;
            while (pre > 0 && !squareBoard[x][pre - 1].tile.isEmpty()) {
                pre--;
            }
            while (post < 14 && !squareBoard[x][post + 1].tile.isEmpty()) {
                post++;
            }
            if (pre != post) {
                String s = "";
                for (int l = pre; l < y; l++) {
                    s += Character.toUpperCase(squareBoard[x][l].tile.getLetter());
                }
                s += c;
                for (int l = y + 1; l <= post; l++) {
                    s += Character.toUpperCase(squareBoard[x][l].tile.getLetter());
                }
                for (int i = 0; i < s.length(); i++) {
                    result += ((int) WordFinder.hashBrowns.get((s.charAt(i) + "").toLowerCase().charAt(0)));
                }
            }
        } else {
            int pre = x;
            int post = x;
            while (pre > 0 && !squareBoard[pre - 1][y].tile.isEmpty()) {
                pre--;
            }
            while (post < 14 && !squareBoard[post + 1][y].tile.isEmpty()) {
                post++;
            }
            if (pre != post) {
                String s = "";
                for (int l = pre; l < x; l++) {
                    s += Character.toUpperCase(squareBoard[l][y].tile.getLetter());
                }
                s += c;
                for (int l = x + 1; l <= post; l++) {
                    s += Character.toUpperCase(squareBoard[l][y].tile.getLetter());
                }
                for (int i = 0; i < s.length(); i++) {
                    result += ((int) WordFinder.hashBrowns.get((s.charAt(i) + "").toLowerCase().charAt(0)));
                }
            }
        }
        return result;
    }

    public void storeLocations(boolean horizontal) {
        for (int i = 0; i < solve.xSquares.size(); i++) {
            isPlayed.add(solve.isPlayed.get(i));
            ArrayList<Integer> extraPoints = new ArrayList<>();
            ArrayList<Integer> x = new ArrayList<>();
            ArrayList<Integer> y = new ArrayList<>();
            String sol = solve.xSquares.get(i);
            char h = '*';
            int k = 0;
            for (; !"1234567890".contains(h + ""); k++) {
                h = sol.charAt(k);
            }
            k--;
            sol = sol.substring(k);
            StringTokenizer st = new StringTokenizer(sol, "][");

            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                x.add(Integer.parseInt(s));
                if (Integer.parseInt(s) > 200) {
                    System.out.println(420);
                }
            }
            String sol2 = solve.ySquares.get(i).substring(k);
            StringTokenizer st2 = new StringTokenizer(sol2, "][");
            while (st2.hasMoreTokens()) {
                y.add(Integer.parseInt(st2.nextToken()));
            }
//            for (int j = 0; j < legalMoves.size(); j++) {
//                char c = legalMoves.get(i).charAt(j);
//                int pts = checkCross(horizontal, x.get(j), y.get(j), c);
//                extraPoints.add(pts);
//            }
            crossSections.add(extraPoints);
//            if (solve.blanks.get(i).length() > 0) {
//                System.out.println(solve.blanks.get(i));
//                legalMoves.set(i,legalMoves.get(i).replaceFirst(solve.blanks.get(i).charAt(solve.blanks.get(i).length()-1)+"", "_"));
//                StringBuilder sb = new StringBuilder(legalMoves.get(i));
//                sb.replace(solve.blanks.get(i).length()-1, solve.blanks.get(i).length(), "_");
//                legalMoves.set(i, sb.toString());
            System.out.println(legalMoves.get(i));
//            }
            if (horizontal) {
                fixLeft(x);
            } else {
                fixLeft(y);
            }
            xMoves.add(x);
            yMoves.add(y);
        }
    }

    public void fixLeft(ArrayList<Integer> p) {
        int p1 = p.get(0);
        int i = 0;
        while (i < p.size() && p.get(i).equals(p1)) {
            i++;
        }
        for (int j = i - 2; j >= 0; j--) {
            p.set(j, p.get(j) - (i - 1) + j);
        }
    }

    public void fillBoard() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                squareBoard[i][j] = new Square(i, j);
            }
        }
        for (int[] dl : panel.dls) {
            squareBoard[dl[0] - 1][dl[1] - 1].bonusAmount = 2;
        }
        for (int[] tl : panel.tls) {
            squareBoard[tl[0] - 1][tl[1] - 1].bonusAmount = 3;
        }
        for (int[] dw : panel.dws) {
            squareBoard[dw[0] - 1][dw[1] - 1].bonusAmount = 2;
            squareBoard[dw[0] - 1][dw[1] - 1].wordBonus = true;
        }
        for (int[] tw : panel.tws) {
            squareBoard[tw[0] - 1][tw[1] - 1].bonusAmount = 3;
            squareBoard[tw[0] - 1][tw[1] - 1].wordBonus = true;
        }
        for (int i = 0; i < squareBoard[0].length; i++) {
            for (int j = 0; j < squareBoard.length; j++) {
                squareBoard[i][j].tile = new Tile();
            }
        }
        for (Tile tile : panel.tiles) {
            squareBoard[tile.col - 1][tile.row - 1].tile = new Tile(tile.col - 1, tile.row - 1, tile.letter);
            addPoints(squareBoard[tile.col - 1][tile.row - 1].tile);
        }
        tileRack = new Tile[panel.rack.size()];
        for (int i = 0; i < panel.rack.size(); i++) {
            tileRack[i] = new Tile(panel.rack.get(i));
            tileRack[i].setPoints((int) WordFinder.hashBrowns.get(panel.rack.get(i)));
        }
    }

    public String toString() {
        String s = "";
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 15; i++) {
                s += "[";
                if (!squareBoard[i][j].tile.isEmpty()) {
                    s += (squareBoard[i][j].tile.letter + "").toUpperCase() + squareBoard[i][j].tile.points;
                    if (squareBoard[i][j].tile.getPoints() < 10) {
                        s += " ";
                    }
                } else {
                    s += "   ";
                }

                if (squareBoard[i][j].bonusAmount == 2 && !squareBoard[i][j].wordBonus) {
                    s += "+";
                } else if (squareBoard[i][j].bonusAmount == 2 && squareBoard[i][j].wordBonus) {
                    s += "*";
                } else if (squareBoard[i][j].bonusAmount == 3 && !squareBoard[i][j].wordBonus) {
                    s += "%";
                } else if (squareBoard[i][j].bonusAmount == 3 && squareBoard[i][j].wordBonus) {
                    s += "$";
                } else {
                    s += " ";
                }
                s += "]\t";
            }
            s += "\n";
        }
        s += "Tile Rack: ";
        for (Tile tile : tileRack) {
            s += "[" + tile.letter + "]";
        }
        return s;
    }

    private void addPoints(Tile tile) {
        if ("aeilnorstu".contains(tile.letter + "")) {
            tile.points = 1;
        } else if ("dg".contains(tile.letter + "")) {
            tile.points = 2;
        } else if ("bcmp".contains(tile.letter + "")) {
            tile.points = 3;
        } else if ("fhvwy".contains(tile.letter + "")) {
            tile.points = 4;
        } else if ("k".contains(tile.letter + "")) {
            tile.points = 5;
        } else if ("jx".contains(tile.letter + "")) {
            tile.points = 8;
        } else if ("qz".contains(tile.letter + "")) {
            tile.points = 10;
        } else if ("_".equals(tile.letter + "")) {
            tile.points = 0;
        }
    }

}
