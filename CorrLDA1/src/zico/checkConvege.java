/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zico;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

public class checkConvege {

    public static void main(String[] args) throws Exception {

        File root = new File("/home/kaldr/NetBeansProjects/CorrLDA/src/Datasets/Movielens/Sampled");
        showAllFiles(root);
    }

    final static void showAllFiles(File dir) throws Exception {
        File[] fs = dir.listFiles();
        String filename = "";
        String type = "";
        String filesub = "";
        String no = "";
        StringTokenizer tknz;
        HashMap theta = new HashMap<Integer, double[][]>();
        double[][] thetamatrix = new double[3483][50];
        for (int i = 0; i < fs.length; i++) {
            filename = fs[i].getName().toString();
            tknz = new StringTokenizer(filename, "._");
            if (tknz.countTokens() == 3) {
                type = tknz.nextToken();
                filesub = tknz.nextToken();
                no = tknz.nextToken();
                if (type.equals("Theta")) {
                    if (Integer.parseInt(no) >= 3000) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/home/kaldr/NetBeansProjects/CorrLDA/src/Datasets/Movielens/Sampled/" + filename), "UTF-8"),6553409);
                       
                        System.out.println(filename);
                        for (int k = 0; k < 3483; k++) {

                            String line = reader.readLine();
                            
                            if (line != null) {
                                StringTokenizer t = new StringTokenizer(line, " \t\r\n");
                                if (t.countTokens() == 50) {
                                    for (int j = 0; j < 50; j++) {
                                        thetamatrix[k][j] = Double.parseDouble(t.nextToken().toString());
                                        //System.out.println(thetamatrix[k][j]);
                                    }
                                }else{
                                    System.out.println(line);
                                    System.out.println(k+" has "+t.countTokens());
                                }
                            }

                        }
                        theta.put(Integer.parseInt(no), thetamatrix);
                        reader.close();
                    }
                }
            }

        }
    }
}
