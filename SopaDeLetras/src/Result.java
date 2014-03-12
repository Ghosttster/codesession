
import java.util.ArrayList;
import java.util.List;

/**
 * Result
 * 
 * @author afs
 * @version 2013
 */

final class Result implements Comparable<Result> {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    final String word;
    private final List<Index> path;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class Result.
     */
    Result(String word, List<Index> path) {
        this.word   = word;
        this.path   = path;
    }



    /*************************************************************************\
     *  Methods
    \*************************************************************************/

    /** */
    static Result parseResult(String s) {
        String[] tokens = s.split("[ ]+");
        List<Index> list = new ArrayList<>(tokens.length - 1);
        tokens[1] = tokens[1].substring(1);
        tokens[tokens.length - 1] = tokens[tokens.length - 1]
            .substring(0, tokens[tokens.length - 1].length() - 1);
        for (int i = 1; i < tokens.length; ++i) {
            list.add(Index.parseIndex(tokens[i]));
        }
        return new Result(tokens[0], list);
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
        Result n = (Result) o;
        return word.equals(n.word);
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
        return word.hashCode();
    }

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(word);
        sb.append(' ');
        sb.append('[');
        int i = 0, size = path.size();
        for (Index index: path) {
            sb.append(index.toString());
            if (++i < size) {
                sb.append(' ');
            }
        }
        sb.append(']');
        return sb.toString();
    }


    /**
     *  Returns a string representation of the object.
     */
    public String toString(int tab) {
        StringBuilder sb = new StringBuilder(word);
        for (int i = word.length(); i < tab; ++i) {
            sb.append(' ');
        }
        sb.append('[');
        int i = 0, size = path.size();
        for (Index index: path) {
            sb.append(index.toString());
            if (++i < size) {
                sb.append(' ');
            }
        }
        sb.append(']');
        return sb.toString();
    }


    /** */
    @Override
    public int compareTo(Result o) {
        return word.compareTo(o.word);
    }
}
