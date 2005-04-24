package cytoscape.data.attr.util.test;

import cytoscape.data.attr.CountedEnumeration;
import cytoscape.data.attr.CyData;
import cytoscape.data.attr.CyDataDefinition;
import cytoscape.data.attr.util.CyDataFactory;
import cytoscape.data.attr.util.CyDataHelpers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class TestCyData
{

  public final static void main(final String[] args)
  {
    Object o = CyDataFactory.instantiateDataModel();
    final CyDataDefinition def = (CyDataDefinition) o;
    final CyData data = (CyData) o;
    RuntimeException exc = null;
    def.defineAttribute("p-values", CyDataDefinition.TYPE_FLOATING_POINT,
                        new byte[] { CyDataDefinition.TYPE_STRING,
                                     CyDataDefinition.TYPE_INTEGER });
    data.setAttributeValue("node1", "p-values", new Double(0.5),
                           new Object[] { "Ideker", new Integer(0) });
    data.setAttributeValue("node1", "p-values", new Double(0.6),
                           new Object[] { "Ideker", new Integer(1) });
    data.setAttributeValue("node1", "p-values", new Double(0.6),
                           new Object[] { "Ideker", new Integer(2) });
    data.setAttributeValue("node1", "p-values", new Double(0.7),
                           new Object[] { "Salk", new Integer(0) });
    data.setAttributeValue("node1", "p-values", new Double(0.6),
                           new Object[] { "Salk", new Integer(1) });
    data.setAttributeValue("node2", "p-values", new Double(0.4),
                           new Object[] { "Salk", new Integer(0) });
    data.setAttributeValue("node2", "p-values", new Double(0.2),
                           new Object[] { "Weirdo", new Integer(0) });
    data.setAttributeValue("node3", "p-values", new Double(0.1),
                           new Object[] { "Foofoo", new Integer(11) });
    data.setAttributeValue("node4", "p-values", new Double(0.9),
                           new Object[] { "BarBar", new Integer(9) });
    try { data.setAttributeValue("node4", "p-values", new Double(0.4),
                                 new Object[] { "BarBar", new Long(1) }); }
    catch (ClassCastException e) { exc = e; }
    if (exc == null) throw new IllegalStateException("expected exception");
    exc = null;
    try { data.setAttributeValue("node5", "p-values", new Double(0.4),
                                 new Object[] { "BarBar", new Long(1) }); }
    catch (ClassCastException e) { exc = e; }
    if (exc == null) throw new IllegalStateException("expected exception");
    exc = null;

    def.defineAttribute("color", CyDataDefinition.TYPE_STRING, null);
    data.setAttributeValue("node1", "color", "red", null);
    data.setAttributeValue("node4", "color", "cyan", null);
    data.setAttributeValue("node8", "color", "yellow", null);

    try { data.removeAttributeValue("node1", "p-values",
                                    new Object[] { "Salk", new Long(1) }); }
    catch (ClassCastException e) { exc = e; }
    if (exc == null) throw new IllegalStateException("expected exception");
    exc = null;
    if (!(data.removeAttributeValue
          ("node4", "p-values",
           new Object[] { "BarBar", new Integer(9) }).equals(new Double(0.9))))
      throw new IllegalStateException("expected to remove 0.9");
    if (!(data.removeAttributeValue("node4", "color", null).equals("cyan")))
      throw new IllegalStateException("expected to remove cyan");

    Enumeration attrDefEnum = def.getDefinedAttributes();
    while (attrDefEnum.hasMoreElements()) {
      String attrDefName = (String) attrDefEnum.nextElement();
      System.out.println("ATTRIBUTE DOMAIN " + attrDefName + ":");
      Enumeration objKeyEnum = data.getObjectKeys(attrDefName);
      while (objKeyEnum.hasMoreElements()) {
        String objKey = (String) objKeyEnum.nextElement();
        System.out.println("(" + objKey + ")");
        List keySeqList = CyDataHelpers.getAllAttributeKeys
          (objKey, attrDefName, data, def);
        Iterator keySeqIter = keySeqList.iterator();
        while (keySeqIter.hasNext()) {
          System.out.print(objKey);
          Object[] keySeq = (Object[]) keySeqIter.next();
          for (int i = 0; i < keySeq.length; i++)
            System.out.print("." + keySeq[i]);
          System.out.print(" = ");
          System.out.println(data.getAttributeValue(objKey, attrDefName,
                                                    keySeq)); } }
      System.out.println(); }
  }

}
