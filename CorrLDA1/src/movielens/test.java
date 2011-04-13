/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author kaldr
 */
public class test {

    public Vector<Integer>[] z;

    public test() {
        z = new Vector[3];
    }

    public static void main(String args[]) {
        String c="Lady is baby is fucked \r\n";
        StringTokenizer tknz=new StringTokenizer(c," ");
        double a=0.323212;
        double b=0.1264545;
        System.out.println(tknz.countTokens());
    }
}


