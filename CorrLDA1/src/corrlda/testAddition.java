/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package corrlda;

/**
 *
 * @author Administrator
 */
public class testAddition {
    public static void main(String args[]){
        int topic = 50;
        int userlen = 1723;
        int movielen = 8073;
        int taglen = 2688;
        double[][] score = new double[userlen][movielen];
        
        for (int i = 0; i < userlen; i++) {
            long startTime = System.currentTimeMillis();
            
            for (int j = 0; j < movielen; j++) {
                score[i][j] = 0;
                for (int k = 0; k < topic; k++) {
                    for (int t = 0; t < taglen; t++) {
                        score[i][j] += 7.876+10.212+8.1212;
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            double time = 0;
            time = time + (endTime - startTime) / 1000;
            double timeover = time * (userlen - i - 1) / 60;
            System.out.println("Ecalepsed time: " + (time) + "s, Completed in " + timeover + " m");

        }
    }
}
