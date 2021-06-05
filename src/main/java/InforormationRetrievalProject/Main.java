/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InforormationRetrievalProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author m18coppola
 */
public class Main {

    static final boolean SMALL_DATA = false; //change for debugging

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        File dir = new File("data/");
        File[] directoryListing = dir.listFiles();
        
        ArrayList<Document> docs = new ArrayList<Document>();
        if (!SMALL_DATA) {
            for (File file : directoryListing) {
                docs.addAll(Parser.parseTREC(file));
            }
        } else {
            docs.addAll(Parser.parseTREC(directoryListing[0]));
        }

        System.out.println(docs.size() + " documents loaded.");

        InvertedIndex myIndex = new InvertedIndex(false);
        for (Document d : docs) {
            myIndex.addDoc(d);
        }

        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter a search term or \"q\" to exit: ");
        String in = userInput.nextLine();
        while (!in.equals("q")) {
            Query q = new Query(in);
            PostingList p = q.search(myIndex);
            if (p != null) {
                System.out.println((float) p.size() / (float) docs.size() * 100.0f + "% of documents matched (" + p.size() + " docs)");
            } else {
                System.out.println("No matches");
            }
            System.out.print("Enter a search term or \"q\" to exit: ");
            in = userInput.nextLine();
        }
        userInput.close();

        System.out.println("Done.");
    }

}
