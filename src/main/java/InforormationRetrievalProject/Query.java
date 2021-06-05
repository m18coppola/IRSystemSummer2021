/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InforormationRetrievalProject;

import java.util.ArrayList;
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
}
