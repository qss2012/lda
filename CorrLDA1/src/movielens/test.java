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
        orUser user=new orUser();
        orTags tag=new orTags();
        String moviefile="movies.dat";
        String user2moviefile="ratings.dat";
        String tagfile="tags.dat";
        //setting data;
        movie.readMovie(moviefile);//get movie and movie id;
        Integer movielen=movie.id_title.size();//
        user.readForItem(user2moviefile);//get user and rated movie;
        Integer userlen=user.userid2doc.size();
        tag.setTags(tagfile);//get tag id and tags; get user and tagged movie and corresponding tags; get movie and its tags;
        Integer taglen=tag.id2tags.size();
        //initialdata;
        System.out.println("Movies: "+movielen);
        System.out.println("Users: "+userlen);
        System.out.println("Tags: "+taglen);
    }
}
