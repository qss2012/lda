/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package corrlda;

/**
 *
 * @author kaldr
 */
public class Estimate {

    public CorrLDA corrlda;

    public void estimate(CorrLDA corrlda) {
        System.out.println("***************************\nSampling " + corrlda.nitter + " iteration!\n***************************");
        for (int iter = 1; iter < corrlda.nitter + 1; iter++) {
            System.out.println("Iteration " + iter + " ...");
            for (int u = 0; u < corrlda.userlen; u++) {
                int movieForU = corrlda.movielens.userData.userid2doc.get(u).size();
                for (int m = 0; m < movieForU; m++) {
                    int topic = samplingMovieTopic(u, m);
                    corrlda.z[u].set(m, topic);
                }

                if (corrlda.movielens.tagData.user2tag.containsKey(u)) {
                    int tagForU = corrlda.movielens.tagData.user2tag.get(u).size();
                    for (int t = 0; t < tagForU; t++) {
                        int topic = samplingTagTopic(u, t, movieForU);
                        corrlda.ztag[u].set(t, topic);
                    }
                }

            }
        }
        System.out.println("Gibbs sampling completed!\n");
        System.out.println("Saving the final model!\n");
        computeTheta();
        computePhi();
        computeDigamma();
    }

    public int samplingMovieTopic(int u, int m) {

        int topic = corrlda.z[u].get(m);

        int movie = Integer.parseInt(corrlda.movielens.userData.userid2doc.get(u).get(m).toString());
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
        corrlda.nm_z[movie][topic] += 1;
        corrlda.nsumm_z[topic] += 1;
        corrlda.nz_u[u][topic] += 1;
        corrlda.nsumz_u[u] += 1;
        return topic;
    }

    public int samplingTagTopic(int u, int t, int movie) {
        int topic = corrlda.ztag[u].get(t);

        int tag = Integer.parseInt(corrlda.movielens.tagData.user2tag.get(u).get(t).toString());
        corrlda.nt_z[tag][topic] -= 1;
        corrlda.nsumt_z[topic] -= 1;

        double Tgamma = corrlda.taglen * corrlda.gamma;
        double[] p = new double[corrlda.K];
        for (int k = 0; k < corrlda.K; k++) {
            p[k] = corrlda.nm_z[u][k] / movie * (corrlda.nz_u[u][k] + corrlda.alpha) / (corrlda.nsumz_u[u] + Tgamma);
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
        corrlda.nt_z[tag][topic] += 1;
        corrlda.nsumt_z[topic] += 1;
        return topic;
    }

    public void computePhi() {
        for (int m = 0; m < corrlda.movielen; m++) {
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.fine[m][k] = (corrlda.nm_z[m][k] + corrlda.beta) / (corrlda.nsumm_z[k] + corrlda.movielen * corrlda.beta);
            }
        }
    }

    public void computeTheta() {
        for (int u = 0; u < corrlda.userlen; u++) {
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.theta[u][k] = (corrlda.nz_u[u][k] + corrlda.alpha) / (corrlda.nsumz_u[u] + corrlda.K * corrlda.alpha);
            }
        }
    }

    public void computeDigamma() {
        for (int t = 0; t < corrlda.taglen; t++) {
            for (int k = 0; k < corrlda.K; k++) {
                corrlda.digamma[t][k] = (corrlda.nt_z[t][k] + corrlda.gamma) / (corrlda.nsumt_z[t] + corrlda.taglen * corrlda.gamma);
            }
        }
    }
}
