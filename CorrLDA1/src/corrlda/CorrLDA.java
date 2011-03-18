/*
 * This programme is Correlative LDA 1
 * The original model is in the paper: Statistical entity-topic models
 */

package corrlda;


/**
 *
 * @author kaldr
 */
public class CorrLDA {
    //Parameters
    public double alpha;
    public double beta;
    public double gamma;
    public Integer k;//k is the topic number;    
    public double[][] theta;//theta is the topic distribution for each user; theta=p(z|user); theta~Dir(alpha)
    public int[][] z;//z is the topic chosen for a movie; z~mul(theta)
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

    
    public CorrLDA(){
        alpha=0.1;
        beta=0.1;
        gamma=0.1;
        k=50;        
    }
    public void initialize(Model model){
        System.out.println("************************\nThe model is initializing.\n************************\n");
        model.initialize();
        userlen=model.userlen;
        taglen=model.taglen;
        movielen=model.itemlen;
                
    }

}
