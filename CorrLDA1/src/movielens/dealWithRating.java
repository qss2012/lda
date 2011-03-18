/*
 * This programme deals with rating.dat to userwithmovie.dat, because the orienated document is too large to deal with.
 */
package movielens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

/**
 *
 * @author kaldr
 */
public class dealWithRating {
    public dealWithRating(){
        
    }

    public static void main(String args[]) {
        try {
            String filename="ratings.dat";
            String outputfile = "userwithmovies.dat";
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            System.out.println("----" + filename + " loaded! ----");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputfile), "UTF-8"));
            System.out.println("----" + outputfile + " is been writing now ----");
            String line = reader.readLine();
            String userid = "";
            String docid = "";
            String lastuserid = userid;
            //the first user
            StringTokenizer tknr = new StringTokenizer(line, "::");
            userid = tknr.nextToken();
            docid = tknr.nextToken();
            lastuserid = userid;
            writer.write(userid + " " + docid + " ");
            line = reader.readLine();
            //other users
            
            while (line!=null) {
               
                tknr = new StringTokenizer(line, "::");
                userid = tknr.nextToken();
                docid = tknr.nextToken();
                if (lastuserid.equals(userid)) {
                    writer.write(docid+" ");
                }else{
                    writer.write("\r\n"+userid+" "+docid+" ");
                }
                lastuserid = userid;
                line=reader.readLine();
            }
            reader.close();
            writer.close();
        } catch (Exception e) {
        }
    }
}
