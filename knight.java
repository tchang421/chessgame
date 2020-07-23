public class knight implements pieces{

   private int row, col, color, type;

   public knight(int r, int c, int t, int q){
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
      if (r == getRow() && c == getCol()) return false;
      if (Math.abs(getRow() - r) == 2 && Math.abs(getCol() - c) == 1 || 
         (Math.abs(getRow() - r) == 1 && Math.abs(getCol() - c) == 2))
         return true;
      return false;
   }
   
   public boolean canMove(int r, int c){
      if (r == getRow() && c == getCol()) return false;
      if (Math.abs(getRow() - r) == 2 && Math.abs(getCol() - c) == 1 || 
         (Math.abs(getRow() - r) == 1 && Math.abs(getCol() - c) == 2))
         return true;
      return false;
   }
}