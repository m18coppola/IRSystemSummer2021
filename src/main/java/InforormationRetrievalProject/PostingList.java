/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InforormationRetrievalProject;

/**
 *
 * @author m18coppola
 */
public class PostingList {
    Post head;
    int size;
    
    public void PostngList(){
        head = null;
        size = 0;
    }
    
    public void insert(int docID) {
        if (head == null) {
            head = new Post(docID);
            size++;
            head.freq++;
        } else {
            Post finger = head;
            while (!(finger.docID == docID || finger.next == null)) {
                finger = finger.next;
            }
            if (finger.docID == docID) {
                finger.freq++;
            } else {
                finger.next = new Post(docID);
                size++;
                finger.next.freq++;
            }
        }
        
    }

    public static PostingList intersect(PostingList listA, PostingList listB) {
        
        PostingList resultList = new PostingList();
        Post fingerA = listA.head;
        Post fingerB = listB.head;
        while (fingerA != null && fingerB != null) {
            if (fingerA.docID == fingerB.docID) {
                resultList.insert(fingerA.docID);
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
        Post fingerA = listA.head;
        Post fingerB = listB.head;
        while (fingerA != null || fingerB != null) {
            if(fingerA != null && fingerB != null) {
                if (fingerA.docID < fingerB.docID) {
                    resultList.insert(fingerA.docID);
                    fingerA = fingerA.next;
                } else if (fingerB.docID < fingerA.docID) {
                    resultList.insert(fingerB.docID);
                    fingerB = fingerB.next;
                } else {
                    resultList.insert(fingerA.docID);
                    fingerA = fingerA.next;
                    fingerB = fingerB.next;
                }
            } else {
                if (fingerA == null) {
                    while (fingerB != null) {
                        resultList.insert(fingerB.docID);
                        fingerB = fingerB.next;
                    }
                } else {
                    while (fingerA != null) {
                        resultList.insert(fingerA.docID);
                        fingerA = fingerA.next;
                    }
                }
            }
            
        }
        
        return resultList;
    }
    
    public static PostingList negate(PostingList masterList, PostingList subtractList) {
        
        PostingList resultList = new PostingList();
        Post masterFinger = masterList.head;
        Post subtractFinger = subtractList.head;
        
        while (masterFinger != null) {
            if (subtractFinger == null || masterFinger.docID < subtractFinger.docID) {
                resultList.insert(masterFinger.docID);
                masterFinger = masterFinger.next;
            } else {
                masterFinger = masterFinger.next;
                subtractFinger = subtractFinger.next;
            }
        }
        
        return resultList;
    }
    
    public class Post{       
        int docID;
        Post next;
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
}
