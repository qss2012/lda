/*
 * This function returns 
 */
package movielens;
import data.Item;
import java.util.Arrays;

/**
 *
 * @author kaldr
 */
public class getItem {

    public Item movies = new Item();
    public orII orMovie = new orII();
    //structural function

    public getItem() {
        movies = new Item();
        orMovie = new orII();
    }
    //main

    /**
    public static void main(String args[]) {
    getItem item = new getItem();
    String orFile = "movies.dat";
    item.setItem(orFile);
    }
     **/
    //Configure the movies from orginal movie data.
    public Item setItem(String orFile) {
        orMovie.readMovie(orFile);
        Object[] idKeys = orMovie.getIDKey();
        Arrays.sort(idKeys);
        int len = idKeys.length;
        for (int i = 0; i < len; i++) {
            movies.id2item.put(i, idKeys[i].toString());
            movies.item2id.put(idKeys[i].toString(), i);
        }
        return movies;
    }
}
