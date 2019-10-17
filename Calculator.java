/*
* author: Wang Hao  
*/
package session04;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;

public class Calculator extends JFrame{
    JTextField t = new JTextField();
    String store = "";
    String[] sp = new String[2];
    char ch;
    double d;
    char flag;
    int count = 0;
    
    public Calculator(Color foreground, Color background, int width, int height){
        /**
         * Create a window
         * @param foreground Foreground color
         * @param background Background color
         * @param width   width of the window
         * @param height  height of the window
        */
        setSize(width, height);
        Container c = getContentPane();
        c.setBackground(background);
        c.setForeground(foreground);
        
        Font f = new Font("Times New Roman", Font.BOLD, 70);
        t.setFont(f);
        t.setBackground(Color.BLACK);
        t.setForeground(Color.WHITE);
        c.add(BorderLayout.NORTH, t);
        
        JButton b = new JButton("test button");
        b.addActionListener((ActionEvent e) ->{  //Win5$1
            System.out.println("testing button");
        });
        c.add(BorderLayout.SOUTH, b);
        b.setFont(f);
        
        JPanel p = new JPanel();
        p.setBackground(new Color(50, 50, 50));
        p.setLayout(new GridLayout(4,4, 10,10));
        String[] labels = {
            "1","2","3","+",
            "4","5","6","-",
            "7","8","9","*",
            "CR","0","=","/",
        };
        for(int i = 0; i < 16; i++){
            JButton button = new JButton(labels[i]);
            p.add(button);
            button.setFont(f);
            button.addActionListener(new MyListener()); 
        }
        
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(2, 1));
        p2.add(p);
        JTextArea textarea = new JTextArea();
        textarea.setFont(f);
        p2.add(textarea);
        c.add(BorderLayout.CENTER, p2);
              
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public static void main(String[] args){
        new Calculator(Color.BLACK, Color.GRAY, 1000, 800);
    }
    
    class MyListener extends JFrame implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            String[] str = {"1","2","3","+","4","5","6","-","7","8","9","*","0","/"};
            String clear = "";
            store += e.getActionCommand();
            for (String s : str) {
                if (e.getActionCommand().equals(s))
                    t.setText(store);  
            }
            if(e.getActionCommand().equals("CR")){  // enter "CR" -> clear textfield
                t.setText(clear);
                store = clear;
                sp = new String[2];
                count = 0;
                ch = 'a';
            }
            if(e.getActionCommand().equals("+")){
                flag = '+';
                count++;
            }
            if(e.getActionCommand().equals("-")){
                flag = '-';
                count++;
            }
            if(e.getActionCommand().equals("*")){
                flag = '*';
                count++;
            }
            if(e.getActionCommand().equals("/")){
                flag = '/';
                count++;
            }
            sp = store.split("\\+|\\-|\\*|\\/|\\=");
            if(count == 1){
                ch = flag;
            }
            System.out.println("sp= " + Arrays.toString(sp) + "  ch= " + ch + " count= " + count);
            if(e.getActionCommand().equals("=") || count > 1){
                if(ch == '+'){
                    d = Double.valueOf(sp[0]) + Double.valueOf(sp[1]);
                }
                if(ch == '-'){
                    d = Double.valueOf(sp[0]) - Double.valueOf(sp[1]);
                }
                if(ch == '*'){
                    d = Double.valueOf(sp[0]) * Double.valueOf(sp[1]);
                }
                if(ch == '/'){
                    d = Double.valueOf(sp[0]) / Double.valueOf(sp[1]);
                }
                System.out.print(d);
                store = String.valueOf(d);
                t.setText(store);
                count = 0;                                     // BUG : "-" minus   <---- ? ----> "-" negative
            }
         }
    }
} 
    
    


