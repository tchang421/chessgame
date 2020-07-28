import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/* MY DOCUMENTATION
///////////////////////////////////////
VARIABLES
wkr, wkc, bkr, bkc - color/king/row/column
occupied[][] - array of ints (0: unoccupied, 1: whte piece, 2: black piece)
living[][] - array of piece objects
turn - true is white turn
*/

public class Chess extends JPanel {
   private JButton[][]board = new JButton[8][8];
   private int[][]occupied = new int[8][8]; //0-none, 1-white, 2-black
   private ImageIcon [] imageArray = new ImageIcon[12];
   private boolean selected;
   private int bcheckr, bcheckc, wcheckr, wcheckc;
   int rsel, csel;
   int bkr, bkc, wkr, wkc; // black king's position
   Color previous, previous2, previous3;
   private pieces[][]living = new pieces[8][8]; // active pieces
   private boolean turn; // true - white
   private JButton exit;
   private JPanel bigBoard;
 
   public Chess(){ 
      setLayout(new BorderLayout());
      bigBoard = new JPanel();
      bigBoard.setLayout(new GridLayout(8, 8));
      bcheckc = 0;
      bcheckr = 0;
      wcheckc = 0;
      wcheckr = 0;

      selected = false;
      turn = true;
      
      exit = new JButton("exit");
      exit.addActionListener(new Exit());
      add(exit, BorderLayout.SOUTH);
      rsel = 0;
      csel = 0;
      //TESTING THIS
      imageArray[0] = new ImageIcon("WPawn.PNG");
      imageArray[1] = new ImageIcon("BPawn.PNG");
      imageArray[2] = new ImageIcon("WKnight.PNG");
      imageArray[3] = new ImageIcon("Bknight.PNG");
      imageArray[4] = new ImageIcon("WBishop.PNG");
      imageArray[5] = new ImageIcon("BBishop.PNG");
      imageArray[6] = new ImageIcon("WRook.PNG");
      imageArray[7] = new ImageIcon("BRook.PNG");
      imageArray[8] = new ImageIcon("WQueen.PNG");
      imageArray[9] = new ImageIcon("BQueen.PNG");
      imageArray[10] = new ImageIcon("WKing.PNG");
      imageArray[11] = new ImageIcon("BKing.PNG");
      
      bkc = 4;
      bkr = 0;
      wkc = 4;
      wkr = 7;
        
      setBoard(occupied, board);
      setPieces(board, occupied, living, imageArray);

      for(int r = 0; r<8; r++){
         for(int c = 0; c<8; c++){ 
            add(board[r][c]);  
            previous = board[0][0].getBackground();
            previous3 = Color.gray;
            previous2 = board[0][0].getBackground();
            board[r][c].addActionListener(new PieceSelected(r, c)); 
            bigBoard.add(board[r][c]);
         }
      }
      add(bigBoard, BorderLayout.CENTER);
   }   
      
   private class PieceSelected implements ActionListener{

      int row, col;

      public PieceSelected(int r, int c){
         row = r;
         col = c;
      }  

