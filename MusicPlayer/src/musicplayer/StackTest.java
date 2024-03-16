/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package musicplayer;

/**
 *
 * @author domas
 */
public class StackTest
{
    public static void main(String[] args)
    {
        StackInterface stack = new MyStack();
        
        String[] s1 = {"a","b"};
        String[] s2 = {"c","d"};
        
        stack.push(s1);
        stack.push(s2);
        
        stack.remove(s2);
        
        System.out.println(stack.printAll());
        
    }
}
