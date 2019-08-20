/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabblesolverexp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JPanel;
import java.io.FileInputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author callumijohnston
 */
public class ScrabblePanel extends JPanel {

    int WIDTH = 700;
    int HEIGHT = 700;
    int square = HEIGHT / 16;
    int border = HEIGHT / 32;
    int line = square / 5;
    int[] selected = new int[2];
    Color findButton = new Color(83, 31, 0);
    ArrayList<Tile> tiles = new ArrayList<>();
    ArrayList<Character> rack = new ArrayList<>();
    boolean tileRack = false;
    int scoreX;
    int scoreY;
    boolean score;
    int theScore;
    int[][] dls = {{1, 4}, {1, 12}, {3, 7}, {3, 9}, {4, 1}, {4, 8}, {4, 15}, {7, 3}, {7, 7}, {7, 9}, {7, 13},
    {8, 4}, {8, 12}, {15, 4}, {15, 12}, {13, 7}, {13, 9}, {12, 1}, {12, 8}, {12, 15}, {9, 3}, {9, 7}, {9, 9}, {9, 13}};
    int[][] tls = {{2, 6}, {2, 10}, {6, 2}, {6, 6}, {6, 10}, {6, 13}, {10, 2},
    {10, 6}, {10, 10}, {10, 13}, {13, 6}, {13, 10}};
    int[][] tws = {{1, 1}, {1, 8}, {1, 15}, {8, 1}, {8, 15}, {15, 1}, {15, 8}, {15, 15}};
    int[][] dws = {{2, 2}, {3, 3}, {4, 4}, {5, 5}, {2, 14}, {3, 13}, {4, 12}, {5, 11},
    {14, 2}, {13, 3}, {12, 4}, {11, 5}, {14, 14}, {13, 13}, {12, 12}, {11, 11}};

    public ScrabblePanel() {
        selected[0] = -1;
        selected[1] = -1;
    }

    public void paint(Graphics g) {
        drawSide(g);
        drawBoard(g);
        drawScore(g);
    }

    public void drawTiles(Graphics g) {
        for (Tile tile : tiles) {
            drawTile(g, tile.col, tile.row, tile.letter);
        }
        for (int i = 0; i < rack.size(); i++) {
            drawImage("src/tiles/" + rack.get(i) + ".jpg", g, WIDTH + border + (i * square + 1), square * 3 + border, square, square);
        }
    }

    private void drawSide(Graphics g) {
        g.setColor(new Color(107, 57, 2));
        g.fillRect(WIDTH, 0, WIDTH / 2, HEIGHT);
        g.setColor(new Color(83, 31, 0));
        g.fillRect(WIDTH + border / 2, square * 3 + border / 2, square * 7 + border, square + border);
//        g.setColor(new Color(102, 51, 0));
        g.fill3DRect(WIDTH + border, square * 3 + border, square * 7, square, tileRack);
        g.setColor(new Color(133, 81, 30));
        g.fillRect(WIDTH + square * 3 - line, square * 7 + line / 2 - line, square * 2 + line * 2, square + line * 2);
        g.setColor(findButton);
        g.fillRect(WIDTH + square * 3, square * 7 + line / 2, square * 2, square);
        g.setColor(new Color(255, 255, 255, 80));
        g.setFont(new Font("", Font.BOLD, 30));
        g.drawString("Tile Rack", WIDTH + (square * 9) / 4, (square * 3));
        g.drawString("Find", WIDTH + square * 3 + line, square * 8 - line / 2);
    }

    public void toggleRack() {
        if (tileRack) {
            tileRack = false;
        } else {
            tileRack = true;
        }
    }

    public void addTile(int col, int row, char letter) {
        for (Tile tile : tiles) {
            if (tile.col == col && tile.row == row) {
                tiles.remove(tile);
                break;
            }
        }
        Tile t = new Tile(row, col, letter);
        tiles.add(t);
    }

    public void addTileToRack(char letter) {
        if (rack.size() < 7) {
            rack.add(letter);
        }
    }

    public void removeTile() {
        for (Tile tile : tiles) {
            if (tile.col == selected[0] && tile.row == selected[1]) {
                tiles.remove(tile);
                break;
            }
        }
    }

    public void fillSelected(Graphics g) {
        fillSquare(g, selected[0], selected[1], new java.awt.Color(0, 0, 0, 50));
    }

    public void drawTile(Graphics g, int col, int row, char letter) {
        drawImage("src/tiles/" + letter + ".jpg", g, border + ((WIDTH * (col - 1)) / 16) + line / 2, border + ((WIDTH * (row - 1)) / 16) + 4, square - line, square - line);
    }

