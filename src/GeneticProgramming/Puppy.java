/*
 * Puppy.java
 *
 * Created on August 11, 2007, 7:31 PM
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
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import java.util.*;
import java.util.Random;
import java.lang.*;


public class Puppy {
    
    private int parentPopSize;
    private Random dice;
    private ArrayList<Tree> childPop;
    private ArrayList<Integer>  lMateVector;
    public double [][] trainingDataX;
    public double [] trainingDataY;
    private double bestFitness;
    private int dynamicLevel;
    
    /** Creates a new instance of Puppy */
    public Puppy() {
        dice=new Random();
        childPop=new ArrayList();
        lMateVector=new ArrayList();
        this.dynamicLevel=6;//to be changed
        this.bestFitness=1000;
        //Initialize the IO data here
    }
    
 
 /**
 *  Initialize ramped half-and-half a population of GP trees.
 *  \param ioPopulation Population to initialize.
 *  \param inInitGrowProba Probability to use grow initialization, in opposition to full.
 *  \param inMinDepth Minimum initialization tree depth allowed.
 *  \param inMaxDepth Maximum initialization tree depth allowed.
 *  \ingroup Puppy
 */
public ArrayList<Tree> initializePopulation(ArrayList<Tree> ioPopulation)
{
  
 
  this.parentPopSize=Context.popSize;
  int inMinDepth=Context.initTreeDepthMin;
  int inMaxDepth=Context.initTreeDepthMax;
  float inInitGrowProba=Context.inInitGrowProba;
  
   assert(inMinDepth <= inMaxDepth);
 // ArrayList<Tree> ioPopulation=new ArrayList();
  this.dice=new Random();
  for(int i=0; i<Context.popSize; ++i) {
      ioPopulation.add(i,new Tree());
      ioPopulation.get(i).clear();
    
    int lInitDepth = dice.nextInt(inMaxDepth-inMinDepth)+inMinDepth+1;
    Tree tmpTr=new Tree();
   if(dice.nextFloat() >= inInitGrowProba) {
      initializeTreeFull(ioPopulation.get(i), lInitDepth);
 //     ioPopulation.add(i,new Tree(tmpTr));
      ioPopulation.get(i).setValid(false);
      ioPopulation.get(i).setEvaluated(false);
 //     tmpTr.clear();
   }
    else {
        initializeTreeGrow(ioPopulation.get(i), inMinDepth, lInitDepth);
     //   ioPopulation.add(i,new Tree(tmpTr));
        ioPopulation.get(i).setValid(false);
        ioPopulation.get(i).setEvaluated(false);
      //  tmpTr.clear();
    }
    
  }
  evaluatePopulation(ioPopulation);
  return ioPopulation;
}

/**
 *  Initialize a GP tree with full approach.
 *  \param ioTree Tree to initialize.
 *  \param ioContext Evolutionary context.
 *  \param inDepth Actual depth to go in initialization.
 *  \return Generated tree size.
 *  \ingroup Puppy
 *
 *  If the tree is not empty, the initialization append the generated sub-tree to the actual tree.
 */
public int initializeTreeFull(Tree ioTree, int inDepth)
{
  
  if(inDepth >= 1){//assert was here
  
  if(inDepth == 1) {
    assert(Context.mTerminalSet.size() > 0);
    Primitive lTerminal = Context.mTerminalSet.get(dice.nextInt(Context.mTerminalSet.size()));
    ioTree.add(new Node(lTerminal.giveReference(dice),1));
    return 1;
  }

  assert(Context.mFunctionSet.size() > 0);
  Primitive lFunction =Context.mFunctionSet.get(dice.nextInt(Context.mFunctionSet.size()));
  int lNodeIndex = ioTree.size();
  ioTree.add(new Node(lFunction.giveReference(dice),0));
  int lNbArgs = ioTree.get(lNodeIndex).getPrimitive().getArity();
  int lTreeSize = 1;
//  Tree tmpT;
  for(int i=0; i<lNbArgs; ++i) {
     lTreeSize+= initializeTreeFull(ioTree, inDepth-1);
     //iTree=tmpT;
  }
  ioTree.get(lNodeIndex).setSubTreeSize(lTreeSize) ;
  return lTreeSize;
  }
  else return 0;
}