      public void actionPerformed(ActionEvent PieceSelection){
         ///////////////////////////////////////////////////////////////////////
         //SELECTING A PIECE
         ///////////////////////////////////////////////////////////////////////      
         if (!selected){
            if ((occupied[row][col] == 1 && turn) || (occupied[row][col] == 2 && !turn)){
               selected = true;
               rsel = row;
               csel = col;
               previous = board[row][col].getBackground();
               board[row][col].setBackground(Color.yellow); 
            }
         }
         ///////////////////////////////////////////////////////////////////////
         //UNSELECTING A PIECE
         ///////////////////////////////////////////////////////////////////////  
         else if (selected && rsel == row && csel == col){ 
            selected = false;
            board[rsel][csel].setBackground(previous);
         }
         ///////////////////////////////////////////////////////////////////////
         //MOVING SELECTED PIECE
         /////////////////////////////////////////////////////////////////////// 
         else{
            selected = false;
            board[rsel][csel].setBackground(previous); 
            
            if (living[rsel][csel].getType() <= 2)
               movePawn(row, col);
            else if (living[rsel][csel].getType() <= 4)
               movePiece(row, col);
            else if (living[rsel][csel].getType() >= 5 && noPieceBetween(row, col, rsel, csel))
               movePiece(row, col);
            
            //////////////////////////////////////////////////////////////////
            //HIGHLIGHTING CHECKED KING'S SQUARE RED
            //////////////////////////////////////////////////////////////////
            if (!turn && inCheck(bkr, bkc, 1)){
               if (board[bkr][bkc].getBackground() != Color.red)
                  previous2 = board[bkr][bkc].getBackground();
               board[bkr][bkc].setBackground(Color.red);
               bcheckr = bkr;
               bcheckc = bkc;
            }
            else{
               if (board[bcheckr][bcheckc].getBackground() == Color.red)
                  board[bcheckr][bcheckc].setBackground(previous2);
            }
            
            if (turn && inCheck(wkr, wkc, 2)){
               if (board[wkr][wkc].getBackground() != Color.red)
                  previous3 = board[wkr][wkc].getBackground();
               board[wkr][wkc].setBackground(Color.red);
               wcheckr = wkr;
               wcheckc = wkc;
            }
            else{
               if (board[wcheckr][wcheckc].getBackground() == Color.red)
                  board[wcheckr][wcheckc].setBackground(previous3);
            }   
            
         }
         if (gameOver(turn)){
            for (int r = 0; r < 8; r++){
               for (int c = 0; c < 8; c++){
                  board[r][c].setEnabled(false);
               }
            }
         }
      }
   }  


   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   //MOVING MECHANIC HELPER METHODS
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   public void movePawn(int row, int col){
      if (living[rsel][csel].getType() <= 2){ //PAWN
         if ((occupied[row][col] != occupied[rsel][csel] && occupied[row][col] != 0 
                  && living[rsel][csel].canCapture(row, col))
                  || (occupied[row][col] == 0 && living[rsel][csel].canMove(row, col))
                  && (row!=bkr || col!=bkc) && (row!=wkr || col!=wkc)){ 
            
            pieces temp = living[row][col];
            move(row, col, rsel, csel);
            if (inCheck(wkr, wkc, 2) && turn || inCheck(bkr, bkc, 1) && !turn){
               //RESET THE OLD SPACE
               living[row][col].setLocation(rsel, csel);
               living[rsel][csel] = living[row][col];
               occupied[rsel][csel] = living[rsel][csel].getColor();
               board[rsel][csel].setIcon(board[row][col].getIcon());
               //RESET THE CAPTURED SPACE
               if (temp != null){
                  living[row][col] = temp;
                  occupied[row][col] = temp.getColor();
                  board[row][col].setIcon(imageArray[temp.getType()-1]);
               }
               else{
                  living[row][col] = null;
                  occupied[row][col] = 0;
                  board[row][col].setIcon(null);
               }
            }
            else{
               if (turn && row == 0){
                  living[row][col] = new  queen(row, col, 1, 9);
                  board[row][col].setIcon(imageArray[8]);
               }
               else if (!turn && row == 7){
                  living[row][col] = new  queen(row, col, 2, 10);
                  board[row][col].setIcon(imageArray[9]);
               }
               turn = !turn;
            }
         }
      }
   }

   public void movePiece(int row, int col){
      if (occupied[row][col] != occupied[rsel][csel]
                     && living[rsel][csel].canMove(row, col)
                     && (row!=bkr || col!=bkc) && (row!=wkr || col!=wkc)){
         pieces temp;
         if (occupied[row][col] != 0) temp = living[row][col];
         else temp = null;
         
         if (rsel == wkr && csel == wkc){
            wkr = row; 
            wkc = col;
         }
         if (rsel == bkr && csel == bkc){
            bkr = row;
            bkc = col;
         }
         move(row, col, rsel, csel);
         if (inCheck(wkr, wkc, 2) && turn || inCheck(bkr, bkc, 1) && !turn){
            if (row == wkr && col == wkc){
               wkr = rsel;   
               wkc = csel;
            }
            if (row == bkr && col == bkc){
               bkr = rsel;   
               bkc = csel;
            }
            //RESET THE OLD SPACE
            living[row][col].setLocation(rsel, csel);
            living[rsel][csel] = living[row][col];
            occupied[rsel][csel] = living[rsel][csel].getColor();
            board[rsel][csel].setIcon(board[row][col].getIcon());
            //RESET THE CAPTURED SPACE
            if (temp == null){
               living[row][col] = null;
               occupied[row][col] = 0;
               board[row][col].setIcon(null);
            }
            else{
               living[row][col] = temp;
               occupied[row][col] = temp.getColor();
               board[row][col].setIcon(imageArray[temp.getType()-1]);
            }
         }
         else turn = !turn;  
      }
   }
   
