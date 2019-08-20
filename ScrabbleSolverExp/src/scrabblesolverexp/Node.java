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
public class Node {
    char letter;
    Node next;
    Node left;
    boolean cap;
    
    public Node(char c){
        letter = c;
        cap = false;
    }
    
    public void cap(){
        cap = true;
    }
    
}
