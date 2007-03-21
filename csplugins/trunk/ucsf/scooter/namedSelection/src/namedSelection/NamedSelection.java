/* vim: set ts=2: */
/**
 * Copyright (c) 2006 The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions, and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions, and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   3. Redistributions must acknowledge that this software was
 *      originally developed by the UCSF Computer Graphics Laboratory
 *      under support by the NIH National Center for Research Resources,
 *      grant P41-RR01081.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package namedSelection;

// System imports
import javax.swing.JOptionPane;
import java.util.List;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;

// giny imports
import giny.view.NodeView;
import ding.view.*;

// Cytoscape imports
import cytoscape.*;
import cytoscape.plugin.CytoscapePlugin;
import cytoscape.view.CytoscapeDesktop;
import cytoscape.view.CyNetworkView;
import cytoscape.data.CyAttributes;
import cytoscape.util.CytoscapeAction;

// our imports
import namedSelection.ui.GroupPanel;

/**
 * The NamedSelection class provides the primary interface to the
 * Cytoscape plugin mechanism
 */
public class NamedSelection extends CytoscapePlugin 
                            implements CyGroupViewer, 
                                       NodeContextMenuListener,
                                       PropertyChangeListener {

	public static final String viewerName = "namedSelection";
	public static final double VERSION = 0.1;
	public static final int NONE = 0;
	public static final int SELECT = 1;
	public static final int UNSELECT = 2;
	public static final int NEW = 3;
	public static final int REMOVE = 4;
	public static final int ADD = 5;
	public static final int DELETE = 6;

	// State values
	public static final int SELECTED = 1;
	public static final int UNSELECTED = 2;

	private static CyGroupViewer groupViewer = null;

	private static GroupPanel groupPanel = null;

  /**
   * Create our action and add it to the plugins menu
   */
	/*
  public NamedSelection() {
		JMenu menu = new JMenu("Named Selection Tool");
		menu.addMenuListener(new NamedSelectionMenuListener(null));

		JMenu pluginMenu = Cytoscape.getDesktop().getCyMenus().getMenuBar()
																.getMenu("Plugins");
		pluginMenu.add(menu);
		// Register with CyGroup
		CyGroup.registerGroupViewer(this);
		this.groupViewer = this; // this makes it easier to get at from inner classes
		System.out.println("namedSelectionPlugin "+VERSION+" initialized");
  }
	*/

	/**
	 * Future version....
	 */
	public NamedSelection() {
		// Listen for network changes (so we can add our context menu)
		try {
			// Add ourselves to the network view created change list
			Cytoscape.getDesktop().getSwingPropertyChangeSupport()
			          .addPropertyChangeListener( CytoscapeDesktop.NETWORK_VIEW_CREATED, this);
			// Add our context menu
			((DGraphView)Cytoscape.getCurrentNetworkView()).addNodeContextMenuListener(this);
		} catch (ClassCastException e) {
			System.out.println(e.getMessage());
		}
		// Create our main plugin menu
		JMenu menu = new JMenu("Named Selection Tool");
		menu.addMenuListener(new NamedSelectionMenuListener(null));

		JMenu pluginMenu = Cytoscape.getDesktop().getCyMenus().getMenuBar()
																.getMenu("Plugins");
		pluginMenu.add(menu);

		// Add our interface to CytoPanel 1
		groupPanel = new GroupPanel(this);
		Cytoscape.getDesktop().getCytoPanel(SwingConstants.WEST).add("Groups", groupPanel);
		// We want to listen for graph perspective changes (primarily SELECT/UNSELECT)
		Cytoscape.getCurrentNetworkView().addGraphViewChangeListener(groupPanel);

		// Register with CyGroup
		CyGroup.registerGroupViewer(this);
		this.groupViewer = this; // this makes it easier to get at from inner classes
		System.out.println("namedSelectionPlugin "+VERSION+" initialized");
	}

	// These are required by the CyGroupViewer interface

	/**
	 * Return the name of our viewer
	 *
	 * @return viewer name
	 */
	public String getViewerName() { return viewerName; }

	/**
	 * This is called when a new group has been created that
	 * we care about.  If we weren't building our menu each
	 * time, this would be used to update the list of groups
	 * we present to the user.
	 *
	 * @param group the CyGroup that was just created
	 */
	public void groupCreated(CyGroup group) { 
		groupPanel.groupCreated(group);
	}

	/**
	 * This is called when a group we care about is about to 
	 * be deleted.  If we weren't building our menu each
	 * time, this would be used to update the list of groups
	 * we present to the user.
	 *
	 * @param group the CyGroup that will be deleted
	 */
	public void groupWillBeRemoved(CyGroup group) { 
		groupPanel.groupRemoved(group);
	}

	/**
	 * This is called when a group we care about is changed.
	 *
	 * @param group the CyGroup that has changed
	 * @param node the CyNode that caused the change
	 * @param change the change that occured
	 */
	public void groupChanged(CyGroup group, CyNode node, int change) { 
		// At some point, this should be a little more granular.  Do we really
		// need to rebuild the tree when we have a simple node addition/removal?
		groupPanel.groupChanged(group);
	}

	// PropertyChange support

	/**
	 * Implements propertyChange
	 *
	 * @param e the property change event
	 */
	public void propertyChange (PropertyChangeEvent e) {
		if (e.getPropertyName() == CytoscapeDesktop.NETWORK_VIEW_CREATED) {
			((DGraphView)Cytoscape.getCurrentNetworkView()).addNodeContextMenuListener(this);
			Cytoscape.getCurrentNetworkView().addGraphViewChangeListener(groupPanel);
		}
	}

	/**
	 * Implements addNodeContextMenuItems
	 *
	 * @param nodeView the views to add this to
	 * @param menu the menu to add
	 */
	public void addNodeContextMenuItems (NodeView nodeView, JPopupMenu menu) {
		if (menu == null) {
			menu = new JPopupMenu();
		}
		menu.add(getNodePopupMenu(nodeView));
	}

	/**
	 * return the context menu to popup for a node.  The menu depends not
	 * only on the context of the node it's over, but also the number of
	 * other selected items, etc.
	 *
	 */
	private JMenu getNodePopupMenu(NodeView nodeView) {
		JMenu menu = new JMenu("Group operations");
		menu.addMenuListener(new NamedSelectionMenuListener(nodeView));
		return menu;
	}


	/**
	 * The NamedSelectionMenuListener provides the interface to the structure viz
	 * Node context menu and the plugin menu.
	 */
	public class NamedSelectionMenuListener implements MenuListener {
		private NamedSelectionCommandListener staticHandle;
		private NodeView overNode = null;

		/**
		 * Create the namedSelection menu listener
		 *
		 * @param nv the Cytoscape NodeView the mouse was over
		 */
		NamedSelectionMenuListener(NodeView nv) {
			this.staticHandle = new NamedSelectionCommandListener(NONE,null,null);
			this.overNode = nv;
		}

	  public void menuCanceled (MenuEvent e) {};
		public void menuDeselected (MenuEvent e) {};

		/**
		 * Process the selected menu
		 *
		 * @param e the MenuEvent for the selected menu
		 */
		public void menuSelected (MenuEvent e)
		{
			JMenu m = (JMenu)e.getSource();
			// Clear the menu
			Component[] subMenus = m.getMenuComponents();
			for (int i = 0; i < subMenus.length; i++) { m.remove(subMenus[i]); }

			CyNetwork network = Cytoscape.getCurrentNetwork();
			Set currentNodes = network.getSelectedNodes();
			List<CyGroup>groupList = CyGroup.getGroupList(groupViewer);

			// Add our menu items
			{
			  JMenuItem item = new JMenuItem("Create new named selection");
				NamedSelectionCommandListener l = new NamedSelectionCommandListener(NEW, null,null);
				item.addActionListener(l);
				if (currentNodes.size() > 0) {
					item.setEnabled(true);
				} else {
					item.setEnabled(false);
				}
				m.add(item);
			}

			if (groupList != null && groupList.size() > 0) {
			  JMenu item = new JMenu("Remove named selection");
				if (addGroupMenu(item, REMOVE, groupList, null))
					m.add(item);
			} else {
			  JMenuItem item = new JMenuItem("Remove named selection");
				item.setEnabled(false);
				m.add(item);
			}

			if (groupList != null && groupList.size() > 0) {
			  JMenu item = new JMenu("Select");
				if (addGroupMenu(item, SELECT, groupList, null))
					m.add(item);
			} else {
			  JMenuItem item = new JMenuItem("Select");
				item.setEnabled(false);
				m.add(item);
			}

			if (groupList != null && groupList.size() > 0) {
			  JMenu item = new JMenu("Unselect");
				if (addGroupMenu(item, UNSELECT, groupList, null))
					m.add(item);
			} else {
			  JMenuItem item = new JMenuItem("Unselect");
				item.setEnabled(false);
				m.add(item);
			}

			if (overNode != null) {
				CyNode contextNode = (CyNode)overNode.getNode();
				if (groupList != null && groupList.size() > 0) {
					JMenu item = new JMenu("Add node to");
					if (addGroupMenu(item, ADD, groupList, contextNode))
						m.add(item);
				}

				List<CyGroup>nodeGroups = contextNode.getGroups();
				if (nodeGroups != null && nodeGroups.size() > 0) {
 					JMenu item = new JMenu("Remove node from");
					// Figure out what groups this node is part of
					if (addGroupMenu(item, DELETE, nodeGroups, (CyNode)overNode.getNode()))
						m.add(item);
				}
			}
		}

		/**
		 * Add all groups to a menu
		 */
		private boolean addGroupMenu(JMenu menu, int command, List<CyGroup>groupList,
		                          CyNode node) {
			boolean foundItem = false;
			if (groupList == null) return false;
			// List current named selections
			Iterator iter = groupList.iterator();
			while (iter.hasNext()) {
				CyGroup group = (CyGroup)iter.next();
				if (group.getViewer().equals(groupViewer.getViewerName())) {
					addSubMenu(menu, group.getGroupName(), command, group, node);
					foundItem = true;
				}
			}
			return foundItem;
		}

		/**
		 * Add a submenu item to an existing menu
		 *
		 * @param menu the JMenu to add the new submenu to
		 * @param group the group node
		 * @param node the node
		 */
		private void addSubMenu(JMenu menu, String label, int command, CyGroup group, CyNode node) {
			JMenuItem item = new JMenuItem(label);
			NamedSelectionCommandListener l = new NamedSelectionCommandListener(command, group, node);
			item.addActionListener(l);
		  menu.add(item);
		}
	}
	
  /**
   * This class gets attached to the menu item.
   */
  static class NamedSelectionCommandListener implements ActionListener {
  	private static final long serialVersionUID = 1;
		private int command;
		private CyGroup group = null; // The group we care about
		private CyNode node = null; // The node this is refering to

		NamedSelectionCommandListener(int command, CyGroup group, CyNode node) {
			this.command = command;
			this.group = group;
			this.node = node;
		}

    /**
     * This method is called when the user selects the menu item.
     */
    public void actionPerformed(ActionEvent ae) {
			String label = ae.getActionCommand();
			if (command == SELECT) {
				select();
			} else if (command == UNSELECT) {
				unselect();
			} else if (command == NEW) {
				newGroup();
			} else if (command == REMOVE) {
				removeGroup();
			} else if (command == ADD) {
				addToGroup(node);
			} else if (command == DELETE) {
				removeFromGroup(node);
			}
		}

		/**
		 * Create a new group.
		 */
		private void newGroup() {
			CyNetwork network = Cytoscape.getCurrentNetwork();
			List<CyNode> currentNodes = new ArrayList(network.getSelectedNodes());
			List<CyGroup> groupList = CyGroup.getGroupList();
			String groupName = JOptionPane.showInputDialog("Please enter a name for this selection");
			CyGroup group = CyGroup.createGroup(groupName, currentNodes, viewerName);
			groupPanel.groupCreated(group);
			group.setState(SELECTED);
		}

		/**
		 * Remove a group.
		 */
		private void removeGroup() {
			CyGroup.removeGroup(group);
			groupPanel.groupRemoved(group);
		}

		/**
		 * Add a node to a group
		 */
		private void addToGroup(CyNode node) {
			node.addToGroup(group);
			groupPanel.groupChanged(group);
		}

		/**
		 * Remove a node to a group
		 */
		private void removeFromGroup(CyNode node) {
			node.removeFromGroup(group);
			groupPanel.groupChanged(group);
		}

		/**
		 * Perform the action associated with a select menu selection
		 */
		private void select() {
			List<CyNode> nodeList = group.getNodes();
			Cytoscape.getCurrentNetwork().setSelectedNodeState(nodeList, true);
			Cytoscape.getCurrentNetworkView().updateView();
			groupPanel.groupChanged(group);
			group.setState(SELECTED);
		}

		/**
		 * Perform the action associated with an unselect menu selection
		 */
		private void unselect() {
			List<CyNode> nodeList = group.getNodes();
			Cytoscape.getCurrentNetwork().setSelectedNodeState(nodeList, false);
			Cytoscape.getCurrentNetworkView().updateView();
			groupPanel.groupChanged(group);
			group.setState(UNSELECTED);
		}
	}
}
