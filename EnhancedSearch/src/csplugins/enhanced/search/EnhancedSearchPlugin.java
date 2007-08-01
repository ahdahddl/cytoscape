
/*
 Copyright (c) 2006, 2007, The Cytoscape Consortium (www.cytoscape.org)

 The Cytoscape Consortium is:
 - Institute for Systems Biology
 - University of California San Diego
 - Memorial Sloan-Kettering Cancer Center
 - Institut Pasteur
 - Agilent Technologies

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

package csplugins.enhanced.search;

import cytoscape.plugin.CytoscapePlugin;
import cytoscape.plugin.PluginInfo;

/**
 * Enhanced Search plugin
 * 
 * @author Maital Ashkenazi
 * @version 1.0
 * 
 */
public class EnhancedSearchPlugin extends CytoscapePlugin {

	private static final double VERSION = 1.00;

	// Creates a new EnhancedSearchPlugin object.
	public EnhancedSearchPlugin() {
		EnhancedSearch plugin = new EnhancedSearch();
	}

	// Information needed for Cytoscape version 2.5 plugin management
	public PluginInfo getPluginInfoObject() {

		PluginInfo info = new PluginInfo();

		info.setName("EnhancedSearch");
		info.setDescription("Perform search on multiple attribute fields.");
		info.setCategory(PluginInfo.Category.NETWORK_ATTRIBUTE_IO);
		info.setPluginVersion(VERSION);
		info.setCytoscapeVersion("2.5");
		info.setProjectUrl("http://conklinwolf.ucsf.edu/genmappwiki/Google_Summer_of_Code_2007/Maital");
		info.addAuthor("Maital Ashkenazi", "HUJI");

		return info;
	}

}
