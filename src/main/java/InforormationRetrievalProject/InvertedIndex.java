/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InforormationRetrievalProject;

import java.util.HashMap;

/**
 *
 * @author m18coppola
 */
public class InvertedIndex extends HashMap<String, PostingList>{
    boolean containsStopWords;
    
    public InvertedIndex(boolean containsStopWords) {
        this.containsStopWords = containsStopWords;
    }
    
    public void addDoc(Document d) {
        TokenizerList tokens;
        if (containsStopWords) {
            tokens = new TokenizerList(d.getText(), true);
        } else {
            tokens = new TokenizerList(d.getText(), false);
        }
        
        for (String token: tokens) {
            if (!this.containsKey(token)) {
                PostingList pl = new PostingList();
                pl.insert(d);
                this.put(token, pl);
            } else {
                PostingList pl = this.get(token);
                pl.insert(d);
            }
        }
    }
}
