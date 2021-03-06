
package org.cytoscape.dnd;


import java.awt.geom.Point2D;
import java.awt.datatransfer.Transferable;

import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.task.NetworkViewTaskFactory;

/**
 * An extension of TaskFactory that provides support for
 * tasks to deal with drag and drop.
 */
public interface DropNetworkViewTaskFactory extends NetworkViewTaskFactory {

	/**
	 * Sets the drop information for a TaskFactory. 
	 * @param t The transferable object that was dropped.
	 * @param javaPt The raw Java point at which the object was dropped.
	 * @param xformPt The drop point transformed into Cytoscape coordinates.
	 */
	public void setDropInformation (Transferable t, Point2D javaPt, Point2D xformPt);
}
