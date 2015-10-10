/*
 * MyDivide.java
 *
 * Created on September 27, 2007, 11:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GeneticProgramming.Primitives.Functions;

/**
 *
 * @author adilraja
 */
import GeneticProgramming.*;
import GeneticProgramming.Primitives.Primitive;
import java.util.*;

public class MyDivide extends Primitive{
    
    /** Creates a new instance of X1 */
    public MyDivide() {
    }
    /**
 *  \param inNumberArguments Number of arguments of the primitive.
 *  \param inName Name of the primitive.
 */
    public MyDivide(String inName, int arity){
        super(inName, arity);
    }
    /**
 *  \brief Copy-construct a primitive.
 *  \param inRightPrimit Primitive to copy.
 */
public MyDivide(final MyDivide copy)
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
public MyDivide giveReference(Random dice)
{
//  if(this.getName().equalsIgnoreCase("Ephemeral")){//should return the Ephemeral random number instead
  //    return new Primitive(java.lang.Double.toString(dice.nextDouble()*12-6),0);
 // }  
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
    double[] outResult=null;
        double[] lArg2=null;
        outResult=getArgument(0, callStack, tree, XData);
        lArg2=getArgument(1, callStack, tree, XData);
            if(outResult.length==lArg2.length){
            for(int i=outResult.length;--i>=0;){
                try{
                    outResult[i] /= lArg2[i];
                }
                catch(Exception e){
                    outResult[i]=Double.NaN;
                }
            }
        }
        else if(outResult.length==1){//i.e. a terminal returned
            for(int i=lArg2.length;--i>=0;){
                try{
                    lArg2[i]+=outResult[0];
                }
                catch(Exception e){
                    outResult[i]=Double.NaN;
                }
            }
            outResult=lArg2;//point outResult to this array now
        }
        else if(lArg2.length==1){
            for(int i=outResult.length;--i>=0;){
                try{
                    outResult[i]+=lArg2[0];
                }
                catch(Exception e){
                    outResult[i]=Double.NaN;
                }
            }
        }
        
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

public MyDivide clone(){
    return (new MyDivide(this));
}

}




