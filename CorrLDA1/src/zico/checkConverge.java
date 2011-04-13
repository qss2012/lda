package zico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class checkConverge {

    public void checkConverge() {
    }

    public ArrayList getThetaFiles() {
        File folder = new File("/home/kaldr/NetBeansProjects/CorrLDA/src/zico/del/3000");
        File[] files = folder.listFiles();
        int fileslen = files.length;
        ArrayList fs = new ArrayList();
        StringTokenizer fstknz;
        for (int i = 0; i < fileslen; i++) {
            String filename = files[i].getName().toString();
            if (files[i].isFile()) {
                fstknz = new StringTokenizer(filename, "._");
                if (fstknz.countTokens() == 3) {
                    String name = fstknz.nextToken();
                    if (name.equals("Theta")) {
                        fs.add(filename);
                    }
                }
            }
        }
        Collections.sort(fs);
        return fs;
    }

    public ArrayList checkTheta(ArrayList fs) {
        ArrayList ac = new ArrayList();
        int user = 1723;
        int topic = 50;
        double[][] theta = new double[user][topic];
        double[][] lasttheta = new double[user][topic];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fs.get(0).toString()), "UTF-8"));
            for (int i = 0; i < user; i++) {
                String line = reader.readLine();
                StringTokenizer tknz = new StringTokenizer(line, "\t");
                for (int j = 0; j < topic; j++) {
                    theta[i][j] = Double.parseDouble(tknz.nextToken());
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        lasttheta = theta;
        
        int fslen = fs.size();
        for (int time = 1; time < fslen; time++) {
            try {
                double a = 0.0;
                theta=new double[user][topic];
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(fs.get(time).toString()), "UTF-8"));
                System.out.println(fs.get(time).toString());
                for (int i = 0; i < user; i++) {
                    String line = reader.readLine();
                    StringTokenizer tknz = new StringTokenizer(line, "\t");
                    for (int j = 0; j < topic; j++) {
                        String b = tknz.nextToken();
                        //System.out.println(b);
                        theta[i][j] = Double.parseDouble(b.toString());
                        // System.out.println(theta[i][j]);
                        //System.out.println(lasttheta[i][j]);
                        a += Math.pow(lasttheta[i][j] - theta[i][j], 2);
                        //System.out.println(a);
                    }
                }
                a=Math.sqrt(a);
                System.out.println(a);
                ac.add(a);
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            lasttheta = theta;
        }
        System.out.println(ac);
        return ac;
    }

    public static void main(String args[]) {
        checkConverge check = new checkConverge();
        ArrayList fs = new ArrayList();
        ArrayList ac = new ArrayList();
        fs = check.getThetaFiles();
        ac = check.checkTheta(fs);
    }
}
