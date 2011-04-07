/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
        int i;
        
        for(i=0;i<50;i++){
            if(i%5==0)
                System.out.println(i);
        }
        
    }
}


