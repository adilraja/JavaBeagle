/*
 * Statistics.java
 *
 * Created on September 14, 2007, 3:06 PM
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
public class Statistics implements Serializable{
    
    /** Creates a new instance of Statistics */
    public Statistics() {
    }
    /**
     *copy constructor
     */
    public Statistics(Statistics copy){
        trainFitHistory=new double[copy.trainFitHistory.length];
        testFitHistory=new double[copy.testFitHistory.length];
        for(int i=0;i<copy.trainFitHistory.length;i++){
            trainFitHistory[i]=copy.trainFitHistory[i];
            testFitHistory[i]=copy.testFitHistory[i];
        }
        bestIndividual=new Tree(copy.bestIndividual);
        
    }
   
    public static double[] trainFitHistory;
    public static double[] testFitHistory;
    public static Tree bestIndividual;
    
}
