/** Copyright (c) 2002 Institute for Systems Biology and the Whitehead Institute
 **
 ** This library is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Lesser General Public License as published
 ** by the Free Software Foundation; either version 2.1 of the License, or
 ** any later version.
 ** 
 ** This library is distributed in the hope that it will be useful, but
 ** WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 ** MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 ** documentation provided hereunder is on an "as is" basis, and the
 ** Institute for Systems Biology and the Whitehead Institute 
 ** have no obligations to provide maintenance, support,
 ** updates, enhancements or modifications.  In no event shall the
 ** Institute for Systems Biology and the Whitehead Institute 
 ** be liable to any party for direct, indirect, special,
 ** incidental or consequential damages, including lost profits, arising
 ** out of the use of this software and its documentation, even if the
 ** Institute for Systems Biology and the Whitehead Institute 
 ** have been advised of the possibility of such damage.  See
 ** the GNU Lesser General Public License for more details.
 ** 
 ** You should have received a copy of the GNU Lesser General Public License
 ** along with this library; if not, write to the Free Software Foundation,
 ** Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 **/
//-------------------------------------------------------------------------
// $Revision$
// $Date$
// $Author$
//-------------------------------------------------------------------------
package cytoscape.data;
//-------------------------------------------------------------------------
import java.io.*;
import javax.swing.JOptionPane;

import y.base.*;
import y.view.Graph2D;

import cytoscape.GraphObjAttributes;
//-------------------------------------------------------------------------
/**
 * This class provides static methods that operate on a CyNetwork to perform
 * various useful tasks. Many of these methods make assumptions about the
 * data types that are available in the node and edge attributes of the network.
 */
public class CyNetworkUtilities {
    
    /**
     * Returns an array containing all of the unique interaction types present
     * in the network. Formally, gets from the edge attributes all of the unique
     * values for the "interaction" attribute.
     */
    public static String[] getInteractionTypes(CyNetwork network) {
        return network.getEdgeAttributes().getUniqueStringValues("interaction");
    }
    
    /**
     * Returns the interaction type of the given edge. Formally, gets from the
     * edge attributes the value for the "interaction" attribute".
     */
    public static String getInteractionType(CyNetwork network, Edge e) {
        String canonicalName = network.getEdgeAttributes().getCanonicalName(e);
        return network.getEdgeAttributes().getStringValue("interaction", canonicalName);
    }
    
    /**
     * Saves all selected nodes in the network to a file with the given name.
     */
    public static boolean saveSelectedNodeNames(CyNetwork network, String filename) {
        String callerID = "CyNetworkUtilities.saveSelectedNodeNames";
        network.beginActivity(callerID);
        Graph2D theGraph = network.getGraph();
        Node[] nodes = theGraph.getNodeArray();
        GraphObjAttributes nodeAttributes = network.getNodeAttributes();
        File file = new File(filename);
        try {
            FileWriter fout = new FileWriter(file);
            for (int i=0; i < nodes.length; i++) {
                Node node = nodes[i];
                if(theGraph.isSelected(node)) {
                    String canonicalName = nodeAttributes.getCanonicalName(node);
                    fout.write(canonicalName + "\n");
                }
            } // for i
            fout.close();
            network.endActivity(callerID);
            return true;
        }  catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.toString(),
                                          "Error Writing to \"" + file.getName()+"\"",
                                          JOptionPane.ERROR_MESSAGE);
            network.endActivity(callerID);
            return false;
        }
    } // saveSelectedNodeNames
}

