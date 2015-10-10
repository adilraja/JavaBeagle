/*
 * PuppysMain.java
 *
 * Created on August 14, 2007, 8:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GeneticProgramming;

/**
 *
 * @author adilraja
 */
import GeneticProgramming.Primitives.*;
import GeneticProgramming.Primitives.Terminals.*;
import GeneticProgramming.Primitives.Functions.*;
import java.util.*;
import java.lang.*;
import java.io.*;


public class PuppysMain {
    
    public static float[][] X;
    public static float[] Target;
    public static Context lContext;
    public static int cacheHits, noCacheHits;
    
    /** Creates a new instance of PuppysMain */
    public PuppysMain() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Context();
        Context.insert(new Plus("plus",2));
        Context.insert(new Minus("minus",2));
        lContext.insert(new MyDivide("mydivide",2));
        lContext.insert(new AdilPower("adilpower",2));
        Context.insert(new Times("times",2));
        lContext.insert(new Sin("sin",1));//sin
        lContext.insert(new Cos("cos",1));//cos
        lContext.insert(new AdilLog("adillog",1));//ln
        lContext.insert(new AdilLog10("adillog10",1));//log-10
   //     lContext.insert(new Primitive("if",3));       
        Context.insert(new X1("X1",0));
        Context.insert(new X2("X2",0));
        Context.insert(new X3("X3",0));
        Context.insert(new X4("X4",0));
        Context.insert(new X5("X5",0));
        Context.insert(new X6("X6",0));
        Context.insert(new X7("X7",0));
        Context.insert(new X8("X8",0));
        Context.insert(new X9("X9",0));
        Context.insert(new X10("X10",0));
        Context.insert(new X11("X11",0));
        Context.insert(new X12("X12",0));
        Context.insert(new X13("X13",0));
        Context.insert(new X14("X14",0));
        Context.insert(new X15("X15",0));
        Context.insert(new X16("X16",0));
        Context.insert(new X17("X17",0));
        Context.insert(new X18("X18",0));
        Context.insert(new X19("X19",0));
        Context.insert(new X20("X20",0));
        Context.insert(new X21("X21",0));
        Context.insert(new X22("X22",0));
        Context.insert(new X23("X23",0));
        Context.insert(new X24("X24",0));
        Context.insert(new X25("X25",0));
        Context.insert(new X26("X26",0));
        Context.insert(new X27("X27",0));
        Context.insert(new X28("X28",0));
        Context.insert(new X29("X29",0));
        Context.insert(new X30("X30",0));
        Context.insert(new X31("X31",0));
        Context.insert(new X32("X32",0));
        Context.insert(new X33("X33",0));
        Context.insert(new X34("X34",0));
        Context.insert(new X35("X35",0));
        Context.insert(new X36("X36",0));
        Context.insert(new X37("X37",0));
        Context.insert(new X38("X38",0));
        Context.insert(new X39("X39",0));
        Context.insert(new X40("X40",0));
        Context.insert(new X41("X41",0));
        Context.insert(new X42("X42",0));
        Context.insert(new X43("X43",0));
        Context.insert(new X44("X44",0));
        Context.insert(new Ephemeral("Ephemeral",0));
        Context.insert(new Ephemeral("Ephemeral",0));
        Context.insert(new Ephemeral("Ephemeral",0));
        Context.insert(new Ephemeral("Ephemeral",0));
        Context.insert(new Ephemeral("Ephemeral",0));
        Context.insert(new Ephemeral("Ephemeral",0));
        Context.insert(new Ephemeral("Ephemeral",0));
        Context.insert(new Ephemeral("Ephemeral",0));
        Context.insert(new Ephemeral("Ephemeral",0));
        Context.insert(new Ephemeral("Ephemeral",0));
        Context.popSize=2000;
        Context.xoverProba=0.95F;
        Context.mutationProba=0.1F;
        Context.inInitGrowProba=0.5F;
        Context.xoverNodeDistribProb=0.5F;
        Context.maxTreeDepth=17;
        Context.initTreeDepthMax=6;
        Context.initTreeDepthMin=2;
        Context.tournamentSize=7;
        Context.trainDataFileX=new String("./OnP563/p563-input-params-cond-n-exp-wise.txt");
//    Context.trainDataFileX="adil.txt";
        Context.trainDataFileY=new String("./OnP563/Subj-MOS-1328-cond-wise-sorted.txt");
        Context.testDataFileX=new String("./OnP563/combined-p563-features-Nortel-czech.txt");
        Context.testDataFileY=new String("./OnP563/combined-ACR-MOS-Nortel-czech.txt");
        Context.useTestData=true;
        Context.length_of_run=100;
        int numRuns=2;
        int numVARs=44;
        