    public void drawBoard(Graphics g) {
        g.setColor(new Color(102, 51, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(new Color(172, 115, 57));
        g.fillRect(border, border, WIDTH - border * 2 - 3, HEIGHT - border * 2 - 3);
        fillSquare(g, 8, 8, new Color(89, 0, 179));
        drawImage("src/scrabblesolverexp/star.png", g, border + ((WIDTH * 7) / 16) + square * 1 / 10,
                border + ((HEIGHT * 7) / 16) + square * 1 / 10, square * 8 / 10, square * 8 / 10);
        fillDLS(g);
        fillTLS(g);
        fillTWS(g);
        fillDWS(g);
        drawTiles(g);
        fillSelected(g);
        g.setColor(new Color(107, 57, 2));
        for (int i = 1; i < 15; i++) {
            g.fillRect(border + ((WIDTH * i) / 16) - (line / 2), border, line, HEIGHT - border * 2 - 3);
        }
        for (int i = 1; i < 15; i++) {
            g.fillRect(border, border + ((HEIGHT * i) / 16) - (line / 2), WIDTH - border * 2 - 3, line);
        }
    }

    public void fillDLS(Graphics g) {
        Color c = new Color(15, 85, 153);
        for (int i = 0; i < dls.length; i++) {
            int[] dlsi = dls[i];
            fillSquare(g, dlsi[1], dlsi[0], c);
            g.setColor(new Color(255, 255, 255, 80));
            g.setFont(new Font("", Font.BOLD, 23));
            g.drawString("DL",
                    border + ((WIDTH * (dlsi[1] - 1)) / 16) + (line * 3 / 4),
                    border + ((HEIGHT * (dlsi[0] - 1)) / 16) + line * 4);
        }
    }

    public void fillTLS(Graphics g) {
        Color c = new Color(38, 77, 0);
        for (int i = 0; i < tls.length; i++) {
            int[] tlsi = tls[i];
            fillSquare(g, tlsi[1], tlsi[0], c);
            g.setColor(new Color(255, 255, 255, 80));
            g.setFont(new Font("", Font.BOLD, 23));
            g.drawString("TL",
                    border + ((WIDTH * (tlsi[1] - 1)) / 16) + (line * 3 / 4),
                    border + ((HEIGHT * (tlsi[0] - 1)) / 16) + line * 4);
        }
    }

    public void fillTWS(Graphics g) {
        Color c = new Color(153, 38, 0);
        for (int i = 0; i < tws.length; i++) {
            int[] twsi = tws[i];
            fillSquare(g, twsi[1], twsi[0], c);
            g.setColor(new Color(255, 255, 255, 80));
            g.setFont(new Font("", Font.BOLD, 23));
            g.drawString("TW",
                    border + ((WIDTH * (twsi[1] - 1)) / 16) + (line * 3 / 5),
                    border + ((HEIGHT * (twsi[0] - 1)) / 16) + line * 4);
        }
    }

    public void fillDWS(Graphics g) {
        Color c = new Color(102, 0, 102);
        for (int i = 0; i < dws.length; i++) {
            int[] dwsi = dws[i];
            fillSquare(g, dwsi[1], dwsi[0], c);
            g.setColor(new Color(255, 255, 255, 80));
            g.setFont(new Font("", Font.BOLD, 22));
            g.drawString("DW",
                    border + ((WIDTH * (dwsi[1] - 1)) / 16) + (line * 3 / 5),
                    border + ((HEIGHT * (dwsi[0] - 1)) / 16) + line * 4);
        }
    }

    public void fillSquare(Graphics g, int col, int row, Color c) {
        g.setColor(c);
        g.fillRect(border + ((WIDTH * (col - 1)) / 16), border + ((HEIGHT * (row - 1)) / 16), square, square);
    }

    /**
     * Creates new Image from file at a location
     *
     * @param file file link (ex: "src/mypackage/myimage.png")
     * @param g Graphics 2D
     * @param x x position (left)
     * @param y y position (top)
     * @param w width
     * @param h height
     */
    public void drawImage(String file, Graphics g, int x, int y, int w, int h) {
        Graphics2D g1 = (Graphics2D) g;
        BufferedImage preImg = null;
        try {
        //    preImg = ImageIO.read(new File(file));
            preImg = ImageIO.read(new FileInputStream(file));
        } catch (IOException e) {
        }
        //Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(file));
        Image img = preImg.getScaledInstance(w, h, 1);
        g1.drawImage(img, x, y, this);
    }

    /**
     * Initializes the JPanel vertical and horizontal layouts
     */
    private void initComponents() {
        setBackground(new Color(70, 100, 245));
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        Group g1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        Group g2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

        g1.addGap(0, 400, Short.MAX_VALUE);
        g2.addGap(0, 300, Short.MAX_VALUE);

        layout.setHorizontalGroup(g1);
        layout.setVerticalGroup(g2);
    }

    void displayScore(int x, int y, int finalPoints) {
        scoreX = x;
        scoreY = y;
        theScore = finalPoints;
        score = true;
        repaint();
    }

    private void drawScore(Graphics g) {
        if (score) {
            g.setColor(Color.GREEN);
            g.fillOval(((WIDTH * (scoreX - 1)) / 16) + line / 2 + square + line +1, 
                    border + ((WIDTH * (scoreY - 1)) / 16) + 4 + square - line*2, square/2, square/2);
            g.setColor(Color.black);
            g.drawOval(((WIDTH * (scoreX - 1)) / 16) + line / 2 + square + line +1, 
                    border + ((WIDTH * (scoreY - 1)) / 16) + 4 + square - line*2, square/2, square/2);
            g.setFont(new Font("", Font.BOLD, 12));
            g.setColor(Color.BLACK);
            g.drawString(theScore + "", ((WIDTH * (scoreX - 1)) / 16) + line / 2 + square +(line*3)/2,
                    border + ((WIDTH * (scoreY - 1)) / 16) + 4 + square );
        }
    }

}

