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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author kaldr
 */
public class SampleData {

    ArrayList users;
    Vector movies;

    public SampleData() {
        movies = new Vector();
        users = new ArrayList();
    }

    public static void main(String args[]) {
        SampleData sampledata = new SampleData();
        sampledata.SampleUser("userwithmovies.dat");
        sampledata.CheckMovieAmount(sampledata);
        sampledata.SampleTags("tags.dat");
    }

    public void CheckMovieAmount(SampleData sampledata) {
        try {
            System.out.println("Start Checking...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("Sampled/userwithmovies.dat"), "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                StringTokenizer tknz = new StringTokenizer(line, " ");
                String user = tknz.nextToken();
                if (!tknz.hasMoreTokens()) {
                    System.out.println("Not correct, resampling...");
                    sampledata.SampleUser("userwithmovies.dat");
                    sampledata.CheckMovieAmount(sampledata);
                    break;
                }
                line = reader.readLine();
            }
            System.out.println("Totally correct.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void SampleUser(String filename) {
        int maxMovie=0;
        try {
            System.out.println("Start Sampling user...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            BufferedReader Moviereader = new BufferedReader(new InputStreamReader(new FileInputStream("movies.dat"), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Sampled/userwithmovies.dat"), "UTF-8"));
            BufferedWriter Moviewriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Sampled/movies.dat"), "UTF-8"));
            String line = reader.readLine();
            String movieline = Moviereader.readLine();
            StringTokenizer tknz;
            //Choose half of the movies
            while (movieline != null) {
                double randnum = Math.random();
                if (randnum < 0.3) {
                    
                    tknz = new StringTokenizer(movieline,"::");
                    String movieid = tknz.nextToken();
                    String moviename=tknz.nextToken();
                    String moviegenre=tknz.nextToken();
                    maxMovie=Integer.parseInt(movieid);
                    movies.add(maxMovie);
                   
                    Moviewriter.write(movieid+"::"+moviename+"::"+moviegenre);
                    Moviewriter.flush();
                    Moviewriter.newLine();
                }
                movieline = Moviereader.readLine();
            }
            System.out.println("Last movie is "+maxMovie);
            System.out.println("Size is "+movies.size());
            
            while (line != null) {
                double randnum = Math.random();
                if (randnum < 0.05) {
                    tknz = new StringTokenizer(line, " ");
                    String user = tknz.nextToken();
                    users.add(Integer.parseInt(user));
                    writer.write(user + " ");
                    while (tknz.hasMoreTokens()) {
                        String movieStr = tknz.nextToken();
                        int movie = Integer.parseInt(movieStr);
                        
                        if (movie<=maxMovie) {
                            
                            if(movies.contains(movie)){
                                if(movie>maxMovie)
                                System.out.println("Wrong Writing "+movie);
                            writer.write(movie + " ");
                            }
                        }else{
                            System.out.println(movie+" is bigger");
                        }
                    }
                    writer.write("\r\n");
                }
                line = reader.readLine();
            }
             
            reader.close();
            writer.close();
            System.out.println("Sampled");
            Collections.sort(users);
            Collections.sort( movies);
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void SampleTags(String filename) {
        try {
            System.out.println("Start Sampling tags...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Sampled/tags.dat"), "UTF-8"));
            String line = reader.readLine();
            StringTokenizer tknz;
            String user;
            String movie;
            int i = 0;
            int n = 0;
            int j = 0;
            int m = 0;
            int k = 0;
            int d = 0;
            while (line != null) {

                tknz = new StringTokenizer(line, "::");
                user = tknz.nextToken();
                movie = tknz.nextToken();
                for (i = n; i < users.size(); i++) {
                    if (Integer.parseInt(users.get(i).toString()) < Integer.parseInt(user)) {
                        continue;
                    } else if (Integer.parseInt(users.get(i).toString()) == Integer.parseInt(user)) {
                        n = i;
                        m = 0;
                        for (j = m; j < movies.size(); j++) {
                            if (Integer.parseInt(movies.get(j).toString()) < Integer.parseInt(movie)) {
                                continue;
                            } else if (Integer.parseInt(movies.get(j).toString()) == Integer.parseInt(movie)) {
                                writer.write(line + "\r\n");
                                m = j;
                                break;
                            } else if (Integer.parseInt(movies.get(j).toString()) > Integer.parseInt(movie)) {
                                m = j;
                                break;
                            }
                        }
                        break;
                    } else if (Integer.parseInt(users.get(i).toString()) > Integer.parseInt(user)) {
                        m = j;
                        break;
                    }
                }
                line = reader.readLine();
            }

            reader.close();
            writer.close();
            System.out.println("Sampled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