  //     for(int k=0;k<5;k++){
    //        Context.insert(new Primitive("X".concat(Integer.toString(k+1)), 0));
      //  }
        ArrayList<Statistics> statistics=new ArrayList();
        
        
        /*
         *Lets do an example problem here
         */
         
//        Context.XData=new float[1000][2];
//        Context.Target=new double[1000];
        double fitness;
//        Random dice=new Random();
//        for(int i=0;i<1000;i++){
//            Context.XData[i][0]=dice.nextFloat()*10-5;
//            Context.XData[i][1]=dice.nextFloat()*10-5;
//            Context.Target[i]=Math.pow(Context.XData[i][0],4)+Context.XData[i][0]*Context.XData[i][1]+Math.pow(Context.XData[i][1],4);
         //   Context.Target[i]=(double)(Context.XData[i][0]*(Context.XData[i][0]*(Context.XData[i][0]*(Context.XData[i][0]+1)+1)+1));
//        }
        Context.readDataPatterns(1328, 44, true);
   //     System.exit(0);
        if(Context.useTestData)//if testdata is used as well
            Context.readDataPatterns(276, 44, false);
        
        int lPopSize=1000;
        Puppy puppy=new Puppy();
       
        
        cacheHits=0;
          
        for(int i=0;i<numRuns;i++){//the run starts here
            Statistics thisRunStats=new Statistics();
            thisRunStats.trainFitHistory=new double[Context.length_of_run];
            if(Context.useTestData)
                thisRunStats.testFitHistory=new double[Context.length_of_run];
        
        ArrayList<Tree> lPopulation=new ArrayList(Context.popSize);
            
        puppy.initializePopulation(lPopulation);
        puppy.evaluatePopulation(lPopulation);

        int j=0;
        fitness=500;
        
        Tree bestTree=null;
        while(j<Context.length_of_run){//run starts from here
      
        System.out.println();
        bestTree=puppy.applyAllEvolutionarySteps(lPopulation);
        double tmpFitness=200;
 
        tmpFitness=lPopulation.get(0).getTrainingFitness();
        double testFitness=Double.MAX_VALUE;
        if(Context.useTestData){
            testFitness=lPopulation.get(0).evaluateTreeForTestData();
        }
        thisRunStats.trainFitHistory[j]=tmpFitness;
//        thisRunStats.testFitHistory[j]=testFitness;
        System.out.println("CurrentBest "+tmpFitness+" TestFitness: "+testFitness+" Generation "+j);
        System.out.println("Xover Prob: "+Context.xoverProba+" Mutation Prob: "+Context.mutationProba);
        StringBuilder stbldr=new StringBuilder();
        lPopulation.get(0).writeTree(0,stbldr);
        System.out.println(stbldr);
        
        System.gc();
        j++;
        }// a generation ends here
        bestTree.setSExpression();// set the s-expression now, shall be used latter
        thisRunStats.bestIndividual=new Tree(bestTree);
        statistics.add(thisRunStats);
    }// a run ends here
       try{
            StatisticsIO.writeStatsToDisk(statistics, "SimulationResults.expr");
       }
       catch(java.io.IOException e){
           System.out.println(e);
       }
        ArrayList<Statistics> stato=null;
        try{
            stato=(ArrayList<Statistics>) StatisticsIO.readStatsFromDisk("SimulationResults.expr");
        }catch(java.io.IOException e){}
        catch(java.lang.ClassNotFoundException e){}
        System.out.println("Here are the statistics");
        for(int i=0;i<stato.size();i++){
            System.out.println(stato.get(i).bestIndividual.getTrainingFitness());
        }
    
}

    
}
