/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

/**
 *
 * @author Hamilton1
 */
public class DoublyLinkedListApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* create a DLList object
         * Add 6 names to the list on different positions
         * print the size of the DLL
         * remove some names (nodes) from the list. For example, the 3rd name, the 1st name, the last name
         * print the list after each removal.
         * */

        LinearListInterface myDll = new DLList();
        myDll.add(1, "Bill");
        myDll.add(2, "Alice");
        myDll.add(3, "Elena");
        myDll.add(4, "Paul");
        myDll.add(3, "Danny");
        myDll.add(1, "AnnMarie");
        
        System.out.println(myDll.printList());
        //System.out.println("index: " + myDll.getIndex("Danny"));
        System.out.println(myDll.getIndex("Danny"));
        
        myDll.sendForward("Danny");
        
        System.out.println(myDll.printList());
        
        myDll.sendBackward("Danny");
        
        System.out.println(myDll.printList());

//        System.out.println("The size of the list is " + String.valueOf(myDll.size()));
//        System.out.println("Listing the Elements:");
//        System.out.println(myDll.printList());
//        System.out.println("*********************");
//
//        myDll.remove(3); // Remove element "Alice"
//        System.out.println("Element at index 2 (Alice) was removed");
//        System.out.println("*********************");
//
//        System.out.println("Listing the Elements:");
//        System.out.println(myDll.printList());
//        System.out.println("*********************");
//
//        myDll.remove(1); // Remove element "AnnMarie"
//        System.out.println("Element at index 1 (AnnMarie) was removed");
//        System.out.println("*********************");
//
//        System.out.println("Listing the Elements:");
//        System.out.println(myDll.printList());
//        System.out.println("*********************");
//
//        System.out.println("The size of the list is " + String.valueOf(myDll.size()));
//        System.out.println("*********************");
//
//        myDll.remove(myDll.size()); // Remove element "Paul"
//        System.out.println("The last Element (Paul) was removed");
//        System.out.println("*********************");
//
//        System.out.println("Listing the Elements:");
//        System.out.println(myDll.printList());
//        System.out.println("*********************");
//
//        System.out.println("Printing the Elements in reverse order");
//        System.out.println(myDll.printListBwd());
    }
}
