/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package corrlda;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author Administrator
 */
public class AUC {

    public AUC() {
    }

    public double computeAUC(CorrLDA corrlda) {
        double auc = 0;
        double score[][] = computeScore(corrlda);
        HashMap<Integer, ArrayList> testuser2item = getTestData(); //Get the  user item list in the test data;
        HashMap<Integer, ArrayList> trainuser2item = corrlda.movielens.userData.userid2doc;
        HashMap<Integer, Integer> user2ID = corrlda.movielens.userData.id_idr;
        HashMap<Integer, Integer> item2ID = corrlda.movielens.itemData.id_idr;
        HashMap<Integer, Integer> userID2i = corrlda.movielens.userData.idr_id;
        HashMap<Integer, Integer> itemID2i = corrlda.movielens.itemData.idr_id;
        Object[] testuserset = testuser2item.keySet().toArray();

        int auctime = 1;

        int userno = userID2i.size();
        int itemno = itemID2i.size();

        int testuserno = testuser2item.size();
        int outuser;
        int outuserid;
        int outitem;
        int outitemid;

        int testuser;
        int testuserid;
        int testitem;
        int testitemid;
        double testScore;
        double outScore;
        for (int i = 0; i < auctime; i++) {
            if (i % 50 == 0) {
                System.out.println(i);
            }
            testuserid = (int) Math.floor(Math.random() * testuserno);
            testuser = Integer.parseInt(testuserset[testuserid].toString());//*
            ArrayList testUserItems = testuser2item.get(testuser);
            int testUserItemSize = testUserItems.size();
            testitemid = (int) Math.floor(Math.random() * testUserItemSize);
            testitem = Integer.parseInt(testUserItems.get(testitemid).toString());//*
            int tuser = user2ID.get(testuser);
            int titem = item2ID.get(testitem);
            testScore = score[tuser][titem];//*

            outuserid = (int) Math.floor(Math.random() * userno);//**
            outuser = userID2i.get(outuserid);
            outitemid = (int) Math.floor(Math.random() * itemno);//**
            outitem = itemID2i.get(outitemid);
            ArrayList testfinditem = testuser2item.get(outuser);
            ArrayList trainfinditem = trainuser2item.get(outuser);
            while (testfinditem.contains(outitem) || trainfinditem.contains(outitem)) {
                outitemid = (int) Math.floor(Math.random() * itemno);//**
                outitem = itemID2i.get(outitemid);
            }
            outScore = score[outuser][outitem];
            if (testScore > outScore) {
                auc += 1;
            } else if (testScore == outScore) {

                auc += 0.5;
            }
        }
        auc /= auctime;
        return auc;
    }

    public HashMap<Integer, ArrayList> getOutData(CorrLDA corrlda, HashMap<Integer, ArrayList> testuser2item) {
        HashMap<Integer, ArrayList> user2item = new HashMap<Integer, ArrayList>();
        HashMap<Integer, ArrayList> trainuser2item = corrlda.movielens.userData.userid2doc;
        Object[] testuserset = testuser2item.keySet().toArray();
        Object[] trainuserset = corrlda.movielens.userData.id_idr.keySet().toArray();
        Object[] movieset = corrlda.movielens.itemData.id_idr.keySet().toArray();
        int movielen = movieset.length;
        int testuserlen = testuserset.length;
        int trainuserlen = trainuserset.length;



        return user2item;
    }

    public HashMap<Integer, ArrayList> getTestData() {
        HashMap<Integer, ArrayList> user2item = new HashMap<Integer, ArrayList>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("test/userwithmovies.dat"), "UTF-8"));
            String line = reader.readLine();
            StringTokenizer tknz = new StringTokenizer(line, " ");
            int user = Integer.parseInt(tknz.nextToken());
            int item;
            while (line != null) {
                ArrayList a = new ArrayList();
                user2item.put(user, a);
                while (tknz.hasMoreTokens()) {
                    item = Integer.parseInt(tknz.nextToken());
                    a.add(item);
                }
                line = reader.readLine();
                tknz = new StringTokenizer(line, " ");
                user = Integer.parseInt(tknz.nextToken());
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user2item;

    }

    public double[][] computeScore(CorrLDA corrlda) {
        int topic = corrlda.K;
        int userlen = corrlda.userlen;
        int movielen = corrlda.movielen;
        int taglen = corrlda.taglen;
        double[][] score = new double[userlen][movielen];
        for (int i = 0; i < userlen; i++) {
            long startTime = System.currentTimeMillis();
            if(i%Math.floor(userlen/10)==0){
                System.out.println(Math.floor(userlen/10));
            }
            for (int j = 0; j < movielen; j++) {
                score[i][j] = 0;
                for (int k = 0; k < topic; k++) {
                    for (int t = 0; t < taglen; t++) {
                        score[i][j] += corrlda.theta[i][k] + corrlda.fine[j][k] + corrlda.digamma[t][k];
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            double time = 0;
            time = time + (endTime - startTime) / 1000;
            double timeover = time * (userlen -i-1) / 60;
            System.out.println("Ecalepsed time: " + (time) + "s, Completed in " + timeover + " m");
            
        }
        return score;
    }
}
