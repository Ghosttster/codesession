
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Solver
 * 
 * @author afs
 * @version 2013
 */

public final class Solver {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    private char[][] board;
    private final Dictionary dictionary = new Dictionary();
    private int maxWordLength = 0;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class Solver.
     */
    private Solver() {}



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static void main(String[] args) {
        int start = 0, end = 100;
        /*if (args.length > 0) {
            start = Integer.parseInt(args[0]);
            end = Integer.parseInt(args[1]);
        }*/
        Solver solver = new Solver();
        try {
            solver.readDictionary("files/dicio.txt");
            solver.readBoard("files/sopa.txt");
        } catch (FileNotFoundException ex) {
            System.err.println("File not found.");
            return;
        } catch (IOException ex) {
            System.err.println("Error while reading file.");
            return;
        }
        try {
            solver.solve("files/words.txt", start, end);
        } catch (IOException ex) {
            System.err.println("Error while writing output.");
            return;
        } catch (InterruptedException | ExecutionException ex) {
            System.err.println("Error processing results.");
            return;
        }
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */
    private void solve(String outFile, int startAt, int endAt)
            throws IOException, InterruptedException, ExecutionException {
        Set<Result> set = new TreeSet<>();
        ExecutorService exe = Executors.newFixedThreadPool
            (Runtime.getRuntime().availableProcessors());
        CompletionService<File> queue = new ExecutorCompletionService<>(exe);
        List<WordFinder> solvers = map(startAt, endAt);
        for (WordFinder wf: solvers) {
            queue.submit(wf);
        }
        try {
            for (int n = solvers.size(), i = 0; i < n; ++i) {
                File found = queue.take().get();
                Set<Result> rs = readResults(found);
                System.out.println("Worker found " + rs.size() + " results.");
                for (Result r: rs) {
                    set.add(r);
                    maxWordLength = Math.max(maxWordLength, r.word.length());
                }
            }
            persist(set, outFile, maxWordLength + 2);
            // System.out.println("Found total of " + set.size() + " results.");
            // for (Result r: set) System.out.println(r.toString(maxWordLength + 2));
        } finally {
            exe.shutdownNow();
        }
    }


    /** */
    private List<WordFinder> map(int startAt, int endAt) {
        if (endAt < 0) endAt = board.length;
        else endAt = Math.min(endAt, board.length);
        List<WordFinder> list = new ArrayList<>(board.length);
        for (int i = startAt; i < endAt; ++i) {
            list.add(new WordFinder(dictionary, board, new Index(i, 0),
                new Index(i + 1, board[i].length)));
        }
        return list;
    }


    /** */
    private void readDictionary(String path)
            throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String word = null;
            while((word = br.readLine()) != null) {
                dictionary.add(word.toUpperCase());
            }
        }
    }


    /** */
    private void readBoard(String path)
            throws FileNotFoundException, IOException {
        List<char[]> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = null;
            while((line = br.readLine()) != null) {
                lines.add(line.toCharArray());
            }
        }
        board = lines.toArray(new char[lines.size()][]);
    }


    /** */
    private Set<Result> readResults(File file) throws IOException {
        Set<Result> results = new HashSet<>();
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = in.readLine()) != null) {
                results.add(Result.parseResult(line));
            }
        }
        return results;
    }


    /** */
    private void persist(Set<Result> results, String path, int tab)
            throws IOException {
        try (PrintWriter out = new PrintWriter
                (new FileOutputStream(path))) {
            for (Result r: results) {
                out.println(r.toString(tab));
            }
        }
    }
}
