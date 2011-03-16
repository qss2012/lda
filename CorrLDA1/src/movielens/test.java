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
        orII doc = new orII();
        orUser user = new orUser();
        orTags tag = new orTags();
        String document = "movies.dat";
        //doc.readMovie(document);
        String tagdoc = "tags.dat";
        tag.setTags(tagdoc);
        //get all doc id.
        Object[] docids = tag.doc2tag.keySet().toArray();
        int len = docids.length;
        System.out.println(len + " documents have tags");
        for (int i = 0; i < 50; i++) {
            String docidS = docids[i].toString();
            Integer docid = Integer.parseInt(docidS);
            ArrayList tags = tag.doc2tag.get(docid);
            int len_tag = tags.size();
            System.out.println("Doc " + docid + " has tags:");
            for (int j = 0; j < len_tag; j++) {
                String tagidS = tags.get(j).toString();
                Integer tagid = Integer.parseInt(tagidS);
                System.out.println("::" + tag.id2tags.get(tagid) + "::");

            }
            System.out.println("");
        }
    }
}