/**
 *  Initialize a GP tree with grow approach.
 *  \param ioTree Tree to initialize.
 *  \param ioContext Evolutionary context.
 *  \param inMinDepth Minimal depth to go in initialization.
 *  \param inMaxDepth Maximal depth to go in initialization.
 *  \return Generated tree size.
 *  \ingroup Puppy
 *
 *  If the tree is not empty, the initialization append the generated sub-tree to the actual tree.
 */
public int initializeTreeGrow(Tree ioTree, int inMinDepth, int inMaxDepth)
{
   if(inMinDepth >= 1 && inMinDepth <= inMaxDepth){
  
  Primitive lPrimit = null;
  if(inMinDepth > 1) {
    assert(Context.mFunctionSet.size() > 0);
    lPrimit = Context.mFunctionSet.get(dice.nextInt(Context.mFunctionSet.size()));
  }
  else if(inMaxDepth == 1) {
    assert(Context.mTerminalSet.size() > 0);
    lPrimit = Context.mTerminalSet.get(dice.nextInt(Context.mTerminalSet.size()));
  }
  else {
    int lIndexSel = dice.nextInt(Context.mFunctionSet.size() + Context.mTerminalSet.size());
    if(lIndexSel >= Context.mFunctionSet.size()) {
      lPrimit = Context.mTerminalSet.get(lIndexSel - Context.mFunctionSet.size());
    }
    else lPrimit = Context.mFunctionSet.get(lIndexSel);
  }

//  int lNodeIndex = tmpArr.size();
  int lNodeIndex = ioTree.size();
  ioTree.add(new Node(lPrimit.giveReference(dice),1));
  
  
  int lTreeSize = 1;
  int lMinDepth = (inMinDepth > 1) ? (inMinDepth-1) : 1;
  int lNbArgs = ioTree.get(lNodeIndex).getPrimitive().getArity();//index was lNodeIndex
 try{
      for(int i=0; i<lNbArgs; i++) {

             lTreeSize+=initializeTreeGrow(ioTree, lMinDepth, inMaxDepth-1);

         //ioTree=tmpT;
      }
  }
  catch(java.lang.StackOverflowError e){
      System.out.println(e+" in echange subtrees:"+inMinDepth+" "+inMaxDepth);
      System.exit(0);
  }
  ioTree.get(lNodeIndex).setSubTreeSize(lTreeSize);
  return lTreeSize;
   }
   else {
       System.out.println("The problem is here");
       return 0;
   }
}


/**
 *  Apply tournament selection to a population of trees.
 *  \param ioPopulation Population to apply selection on.
 *  \param ioContext Evolutionary context.
 *  \param inNumberParticipants Number of participants to each tournament selection.
 *  \ingroup Puppy
 *This function has to be modified. This only suits the replacement criteria but not an elitist criteria.
 *The routine also implements LPP by Sean Luke
 */
public ArrayList<Tree> applySelectionTournament(ArrayList<Tree> ioPopulation)
{
  if(ioPopulation.size() == 0) return null;
  
  int inNumberParticipants=Context.tournamentSize;
  ArrayList<Tree> childPop=new ArrayList(ioPopulation.size());
  
  int [] lIndices=new int[ioPopulation.size()];
  for(int i=0;i<ioPopulation.size();i++)
      lIndices[i]=0;
  for(int i=0; i<ioPopulation.size(); i++) {
    int lChoosenIndividual = dice.nextInt(ioPopulation.size());
    for(int j=1; j<inNumberParticipants; ++j) {
      int lTriedIndividual = dice.nextInt(ioPopulation.size());//the following line implements LPP
      if(ioPopulation.get(lTriedIndividual).compareRank(ioPopulation.get(lChoosenIndividual))||(ioPopulation.get(lChoosenIndividual).compareTrees(ioPopulation.get(lTriedIndividual)) && ioPopulation.get(lTriedIndividual).size()<ioPopulation.get(lChoosenIndividual).size()) ) {
        lChoosenIndividual = lTriedIndividual;
      }
    }
    ++lIndices[lChoosenIndividual];
  }
  
  int lNextEmpty  = 0;
  int lNextFilled = 0;
  while((lNextFilled < ioPopulation.size()) && (lIndices[lNextFilled] <= 1)) lNextFilled++;
  while(lNextFilled < ioPopulation.size()) {
    while(lIndices[lNextFilled] > 1) {
      while(lIndices[lNextEmpty]!= 0) ++lNextEmpty;
        childPop.add(ioPopulation.get(lNextFilled));//remove(lNextEmpty);
      //  ioPopulation.add(ioPopulation.get(lNextFilled));
        --lIndices[lNextFilled];
        ++lIndices[lNextEmpty];
    }
    while((lNextFilled < ioPopulation.size()) && (lIndices[lNextFilled] <= 1)) ++lNextFilled;
    
  }
  while(childPop.size()<ioPopulation.size())
        childPop.add(ioPopulation.get(dice.nextInt(ioPopulation.size())));//fill the remaining places randomly
  return childPop;
}

