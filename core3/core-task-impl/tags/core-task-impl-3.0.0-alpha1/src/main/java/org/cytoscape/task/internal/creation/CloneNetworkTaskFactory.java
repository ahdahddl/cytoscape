/*
 File: CloneNetworkTaskFactory.java

 Copyright (c) 2006, 2010, The Cytoscape Consortium (www.cytoscape.org)

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
package org.cytoscape.task.internal.creation;  


import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.TaskIterator;


public class CloneNetworkTaskFactory extends AbstractNetworkTaskFactory {
	private final CyNetworkManager networkManager;
	private final CyNetworkViewManager networkViewManager;
	private final VisualMappingManager vmm;
    private final CyNetworkFactory netFactory;
    private final CyNetworkViewFactory netViewFactory;
    private final RenderingEngineManager reMgr;
    private final CyNetworkNaming naming;

	public CloneNetworkTaskFactory(final CyNetworkManager networkManager, 
            final CyNetworkViewManager networkViewManager, final VisualMappingManager vmm, 
            final CyNetworkFactory netFactory, final CyNetworkViewFactory netViewFactory, 
            final RenderingEngineManager reMgr, final CyNetworkNaming naming)
	{
		this.networkManager = networkManager;
		this.networkViewManager = networkViewManager;
		this.vmm = vmm;
		this.netFactory = netFactory;
		this.netViewFactory = netViewFactory;
		this.reMgr = reMgr;
		this.naming = naming;
	}

	public TaskIterator getTaskIterator() {
		return new TaskIterator(new CloneNetworkTask(net, networkManager, networkViewManager, vmm, netFactory, netViewFactory, reMgr, naming));
	} 
}
