/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class orTags {

    public HashMap<Integer, String> id2tags;
    public HashMap<String, Integer> tags2id;

    public orTags() {
        id2tags = new HashMap<Integer, String>();
        tags2id = new HashMap<String, Integer>();
    }

    public void setTags(String filename) {
        String tag = "";
        String user = "";
        String doc = "";
        Integer no = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            System.out.println("----" + filename + " loaded! ----");
            String line = reader.readLine();
            while (line != null) {
                StringTokenizer tknr = new StringTokenizer(line, "::");
                user = tknr.nextToken();
                doc = tknr.nextToken();
                tag = tknr.nextToken().toLowerCase().replace("!","").replace("?","");
                if (no==0) {
                    id2tags.put(no, tag);
                    tags2id.put(tag,no);
                    System.out.println(id2tags.get(no));
                    no++;
                } else {
                    if(!tags2id.containsKey(tag)){
                        id2tags.put(no, tag);
                        tags2id.put(tag,no);
                        System.out.println(id2tags.get(no));
                        no++;
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();

        }
    }
}
