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
        int ec = 0;
        Object[] userIDset = corrlda.movielens.userData.userid2doc.keySet().toArray();
        System.out.println("***************************\nSampling " + corrlda.nitter + " iteration!\n***************************");
        int itertimes = corrlda.nitter;
        //itertimes = 4;
        //int halfitertimes = itertimes / 2;
        int iter = 1;
        for (iter = 1; iter < itertimes + 1; iter++) {
            long startTime = System.currentTimeMillis();
            System.out.println("Iteration " + iter + " ...");
            //System.out.println(corrlda.movielens.userData.userid2doc.get(1).size());
            for (int u = 0; u < corrlda.userlen; u++) {
                int userID = Integer.parseInt(userIDset[u].toString());
                movieForU = corrlda.movielens.userData.userid2doc.get(userID).size();

                for (int m = 0; m < movieForU; m++) {
                    topic = samplingMovieTopic(u, m, corrlda);
                    corrlda.z[u].set(m, topic);
                }
                if (corrlda.movielens.tagData.user2tag.containsKey(userID)) {
                    int tagForU = corrlda.movielens.tagData.user2tag.get(userID).size();
                    for (int t = 0; t < tagForU; t++) {
                        topic = samplingTagTopic(u, t, movieForU, corrlda);
                        corrlda.ztag[u].set(t, topic);
                    }
                }
            }
            ec = ec + 1;
            long endTime = System.currentTimeMillis();
            double time = 0;
            time = time + (endTime - startTime) / 1000;
            double timeover = time * (itertimes - ec) / 60;
            System.out.println("Ecalepsed time: " + (time) + "s, Completed in " + timeover + " m");
            if (iter%50==0) {
                computeTheta(corrlda, iter);
                computePhi(corrlda, iter);
                computeDigamma(corrlda, iter);
            }
        }
        System.out.println("Gibbs sampling completed!\n");
        System.out.println("Saving the final model!\n");
        computeTheta(corrlda, iter - 1);
        computePhi(corrlda, iter - 1);
        computeDigamma(corrlda, iter - 1);
        saveModel(corrlda);
    }

    public void saveTheta(CorrLDA corrlda, int iter) {
        String file = "Theta.dat";
        String pendix = Integer.toString(iter);
        file = file + "_" + pendix;
        System.out.println("Saving theta...");
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));
            for (int u = 0; u < corrlda.userlen; u++) {
                for (int k = 0; k < corrlda.K; k++) {
                    String t = Double.toString(corrlda.theta[u][k]);
                    writer.write(t + "\t");
                    writer.flush();
                }
                writer.write("\r\n");
                writer.flush();
            }
        } catch (Exception e) {
        }
    }

    public void savePhi(CorrLDA corrlda, int iter) {
        System.out.println("Saving phi...");
        try {
            String file = "Phi.dat";
            String pendix = Integer.toString(iter);
            file = file + "_" + pendix;
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));
            for (int u = 0; u < corrlda.userlen; u++) {
                for (int k = 0; k < corrlda.K; k++) {
                    String t = Double.toString(corrlda.theta[u][k]);
                    writer.write(t + "\t");
                    writer.flush();

                }
                writer.write("\r\n");
                writer.flush();
            }
        } catch (Exception e) {
        }
    }

    public void saveDigamma(CorrLDA corrlda, int iter) {
        try {
            String file = "Digamma.dat";
            String pendix = Integer.toString(iter);
            file = file + "_" + pendix;
            System.out.println("Saving digamma...");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));
            for (int u = 0; u < corrlda.userlen; u++) {
                for (int k = 0; k < corrlda.K; k++) {
                    String t = Double.toString(corrlda.theta[u][k]);

                    writer.write(t + " ");
                    writer.flush();
                }
               writer.write("\r\n");
                writer.flush();
            }
        } catch (Exception e) {
        }
    }

    public int samplingMovieTopic(int u, int m, CorrLDA corrlda) {
        Object[] userIDset = corrlda.movielens.userData.userid2doc.keySet().toArray();
        int userID = Integer.parseInt(userIDset[u].toString());
        int topic = corrlda.z[u].get(m);//*********************************************

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
        int topic = corrlda.ztag[u].get(t);//*********************************************
        int tag = Integer.parseInt(corrlda.movielens.tagData.user2tag.get(userID).get(t).toString());
        corrlda.nt_z[tag][topic] -= 1;
        corrlda.nsumt_z[topic] -= 1;
        double movie2=(double)movie;
        double Tgamma = corrlda.taglen * corrlda.gamma;
        double[] p = new double[corrlda.K];
        for (int k = 0; k < corrlda.K; k++) {
            p[k] = corrlda.nz_u[u][k]/movie2* (corrlda.nt_z[t][k] + corrlda.gamma) / (corrlda.nsumt_z[k] + Tgamma);            
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

    public void computePhi(CorrLDA corrlda, int iter) {
        for (int m = 0; m < corrlda.movielen; m++) {
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.fine[m][k] = (corrlda.nm_z[m][k] + corrlda.beta) / (corrlda.nsumm_z[k] + corrlda.movielen * corrlda.beta);
            }
        }
        savePhi(corrlda, iter);
    }

    public void computeTheta(CorrLDA corrlda, int iter) {
        for (int u = 0; u < corrlda.userlen; u++) {
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.theta[u][k] = (corrlda.nz_u[u][k] + corrlda.alpha) / (corrlda.nsumz_u[u] + corrlda.K * corrlda.alpha);
            }
        }

        saveTheta(corrlda, iter);
    }

    public void computeDigamma(CorrLDA corrlda, int iter) {
        for (int t = 0; t < corrlda.taglen; t++) {
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.digamma[t][k] = (corrlda.nt_z[t][k] + corrlda.gamma) / (corrlda.nsumt_z[k] + corrlda.taglen * corrlda.gamma);
            }
        }
        saveDigamma(corrlda, iter);
    }

    public void saveModel(CorrLDA corrlda) {
        String file = "model.dat";
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.write(corrlda.movielen + "::" + corrlda.userlen + "::" + corrlda.taglen + "\r\n");
            writer.write("nz_u\r\n");
            for (int i = 0; i < corrlda.userlen; i++) {
                for (int j = 0; j < corrlda.K; j++) {
                    writer.write(corrlda.nz_u[i][j] + " ");
                }
                writer.write("\r\n");
            }
            writer.write("nm_z\r\n");
            for (int i = 0; i < corrlda.movielen; i++) {
                for (int j = 0; j < corrlda.K; j++) {
                    writer.write(corrlda.nm_z[i][j] + " ");
                }
                writer.write("\r\n");
            }
            writer.write("nt_z\r\n");
            for (int i = 0; i < corrlda.taglen; i++) {
                for (int j = 0; j < corrlda.K; j++) {
                    writer.write(corrlda.nt_z[i][j] + " ");
                }
                writer.write("\r\n");
            }
            writer.write("nsumz_u\r\n");
            for (int i = 0; i < corrlda.userlen; i++) {
                writer.write(corrlda.nsumz_u[i] + " ");
            }
            writer.write("\r\n");
            writer.write("nsumm_z\r\n");
            for (int i = 0; i < corrlda.K; i++) {
                writer.write(corrlda.nsumm_z[i] + " ");
            }
            writer.write("\r\n");
            writer.write("nsumt_z\r\n");
            for (int i = 0; i < corrlda.K; i++) {
                writer.write(corrlda.nsumt_z[i] + " ");
            }
            writer.write("\r\n");
            writer.write("z\r\n");
            for(int i=0;i<corrlda.userlen;i++){
                int k=corrlda.z[i].size();
                for(int j=0;j<k;j++){
                    writer.write(corrlda.z[i].get(j)+" ");
                }
                writer.write("\r\n");
            }
            writer.write("\r\n");
            writer.write("ztag\r\n");
            for(int i=0;i<corrlda.userlen;i++){
                int k=corrlda.ztag[i].size();
                for(int j=0;j<k;j++){
                    writer.write(corrlda.ztag[i].get(j)+" ");
                }
                writer.write("\r\n");
            }
            writer.write("\r\n");
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
