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
    public Integer k = 50;//k is the topic number;
    public double[][] theta;//theta is the topic distribution for each user; theta=p(z|user); theta~Dir(alpha)
    public Vector<Integer>[] z;;//z is the topic chosen for a movie; z~mul(theta)
    public int[][] ztag;//ztag is the topic chosen for a tag; ztag~uni(z)
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
    public Estimate estimate;

    public CorrLDA() {
        Model movielens = new Model();
        initialize(movielens);
    }

    public static void main(String args[]) {
    }

    public void initialize(Model model) {
        System.out.println("******************************\nThe model is initializing.\n******************************\n");
        model.initialize();
        
        //initial count
        movielen = model.itemlen;
        userlen = model.userlen;
        taglen = model.taglen;
        nz_u = new int[userlen][k];
        nm_z = new int[movielen][k];
        nt_z = new int[taglen][k];
        nsumz_u = new int[userlen];
        nsumm_z = new int[k];
        nsumt_z = new int[k];
        
        
        for (int i = 0; i < userlen; i++) {
            for (int j = 0; j < k; j++) {
                nz_u[i][j] = 0;
            }
        }
        for (int i = 0; i < movielen; i++) {
            for (int j = 0; j < k; j++) {
                nm_z[i][j] = 0;
            }
        }
        for (int i = 0; i < taglen; i++) {
            for (int j = 0; j < k; j++) {
                nt_z[i][j] = 0;
            }
        }
        for (int i = 0; i < k; i++) {
            nsumm_z[i] = 0;
            nsumt_z[i] = 0;
        }
        for (int i = 0; i < userlen; i++) {
            nsumz_u[i] = 0;
        }
        
    }
}
