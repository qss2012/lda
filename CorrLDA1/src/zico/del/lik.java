/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zico.del;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 *
 * @author kaldr
 */
public class lik {

    public void lik() {
    }

    public static void main(String arg[]) {
        lik likehood=new lik();
        ArrayList filenames=likehood.getFiles();
        System.out.println(filenames);
        likehood.clik2((String) filenames.get(1));
        
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
    public double[][] readFile(String file){
        try{
            
            BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line=reader.readLine();
            StringTokenizer tknz=new StringTokenizer(line," ");
            int no=Integer.parseInt(tknz.nextToken());
            int topic=Integer.parseInt(tknz.nextToken());
            System.out.println(topic);
            double[][] array=new double[no][topic];
            for(int i=0;i<no;i++){
                line=reader.readLine();
                
                while(line==null){
                    line=reader.readLine();
                }
                tknz=new StringTokenizer(line," \t");
                for(int j=0;j<topic;j++){
                    if(tknz.countTokens()==topic){
                        array[i][j]=Double.parseDouble(tknz.nextToken());
                    }else{
                        System.out.println("Error writing "+file+",  it has "+tknz.countTokens()+" topics in line "+i);
                        System.exit(0);
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
