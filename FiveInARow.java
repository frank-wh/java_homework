/*
* author: Hao Wang
*/
package session05;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.JOptionPane.*;
/**************************************************************************
 * FIVE-IN-A-ROW GAME。                                                   *
 * Example To Win:                                                        *
 *                       。        。                               。    *
 *                       。           。                         。       *
 *  。 。 。 。 。       。              。                   。          *
 *                       。                 。             。             *
 *                       。                    。       。                *
 **************************************************************************/
public class FiveInARow extends JFrame {
    public int width = 766;
    public int height = 810;
    boolean isBlack = true;
    int board[][] = new int[15][15];  // chessboard size 15x15
    int flag = 0;
    int repaintX;
    int repaintY;
    
    public static void main(String[] args) {
        FiveInARow g = new FiveInARow();
    }      
    
    public FiveInARow(){
        super("Five In A Row");  
        setSize(width, height);  
        buildMenus();
        GameBoard b = new GameBoard();
        b.setBackground(Color.getHSBColor(15, 65, 61));     // HSB -> background color
        Container c = getContentPane();
        c.add(BorderLayout.CENTER, b);
        setResizable(false);                                // make the size unchangable
        setLocationRelativeTo(null);                        // make chessboard middle of the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);     
    }    
    
    private void buildMenus(){
        JMenuBar mBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        
        JMenuItem mItem = new JMenuItem("Show message");
        mItem.addActionListener((ActionEvent e) -> {
                showMessageDialog(null, "Author: Hao Wang\nDate: 09/28/2019", "Message", JOptionPane.CLOSED_OPTION);
            });
        menu.add(mItem);
        
        mItem = new JMenuItem("New Game");
        mItem.addActionListener((ActionEvent e) -> {
                super.repaint();
                flag = 0;
                isBlack = true;
                for(int i = 0;i < 15; i++){
                    for(int j = 0;j < 15; j++){
                            board[i][j] = 0;
                        }
                    }
            });
        menu.add(mItem);
        
//        mItem = new JMenuItem("Redo");
//        mItem.addActionListener((ActionEvent e) -> {
//                //super.repaint(50,50,repaintX*50, 44 + repaintY*50);
//                
//                isBlack = isBlack != true;
//                board[repaintX][repaintY] = 0;
//            });
//        menu.add(mItem);
        
        mItem = new JMenuItem("Quit");
        mItem.addActionListener((ActionEvent e) -> {
                System.exit(0);
            }); // FiveInARow$1
        menu.add(mItem);
        
        mBar.add(menu);
        setJMenuBar(mBar);
    } 
    
    class GameBoard extends JPanel{    
        public GameBoard(){
            MoveListener m = new MoveListener();
            addMouseListener(m);
            addMouseMotionListener(m);
        }
        
        @Override
        public void paint(Graphics g){
            super.paint(g);            
            for(int horizontal = 25 ; horizontal < height; horizontal+= 50){  // draw chessboard
                for(int vertical = 25; vertical < width ; vertical+= 50){   //  horizontal  vertical
                    g.drawLine(25, horizontal, width-41, horizontal);
                    g.drawLine(vertical, 25, vertical, height-85);
                    
                    g.drawOval(25 + 3*50 - 3, 25 + 3*50 - 3,   6, 6);
                    g.drawOval(25 + 11*50 - 3, 25 + 11*50 - 3,   6, 6); 
                    g.drawOval(25 + 7*50 - 3, 25 + 7*50 - 3,   6, 6);  
                    g.drawOval(25 + 3*50 - 3, 25 + 11*50 - 3,   6, 6);  
                    g.drawOval(25 + 11*50 - 3, 25 + 3*50 - 3,   6, 6);
                }
            }
        }

        private class MoveListener implements MouseListener, MouseMotionListener {
            @Override
            public void mousePressed(MouseEvent e) {
                if(flag!=1){                                  // flag -> judge whether the game has over
                    Graphics g = getGraphics();
                    int x = e.getX() / 50;
                    int y = e.getY() / 50;
                    repaintX = x;
                    repaintY = y;
                    if(board[x][y] == 0){
                        if(isBlack == true){
                            board[x][y] = 1;
                            isBlack = false;
                            g.setColor(Color.BLACK);
                            g.fillOval(x*50, y*50, 50, 50);
                        } else {
                            board[x][y] = 2;
                            isBlack = true;
                            g.setColor(Color.WHITE);
                            g.fillOval(x*50, y*50, 50, 50);
                        }
                    }
                    isWin();
                }
            }
            
