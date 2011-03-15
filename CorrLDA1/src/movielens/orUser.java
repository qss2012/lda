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
public class orUser {

    public HashMap<Integer, ArrayList> userid2doc;
    public HashMap<Integer, ArrayList> userid2tag;

    public orUser() {
        userid2doc = new HashMap<Integer, ArrayList>();
        userid2tag = new HashMap<Integer, ArrayList>();
    }

    public void readRating(String filename) {
    }

    public void readForTag(String filename) {
        String userid = "";
        String lastuserid = userid;
        String doc = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            System.out.println("----" + filename + "loaded! ----");
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                i++;
                StringTokenizer tknr = new StringTokenizer(line, "::");
                userid = tknr.nextToken();
                Integer id = Integer.parseInt(userid);
                doc = tknr.nextToken();
                if (!lastuserid.equals(userid)) {
                    ArrayList b = new ArrayList();
                    b.add(doc);
                    userid2doc.put(id, b);
                } else {
                    userid2doc.get(id).add(doc);
                }
                lastuserid = userid;
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void readForItem(String filename) {
        String userid = "";
        String lastuserid = userid;
        String doc = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            System.out.println("----" + filename + "loaded! ----");
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                i++;
                StringTokenizer tknr = new StringTokenizer(line, "::");
                userid = tknr.nextToken();
                Integer id = Integer.parseInt(userid);
                doc = tknr.nextToken();
                if (!lastuserid.equals(userid)) {
                    ArrayList b = new ArrayList();
                    b.add(doc);
                    userid2doc.put(id, b);
                } else {
                    userid2doc.get(id).add(doc);
                }
                lastuserid = userid;
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
