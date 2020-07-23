import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/* MY DOCUMENTATION
///////////////////////////////////////
VARIABLES
bkr - black king row
occupied[][] - array of ints (0: unoccupied, 1: whte piece, 2: black piece)
living[][] - array of piece objects

///////////////////////////////////////
when dealing with colors - 1 is white, 2 is black

TYPES
1 - wp
2 - bp
3 - wk
4 - bk
5 - wb
6 - bb
7 - wr
8 - br
9 - wq
10 - bq
11 - wking
12 - bking


*/

public class Chess extends JPanel
{
   private JButton[][]Board = new JButton[8][8];
   private int[][]occupied = new int[8][8]; //0-none, 1-white, 2-black
   private ImageIcon bb, wb, bp, wp, bk, wk, br, wr, bK, wK, bq, wq;
   private boolean selected;
   private int bcheckr, bcheckc, wcheckr, wcheckc;
   int rsel, csel;
   int bkr, bkc, wkr, wkc; // black king's position
   Color previous, previous2, previous3; // stack overflow
   private pieces[][]living = new pieces[8][8]; // active pieces
   private boolean turn, captured; // true - white
   private boolean WIC, BIC, WKORM, BKORM;
   private JButton exit;
   private JPanel BigBoard;

   public static void setBoard(int[][] oc, JButton[][] b){
      for(int r = 0; r<8; r++)
      {
         for(int c = 0; c<8; c++)
         { 
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
            b[r][c].setOpaque(true);// stack overflow
            b[r][c].setBorderPainted(false); // stack overflow

         }
      }
   }
 
   public Chess()
   { 
      setLayout(new BorderLayout());
      BigBoard = new JPanel();
      BigBoard.setLayout(new GridLayout(8, 8));
      WKORM = false;
      BKORM = false;
      bcheckc = 0;
      bcheckr = 0;
      wcheckc = 0;
      wcheckr = 0;
      captured = false;
      
      selected = false;
      turn = true;
      
      WIC = false;
      BIC = false;
      exit = new JButton("exit");
      exit.addActionListener(new Exit());
      add(exit, BorderLayout.SOUTH);
      rsel = 0;
      csel = 0;
      int x = 1;
      wb = new ImageIcon("WBishop.PNG");
      bb = new ImageIcon("BBishop.PNG");
      bp = new ImageIcon("BPawn.PNG");
      wp = new ImageIcon("WPawn.PNG");
      bk = new ImageIcon("Bknight.PNG");
      wk = new ImageIcon("WKnight.PNG");
      br = new ImageIcon("BRook.PNG");
      wr = new ImageIcon("WRook.PNG");
      bK = new ImageIcon("BKing.PNG");
      wK = new ImageIcon("WKing.PNG");
      bq = new ImageIcon("BQueen.PNG");
      wq = new ImageIcon("WQueen.PNG");
      
      bkc = 4;
      bkr = 0;
      wkc = 4;
      wkr = 7;
        
      setBoard(occupied, Board);
      for(int r = 0; r<8; r++)
      {
         for(int c = 0; c<8; c++)
         { 
            add(Board[r][c]);  
            previous = Board[0][0].getBackground();//stackoverflow
            previous3 = Color.gray;
            previous2 = Board[0][0].getBackground();
            
            Board[r][c].addActionListener(new PieceSelected(r, c));
            
            // instantiates Icons and piece objects
            if (r == 1)
            {
               Board[r][c].setIcon(bp);
               living[r][c] = new pawn(r, c, occupied[r][c], 2);
                           }
            else if (r == 6)
            {
               Board[r][c].setIcon(wp);
               living[r][c] = new pawn(r, c, occupied[r][c], 1);
            }
            else if (r == 0)
            {
               if (c == 0 || c == 7)
               {
                  Board[r][c].setIcon(br);
                  living[r][c] = new Brook(r, c, occupied[r][c], 8);
               }
               else if (c == 1 || c == 6) // black knight
               {
                  Board[r][c].setIcon(bk);
                  living[r][c] = new Bknight(r, c, occupied[r][c], 4);
               }
               else if (c == 2 || c == 5)
               {
                  Board[r][c].setIcon(bb);
                  living[r][c] = new Bbishop(r, c, occupied[r][c], 6);
               }
               else if (c == 4)
               {
                  Board[r][c].setIcon(bK);
                  living[r][c] = new Bking(r, c, occupied[r][c], 12);
               }
               else 
               {
                  Board[r][c].setIcon(bq);
                  living[r][c] = new Bqueen(r, c, occupied[r][c], 10);
               }
            }
            else if (r == 7)
            {
               if (c == 0 || c == 7)
               {
                  Board[r][c].setIcon(wr);
                  living[r][c] = new Wrook(r, c, occupied[r][c], 7);
               }
               else if (c == 1 || c == 6) // White knight
               {
                  Board[r][c].setIcon(wk);
                  living[r][c] = new Wknight(r, c, occupied[r][c], 3);
               }
               else if (c == 2 || c == 5)
               {
                  Board[r][c].setIcon(wb);
                  living[r][c] = new Wbishop(r, c, occupied[r][c], 5);
               }
               else if (c == 4)
               {
                  Board[r][c].setIcon(wK);
                  living[r][c] = new Wking(r, c, occupied[r][c], 11);
               }
               else 
               {
                  Board[r][c].setIcon(wq);
                  living[r][c] = new Wqueen(r, c, occupied[r][c], 9);
               }
            }
          BigBoard.add(Board[r][c]);
         }
      }
      add(BigBoard, BorderLayout.CENTER);
   }   
      
   
   
   
   

   
   
   
   private class PieceSelected implements ActionListener
   {
      int row, col;
      public PieceSelected(int r, int c)
      {
         row = r;
         col = c;
      }
      
      
      
      
      
      
      
      
      
      
      //////////////////////////////////////////////////////
      //ACTION LISTENER
      /////////////////////////////////////////////////////
         
