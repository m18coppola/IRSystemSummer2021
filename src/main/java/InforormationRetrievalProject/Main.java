/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InforormationRetrievalProject;

import InforormationRetrievalProject.PostingList.Post;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author m18coppola
 */
public class Main {

    static final boolean SMALL_DATA = false; //change for debugging
    static final String filepath1 = "/Users/m18coppola/Documents/NetBeansProjects/InforormationRetrievalProject/index.bin";
    static final String filepath2 = "/Users/m18coppola/Documents/NetBeansProjects/InforormationRetrievalProject/fullindex.bin";

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
            docs.addAll(Parser.parseTREC(directoryListing[1]));
            docs.addAll(Parser.parseTREC(directoryListing[2]));
        }
        System.out.println(docs.size() + " documents loaded.");
        
        Instant start = Instant.now();
        InvertedIndex myIndex = new InvertedIndex(false);
        for (Document d : docs) {
            myIndex.addDoc(d);
        }
        myIndex.updateTFs(docs.size());
        
        InvertedIndex fullIndex = new InvertedIndex(true);
        for (Document d : docs) {
            fullIndex.addDoc(d);
        }
        Instant end = Instant.now();
        
        long time = Duration.between(start, end).toSeconds();
        System.out.println("Indexed in " + time + " seconds.");

        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter a search term or \"q\" to exit: ");
        String in = userInput.nextLine();
        while (!in.equals("q")) {
            Query q = new Query(in);
            PostingList p = q.vectorSpaceSearch(myIndex, fullIndex);
            if (p != null) {
                System.out.println((float) p.size() / (float) docs.size() * 100.0f + "% of documents matched (" + p.size() + " docs)");
            } else {
                System.out.println("No matches");
            }
            
            PriorityQueue<Post> pq = new PriorityQueue<Post>(new Comparator<Post>(){
                @Override
                public int compare(Post arg0, Post arg1) {
                    return (arg0.cosineSim < arg1.cosineSim) ? 1 : -1;
                }  
            });
            
            
            Post docsWithCS = null;
            Iterator iter = p.treemap.entrySet().iterator();
            if (iter.hasNext()) {
                docsWithCS = (iter.hasNext()) ? (Post)((Map.Entry)(iter.next())).getValue() : null;
            }
            
            
            while (docsWithCS != null) {
                pq.add(docsWithCS);
                docsWithCS = (iter.hasNext()) ? (Post)((Map.Entry)(iter.next())).getValue() : null;
            }
            
            int i = 1;
            Post post = pq.poll();
            while (post != null && i <= 1000) {
                System.out.println("0 1 " + docs.get(post.docID - 1).docNo + " " + i + " " + post.cosineSim + " MichaelAndLauren");
                i++;
                post = pq.poll();
            }
            
            System.out.print("Enter a search term or \"q\" to exit: ");
            in = userInput.nextLine();
        }
        userInput.close();

        System.out.println("Done.");
    }

}
