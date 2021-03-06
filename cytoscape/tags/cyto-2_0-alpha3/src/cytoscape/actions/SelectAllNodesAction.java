//-------------------------------------------------------------------------
// $Revision$
// $Date$
// $Author$
//-------------------------------------------------------------------------
package cytoscape.actions;
//-------------------------------------------------------------------------
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import cytoscape.Cytoscape;
import cytoscape.util.CytoscapeAction;
//-------------------------------------------------------------------------
public class SelectAllNodesAction extends CytoscapeAction  {



    public SelectAllNodesAction () {
        super ("Select all nodes");
        setPreferredMenu( "Select.Nodes" );
    }

    public void actionPerformed (ActionEvent e) {		
        GinyUtils.selectAllNodes( Cytoscape.getCurrentNetworkView() );
    }//action performed
}

