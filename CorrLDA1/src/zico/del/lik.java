/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zico.del;

import corrlda.CorrLDA;
import corrlda.Model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author kaldr
 */
public class lik {
    CorrLDA corrlda;
    public void lik() {
        corrlda=new CorrLDA();
        corrlda.movielens = new Model();
        corrlda.initialize(corrlda.movielens);
        int userlen=corrlda.movielens.userlen;
        int movielen=corrlda.movielens.itemlen;
        int taglen=corrlda.movielens.taglen;
        int K=50;
        corrlda.theta = new double[userlen][K];
        corrlda.fine = new double[movielen][K];
        corrlda.digamma = new double[taglen][K];
    }

    public static void main(String arg[]) {
        lik likehood=new lik();
        ArrayList filenames=likehood.getFiles();
        System.out.println(filenames);
        likehood.clik2((String) filenames.get(0));
        
    }

    public void clik2(String file) {
        try{
            double[][] array=readFile(file);
            System.out.println(array);
        }catch(Exception e){
            e.printStackTrace();
        }
        double[][] theta = new double[1723][50];
        double[][] lasttheta = new double[1723][50];
    }
    public void clik(ArrayList files) {
        try{
            
            double[][] array=readFile((String) files.get(1));
            System.out.println(array);
        }catch(Exception e){
            e.printStackTrace();
        }
        double[][] theta = new double[1723][50];
        double[][] lasttheta = new double[1723][50];
    }
    public void readModel(CorrLDA corrlda,String filename){
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
    public double[][] readFile(String file){
        try{            
            BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line=reader.readLine();
            StringTokenizer tknz=new StringTokenizer(line," \r\n");
            int no=Integer.parseInt(tknz.nextToken());
            int topic=Integer.parseInt(tknz.nextToken());
            System.out.println(topic);
            double[][] array=new double[no][topic];
            for(int i=0;i<no;i++){
                line=reader.readLine();
                
                while(line==null){
                    line=reader.readLine();
                }
                tknz=new StringTokenizer(line,"\t \r\n");
                for(int j=0;j<topic;j++){
                    array[i][j]=Double.parseDouble(tknz.nextToken());
                    if(tknz.countTokens()==topic){
                        
                    }else{
                        System.out.println("Error writing "+file+",  it has "+tknz.countTokens()+" topics in line "+i);
                       System.out.println(line);
                        //System.exit(0);
                    }
                }
            }
            return array;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
        
    }
    public ArrayList getFiles() {
        File folder = new File("/home/kaldr/NetBeansProjects/CorrLDA/src/zico/del/3000");
        File[] fs = folder.listFiles();
        StringTokenizer tknz;
        ArrayList fsname = new ArrayList();
        String type;
        String filetype;
        String no;
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isFile()) {
                String filename = fs[i].getName();
                tknz = new StringTokenizer(filename, "._");
                if (tknz.countTokens() == 3) {
                    type = tknz.nextToken();
                    filetype = tknz.nextToken();
                    no = tknz.nextToken();
                    fsname.add(fs[i].getName());
                }
            }
        }
        Collections.sort(fsname);
        return fsname;
    }
}
