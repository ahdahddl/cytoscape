/*
 File: DeleteSelectedNodesAndEdgesTaskFactory.java

 Copyright (c) 2010, 2011, The Cytoscape Consortium (www.cytoscape.org)

 This library is free software; you can redistribute it and/or modify it
 under the terms of the GNU Lesser General Public License as published
 by the Free Software Foundation; either version 2.1 of the License, or
 any later version.

 This library is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 documentation provided hereunder is on an "as is" basis, and the
 Institute for Systems Biology and the Whitehead Institute
 have no obligations to provide maintenance, support,
 updates, enhancements or modifications.  In no event shall the
 Institute for Systems Biology and the Whitehead Institute
 be liable to any party for direct, indirect, special,
 incidental or consequential damages, including lost profits, arising
 out of the use of this software and its documentation, even if the
 Institute for Systems Biology and the Whitehead Institute
 have been advised of the possibility of such damage.  See
 the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation,
 Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
*/
package org.cytoscape.task.internal.networkobjects;


import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.undo.UndoSupport;


public class DeleteSelectedNodesAndEdgesTaskFactory extends AbstractTaskFactory {
	private final UndoSupport undoSupport;
	private final CyApplicationManager applicationManager;
	private final CyNetworkViewManager networkViewManager;
	private final VisualMappingManager visualMappingManager;
	private final CyEventHelper eventHelper;

	public DeleteSelectedNodesAndEdgesTaskFactory(final UndoSupport undoSupport,
						      final CyApplicationManager applicationManager,
						      final CyNetworkViewManager networkViewManager,
						      final VisualMappingManager visualMappingManager,
						      final CyEventHelper eventHelper)
	{
		this.undoSupport          = undoSupport;
		this.applicationManager   = applicationManager;
		this.networkViewManager   = networkViewManager;
		this.visualMappingManager = visualMappingManager;
		this.eventHelper          = eventHelper;
	}

	public TaskIterator createTaskIterator() {
		return new TaskIterator(
			new DeleteSelectedNodesAndEdgesTask(undoSupport, applicationManager,
							    networkViewManager,
							    visualMappingManager, eventHelper));
	}
}
