public class queen implements pieces{

   private int row, col, color, type;

   public queen(int r, int c, int t, int q){
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
      else if (c == getCol() || r == getRow()) return true;
      else if (Math.abs(c - getCol()) ==  Math.abs(r - getRow())) return true;
      else return false;
   }
}