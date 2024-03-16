/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

import java.util.*;

/**
 *
 * @author Hamilton1
 */
public class MyStack implements StackInterface {

    private ArrayList<String[]> theStack;

    /**
     * Create a new instance of Stack
     */
    public MyStack() {
        theStack = new ArrayList<String[]>();
    }

    public int size() {
        return theStack.size();

    }

    public boolean isEmpty() {
        return theStack.isEmpty();
    }

    /**
     * always false as there is no limit on the size of this ArrayList based
     * stack
     */
    public boolean isFull() {
        return false;
    }

    /**
     * removes the item from the top of the stack and returns it
     */
//    public Object pop() {
//        if (!(theStack.isEmpty())) {
//            return theStack.remove(0);
//        } else {
//            return null;
//        }
//    }
    
    public void pop()
    {
        if (!(theStack.isEmpty()))
        {
            theStack.remove(0);
        }
    }

    /**
     * puts an item onto the top of the stack
     */
    public void push(String[] newItem) {
        theStack.add(0,  newItem);
    }

    // removes all elements from the stack
    public void emptyStack() {
        while (!theStack.isEmpty()) {
            pop();
        }
    }

    // return a String object that consists of all elements from the stack
    // a FOR loop is used here, but you can use an Iterator instead
//    public String displayStack() {
//        int iCount;
//        String sMessage = "";
//        if (theStack.isEmpty()) {
//            sMessage = sMessage.concat("The Stack is EMPTY!");
//        } else {
//            sMessage = "The Stack contains: ";
//            for (iCount = 0; iCount < theStack.size(); iCount++) {
//                sMessage = sMessage.concat(theStack.get(iCount));
//                sMessage = sMessage.concat("; ");
//            }
//        }
//        return sMessage;
//    }
    
    public ArrayList<String[]> getStack()
    {
        return theStack;
    }
    
    public void remove(String[] name)
    {
        int index = getIndex(name);
        if (index != -1)
        {
            theStack.remove(index);
        }
    }
    
    public String[] getValue(int index)
    {
        return theStack.get(index);
    }
    
    public String printAll()
    {
        String output = "";
        
        for (String[] array : theStack)
        {
            for (String value : array)
            {
                output += value;
            }
        }
        return output;
    }
    
    public int getIndex(String[] name)
    {
        //so arrays get compared by their possition in memory?! 
        //therfore i had to make this method
        String find = name[0];
        int index = 0;
        for (String[] item : theStack)
        {
            String look = item[0];
            if (look.equals(find))
            {
                return index;
            }
            index++;
        }
        return -1;
    }
}
