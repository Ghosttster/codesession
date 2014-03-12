
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Dictionary
 * 
 * @author afs
 * @version 2013
 */

final class Dictionary {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    private final SortedSet<String> dictionary = new TreeSet<>();

    private final ConcurrentMap<String, Boolean> cache =
        new ConcurrentHashMap<>();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class Dictionary.
     */
    Dictionary() {}



    /*************************************************************************\
     *  Predicates
    \*************************************************************************/

    /** */
    boolean matchesPrefix(String prefix) {
        // long t0 = System.nanoTime();
        Boolean cached = cache.get(prefix);
        if (cached != null) {
            // System.out.println("prefix: " + (System.nanoTime() - t0));
            return cached.booleanValue();
        }
        Set<String> tailSet = dictionary.tailSet(prefix);
        for (String tail: tailSet) {
            if (tail.startsWith(prefix)) {
                // System.out.println("prefix: " + (System.nanoTime() - t0));
                cache.putIfAbsent(prefix, Boolean.TRUE);
                return true;
            }
        }
        // System.out.println("prefix: " + (System.nanoTime() - t0));
        cache.putIfAbsent(prefix, Boolean.FALSE);
        return false;
    }


    /** */
    boolean contains(String word) {
        // long t0 = System.nanoTime();
        boolean r = dictionary.contains(word);
        // System.out.println("contains: " + (System.nanoTime() - t0));
        return r;
    }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    void add(String word) { dictionary.add(word); }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */



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
        Dictionary n = (Dictionary) o;
        return dictionary.equals(n.dictionary);
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
        return dictionary.hashCode();
    }

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return dictionary.toString();
    }
}
