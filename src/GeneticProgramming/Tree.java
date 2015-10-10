/*
 * Tree.java
 *
 * Created on August 10, 2007, 10:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GeneticProgramming;

/**
 *
 * @author adilraja
 */
import GeneticProgramming.Primitives.Primitive;
import java.util.*;
import java.lang.*;
import EvoNursery.*;
import java.io.*;

public class Tree extends ArrayList<Node> implements Serializable{
    private double fitness, testFitness;
    private boolean valid;
    private String s_expression;
    private boolean evaluated;//tells if the fitness of this tree has been evaluated
    private double slope;
    private double intercept;
    private double [] trainingResult; //This contains the result of the individual over whole data
    public static double[] forConstants;//These two variables shall be used by the primitives execute
    public boolean usedXover, usedMutationStd, usedMutationSwap;
    
    
    /** Creates a new instance of Tree */
    public Tree() {
        this.fitness=Double.MAX_VALUE;//assign a fitness value
        this.valid=false;
        this.evaluated=false;
    }

/**
 *  Construct a new tree, with given fitness and validity flag.
 */
public Tree(double sFitness, boolean iValid){
    this.fitness=sFitness;
    this.valid=iValid;
    this.s_expression=new String();
    this.evaluated=false;//that it has not been evaluated by default
    this.slope=1;
    this.intercept=0;
}

/**
 * copy Constructor
 */
public Tree(final Tree copy){
    this.fitness=copy.fitness;
    this.testFitness=copy.testFitness;
    this.valid=copy.valid;
//    this.s_expression=copy.writeTree(0);
    this.evaluated=copy.evaluated;
    for(int i=0;i<copy.size();i++)
        this.add(new Node(copy.get(i)));
    this.slope=copy.slope;
    this.intercept=copy.intercept;
}

/**
 *  \brief Return tree depth at given index.
 *  \param inIndex Index of sub-tree root to get the depth from.
 *  \return Sub-tree depth.
 */
public final int getDepth(int inIndex)
{
  if (inIndex < this.size()){// I had assert here
  int lNbArgs = this.get(inIndex).getPrimitive().getArity();
  int lDepth = 1;
  int j = inIndex + 1;
  for(int i=0; i<lNbArgs; ++i) {
    int lChildDepth = getDepth(j) + 1;
    if(lChildDepth > lDepth) lDepth = lChildDepth;
    j += this.get(j).getSubTreeSize();
  }
  return lDepth;
  }
  else{
      return 0;
  }
}

    
 /**
 *  Compare equality of two trees.
 * Two trees are condired equal if their fitness differs by less than 0.01. 
 * For bucketting reasons
 *  return: True is trees are equals, false if not.
 */
public final boolean compareTrees(final Tree aTree) 
{
  if(this==aTree) return true;  
  return (this.valid && aTree.valid && (java.lang.Math.abs(this.fitness - aTree.fitness)<0.01));
}

/**
 *  Compare ranking of two trees.
 *  \return True is actual tree is less than a tree given as argument, false if not.
 */
public final boolean compareRank(Tree aTree)
{
  return (this.valid && aTree.valid && (this.fitness < aTree.fitness));
}

/*
 *  Interpret the GP tree.
 *  \param outResult Datum containing the result of the interpretation.
 *  \param ioContext Evolutionary context.
 *
void Puppy::Tree::interpret(void* outResult, Puppy::Context& ioContext)
{
  assert(size() > 0);
  ioContext.mTree = this;
  ioContext.mCallStack.push_back(0);
  front().mPrimitive->execute(outResult, ioContext);
  ioContext.mCallStack.pop_back();
}*/

/**
 *  Set call stack to include the correctly refer to a given node.
 *  \param inIndex Node index to which call stack must be set.
 *  \param outCallStack Result of call stack setting.
 */
public final java.util.ArrayList<Integer> setStackToNode(int inIndex)
{
  assert(inIndex < size());
  
 ArrayList<Integer> callStack=new ArrayList();
  int i = 0;
  callStack.add(new Integer(i));
  while(i < inIndex) {
    int lNbArgs=this.get(i).getPrimitive().getArity();
    int lChildIndex = i + 1;
    for(int j=0; j<lNbArgs; ++j) {
      if((lChildIndex+this.get(lChildIndex).getSubTreeSize()) > inIndex) break;
      lChildIndex += this.get(lChildIndex).getSubTreeSize();
    }
    assert(lChildIndex < this.size());
    i = lChildIndex;
    callStack.add(new Integer(i));
  }
  assert(i == inIndex);
  return callStack;
}

/**
 *Returns the slope parameter found by linear scaling
 */
public final double getSlope(){
    return this.slope;
}

/**
 *Returns the intercept parameter found by linear scaling
 */
public final double getIntercept(){
    return this.intercept;
}

/**
 *  \brief Write GP tree at given index as a s-expression into a Java String object.
 *  \param ioOS C++ output stream to write tree into.
 *  \param inIndex Actual node index in tree.
 */
public void writeTree(int inIndex, StringBuilder expression)
{
  if(inIndex >= this.size()) return;
//  expression.append(" ");
  int lNbArgs = this.get(inIndex).getPrimitive().getArity();
  
  expression.append(this.get(inIndex).getPrimitive().getName());
  if(lNbArgs > 0) expression.append("(");
  int j = inIndex + 1;
  for(int i=0; i<lNbArgs; ++i){
        writeTree(j, expression);
    if(i<lNbArgs-1)
        expression.append(", ");
    try{
        j =j+this.get(j).getSubTreeSize();
    }
    catch(java.lang.IndexOutOfBoundsException e){
        System.out.println(e+" In write tree");
 //       System.exit(0);
    }
  }
  if(lNbArgs > 0) expression.append(")");
  //return expression.trtrim();//removes the " " added in the beginning.
}

/**
 *  \brief Write GP tree at given index as a s-expression into a Java String object.
 *  \param ioOS C++ output stream to write tree into.
 *  \param inIndex Actual node index in tree.
 */
public void writeTree2(int inIndex, StringBuilder expression)
{
  if(inIndex >= this.size()) return;
//  expression.append(" ");
  int lNbArgs = this.get(inIndex).getPrimitive().getArity();
  
  expression.append(this.get(inIndex).getPrimitive().getName());
  //if(lNbArgs > 0) expression.append("(");
  int j = inIndex + 1;
  for(int i=0; i<lNbArgs; ++i){
        writeTree(j, expression);
//    if(i<lNbArgs-1)
  //      expression.append(", ");
    try{
        j =j+this.get(j).getSubTreeSize();
    }
    catch(java.lang.IndexOutOfBoundsException e){
        System.out.println(e+" In write tree");
 //       System.exit(0);
    }
  }
  //if(lNbArgs > 0) expression.append(")");
  //return expression.trtrim();//removes the " " added in the beginning.
}


/**
 *sets the s-expression
 */
public void setSExpression(){
    StringBuilder expr=new StringBuilder(this.size());
    this.writeTree(0, expr);
    this.s_expression=new String(expr.toString());
            
}
/**
 *Gets the s-epression
 */
public final String getSExpression(){
    return this.s_expression;
}

/**
 *Set the valid flag to something
 */
public void setValid(final boolean inValid){
    this.valid=inValid;
}
    
/**
 *Get the validity status of this tree
 */
public final boolean getValid(){
    return this.valid;
}
/*
 *Sets the primitive to new value
 */

public void setPrimitive(final Primitive prim){
    this.setPrimitive(prim);
}

/**
 *Set the evaluated flag of this tree
 */
public void setEvaluated(final boolean eval){
    this.evaluated=eval;
}

/**
 *Get the evaluation flag of this tree
 */
public final boolean getEvaluated(){
    return this.evaluated;
}

/**
 *Set the fitness of this Tree
 */
public void setTrainingFitness(final double fit){
    this.fitness=fit;
}

/**
 *Return the training fitness of this Tree
 */
public final double getTrainingFitness(){
    return this.fitness;
}
/**
 *Return the fitness of Testing data
 */
public final double getTestingFitness(){
    return this.testFitness;
}

//public void run(){
    //this.evaluateTree();
//}

/**
 *  Evaluate fitness of a Tree
 *  
 *  inX Independant sample values for evaluation.
 *  inF Dependant sample values for evaluation.
 *  \return Number of fitness evaluated.
 *  \ingroup SymbReg
 */
public double evaluateTree()
{  
  assert(Context.XData.length == Context.Target.length);
  if(this.evaluated) 
      return this.fitness;
  
  double[] target=Context.Target;
  double[] evolved=this.interpretTree(Context.XData);
  
   if(evolved.length!=Context.Target.length){
        double tmpEvolved=evolved[0];//if everything that is returned is a constant
        evolved=new double[Context.Target.length];
        for(int z=0;z<Context.Target.length;z++)
            evolved[z]=tmpEvolved;
   }  
    double[] slope_n_intercept=new double[2];
    
    linearScaling(slope_n_intercept, evolved, Context.Target);
//    this.trainingResult=new double[evolved.length];
    double tmpFitness=0;
    double res;
    for(int i=0; i<evolved.length;i++){
        res=Context.Target[i]-(slope_n_intercept[0]*evolved[i]+slope_n_intercept[1]);
        tmpFitness+=res*res;
    }
    
    tmpFitness=tmpFitness/evolved.length;
//    if(tmpFitness<this.fitness){
        this.fitness=tmpFitness;
        this.slope=slope_n_intercept[0];
        this.intercept=slope_n_intercept[1];
  //  }
        
      if(Double.isNaN(tmpFitness)||Double.isInfinite(tmpFitness))
          this.fitness=Double.MAX_VALUE;
//    System.out.println("Current best "+this.fitness);
    this.valid = true;
    this.evaluated=true;
  return this.fitness;
}

/**
 *  Evaluate fitness of a Tree
 *  
 *  inX Independant sample values for evaluation.
 *  inF Dependant sample values for evaluation.
 *  \return Number of fitness evaluated.
 *  \ingroup SymbReg
 */
public double evaluateTreeForTestData()
{  
  assert(Context.TestXData.length == Context.TestTarget.length);
 
  double[] target=Context.TestTarget;
  double[] evolved=this.interpretTree(Context.TestXData);
  
   if(evolved.length!=Context.TestTarget.length){
        double tmpEvolved=evolved[0];//if everything that is returned is a constant
        evolved=new double[Context.TestTarget.length];
        for(int z=0;z<Context.TestTarget.length;z++)
            evolved[z]=tmpEvolved;
   }   
//    this.trainingResult=new double[evolved.length];
    double tmpFitness=0;
    double res;
    for(int i=0; i<evolved.length;i++){
        res=Context.TestTarget[i]-(this.slope*evolved[i]+this.intercept);
        tmpFitness+=res*res;
    }   
    tmpFitness=tmpFitness/evolved.length;
//    if(tmpFitness<this.fitness){
        this.testFitness=tmpFitness;      
      if(Double.isNaN(tmpFitness) ||Double.isInfinite(tmpFitness))
          this.testFitness=Double.MAX_VALUE; 
//    System.out.println("Current best "+this.fitness);
    this.valid = true;
    this.evaluated=true;
  return this.testFitness;
}


/**
 *LinearScaling, by Maarten Keijzer, is implemented here
 */
public void linearScaling(double[] slope_n_intercept, double[] evolved, double[] target){
    assert(evolved.length==target.length);
    double meanTarget=0, meanEvolved=0;
    
    for(int i=0;i<evolved.length; i++){
        meanEvolved+=evolved[i];
        meanTarget+=target[i];
    }
    meanEvolved/=evolved.length;
    meanTarget/=target.length;
    
    double numerator=0, denominator=0;
    for(int i=0;i<evolved.length;i++){
        numerator+=(target[i]-meanTarget)*(evolved[i]-meanEvolved);
        denominator+=java.lang.Math.pow(evolved[i]-meanEvolved, 2);
    }
//    numerator=numerator/target.length;
//    denominator/=evolved.length;
    double slope1=1;
    if(denominator!=0) slope1=numerator/denominator;
    else if(meanEvolved==0) slope1=1;
    else slope1=meanTarget/meanEvolved;
    slope_n_intercept[0]=slope1;
    slope_n_intercept[1]=meanTarget-slope*meanEvolved;
}

/**
 *  Interpret the GP tree.
 *  outResult Datum containing the result of the interpretation.
 *  XData to be evaluated
 */
public double[] interpretTree(float[][] xdata)
{
  assert(this.size() > 0);
  ArrayList<Integer> callStack=new ArrayList();
  callStack.add(new Integer(0));
  double[] outResult=this.get(0).getPrimitive().execute(callStack, this, xdata);
  callStack.remove(callStack.size()-1);
  return outResult;
}
/**
 *  tune the terminals (constants) of this tree 
 *  
 *  inX Independant sample values for evaluation.
 *  inF Dependant sample values for evaluation.
 *  \return Number of fitness evaluated.
 *  \ingroup SymbReg
 */
public double tuneTreesCoeffs()
{  
  assert(Context.XData.length == Context.Target.length);
//  if(this.evaluated) return this.fitness;
  
  Iterator<Node> nodItr=this.iterator();
  int ii=0;
  while(nodItr.hasNext()){
      Node tmpNode=nodItr.next();
      if(tmpNode.getPrimitive().getArity()==0
          && !tmpNode.getPrimitive().getName().startsWith("X"))
          ii++;//find the number of constants
  }
  if(ii<=1)
      return this.evaluateTree();//i.e. no coefficients to be tuned by GA so evaluate the ind only
  int[] constIndex=new int[ii];//keeps the indices of constants in this tree
  double[] constants=new double[ii];//this array would keep the constants for the interim (a temp array for best combo)
  int jj=0;
  
  for(ii=0;ii<this.size();ii++){
      if(this.get(ii).getPrimitive().getArity()==0
              && !this.get(ii).getPrimitive().getName().startsWith("X")){
          constIndex[jj]=ii;//this loop copies the indices to the array
          constants[jj]=Double.valueOf(this.get(ii).getPrimitive().getName());
          jj++;
      }        
  } 
 //Initialize the GA here
  Nursery nurse=new Nursery();
  ArrayList<Genotype> pop=nurse.initializePopulation(100, constIndex.length, -6, +6);//should initialize a population
   for(int i=0;i<50;i++){//numGens is chosen to be 150
        ArrayList<Genotype> childPop=nurse.applySelectionTournament(pop,4);
        nurse.applySinglePointCrossover(childPop,constIndex.length,0.8);
        nurse.applyMutationUniform(childPop,constIndex.length,0.2);
        pop.addAll(childPop);//append the two pops here
        for(int j=0;j<pop.size();j++){
            if(pop.get(j).getEvaluatedFlag()==false){//if it has not been evaluated
                double[] tmpGenes=pop.get(j).getGenotype();
                for(int k=0;k<constIndex.length;k++)
                    this.get(constIndex[k]).getPrimitive().setName(Double.toString(tmpGenes[k]));//insert the genes into tree
                double[] evolved=this.interpretTree(Context.XData);    
                if(evolved.length!=Context.Target.length){
                    double tmpEvolved=evolved[0];//if everything that is returned is a constant
                    evolved=new double[Context.Target.length];
                    for(int z=0;z<Context.Target.length;z++)
                        evolved[z]=tmpEvolved;
                }        
                double[] slope_n_intercept=new double[2];
                linearScaling(slope_n_intercept, evolved, Context.Target);

            //    this.trainingResult=new double[evolved.length];
                double tmpFitness=0;
                double res;
                for(int ij=0; ij<Context.Target.length;ij++){
                    res=Context.Target[ij]-(slope_n_intercept[0]*evolved[ij]+slope_n_intercept[1]);
                    tmpFitness+=res*res;
                }
                tmpFitness=tmpFitness/evolved.length;
                if(Double.isNaN(tmpFitness) || Double.isInfinite(tmpFitness))
                    tmpFitness=Double.MAX_VALUE;
                if(tmpFitness<this.fitness){
                    this.fitness=tmpFitness;

                    this.slope=slope_n_intercept[0];
                    this.intercept=slope_n_intercept[1];
                    for(int k=0;k<tmpGenes.length;k++)
                        constants[k]=tmpGenes[k];
                }
                pop.get(j).setFitness(tmpFitness);
                if(Double.isNaN(tmpFitness) || Double.isInfinite(tmpFitness))
                {
                    this.fitness=Double.MAX_VALUE;//assign a very large fitness
                    pop.get(j).setFitness(Double.MAX_VALUE);
                }
                pop.get(j).setEvaluated(true);
            }// if getEvaluated
        }
        nurse.applySurvival(pop);
    }
    for(int i=0;i<constants.length;i++)
        this.get(constIndex[i]).getPrimitive().setName(Double.toString(constants[i]));//set the coeffs in the tree
 
//    System.out.println("Current best "+this.fitness);
    this.valid = true;
    this.evaluated=true;
  return this.fitness;
}

}
