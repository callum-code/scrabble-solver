/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabblesolverexp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author callumijohnston
 */
public class ScrabbleFrame extends JFrame {

    ScrabblePanel panel = new ScrabblePanel();
    ScrabbleBoard board = new ScrabbleBoard(panel);
    final int WIDTH = (int) (panel.WIDTH * 1.5);
    final int HEIGHT = panel.HEIGHT + 22;

    public ScrabbleFrame() {
        new Trie();
        this.setTitle("Scrabble Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2 + 25);
        setLayout(new BorderLayout());
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        initComponents();
    }

    private void myMousePressed(MouseEvent evt) {
        int x = evt.getX();
        int y = evt.getY() - 22;
        if (x > 0 && x < panel.WIDTH && y > 0 && y < panel.HEIGHT) {
            panel.tileRack = false;
            int row = (y - panel.border) / panel.square;
            int col = (x - panel.border) / panel.square;
            panel.selected[0] = col + 1;
            panel.selected[1] = row + 1;
            panel.repaint();
        } else if (x > panel.WIDTH + panel.border / 2 && y > panel.square * 3 + panel.border / 2
                && x < panel.WIDTH + panel.border / 2 + panel.square * 7 + panel.border
                && y < panel.square * 4 + panel.border / 2 + panel.border
                && !panel.tileRack) {
            panel.toggleRack();
            panel.selected[0] = -1;
            panel.selected[1] = -1;
            panel.repaint();
        } else if (x > panel.WIDTH + panel.square * 2 && y > panel.square * 7
                && x < panel.WIDTH + panel.square * 6 && y < panel.square * 8) {
            panel.findButton = new Color(102, 51, 0);
            panel.repaint();
            board = new ScrabbleBoard(panel);
            board.fillBoard();
            Tile[] result = board.solve();
//            String print = "";
//            if (result != null) {
//                for (Tile tile : result) {
//                    print += tile.letter + "";
//                }
//            }
//            System.out.println(print);
        }
    }

    //cross , blanks, mouse clicked good buttons/interface
    private void myMouseReleased(MouseEvent evt) {
        panel.findButton = new Color(83, 31, 0);
        panel.repaint();
    }

    private void myKeyPressed(KeyEvent evt) {
//        System.out.println(panel.selected[0]);
        char letter = (evt.getKeyChar() + "").toLowerCase().charAt(0);
        if (letter == ' ') {
//            letter = '_';
        }
        if (panel.selected[0] > 0 && panel.selected[1] > 0 && isLetter(letter)) {
            panel.addTile(panel.selected[0], panel.selected[1], letter);
            panel.repaint();
        } else if (evt.getKeyCode() == 8) {
            if (panel.selected[0] > 0 && panel.selected[1] > 0) {
                panel.removeTile();
                panel.repaint();
            } else if (panel.tileRack && panel.rack.size() > 0) {
                panel.rack.remove(panel.rack.size() - 1);
                panel.repaint();
            }
        } else if (panel.tileRack) {
            if (WordFinder.ALPHABET.contains(letter + "")) {
                panel.addTileToRack(letter);
                panel.repaint();
            }
        } else if (evt.getKeyCode() == 37) {
            if (panel.selected[0] > 1) {
                panel.selected[0]--;
                panel.repaint();
            }
        } else if (evt.getKeyCode() == 38) {
            if (panel.selected[1] > 1) {
                panel.selected[1]--;
                panel.repaint();
            }
        } else if (evt.getKeyCode() == 39) {
            if (panel.selected[0] < 15) {
                panel.selected[0]++;
                panel.repaint();
            }
        } else if (evt.getKeyCode() == 40) {
            if (panel.selected[1] < 15) {
                panel.selected[1]++;
                panel.repaint();
            }
        }
        if (evt.getKeyCode() == 10) {
            board = new ScrabbleBoard(panel);
            board.fillBoard();
            Tile[] result = board.solve();
        }
    }
//
//    private void myMouseClicked(MouseEvent evt) {
//        int x = evt.getX();
//        int y = evt.getY() - 22;
//        if (x > panel.WIDTH + panel.square * 2 && y > panel.square * 7
//                && x < panel.WIDTH + panel.square * 6 && y < panel.square * 8) {
//            panel.findButton = new Color(102, 51, 0);
//            panel.repaint();
//            board = new ScrabbleBoard(panel);
//            board.fillBoard();
//            Tile[] result = board.solve();
//        }
//    }

//    private void myMouseMoved(MouseEvent evt) {
//        int x = evt.getX();
//        int y = evt.getY() - 22;
//        if (x > 0 && x < panel.WIDTH && y > 0 && y < panel.HEIGHT) {
//            int row = (y - panel.border) / panel.square;
//            int col = (x - panel.border) / panel.square;
//            panel.repaint();
//        }
//    }
    private void initComponents() {
        add(panel);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                myMousePressed(evt);
            }

            public void mouseReleased(MouseEvent evt) {
                myMouseReleased(evt);
            }

//            public void mouseClicked(MouseEvent evt) {
//                myMouseClicked(evt);
//            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                myKeyPressed(evt);
            }
        });
    }

    private boolean isLetter(char letter) {
        return "abcdefghijklmnopqrstuvwxyz _".contains(letter + "");
    }
}
