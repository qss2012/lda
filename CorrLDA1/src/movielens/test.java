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
        orII movie=new orII();
        movie.readMovie("movies.dat");
        orTags tag=new orTags();
        tag.setTags("tags.dat");
    }
}
