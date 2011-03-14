/*
 * this function is for original item and id
 * orII has 2 maps, one is id_title, the other is title_id;
 * orII has a method readMovie(String filename) which return a boolean and get content for 2 maps.
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
    public HashMap<String, Integer> title_id;

    public orII() {
        id_title = new HashMap<Integer, String>();
        title_id = new HashMap<String, Integer>();
    }
    //get id
    public Object[] getIDKey(){
        return id_title.keySet().toArray();
    }
    public Integer getID(String word){
        return title_id.get(word);
    }
    //get title
    public String getID(Integer id){
        return id_title.get(id);
    }
    //readMovie
    public boolean readMovie(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filename), "UTF-8"));
            System.out.println("----"+filename + " loaded!----");
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                i++;
                StringTokenizer tknr = new StringTokenizer(line, "::");
                String id = tknr.nextToken();
                String word = tknr.nextToken();
                Integer intID = Integer.parseInt(id);
                id_title.put(intID, word);
                title_id.put(word, intID);
                line = reader.readLine();
            }
            reader.close();
            System.out.println("----Original data "+filename+" is analysed.----");
            return true;
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
