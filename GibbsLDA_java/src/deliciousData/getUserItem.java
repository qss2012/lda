/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deliciousData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *This file is written for get a text data of user and item.
 * In the final UserItem.txt file, each line stands for a user 
 * and the tagged documents.But the first line is the
 * number of items.
 * @author kaldr
 */
public class getUserItem {

    public Map<Integer, ArrayList> user2item;
    public document doc = new document();

    public void readfirst2no() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(doc.file), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("UserItem.dat"), "UTF-8"));
            writer.write(doc.userno.toString()+"\n");
            String line = reader.readLine();
            StringTokenizer parser = new StringTokenizer(line, " ");
            long doclen = doc.doclen;
            String lastuser = "1";

            for (Integer i = 0; i < doclen; i++) {
                String user = parser.nextToken();
                String item = parser.nextToken();
                if (lastuser.equals(user)) {
                    writer.write(item + " ");
                } else {
                    writer.write("\n" + item + " ");
                }
                if (i != doclen - 1) {
                    line = reader.readLine();
                    parser = new StringTokenizer(line, " ");
                }
                lastuser = user;
            }
            writer.close();
            reader.close();
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
        }
    }

    public static void main(String args[]) {
        getUserItem geUI = new getUserItem();
        geUI.readfirst2no();
    }
}
