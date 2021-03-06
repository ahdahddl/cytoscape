/*
 Copyright (c) 2006, 2011, The Cytoscape Consortium (www.cytoscape.org)

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
package org.cytoscape.io.internal.read.xgmml.handler;

import org.cytoscape.io.internal.read.xgmml.Handler;
import org.cytoscape.io.internal.read.xgmml.ObjectTypeMap;
import org.cytoscape.io.internal.read.xgmml.ParseState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class AbstractHandler implements Handler {

	protected ReadDataManager manager;
	protected AttributeValueUtil attributeValueUtil;
	
	ObjectTypeMap typeMap;
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractHandler.class);

	public AbstractHandler() {
	    typeMap = new ObjectTypeMap();
	}

	@Override
	abstract public ParseState handle(String tag, Attributes atts, ParseState current) throws SAXException;

	@Override
	public void setManager(ReadDataManager manager) {
		this.manager = manager;
	}

	@Override
	public void setAttributeValueUtil(AttributeValueUtil attributeValueUtil) {
		this.attributeValueUtil = attributeValueUtil;
	}
	
	protected String getLabel(Attributes atts) {
		String label = atts.getValue("label");
		
		if (label == null || label.isEmpty())
			label = atts.getValue("id");

		return label;
	}
	
	protected Object getId(Attributes atts) {
		Object id = atts.getValue("id");

		if (id != null) {
			final String str = id.toString().trim();

			if (!str.isEmpty()) {
				try {
					id = Long.valueOf(str);
				} catch (NumberFormatException nfe) {
					logger.debug("Graph id is not a number: " + id);
					id = str;
				}
			}
		}
		
		if (id == null || id.toString().isEmpty())
			id = atts.getValue("label");
		
		return id;
	}
}
