/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package corrlda;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author kaldr
 */
public class Estimate {
    public void estimate(CorrLDA corrlda) {
        Integer movieForU = 0;
        int topic = 0;
        Object[] userIDset = corrlda.movielens.userData.userid2doc.keySet().toArray();
        System.out.println("***************************\nSampling " + corrlda.nitter + " iteration!\n***************************");
        for (int iter = 1; iter < corrlda.nitter + 1; iter++) {
            System.out.println("Iteration " + iter + " ...");
            System.out.println(corrlda.movielens.userData.userid2doc.get(1).size());
            for (int u = 0; u < corrlda.userlen; u++) {
                int userID = Integer.parseInt(userIDset[u].toString());
                movieForU = corrlda.movielens.userData.userid2doc.get(userID).size();
                for (int m = 0; m < movieForU; m++) {
                    topic = samplingMovieTopic(u, m, corrlda);
                    corrlda.z[u].set(m, topic);

                }
                System.out.println("Movie Topic sampled.");
                if (corrlda.movielens.tagData.user2tag.containsKey(userID)) {
                    int tagForU = corrlda.movielens.tagData.user2tag.get(userID).size();
                    for (int t = 0; t < tagForU; t++) {
                        topic = samplingTagTopic(u, t, movieForU, corrlda);
                        corrlda.ztag[u].set(t, topic);

                    }
                    System.out.println("Tag Topic sampled");
                }
                System.out.println("User " + u + " complete!");
            }
            System.out.println("Iteration " + iter + "complete !");
        }
        System.out.println("Gibbs sampling completed!\n");
        System.out.println("Saving the final model!\n");
        computeTheta(corrlda);
        computePhi(corrlda);
        computeDigamma(corrlda);
        saveTheta(corrlda);
        savePhi(corrlda);
        saveDigamma(corrlda);
    }

    public void saveTheta(CorrLDA corrlda) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("Theta.dat"), "UTF-8"));
            for(int u=0;u<corrlda.userlen;u++){
                for(int k=0;k<corrlda.K;u++){
                    String t=Double.toString(corrlda.theta[u][k]);
                    writer.write(t+"\t");
                }
                writer.write("\r\n");
            }
        } catch (Exception e) {
        }
    }

    public void savePhi(CorrLDA corrlda) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("Phi.dat"), "UTF-8"));
            for(int u=0;u<corrlda.userlen;u++){
                for(int k=0;k<corrlda.K;u++){
                    String t=Double.toString(corrlda.theta[u][k]);
                    writer.write(t+"\t");
                }
                writer.write("\r\n");
            }
        } catch (Exception e) {
        }
    }

    public void saveDigamma(CorrLDA corrlda) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("Digamma.dat"), "UTF-8"));
            for(int u=0;u<corrlda.userlen;u++){
                for(int k=0;k<corrlda.K;u++){
                    String t=Double.toString(corrlda.theta[u][k]);
                    writer.write(t+"\t");
                }
                writer.write("\r\n");
            }
        } catch (Exception e) {
        }
    }

    public int samplingMovieTopic(int u, int m, CorrLDA corrlda) {
        Object[] userIDset = corrlda.movielens.userData.userid2doc.keySet().toArray();
        int userID = Integer.parseInt(userIDset[u].toString());
        int topic = corrlda.z[u].get(m);

        int movier = Integer.parseInt(corrlda.movielens.userData.userid2doc.get(userID).get(m).toString());
        int movie = Integer.parseInt(corrlda.movielens.itemData.id_idr.get(movier).toString());
        corrlda.nm_z[movie][topic] -= 1;
        corrlda.nsumm_z[topic] -= 1;
        corrlda.nz_u[u][topic] -= 1;
        corrlda.nsumz_u[u] -= 1;

        double Vbeta = corrlda.movielen * corrlda.beta;
        double Kalpha = corrlda.K * corrlda.alpha;
        double[] p = new double[corrlda.K];
        for (int k = 0; k < corrlda.K; k++) {
            p[k] = (corrlda.nm_z[movie][k] + corrlda.beta) / (corrlda.nsumm_z[k] + Vbeta)
                    * (corrlda.nz_u[u][k] + corrlda.alpha) / (corrlda.nsumz_u[u] + Kalpha);
        }
        for (int k = 1; k < corrlda.K; k++) {
            p[k] += p[k - 1];
        }
        double sample = Math.random() * p[corrlda.K - 1];
        for (topic = 0; topic < corrlda.K; topic++) {
            if (p[topic] > sample) {
                break;
            }
        }
        if (topic == corrlda.K) {
            topic -= 1;
        }
        corrlda.nm_z[movie][topic] += 1;
        corrlda.nsumm_z[topic] += 1;
        corrlda.nz_u[u][topic] += 1;
        corrlda.nsumz_u[u] += 1;
        return topic;
    }

    public int samplingTagTopic(int u, int t, int movie, CorrLDA corrlda) {
        Object[] userIDset = corrlda.movielens.userData.userid2doc.keySet().toArray();
        int userID = Integer.parseInt(userIDset[u].toString());
        int topic = corrlda.ztag[u].get(t);

        int tag = Integer.parseInt(corrlda.movielens.tagData.user2tag.get(userID).get(t).toString());
        corrlda.nt_z[tag][topic] -= 1;
        corrlda.nsumt_z[topic] -= 1;

        double Tgamma = corrlda.taglen * corrlda.gamma;
        double[] p = new double[corrlda.K];
        for (int k = 0; k < corrlda.K; k++) {
            p[k] = corrlda.nz_u[u][k] / movie * (corrlda.nt_z[t][k] + corrlda.gamma) / (corrlda.nsumt_z[k] + Tgamma);
        }
        for (int k = 1; k < corrlda.K; k++) {
            p[k] += p[k - 1];
        }
        double sample = Math.random() * p[corrlda.K - 1];
        for (topic = 0; topic < corrlda.K; topic++) {
            if (p[topic] > sample) {
                break;
            }
        }
        if (topic == corrlda.K) {
            topic -= 1;
        }
        corrlda.nt_z[tag][topic] += 1;
        corrlda.nsumt_z[topic] += 1;
        return topic;
    }

    public void computePhi(CorrLDA corrlda) {
        for (int m = 0; m < corrlda.movielen; m++) {
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.fine[m][k] = (corrlda.nm_z[m][k] + corrlda.beta) / (corrlda.nsumm_z[k] + corrlda.movielen * corrlda.beta);
            }
        }
    }

    public void computeTheta(CorrLDA corrlda) {
        for (int u = 0; u < corrlda.userlen; u++) {
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.theta[u][k] = (corrlda.nz_u[u][k] + corrlda.alpha) / (corrlda.nsumz_u[u] + corrlda.K * corrlda.alpha);
            }
        }
    }

    public void computeDigamma(CorrLDA corrlda) {
        for (int t = 0; t < corrlda.taglen; t++) {
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.digamma[t][k] = (corrlda.nt_z[t][k] + corrlda.gamma) / (corrlda.nsumt_z[t] + corrlda.taglen * corrlda.gamma);
            }
        }
    }
}
