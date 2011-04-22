/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package corrlda;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author Administrator
 */
public class AUC {
    public AUC(){
        
    }
    public double computeAUC(CorrLDA corrlda) {
        double auc = 0;
        double score[][] = computeScore(corrlda);
        HashMap<Integer, ArrayList> testuser2item = getTestData(); //Get the  user item list in the test data;
        HashMap<Integer,ArrayList> trainuser2item=corrlda.movielens.userData.userid2doc;
        HashMap<Integer, Integer> userID2i = corrlda.movielens.userData.id_idr;
        HashMap<Integer,Integer> movieID2i=corrlda.movielens.itemData.id_idr;
        
        int auctime=1000;
        int testuserid;
        int testitemid;
        double testuserrand;
        double testitemrand;
        double trainuserrand;
        double trainitemrand;
        for(int i=0;i<auctime;i++){
            testuserrand=Math.random();
            testuserid=Math.ceil(testuserrand*);
            
            auc+=0;
        }

        
        return auc;
    }
    public HashMap<Integer,ArrayList> getOutData(CorrLDA corrlda,HashMap<Integer,ArrayList> testuser2item){
        HashMap<Integer, ArrayList> user2item=new HashMap<Integer, ArrayList>();
        HashMap<Integer,ArrayList> trainuser2item=corrlda.movielens.userData.userid2doc;
        Object[] testuserset=testuser2item.keySet().toArray();
        Object[] trainuserset=corrlda.movielens.userData.id_idr.keySet().toArray();
        Object[] movieset=corrlda.movielens.itemData.id_idr.keySet().toArray();
        int movielen = movieset.length;
        int testuserlen=testuserset.length;
        int trainuserlen=trainuserset.length;       
        


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
            for (int j = 0; j < movielen; j++) {
                score[i][j] = 0;
                for (int k = 0; k < topic; k++) {
                    for (int t = 0; t < taglen; t++) {
                        score[i][j] += corrlda.theta[i][k] * corrlda.fine[j][k] * corrlda.digamma[t][k];
                    }
                }
            }
        }
        return score;
    }
}
