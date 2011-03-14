/*
 * this function is for original item and id
 */
package movielens;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author kaldr
 */
public class orII {

    public HashMap<Integer, String> id_title;
    public HashMap<String,Integer> title_id;
    public  orII(){
        id_title=new HashMap<Integer,String>();
        title_id=new HashMap<String,Integer>();
    }
    public boolean readMovie(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filename), "UTF-8"));
            System.out.println(filename + " loaded!");
            String line = reader.readLine();
            while (line != null) {
                StringTokenizer tknr = new StringTokenizer(line, "::");
                String id = tknr.nextToken();
                String word = tknr.nextToken();
                Integer intID = Integer.parseInt(id);
                id_title.put(intID, word);
                title_id.put( word,intID);
                line = reader.readLine();
            }
            reader.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
