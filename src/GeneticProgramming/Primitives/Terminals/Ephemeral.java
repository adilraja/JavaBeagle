/*
 * Ephemeral.java
 *
 * Created on September 27, 2007, 11:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GeneticProgramming.Primitives.Terminals;

/**
 *
 * @author adilraja
 */
import GeneticProgramming.*;
import GeneticProgramming.Primitives.Primitive;
import java.util.*;

public class Ephemeral extends Primitive{
    
    /** Creates a new instance of Ephemeral */
    public Ephemeral() {
    }
    /**
 *  \param inNumberArguments Number of arguments of the primitive.
 *  \param inName Name of the primitive.
 */
    public Ephemeral(String inName, int arity){
        super(inName, arity);
    }
    /**
 *  \brief Copy-construct a primitive.
 *  \param inRightPrimit Primitive to copy.
 */
public Ephemeral(final Ephemeral copy)
{  
    super(copy);
//    this.name=new String(copy.name);
//    this.arity=copy.arity;
//    mRefCounter=0;
}


/**
 *  \brief Give a reference on the actual primitive.
 *  \param object of Random
 *  \return Primitive handle to this pointer.
 */
public Primitive giveReference(Random dice)
{
      this.name=java.lang.Double.toString(dice.nextDouble()*12-6);
      return this;
}

/**
 *Function is used to execute this primitive
 *outResult
 *callStack
 *tree to which this primitive belongs
 *XData
 *patternNumber, that comes in from evaluateTree in Tree
 */
public double[] execute(ArrayList<Integer> callStack, Tree tree, float [][] XData){
 //call the cache here?
/*    StringBuilder key=new StringBuilder(tree.size()-callStack.get(callStack.size()-1).intValue());
    tree.writeTree2(callStack.get(callStack.size()-1).intValue(), key);
    Evaluation evaluation=PuppysMain.lContext.mCache.get(key.toString());
    if(this.getArity()>0){//i.e. only non-terminals are evaluated here
        if(evaluation!=null){
 //           double[] evaled=lContext.mCache.get(key.toString()).evaluation;
  //         for(int i=0;i<evaled.length;i++)
    //            outResult[i]=(float)evaled[i];
            PuppysMain.lContext.mCache.get(key.toString()).cachedFlag=true;//the entry is now cached, even if it was not previously
            PuppysMain.cacheHits+=tree.get(callStack.size()-1).getSubTreeSize();;
            
            return evaluation.evaluation;
        }
    }
 */
    //  double lArg2;
        //    double tmpVal=Double.valueOf(this.name).doubleValue();
           // for(int i=0;i<outResult.length;i++)
           //    outResult[i]=tmpVal;
            double[] outResult=new double[1];
            outResult[0]=Double.valueOf(this.name).doubleValue();
/*    PuppysMain.noCacheHits++;
     if(evaluation==null){//i.e. if the entry is not present
        if(this.getArity()==0){
            lContext.mCache.put(key.toString(), new Evaluation());//call the default constructor of Evaluation
            //but the actual evaluation is done here. Aimed at saving extra array-copying
        }
        else{//the tree has child(ren).
            int lIndex = callStack.get(callStack.size()-1).intValue() + 1;//index of the next subtree
            boolean hasCached=true;//that the subtree is cahched
            for(int i=0;i<this.arity;i++){
                StringBuilder subTree=new StringBuilder(tree.size()-lIndex);
                tree.writeTree2(lIndex, subTree);
                String str=new String(subTree.toString());
                Evaluation evaluation2=lContext.mCache.get(str);
                try{
                    
                    if(evaluation2!=null)
                        if(evaluation2.cachedFlag==false)
                            hasCached=false;
                //    System.out.println("Alarming");
                }
                catch(Exception e){
                    hasCached=false;
                    System.out.println("Do I reach here? "+e);
         //           System.exit(0);
                }
                lIndex+=tree.get(lIndex).getSubTreeSize();
            }
            if(hasCached){//That is hasCached survived through all the subChildren
                lContext.mCache.put(new String(key.toString()), new Evaluation(outResult, false));
            }
        }
        
     }*/
    return outResult;
}
public Ephemeral clone(){
    return (new Ephemeral(this));
}

}

