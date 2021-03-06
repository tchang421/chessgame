public class pawn implements pieces{

   private int row, col, color, type;

   public pawn(int r, int c, int t, int q){
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

      if (color == 2){
         if ((c == getCol() + 1 || c == getCol() - 1) && r == getRow() + 1)
            return true;
      }
      else{
         if ((c == getCol() + 1 || c == getCol() - 1) && r == getRow() - 1)
            return true;
      }
      return false;
   }
   
   public boolean canMove(int r, int c){
      if (r == getRow() && c == getCol()) return false;

      if (color == 2){
         if (getRow() == 1)
         {
            if (c == getCol() && (r == (getRow()+1) || r == (getRow()+2))) 
               return true;
            else return false;
         }
         else
            if (c == getCol() && r == (getRow()+1)) return true;
      }
      else{
         if (getRow() == 6){
            if (c == getCol() && (r == (getRow()-1) || r == (getRow()-2)))
               return true;
            else return false;
         }
         else
            if (c == getCol() && r == (getRow()-1)) return true;
      }
      return false;
   }
}