package filter.cytoscape;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import filter.model.*;

import javax.swing.border.*;
import java.beans.*;
import javax.swing.event.SwingPropertyChangeSupport;

import cytoscape.*;
import cytoscape.data.*;
import cytoscape.view.CyWindow;
import cytoscape.data.GraphObjAttributes;
import giny.model.*;
import cytoscape.CyNetwork;
import ViolinStrings.Strings;

/**
 * This is a Cytoscape specific filter that will pass nodes if
 * a selected attribute matches a specific value.
 */
public class NumericAttributeFilter
  implements Filter{
  
  //----------------------------------------//
  // Filter specific properties 
  //----------------------------------------//
  protected String selectedAttribute;
  protected Number searchNumber;
  protected String comparison;
  protected Class classType;
  protected Class NODE_CLASS;
  protected Class EDGE_CLASS;
  public static String EQUAL = "=";
  public static String LESS = "<";
  public static String GREATER = ">";
  public static String NODE = "Node";
  public static String EDGE = "Edge";
		
  public static String SEARCH_NUMBER_EVENT = "SEARCH_STRING_EVENT";
  public static String SELECTED_ATTRIBUTE_EVENT = "SELECTED_ATTRIBUTE_EVENT";
  public static String FILTER_NAME_EVENT = "FILTER_NAME_EVENT";
  public static String CLASS_TYPE_EVENT = "CLASS_TYPE";
  public static String FILTER_ID = "Numeric Attribute Filter";
  public static String FILTER_DESCRIPTION ="Select nodes or edges based on the value of a numeric attribute"; 
  public static String COMPARISON_EVENT = "COMPARISON_EVENT";
		
  //----------------------------------------//
  // Cytoscape specific Variables
  //----------------------------------------//
  protected CyWindow cyWindow;


  //----------------------------------------//
  // Needed Variables
  //----------------------------------------//
  protected String identifier = "default";
  protected SwingPropertyChangeSupport pcs = new SwingPropertyChangeSupport(this);
  
  
  //---------------------------------------//
  // Constructor
  //----------------------------------------//

  /**
   * Creates a new NumericAttributeFilter
   */  
  public NumericAttributeFilter ( CyWindow cyWindow,
				  String comparison,
				  String classString,
				  String selectedAttribute, 
				  Number searchNumber,
				  String identifier){
    this.cyWindow = cyWindow;
    this.comparison = comparison;
    
    try{
      NODE_CLASS = Class.forName("giny.model.Node");
      EDGE_CLASS = Class.forName("giny.model.Edge");
    }catch(Exception e){
      e.printStackTrace();
    }
    this.selectedAttribute = selectedAttribute;  
    this.searchNumber = searchNumber;
    this.identifier =identifier;
    setClassType(classString);
  }

  /**
   * Creates a new NumericAttributeFilter
   */  
  public NumericAttributeFilter ( String desc){
    this.cyWindow = Cytoscape.getDesktop();
    try{
      NODE_CLASS = Class.forName("giny.model.Node");
      EDGE_CLASS = Class.forName("giny.model.Edge");
    }catch(Exception e){
      e.printStackTrace();
    }
    input(desc);
   
  }

  
  //----------------------------------------//
  // Implements Filter
  //----------------------------------------//

  /**
   * Returns the name for this Filter
   */
  public String toString () {
    return identifier;
  }

  /**
   * sets a new name for this filter
   */
  public void setIdentifier ( String new_id ) {
    this.identifier = new_id;
    pcs.firePropertyChange(FILTER_NAME_EVENT,null,new_id);
  }

  
  /**
   * This is usually the same as the class name
   */
  public String getFilterID () {
    return FILTER_ID;
  }
  
  public String getDescription () {
    return FILTER_DESCRIPTION;
  }
  
  
  /**
   * An Object Passes this Filter if its "toString" method
   * matches any of the Text from the TextField
   */
  public boolean passesFilter ( Object object ) {
    if ( !classType.isInstance(object)) {
      return false;
    }
    GraphObjAttributes objectAttributes = null;
    if(classType.equals(NODE_CLASS)){
      objectAttributes = cyWindow.getNetwork().getNodeAttributes();
    }
    else{
      objectAttributes = cyWindow.getNetwork().getEdgeAttributes();
    }
    String name = objectAttributes.getCanonicalName(object);
    if(name == null){
      return false;
    }
    Number value = (Number)objectAttributes.getValue( selectedAttribute,name );
    if( value == null){
      return false;
    }
    if(comparison == EQUAL){
      return searchNumber.doubleValue() == value.doubleValue();
    }
    else if(comparison == LESS){
      return searchNumber.doubleValue() > value.doubleValue();
    }
    else if(comparison == GREATER){
      return searchNumber.doubleValue() < value.doubleValue();
    }
    else{
      //System.err.println("Comparison not identified");
      return false;
    }
				
				
  }

  public Class[] getPassingTypes () {
    return null;
  }
  
  public boolean equals ( Object other_object ) {
    return super.equals(other_object);
  }
  
  public Object clone () {
    return new NumericAttributeFilter ( cyWindow, comparison,getClassType(),selectedAttribute, searchNumber, identifier+"_new" );
  }
  
  public SwingPropertyChangeSupport getSwingPropertyChangeSupport() {
    return pcs;
  }

   // SearchString /////////////////////////////////

  public Number getSearchNumber () {
    return searchNumber;
  }

  public void setSearchNumber ( Number searchNumber ) {
    this.searchNumber = searchNumber;
    pcs.firePropertyChange( SEARCH_NUMBER_EVENT, null, searchNumber );
  }

  // Selected_Attribute ///////////////////////////
  public String getSelectedAttribute () {
    return selectedAttribute;
  }

  public void setSelectedAttribute ( String new_attr ) {
    this.selectedAttribute = new_attr;
    pcs.firePropertyChange( SELECTED_ATTRIBUTE_EVENT, null, selectedAttribute );
  }

   public void setClassType(String classString){
    if(classString == NODE || classString.equals("Node")){
      this.classType = NODE_CLASS;
    }else{
      this.classType = EDGE_CLASS;
    }
    pcs.firePropertyChange(CLASS_TYPE_EVENT,null,classString);
  }

  public String getClassType(){
    if(classType == NODE_CLASS){
      return NODE;
    }else{
      return EDGE;
    }
  }

  public void setComparison(String comparison){
    this.comparison = comparison;
    pcs.firePropertyChange(COMPARISON_EVENT,null,comparison);	
  }

  public String getComparison(){
    return comparison;
  }
		
  
  


  //----------------------------------------//
  // IO
  //----------------------------------------//

  public String output () {
    StringBuffer buffer = new StringBuffer();
    buffer.append( getComparison()+"," );
    buffer.append( getClassType()+"," );
    buffer.append( getSelectedAttribute()+"," );
    buffer.append( getSearchNumber()+"," );
    buffer.append( toString() );
    return buffer.toString();
  }

  public void input(String desc){
    String [] array = desc.split(",");
    setComparison(array[0]);
    setSelectedAttribute(array[2]);  
    setSearchNumber(new Double( array[3] ));
    setIdentifier(array[4]);
    setClassType(array[1]);
  }
  
}

