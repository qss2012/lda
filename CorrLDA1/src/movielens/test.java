/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

/**
 *
 * @author kaldr
 */
public class test {
    public static void main(String args[]) {
        orII doc = new orII();
        String document="movies.dat";
        doc.readMovie(document);
        for (int i = 1; i < 15; i++) {
            System.out.println("document " + i + " is " + doc.id_title.get(i));
        }
    }
}