/**
 *  Apply tournament selection to a population of trees.
 *  \param ioPopulation Population to apply selection on.
 *  \param ioContext Evolutionary context.
 *  \param inNumberParticipants Number of participants to each tournament selection.
 *  \ingroup Puppy
 *This function has to be modified. This only suits the replacement criteria but not an elitist criteria.
 *The routine also implements LPP by Sean Luke
 */
public ArrayList<Tree> applySelectionTournament1(ArrayList<Tree> ioPopulation)
{
  if(ioPopulation.size() == 0) return null;
  
  ArrayList<Tree> childPop=new ArrayList(ioPopulation.size());
  int inNumberParticipants=Context.tournamentSize;
  //choose the first candidate and store in the child population
  while(childPop.size()<ioPopulation.size()){
  int jj=0;
  int lChosenCand1=0, lChosenCand2=0;
   int tmpCand1=ioPopulation.size()+1, tmpCand2=ioPopulation.size()+1;
  while(jj<inNumberParticipants){
      lChosenCand1=dice.nextInt(ioPopulation.size());
      if(tmpCand1<ioPopulation.size()){
          if(ioPopulation.get(lChosenCand1).compareRank(ioPopulation.get(tmpCand1))){//||(ioPopulation.get(lChosenCand).compareTrees(tmpCand) && ioPopulation.get(lChoosenCand).size()<ioPopulation.get(lTriedIndividual).size()) ) {
                tmpCand1=lChosenCand1;//choose an individual,               
          }
      }
      else
          tmpCand1=lChosenCand1;//choose this
      jj++;
  }
  childPop.add(new Tree(ioPopulation.get(tmpCand1)));//Add this to the child population
   //choose the second candidate now and store in the child population
  jj=0;
  while(jj<inNumberParticipants){
      lChosenCand2=dice.nextInt(ioPopulation.size());
      if(tmpCand2<=ioPopulation.size()){//implements Gustafson and LPP
          if(!ioPopulation.get(tmpCand1).compareTrees(ioPopulation.get(tmpCand2))
          && ioPopulation.get(lChosenCand2).compareRank(ioPopulation.get(tmpCand2))
          ||(ioPopulation.get(lChosenCand2).compareTrees(ioPopulation.get(tmpCand2))
          && ioPopulation.get(lChosenCand2).size()<ioPopulation.get(tmpCand2).size()) ) {
                tmpCand2=lChosenCand2;//choose an individual, 
                
          }
      }
      else
          tmpCand2=lChosenCand2;//choose this
      jj++;
  }
  childPop.add(new Tree(ioPopulation.get(tmpCand2)));//Add this to the child population
  }
 for(int i=0;i<ioPopulation.size();i++){
      childPop.get(i).usedMutationStd=false;
      childPop.get(i).usedMutationSwap=false;
      childPop.get(i).usedXover=false;
 }
  return childPop;
}

//what is this function doing?

/**
 *  Apply sub-tree crossover operation on a population of GP trees.
 *  \param ioPopulation Population to apply crossover on.
 *  \param ioContext Evolutionary context.
 *  \param inMatingProba Probability for each individual to be modified by crossover.
 *  \param inDistribProba Probability that a crossover exchange two sub-trees of non-terminal roots.
 *  \param inMaxTreeDepth Maximum tree depth allowed.
 *  \ingroup Puppy
 */