   public boolean noPieceBetween(int r, int c, int rsel, int csel){
      boolean between = false;
      if (r == rsel){ //HORIZONTAL
         for (int i = 0; i < 8; i++){
            if (!between && (i == c || i == csel))
               between = true;
            else if (between && (i == c || i == csel))
               return true;
            else if (between && occupied[r][i] != 0)
               return false;
         }
      }
      else if (c == csel){ //VERTICAL 
         for (int i = 0; i < 8; i++){
            if (!between && (i == r || i == rsel))
               between = true;
            else if (between && (i == r || i == rsel))
               return true;
            else if (between && occupied[i][c] != 0)
               return false;
         }
      }  
      else{ //DIAGONAL
         int incR = 0, incC = 0, newR = r, newC = c;
         if (r > rsel) incR = -1; else incR = 1;
         if (c > csel) incC = -1; else incC = 1;

         while (Math.abs(newR-rsel) != 0){
            if (newR != r && occupied[newR][newC] != 0)
               return false;
            else{
               newR = newR + incR;
               newC = newC + incC;
            }
         } 
      }      
      return true;
   } 
   
   public void move(int row, int col, int rsel, int csel){
      living[rsel][csel].setLocation(row, col);
      living[row][col] = living[rsel][csel];
      living[rsel][csel] = null;
      occupied[rsel][csel] = 0;
      occupied[row][col] = living[row][col].getColor();
      board[row][col].setIcon(board[rsel][csel].getIcon());
      board[rsel][csel].setIcon(null);
   }
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   //MOVING MECHANIC HELPER METHODS
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   //CHECK, CHECKMATE, STALEMATE HELPER METHODS
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   public boolean inCheck(int r, int c, int color){
      for (int i = 0; i < 8; i++){
         for (int q = 0; q < 8; q++){
            if (occupied[i][q] == color && living[i][q].canCapture(r, c) && noPieceBetween(i, q, r, c))
               return true;  
         }
      }
      return false;
   }
   
   public int howManyChecking(int r, int c, int color){
      int count = 0;
      for (int i = 0; i < 8; i++){
         for (int q = 0; q < 8; q++){
            if (occupied[i][q] == color && living[i][q].canCapture(r, c) == true 
                                       && noPieceBetween(i, q, r, c) == true)
               count++; 
         }
      }
      return count;
   }

   public boolean BlockACheck(int r, int c, int rsel, int csel, int movedr, int movedc){
      if (r == rsel && r == movedr){ // horizontal 
         if (c > csel + 1){ //moves left
            for (int i = csel+1; i < c; i++){
               if (i == movedc) return true;
            }
         }
         else{ //move right
            for (int i = csel-1; i > c; i--){
               if (i == movedc) return true;
            }
         }
      }
      
      else if (c == csel && c == movedc){ // vertical
         if (r > rsel + 1){ //moves up
            for (int i = rsel+1; i < r; i++){
               if (i == movedr) return true;
            }
         }
         else{ //move rdown
            for (int i = rsel-1; i > r; i--){
               if (i == movedr) return true;
            }
         }
      }
      
      else{ //diagonal
         if (rsel+1 < r && csel -1 > c){ // up  and right
            for (int i = r-1; i > rsel; i --){
               for (int p = c+1; p < csel; p++){
                  if ((i == movedr && p == movedc) && Math.abs(movedc - csel) == Math.abs(movedr - rsel))
                     return true;
               }
            }
         }
         
         else if (rsel+1 < r  && csel + 1 < c){ // up and left
            for (int i = r-1; i > rsel; i --){
               for (int p = c-1; p > csel; p--){
                  if ((i == movedr && p == movedc) && Math.abs(movedc - csel) == Math.abs(movedr - rsel))
                     return true;
               }
            }
         }
         
         else if (rsel-1 > r && csel -1 > c){ // down and right
            for (int i = r + 1; i < rsel; i++){
               for (int p = c +1; p < csel; p++){
                  if ((i == movedr && p == movedc) && Math.abs(movedc - csel) == Math.abs(movedr - rsel))
                     return true;
               }
            }
         }
         
         else{ // down and left
            for (int i = r+1; i < rsel; i++){
               for (int p = c-1; p > csel; p--){
                  if ((i == movedr && p == movedc) && Math.abs(movedc - csel) == Math.abs(movedr - rsel))
                     return true;
               }
            }
         }
      }
      return false;
   }

