/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

import java.util.ArrayList;

/**
 *
 * @author Hamilton1
 */
public interface StackInterface {

    public boolean isEmpty();

    public boolean isFull();

    public void push(String[] newItem);

    //public Object pop();
    
    public void pop();

    public int size();

    public void emptyStack();

    //public String displayStack();
    
    public ArrayList<String[]> getStack();
    
    public void remove(String[] name);
    
    public String[] getValue(int index);
    
    public String printAll();
    
    public int getIndex(String[] name);
}
