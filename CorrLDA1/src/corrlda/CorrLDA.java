/*
 * This programme is Correlative LDA 1
 * The original model is in the paper: Statistical entity-topic models
 */
package corrlda;

import java.util.Vector;

/**
 *
 * @author kaldr
 */
public final class CorrLDA {
    /*
     * Model requirement
     */
    //Parameters

    public double alpha = 0.1;
    public double beta = 0.1;
    public double gamma = 0.1;
    public Integer K = 50;//k is the topic number;
    public double[][] theta;//theta is the topic distribution for each user; theta=p(z|user); theta~Dir(alpha)
    public Vector<Integer>[] z;//z is the topic chosen for a movie; z~mul(theta)
    public Vector<Integer>[] ztag;//ztag is the topic chosen for a tag; ztag~uni(z)
    public double[][] fine;//fine is p(movie|z); fine~Dir(beta)
    public double[][] digamma;//diggamma is p(tag|ztag); diggama~Dir(gamma)
    //Counts
    public int movielen;
    public int userlen;
    public int taglen;
    public int[][] nz_u;//number of times that topic z has occured in user u;
    public int[][] nm_z;//number of times the movie m is assigned to topic z;
    public int[][] nt_z;//number of times tag t is generated from topic z;
    public int[] nsumz_u;//sum of all the topics occured in user u;
    public int[] nsumm_z;//sum of all the movies assigned to topic k;
    public int[] nsumt_z;//sum of all the tags assigned to topic k;
    //configuration
    public int nitter = 2000;
    

    public CorrLDA() {
        Model movielens = new Model();
        initialize(movielens);
    }

    public static void main(String args[]) {
        Estimate estimate=new Estimate();
        CorrLDA corrlda=new CorrLDA();
        estimate.estimate(corrlda);
    }

    public void initialize(Model model) {
        System.out.println("******************************\nThe model is initializing.\n******************************\n");
        model.initialize();

        //initial count
        movielen = model.itemlen;
        userlen = model.userlen;
        taglen = model.taglen;
        nz_u = new int[userlen][K];
        nm_z = new int[movielen][K];
        nt_z = new int[taglen][K];
        nsumz_u = new int[userlen];
        nsumm_z = new int[K];
        nsumt_z = new int[K];


        for (int i = 0; i < userlen; i++) {
            for (int j = 0; j < K; j++) {
                nz_u[i][j] = 0;
            }
        }
        for (int i = 0; i < movielen; i++) {
            for (int j = 0; j < K; j++) {
                nm_z[i][j] = 0;
            }
        }
        for (int i = 0; i < taglen; i++) {
            for (int j = 0; j < K; j++) {
                nt_z[i][j] = 0;
            }
        }
        for (int i = 0; i < K; i++) {
            nsumm_z[i] = 0;
            nsumt_z[i] = 0;
        }
        for (int i = 0; i < userlen; i++) {
            nsumz_u[i] = 0;
        }
        //initial topic/z for movies and tags;
        z = new Vector[userlen];
        ztag = new Vector[userlen];
        Object[] userIDset = model.userData.userid2doc.keySet().toArray();
        for (int u = 0; u < userlen; u++) {//i is user
            int userID = Integer.parseInt(userIDset[u].toString());
            int M = model.userData.userid2doc.get(userID).size();//number of movies of a user;
            int T = 0;
            if (model.tagData.user2tag.containsKey(userID)) {
                T = model.tagData.user2tag.get(userID).size();
            }
            for (int m = 0; m < M; m++) {
                int topic = (int) Math.floor(Math.random() * K);
                int movieID = Integer.parseInt(model.userData.userid2doc.get(userID).get(m).toString());
                z[u].add(topic);
                //number of topic occured in user u
                nz_u[u][topic] += 1;
                nsumz_u[topic] += 1;
                //number of movie assigned to topic
                nm_z[movieID][topic] += 1;
                nsumm_z[topic] += 1;
            }
            if (T > 0) {
                for (int t = 0; t < T; t++) {
                   int zID=(int) Math.floor(Math.random()*M);
                   int topic= z[u].get(zID);
                   ztag[u].add(topic);
                   //number of tag assigend to topic
                   nt_z[u][topic]+=1;
                   nsumt_z[topic]+=1;
                }
            }
        }


    }
}
