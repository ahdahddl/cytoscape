
/*
 Copyright (c) 2011, The Cytoscape Consortium (www.cytoscape.org)

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
package org.cytoscape.service.util.internal;


import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import org.osgi.framework.BundleContext; 
import org.osgi.framework.BundleActivator; 
import org.osgi.framework.ServiceRegistration; 
import org.osgi.framework.ServiceReference; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.cytoscape.service.util.internal.CyServiceListener;

/**
 * Various utilities for registering services. 
 */
public class RegisterUtil {

	private static final Logger logger = LoggerFactory.getLogger(RegisterUtil.class);

	public static List<Class<?>> getAllInterfaces(Class<?> clazz) {
		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		for ( Class<?> c : clazz.getInterfaces() )
			interfaces.add(c);

		// recurse into superclass
		Class<?> superClass = clazz.getSuperclass();
		if ( superClass != null && superClass != Object.class )
			interfaces.addAll( getAllInterfaces( superClass ) );

		return interfaces;
	}
}
