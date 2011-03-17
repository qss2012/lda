/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

import java.util.ArrayList;

/**
 *
 * @author kaldr
 */
public class test {

    public static void main(String args[]) {
        orII movie = new orII();
        orUser user = new orUser();
        orTags tag = new orTags();
        String moviefile = "movies.dat";
        String user2moviefile = "userwithmovies.dat";
        String tagfile = "tags.dat";

       // setting data;
        movie.readMovie(moviefile);//get movie and movie id;
        Integer movielen=movie.id_title.size();//

        user.readForItem(user2moviefile);//get user and rated movie;
        Integer userlen = user.userid2doc.size();
        //tag.setTags(tagfile);//get tag id and tags; get user and tagged movie and corresponding tags; get movie and its tags;
        //Integer taglen=tag.id2tags.size();

        //initialdata;
        //System.out.println("Movies: "+movielen);
        System.out.println("Users: " + userlen);
        //System.out.println("Tags: "+taglen);
        ArrayList docid = user.userid2doc.get(324);
        int doclen = docid.size();
        System.out.println("User "+324+" has rated Movie:");
        for (int k = 0; k < doclen; k++) {            
            Integer id=Integer.parseInt((String) docid.get(k));
            System.out.println("::"+movie.id_title.get(id)+"::");
        }
    }
}
