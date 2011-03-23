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
    public HashMap<Integer,Integer>id_idr;

    public orUser() {
        userid2doc = new HashMap<Integer, ArrayList>();
        id_idr=new HashMap<Integer,Integer>();
    }

    public void readForItem(String filename) {
        String userid = "";
        String lastuserid = userid;
        String doc = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            System.out.println("******************************\n" + filename + " loaded!");
            System.out.println("Arranging user to id and movies\n******************************");
            String line = reader.readLine();
            int tknrlen = 0;
            int i = 0;
            while (line != null) {
                
                StringTokenizer tknr = new StringTokenizer(line, " ");
                tknrlen = tknr.countTokens();
                userid = tknr.nextToken();
                doc = tknr.nextToken();
                ArrayList b=new ArrayList();
                b.add(doc);
                 Integer id = Integer.parseInt(userid);
                userid2doc.put(id,b);
                id_idr.put(id,i);
                for (int j = 1; j < tknrlen-1; j++) {
                    doc = tknr.nextToken();
                    userid2doc.get(id).add(doc);
                }
                line = reader.readLine();
                i++;
            }
            reader.close();
            System.out.println("Data "+filename+" is analyzed.\n");
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
