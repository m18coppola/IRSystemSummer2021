/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InforormationRetrievalProject;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author m18coppola
 */
public class Query {
    
    ArrayList<String> tokens;
    
    public Query(String userInput) {
        tokens = new ArrayList<String>();
        
        
        userInput = userInput.trim();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(userInput);
        /* regexp:  [^"]\S*  |  ".+?" */
        /* [^"]\S* any token starting with anything other than a quote and followed by anything other than a space */
        /* ".+?" unconditionally everything between two quotes */
        while (m.find()) {
            tokens.add(m.group(1).replace("\"", ""));
        }
    }
    
    public PostingList search(InvertedIndex index) {
        PostingList answer = new PostingList();
        Stack<PostingList> booleanStack = new Stack<PostingList>();
        for(int i = 0; i < tokens.size(); i++) {
            if (i == 2);
            if (tokens.get(i).equals("AND")) {
                PostingList temp = booleanStack.pop();
                if (tokens.get(i+1).equals("NOT")){
                   booleanStack.push(PostingList.intersect(temp,PostingList.negate(index.masterPostingList, index.get(tokens.get(i+2).toLowerCase())) ));
                   i=i+2;
                } else {
                    booleanStack.push(PostingList.intersect(temp, index.get(tokens.get(i + 1).toLowerCase())));
                    i++;
                }
            } else if (tokens.get(i).equals("OR")) {
                if (tokens.get(i+1).equals("NOT")){
                   booleanStack.push(PostingList.negate(index.masterPostingList, index.get(tokens.get(i+2).toLowerCase())));
                   i=i+2;
                } else {
                    i++;
                    booleanStack.push(index.get(tokens.get(i).toLowerCase()));
                }
            } else if (tokens.get(i).equals("NOT")){
                i++;
                booleanStack.push(PostingList.negate(index.masterPostingList, index.get(tokens.get(i).toLowerCase())));
            }else {
                booleanStack.push(index.get(tokens.get(i).toLowerCase()));
            }
        }
        
        while (!booleanStack.isEmpty()) {
            answer = PostingList.union(answer, booleanStack.pop());
        }
        
        return answer;
    }
}