public void applyCrossover(ArrayList<Tree> ioPopulation)
{
  int inMaxTreeDepth=Context.maxTreeDepth;  
  float inDistribProba=Context.inInitGrowProba;//not used here but still
  float inMatingProba=Context.xoverProba;
  if((ioPopulation.size() % 2) != 0) ioPopulation.remove(lMateVector.size()-1);
//System.out.println("MATING VECTOR SIZE= "+lMateVector.size());
  //for(unsigned int j=0; j<lMateVector.size(); ++j) {
  //  std::cout << j << ": " << ioPopulation[lMateVector[j]] << std::endl;
  //}
 // childPop.clear();//=new ArrayList();
  for(int j=0; j<ioPopulation.size(); j+=2) {
   try{
              mateTrees(ioPopulation.get(j), ioPopulation.get(j+1));
   }
   catch(java.lang.NullPointerException e){
       System.out.println(e+" in apply crossover... call to mateTrees");
       System.exit(0);
   }
              try{
   //               childPop.add(trees.get(0));//this line is meant to implement Elitism, as it retains the parent and child pops
    //              childPop.add(trees.get(1));
                  
         //         ioPopulation.add(new Tree(ioPopulation.get(lMateVector.get(mate).intValue())));
              }
              catch(java.lang.Exception e){
                  
            //      System.out.println(j+" "+mate+" "+"WAITING In applyCrossover "+e);//+" "+ioPopulation.size());
           //       System.exit(0);
              }
  }
 
}

/**
 *  Mate two GP trees for crossover.
 *  \param ioTree1 First tree to mate.
 *  \param ioTree2 Second tree to mate.
 *  \param ioContext Evolutionary context.
 *  \param inDistribProba Distribution probability.
 *  \param inMaxTreeDepth Maximum tree depth allowed.
 *  \ingroup Puppy
 */
public void mateTrees(final Tree ioTree1, final Tree ioTree2)
{
  // Initial parameters checks
  assert(ioTree1.size() > 0);
  assert(ioTree2.size() > 0);
  
  float inDistribProba=Context.xoverNodeDistribProb;
  int inMaxTreeDepth=Context.maxTreeDepth;
//  ArrayList<Tree> trees=null;
  // Crossover loop. Try the given number of attempts to mate two individuals.
  for(int i=0; i<7; ++i) {

    // Choose a type of node (branch or leaf) following the distribution probability and change the
    // node for another node of the same tree if the types mismatch.
    boolean lNode1IsTerminal = true;
    if(ioTree1.size() > 1){
         if(dice.nextFloat() >= inDistribProba) lNode1IsTerminal =true;
         else lNode1IsTerminal = false;
    }
    int lChoosenNode1=0;
    try{
        lChoosenNode1= dice.nextInt(ioTree1.size());//cant choose the root node
    }
    catch(java.lang.IllegalArgumentException e){
        System.out.println(ioTree1.size()+"mate tree"+e);
        System.exit(0);
    }
    while((ioTree1.get(lChoosenNode1).getPrimitive().getArity() == 0) != lNode1IsTerminal) {
      lChoosenNode1 = dice.nextInt(ioTree1.size());
    }
    if(lChoosenNode1==0) lChoosenNode1+=1;

    boolean lNode2IsTerminal = true;
    if(ioTree2.size() > 1) lNode2IsTerminal = (dice.nextFloat() >= inDistribProba);
    int lChoosenNode2 = dice.nextInt(ioTree2.size());
    while((ioTree2.get(lChoosenNode2).getPrimitive().getArity() == 0) != lNode2IsTerminal) {
      lChoosenNode2 = dice.nextInt(ioTree2.size());
    }
    if(lChoosenNode2==0)lChoosenNode2+=1;
 //   lChoosenNode2=ioTree2.size()-1;//trial

    // Set first stack to the node of the first tree.
    // Check if depth is ok. Do a new crossover attempt if not.
    ArrayList<Integer> lStack1=ioTree1.setStackToNode(lChoosenNode1);
    int lNewDepthTree1 =lStack1.size() + ioTree2.getDepth(lChoosenNode2) - 1;
    if(lNewDepthTree1 > inMaxTreeDepth) continue;

    // Set second stack to the node of the second tree.
    // Check if depth is ok. Do a new crossover attempt if not.
    ArrayList<Integer> lStack2=ioTree2.setStackToNode(lChoosenNode2);
    int lNewDepthTree2 =lStack2.size() + ioTree1.getDepth(lChoosenNode1) - 1;
    if(lNewDepthTree2 > inMaxTreeDepth) continue;

    // The crossover is valid.
 //   try{
        exchangeSubTrees(ioTree1, lChoosenNode1, lStack1, ioTree2, lChoosenNode2, lStack2);
  //      ioTree1.setValid(false);
  //      ioTree2.setValid(false);
  //      trees.get(0).setValid(false);
  //      trees.get(1).setValid(false);
   // }
 //   catch(java.lang.Exception e){
     //   System.out.println(e+"The return value is null in mate-trees: ");
       // System.exit(0);
   // }
    break;
  }
  
//  return trees;
}


