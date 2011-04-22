/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zico.del;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Administrator
 */
public class delProcess {
    /*
    public ArrayList trainMovie;
    public ArrayList testMovie;

    public delProcess() {
    trainMovie = new ArrayList();
    testMovie = new ArrayList();
    }
    
     */

    public static void main(String args[]) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("tags.dat"), "UTF-8"));
            
            BufferedWriter tagswriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("train/tags.dat"), "UTF-8"));
            BufferedWriter ttagswriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test/tags.dat"), "UTF-8"));

            String line = reader.readLine();
            double rand;
            while (line != null) {
                rand = Math.random();
                if (rand < 0.9) {
                    tagswriter.write(line + "\r\n");
                } else {
                    ttagswriter.write(line + "\r\n");
                }
                line = reader.readLine();
            }
            reader.close();
            tagsProcess("train");
            tagsProcess("test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tagsProcess(String folder) {

        try {
            BufferedReader tagdoc = new BufferedReader(new InputStreamReader(new FileInputStream(folder+"/tags.dat"), "UTF-8"));
            BufferedWriter moviewriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder + "/movies.dat"), "UTF-8"));
            BufferedWriter umwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder + "/userwithmovies.dat"), "UTF-8"));
            ArrayList itemlist = new ArrayList();
            String line = tagdoc.readLine();
            
            StringTokenizer tknz = new StringTokenizer(line, "::\r\n");
            int user = Integer.parseInt(tknz.nextToken());
            int item = Integer.parseInt(tknz.nextToken());
            int tag = Integer.parseInt(tknz.nextToken());
            int lastuser=0;
            while (line != null) {
                if(!itemlist.contains(item)){
                    itemlist.add(item);                    
                    moviewriter.write(item+"::"+item+"\r\n");
                }
                if(lastuser!=user){
                    umwriter.write("\r\n"+user+" "+item+" ");
                }else{
                    umwriter.write(item+" ");
                }
                lastuser=user;
                line = tagdoc.readLine();
                tknz = new StringTokenizer(line, "::\r\n");
                user = Integer.parseInt(tknz.nextToken());
                item = Integer.parseInt(tknz.nextToken());
                tag = Integer.parseInt(tknz.nextToken());
            }
            moviewriter.close();
            umwriter.close();
            tagdoc.close();
        } catch (Exception e) {
        }

    }
}
