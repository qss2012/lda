/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author Administrator
 */
public class dealwithDel {

    public HashMap<Integer, Integer> userwithmovie;
    public ArrayList movie;

    public void dealwithDel() {
        userwithmovie = new HashMap<Integer, Integer>();
        movie = new ArrayList();
    }

    public static void main(String args[]) {
        dealwithDel data = new dealwithDel();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("delicious_tag_purified.dat"), "UTF-8"));
            BufferedWriter moviewriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("del/movies.dat"), "UTF-8"));
            BufferedWriter tagswriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("del/tags.dat"), "UTF-8"));
            BufferedWriter umwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("del/userwithmovies.dat"), "UTF-8"));
            String line = reader.readLine();
            String user = "";
            String movie = "";
            String tag = "";
            StringTokenizer tknz = new StringTokenizer(line, " ");
            String lastuser = "1";
            user = tknz.nextToken();
            movie = tknz.nextToken();
            while (tknz.hasMoreTokens()) {
                tag = tknz.nextToken();
                tagswriter.write(user + "::" + movie + "::" + tag + "\r\n");
                tagswriter.flush();
            }
            umwriter.write(user + " " + movie + " ");
            lastuser = user;
            line = reader.readLine();
            int movieID = Integer.parseInt(movie);
            System.out.println(movieID);
            data.movie = new ArrayList();
            data.movie.add(movieID);
            while (line != null) {
                tknz = new StringTokenizer(line, " ");
                user = tknz.nextToken();
                movie = tknz.nextToken();
                movieID = Integer.parseInt(movie);
                if (!data.movie.contains(movieID)) {
                    data.movie.add(movieID);
                }
                while (tknz.hasMoreTokens()) {
                    tag = tknz.nextToken();
                    tagswriter.write(user + "::" + movie + "::" + tag + "\r\n");
                    tagswriter.flush();
                }
                if (!lastuser.equals(user)) {
                    umwriter.write("\r\n" + user + " " + movie + " ");
                    umwriter.flush();
                } else {
                    umwriter.write(movie + " ");
                    umwriter.flush();
                }
                lastuser = user;
                line = reader.readLine();
            }
            Collections.sort(data.movie);
            int len = data.movie.size();
            for (int i = 0; i < len; i++) {
                moviewriter.write(data.movie.get(i) + "::" + data.movie.get(i) + "\r\n");
                moviewriter.flush();
            }
            umwriter.close();
            moviewriter.close();
            tagswriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