/**
 *  Exchange two sub-trees.
 *  \param ioTree1  Tree containing the first sub-tree to exchange.
 *  \param inNode1  Index of root node to sub-tree to swap in first tree.
 *  \param inStack1 Stack containing the parents to the first sub-tree root node.
 *  \param ioTree2  Tree containing the second sub-tree to exchange.
 *  \param inNode2  Index of root node to sub-tree to swap in second tree.
 *  \param inStack2 Stack containing the parents to the second sub-tree root node.
 *  \ingroup Puppy
 */
public void exchangeSubTrees(final Tree iTree1, int inNode1, final ArrayList<Integer> inStack1, final Tree iTree2, int inNode2, final ArrayList<Integer> inStack2)
{
//  assert(iTree1 != iTree2); //I can mate mate between similar trees as well, in the worst case. See the calls to this func
  assert(inStack1.size() > 0);
  assert(inStack2.size() > 0);

  Tree ioTree1=new Tree(iTree1);
  Tree ioTree2=new Tree(iTree2);
  
  int lSwapSize1 = ioTree1.get(inNode1).getSubTreeSize();
  int lSwapSize2 = ioTree2.get(inNode2).getSubTreeSize();
  
  Tree ioSubTree1=new Tree();
  Tree ioSubTree2=new Tree();
  
  for(int i=0;i<lSwapSize1;i++){
      try{
        ioSubTree1.add(ioTree1.remove(inNode1));//I had i here NEW    
      }
      catch(java.lang.IndexOutOfBoundsException e){
          System.out.println("exchangeSubTrees1 "+e);
          System.exit(0);
      }
  }
  for(int i=0;i<lSwapSize2;i++){ 
          try{
              ioSubTree2.add(ioTree2.remove(inNode2));//I had i here  NEW 
          }
          catch(java.lang.IndexOutOfBoundsException e){
              System.out.println(" exchangeSubTrees2 "+e);
              System.exit(0);       
          }    
  }  
      ioTree1.addAll(inNode1, ioSubTree2);
      ioTree2.addAll(inNode2, ioSubTree1);
  
  int lDiffSize = lSwapSize1 - lSwapSize2;
  
  for(int i=0; i<(inStack1.size()-1); ++i)
      try{
    ioTree1.get(inStack1.get(i).intValue()).setSubTreeSize(ioTree1.get(inStack1.get(i).intValue()).getSubTreeSize()-lDiffSize) ;
      }
      catch(java.lang.IndexOutOfBoundsException e)
  {
          System.out.println(" exchangeSubTrees4 "+e);
          System.exit(0);
   }
  for(int j=0; j<(inStack2.size()-1); ++j)
    try{  
    ioTree2.get(inStack2.get(j).intValue()).setSubTreeSize(ioTree2.get(inStack2.get(j).intValue()).getSubTreeSize()+lDiffSize);
    }
    catch(java.lang.IndexOutOfBoundsException e)
  {
          System.out.println("exchangeSubTrees5 "+e);
          System.exit(0);
   }
  iTree1.clear();
  iTree1.addAll(ioTree1);
  iTree2.clear();
  iTree2.addAll(ioTree2);
  iTree1.setEvaluated(false);
  iTree2.setEvaluated(false);
  iTree1.setValid(false);
  iTree2.setValid(false);
  iTree1.usedXover=true;
  iTree2.usedXover=true;
}

/**
 *  Apply standard (Koza's) mutation on a GP trees.
 *  This performs subtree mutation
 *  Input: - ioTree GP tree to mutate.
 *           ioContext Evolutionary context.
 *           inMaxRegenDepth Maximum tree regeneration depth allowed.
 *           inMaxDepth Maximum tree depth allowed.
 *  Output: -
 *           ioTree - Mutated GP Tree
 *  \ingroup Puppy
 */
