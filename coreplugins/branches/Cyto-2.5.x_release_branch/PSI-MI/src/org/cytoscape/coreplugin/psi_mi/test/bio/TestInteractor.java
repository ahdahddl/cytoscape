/*
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
package org.cytoscape.coreplugin.psi_mi.test.bio;

import junit.framework.TestCase;

import org.cytoscape.coreplugin.psi_mi.model.Interactor;
import org.cytoscape.coreplugin.psi_mi.model.vocab.GoVocab;
import org.cytoscape.coreplugin.psi_mi.model.vocab.InteractorVocab;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Tests the Interactor Class.
 *
 * @author Ethan Cerami
 */
public class TestInteractor extends TestCase {
	/**
	 * Tests the Interactor Class.
	 *
	 * @throws Exception All Exceptions.
	 */
	public void testInteractor() throws Exception {
		Interactor interactor = new Interactor();
		interactor.setName("YHR119W");
		interactor.setDescription("Gene has a SET or TROMO domain at its "
		                          + "carboxyterminus like the trithorax gene family from human "
		                          + "and Drosophila with postulated function in chromatin-"
		                          + "mediated gene regulation.");

		//  Set Gene Names
		ArrayList geneNameList = new ArrayList();
		geneNameList.add("SET1");
		geneNameList.add("YTX1");
		interactor.addAttribute(InteractorVocab.GENE_NAME, geneNameList);

		//  Set GO Process Terms
		ArrayList goProcessTerms = new ArrayList();
		HashMap goTerm = new HashMap();
		goTerm.put(GoVocab.GO_ID, "GO:0006348");
		goTerm.put(GoVocab.GO_NAME, "chromatin silencing at telomere");
		goProcessTerms.add(goTerm);
		goTerm = new HashMap();
		goTerm.put(GoVocab.GO_ID, "GO:0016571");
		goTerm.put(GoVocab.GO_NAME, "histone methylation");
		goProcessTerms.add(goTerm);
		interactor.addAttribute(GoVocab.GO_CATEGORY_PROCESS, goProcessTerms);
		validateAttributes(interactor);
	}

	/**
	 * Validates the First Go Term.
	 */
	private void validateAttributes(Interactor interactor) {
		ArrayList list = (ArrayList) interactor.getAttribute(GoVocab.GO_CATEGORY_PROCESS);
		HashMap map = (HashMap) list.get(0);
		String id = (String) map.get(GoVocab.GO_ID);
		String name = (String) map.get(GoVocab.GO_NAME);
		assertEquals("GO:0006348", id);
		assertEquals("chromatin silencing at telomere", name);
	}
}
