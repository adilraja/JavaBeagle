/*
 * Node.java
 *
 * Created on August 11, 2007, 11:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GeneticProgramming;

import GeneticProgramming.Primitives.Primitive;
import java.io.*;

/**
 *
 * @author adilraja
 */
public class Node implements Serializable{
    private Primitive mPrimitive;    //!< Smart pointer to the associated primitive.
  private int    mSubTreeSize;  //!< Sub-tree size, including actual node.
    
    /** Creates a new instance of Node */
    public Node(Primitive primitive, int size) {
        this.mPrimitive=primitive.clone();
        this.mSubTreeSize=size;
    }
    
    /**
     *Copy Constructor
     */
    public Node(Node copy){
        //this.mPrimitive=new Primitive(copy.mPrimitive);
        this.mPrimitive=copy.mPrimitive.clone();
        this.mSubTreeSize=copy.mSubTreeSize;
    }
    
    /**
     *Set the primitive
     */
    public void setPrimitive(final Primitive prim){
    //    this.mPrimitive=new Primitive(prim);
        this.mPrimitive=prim.clone();
    }
    
    /**
     *Return the primitive
     */
    public final Primitive getPrimitive(){
        return this.mPrimitive;
    }
    /**
     *void subtree size
     */
    public void setSubTreeSize(final int treeSize){
        this.mSubTreeSize=treeSize;
    }
    
    /**
     *Return the subtree size
     */
    public final int getSubTreeSize(){
        return this.mSubTreeSize;
    }
}
