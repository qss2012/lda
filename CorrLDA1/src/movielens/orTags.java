/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
        int findtagfile = 0;
        try {
            File dir = new File("/");
            File[] listOfFiles = dir.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    if (listOfFiles[i].getName().equals("tagmap.dat")) {
                        findtagfile = 1;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filename), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("tagmap.dat"), "UTF-8"));
            System.out.println("******************************\n" + filename + " loaded!");
            System.out.println("Arranging tags to id, movies and users\n******************************");
            String line = reader.readLine();
            while (line != null) {
                StringTokenizer tknr = new StringTokenizer(line, "\t\r\n");
                user = Integer.parseInt(tknr.nextToken());
                doc = Integer.parseInt(tknr.nextToken());
                tag = tknr.nextToken().toLowerCase().replace("!", "").replace("?", "");
                if (no == 0) {
                    id2tags.put(no, tag);
                    if(findtagfile==0)writer.write(no+"::"+tag+"\n");
                    tags2id.put(tag, no);
                    user2tag.put(user, b);
                    doc2tag.put(doc, b);
                    no++;
                } else {

                    if (!tags2id.containsKey(tag)) {
                        id2tags.put(no, tag);
                         if(findtagfile==0)writer.write(no+"::"+tag+"\r\n");
                        tags2id.put(tag, no);
                        no++;
                    }
                    Integer newtagid = tags2id.get(tag);
                    if (doc2tag.containsKey(doc)) {
                        doc2tag.get(doc).add(newtagid);
                    } else {
                        b = new ArrayList();
                        b.add(newtagid);
                        doc2tag.put(doc, b);
                    }
                    if (user2tag.containsKey(user)) {
                        user2tag.get(user).add(newtagid);
                    } else {
                        b = new ArrayList();
                        b.add(newtagid);
                        user2tag.put(user, b);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
            System.out.println("Data " + filename + " is analyzed.\n");
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();

        }
    }
}
