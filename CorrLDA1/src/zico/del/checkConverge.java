/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zico.del;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 *
 * @author Administrator
 */
public class checkConverge {

    public static void main(String args[]) throws FileNotFoundException {
        File folder = new File("E:\\Research\\LDA code\\CorrLDA1\\src\\zico\\del");
        double[][] theta = new double[1723][50];
        double[][] lasttheta = new double[1723][50];
        File[] fs = folder.listFiles();

        StringTokenizer tknz;
        ArrayList fsname = new ArrayList();
        String type;
        String filetype;
        String no;
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isFile()) {
                String filename = fs[i].getName();

                tknz = new StringTokenizer(filename, "._");
                if (tknz.countTokens() == 3) {
                    type = tknz.nextToken();
                    filetype = tknz.nextToken();
                    no = tknz.nextToken();
                    if (type.equals("Theta")) {
                        fsname.add(fs[i].getName());
                    }
                }
            }
        }
        Collections.sort(fsname);
        int len = fsname.size();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fsname.get(0).toString()), "UTF-8"));
            String line = reader.readLine();
            StringTokenizer t;
            for (int user = 0; user < 1723; user++) {
                t = new StringTokenizer(line, " ");
                for (int topic = 0; topic < 50; topic++) {
                    if (t.countTokens() == 50) {
                        theta[user][topic] = Double.parseDouble(t.nextToken());
                    } else {
                        System.out.println(user);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lasttheta = theta;
        for (int i = 1; i < len; i++) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fsname.get(i).toString()), "UTF-8"));
                String line = reader.readLine();
                StringTokenizer t;

                double ac = 0;
                for (int user = 0; user < 1723; user++) {
                    t = new StringTokenizer(line, " ");
                    for (int topic = 0; topic < 50; topic++) {
                        if (t.countTokens() == 50) {
                            theta[user][topic] = Double.parseDouble(t.nextToken());
                            ac += Math.pow(theta[user][topic] - lasttheta[user][topic], 2);
                        }else{
                            System.out.println(user);
                        }
                    }
                    line = reader.readLine();
                }
                System.out.println(Math.sqrt(ac));
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
