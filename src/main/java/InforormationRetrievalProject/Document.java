package InforormationRetrievalProject;

/*
    Basic datatype to express a document in TREC datasets
*/

public class Document {
    String docNo;
    String profile;
    String date;
    String headline;
    String byLine;
    String text;
    String publisher;
    String page;
    String dateLine;
    int docID;
    TokenizerList keywords;
    TokenizerList allwords;
    
    static int docCount = 0;
    
    public Document()
    {
        docID = docCount;
        docCount++;
    }
    
    public String getText() {
        return text;
    }
    
    public int getDocID() {
        return docID;
    }
    
    /*
        Returns the headline and document text
    
        @return String containing Document name and text
    */
    @Override
    public String toString() {
        return
                "**********" +
                headline +
                "**********" +
                text +
                "**********" ;
    }
}
