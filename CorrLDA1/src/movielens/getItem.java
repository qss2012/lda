/*
 * This function returns 
 */
package movielens;

import data.Item;
import data.User;
import java.util.Arrays;

/**
 *
 * @author kaldr
 */
public class getItem {

    public User users = new User();
    public Item movies = new Item();
    public orII orMovie = new orII();
    //structural function

    public getItem() {
        movies = new Item();
        users = new User();
        orMovie = new orII();
    }
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
