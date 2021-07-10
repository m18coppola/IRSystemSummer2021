package InforormationRetrievalProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Stack;

/*
    The parsing static utilities
*/

public class Parser {
    public enum Attribute {
        DOC,
        DOCNO,
        PROFILE,
        DATE,
        HEADLINE,
        BYLINE,
        TEXT,
        PUBLISHER,
        PAGE,
        DATELINE
    }
    
    /*
        Takes a TREC file and parses it into an ArrayList of Documents.
    
        @param file The file to be parsed.
        @return An ArrayList populated with the documents from the input file.
    */
    
    public static ArrayList<Document> parseTREC(File file) throws FileNotFoundException, IOException{
        String bufferedXMLcontent = Files.readString(file.toPath());
        String[] XMLtokens = bufferedXMLcontent.split("(?=<)|(?<=>)");
        ArrayList<Document> docs = new ArrayList<>();
        Document currDoc = null;
        Stack<Attribute> currState = new Stack<>();
        for (int i = 0; i < XMLtokens.length; i++) {
            if (XMLtokens[i].equals("<DOC>")) {
                currDoc = new Document();
                currState.push(Attribute.DOC);
            } else if (XMLtokens[i].equals("</DOC>")) {
                docs.add(currDoc);
            } else if (XMLtokens[i].equals("<DOCNO>")) {
                currState.push(Attribute.DOCNO);
            } else if (XMLtokens[i].equals("</DOCNO>")) {
                currState.pop();
            } else if (XMLtokens[i].equals("<PROFILE>")) {
                currState.push(Attribute.PROFILE);
            } else if (XMLtokens[i].equals("</PROFILE>")) {
                currState.pop();
            } else if (XMLtokens[i].equals("<DATE>")) {
                currState.push(Attribute.DATE);
            } else if (XMLtokens[i].equals("</DATE>")) {
                currState.pop();
            } else if (XMLtokens[i].equals("<HEADLINE>")) {
                currState.push(Attribute.HEADLINE);
            } else if (XMLtokens[i].equals("</HEADLINE>")) {
                currState.pop();
            } else if (XMLtokens[i].equals("<BYLINE>")) {
                currState.push(Attribute.BYLINE);
            } else if (XMLtokens[i].equals("</BYLINE>")) {
                currState.pop();
            } else if (XMLtokens[i].equals("<TEXT>")) {
                currState.push(Attribute.TEXT);
            } else if (XMLtokens[i].equals("</TEXT>")) {
                currState.pop();
            } else if (XMLtokens[i].equals("<PUB>")) {
                currState.push(Attribute.PUBLISHER);
            } else if (XMLtokens[i].equals("</PUB>")) {
                currState.pop();
            } else if (XMLtokens[i].equals("<PAGE>")) {
                currState.push(Attribute.PAGE);
            } else if (XMLtokens[i].equals("</PAGE>")) {
                currState.pop();
            } else if (XMLtokens[i].equals("<DATELINE>")) {
                currState.push(Attribute.DATELINE);
            } else if (XMLtokens[i].equals("</DATELINE>")) {
                currState.pop();
            } else {
                switch(currState.peek()){
                    case DOCNO:
                        currDoc.docNo = XMLtokens[i];
                        break;
                    case PROFILE:
                        currDoc.profile = XMLtokens[i];
                        break;
                    case DATE:
                        currDoc.date = XMLtokens[i];
                        break;
                    case HEADLINE:
                        currDoc.headline = XMLtokens[i];
                        break;
                    case BYLINE:
                        currDoc.byLine = XMLtokens[i];
                        break;
                    case TEXT:
                        currDoc.text = XMLtokens[i];
                        break;
                    case PUBLISHER:
                        currDoc.publisher = XMLtokens[i];
                        break;
                    case PAGE:
                        currDoc.page = XMLtokens[i];
                        break;
                    case DATELINE:
                        currDoc.dateLine = XMLtokens[i];
                        break;
                    default:
                        break;
                }
            }
        }
        
        return docs;
    }
}
