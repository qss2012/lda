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
                    int topic = sampling(u, m, "movie");
                    corrlda.z[u].set(m, topic);
                }
            }
        }
        System.out.println("Gibbs sampling completed!\n");
        System.out.println("Saving the final model!\n");
        computeTheta();
        computePhi();
        computeDigamma();
    }

    public int sampling(int u, int m, String type) {

        int topic = corrlda.z[u].get(m);
        if (type.equals("movie")) {
            int movie = Integer.parseInt(corrlda.movielens.userData.userid2doc.get(u).get(m).toString());
            corrlda.nm_z[movie][topic] -= 1;
            corrlda.nsumm_z[topic] -= 1;
            corrlda.nz_u[u][topic] -= 1;
            corrlda.nsumz_u[u] -= 1;

            double Vbeta=corrlda.movielen*corrlda.beta;
            double Kalpha=corrlda.K*corrlda.alpha;
            double[] p=new double[corrlda.K];
            for(int k=0;k<corrlda.K;k++){
                p[k]=(corrlda.nm_z[movie][k] + corrlda.beta)/(corrlda.nsumm_z[k] + Vbeta) *
					(corrlda.nz_u[u][k] + corrlda.alpha)/(corrlda.nsumz_u[u] + Kalpha);
            }
            for(int k=1;k<corrlda.K;k++){
                p[k]+=p[k-1];
            }
            double sample=Math.random()*p[corrlda.K-1];
            for(topic=0;topic<corrlda.K;topic++){
                if(p[topic]>sample)
                    break;
            }
        } else if (type.equals("tag")) {
            int t=m;
            int tag=Integer.parseInt(corrlda.movielens.tagData.user2tag.get(u).get(t).toString());
            
        }
        return topic;
    }

    public void computePhi() {

    }

    public void computeTheta() {
    }

    public void computeDigamma() {
    }
}
