
/**
 * Index
 * 
 * @author afs
 * @version 2013
 */

final class Index implements Comparable<Index> {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    final int row;
    final int column;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class Index.
     */
    Index(int row, int column) {
        this.row = row;
        this.column = column;
    }



    /*************************************************************************\
     *  Methods
    \*************************************************************************/

    /** */
    static Index parseIndex(String s) {
        s = s.substring(1, s.length() - 1);
        String[] tokens = s.split(",");
        return new Index(Integer.parseInt(tokens[0]),
            Integer.parseInt(tokens[1]));
    }



    /*************************************************************************\
     *  Equals, HashCode, ToString & Clone
    \*************************************************************************/

    /**
     *  Equivalence relation.
     *  Contract (for any non-null reference values x, y, and z):
     *      Reflexive: x.equals(x).
     *      Symmetric: x.equals(y) iff y.equals(x).
     *      Transitive: if x.equals(y) and y.equals(z), then x.equals(z).
     *      Consistency: successive calls return the same result,
     *          assuming no modification of the equality fields.
     *      x.equals(null) should return false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || this.getClass() != o.getClass()) { return false; }
        Index n = (Index) o;
        return row == n.row && column == n.column;
    }

    /**
     *  Contract:
     *      Consistency: successive calls return the same code,
     *          assuming no modification of the equality fields.
     *      Function: two equal objects have the same (unique) hash code.
     *      (Optional) Injection: unequal objects have different hash codes.
     *
     *  Common practices:
     *      boolean: calculate (f ? 0 : 1);
     *      byte, char, short or int: calculate (int) f;
     *      long: calculate (int) (f ^ (f >>> 32));
     *      float: calculate Float.floatToIntBits(f);
     *      double: calculate Double.doubleToLongBits(f)
     *          and handle the return value like every long value;
     *      Object: use (f == null ? 0 : f.hashCode());
     *      Array: recursion and combine the values.
     *
     *  Formula:
     *      hash = prime * hash + codeForField
     */
    @Override
    public int hashCode() {
        return 37 * row + column;
    }

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return new StringBuilder()
            .append('[')
            .append(row)
            .append(',')
            .append(column)
            .append(']')
            .toString();
    }

    /** */
    @Override
    public int compareTo(Index o) {
        if (row > o.row) return 1;
        if (row < o.row) return -1;
        if (column > o.column) return 1;
        if (column < o.column) return -1;
        return 0;
    }
}
