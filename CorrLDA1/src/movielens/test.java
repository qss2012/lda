/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

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
        double a;
        test test = new test();
        test.z[1] = new Vector();
        test.z[1].add(12);
        test.z[1].add(3);
        System.out.println(test.z[1].contains(11));
    }
}


