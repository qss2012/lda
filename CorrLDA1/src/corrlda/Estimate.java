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

    public void estimate(CorrLDA corrlda, int no) {
        Integer movieForU = 0;
        AUC auc=new AUC();
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

            if (iter % corrlda.step == 0) {
                computeTheta(corrlda, iter + no);
                computePhi(corrlda, iter + no);
                computeDigamma(corrlda, iter + no);
                //saveModel(corrlda, iter + no);
                
            }
            ec = ec + 1;
            long endTime = System.currentTimeMillis();
            double time = 0;
            time = time + (endTime - startTime) / 1000;
            double timeover = time * (itertimes - ec) / 60;
            System.out.println("Ecalepsed time: " + (time) + "s, Completed in " + timeover + " m");
        }
        System.out.println("Gibbs sampling completed!\n");
        System.out.println("Saving the final model!\n");
        computeTheta(corrlda, iter - 1 + no);
        computePhi(corrlda, iter - 1 + no);
        computeDigamma(corrlda, iter - 1 + no);
        
        //saveModel(corrlda, iter - 1 + no);
        System.out.println(auc.computeAUC(corrlda));
    }

    public double clik(CorrLDA corrlda) {
        int nitter = corrlda.nitter;
        int step = corrlda.step;
        int n = (int) Math.ceil(nitter / step);
        double likelihood = 1.0;
        Object[] userIDset = corrlda.movielens.userData.userid2doc.keySet().toArray();
        double[][] theta = corrlda.theta;
        double[][] phi = corrlda.fine;
        double[][] digamma = corrlda.digamma;
        int userlen = corrlda.movielens.userlen;
        int movielen = corrlda.movielens.itemlen;
        int taglen = corrlda.movielens.taglen;
        int K = corrlda.K;
        for (int k = 0; k < K; k++) {
            for (int u = 0; u < userlen; u++) {
                int userID = Integer.parseInt(userIDset[u].toString());
                double usermovie = (double) corrlda.movielens.userData.userid2doc.get(userID).size();
               
                for (int m = 0; m < movielen; m++) {
                    if (phi[m][k] == 0) {
                        System.out.println("0");
                    }
                    likelihood += theta[u][k]*phi[m][k];
                }
                /*
                for (int t = 0; t < taglen; t++) {
                    if (digamma[t][k] == 0) {
                        System.out.println("0");
                    }
                    //likelihood += digamma[t][k]/usermovie;
                }
                 * 
                 */
            }
        }
        System.out.println(likelihood);

        return likelihood;
    }

    public void saveTheta(CorrLDA corrlda, int iter) {
        String file = "Theta.dat";
        String pendix = Integer.toString(iter);
        if (iter < 10) {
            pendix = "000" + Integer.toString(iter);
        } else if (iter < 100) {
            pendix = "00" + Integer.toString(iter);
        } else if (iter < 1000) {
            pendix = "0" + Integer.toString(iter);
        }
        file = file + "_" + pendix;
        System.out.println("Saving theta...");
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("3000/" + file), "UTF-8"));
            writer.write(corrlda.userlen + " " + corrlda.K + "\r\n");
            writer.flush();
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
            e.printStackTrace();
        }
    }

    public void savePhi(CorrLDA corrlda, int iter) {
        System.out.println("Saving phi...");
        try {
            String file = "Phi.dat";
            String pendix = Integer.toString(iter);
            if (iter < 10) {
                pendix = "000" + Integer.toString(iter);
            } else if (iter < 100) {
                pendix = "00" + Integer.toString(iter);
            } else if (iter < 1000) {
                pendix = "0" + Integer.toString(iter);
            }
            file = file + "_" + pendix;
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("3000/" + file), "UTF-8"));
            writer.write(corrlda.movielen + " " + corrlda.K + "\r\n");
            writer.flush();
            for (int u = 0; u < corrlda.movielen; u++) {
                for (int k = 0; k < corrlda.K; k++) {
                    String t = Double.toString(corrlda.fine[u][k]);
                    writer.write(t + " ");
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
            if (iter < 10) {
                pendix = "000" + Integer.toString(iter);
            } else if (iter < 100) {
                pendix = "00" + Integer.toString(iter);
            } else if (iter < 1000) {
                pendix = "0" + Integer.toString(iter);
            }
            file = file + "_" + pendix;
            System.out.println("Saving digamma...");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("3000/" + file), "UTF-8"));
            writer.write(corrlda.taglen + " " + corrlda.K + "\r\n");
            writer.flush();
            for (int u = 0; u < corrlda.taglen; u++) {
                for (int k = 0; k < corrlda.K; k++) {
                    String t = Double.toString(corrlda.digamma[u][k]);
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
        double movie2 = (double) movie;
        double Tgamma = corrlda.taglen * corrlda.gamma;
        double[] p = new double[corrlda.K];
        for (int k = 0; k < corrlda.K; k++) {
            p[k] = corrlda.nz_u[u][k] / movie2 * (corrlda.nt_z[t][k] + corrlda.gamma) / (corrlda.nsumt_z[k] + Tgamma);
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
        for (int k = 0; k < corrlda.K; k++) {
            double sum=0;
            for (int t = 0; t < corrlda.movielen; t++) {
                sum+=corrlda.fine[t][k];
            }
            for (int t = 0; t < corrlda.movielen; t++) {
                corrlda.fine[t][k]/=sum;
            }
        }
           
          
        // savePhi(corrlda, iter);
    }

    public void computeTheta(CorrLDA corrlda, int iter) {
        for (int u = 0; u < corrlda.userlen; u++) {
            double sum = 0;
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.theta[u][k] = (corrlda.nz_u[u][k] + corrlda.alpha) / (corrlda.nsumz_u[u] + corrlda.K * corrlda.alpha);
                sum += corrlda.theta[u][k];
            }
            
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.theta[u][k] /= sum;
            }
             

        }
        

        //  saveTheta(corrlda, iter);
    }

    public void computeDigamma(CorrLDA corrlda, int iter) {

        for (int t = 0; t < corrlda.taglen; t++) {

            for (int k = 0; k < corrlda.K; k++) {
                corrlda.digamma[t][k] = (corrlda.nt_z[t][k] + corrlda.gamma) / (corrlda.nsumt_z[k] + corrlda.taglen * corrlda.gamma);
            }
        }
        
        for (int k = 0; k < corrlda.K; k++) {
            double sum=0;
            for (int t = 0; t < corrlda.taglen; t++) {
                sum+=corrlda.digamma[t][k];
            }
            for (int t = 0; t < corrlda.taglen; t++) {
                corrlda.digamma[t][k]/=sum;
            }
        }
         
        // saveDigamma(corrlda, iter);
    }

    public void saveModel(CorrLDA corrlda, int iter) {
        System.out.println("Saving model...");
        String pendix = "";
        if (iter < 10) {
            pendix = "000" + Integer.toString(iter);
        } else if (iter < 100) {
            pendix = "00" + Integer.toString(iter);
        } else if (iter < 1000) {
            pendix = "0" + Integer.toString(iter);
        }
        String file = "model.dat_" + pendix;
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("3000/" + file), "UTF-8"));
            writer.write(corrlda.movielen + "::" + corrlda.userlen + "::" + corrlda.taglen + "\r\n");
            writer.flush();
            writer.write("nz_u\r\n");
            writer.flush();
            for (int i = 0; i < corrlda.userlen; i++) {
                for (int j = 0; j < corrlda.K; j++) {
                    writer.write(corrlda.nz_u[i][j] + " ");
                    writer.flush();
                }
                writer.write("\r\n");
                writer.flush();
            }
            writer.write("nm_z\r\n");
            writer.flush();
            for (int i = 0; i < corrlda.movielen; i++) {
                for (int j = 0; j < corrlda.K; j++) {
                    writer.write(corrlda.nm_z[i][j] + " ");
                    writer.flush();
                }
                writer.write("\r\n");
                writer.flush();
            }
            writer.write("nt_z\r\n");
            writer.flush();
            for (int i = 0; i < corrlda.taglen; i++) {
                for (int j = 0; j < corrlda.K; j++) {
                    writer.write(corrlda.nt_z[i][j] + " ");
                    writer.flush();
                }
                writer.write("\r\n");
                writer.flush();
            }
            writer.write("nsumz_u\r\n");
            writer.flush();
            for (int i = 0; i < corrlda.userlen; i++) {
                writer.write(corrlda.nsumz_u[i] + " ");
                writer.flush();
            }
            writer.write("\r\n");
            writer.flush();
            writer.write("nsumm_z\r\n");
            writer.flush();
            for (int i = 0; i < corrlda.K; i++) {
                writer.write(corrlda.nsumm_z[i] + " ");
                writer.flush();
            }
            writer.write("\r\n");
            writer.flush();
            writer.write("nsumt_z\r\n");
            writer.flush();
            for (int i = 0; i < corrlda.K; i++) {
                writer.write(corrlda.nsumt_z[i] + " ");
                writer.flush();
            }
            writer.write("\r\n");
            writer.flush();
            writer.write("z\r\n");
            writer.flush();
            for (int i = 0; i < corrlda.userlen; i++) {
                int k = corrlda.z[i].size();
                for (int j = 0; j < k; j++) {
                    writer.write(corrlda.z[i].get(j) + " ");
                    writer.flush();
                }
                writer.write("\r\n");
                writer.flush();
            }
            writer.write("\r\n");
            writer.flush();
            writer.write("ztag\r\n");
            writer.flush();
            for (int i = 0; i < corrlda.userlen; i++) {
                int k = corrlda.ztag[i].size();
                for (int j = 0; j < k; j++) {
                    writer.write(corrlda.ztag[i].get(j) + " ");
                    writer.flush();
                }
                writer.write("\r\n");
                writer.flush();
            }
            writer.write("\r\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
