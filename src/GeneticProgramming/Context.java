/*
 * Context.java
 *
 * Created on August 11, 2007, 8:34 PM
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
import java.util.*;
import java.io.*;
public class Context {
    
    /** Creates a new instance of Context */
    public Context() {
        this.mFunctionSet=new ArrayList();
        this.mTerminalSet=new ArrayList();
        this.mPrimitiveMap=new TreeMap();
        this.mCache=new MyLinkedHashMap(500, .75F, true);
        this.previousBest=Double.MAX_VALUE;
}
    
    
  /**
   *  \brief Add a new primitive in the sets of primitive.
   *  \param inPrimitive Primitive added.
   */
  public static void insert(Primitive inPrimitive)
  {
    assert(!mPrimitiveMap.containsKey(inPrimitive.getName()));
    mPrimitiveMap.put(inPrimitive.getName(), inPrimitive);
    if(inPrimitive.getArity() == 0) mTerminalSet.add(inPrimitive);
    else mFunctionSet.add(inPrimitive);
  }
  
  /**
   *This is used to initialize the data
   */
  public static void initData(float[][] X, float[] Y){
      assert(X.length==Y.length);
      
      XData=new float[X.length][X[0].length];
      Target=new double[X.length];
      for(int i=0;i<X.length;i++){
          for(int j=0;j<X[0].length;j++)
              XData[i][j]=X[i][j];
          Target[i]=Y[i];
      }
  }
  /**
   *This is used to initialize the Test data
   */
  public static void initTestData(float[][] X, double[] Y){
      assert(X.length==Y.length);
      TestXData=new float[X.length][X[0].length];
      TestTarget=new double[X.length];
      for(int i=0;i<X.length;i++){
          for(int j=0;j<X[0].length;j++)
              TestXData[i][j]=X[i][j];
          TestTarget[i]=Y[i];
      }
  }
  /**
   *This function is used to read in data patterns. To be used by the java program
   *params:
   *filename-the file containing IO patterns
   *sizeX- number of patterns
   *sizeY- number of variables (for input data)
   *trainORtest- if true, then train data, else test data
   */
  public static void readDataPatterns(int sizeX, int sizeY, boolean trainORtest){
      StreamTokenizer tok=null;
      String filenameX, filenameY;
      if(trainORtest){
          filenameX=trainDataFileX;
          filenameY=trainDataFileY;
      }
      else{
          filenameX=testDataFileX;
          filenameY=testDataFileY;
      }
        try{
            tok = new StreamTokenizer(new BufferedReader(new FileReader(filenameX)));
            tok.ordinaryChar('\n');
            //tok.ordinaryChar()
        }
        catch(java.io.FileNotFoundException e){
            System.out.println(e+" in Context.java");
        }
       int i=0;
       int j=0;
       float [][] curDat=new float[sizeX][sizeY];
       
       String str;
        try{
            j=0;
            while(tok.nextToken()!=StreamTokenizer.TT_EOF){
                if(j>=44){j=0;i++;}
                switch (tok.ttype){
                case StreamTokenizer.TT_EOL:
                    i++;
                    j=0;
                    break;
               case '\r': 
                    i++;
                    j=0;
                    break;
                case 'n':
                    i++;
                    j=0;
                    break;
                case StreamTokenizer.TT_NUMBER:
                   curDat[i][j]=(float)tok.nval;
              //     System.out.println(tok.nval);
              //     j=j+1;
                //   if(j==3){
                  //     j=0;
                    //   i++;
                  // }
                   j++;
                   break;
                default:
           //         if(tok.ttype!=StreamTokenizer.TT_NUMBER && (tok.sval.compareTo("\t")!=0||tok.sval.compareTo(" ")!=0)){//means a newline
             //           j=0;
               //         i++;
                 //   }
                   break;
                }
            }
        }
        catch(java.io.EOFException e){
            //do nothing
        }
       catch(java.io.IOException e){
           //do nothing
       }
       catch(java.lang.ArrayIndexOutOfBoundsException e){
           //do nothing agin :(
           System.out.println(e+"Here lies the bug"+trainORtest+j+i);
        //   System.exit(0);
       }
        System.out.println("Here is what I read\n"+i+j);
       for(i=0;i<2;i++){
           for(j=0;j<3;j++)
               System.out.print(curDat[i][j]+"\t");
               System.out.println();
       }
        try{
            tok = new StreamTokenizer(new BufferedReader(new FileReader(filenameY)));
            tok.ordinaryChar('\n');
            //tok.ordinaryChar()
        }
        catch(java.io.FileNotFoundException e){
            System.out.println(e+" in Context.java");
        }
       i=0;
       double [] DatY=new double[sizeX]; 
        try{
            j=0;
            while(tok.nextToken()!=StreamTokenizer.TT_EOF){
                switch (tok.ttype){
                case StreamTokenizer.TT_EOL:
           //         i++;
                    break;
         //      case '\n':
           //         i++;
          //          j=0;
             //       break;
                case StreamTokenizer.TT_NUMBER:
                   DatY[i]=tok.nval;
                   i++;
            //       System.out.println(tok.nval);
                   break;
                default:
         //           j++;
                   break;
                }
            }
        }
        catch(java.io.EOFException e){
            //do nothing
        }
       catch(java.io.IOException e){
           //do nothing
       }
       catch(java.lang.ArrayIndexOutOfBoundsException e){
           //do nothing agin :(
           System.out.println(e+"Here lies the bug");
           System.exit(0);
       }
        if(trainORtest){
           XData=curDat;
           Target=DatY;
        }
        else{
           TestXData=curDat;
           TestTarget=DatY;
        }
//       for(int jj=0;jj<XData.length;jj++){
  //         XData[jj][1]*=100;
    //   }
  }
  
public static ArrayList<Primitive>            mFunctionSet;   //!< Set of functions usable to build trees.
public static ArrayList<Primitive>            mTerminalSet;   //!< Set of terminals usable to build trees.
public static TreeMap<String, Primitive>       mPrimitiveMap;  //!< Name-primitive map.  
public static LinkedHashMap<String, Evaluation> mCache; 
public static float mutationProba,swapMutationProba, xoverProba, inInitGrowProba, xoverNodeDistribProb;
public static int dynamicTreeDepth, popSize, initTreeDepthMin, initTreeDepthMax, tournamentSize, maxTreeDepth;
public static float[][] XData;
public static double[] Target;
public static float[][] TestXData;
public static double[] TestTarget;
public static String trainDataFileX;
public static String trainDataFileY;
public static String testDataFileX;
public static String testDataFileY;
public static boolean useTestData;
public static int length_of_run;
public static double previousBest;//fitness of the best individual of the previous generation

}

class MyLinkedHashMap extends LinkedHashMap{
        // This method is called just after a new entry has been added
        public MyLinkedHashMap(int val1, float val2, boolean val3){
            super(val1, val2, val3);
        }
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 500;
        }
  /*      public boolean containsKey(String key){
            Iterator<String> str=this.keySet().iterator();
            while(str.hasNext()){
                if(str.next().compareTo(key)==0){
                    return true;
                    
                }
            }
            return false;
        }
        public Evaluation get(String key){
            Evaluation eval=null;
            Iterator<Map.Entry> entryItr=this.entrySet().iterator();
            while(entryItr.hasNext()){
                Map.Entry temp=entryItr.next();
                if(temp.getKey().toString().compareTo(key)==0){
                    return (Evaluation)temp.getValue();
                    
                }
            }
            return null;
        }*/
}