   public boolean gameOver(boolean turn){
      if (insufficientMat()) return true;

      if (turn){//WHITE IN CHECKMATE
         

      }
      return false;
   }
    
   public boolean insufficientMat(){
      int white = 0, black = 0;
      for (int r = 0; r < 8; r ++){
         for (int c = 0; c < 8; c ++){
            if (occupied[r][c] == 1 && living[r][c].getType() != 1 && living[r][c].getType() != 7 && living[r][c].getType() != 9 && living[r][c].getType() != 11)
               white = white + living[r][c].getType();
            else if (occupied[r][c] == 2 && living[r][c].getType() != 2 && living[r][c].getType() != 8 && living[r][c].getType() != 10 && living[r][c].getType() != 12)
               black = black + living[r][c].getType();
         }
      }
      if ((white == 3 || white == 5) && (black == 4 && black == 6))
         return true;
      return false;
   }
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   //CHECK, CHECKMATE, STALEMATE HELPER METHODS
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   //GAME AND BOARD SET UP HELPER METHODS
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   public static void setBoard(int[][] oc, JButton[][] b){
      for(int r = 0; r<8; r++){
         for(int c = 0; c<8; c++){ 
            // SETS THE BOARD OCCPUATION
            if (r<2)
               oc[r][c] = 2;
            else if (r>5)
               oc[r][c] = 1;
            else 
               oc[r][c] = 0;

            // SETS THE COLOR OF THE BOARD
            b[r][c] = new JButton();
            if ((r+c)%2 == 0)
               b[r][c].setBackground(Color.white);
            else
               b[r][c].setBackground(Color.gray);
            b[r][c].setOpaque(true);
            b[r][c].setBorderPainted(false); 
         }
      }
   }

   public static void setPieces(JButton[][]b, int[][]o, pieces[][]l, ImageIcon[]i){
      //SETS THE OBJECT OF EACH PIECE
      for (int r = 0; r < 8; r++){
         for (int c = 0; c < 8; c++){
            if (r == 2){
               r = 6;
               c = -1;
               continue;
            }
            int isBlack = 1;
            if (r == 7) isBlack = 0;

            if (r == 1) //BLACK PAWN
               l[r][c] = new pawn(r, c, o[r][c], 2);
            else if (r == 6) //WHITE PAWN
               l[r][c] = new pawn(r, c, o[r][c], 1);
            else if (c == 0 || c == 7) //BLACK ROOK
               l[r][c] = new rook(r, c, o[r][c], 7+isBlack);
            else if (c == 1 || c == 6) //BLACK KNIGHT
               l[r][c] = new knight(r, c, o[r][c], 3+isBlack);
            else if (c == 2 || c == 5) //BLACK BISHOP
               l[r][c] = new bishop(r, c, o[r][c], 5+isBlack);
            else if (c == 4) //BLACK KING
               l[r][c] = new king(r, c, o[r][c], 11+isBlack);
            else  //BLACK QUEEN
               l[r][c] = new queen(r, c, o[r][c], 9+isBlack);
         }
      }
      //SETS THE IMAGE FOR THE BUTTON
      for (int r = 0; r < 8; r++){
         for (int c = 0; c < 8; c++){
            if (l[r][c] != null)
               b[r][c].setIcon(i[l[r][c].getType()-1]);
         }
      }
   }
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////
   //GAME AND BOARD SET UP HELPER METHODS
   ////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////


   private class Exit implements ActionListener{    
      public void actionPerformed(ActionEvent click){
         JOptionPane.showMessageDialog( new JOptionPane(),
         "Thanks for playing,\n"
         + "Tae Chang.", "Tae Chang",
         JOptionPane.INFORMATION_MESSAGE);
         System.exit(0);
      }
   }
}     