      public void actionPerformed(ActionEvent PieceSelection)
      {

         
         //is a king in check
         WIC = false;
         BIC = false;
         if (WhiteInCheck(wkr, wkc)) //white
         {
            WIC = true;
         }
         if (BlackInCheck(bkr, bkc)) //black
         {
            BIC = true;
         }
         
      
         
         if (!selected)                   // highlights square
         {
            if ((occupied[row][col] == 1 && turn) || (occupied[row][col] == 2 && !turn))
            {
               selected = true;
               rsel = row;
               csel = col;
               previous = Board[row][col].getBackground();
               Board[row][col].setBackground(Color.yellow); // stack overflow
            }
            captured = false;
         }
         
         else if (selected && rsel == row && csel == col)   //unhighlight
         {
            selected = false;
            Board[rsel][csel].setBackground(previous);
            captured = false;
         }
         
         
         else// moves or captures ////////////////////
         {
            selected = false;
            Board[rsel][csel].setBackground(previous); // stack overflow
            if (living[rsel][csel].getType() == 1 && living[rsel][csel].getColor() == 1) // White Pawns
            {
/*capturing*/  if (occupied[row][col] == 2 && living[rsel][csel].canCapture(row, col) && NoPieceBetween(row, col, rsel, csel) && (row!=bkr || col!=bkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  
                  if (WIC && HowManyCheckingWhite(wkr, wkc) == 1)
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))
                     {
                        Capture(row, col, rsel, csel);
                        turn = !turn;
                        if (row == 0)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Wqueen(row, col, 1, 9);
                           Board[row][col].setIcon(wq);
                        }
                     }
                  }
                  else if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  else
                  {
                     
                        occupied[rsel][csel] = 0;
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                              if (row == 0)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Wqueen(row, col, 1, 9);
                           Board[row][col].setIcon(wq);
            
                        }
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                           Capture(row, col, rsel, csel); 
                           if (row == 0)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Wqueen(row, col, 1, 9);
                           Board[row][col].setIcon(wq);
            
                        }
                        }
                        turn = !turn;
                     
                  }
               }
