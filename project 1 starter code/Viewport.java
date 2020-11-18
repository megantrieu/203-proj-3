/*
Viewport ideally helps control what part of the world we are looking at for drawing only what we see
Includes helpful helper functions to map between the viewport and the real world
 */


final class Viewport
{
   private int row;
   private int col;
   private final int numRows;
   private final int numCols;

   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
   }

    public int getNumCols() {
        return this.numCols;
    }

    public int getNumRows() {
        return this.numRows;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public void shift(int col, int row)
    {
       this.col = col;
       this.row = row;
    }


    public boolean contains(Point p)
    {
       return p.y >= this.row && p.y < this.row + this.numRows &&
               p.x >= this.col && p.x < this.col + this.numCols;
    }

    public Point viewportToWorld(int col, int row)
    {
       return new Point(col + this.col, row + this.row);
    }

    public Point worldToViewport(int col, int row)
    {
       return new Point(col - this.col, row - this.row);
    }
}
