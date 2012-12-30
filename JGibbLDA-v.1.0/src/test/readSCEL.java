/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import test.SougouScelReader;


/**
 *
 * @author Kaldr
 */
public class readSCEL {
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException{
        String fileName="C:\\Users\\Kaldr\\Downloads\\data\\SEO.scel";
        File file = new File(fileName);
        SougouScelReader reader=new SougouScelReader();
        SougouScelMdel model=reader.read(file);
        System.out.println("ok");

    }

   
   
}
