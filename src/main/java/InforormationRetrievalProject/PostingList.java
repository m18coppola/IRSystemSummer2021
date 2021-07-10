/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InforormationRetrievalProject;

import static java.lang.Math.log10;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author m18coppola
 */
public class PostingList {
    

    
    
    
    private int size;
    private int height = 0;
    public double idf = -1.0;
    public TreeMap<Integer, Post> treemap;
    
    
    
    public PostingList(){
        treemap = new TreeMap<Integer, Post>();
        size = 0;
    }
    
    public double getIDF(int n) {
        return idf;
    }
    
    public void calculate(int n) {
        idf = log10((double)n/(double)size);
        Post p = null;
        Iterator iter = treemap.entrySet().iterator();
        if (iter.hasNext()) {
            p = (iter.hasNext()) ? (Post)((Entry)(iter.next())).getValue() : null;
        }
        while (p != null) {
            p.calcWeightedTF(idf);
            p = (iter.hasNext()) ? (Post)((Entry)(iter.next())).getValue() : null;
        }
    }
    
    public Post getPostByID(int docID) {
        return treemap.get(docID);
    }
    
    public void insert(int docID, int pos) {
        Post newPost;
        if (treemap.containsKey(docID)) {
            newPost = treemap.get(docID);
        } else {
            newPost = new Post(docID);
            treemap.put(docID, newPost);
        }
        newPost.freq++;
        size++;
        newPost.positions.add(pos);
    }

    public static PostingList intersect(PostingList listA, PostingList listB) {
        
        PostingList resultList = new PostingList();
        
        Post fingerA = null;
        Post fingerB = null;
        Iterator iterA = listA.treemap.entrySet().iterator();
        Iterator iterB = listB.treemap.entrySet().iterator();
        if (iterA.hasNext()) {
            fingerA = (iterA.hasNext()) ? (Post)((Entry)(iterA.next())).getValue() : null;
        }
        if (iterB.hasNext()) {
            fingerB = (iterB.hasNext()) ? (Post)((Entry)(iterB.next())).getValue() : null;
        }
        
        while (fingerA != null && fingerB != null) {
            if (fingerA.docID == fingerB.docID) {
                resultList.insert(fingerA.docID, 0);
                fingerA = (iterA.hasNext()) ? (Post)((Entry)(iterA.next())).getValue() : null;
                fingerB = (iterB.hasNext()) ? (Post)((Entry)(iterB.next())).getValue() : null;
            } else if (fingerA.docID < fingerB.docID) {
                fingerA = (iterA.hasNext()) ? (Post)((Entry)(iterA.next())).getValue() : null;
            } else {
                fingerB = (iterB.hasNext()) ? (Post)((Entry)(iterB.next())).getValue() : null;
            }
        }
        
        return resultList;
    }
    
    public static PostingList union(PostingList listA, PostingList listB) {
        
        PostingList resultList = new PostingList();
        
        Post fingerA = null;
        Post fingerB = null;
        Iterator iterA = listA.treemap.entrySet().iterator();
        Iterator iterB = listB.treemap.entrySet().iterator();
        if (iterA.hasNext()) {
            fingerA = (iterA.hasNext()) ? (Post)((Entry)(iterA.next())).getValue() : null;
        }
        if (iterB.hasNext()) {
            fingerB = (iterB.hasNext()) ? (Post)((Entry)(iterB.next())).getValue() : null;
        }
        
        while (fingerA != null || fingerB != null) {
            if(fingerA != null && fingerB != null) {
                if (fingerA.docID < fingerB.docID) {
                    resultList.insert(fingerA.docID, 0);
                    fingerA = (iterA.hasNext()) ? (Post)((Entry)(iterA.next())).getValue() : null;
                } else if (fingerB.docID < fingerA.docID) {
                    resultList.insert(fingerB.docID, 0);
                    fingerB = (iterB.hasNext()) ? (Post)((Entry)(iterB.next())).getValue() : null;
                } else {
                    resultList.insert(fingerA.docID, 0);
                    fingerA = (iterA.hasNext()) ? (Post)((Entry)(iterA.next())).getValue() : null;
                    fingerB = (iterB.hasNext()) ? (Post)((Entry)(iterB.next())).getValue() : null;
                }
            } else {
                if (fingerA == null) {
                    while (fingerB != null) {
                        resultList.insert(fingerB.docID, 0);
                        fingerB = (iterB.hasNext()) ? (Post)((Entry)(iterB.next())).getValue() : null;
                    }
                } else {
                    while (fingerA != null) {
                        resultList.insert(fingerA.docID, 0);
                        fingerA = (iterA.hasNext()) ? (Post)((Entry)(iterA.next())).getValue() : null;
                    }
                }
            }
            
        }
        
        return resultList;
    }
    