            private int isWin(){
                for(int i = 0;i < 15; i++){
                    for(int j = 0;j < 15; j++){
            // horizontal
                        if(i<=10){
                            if(board[i][j]==1 && board[i+1][j]==1 && board[i+2][j]==1 && board[i+3][j]==1 && board[i+4][j]==1){
                                showMessageDialog(null, "Black win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            } 
                            else if(board[i][j]==2 && board[i+1][j]==2 && board[i+2][j]==2 && board[i+3][j]==2 && board[i+4][j]==2){
                                showMessageDialog(null, "White win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            } 
                        }
                        if(i>=4){
                            if(board[i][j]==1 && board[i-1][j]==1 && board[i-2][j]==1 && board[i-3][j]==1 && board[i-4][j]==1){
                                showMessageDialog(null, "Black win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                            else if(board[i][j]==2 && board[i-1][j]==2 && board[i-2][j]==2 && board[i-3][j]==2 && board[i-4][j]==2){
                                showMessageDialog(null, "White win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                        }
            // vertical
                        if(j>=4){
                            if(board[i][j]==1 && board[i][j-1]==1 && board[i][j-2]==1 && board[i][j-3]==1 && board[i][j-4]==1){
                                showMessageDialog(null, "Black win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                            else if(board[i][j]==2 && board[i][j-1]==2 && board[i][j-2]==2 && board[i][j-3]==2 && board[i][j-4]==2){
                                showMessageDialog(null, "White win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                        }
                        if(j<=10){
                            if(board[i][j]==1 && board[i][j+1]==1 && board[i][j+2]==1 && board[i][j+3]==1 && board[i][j+4]==1){
                                showMessageDialog(null, "Black win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                            else if(board[i][j]==2 && board[i][j+1]==2 && board[i][j+2]==2 && board[i][j+3]==2 && board[i][j+4]==2){
                                showMessageDialog(null, "White win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                        }
            // left diagonal 
                        if(i<=10 && j<=10){
                            if(board[i][j]==1 && board[i+1][j+1]==1 && board[i+2][j+2]==1 && board[i+3][j+3]==1 && board[i+4][j+4]==1){
                                showMessageDialog(null, "Black win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                            else if(board[i][j]==2 && board[i+1][j+1]==2 && board[i+2][j+2]==2 && board[i+3][j+3]==2 && board[i+4][j+4]==2){
                                showMessageDialog(null, "White win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                        }
                        if(i>=4 && j>=4){
                            if(board[i][j]==1 && board[i-1][j-1]==1 && board[i-2][j-2]==1 && board[i-3][j-3]==1 && board[i-4][j-4]==1){
                                showMessageDialog(null, "Black win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                            else if(board[i][j]==2 && board[i-1][j-1]==2 && board[i-2][j-2]==2 && board[i-3][j-3]==2 && board[i-4][j-4]==2){
                                showMessageDialog(null, "White win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                        }
            // right diagonal
                        if(i<=10 && j>=4){
                            if(board[i][j]==1 && board[i+1][j-1]==1 && board[i+2][j-2]==1 && board[i+3][j-3]==1 && board[i+4][j-4]==1){
                                showMessageDialog(null, "Black win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                            else if(board[i][j]==2 && board[i+1][j-1]==2 && board[i+2][j-2]==2 && board[i+3][j-3]==2 && board[i+4][j-4]==2){
                                showMessageDialog(null, "White win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                        }
                        if(i>=4 && j<=10){
                            if(board[i][j]==1 && board[i-1][j+1]==1 && board[i-2][j+2]==1 && board[i-3][j+3]==1 && board[i-4][j+4]==1){
                                showMessageDialog(null, "Black win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                            else if(board[i][j]==2 && board[i-1][j+1]==2 && board[i-2][j+2]==2 && board[i-3][j+3]==2 && board[i-4][j+4]==2){
                                showMessageDialog(null, "White win", "Game Over", JOptionPane.CLOSED_OPTION);
                                return flag =1;
                            }
                        }
                    }
                }
                return flag;
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent arg0) {
            }
            @Override
            public void mouseEntered(MouseEvent arg0) {
            }
            @Override
            public void mouseExited(MouseEvent arg0) {
            }
            @Override
            public void mouseMoved(MouseEvent arg0) {
            }
            @Override
            public void mouseClicked(MouseEvent arg0) {
            }
        }
    }
}