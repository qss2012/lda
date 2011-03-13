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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author kaldr
 */
public class getItemTag {

    public document doc = new document();
    public HashMap<Integer, String> itemTag = new HashMap<Integer, String>();

    public void read() {
        
        try {
            System.out.println(itemTag.size());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(doc.file), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ItemTag2.dat"), "UTF-8"));
            String line = reader.readLine();
            StringTokenizer parser = new StringTokenizer(line, " ");
            Integer doclen = doc.doclen;
            ArrayList array = new ArrayList();

            for (Integer i = 0; i < doc.doclen; i++) {
                String user = parser.nextToken();
                int item = Integer.parseInt(parser.nextToken());

                while (parser.hasMoreTokens()) {
                    array.add(parser.nextToken());
                }
                if (itemTag.containsKey(item)) {

                    if (true) {
                        String temp = itemTag.get(item) + array.toString();
                        temp = temp.replace("][", ", ");
                        temp = temp.replace("[", "");
                        temp = temp.replace("]", "");
                        temp = temp.replace(", ", " ");
                        StringTokenizer parserS = new StringTokenizer(temp, " ");
                        ArrayList tempArrayList = new ArrayList();
                        while (parserS.hasMoreTokens()) {
                            tempArrayList.add(parserS.nextToken());
                        }
                        Object[] tempArray = tempArrayList.toArray();
                        Arrays.sort(tempArray);
                        int arraylen = tempArray.length;
                        temp = "";
                        for (int m = 0; m < arraylen; m++) {
                            temp +=tempArray[m] +" " ;
                        }
                        temp = temp.replace("][", ", ");
                        temp = temp.replace("[", "");
                        temp = temp.replace("]", "");
                        temp = temp.replace(", ", " ");
                        itemTag.put(item, temp.toString());

                    }
                } else {
                    String temp = array.toString();
                    temp = temp.replace("][", ", ");
                    temp = temp.replace("[", "");
                    temp = temp.replace("]", "");
                    temp = temp.replace(", ", " ");
                    itemTag.put(item, temp+" ");
                }

                if (i != doclen - 1) {
                    array.clear();
                    line = reader.readLine();
                    parser = new StringTokenizer(line, " ");
                }
            }
            Object[] key = itemTag.keySet().toArray();
            int keylen = key.length;
            writer.write(doc.docno+"\n");
            for(int n=0;n<keylen;n++){
                int keyno=Integer.parseInt(key[n].toString());
                String newline=itemTag.get(keyno);
                writer.write(newline+"\n");
            }
            writer.close();
            reader.close();



        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
        }
    }

    public static void main(String args[]) {
        getItemTag getIT2 = new getItemTag();
        getIT2.read();
    }
}
