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
    
    public void insert(Document d) {
        if (head == null) {
            head = new Post(d);
            size++;
            head.freq++;
        } else {
            Post finger = head;
            while (!(finger.doc.docID == d.docID || finger.next == null)) {
                finger = finger.next;
            }
            if (finger.doc.docID == d.docID) {
                finger.freq++;
            } else {
                finger.next = new Post(d);
                size++;
                finger.next.freq++;
            }
        }
        
    }


    
    public class Post{       
        Document doc;
        Post next;
        int freq;
        
        public Post(Document d) {
            doc = d;
            next = null;
            freq = 0;
        }        
    }
    
    public int size() {
        return size;
    }
}
