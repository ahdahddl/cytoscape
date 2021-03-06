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
package cytoscape.data.ontology;

import cytoscape.Cytoscape;

import cytoscape.data.ontology.readers.OBOTags;

import org.biojava.bio.Annotation;

import org.biojava.ontology.Ontology;

import org.biojava.utils.AbstractChangeable;

import java.util.HashMap;
import java.util.Map;


/**
 * Simple in-memory implementation of an ontology term based on BioJava's
 * interface.<br>
 *
 * <p>
 * This implementation uses CyNetwork and CyAttributes as its actual data
 * storage. A term is equal to a CyNode and the node can have attributes like
 * other regular nodes in CyNetwork. Synonyms, description, etc are stored in
 * CyAttributes.
 * </p>
 *
 * @since Cytoscape 2.4
 * @version 0.8
 * @author kono
 *
 */
public class OntologyTerm extends AbstractChangeable implements org.biojava.ontology.OntologyTerm {
	/*
	 * These constants will be used as the attribute names in CyAttributes.
	 */

	//protected static final String DESCRIPTION = "description";
	protected static final String SYNONYM = "synonym";

	/*
	 * Type of Synonyms used in OBO
	 */
	public enum SynonymType {
		NORMAL("synonym"),
		RELATED("related_synonym"),
		EXACT("exact_synonym"),
		BROAD("broad_synonym"),
		NARROW("narrow_synonym");

		private String typeText;

		private SynonymType(String type) {
			this.typeText = type;
		}

		public String toString() {
			return typeText;
		}
	}

	/**
	 * ID of this ontology term.
	 */
	private String name;

	/**
	 * Name (ID) of the ontology which contains this term.
	 */
	private String ontologyName;

	/**
	 * Constructor.<br>
	 *
	 * @param name ID of this term.  SHOULD NOT BE NULL.
	 * @param ontologyName
	 * @param description
	 */
	public OntologyTerm(String name, String ontologyName, String description) {
		this.name = name;
		this.ontologyName = ontologyName;

		if (description != null) {
			Cytoscape.getNodeAttributes()
			         .setAttribute(name, OBOTags.getPrefix() + "." + OBOTags.DEF.toString(),
			                       description);
		}
	}

	/**
	 * Return name (ID) of this term.<br>
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get ontology object which contains this term.<br>
	 */
	public Ontology getOntology() {
		return Cytoscape.getOntologyServer().getOntologies().get(ontologyName);
	}

	/**
	 * In this implementation, key is the synonym name, and value is the synonym
	 * type.
	 */
	public void addSynonym(Object synonym) {
		addSynonym(synonym, SynonymType.NORMAL);
	}

	/**
	 * In this implementation, key is the synonym name, and value is the synonym
	 * type.
	 */
	public void addSynonym(Object synonym, SynonymType type) {
		Map<Object, SynonymType> synoMap = Cytoscape.getNodeAttributes()
		                                            .getMapAttribute(name,
		                                                             OBOTags.getPrefix() + "."
		                                                             + SYNONYM);

		if (synoMap == null) {
			synoMap = new HashMap();
		}

		synoMap.put(synonym, type);
		Cytoscape.getNodeAttributes()
		         .setMapAttribute(name, OBOTags.getPrefix() + "." + SYNONYM, synoMap);
	}

	/**
	 * Return a human-readable description of this term, or the empty string if
	 * none is available.
	 *
	 */
	public String getDescription() {
		return Cytoscape.getNodeAttributes()
		                .getStringAttribute(name, OBOTags.getPrefix() + "."
		                                    + OBOTags.DEF.toString());
	}

	/**
	 * Return sysnonym attributes for this term.
	 */
	public Object[] getSynonyms() {
		return Cytoscape.getNodeAttributes()
		                .getMapAttribute(name, OBOTags.getPrefix() + "." + SYNONYM).keySet()
		                .toArray();
	}

	/**
	 * Remove a synonym for this term.<br>
	 *
	 */
	public void removeSynonym(Object synonym) {
		Map synoMap = Cytoscape.getNodeAttributes().getMapAttribute(name, SYNONYM);

		if (synoMap != null) {
			synoMap.remove(synonym);
			Cytoscape.getNodeAttributes()
			         .setMapAttribute(name, OBOTags.getPrefix() + "." + SYNONYM, synoMap);
		}
	}

	/**
	 * Always return empty annotation object. Instead of using this, use normal
	 * Cyattributes API.
	 */
	public Annotation getAnnotation() {
		return Annotation.EMPTY_ANNOTATION;
	}
}