public void mutateStandard(Tree ioTree, int inMaxRegenDepth, int inMaxDepth)
{
  // System.out.println("Following is the tree before mutation: \n");//+ ioTree.writeTree(0));
  // for(int i=0;i<ioTree.size();i++){
  //      System.out.print(ioTree.get(i).getPrimitive().getName()+ioTree.get(i).getSubTreeSize()+" ");
  // } 
   
  if(ioTree.size()<=0)System.out.println("HUGE ERROR IN MUTATESTANDARD");
  assert(ioTree.size() > 0);
  int lMutIndex = dice.nextInt(ioTree.size()-1);
  if (lMutIndex==0)lMutIndex+=1;
  Tree lNewTree=new Tree();
  for(int i=0;i<lMutIndex;i++){
      lNewTree.add(ioTree.get(i));//NEW
  }
  
  ArrayList<Integer> lStack=ioTree.setStackToNode(lMutIndex);
  lStack.remove(lStack.size()-1);
  int lTreeDepth = dice.nextInt(inMaxRegenDepth)+1;
  int lTreeDepth2 = inMaxDepth - lStack.size();
  if(lTreeDepth2 < lTreeDepth) lTreeDepth = lTreeDepth2;
  assert(lTreeDepth > 0);
  
  initializeTreeGrow(lNewTree, 1, lTreeDepth);
  for(int i=(lMutIndex+ioTree.get(lMutIndex).getSubTreeSize());i<ioTree.size() ;i++){
    try{    
      lNewTree.add(ioTree.get(i));//NEW
    }
    catch(java.lang.IndexOutOfBoundsException e){
        System.out.println(e+" in mutateStandard"+" "+lMutIndex+" "+ioTree.get(lMutIndex).getSubTreeSize()+" "+ioTree.size());
        System.exit(0);
    }
  }
  int lDiffSize=0;
  try{
      lDiffSize =ioTree.get(lMutIndex).getSubTreeSize() - lNewTree.get(lMutIndex).getSubTreeSize();
  }
  catch(java.lang.Exception e){
      System.out.println(e+" mutIndex: "+lMutIndex);
 //     System.out.println(ioTree.writeTree(0));
      for(int ii=0;ii<ioTree.size();ii++)
          System.out.print(ioTree.get(ii).getSubTreeSize()+" ");
      System.exit(0);
  }
  for(int i=0; i<lStack.size(); ++i) 
      lNewTree.get(lStack.get(i)).setSubTreeSize(lNewTree.get(lStack.get(i)).getSubTreeSize()-lDiffSize);
 // ioTree=null;
//  ioTree=lNewTree;
  lNewTree.setValid(false);
  lNewTree.setEvaluated(false);
  ioTree.setValid(false);
  ioTree.setEvaluated(false);
  ioTree.clear();
  for(int i=0;i<lNewTree.size();i++)
      ioTree.add(new Node(lNewTree.get(i)));
ioTree.usedMutationStd=true;
//  System.out.println("Following is the tree after mutation: \n");//+ ioTree.writeTree(0));
//   for(int i=0;i<ioTree.size();i++){
//        System.out.print(ioTree.get(i).getPrimitive().getName()+ioTree.get(i).getSubTreeSize()+" ");
//   }
//  System.out.println("\nMutIndex: "+lMutIndex);
//  return lNewTree;
}

/*!
 *  \brief Apply swap point mutation to a population of GP trees.
 *  \param ioPopulation Population to mutate.
 *  \param ioContext Evolutionary context.
 *  \param inMutationProba Mutation probability.
 *  \param inDistribProba Probability to mutate a function node, in opposition to a terminal.
 *  \ingroup Puppy
 */
public void applyMutationSwap(ArrayList<Tree> ioPopulation)
{
  float inMutationProba=Context.swapMutationProba;
  for(int i=0; i<ioPopulation.size(); ++i) {
    if(dice.nextFloat() < inMutationProba) {
      mutateSwap(ioPopulation.get(i));
    }
  }
}

