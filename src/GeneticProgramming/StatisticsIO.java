/*
 * StatisticsIO.java
 *
 * Created on September 15, 2007, 6:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GeneticProgramming;

/**
 *
 * @author adilraja
 */
import java.io.*;
import java.util.*;

public class StatisticsIO {
    
    /** Creates a new instance of StatisticsIO */
    public StatisticsIO() {
    }
    
    /**
     *writeStatsToDisk
     */
    public static void writeStatsToDisk(ArrayList<Statistics> simStats, String filename) throws IOException {
        ObjectOutputStream out = null;
 
        try {
            out = new ObjectOutputStream (new FileOutputStream (filename));
            out.writeObject((Object)simStats);
            out.flush();
        } finally {
            if (out != null) {
                try {
                    out.close ();
                } catch (IOException exception) {}
            }
        }
    }
    public static ArrayList<Statistics> readStatsFromDisk(String filename) throws IOException, ClassNotFoundException{
        
        ObjectInputStream in = null;
        try{
            in=new ObjectInputStream(new FileInputStream(filename));
            return (ArrayList<Statistics>)in.readObject();  
        }finally{
            if(in!=null){
                try{
                    in.close();
                }catch(IOException e){}
            }
        }
        
    }
}
