/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

/**
 *
 * @author Hamilton1
 */
public interface LinearListInterface {

    public boolean isEmpty();

    public int size();

    public Object get(int iIndex);

    public void remove(int iIndex);

    public void add(int iIndex, Object element);

    public String printList();

    public String printListBwd();
    
    //added by Domas
    public int getIndex(String name);
    
    public void sendForward(String name);
    
//    public void sendForward(int index);
    
    public void sendBackward(String name);
    
    public void replace(String oldName, String newName);
    
    public void removeAll();
    
    public String printItem(int index);
    
    public int getNextQueue(int index);
    
    public int getPrevQueue(int index);
}