/*!
 *  \brief Swap mutate a GP tree.
 *  \param ioTree GP tree to mutate.
 *  \param ioContext Evolutionary context.
 *  \param inDistribProba Probability to mutate a function node, in opposition to a terminal.
 *  \ingroup Puppy
 */
public void mutateSwap(Tree ioTree)
{
  float inDistribProba=Context.xoverNodeDistribProb;
  assert(ioTree.size() > 0);
  int lMutIndex = dice.nextInt(ioTree.size());
  if(ioTree.size() > 1) {
    boolean lType = (dice.nextFloat() < inDistribProba);
    while((ioTree.get(lMutIndex).getPrimitive().getArity() != 0) != lType) {
      lMutIndex = dice.nextInt(ioTree.size());
    }
  }
  int lNbArgs = ioTree.get(lMutIndex).getPrimitive().getArity();
  if(lNbArgs == 0) {
    assert(Context.mTerminalSet.size() > 0);
    Primitive lTerminal =
      Context.mTerminalSet.get(dice.nextInt(Context.mTerminalSet.size()));
    ioTree.get(lMutIndex).setPrimitive(lTerminal.clone().giveReference(dice));
  }
  else {
     ArrayList<Integer> lKArgsFunction=new ArrayList();
    for(int i=0; i<Context.mFunctionSet.size(); ++i) {
      if(Context.mFunctionSet.get(i).getArity() == lNbArgs) {
        lKArgsFunction.add(new Integer(i));
      }
    }
    assert(lKArgsFunction.size() > 0);
    Primitive lFunction =Context.mFunctionSet.get(lKArgsFunction.get(dice.nextInt(lKArgsFunction.size())));
    ioTree.get(lMutIndex).setPrimitive(lFunction.clone());
  }
  ioTree.usedMutationSwap=true;
}

/**
 *   Apply standard (Koza's) mutation to a population of GP trees.
 *  \param ioPopulation Population to mutate.
 *  \param ioContext Evolutionary context.
 *  \param inMutationProba Mutation probability.
 *  \param inMaxRegenDepth Maximum tree regeneration depth allowed.
 *  \param inMaxDepth Maximum tree depth allowed.
 *  \ingroup Puppy
 */
public void applyMutationStandard(ArrayList<Tree> ioPopulation)
{
    float inMutationProba=Context.mutationProba;
    int inMaxRegenDepth=Context.initTreeDepthMax;
    int inMaxDepth=Context.maxTreeDepth;
 //   System.out.println("Lets see how Mutation works\n"+ioPopulation.get(this.parentPopSize).writeTree(0));
  for(int i=this.parentPopSize; i<ioPopulation.size(); i++) {
    if( dice.nextFloat()<= inMutationProba) {
        mutateStandard(ioPopulation.get(i), inMaxRegenDepth, inMaxDepth);
    }
  }
   // System.out.println("Mutation worked like this\n"+ioPopulation.get(this.parentPopSize).writeTree(0));
 //   return ioPopulation;
}

/**
 *adapt the probabilities of genetic operators in a dynamic fashion
 */
public void adaptOperatorProbabilities(List<Tree> lPopulation){
    Iterator<Tree> popItr=lPopulation.iterator();
    int numUsedXover=1, numUsedMutStd=1, numUsedMutSwap=1;
    int numImpXover=0, numImpMutStd=0, numImpMutSwap=0;
    while(popItr.hasNext()){
        Tree tmpTree=popItr.next();
        if(tmpTree.usedXover){
            numUsedXover++;
            if(tmpTree.getTrainingFitness()<=Context.previousBest)
                numImpXover++;
        }
        if(tmpTree.usedMutationStd){
            numUsedMutStd++;
            if(tmpTree.getTrainingFitness()<=Context.previousBest)
                numImpMutStd++;
        }
        if(tmpTree.usedMutationSwap){
            numUsedMutSwap++;
            if(tmpTree.getTrainingFitness()<=Context.previousBest)
                numImpMutSwap++;
        }
    }
    Context.xoverProba=(float)(numImpXover/numUsedXover);
    Context.mutationProba=(float)(numImpMutStd/numUsedMutStd);
    Context.swapMutationProba=(float)(numImpMutSwap/numUsedMutSwap);
    
    if(Context.xoverProba<0.5F)Context.xoverProba=0.7F;
    if(Context.mutationProba<0.01F)Context.mutationProba=0.15F;
    if(Context.swapMutationProba<0.01F)Context.swapMutationProba=0.15F;
}

