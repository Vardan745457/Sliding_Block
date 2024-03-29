import java.lang.Math;

public class coordinates {
    public final static coordinates
            UP = new coordinates(-1,0),
            DOWN = new coordinates(1,0),
            LEFT = new coordinates(0,-1),
            RIGHT = new coordinates(0,1);

    public final static coordinates[] DIRS = { UP, DOWN, LEFT, RIGHT };
    private int row; // represents the row at which something starts
    private int col; // represents the column at which something starts

    /**
     * default constructor for a given coordinate thats being created
     * @param r represents a row in the board of blocks
     * @param c represents a column in the board of blocks
     */
   public coordinates(int r , int c){
       this.row = r;
       this.col = c;

    }

    /**
     * second constructor of coordinates for a given coordinate thats being created
     * @param other this represents another coordinate
     */
    public coordinates( coordinates other){
        this.row = other.row;
        this.col = other.col;
    }

    /**
     * takes other coordinates and this coordinates and adds them and returns new coordinates ,notice this and other
     * remain unchanged
     * @param obj is the other object , must be of type coordinate for the method to behave correctly
     * @return the sum of the rows and columns in a newly constructed pair of coordinates
     * @throws InvalidTypeException if it receives anything besides a type coordinate, sum is invalid
     */
    public coordinates addCoordinates(Object obj) throws Exception {
        if ( this.getClass() != obj.getClass()) {
          throw new Exception("Class Error!");
        }

        coordinates cord = (coordinates) obj;
        coordinates result = new coordinates( this.row + cord.row , this.col + cord.col);
        
        return result;
    }

    /**
     *  a helper method to check if given size of coordinates is not 0 by 0 or less ( size cant be negative)
     * @return true if given size is valid , false otherwise
     */
    public boolean isValidSize(){
        return (this.col >= 1 && this.row >= 1);
    }

    /**
     * manhattan distance is calculated by adding the minimum amount of rows and columns distance from the
     * two coordinates
     * @param lhs the source coordinates or the start
     * @param rhs the dest coordinates or the end
     * @return the sum of the difference of the rows and the difference of columns of the two coordinates
     */
    int manhattanDist(coordinates lhs , coordinates rhs){
        int result = Math.abs(lhs.row - rhs.row) + Math.abs(lhs.col - rhs.col);

        // System.out.println("lhs.row = " + lhs.row + " ,rhs.row = " + rhs.row);
        // System.out.println("lhs.col = " + lhs.col + " ,rhs.col = " + rhs.col);

        return result;
    } 

    /**
     * checks if object is correct type , then checks if rows and columns and hashcode match
     * @param obj the other object that should be another pair of coordinates
     * @return ture if all criteria match ,false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if ( obj == null || this.getClass() != obj.getClass() ) return false;

        coordinates cord = (coordinates) obj;
        return (this.row == cord.row && this.col == cord.col && this.hashCode() == cord.hashCode()) ;
   }

    /**
     * generates hashcode based on row and column for a coordinate
     * @return hashcode
     */
   @Override public int hashCode(){
      return (int) Math.abs(Math.pow(2, row) - Math.pow(3, col));
   }

   /**
    * simply gives a string with row and column
    * @return printable or readable or storable coordinate information
    */
   @Override
   public String toString(){
        //return "Row: " + row + ", Col: " + col ;
        return "( " + row + " , " +  col + ")";
   }
    /**
     * setter for the row of the coordinate
     * @param row set row for this coordinate to the given row
     */
    public void setRow(int row) {
        this.row = row;
    }
    /**
     * setter for the column of the coordinate
     * @param col set column for this coordinate to the given row
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * checks if position has both non-negative row and column
     * @return true if conditions are met , false otherwise
     */
    public boolean isValidPosition(){
        return (this.col >= 0 && this.row >= 0);
    }

    /**
     * getter for row
     * @return only the row as an integer
     */
    public int getRow(){
        return row;
    }

    /**
     * getter for column
     * @return only the column for this coordinate
     */
    public int getCol(){
        return col;
    }
}