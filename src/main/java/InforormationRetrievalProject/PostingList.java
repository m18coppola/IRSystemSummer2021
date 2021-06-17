/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InforormationRetrievalProject;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author m18coppola
 */
public class PostingList {
    private static final int NEG_INF = Integer.MIN_VALUE;
    private static final int POS_INF = Integer.MAX_VALUE;
    
    private Post head, tail;
    private int size;
    private int height = 0;
    
    private Random random = new Random();
    
    public PostingList(){
        head = new Post(NEG_INF);
        tail = new Post(POS_INF);
        head.next = tail;
        tail.prev = head;
        size = 0;
    }
    
    public Post search(int docKey) {
        Post p = head;
        //System.out.println("searching for " + docKey);
        
        while (p.below != null) {
            p = p.below;
            while (docKey >= p.next.docID) {
                p = p.next;
            }
        }
        
        return p;
    }
    
    public Post skipInsert(int docID) {
        Post position = search(docID);
        Post p;
        int currHeight = -1;
        
        if (position.docID == docID) {
            return position;
        }
        
        do {
            currHeight++;
            
            increaseHeight(currHeight);
            p = position;
            while (position.above == null) {
                position = position.prev;
            }
            
            position = position.above;
            
            p = insertAfterAbove(position, p, docID);
            
        } while (random.nextBoolean());
        
        size++;
        
        return p;
    }
    
    private Post insertAfterAbove(Post position, Post p, int docID) {
        Post newPost = new Post(docID);
        Post priorPost = position.below.below;
        
        setBeforeAfter(p, newPost);
        setAboveBelow(position, docID, newPost, priorPost);
        
        return newPost;
    }
    
    private void increaseHeight(int level) {
        if (level >= height) {
            height++;
            addNewLevel();
        }
    }
    
    private void addNewLevel() {
        Post newHead = new Post(NEG_INF);
        Post newTail = new Post(POS_INF);
        newHead.next = newTail;
        newTail.prev = newHead;
        newHead.below = head;
        newTail.below = tail;
        head.above = newHead;
        tail.above = newTail;
        
        head = newHead;
        tail = newTail;
    }
    
    public void insert(int docID, int pos) {
        Post newPost = skipInsert(docID);
        newPost.freq++;
        newPost.positions.add(pos);
    }

    public static PostingList intersect(PostingList listA, PostingList listB) {
        
        PostingList resultList = new PostingList();
        Post fingerA = listA.getLLhead();
        Post fingerB = listB.getLLhead();
        while (fingerA.docID != POS_INF && fingerB.docID != POS_INF) {
            if (fingerA.docID == fingerB.docID) {
                resultList.skipInsert(fingerA.docID);
                fingerA = fingerA.next;
                fingerB = fingerB.next;
            } else if (fingerA.docID < fingerB.docID) {
                fingerA = fingerA.next;
            } else {
                fingerB = fingerB.next;
            }
        }
        
        return resultList;
    }
    
    public static PostingList union(PostingList listA, PostingList listB) {
        
        PostingList resultList = new PostingList();
        Post fingerA = listA.getLLhead();
        Post fingerB = listB.getLLhead();
        while (fingerA.docID != POS_INF || fingerB.docID != POS_INF) {
            if(fingerA.docID != POS_INF && fingerB.docID != POS_INF) {
                if (fingerA.docID < fingerB.docID) {
                    resultList.skipInsert(fingerA.docID);
                    fingerA = fingerA.next;
                } else if (fingerB.docID < fingerA.docID) {
                    resultList.skipInsert(fingerB.docID);
                    fingerB = fingerB.next;
                } else {
                    resultList.skipInsert(fingerA.docID);
                    fingerA = fingerA.next;
                    fingerB = fingerB.next;
                }
            } else {
                if (fingerA.docID == POS_INF) {
                    while (fingerB.docID != POS_INF) {
                        resultList.skipInsert(fingerB.docID);
                        fingerB = fingerB.next;
                    }
                } else {
                    while (fingerA.docID != POS_INF) {
                        resultList.skipInsert(fingerA.docID);
                        fingerA = fingerA.next;
                    }
                }
            }
            
        }
        
        return resultList;
    }
    
    public static PostingList negate(PostingList masterList, PostingList subtractList) {
        
        PostingList resultList = new PostingList();
        Post masterFinger = masterList.getLLhead();
        Post subtractFinger = subtractList.getLLhead();
        
        while (masterFinger.docID != POS_INF) {
            if (subtractFinger.docID == POS_INF || masterFinger.docID < subtractFinger.docID) {
                resultList.skipInsert(masterFinger.docID);
                masterFinger = masterFinger.next;
            } else {
                masterFinger = masterFinger.next;
                subtractFinger = subtractFinger.next;
            }
        }
        
        return resultList;
    }

    private void setBeforeAfter(Post p, Post newPost) {
        newPost.next = p.next;
        newPost.prev = p;
        p.next.prev = newPost;
        p.next = newPost;
    }

    private void setAboveBelow(Post position, int docID, Post newPost, Post priorPost) {
        if (priorPost != null) {
            while (priorPost.next.docID != docID) {
                priorPost = priorPost.next;
            }
            
            newPost.below = priorPost.next;
            priorPost.next.above = newPost;
        }
        
        if (position != null && position.next.docID == docID) {
            newPost.above = position.next;
        }
    }
    
    public class Post{       
        int docID;
        Post next;
        Post prev;
        Post below;
        Post above;
        ArrayList<Integer> positions = new ArrayList<Integer>();
        int freq;
        
        public Post(int docID) {
            this.docID = docID;
            next = null;
            freq = 0;
        }
    }
    
    public int size() {
        return size;
    }
    
    public Post getLLhead() {
        Post finger = head;
        while (finger.below != null) {
            finger = finger.below;
        }
        
        return finger.next;
    }
}
