
/*
 * Evaluation.java
 *
 * Created on September 6, 2007, 9:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *Keeps the Evaluation of a subtee lying in a cache
 */

package GeneticProgramming;

/**
 *
 * @author adilraja
 */

 public class Evaluation{
    public double[] evaluation;
    public boolean cachedFlag;
    /**
     *The default constructor for Terminal nodes
     */
    public Evaluation(){
        this.cachedFlag=true;//Vals for Ts always get true
        this.evaluation=new double[1];
    }
    
    /**
     *The contructor for NT subtrees
     */
    public Evaluation(double[] eval, boolean cFlag){
        this.evaluation=new double[eval.length];
        for(int i=0;i<eval.length;i++){
            this.evaluation[i]=eval[i];
        this.cachedFlag=cFlag;//Defines Whether an entry is effectively cached
        }
    }
 }