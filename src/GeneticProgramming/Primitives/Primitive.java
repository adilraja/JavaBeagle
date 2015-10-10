/*
 * PrimitiveHandle.java
 *
 * Created on August 10, 2007, 9:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GeneticProgramming.Primitives;

/**
 *
 * @author adilraja
 */
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import java.util.*;
import GeneticProgramming.*;
import java.io.*;

public abstract class Primitive implements Serializable{
    protected String name;
    protected int arity;
    protected int mRefCounter;
    protected Random dice;
    
    
    /** Creates a new instance of PrimitiveHandle */
    public Primitive() {
    }
 /**
 *  \param inNumberArguments Number of arguments of the primitive.
 *  \param inName Name of the primitive.
 */
public Primitive(String inName, int arity)
{ 
    this.name=new String(inName);
    this.arity=arity;
    mRefCounter=0;
    
}
  
/**
 *  \brief Copy-construct a primitive.
 *  \param inRightPrimit Primitive to copy.
 */
public Primitive(final Primitive copy)
{  
    this.name=new String(copy.name);
    this.arity=copy.arity;
    mRefCounter=0;
}

/**
 *Assign the input argument (Primitive object) to
 *this object
 */
public Primitive assign(final Primitive primitive)
{
  if(this == primitive) return this;
  name = new String(primitive.name);
  this.arity = primitive.arity;
  return this;
}

/**
 *  \brief Give a reference on the actual primitive.
 *  \param object of Random
 *  \return Primitive handle to this pointer.
 */
public abstract Primitive giveReference(Random dice);//{return null;}
/*{
  if(this.getName().equalsIgnoreCase("Ephemeral")){//should return the Ephemeral random number instead
      return new Primitive(java.lang.Double.toString(dice.nextDouble()*12-6),0);
  }  
  return this;
}*/


/**
 *  \brief Set the name of the primitive.
 *  \param inName Name of the primitive.
 */
public void setName(String inName)
{
  this.name =new String(inName);
}

/**
 *return the name of this primitive
 */
public final String getName(){
    return this.name;
}

/**
 *  \brief Set the number of arguments of the primitive.
 *  \param inNumberArguments Number of arguments of the primitive.
 */
public void setArity(int inArity)
{
  this.arity=inArity;
}


/*!
 *  \brief Set the value of the primitive (do nothing for basic primitive).
 *  \param inValue New value to use.
 */
//void Puppy::Primitive::setValue(const void* inValue)
//{ }

/**
 *Returns the arity of this primitive
 */
public final int getArity(){
    return this.arity;
}

/**
 *  \brief Get the value of the nth argument.
 *  \param inN Index of the argument to get.
 *  \param outResult Value of the nth argument.
 *  \param ioContext Evolutionary context.
 *  XData
 *  patternNumber
 */
public double[] getArgument(int inN, ArrayList<Integer> callStack, Tree tree,
        final float [][] XData)
{
      assert(inN >= 0);
      assert(inN < this.getArity());
      //
      
      //
      int lIndex = callStack.get(callStack.size()-1).intValue() + 1;
      for(int i=0; i<inN; ++i) lIndex += tree.get(lIndex).getSubTreeSize();
      callStack.add(new Integer(lIndex));
      double[] outResult=tree.get(lIndex).getPrimitive().execute(callStack, tree, XData);
      callStack.remove(callStack.size()-1);
      return outResult;
}
public abstract Primitive clone();
/**
 *Function is used to execute this primitive
 *outResult
 *callStack
 *tree to which this primitive belongs
 *XData
 *patternNumber, that comes in from evaluateTree in Tree
 */
//public abstract double[] execute(ArrayList<Integer> callStack, Tree tree, float [][] XData);
public abstract double[] execute(ArrayList<Integer> callStack, Tree tree, float [][] XData);//{return null;}
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
/*    double[] outResult=null;
    switch(this.arity){
        case 2:{//for all the two arity functions
        double[] lArg2=null;
        outResult=getArgument(0, callStack, tree, XData);
        lArg2=getArgument(1, callStack, tree, XData);
        if(this.getName().compareTo("plus")==0){
            if(outResult.length==lArg2.length)
                for(int i=outResult.length;--i>=0;)
                    outResult[i] += lArg2[i];
            else if(outResult.length==1){//i.e. a terminal returned
                for(int i=lArg2.length;--i>=0;)
                    lArg2[i]+=outResult[0];
                outResult=lArg2;//point outResult to this array now
            }
            else if(lArg2.length==1){
                for(int i=outResult.length;--i>=0;)
                    outResult[i]+=lArg2[0];
            }
        }
        else if(this.getName().compareTo("minus")==0){
            if(outResult.length==lArg2.length)
               for(int i=outResult.length;--i>=0;)
                    outResult[i] -= lArg2[i];
            else if(outResult.length==1){//i.e. a terminal returned
                for(int i=lArg2.length;--i>=0;)
                    lArg2[i]-=outResult[0];
                outResult=lArg2;//point outResult to this array now
            }
            else if(lArg2.length==1){
                for(int i=outResult.length;--i>=0;)
                    outResult[i]-=lArg2[0];
            }
        }
    else if(this.getName().compareTo("times")==0){
        if(outResult.length==lArg2.length)
                for(int i=outResult.length;--i>=0;)
                    outResult[i] *= lArg2[i];
        else if(outResult.length==1){//i.e. a terminal returned
            for(int i=lArg2.length;--i>=0;)
                lArg2[i]*=outResult[0];
            outResult=lArg2;//point outResult to this array now
        }
        else if(lArg2.length==1){
            for(int i=outResult.length;--i>=0;)
                outResult[i]*=lArg2[0];
        }
    }
        else if(this.getName().compareTo("adilpower")==0){
        if(outResult.length==lArg2.length)
                for(int i=outResult.length;--i>=0;)
                    outResult[i] =Math.pow(outResult[i], lArg2[i]);
        else if(outResult.length==1){//i.e. a terminal returned
            for(int i=lArg2.length;--i>=0;)
                lArg2[i]=Math.pow(lArg2[i],outResult[0]);
            outResult=lArg2;//point outResult to this array now
        }
        else if(lArg2.length==1){
            for(int i=outResult.length;--i>=0;)
                outResult[i]=Math.pow(outResult[i], lArg2[0]);
        }
    }
    else{//divide
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
    }
        break;
    }
        case 1:{
         outResult=getArgument(0, callStack, tree, XData);
         if(this.name.compareTo("sin")==0)//sin
             for(int i=outResult.length;--i>=0;)
                 outResult[i]=(Math.sin(outResult[i]));
         else if(this.name.compareTo("cos")==0)//cos
             for(int i=outResult.length;--i>=0;)
                 outResult[i]=(Math.cos(outResult[i]));
         else if(this.name.compareTo("adillog")==0)
             for(int i=outResult.length;--i>=0;)
                 outResult[i]=(Math.log(outResult[i]));
         else if(this.name.compareTo("adillog10")==0)
             for(int i=outResult.length;--i>=0;)
                 outResult[i]=(Math.log10(outResult[i]));
         else if(this.name.compareTo("adilsqrt")==0){
             for(int i=outResult.length;--i>=0;)
                 outResult[i]=Math.sqrt(outResult[i]);
         }
         break;
    }
        default:{//A terminal has been reached
        if(!this.name.startsWith("X")){
        //    double tmpVal=Double.valueOf(this.name).doubleValue();
           // for(int i=0;i<outResult.length;i++)
           //    outResult[i]=tmpVal;
            outResult=new double[1];
            outResult[0]=Double.valueOf(this.name).doubleValue();
        }
        else if(this.name.substring(0,1).compareTo("X")==0){
            //The following line would automatically assign the pertinent entry in the pattern to the result to be sent out.
            int tmp=Integer.valueOf(this.name.substring(1)).intValue()-1;
            outResult=new double[XData.length];
            for(int i=outResult.length;--i>=0;)
               outResult[i]=(double)XData[i][tmp];//Return the whole array of the Input data, this should save some data.
        }
        break;
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
  //  return outResult;
//}

}
