/*
 * This programme is Correlative LDA 1
 * The original model is in the paper: Statistical entity-topic models
 */
package corrlda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;
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
    public int nitter = 200;
    public Model movielens;

    public CorrLDA() {
        movielens = new Model();
        initialize(movielens);
        theta = new double[userlen][K];
        fine = new double[movielen][K];
        digamma = new double[taglen][K];
    }

    public static void main(String args[]) {
        Estimate estimate = new Estimate();
        int b = 0;
        /*
        String lastModel="3000/model.dat_101";
        StringTokenizer tknz=new StringTokenizer(lastModel,"_");
        String a=tknz.nextToken();
        a=tknz.nextToken();        
        b=Integer.parseInt(a);
         */
        CorrLDA corrlda = new CorrLDA();
        corrlda.initialize(corrlda.movielens);
        //corrlda.readModel(corrlda, lastModel);
        estimate.estimate(corrlda, b);
        System.out.println("OMG finished");
    }

    public void initialize(Model model) {
        System.out.println("******************************\nThe model is initializing.\n******************************\n");
        model.initialize();
        System.out.println("The model is initialized.");
        //initial count
        movielen = model.itemlen;
        userlen = model.userlen;
        taglen = model.taglen;
        nz_u = new int[userlen][K];
        System.out.println("nz_u ready");
        nm_z = new int[movielen][K];
        System.out.println("nm_z ready");
        nt_z = new int[taglen][K];
        System.out.println("nt_z ready");
        nsumz_u = new int[userlen];
        nsumm_z = new int[K];
        nsumt_z = new int[K];
        System.out.println("nsum ready");

        System.out.println("******************************\nThe Parameter is initializing.\n******************************\n");
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
        System.out.println("count parameter is ready.");
        //initial topic/z for movies and tags;
        z = new Vector[userlen];
        ztag = new Vector[userlen];
        Object[] userIDset = model.userData.userid2doc.keySet().toArray();
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("usermap.dat"), "UTF-8"));
            int u, m, t;
            for (u = 0; u < userlen; u++) {//i is user
                int userID = Integer.parseInt(userIDset[u].toString());


                int M = model.userData.userid2doc.get(userID).size();//number of movies of a user;corrlda.movielens.userData.userid2doc.get(userID).size();
                writer.write(u + "::" + userID + "\r\n");
                int T = 0;
                z[u] = new Vector();
                ztag[u] = new Vector();

                for (m = 0; m < M; m++) {

                    int topic = (int) Math.floor(Math.random() * K);
                    int movieIDr = Integer.parseInt(model.userData.userid2doc.get(userID).get(m).toString());

                    int movieID = model.itemData.id_idr.get(movieIDr);

                    z[u].add(topic);
                    //number of topic occured in user u
                    nz_u[u][topic] += 1;
                    nsumz_u[u] += 1;
                    //number of movie assigned to topic
                    nm_z[movieID][topic] += 1;
                    nsumm_z[topic] += 1;
                }
                if (model.tagData.user2tag.containsKey(userID)) {
                    T = model.tagData.user2tag.get(userID).size();
                    for (t = 0; t < T; t++) {
                        int zID = (int) Math.floor(Math.random() * M);
                        int topic = z[u].get(zID);
                        ztag[u].add(topic);
                        int tag = Integer.parseInt(model.tagData.user2tag.get(userID).get(t).toString());
                        //number of tag assigend to topic
                        nt_z[tag][topic] += 1;
                        nsumt_z[topic] += 1;

                    }
                }

            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while reading dictionary:" + e.getMessage());
            e.printStackTrace();
        }


        System.out.println("CorrLda is totally initialized.");

    }

    public void readModel(CorrLDA corrlda, String filename) {
        try {
            System.out.println("Reading " + filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            System.out.println("File ready");
            String line = reader.readLine();
            StringTokenizer tknz = new StringTokenizer(line, "::\r\n");
            corrlda.movielen = Integer.parseInt(tknz.nextToken().toString());
            corrlda.userlen = Integer.parseInt(tknz.nextToken().toString());
            corrlda.taglen = Integer.parseInt(tknz.nextToken().toString());
            corrlda.nm_z = new int[corrlda.movielen][corrlda.K];
            corrlda.nz_u = new int[corrlda.userlen][corrlda.K];
            corrlda.nt_z = new int[corrlda.taglen][corrlda.K];
            corrlda.nsumm_z = new int[corrlda.K];
            corrlda.nsumt_z = new int[corrlda.K];
            corrlda.nsumz_u = new int[corrlda.userlen];
            corrlda.theta = new double[corrlda.userlen][corrlda.K];
            corrlda.fine = new double[corrlda.movielen][corrlda.K];
            corrlda.digamma = new double[corrlda.taglen][corrlda.K];
            line = reader.readLine();
            if (line.equals("nz_u")) {
                System.out.println("Reading nz_u...");
                for (int i = 0; i < corrlda.userlen; i++) {
                    line = reader.readLine();
                    tknz = new StringTokenizer(line, " ");
                    for (int j = 0; j < corrlda.K; j++) {
                        corrlda.nz_u[i][j] = Integer.parseInt(tknz.nextToken().toString());
                    }
                }
            }
            line = reader.readLine();
            if (line.equals("nm_z")) {
                for (int i = 0; i < corrlda.movielen; i++) {
                    line = reader.readLine();
                    tknz = new StringTokenizer(line, " ");
                    for (int j = 0; j < corrlda.K; j++) {
                        corrlda.nm_z[i][j] = Integer.parseInt(tknz.nextToken().toString());
                    }
                }
            }
            line = reader.readLine();
            if (line.equals("nt_z")) {
                for (int i = 0; i < corrlda.taglen; i++) {
                    line = reader.readLine();
                    tknz = new StringTokenizer(line, " ");
                    for (int j = 0; j < corrlda.K; j++) {
                        corrlda.nt_z[i][j] = Integer.parseInt(tknz.nextToken().toString());
                    }
                }
            }
            line = reader.readLine();
            if (line.equals("nsumz_u")) {
                line = reader.readLine();
                tknz = new StringTokenizer(line, " ");
                for (int i = 0; i < corrlda.userlen; i++) {
                    corrlda.nsumz_u[i] = Integer.parseInt(tknz.nextToken().toString());
                }
            }
            line = reader.readLine();
            if (line.equals("nsumm_z")) {
                line = reader.readLine();
                tknz = new StringTokenizer(line, " ");
                for (int i = 0; i < corrlda.K; i++) {
                    corrlda.nsumm_z[i] = Integer.parseInt(tknz.nextToken().toString());
                }
            }
            line = reader.readLine();
            if (line.equals("nsumt_z")) {
                line = reader.readLine();
                tknz = new StringTokenizer(line, " ");
                for (int i = 0; i < corrlda.K; i++) {
                    corrlda.nsumt_z[i] = Integer.parseInt(tknz.nextToken().toString());
                }
            }
            line = reader.readLine();
            corrlda.z = new Vector[corrlda.userlen];
            corrlda.ztag = new Vector[corrlda.userlen];
            if (line.equals("z")) {
                for (int i = 0; i < corrlda.userlen; i++) {
                    line = reader.readLine();
                    tknz = new StringTokenizer(line, " ");
                    int len = tknz.countTokens();
                    corrlda.z[i] = new Vector(len);
                    for (int j = 0; j < len; j++) {
                        corrlda.z[i].add(j, Integer.parseInt(tknz.nextToken()));
                    }
                }
            }
            line = reader.readLine();
            line = reader.readLine();
            if (line.equals("ztag")) {
                for (int i = 0; i < corrlda.userlen; i++) {
                    line = reader.readLine();
                    tknz = new StringTokenizer(line, " ");
                    int len = tknz.countTokens();
                    corrlda.ztag[i] = new Vector(len);
                    for (int j = 0; j < len; j++) {
                        corrlda.ztag[i].add(j, Integer.parseInt(tknz.nextToken()));
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
