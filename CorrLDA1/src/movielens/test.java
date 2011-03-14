/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

import java.util.Arrays;

/**
 *
 * @author kaldr
 */
public class test {
    public static void main(String args[]) {
        orII doc = new orII();
        String document="movies.dat";
        doc.readMovie(document);
        Object[] b=doc.getIDKey();
       for(int i=0;i<100;i++){
           System.out.println(b[i]);
       }
        
    }
}
