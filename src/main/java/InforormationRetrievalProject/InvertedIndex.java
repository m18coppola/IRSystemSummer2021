/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InforormationRetrievalProject;

import InforormationRetrievalProject.PostingList.Post;
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
        masterPostingList.insert(d.getDocID(), 0);
        TokenizerList tokens;
        if (containsStopWords) {
            tokens = new TokenizerList(d.getText(), true);
        } else {
            tokens = new TokenizerList(d.getText(), false);
        }
        
        int pos = 0;
        for (String token: tokens) {
            PostingList pl;
            
            if (!this.containsKey(token)) {
                pl = new PostingList();
                pl.insert(d.getDocID(), pos);
                this.put(token, pl);
            } else {
                pl = this.get(token);
                pl.insert(d.getDocID(), pos);
            }
            pos++;
        }
    }
    
    public void updateTFs(int n){
        for(Entry e : this.entrySet()) {
            PostingList pl = (PostingList)e.getValue();
            pl.calculate(n);
        }
        masterPostingList.calculateL2norm(this);
        for(Entry e : this.entrySet()) {
            PostingList pl = (PostingList)e.getValue();
            pl.calculateNormalWeights(masterPostingList);
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