/*moving*/     else if (occupied[row][col] == 0 && living[rsel][csel].canMove(row, col))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  
                  if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() != 4 && HowManyCheckingWhite(wkr, wkc) == 1)
                    {
                     if (BlockACheck(wkr, wkc, WhichPieceIsCheckingWhite(wkr, wkc).getRow(),  WhichPieceIsCheckingWhite(wkr, wkc).getCol(), row, col))
                     {
                      
                           Capture(row, col, rsel, csel);
                           turn = !turn;
                           if (row == 0)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Wqueen(row, col, 1, 9);
                           Board[row][col].setIcon(wq);
            
                        }
                        
                     }
                  }
                  
                  else if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  else if(!WIC)
                  {
                     
                        occupied[rsel][csel] = 0;
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                              if (row == 0)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Wqueen(row, col, 1, 9);
                           Board[row][col].setIcon(wq);
            
                        }
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                           Capture(row, col, rsel, csel); 
                           if (row == 0)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Wqueen(row, col, 1, 9);
                           Board[row][col].setIcon(wq);
            
                        }
                        }
                        turn = !turn;
                     
                  }
               } 
            }
            
            else if (living[rsel][csel].getType() == 2  && living[rsel][csel].getColor() == 2) // black pawns
            {
               if (occupied[row][col] == 1 && living[rsel][csel].canCapture(row, col) && NoPieceBetween(row, col, rsel, csel)&& (row!=wkr || col!=wkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  
                  if (BIC && HowManyCheckingBlack(bkr, bkc) == 1)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc))
                     {
                        Capture(row, col, rsel, csel); 
                        turn = !turn;
                        if (row == 7)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Bqueen(row, col,2, 10);
                           Board[row][col].setIcon(bq);
            
                        }
                     }                    
                  }
                  else if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else
                  {
                        occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                              if (row == 7)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Bqueen(row, col,2, 10);
                           Board[row][col].setIcon(bq);
            
                        }
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           Capture(row, col, rsel, csel);
                           if (row == 7)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Bqueen(row, col,2, 10);
                           Board[row][col].setIcon(bq);
            
                        } 
                        }
                        turn = !turn;
                  }
               } 
               else if (occupied[row][col] == 0 && living[rsel][csel].canMove(row, col) && NoPieceBetween(row, col, rsel, csel)&& (row!=wkr || col!=wkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  
                  if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() != 3 && HowManyCheckingBlack(bkr, bkc) == 1)
                    {
                     if (BlockACheck(bkr, bkc, WhichPieceIsCheckingBlack(bkr, bkc).getRow(),  WhichPieceIsCheckingBlack(bkr, bkc).getCol(), row, col))
                     {
                        Capture(row, col, rsel, csel); 
                        turn = !turn;
                        if (row == 7)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Bqueen(row, col,2, 10);
                           Board[row][col].setIcon(bq);
            
                        }
                     }
                  }
                  else if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else if(!BIC)
                  {
                     occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                              if (row == 7)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Bqueen(row, col,2, 10);
                           Board[row][col].setIcon(bq);
            
                        }
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           Capture(row, col, rsel, csel); 
                           if (row == 7)
                        {
                           living[row][col] = null;
                           living[row][col] = new  Bqueen(row, col,2, 10);
                           Board[row][col].setIcon(bq);
            
                        }
                        }
                        turn = !turn;
                  }
               }
            }
            
            else if (living[rsel][csel].getType() == 3) // white knight
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 1 && (row!=bkr || col!=bkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  
                  if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() != 4)//////for white
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc) || BlockACheck(wkr, wkc, WhichPieceIsCheckingWhite(wkr, wkc).getRow(),  WhichPieceIsCheckingWhite(wkr, wkc).getCol(), row, col))/*:)*/
                     {
                        Capture(row, col, rsel, csel);
                        turn = !turn;
                     }
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() == 4)
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))/*:)*/
                     {
                        Capture(row, col, rsel, csel);
                        turn = !turn;
                     }
                  }
                  else
                  {
                        occupied[rsel][csel] = 0;
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                           Capture(row, col, rsel, csel); 
                        }
                        turn = !turn;
                  } /////// for white
               }
            }
            
            else if (living[rsel][csel].getType() == 4) // black knight
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 2 && (row!=wkr || col!=wkc))
               {
                  
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  
                  if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() != 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc) || BlockACheck(bkr, bkc, WhichPieceIsCheckingBlack(bkr, bkc).getRow(),  WhichPieceIsCheckingBlack(bkr, bkc).getCol(), row, col))/*:)*/
                     {
                        Capture(row, col, rsel, csel); 
                        turn = !turn;
                     }
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() == 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc))
                     {
                        Capture(row, col, rsel, csel); 
                        turn = !turn;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           Capture(row, col, rsel, csel); 
                        }
                        turn = !turn;
                  }
               }
            }
            
            else if (living[rsel][csel].getType() == 7)// white rook
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 1 && NoPieceBetween(row, col, rsel, csel) && (row!=bkr || col!=bkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() != 4)//////for white
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc) || BlockACheck(wkr, wkc, WhichPieceIsCheckingWhite(wkr, wkc).getRow(),  WhichPieceIsCheckingWhite(wkr, wkc).getCol(), row, col))/*:)*/
                     {
                        Capture(row, col, rsel, csel);
                        turn = !turn;
                        WKORM = true;
                     }
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() == 4)
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))/*:)*/
                     {
                        Capture(row, col, rsel, csel);
                        turn = !turn;
                        WKORM = true;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                              WKORM = true;
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                           Capture(row, col, rsel, csel); 
                           WKORM = true;
                        }
                        turn = !turn;
                  } /////// for white
               }
            }
            
            else if (living[rsel][csel].getType() == 8)// black rook
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 2 && NoPieceBetween(row, col, rsel, csel)&& (row!=wkr || col!=wkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  
                  if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() != 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc) || BlockACheck(bkr, bkc, WhichPieceIsCheckingBlack(bkr, bkc).getRow(),  WhichPieceIsCheckingBlack(bkr, bkc).getCol(), row, col))/*:)*/
                     {
                        Capture(row, col, rsel, csel); 
                        turn = !turn;
                        BKORM = true;
                     }
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() == 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc))/*:)*/
                     {
                        Capture(row, col, rsel, csel); 
                        turn = !turn;
                        BKORM = true;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                              BKORM = true;
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           Capture(row, col, rsel, csel); 
                           BKORM = true;
                        }
                        turn = !turn;
                  }
               }
            }
            
            else if (living[rsel][csel].getType() == 5)// white bishop
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 1 && NoPieceBetween(row, col, rsel, csel) &&( row!=bkr || col!=bkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() != 4)//////for white
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc) || BlockACheck(wkr, wkc, WhichPieceIsCheckingWhite(wkr, wkc).getRow(),  WhichPieceIsCheckingWhite(wkr, wkc).getCol(), row, col))/*:)*/
                     {
                        Capture(row, col, rsel, csel);
                        turn = !turn;
                     }
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() == 4)
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))/*:)*/
                     {
                        Capture(row, col, rsel, csel);
                        turn = !turn;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                           Capture(row, col, rsel, csel); 
                        }
                        turn = !turn;
                  } /////// for white
               }
            }
            
            else if (living[rsel][csel].getType() == 6)// black bishop
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 2 && NoPieceBetween(row, col, rsel, csel)&& (row!=wkr || col!=wkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  
                  if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() != 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc) || BlockACheck(bkr, bkc, WhichPieceIsCheckingBlack(bkr, bkc).getRow(),  WhichPieceIsCheckingBlack(bkr, bkc).getCol(), row, col))/*:)*/
                     {
                        Capture(row, col, rsel, csel); 
                        turn = !turn;
                     }
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() == 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc))/*:)*/
                     {
                        Capture(row, col, rsel, csel); 
                        turn = !turn;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           Capture(row, col, rsel, csel); 
                        }
                        turn = !turn;
                  }
               }
            }
            
            else if (living[rsel][csel].getType() == 10) // black queen
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 2 && NoPieceBetween(row, col, rsel, csel)&& (row!=wkr || col!=wkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  
                  if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() != 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc) || BlockACheck(bkr, bkc, WhichPieceIsCheckingBlack(bkr, bkc).getRow(),  WhichPieceIsCheckingBlack(bkr, bkc).getCol(), row, col))/*:)*/
                     {
                        Capture(row, col, rsel, csel); 
                        turn = !turn;
                     }
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() == 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc))/*:)*/
                     {
                        Capture(row, col, rsel, csel); 
                        turn = !turn;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           Capture(row, col, rsel, csel); 
                        }
                        turn = !turn;
                  }
               }
            }
            
            else if (living[rsel][csel].getType() == 9) // white queen
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 1 && NoPieceBetween(row, col, rsel, csel)&& (row!=bkr || col!=bkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  
                  if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() != 4)//////for white
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc) || BlockACheck(wkr, wkc, WhichPieceIsCheckingWhite(wkr, wkc).getRow(),  WhichPieceIsCheckingWhite(wkr, wkc).getCol(), row, col))/*:)*/
                     {
                        Capture(row, col, rsel, csel);
                        turn = !turn;
                     }
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() == 4)
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))/*:)*/
                     {
                        Capture(row, col, rsel, csel);
                        turn = !turn;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                     
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              turn = ! turn;
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              Capture(row, col, rsel, csel); 
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                           Capture(row, col, rsel, csel); 
                        }
                        turn = !turn;
                  } 
               }
            }
            
            else if (living[rsel][csel].getType() == 11) // white king
            {
               if ((row == 7 && col == 6 && !WKORM && !WhiteInCheck(wkr, wkc) && occupied[7][5] == 0 && occupied[7][6] == 0 && !WhiteInCheck(7, 6) && !WhiteInCheck(7, 5)) )
               {
                  Capture(row, col, rsel, csel); 
                     turn = !turn;
                     wkc = col;
                     wkr = row;
                     WKORM = true;
                  Capture(row, col-1, 7,7);
               }
               else if (row == 7 && col == 2 && !WKORM && !WhiteInCheck(wkr, wkc) && occupied[7][1] == 0 && occupied[7][2] == 0 && occupied[7][3] == 0 && !WhiteInCheck(7, 1) && !WhiteInCheck(7, 2) && !WhiteInCheck(7, 3))
               {
                  Capture(row, col, rsel, csel); 
                     turn = !turn;
                     wkc = col;
                     wkr = row;
                     WKORM = true;
                  Capture(row, col+1, 7,0);
               }
               else if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 1&& (row!=bkr || col!=bkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  if (WhiteInCheck(row, col) == false)
                  {
                     Capture(row, col, rsel, csel); 
                     turn = !turn;
                     wkc = col;
                     wkr = row;
                     WKORM = true;
                  }
               }
               
            }
            
            else                                       // black king
            {
               if ((row == 0 && col == 6 && !BKORM && !BlackInCheck(bkr, bkc) && occupied[0][5] == 0 && occupied[0][6] == 0 && !BlackInCheck(0, 6) && !BlackInCheck(0, 5)) )
               {
                  Capture(row, col, rsel, csel); 
                     turn = !turn;
                     bkc = col;
                     bkr = row;
                     BKORM = true;
                  Capture(row, col-1, 0,7);
               }
               else if (row == 0 && col == 2 && !BKORM && !BlackInCheck(bkr, bkc) && occupied[0][1] == 0 && occupied[0][2] == 0 && occupied[0][3] == 0 && !BlackInCheck(0, 1) && !BlackInCheck(0, 2) && !BlackInCheck(0, 3))
               {
                  Capture(row, col, rsel, csel); 
                     turn = !turn;
                     bkc = col;
                     bkr = row;
                     BKORM = true;
                  Capture(row, col+1, 0,0);
               }
               else if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 2&& (row!=wkr || col!=wkc))
               {
                  if (occupied[row][col] != 0)
                  {
                     captured = true;
                  }
                  else
                  {
                     captured = false;
                  }
                  if(BlackInCheck(row, col) == false)
                  {
                     Capture(row, col, rsel, csel); 
                     turn = !turn;
                     bkc = col;
                     bkr = row;
                     BKORM = true;
                  }
               }
               
            }
            
            ////////////////////////////////////////////////////////////////
            /* moving finsihed */
            //////////////////////////////////////////////////////////////////
            if (BlackInCheck(bkr, bkc))
            {
               if (Board[bkr][bkc].getBackground() != Color.red)
               previous2 = Board[bkr][bkc].getBackground();
               Board[bkr][bkc].setBackground(Color.red);
               bcheckr =bkr;
               bcheckc = bkc;
            }
            else
            {
               if (Board[bcheckr][bcheckc].getBackground() == Color.red)
               Board[bcheckr][bcheckc].setBackground(previous2);
            }
            
            if (WhiteInCheck(wkr, wkc))
            {
        
               if (Board[wkr][wkc].getBackground() != Color.red)
               previous3 = Board[wkr][wkc].getBackground();
               Board[wkr][wkc].setBackground(Color.red);
               wcheckr = wkr;
               wcheckc = wkc;
            }
            else 
            {
               if (Board[wcheckr][wcheckc].getBackground() == Color.red)
               Board[wcheckr][wcheckc].setBackground(previous3);
            }   
            
         }
      
         
         if (CheckOrStaleMate(turn) != 0)
         {
            for (int Tae = 0; Tae < 8; Tae ++)
            {
               for (int TAE = 0; TAE < 8; TAE ++)
               {
                  Board[Tae][TAE].setEnabled(false);
               }
            }
         }
         
         if (InsMat())
         {
            for (int Tae = 0; Tae < 8; Tae ++)
            {
               for (int TAE = 0; TAE < 8; TAE ++)
               {
                  Board[Tae][TAE].setEnabled(false);
               }
            }
         }
      }
   }  
   
   
   
   
   //////////////////////////////////////////
   //END OF ACTION LISTENER
   /////////////////////////////////////////
   
   
   
   
   public boolean BlackInCheck(int r, int c)
   {
   occupied[bkr][bkc] = 0;
      boolean check = false;
      for (int i = 0; i < 8; i++)
      {
         for (int q = 0; q < 8; q++)
         {
            if (occupied[i][q] == 1) // if piece is white
            {
               if (living[i][q].getType() == 1)
               {
                  if (living[i][q].canCapture(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  check = true;
               }
               else
               {
                  if (living[i][q].canMove(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  check = true;
               }
            }
         }
      }
      occupied[bkr][bkc] = 2;
      return check;
   }
   
   public boolean WhiteInCheck(int r, int c)
   {
      boolean check = false;
      occupied[wkr][wkc] = 0;
      for (int i = 0; i < 8; i++)
      {
         for (int q = 0; q < 8; q++)
         {
            if (occupied[i][q] == 2) // if piece is black
            {
               if (living[i][q].getType() == 2)
               {
                  if (living[i][q].canCapture(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  check = true;
               }
               else
               {
                  if (living[i][q].canMove(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  check = true;
               }
            }
         }
      }
      occupied[wkr][wkc] = 1;
      
      return check;
   }
   
   
   
   /////////////////////////////////////////////
   //no piece between
   
   public boolean NoPieceBetween(int r, int c, int rsel, int csel)
   {
      if (r == rsel) // horizontal 
      {
         if (c > csel + 1) //moves left
         {
            for (int i = csel+1; i < c; i++)
            {
               if (occupied[r][i] != 0)
               return false;
            }
         }
         else //move right
         {
            for (int i = csel-1; i > c; i--)
            {
               if (occupied[r][i] != 0)
               return false;
            }
         }
      }
      
      else if (c == csel) // vertical
      {
         if (r > rsel + 1) //moves up
         {
            for (int i = rsel+1; i < r; i++)
            {
               if (occupied[i][c] != 0)
               return false;
            }
         }
         else //move rdown
         {
            for (int i = rsel-1; i > r; i--)
            {
               if (occupied[i][c] != 0)
               return false;
            }
         }
      }
      
      else //diagonal
      {
         if (rsel+1 < r && csel -1 > c) // up  and right
         {
            for (int i = r-1; i > rsel; i --)
            {
               for (int p = c+1; p < csel; p++)
               {
                  if (Math.abs(p - c) == Math.abs(i - r) && occupied[i][p] != 0)
                  return false;
               }
            }
         }
         
         else if (rsel+1 < r  && csel + 1 < c) // up and left
         {
            for (int i = r-1; i > rsel; i --)
            {
               for (int p = c-1; p > csel; p--)
               {
                  if (Math.abs(p - csel) == Math.abs(i - rsel) && occupied[i][p] != 0)
                  return false;
               }
            }
         }
         
         else if (rsel-1 > r && csel -1 > c) // down and right
         {
            for (int i = r + 1; i < rsel; i++)
            {
               for (int p = c +1; p < csel; p++)
               {
                  if (Math.abs(p - csel) == Math.abs(i - rsel) && occupied[i][p] != 0)
                  return false;
               }
            }
         }
         
         else // down and left
         {
            for (int i = r+1; i < rsel; i++)
            {
               for (int p = c-1; p > csel; p--)
               {
                  if (Math.abs(p - csel) == Math.abs(i - rsel) && occupied[i][p] != 0)
                  return false;
               }
            }
         }
      }
      
      return true;
   }
   
   
     
   
   public void Capture(int row, int col, int rsel, int csel)
   {
      
      living[rsel][csel].setLocation(row, col);
      living[row][col] = living[rsel][csel];
      living[rsel][csel] = null;
      occupied[rsel][csel] = 0;
      occupied[row][col] = living[row][col].getColor();
      Board[row][col].setIcon(Board[rsel][csel].getIcon());
      Board[rsel][csel].setIcon(null);
   }
   
   
   
   
   
   ///////////////////////////////////////////////////
   // checking hiw many and which pieces are checking kings
   ///////////////////////////////////////////////////
   
   
   
   public int HowManyCheckingWhite(int r, int c)
   {
      int count = 0;
      for (int i = 0; i < 8; i++)
      {
         for (int q = 0; q < 8; q++)
         {
            if (occupied[i][q] == 2) // if piece is black
            {
               if (living[i][q].getType() == 2)
               {
                  if (living[i][q].canCapture(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  count ++;
               }
               else
               {
                  if (living[i][q].canMove(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  count++;
               }
            }
         }
      }
      return count;
   }
   
   public int HowManyCheckingBlack(int r, int c)
   {
      int count = 0;
      for (int i = 0; i < 8; i++)
      {
         for (int q = 0; q < 8; q++)
         {
            if (occupied[i][q] == 1) //if White
            {
               if (living[i][q].getType() == 1)
               {
                  if (living[i][q].canCapture(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  count ++;
               }
               else
               {
                  if (living[i][q].canMove(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  count++;
               }
            }
         }
      }
      return count;
   }
   
   
   public pieces WhichPieceIsCheckingWhite(int r, int c)
   {
      for (int i = 0; i < 8; i++)
      {
         for (int q = 0; q < 8; q++)
         {
            if (occupied[i][q] == 2) // if piece is black
            {
               if (living[i][q].getType() == 2)
               {
                  if (living[i][q].canCapture(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  return living[i][q];
               }
               else
               {
                  if (living[i][q].canMove(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  return living[i][q];
               }
            }
         }
      }
      return null;
   }
   
   public pieces WhichPieceIsCheckingBlack(int r, int c)
   {
      for (int i = 0; i < 8; i++)
      {
         for (int q = 0; q < 8; q++)
         {
            if (occupied[i][q] == 1) // if piece is white
            {
               if (living[i][q].getType() == 1)
               {
                  if (living[i][q].canCapture(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  return living[i][q];
               }
               else
               {
                  if (living[i][q].canMove(r, c) == true && NoPieceBetween(i, q, r, c) == true)
                  return living[i][q];
               }
            }
         }
      }
      return null;
   }
   
   
   
   
   
   
   
   
   
   //////////////////////////////////////////////////////////////////
   
   /////////////////////////////////////////////////////////////////
   
   
   
      public boolean BlockACheck(int r, int c, int rsel, int csel, int movedr, int movedc)
   {
      if (r == rsel && r == movedr) // horizontal 
      {
         if (c > csel + 1) //moves left
         {
            for (int i = csel+1; i < c; i++)
            {
               if (i == movedc)
               return true;
            }
         }
         else //move right
         {
            for (int i = csel-1; i > c; i--)
            {
               if (i == movedc)
               return true;
            }
         }
      }
      
      else if (c == csel && c == movedc) // vertical
      {
         if (r > rsel + 1) //moves up
         {
            for (int i = rsel+1; i < r; i++)
            {
               if (i == movedr)
               return true;
            }
         }
         else //move rdown
         {
            for (int i = rsel-1; i > r; i--)
            {
               if (i == movedr)
               return true;
            }
         }
      }
      
      else //diagonal
      {
         if (rsel+1 < r && csel -1 > c) // up  and right
         {
            for (int i = r-1; i > rsel; i --)
            {
               for (int p = c+1; p < csel; p++)
               {
                  if ((i == movedr && p == movedc) && Math.abs(movedc - csel) == Math.abs(movedr - rsel))
                  return true;
               }
            }
         }
         
         else if (rsel+1 < r  && csel + 1 < c) // up and left
         {
            for (int i = r-1; i > rsel; i --)
            {
               for (int p = c-1; p > csel; p--)
               {
                  if ((i == movedr && p == movedc) && Math.abs(movedc - csel) == Math.abs(movedr - rsel))
                  return true;
               }
            }
         }
         
         else if (rsel-1 > r && csel -1 > c) // down and right
         {
            for (int i = r + 1; i < rsel; i++)
            {
               for (int p = c +1; p < csel; p++)
               {
                  if ((i == movedr && p == movedc) && Math.abs(movedc - csel) == Math.abs(movedr - rsel))
                  return true;
               }
            }
         }
         
         else // down and left
         {
            for (int i = r+1; i < rsel; i++)
            {
               for (int p = c-1; p > csel; p--)
               {
                  if ((i == movedr && p == movedc) && Math.abs(movedc - csel) == Math.abs(movedr - rsel))
                  return true;
               }
            }
         }
      }
      
      return false;
   }
   
   
   
   
   
   
   ///////////////// ///////////////// ///////////////// ///////////////// ///////////////// /////////////////
   ///////////////// ///////////////// ///////////////// ///////////////// ///////////////// /////////////////
   ///////////////// ///////////////// ///////////////// ///////////////// ///////////////// /////////////////
   ///////////////// ///////////////// ///////////////// ///////////////// ///////////////// /////////////////
   ///////////////// ///////////////// ///////////////// ///////////////// ///////////////// /////////////////
   
   
   
   public int CheckOrStaleMate(boolean turn)
   {
      for (int rsel = 0; rsel < 8; rsel ++)
      {
         for (int csel = 0; csel < 8; csel ++)
         {
            for (int row = 0; row < 8; row ++)
            {
               for (int col = 0; col < 8; col ++)
               {
                  if(!turn) /// black checking
                  {
                     if (occupied[rsel][csel] == 2)//occupied
                     {///""?/
                        /////////////////start black pawns
                     if (living[rsel][csel].getType() == 2) // black pawns
            {
               if (occupied[row][col] == 1 && living[rsel][csel].canCapture(row, col) && NoPieceBetween(row, col, rsel, csel)&& (row!=wkr || col!=wkc))
               {

                  if (BIC && HowManyCheckingBlack(bkr, bkc) == 1)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc))
                     {
                        return 0;
                     }                    
                  }
                  else if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else
                  {
                        occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              return 0;
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           return 0;

                        }
                        
                  }
               } 
               else if (occupied[row][col] == 0 && living[rsel][csel].canMove(row, col) && NoPieceBetween(row, col, rsel, csel)&& (row!=wkr || col!=wkc))
               {
                  
                  
                  if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() != 3 && HowManyCheckingBlack(bkr, bkc) == 1)
                    {
                     if (BlockACheck(bkr, bkc, WhichPieceIsCheckingBlack(bkr, bkc).getRow(),  WhichPieceIsCheckingBlack(bkr, bkc).getCol(), row, col))
                     {
                        return 0;

                     }
                  }
                  else if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else if(!BIC)
                  {
                     occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              return 0;

                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           return 0;

                        }
                        
                  }
               }
            }
                        /////////////////////////////////end of black pawns
                        
                        /////////check black knights
                        else if (living[rsel][csel].getType() == 4) // black knight
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 2 && (row!=wkr || col!=wkc))
               {
                  
                  
                  
                  if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() != 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc) || BlockACheck(bkr, bkc, WhichPieceIsCheckingBlack(bkr, bkc).getRow(),  WhichPieceIsCheckingBlack(bkr, bkc).getCol(), row, col))/*:)*/
                     {
                        return 0;
                     }
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() == 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc))
                     {
                        return 0;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              return 0; 
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           return 0;
                        }
                        
                  }
               }
            }
                        /////////////// end of knights
                        
                        ///////////////black rook
                        else if (living[rsel][csel].getType() == 8)// black rook
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 2 && NoPieceBetween(row, col, rsel, csel)&& (row!=wkr || col!=wkc))
               {
                 
                  
                  if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() != 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc) || BlockACheck(bkr, bkc, WhichPieceIsCheckingBlack(bkr, bkc).getRow(),  WhichPieceIsCheckingBlack(bkr, bkc).getCol(), row, col))/*:)*/
                     {
                        return 0;
                     }
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() == 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc))/*:)*/
                     {
                        return 0;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                            
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                             return 0;
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           return 0;
                        }
                        
                  }
               }
            }

                        ///////////////end of black rook
                        /////////////////black bishop
                        else if (living[rsel][csel].getType() == 6)// black bishop
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 2 && NoPieceBetween(row, col, rsel, csel)&& (row!=wkr || col!=wkc))
               {
                  
                  
                  if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() != 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc) || BlockACheck(bkr, bkc, WhichPieceIsCheckingBlack(bkr, bkc).getRow(),  WhichPieceIsCheckingBlack(bkr, bkc).getCol(), row, col))/*:)*/
                     {
                        return 0;
                     }
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() == 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc))/*:)*/
                     {
                        return 0;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              return 0;
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           return 0;
                        }
                        
                  }
               }
            }
                        /////////////////end of bishops
                        
                        //////////////////black queen
                        else if (living[rsel][csel].getType() == 10) // black queen
           
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 2 && NoPieceBetween(row, col, rsel, csel)&& (row!=wkr || col!=wkc))
               {
                  
                  
                  if(BIC && HowManyCheckingBlack(bkr, bkc) == 2)
                  {
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() != 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc) || BlockACheck(bkr, bkc, WhichPieceIsCheckingBlack(bkr, bkc).getRow(),  WhichPieceIsCheckingBlack(bkr, bkc).getCol(), row, col))/*:)*/
                     {
                        return 0;
                     }
                  }
                  else if (BIC && WhichPieceIsCheckingBlack(bkr, bkc).getType() == 3)
                  {
                     if (living[row][col] == WhichPieceIsCheckingBlack(bkr, bkc))/*:)*/
                     {
                        return 0;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (BlackInCheck(bkr, bkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 2;
                           
                           if(BlackInCheck(bkr, bkc))
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 2;
                              occupied[row][col] = hold;
                              return 0;
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 2;
                           return 0;
                        }
                       
                  }
               }
            
            
                        ////////////////////end of queen
                        
                        ///////////////////king
            else 
            {
               if (occupied[row][col] != 2 && BlackInCheck(row, col) == false && living[bkr][bkc].canMove(row, col))
               return 0;
            }
                        ////////////////////end king
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        
                       } 
                        
                     }///////////////////end of black checking
                  
                  
                  
                  else //start of white checking
                  {
                  if (occupied[rsel][csel] ==1)
                  {
                     ///////////////////////////white pawns
                     if (living[rsel][csel].getType() == 1) // White Pawns
            {
 if (occupied[row][col] == 2 && living[rsel][csel].canCapture(row, col) && NoPieceBetween(row, col, rsel, csel) && (row!=bkr || col!=bkc))
               {
                 
                  
                  if (WIC && HowManyCheckingWhite(wkr, wkc) == 1)
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))
                     {
                        return 0;
                     }
                  }
                  else if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  else
                  {
                     
                        occupied[rsel][csel] = 0;
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              return 0; 
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                          return 0;
                        }
                      
                     
                  }
               }
               else if (occupied[row][col] == 0 && living[rsel][csel].canMove(row, col))
               {
                  
                  
                  if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() != 4 && HowManyCheckingWhite(wkr, wkc) == 1)
                    {
                     if (BlockACheck(wkr, wkc, WhichPieceIsCheckingWhite(wkr, wkc).getRow(),  WhichPieceIsCheckingWhite(wkr, wkc).getCol(), row, col))
                     {
                        if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))
                        {
                           return 0;
                        }
                     }
                  }
                  
                  else if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  else if(!WIC)
                  {
                     
                        occupied[rsel][csel] = 0;
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              return 0;
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                           return 0;
                        }
                                            
                  }
               } 
            }


                     ///////////////////////////end ofwhite pawns
                     
                     //////////////////////////white knight
                      else if (living[rsel][csel].getType() == 3) // white knight
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 1 && (row!=bkr || col!=bkc))
               {
                  
                  
                  if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() != 4)//////for white
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc) || BlockACheck(wkr, wkc, WhichPieceIsCheckingWhite(wkr, wkc).getRow(),  WhichPieceIsCheckingWhite(wkr, wkc).getCol(), row, col))
                     {
                        return 0; 
                     }
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() == 4)
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))
                     {
                        return 0; 
                     }
                  }
                  else
                  {
                        occupied[rsel][csel] = 0;
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              return 0; 
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                           return 0; 
                        }
                     
                  } /////// for white
               }
            }

                     //////////////////////////end of white knight
                     
                     //////////////////////////white bishop
                     else if (living[rsel][csel].getType() == 5)// white bishop
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 1 && NoPieceBetween(row, col, rsel, csel) &&( row!=bkr || col!=bkc))
               {
                  
                  if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() != 4)//////for white
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc) || BlockACheck(wkr, wkc, WhichPieceIsCheckingWhite(wkr, wkc).getRow(),  WhichPieceIsCheckingWhite(wkr, wkc).getCol(), row, col))
                     {
                       return 0;
                     }
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() == 4)
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))
                     {
                        return 0;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                                                       }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              return 0;
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                           return 0;
                        }
                      
                  } /////// for white
               }
            }

                     ////////////////////////// end white bishop
                     
                     //////////////////////////white rook
                     else if (living[rsel][csel].getType() == 7)// white rook
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 1 && NoPieceBetween(row, col, rsel, csel) && (row!=bkr || col!=bkc))
               {
                  
                  if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() != 4)//////for white
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc) || BlockACheck(wkr, wkc, WhichPieceIsCheckingWhite(wkr, wkc).getRow(),  WhichPieceIsCheckingWhite(wkr, wkc).getCol(), row, col))
                     {
                        return 0;
                     }
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() == 4)
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))
                     {
                          return 0;

                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                                return 0;

                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                             return 0;
 
                        }
                      
                  } /////// for white
               }
            }

                   ////////////////////////// end white rook
                   
                   //////////////////////////white wueen 
                   
            else if (living[rsel][csel].getType() == 9) // white queen
            {
               if (living[rsel][csel].canMove(row, col) && occupied[row][col] != 1 && NoPieceBetween(row, col, rsel, csel)&& (row!=bkr || col!=bkc))
               {
                  
                  if (WIC && HowManyCheckingWhite(wkr, wkc) == 2)
                  {
                  }
                  
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() != 4)//////for white
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc) || BlockACheck(wkr, wkc, WhichPieceIsCheckingWhite(wkr, wkc).getRow(),  WhichPieceIsCheckingWhite(wkr, wkc).getCol(), row, col))
                     {
                        return 0;
                     }
                  }
                  else if (WIC && WhichPieceIsCheckingWhite(wkr, wkc).getType() == 4)
                  {
                     if (living[row][col] == WhichPieceIsCheckingWhite(wkr, wkc))
                     {
                         return 0;
                     }
                  }
                  else
                  {
                     occupied[rsel][csel] = 0;
                     
                        if (WhiteInCheck(wkr, wkc))
                        {
                           int hold = occupied[row][col];
                           occupied[row][col] = 1;
                           
                           if(WhiteInCheck(wkr, wkc))
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                              
                           }
                           
                           else 
                           {
                              occupied[rsel][csel] = 1;
                              occupied[row][col] = hold;
                               return 0;
                           }
                        }
                        else
                        {
                           occupied[rsel][csel] = 1;
                            return 0;
                        }
                    
                  } 
               }
            }

                   ///////////////////////////end white queen
                   
                   ///////////////////////white king
                   else 
            {
               if (occupied[row][col] != 1 && WhiteInCheck(row, col) == false && living[wkr][wkc].canMove(row, col))
               return 0;
            }
                   //////////////////////end white king
                  }
                  }//////////////end of white    
                  
                  
               }
            }
         }
      }/////whoops
      if (!turn)
      return 1;
      else if (turn) // black in checkmate
      return 2;
      else ///stalemate
      return 3;
   }
   
   
   public boolean InsMat()
   {int white = 0;
   int black = 0;
      for (int r = 0; r < 8; r ++)
            {
               for (int c = 0; c < 8; c ++)
               {
                 if (occupied[r][c] == 1 && living[r][c].getType() != 1 && living[r][c].getType() != 7 && living[r][c].getType() != 9 && living[r][c].getType() != 11)
                 {
                  white = white + living[r][c].getType();
                 }
                 else if (occupied[r][c] == 2 && living[r][c].getType() != 2 && living[r][c].getType() != 8 && living[r][c].getType() != 10 && living[r][c].getType() != 12)
                 {
                  black = black + living[r][c].getType();

                 }
               }
            }
            if ((white == 3 || white == 5) && (black == 4 && black == 6))
            return true;
      return false;
   }
   
  /* public boolean WIM ()
   {
      int
      for (int i = 0; i<8; i++)
      {
         for (int p = 0; p<8; p++)
         {
            if (occupied[i][p] == 1)
            {
               if (living[i][p].getType() == 7 || living[i][p].getType() == 9 || living[i][p].getType() == 1)
               return false;
               if (living[i][p].getType() == 3 || living[i][p].getType() == 5)
               {
                  count ++;
               }
            }
         }
      }
   }*/
   private class Exit implements ActionListener
   {    
      public void actionPerformed(ActionEvent click)
      {
         JOptionPane.showMessageDialog( new JOptionPane(),
         "On my honor as a Woodson High School Student,\n"
         + "I, TAE certify that I have neither given \n"
         + "nor received unauthorized aid on this assignment, \n"
         + "that I have cited my sources for authorized aid, and \n"
         + "that this project was created on or after May 10, 2017.", "Honor Code",
         JOptionPane.INFORMATION_MESSAGE);
         System.exit(0);

      }
   }
}     







//////////////////////////////////////////////////////////////////




/* piece ID
White Pawn 1
Black Pawn 2
White Knight 3
Black knight 4
White Bishop 5
Black Bishop 6
White Rook 7
Black Rook 8
White Queen 9
Black Queen 10
White King 11
Black King 12
*/


