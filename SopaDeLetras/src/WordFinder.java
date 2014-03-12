
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * WordFinder
 * 
 * @author afs
 * @version 2013
 */

final class WordFinder implements Callable<File> {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final Index MARK = new Index(-1, -1);

    private static int nextId = 0;



    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    private final String id = nextId();
    private final Dictionary dictionary;
    private final char[][] board;
    private final Index start;
    private final Index end;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class WordFinder.
     */
    WordFinder(Dictionary dict, char[][] board, Index from, Index to) {
        this.dictionary = dict;
        this.board      = board;
        this.start      = from;
        this.end        = to;
    }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    @Override
    public File call() throws IOException {
        Set<Result> results = new HashSet<>();
        for (int i = start.row; i < end.row; ++i) {
            for (int j = start.column; j < end.column; ++j) {
                search(i, j, results);
            }
        }
        return persist(results);
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */
    private void search(int row, int col, Set<Result> results) {
        StringBuilder sb = new StringBuilder();
        Set<Index> discovered = new HashSet<>();
        Deque<Index> stack = new ArrayDeque<>();
        Deque<Index> path = new ArrayDeque<>();
        stack.addFirst(new Index(row, col));
        while (!stack.isEmpty()) {
            Index index = stack.removeFirst();
            assert sb.length() == discovered.size();
            if (index == MARK) {
                // Backtrack
                discovered.remove(path.removeFirst());
                sb.setLength(sb.length() - 1);
                continue;
            }
            stack.addFirst(MARK);
            discovered.add(index);
            path.addFirst(index);
            sb.append(board[index.row][index.column]);
            String word = sb.toString();
            boolean found = false;
            if (found = dictionary.contains(word)) {
                results.add(toResult(word, path));
            }
            if (found || dictionary.matchesPrefix(word)) {
                for (int i = index.row - 1; i <= index.row + 1; ++i) {
                    if (i < 0 || i >= board.length) continue;
                    for (int j = index.column - 1; j <= index.column + 1; ++j) {
                        if (j < 0 || j >= board[i].length) continue;
                        if (i == index.row && j == index.column) continue;
                        Index next = new Index(i, j);
                        if (!discovered.contains(next)) {
                            stack.addFirst(next);
                        }
                    }
                }
            }
        }
    }


    /** */
    private Result toResult(String word, Deque<Index> path) {
        List<Index> list = new ArrayList<>(path.size());
        Iterator<Index> i = path.descendingIterator();
        while(i.hasNext()) { list.add(i.next()); }
        return new Result(word, list);
    }


    /** */
    private File persist(Set<Result> results) throws IOException {
        String filename = "files/" + id + ".txt";
        try (PrintWriter out = new PrintWriter
                (new FileOutputStream(filename))) {
            for (Result r: results) {
                out.println(r.toString());
            }
        }
        return new File(filename);
    }


    /** */
    private static String nextId() {
        return "wordfinder_" + (++nextId);
    }
}
