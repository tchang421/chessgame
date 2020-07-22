public class Wbishop implements pieces
{
   private int row, col, color, type;
   public Wbishop(int r, int c, int t, int q)
   {
      row = r;
      col = c;
      color = t;
      type = q;
   }
   
   public void  setLocation(int r, int c)
   {
      row = r;
      col = c;
   }
   
   public int getType()
   {
      return type;
   }
   
   public int getColor()
   {
      return color;
   }
   
   public int getRow()
   {
      return row;
   }
   
   public int getCol()
   {
      return col;
   }
   
   public boolean canCapture(int r, int c)
   {
      if (r == getRow() && c == getCol())
      return false;
      if 
      (Math.abs(c - getCol()) ==  Math.abs(r - getRow()))
      return true;
      else return false;
   }
   
   
   
   public boolean canMove(int r, int c)
   {
      if (r == getRow() && c == getCol())
      return false;
      if 
      (Math.abs(c - getCol()) ==  Math.abs(r - getRow()))
      return true;
      else return false;
   }
}