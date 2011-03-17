/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package corrlda;

import movielens.orII;
import movielens.orTags;
import movielens.orUser;

/**
 *
 * @author kaldr
 */
public class Model {
    public orII itemData=new orII();
    public orUser userData=new orUser();
    public orTags tagData=new orTags();
    public int itemlen=0;
    public int userlen=0;
    public int taglen=0;
    public Model(){
        itemData=new orII();
        userData=new orUser();
        tagData=new orTags();
        itemlen=0;
        userlen=0;
        taglen=0;
    }
    public void initialize(){
        String moviefile="movies.dat";
        String user_moviefile="userwithmovies.dat";
        String tagfile="tags.dat";
        itemData.readMovie(moviefile);
        userData.readForItem(user_moviefile);
        tagData.setTags(tagfile);
        itemlen=itemData.id_title.size();
        userlen=userData.userid2doc.size();
        taglen=tagData.id2tags.size();
        System.out.println("Dataset has "+itemlen+" movies, "+userlen+" users, and"+taglen+"tags.\n ");
    }
    
}
