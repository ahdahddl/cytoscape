/*
  File: ZoomSelectedAction.java

  Copyright (c) 2006, The Cytoscape Consortium (www.cytoscape.org)

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

//-------------------------------------------------------------------------
// $Revision$
// $Date$
// $Author$
//-------------------------------------------------------------------------
package cytoscape.actions;

import cytoscape.Cytoscape;

import cytoscape.ding.DingNetworkView;

import cytoscape.giny.*;

import cytoscape.util.CytoscapeAction;

import cytoscape.view.CyNetworkView;

import giny.view.*;

//-------------------------------------------------------------------------
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;


//-------------------------------------------------------------------------
/**
 *
 */
public class ZoomSelectedAction extends CytoscapeAction {
	/**
	 * Creates a new ZoomSelectedAction object.
	 */
	public ZoomSelectedAction() {
		super();
	}

	/**
	 *  DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 */
	public void actionPerformed(ActionEvent e) {
		((DingNetworkView) Cytoscape.getCurrentNetworkView()).fitSelected();
		Cytoscape.getCurrentNetworkView().updateView();
	}
}
