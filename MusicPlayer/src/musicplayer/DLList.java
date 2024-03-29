/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

/**
 *
 * @author Hamilton1
 */
public class DLList implements LinearListInterface
{

    private DlNode head;
    private DlNode last;
    private DlNode currNode;
    private int iSize;

    DLList()
    {
        head = null;
        last = null;
        iSize = 0;
        currNode = head;
    }

    public boolean isEmpty()
    {
        return (iSize == 0);
    }

    public int size()
    {
        return iSize;
    }

    public void add(int inIndex, Object inElement)
    {
        DlNode newNode = new DlNode(inElement, null, null);
        if (iSize == 0)
        {
            //insert a new Node when the list is empty
            // write your code here
            head = newNode;
            last = newNode;
        } else
        {
            if (inIndex == 1)
            {
                // Insert a new Node at the head position
                // write your code here
                newNode.setNext(head);
                head.setPrev(newNode);
                head = newNode;
            } else if (inIndex == (iSize + 1))
            {
                // Insert a new Node at the last position
                // write your code here
                newNode.setPrev(last);
                last.setNext(newNode);
                last = newNode;
            } else
            {
                // Insert a new node in the middle
                // write your code here
                setCurrent(inIndex);
                newNode.setNext(currNode);
                DlNode prev = currNode.getPrev();
                newNode.setPrev(prev);
                prev.setNext(newNode);
                currNode.setPrev(newNode);
            }
        }
        //don't forget to adjust the size of the DLL
        iSize++;
    }

//    public void remove(int iIndex)
//    {
//        if (iSize > 0)
//        {
//            // If the item is the last node, then last becomes the previous node in relation to the currently last node
//            if (iIndex == iSize)
//            {
//                last = last.getPrev();
//                last.setNext(null);
//            }
//            // If the first item (head) is to be removed, then head becomes the next node after currently first node
//            else if (iIndex == 1)
//            {
//                head = head.getNext();
//                head.setPrev(null);
//            } // If the item is the last node, then last becomes the previous node in relation to the currently last node
//            else
//            {
//                // If the item is not the last or the first node, then the element to be removed is in the middle
//                setCurrent(iIndex);
//                DlNode prev = currNode.getPrev();
//                DlNode next = currNode.getNext();
//                prev.setNext(next);
//                next.setPrev(prev);
//            }
//            currNode = null;
//            iSize--;
//        } else
//        {
//            System.out.println("The list is empty. No Nodes to be removed");
//        }
//    }
    
    
    public void remove(int iIndex) {
        if (iIndex > 0){
            if (iSize > 0) {
                // If the first item (head) is to be removed
                if (iIndex == 1) {
                    if (iSize == 1) { // If there's only one node in the list
                        head = null;
                        last = null;
                    } else { // If there are multiple nodes in the list
                        head = head.getNext();
                        head.setPrev(null);
                    }
                } else if (iIndex == iSize) { // If the last item (last) is to be removed
                    last = last.getPrev();
                    last.setNext(null);
                } else { // If the item is in the middle of the list
                    setCurrent(iIndex);
                    DlNode prev = currNode.getPrev();
                    DlNode next = currNode.getNext();
                    prev.setNext(next);
                    next.setPrev(prev);
                }
                currNode = null;
                iSize--;
            } 
        }
}


    private void setCurrent(int inIndex)
    {
        currNode = head;
        for (int iCount = 1; iCount < inIndex; iCount++)
        {
            currNode = currNode.getNext();
        }
    }

    public Object get(int iIndex)
    {
        setCurrent(iIndex);
        return currNode;
    }
    //************************************************************/
    // Provide a printlist() method which will print out       */
    // the contents of the double linked list, starting with he first node                                */
    //************************************************************/

    public String printList()
    {
        String allItems = new String();
        for (DlNode aNode = head; aNode != null; aNode = aNode.getNext())
        {
            String oneItem = (aNode.getElement()).toString();
            allItems = allItems + oneItem + ", ";
        }
        return allItems;
    }
    //************************************************************/
    // Provide a printListBwd() method which will print out
    // the information from the nodes starting from the last node
    //up to the first node
    //************************************************************/

    public String printListBwd()
    {
        String allItems = new String();
        for (DlNode aNode = last; aNode != null; aNode = aNode.getPrev())
        {
            String oneItem = (aNode.getElement()).toString();
            allItems = allItems + oneItem + ", ";
        }
        return allItems;
    }
    
    
    //added code by Domas Brazdeikis
    //gets index via node name
    public int getIndex(String name)
    {
        int index = 1;
        for (DlNode aNode = head; aNode != null; aNode = aNode.getNext())
        {
            if (aNode.getElement().toString().equalsIgnoreCase(name))
            {
                return index;
            }
            index++;
        }
        return 0;
    }
    
    //send a node forward in the list
    public void sendForward(String name)
    {
        int index = getIndex(name);
        remove(index);
        setCurrent(index);
        if (currNode == null)
        {
            add(index, name);
        }
        else
        {
            add((index + 1), name);
        }

    }

    //sends a node backwards in the list
    public void sendBackward(String name)
    {
        int index = getIndex(name);
        setCurrent(index);
        remove(index);
        if (index > 1)
        {
            add((index - 1), name);
        }
        else
        {
            add(index, name);
        }
        
    }
    
    //replaces a node with a new node
    public void replace(String oldName, String newName)
    {
        int index = getIndex(oldName);
        setCurrent(index);
        remove(index);
        add(index, newName);
    }
    
    //removes all nodes
    public void removeAll()
    {
        while (!isEmpty())
        {
            remove(iSize);
        }
    }
    
    //prints the as a string from given index
    public String printItem(int index)
    {
        DlNode node = head;
        for (int iCount = 1; iCount < index; iCount++)
        {
            node = node.getNext();
        }
        String name = node.getElement().toString();
        
        return name;
    }
    
    //returns the next index of given index
    public int getNextQueue(int index)
    {
        setCurrent(index);
        if (currNode.getNext() == null)
        {
            return 1;
        }
        return index + 1;
    }
    
    //returns previous index of given index
    public int getPrevQueue(int index)
    {
        setCurrent(index);
        if (currNode.getPrev() == null)
        {
            return iSize;
        }
        return index - 1;
    }
}


