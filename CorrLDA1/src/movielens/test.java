/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author kaldr
 */
public class test {
    HashMap<Integer,Integer> a;
    public Vector<Integer>[] z;

    public test() {
        z = new Vector[3];
        a=new HashMap<Integer,Integer>();
    }

    public static void main(String args[]) {
        test test = new test();
        test.a.put(3,3);
        System.out.println(test.a.get(1));

    }
}


