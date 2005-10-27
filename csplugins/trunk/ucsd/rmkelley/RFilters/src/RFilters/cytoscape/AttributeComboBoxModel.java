package filter.cytoscape;

import java.util.*;
import filter.view.*;
import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;

public abstract class AttributeComboBoxModel implements ComboBoxModel, PropertyChangeListener{

  protected Object selectedObject;
  protected Vector attributeList;
 
  protected AttributeComboBoxModel () {
    attributeList = new Vector();
    Cytoscape.getSwingPropertyChangeSupport().addPropertyChangeListener(this);
  }


  public void notifyListeners(){
    for(Iterator listenIt = listeners.iterator();listenIt.hasNext();){
      ((ListDataListener)listenIt.next()).contentsChanged(new ListDataEvent(this,ListDataEvent.CONTENTS_CHANGED,0,attributeList.size()));
    }
  }
  
  //implements PropertyChange
  public abstract void propertyChange(PropertyChangeEvent pce);


  //implements ListModel
  Vector listeners = new Vector();
  public void addListDataListener(ListDataListener l){
    listeners.add(l);
  }
    
  public Object getElementAt(int index){
    return attributeList.elementAt(index);
  }
   
  public int getSize(){
    return attributeList.size();
  }
  
  public void removeListDataListener(ListDataListener l){
    listeners.remove(l);
  }

  public void setSelectedItem(Object item){
    selectedObject = item;
  }

  public Object getSelectedItem(){
    return selectedObject;
  }
      

}

class NodeAttributeComboBoxModel extends AttributeComboBoxModel{
  Class attributeClass;
  
  CyAttributes nodeAttributes;
  public NodeAttributeComboBoxModel(Class attributeClass){
    super();
    nodeAttributes = Cytoscape.getNodeAttributes();
    this.attributeClass = attributeClass;
    updateAttributes();
  }
  public void propertyChange(PropertyChangeEvent pce){
    updateAttributes();
  }

  protected void updateAttributes(){
    byte type;
    if ( attributeClass == Double.class )
      type = CyAttributes.TYPE_FLOATING;
    else if ( attributeClass == Integer.class )
      type = CyAttributes.TYPE_INTEGER;
    else if ( attributeClass == String.class )
      type = CyAttributes.TYPE_STRING;
    else 
      return;

    String [] na = Cytoscape.getNodeAttributesList();
    attributeList = new Vector();
    for ( int idx = 0; idx < na.length; idx++) {
      if ( nodeAttributes.getType( na[idx] ) == type ) {
        attributeList.add(na[idx]);
      } 
      notifyListeners();
    }
  }
}

class EdgeAttributeComboBoxModel extends AttributeComboBoxModel{
  Class attributeClass;
  

  CyAttributes edgeAttributes;
  public EdgeAttributeComboBoxModel(Class attributeClass){
    super();
    edgeAttributes = Cytoscape.getEdgeAttributes();
    this.attributeClass = attributeClass;
    updateAttributes();
  }
  public void propertyChange(PropertyChangeEvent pce){
    updateAttributes();
  }
  
  protected void updateAttributes(){
    byte type;
    if ( attributeClass == String.class )
      type = CyAttributes.TYPE_STRING;
    else if ( attributeClass == Double.class )
      type = CyAttributes.TYPE_FLOATING;
    else if ( attributeClass == Integer.class )
      type = CyAttributes.TYPE_INTEGER;
    else 
      return;

    String [] ea = Cytoscape.getEdgeAttributesList();
    attributeList = new Vector();
    for ( int idx = 0; idx < ea.length; idx++) {
      if ( edgeAttributes.getType( ea[idx] ) == type ) {
        attributeList.add(ea[idx]);
      } 
      notifyListeners();
    }
  }
}