    public static PostingList negate(PostingList masterList, PostingList subtractList) {
        
        PostingList resultList = new PostingList();
        
        Post fingerA = null;
        Post fingerB = null;
        Iterator iterA = masterList.treemap.entrySet().iterator();
        Iterator iterB = subtractList.treemap.entrySet().iterator();
        if (iterA.hasNext()) {
            fingerA = (iterA.hasNext()) ? (Post)((Entry)(iterA.next())).getValue() : null;
        }
        if (iterB.hasNext()) {
            fingerB = (iterB.hasNext()) ? (Post)((Entry)(iterB.next())).getValue() : null;
        }
        
        while (fingerA != null) {
            if (fingerB == null || fingerA.docID < fingerB.docID) {
                resultList.insert(fingerA.docID, 0);
                fingerA = (iterA.hasNext()) ? (Post)((Entry)(iterA.next())).getValue() : null;
            } else {
                fingerA = (iterA.hasNext()) ? (Post)((Entry)(iterA.next())).getValue() : null;
                fingerB = (iterB.hasNext()) ? (Post)((Entry)(iterB.next())).getValue() : null;
            }
        }
        
        return resultList;
    }
    
    static PostingList phraseSearch(String[] keys, InvertedIndex index) {
        PostingList matchList = new PostingList();
        ArrayList<PostingList> listsByKey = new ArrayList<PostingList>();
        
        for (String key: keys) {
            listsByKey.add(index.get(key));
        }
        
        Iterator iter = matchList.treemap.entrySet().iterator();
        Post firstWord = null;
        if (iter.hasNext()) {
            firstWord = (Post)((Entry)(iter.next())).getValue();
        }
        while (firstWord != null) {
            boolean found = true;
            for(int pos : firstWord.positions) {
                for (int i = 1; i < listsByKey.size(); i++) {
                    PostingList pl = listsByKey.get(i);
                    Post p = pl.getPostByID(firstWord.docID);
                    if (p == null || !p.positions.contains(pos + i)) {
                        found = false;
                    }
                }
            }
            if (found) {
                matchList.insert(firstWord.docID, 0);
            }
            firstWord = (iter.hasNext()) ? (Post)((Entry)(iter.next())).getValue() : null;
        }
        
        return matchList;
    }

    

    void calculateL2norm(InvertedIndex index) {
        
        Post p = null;
        Iterator iter = treemap.entrySet().iterator();
        if (iter.hasNext()) {
            p = (iter.hasNext()) ? (Post)((Entry)(iter.next())).getValue() : null;
        }
        
        
        while (p != null) {
            p.calculatel2norm(index);
            p = (iter.hasNext()) ? (Post)((Entry)(iter.next())).getValue() : null;
        }
    }

    void calculateNormalWeights(PostingList masterPostingList) {
        Post p = null;
        Iterator iter = treemap.entrySet().iterator();
        if (iter.hasNext()) {
            p = (iter.hasNext()) ? (Post)((Entry)(iter.next())).getValue() : null;
        }
        while (p != null) {
            
            p.normWeight = p.weightedTF/masterPostingList.getPostByID(p.docID).l2norm;
            p = (iter.hasNext()) ? (Post)((Entry)(iter.next())).getValue() : null;
        }
    }
    
    public class Post{       
        int docID;
        ArrayList<Integer> positions = new ArrayList<Integer>();
        int freq;
        double weightedTF = -1.0;
        double l2norm = -1.0;
        double normWeight = -1.0;
        double cosineSim = 0.0;
        
        public Post(int docID) {
            this.docID = docID;
            freq = 0;
        }
        
        public double getWeightedTF() {
            return weightedTF;
        }
        
        public void calcWeightedTF(double idf){
            if (freq == 0) {
                weightedTF = 0.0;
            } else {
                weightedTF = (1.0 + log10(freq)) * idf;
            }
        }
        
        public double getl2norm(){
            return l2norm;
        }
        
        public void calculatel2norm(InvertedIndex index){
            double sum = 0.0;
            for(Map.Entry e : index.entrySet()) {
                PostingList pl = (PostingList)e.getValue();
                Post p = pl.getPostByID(docID);
                if (p != null) {
                    sum += p.getWeightedTF();
                }
            }
            l2norm = sqrt(sum);
            for(Map.Entry e : index.entrySet()) {
                PostingList pl = (PostingList)e.getValue();
                Post p = pl.getPostByID(docID);
                if (p != null) p.normWeight = p.weightedTF/l2norm;
            }
            
        }
    }
    
    public int size() {
        return size;
    }
    
}
