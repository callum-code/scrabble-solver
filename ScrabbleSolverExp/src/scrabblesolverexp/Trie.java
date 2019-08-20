/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrabblesolverexp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author callumijohnston
 */
public class Trie {

    static Scanner reader;
    static Node root=new Node('*');

    public Trie() {
        File file = new File("src/scrabblesolverexp/dictionary.txt");
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException ex) {
            System.out.println("shit man");
        }
        buildTrie();
    }

    public static boolean displayWord(String word) {
        word = word.toUpperCase();
        Node N = findLast(word, root);
        if (N==null){
            return false;
        } else if (N.cap) {
            return true;
        }
        return false;
    }

    public static Node findLast(String word, Node temp) {
        if (word.isEmpty())return temp;
        if (temp.next == null) {
            return null;
        } else if (temp.next.letter == word.charAt(0)) {
            word = word.substring(1, word.length());
            return findLast(word, temp.next);
        } else {
            temp = temp.next;
            while (temp.left != null && temp.left.letter != word.charAt(0)) {
                temp = temp.left;
            }
            if (temp.left == null){
                return null;
            }
            if (temp.left.letter == word.charAt(0)) {
                word = word.substring(1, word.length());
                return findLast(word,temp.left);
            } 
        }
        return null;
    }

    private static void buildTrie() {
        while (reader.hasNext()) {
            String word = reader.nextLine();
            Node temp = root;
            addWord(temp, word);
        }
    }

    private static void addWord(Node temp, String word) {
        while (!word.isEmpty()) {
            if (temp.next == null) {
                temp.next = new Node(word.charAt(0));
                word = word.substring(1, word.length());
                temp = temp.next;
            } else if (temp.next.letter == word.charAt(0)) {
                temp = temp.next;
                word = word.substring(1, word.length());
            } else {
                temp = temp.next;
                while (temp.left != null && temp.left.letter != word.charAt(0)) {
                    temp = temp.left;
                }
                 if (temp.left == null) {
                    temp.left = new Node(word.charAt(0));
                    word = word.substring(1, word.length());
                    temp = temp.left;
                }else if (temp.left.letter == word.charAt(0)) {
                    temp = temp.left;
                    word = word.substring(1, word.length());
                }
            }
            if (word.isEmpty()) {
                temp.cap();
            }
        }
    }

}