/**
 *Perform all Evolutionary steps in one go
 *1) ApplySelectionTournament
 *2) ApplyCrossover
 *3) ApplyMutationStandard
 *4) Evaluate fitness of the population
 *5) Apply elitist survival
 */

public Tree applyAllEvolutionarySteps(ArrayList<Tree> lPopulation){
    ArrayList<Tree> childPop=applySelectionTournament1(lPopulation);
    applyCrossover(childPop);
    applyMutationStandard(childPop);
    applyMutationSwap(childPop);
    //Set the S-Expressions now here as this will save some computation
    lPopulation.addAll(childPop);
    evaluatePopulation(lPopulation);
    adaptOperatorProbabilities(lPopulation.subList(Context.popSize, lPopulation.size()-1));
    this.applySurvival1(lPopulation);
    for(int i=0;i<10;i++)
        lPopulation.get(i).tuneTreesCoeffs();
    Collections.sort(lPopulation, new FitnessComparator());//sort again if GA is used
    return lPopulation.get(0);//return the best tree as it remains the first element after sorting
}

/**
 *Evaluate a given population
 */
public void evaluatePopulation(ArrayList<Tree> lPopulation){
    Iterator<Tree> it=lPopulation.iterator();
    double bestFit=Double.MAX_VALUE;
    while(it.hasNext()){
        Tree tmpTree=it.next();
        //new Thread(tmpTree).start();
        double fitness=tmpTree.evaluateTree();
    //    it.next().tuneTreesCoeffs();
        if(fitness<Context.previousBest)Context.previousBest=fitness;
   }
}

/**
 *an elitist survival criterion
 */
public void applySurvival1(ArrayList<Tree> ioPopulation){
    Collections.sort(ioPopulation.subList(0,ioPopulation.size()/2-1), new FitnessComparator());//Sort the pop wrt fitness first in descending order
    Collections.sort(ioPopulation.subList(ioPopulation.size()/2, ioPopulation.size()-1), new FitnessComparator());//Sort the pop wrt fitness first in descending order
//    Collections.sort(ioPopulation, new DepthComparator())//Sort wrt depth as well now -- I guess no need for this sort
    int dynDepth=ioPopulation.get(ioPopulation.size()/2).getDepth(0);//of the best child
//    if(dynDepth>this.dynamicLevel)
    if(dynDepth>=6 && ioPopulation.get(ioPopulation.size()/2).getTrainingFitness()<ioPopulation.get(0).getTrainingFitness())
        this.dynamicLevel=dynDepth;
    int children_;
    for(int i=ioPopulation.size()/2;i<ioPopulation.size();i++){//remove from among childPop
        Tree tmpTree=ioPopulation.get(i);
        if(tmpTree.getDepth(0)>this.dynamicLevel){//dynamic tree depth
                ioPopulation.remove(i);
                i--;
        }
    }
    while(ioPopulation.subList(this.parentPopSize, ioPopulation.size()).size()>this.parentPopSize/2)
        ioPopulation.remove(ioPopulation.size()-1);//remove the extra children here
    int i=this.parentPopSize-1;
    while(ioPopulation.size()>this.parentPopSize)
        ioPopulation.remove(i--);//remove the less fit parents now
    Collections.sort(ioPopulation, new FitnessComparator());
    Context.previousBest=ioPopulation.get(0).getTrainingFitness();
    System.out.println(this.dynamicLevel);
} 

}

 

/**
 *Fitness Comparator
 */

class FitnessComparator implements java.util.Comparator<Tree>{
        public int compare(Tree o1, Tree o2) {
            double fit1=o1.getTrainingFitness();
            double fit2=o2.getTrainingFitness();
            if (fit1<fit2) return -1;
            if(fit1==fit2)return 0;
            return 1;
        }
}

 

/**

 *Size (Tree Depth) Comparator

 */

class DepthComparator implements java.util.Comparator<Tree>{
        public int compare(Tree o1, Tree o2) {
            int depth1=o1.getDepth(0);
            int depth2=o2.getDepth(0);
            if(depth1<depth2) return -1;
            if(depth1==depth2) return 0;
            return 1;
        }

}
