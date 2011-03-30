/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 *
 * @author kaldr
 */
public class SampleData {

    ArrayList users;
    ArrayList movies;

    public SampleData() {
        movies = new ArrayList();
        users = new ArrayList();
    }

    public static void main(String args[]) {
        SampleData sampledata = new SampleData();
        sampledata.SampleUser("userwithmovies.dat");
    }
    
    public void SampleUser(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            BufferedReader Moviereader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Sampled/userwithmovies.dat"), "UTF-8"));
            BufferedWriter Moviewriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Sampled/movies.dat"), "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                double randnum = Math.random();
                if (randnum < 0.05) {
                    StringTokenizer tknz = new StringTokenizer(line, " ");
                    String user = tknz.nextToken();
                    users.add(user);
                    while (tknz.hasMoreTokens()) {
                        String movie = tknz.nextToken();
                        System.out.println(movie);
                        if (!movies.contains(movie)) {
                            movies.add(movie);
                        }
                    }
                    writer.write(line + "\r\n");
                }
                line = reader.readLine();
            }
            Collections.sort(movies);
            System.out.println(movies.size());
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SampleTags(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Sampled/tags.dat"), "UFT-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
