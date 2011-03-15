/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author kaldr
 */
public class orTags {

    public HashMap<Integer, ArrayList> doc2tag;
    public HashMap<Integer, ArrayList> user2tag;
    public HashMap<Integer, String> id2tags;
    public HashMap<String, Integer> tags2id;

    public orTags() {
        id2tags = new HashMap<Integer, String>();
        user2tag = new HashMap<Integer, ArrayList>();
        doc2tag = new HashMap<Integer, ArrayList>();
        tags2id = new HashMap<String, Integer>();
    }

    public void setTags(String filename) {
        String tag = "";
        Integer user = null;
        Integer doc = null;
        Integer no = 0;
        ArrayList b = new ArrayList();
        b.add(no);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            System.out.println("----" + filename + " loaded! ----");
            String line = reader.readLine();
            while (line != null) {
                StringTokenizer tknr = new StringTokenizer(line, "::");
                user = Integer.parseInt(tknr.nextToken());
                doc = Integer.parseInt(tknr.nextToken());
                tag = tknr.nextToken().toLowerCase().replace("!", "").replace("?", "");
                if (no == 0) {
                    id2tags.put(no, tag);
                    tags2id.put(tag, no);
                    user2tag.put(user, b);
                    doc2tag.put(doc, b);
                    //
                    System.out.println("User "+user+" and Doc"+doc+" has tag "+id2tags.get(no));
                    //
                    no++;
                } else {
                    if (doc2tag.containsKey(doc)) {
                        doc2tag.get(doc).add(no);
                    } else {
                        b = new ArrayList();
                        b.add(no);
                        doc2tag.put(doc, b);
                    }
                    if (user2tag.containsKey(user)) {
                        user2tag.get(user).add(no);
                    } else {
                        b = new ArrayList();
                        b.add(no);
                        user2tag.put(user, b);
                    }
                    if (!tags2id.containsKey(tag)) {
                        id2tags.put(no, tag);
                        tags2id.put(tag, no);
                        if(no<100)System.out.println("User "+user+" and Doc"+doc+" has tag "+id2tags.get(no));
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

    public void setTagItem() {
    }
}
