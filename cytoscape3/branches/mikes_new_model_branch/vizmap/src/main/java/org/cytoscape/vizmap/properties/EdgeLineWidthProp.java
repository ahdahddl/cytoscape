/*
 Copyright (c) 2007, The Cytoscape Consortium (www.cytoscape.org)

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
package org.cytoscape.vizmap.properties;

import org.cytoscape.view.EdgeView;
import org.cytoscape.vizmap.VisualPropertyType;
import org.cytoscape.vizmap.icon.LineTypeIcon;
import org.cytoscape.vizmap.parsers.FloatParser;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;


/**
 *
 */
public class EdgeLineWidthProp extends AbstractVisualProperty {
	/**
	 *  DOCUMENT ME!
	 *
	 * @return  DOCUMENT ME!
	 */
	public VisualPropertyType getType() {
		return VisualPropertyType.EDGE_LINE_WIDTH;
	}

	/**
	 *  DOCUMENT ME!
	 *
	 * @return  DOCUMENT ME!
	 */
	public Icon getIcon(final Object value) {
		final LineTypeIcon icon = new LineTypeIcon();
		icon.setColor(new Color(10, 10, 10, 20));
		icon.setText(value.toString());
		icon.setBottomPadding(-7);

		return icon;
	}

	/**
	 *  DOCUMENT ME!
	 *
	 * @param ev DOCUMENT ME!
	 * @param o DOCUMENT ME!
	 */
	public void applyToEdgeView(EdgeView ev, Object o) {
		if ((o == null) || (ev == null))
			return;

		if (ev.getStrokeWidth() != ((Number)o).floatValue()) {
			final BasicStroke oldValue = (BasicStroke) ev.getStroke();
			final Stroke newLine = new BasicStroke(((Number)o).floatValue(), oldValue.getEndCap(), oldValue.getLineJoin(),
					oldValue.getMiterLimit(), oldValue.getDashArray(), oldValue.getDashPhase() );
		
			//System.out.println("*** o = " + o + ", new w = " + ev.getStrokeWidth());
			
			ev.setStroke(newLine);
			//System.out.println("Changed w = " + ev.getStrokeWidth());
		}
	}

	/**
	 *  DOCUMENT ME!
	 *
	 * @param props DOCUMENT ME!
	 * @param baseKey DOCUMENT ME!
	 *
	 * @return  DOCUMENT ME!
	 */
	public Object parseProperty(Properties props, String baseKey) {
		String s = props.getProperty(VisualPropertyType.EDGE_LINE_WIDTH.getDefaultPropertyKey(baseKey));

		if (s != null)
			return (new FloatParser()).parseFloat(s);
		else

			return null;
	}

	/**
	 *  DOCUMENT ME!
	 *
	 * @return  DOCUMENT ME!
	 */
	public Object getDefaultAppearanceObject() {
		return new Float(1.0f);
	}
}
