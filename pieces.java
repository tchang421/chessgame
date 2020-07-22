public interface pieces 
{
   public abstract boolean canMove(int r, int c);
   public abstract boolean canCapture(int r, int c);
   public abstract void setLocation(int r, int c);
   public abstract int getColor();
   public abstract int getType();
   public abstract int getRow();
   public abstract int getCol();
}