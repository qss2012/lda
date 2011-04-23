/*
 * this function is for original item and id
 * orII has 2 maps, one is id_title, the other is title_id;
 * orII has a method readMovie(String filename) which return a boolean and get content for 2 maps.
 */
package movielens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author kaldr
 */
public class orII {

    public HashMap<Integer, String> id_title;
    public HashMap<String, Integer> title_id;
    public HashMap<Integer,Integer>id_idr;
    public HashMap<Integer,Integer>idr_id;

    public orII() {
        id_title = new HashMap<Integer, String>();
        title_id = new HashMap<String, Integer>();
        id_idr=new HashMap<Integer,Integer>();
        idr_id=new HashMap<Integer,Integer>();
    }
    
    //readMovie

    public boolean readMovie(String filename) {
        int findit = 0;
        try {
            File dir = new File("/");
            File[] listOfFiles = dir.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    if (listOfFiles[i].getName().equals("moviemap.dat")) {
                        findit = 1;
                        System.out.println("moviemap.dat already exists.");
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filename), "UTF-8"));

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("moviemap.dat"), "UTF-8"));
            System.out.println("******************************\n" + filename + " loaded!");
            System.out.println("Arranging movies to id----\n******************************");
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                
                StringTokenizer tknr = new StringTokenizer(line, "::");
                String id = tknr.nextToken();
                String word = tknr.nextToken();
                
                Integer intID = Integer.parseInt(id);
                id_title.put(intID, word);
                title_id.put(word, intID);
                id_idr.put(intID,i);
                idr_id.put(i,intID);
                if (findit == 0) {
                    writer.write(i + "::" + intID+"\r\n");
                    writer.flush();
                }
                line = reader.readLine();
                i++;
            }
            reader.close();
            writer.close();
            System.out.println("Original data " + filename + " is analysed.\n");
            return true;
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
