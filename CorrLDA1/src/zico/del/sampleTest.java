/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zico.del;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author kaldr
 */
public class sampleTest {

    public sampleTest() {
    }

    public static void main(String args[]) {
        int[][] data=new int[43726][2];
        int[] datastate=new int[43726];
        ArrayList itemList=new ArrayList();
        ArrayList userList=new ArrayList();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("tags.dat"), "UTF-8"));
            BufferedWriter tagswriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("train/tags.dat"), "UTF-8"));
            BufferedWriter ttagswriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test/tags.dat"), "UTF-8"));

            String line=reader.readLine();
            StringTokenizer tknz;
            int user;
            int item;
            int i=0;
            double rand;
            while(line!=null ){
                tknz=new StringTokenizer(line,"::\r\n");
                user=Integer.parseInt(tknz.nextToken());
                item=Integer.parseInt(tknz.nextToken());
                datastate[i]=0;
                if(!itemList.contains(item)){
                    itemList.add(item);
                    datastate[i]=1;
                }
                if(!userList.contains(user)){
                    userList.add(user);
                    datastate[i]=1;
                }
                rand=Math.random();
                if(datastate[i]==0){
                    if(rand<0.126){
                        ttagswriter.write(line+"\r\n");
                        ttagswriter.flush();
                    }else{
                        tagswriter.write(line+"\r\n");
                        ttagswriter.flush();
                    }
                }else{
                     tagswriter.write(line+"\r\n");
                     ttagswriter.flush();
                }
                data[i][0]=user;
                data[i][1]=item;
                i+=1;
                line=reader.readLine();                
            }
            tagswriter.close();
            ttagswriter.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
