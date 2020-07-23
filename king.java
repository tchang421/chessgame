public class king implements pieces{

   private int row, col, color, type;

   public king(int r, int c, int t, int q){
      row = r;
      col = c;
      color = t;
      type = q;
   }

   public void  setLocation(int r, int c){
      row = r;
      col = c;
   }
   
   public int getType(){
      return type;
   }
   
   public int getColor(){
      return color;
   }
   
   public int getRow(){
      return row;
   }
   
   public int getCol(){
      return col;
   }
   
   public boolean canCapture(int r, int c){
      return canMove(r, c);
   }
   
   public boolean canMove(int r, int c)
   {
      if (r == getRow() && c == getCol()) return false;
      if ((c == getCol() && Math.abs(r - getRow())==1)|| (r == getRow() && Math.abs(c - getCol())==1))
         return true;
      else if (Math.abs(c - getCol()) ==  Math.abs(r - getRow()) && Math.abs(r - getRow())==1)
         return true;
      else return false;
   }
}