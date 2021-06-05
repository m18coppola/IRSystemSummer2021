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
    public static PostingList masterPostingList;
    
    public InvertedIndex(boolean containsStopWords) {
        this.containsStopWords = containsStopWords;
        masterPostingList = new PostingList();
    }
    
    public void addDoc(Document d) {
        masterPostingList.insert(d.getDocID());
        TokenizerList tokens;
        if (containsStopWords) {
            tokens = new TokenizerList(d.getText(), true);
        } else {
            tokens = new TokenizerList(d.getText(), false);
        }
        
        int pos = 0;
        for (String token: tokens) {
            if (!this.containsKey(token)) {
                PostingList pl = new PostingList();
                pl.insert(d.getDocID(), pos);
                this.put(token, pl);
            } else {
                PostingList pl = this.get(token);
                pl.insert(d.getDocID(), pos);
            }
            pos++;
        }
    }
    
    @Override
    public PostingList get(Object key) {
        PostingList result = super.get(key);
        if (result  == null) {
            result = new PostingList();
        }
        return result;
    }
}
