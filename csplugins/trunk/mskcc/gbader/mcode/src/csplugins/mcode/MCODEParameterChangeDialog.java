package csplugins.mcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

/** Copyright (c) 2003 Institute for Systems Biology, University of
 ** California at San Diego, and Memorial Sloan-Kettering Cancer Center.
 **
 ** Code written by: Gary Bader
 ** Authors: Gary Bader, Ethan Cerami, Chris Sander
 **
 ** This library is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Lesser General Public License as published
 ** by the Free Software Foundation; either version 2.1 of the License, or
 ** any later version.
 **
 ** This library is distributed in the hope that it will be useful, but
 ** WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 ** MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 ** documentation provided hereunder is on an "as is" basis, and the
 ** Institute for Systems Biology, the University of California at San Diego
 ** and/or Memorial Sloan-Kettering Cancer Center
 ** have no obligations to provide maintenance, support,
 ** updates, enhancements or modifications.  In no event shall the
 ** Institute for Systems Biology, the University of California at San Diego
 ** and/or Memorial Sloan-Kettering Cancer Center
 ** be liable to any party for direct, indirect, special,
 ** incidental or consequential damages, including lost profits, arising
 ** out of the use of this software and its documentation, even if the
 ** Institute for Systems Biology, the University of California at San
 ** Diego and/or Memorial Sloan-Kettering Cancer Center
 ** have been advised of the possibility of such damage.  See
 ** the GNU Lesser General Public License for more details.
 **
 ** You should have received a copy of the GNU Lesser General Public License
 ** along with this library; if not, write to the Free Software Foundation,
 ** Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 **
 ** User: Gary Bader
 ** Date: Feb 6, 2004
 ** Time: 5:00:00 PM
 ** Description
 **/
public class MCODEParameterChangeDialog extends JDialog {
	private MCODEAlgorithm alg;

    //Parameters for MCODE
	//used in scoring stage
	private boolean includeLoops;
	private int degreeCutOff;
	//used in cluster finding stage
	private int maxDepthFromSeed;
	private double nodeScoreCutOff;
	private boolean fluff;
	private boolean haircut;
	private double fluffNodeDensityCutOff;
	//used in directed mode
	private boolean preprocessNetwork;

	//resetable UI elements
    //scoring
	JCheckBox includeLoopsCheckBox;
    JFormattedTextField degreeCutOffFormattedTextField;
    //cluster finding
    JFormattedTextField maxDepthFormattedTextField;
    JFormattedTextField nodeScoreCutOffFormattedTextField;
	JCheckBox haircutCheckBox;
	JCheckBox fluffCheckBox;
    JFormattedTextField fluffNodeDensityCutOffFormattedTextField;
    //directed mode
	JCheckBox processCheckBox;

	public MCODEParameterChangeDialog(Frame parentFrame) {
		super(parentFrame, "MCODE Parameters", false);
		setResizable(false);

		//get MCODE algorithm instance
		alg = MCODE.alg;
		initParams(alg);

		//main panel for dialog box
		JPanel panel = new JPanel(new BorderLayout());

		//network scoring panel
        JPanel scorePanel = new JPanel();
		includeLoopsCheckBox = new JCheckBox("Include loops", false) {
			public JToolTip createToolTip() {
				return new JMultiLineToolTip();
			}
		};
		includeLoopsCheckBox.addItemListener(new MCODEParameterChangeDialog.includeLoopsCheckBoxAction());
		includeLoopsCheckBox.setToolTipText("If checked, MCODE will include loops (self-edges) in the neighborhood\n" +
		        "density calculation.  This is expected to make a small difference in the results.");
		includeLoopsCheckBox.setSelected(includeLoops);
		scorePanel.add(includeLoopsCheckBox);

        DecimalFormat decFormat = new DecimalFormat();
        decFormat.setParseIntegerOnly(true);
        degreeCutOffFormattedTextField = new JFormattedTextField(decFormat) {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        degreeCutOffFormattedTextField.setColumns(3);
        degreeCutOffFormattedTextField.addPropertyChangeListener("value", new MCODEParameterChangeDialog.formattedTextFieldAction());
        String tipText1 = "Sets the degree cutoff below which a node will not be scored.\n" +
                "Nodes with a degree equal or higher to this value will be scored.\n" +
                "By default this is set to 2. Valid values are 2 or higher to prevent singly connected nodes\n" +
                "from getting an artificially high node score.";
        degreeCutOffFormattedTextField.setToolTipText(tipText1);
        degreeCutOffFormattedTextField.setText((new Integer(degreeCutOff).toString()));
        JLabel degreeCutOffLabel = new JLabel("Degree Cutoff");
        JPanel labelFieldPanel1 = new JPanel() {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        labelFieldPanel1.setToolTipText(tipText1);
        labelFieldPanel1.add(degreeCutOffLabel);
        labelFieldPanel1.add(degreeCutOffFormattedTextField);
        scorePanel.add(labelFieldPanel1);

		//find clusters panel
        JPanel findPanel = new JPanel(new BorderLayout());

        JPanel mainOptionsPanel = new JPanel(new BorderLayout());
        JPanel mainOptionsSubPanel = new JPanel(new BorderLayout(15,2));
        JPanel fluffOptionsPanel = new JPanel();

        nodeScoreCutOffFormattedTextField = new JFormattedTextField(new DecimalFormat("0.000")) {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        nodeScoreCutOffFormattedTextField.setColumns(3);
        nodeScoreCutOffFormattedTextField.addPropertyChangeListener("value", new MCODEParameterChangeDialog.formattedTextFieldAction());
        String tipText3 = "Sets the node score cutoff for expanding a cluster. This is most important\n" +
                "parameter to control the size of MCODE clusters, with smaller values creating smaller clusters.";
        nodeScoreCutOffFormattedTextField.setToolTipText(tipText3);
        nodeScoreCutOffFormattedTextField.setText((new Double(nodeScoreCutOff).toString()));
        JLabel nodeScoreCutOffLabel = new JLabel("Node Score Cutoff");
        JPanel labelFieldPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        labelFieldPanel3.setToolTipText(tipText3);
        labelFieldPanel3.add(nodeScoreCutOffLabel);
        labelFieldPanel3.add(nodeScoreCutOffFormattedTextField);
        mainOptionsSubPanel.add(labelFieldPanel3, BorderLayout.WEST);

        haircutCheckBox = new JCheckBox("Haircut", false) {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        haircutCheckBox.addItemListener(new MCODEParameterChangeDialog.haircutCheckBoxAction());
        haircutCheckBox.setToolTipText("If checked, MCODE will give clusters a haircut\n" +
                "(remove singly connected nodes).");
        haircutCheckBox.setSelected(haircut);
        mainOptionsSubPanel.add(haircutCheckBox, BorderLayout.EAST);

        fluffNodeDensityCutOffFormattedTextField  = new JFormattedTextField(new DecimalFormat("0.000")) {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        fluffNodeDensityCutOffFormattedTextField.setColumns(3);
        fluffNodeDensityCutOffFormattedTextField.addPropertyChangeListener("value", new MCODEParameterChangeDialog.formattedTextFieldAction());
        String tipText4 = "Sets the fluff density cutoff for expanding a cluster according to the unadjusted\n" +
                "node density (clustering coefficient) after the cluster has already been defined by the algorithm.\n" +
                "This allows clusters to slightly overlap at their edges. This parameter is only valid if fluffing\n" +
                "is turned on. A higher value will expand the cluster more.";
        fluffNodeDensityCutOffFormattedTextField.setToolTipText(tipText4);
        fluffNodeDensityCutOffFormattedTextField.setText((new Double(fluffNodeDensityCutOff).toString()));
        fluffNodeDensityCutOffFormattedTextField.setEnabled(fluff);
        JLabel fluffNodeDensityCutOffLabel = new JLabel("Fluff Node Density Cutoff");
        JPanel labelFieldPanel4 = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        labelFieldPanel4.setToolTipText(tipText4);
        labelFieldPanel4.add(fluffNodeDensityCutOffLabel);
        labelFieldPanel4.add(fluffNodeDensityCutOffFormattedTextField);
        fluffOptionsPanel.add(labelFieldPanel4);

        fluffCheckBox = new JCheckBox("Fluff", false) {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        fluffCheckBox.addItemListener(new MCODEParameterChangeDialog.fluffCheckBoxAction());
        fluffCheckBox.setToolTipText("If checked, MCODE will fluff clusters\n" +
                "(expand core cluster one neighbour shell outwards according to fluff\n" +
                "density threshold). This is done after the optional haircut step.");
        fluffCheckBox.setSelected(fluff);
        fluffOptionsPanel.add(fluffCheckBox);

        mainOptionsPanel.setBorder(BorderFactory.createTitledBorder("Main Options"));
        fluffOptionsPanel.setBorder(BorderFactory.createTitledBorder("Fluff Options"));
        mainOptionsPanel.add(mainOptionsSubPanel, BorderLayout.NORTH);
        mainOptionsPanel.add(fluffOptionsPanel, BorderLayout.SOUTH);
        findPanel.add(mainOptionsPanel, BorderLayout.NORTH);

        maxDepthFormattedTextField = new JFormattedTextField(decFormat) {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        maxDepthFormattedTextField.setColumns(3);
        maxDepthFormattedTextField.addPropertyChangeListener("value", new MCODEParameterChangeDialog.formattedTextFieldAction());
        String tipText2 = "Sets the maximum depth from a seed node in the network to search to expand a cluster.\n" +
                "By default this is set to an arbitrarily large number. Set this to a small number\n" +
                "to limit cluster size.";
        maxDepthFormattedTextField.setToolTipText(tipText2);
        maxDepthFormattedTextField.setText((new Integer(maxDepthFromSeed).toString()));
        JLabel maxDepthLabel = new JLabel("Max. Depth");
        JPanel labelFieldPanel2 = new JPanel() {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        labelFieldPanel2.setToolTipText(tipText2);
        labelFieldPanel2.add(maxDepthLabel);
        labelFieldPanel2.add(maxDepthFormattedTextField);
        findPanel.add(labelFieldPanel2, BorderLayout.SOUTH);

		//directed mode panel
        JPanel directedModePanel = new JPanel();
		processCheckBox = new JCheckBox("Preprocess network", false) {
			public JToolTip createToolTip() {
				return new JMultiLineToolTip();
			}
		};
		processCheckBox.addItemListener(new MCODEParameterChangeDialog.processCheckBoxAction());
		processCheckBox.setToolTipText("If checked, MCODE will limit cluster expansion to the\n" +
		        "direct neighborhood of the spawning node.  If unchecked, the cluster will be allowed\n" +
		        "to branch out to denser regions of the network.");
		processCheckBox.setSelected(preprocessNetwork);
		directedModePanel.add(processCheckBox);

		JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Network Scoring", null, scorePanel, "Set parameters for scoring stage (Stage 1)");
        tabbedPane.addTab("Find Clusters", null, findPanel, "Set parameters for cluster finding stage (Stage 2)");
        //uncomment below when directed mode is implemented
		//tabbedPane.addTab("Directed Mode", null, directedModePanel, "Set parameters for directed mode");
		panel.add(tabbedPane, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout());

		JButton OKButton = new JButton("OK");
		OKButton.addActionListener(new MCODEParameterChangeDialog.OKAction(this));
		bottomPanel.add(OKButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new MCODEParameterChangeDialog.cancelAction(this));
		bottomPanel.add(cancelButton);

		panel.add(bottomPanel, BorderLayout.SOUTH);

		setContentPane(panel);
	}

	private void initParams(MCODEAlgorithm alg) {
		//used in scoring stage
		includeLoops = alg.isIncludeLoops();
		degreeCutOff = alg.getDegreeCutOff();
		//used in cluster finding stage
		maxDepthFromSeed = alg.getMaxDepthFromStart();
		nodeScoreCutOff = alg.getNodeScoreCutOff();
		fluff = alg.isFluff();
		haircut = alg.isHaircut();
		fluffNodeDensityCutOff = alg.getFluffNodeDensityCutOff();
		//used in directed mode
		preprocessNetwork = alg.isPreprocessNetwork();
	}

	private void saveParams(MCODEAlgorithm alg) {
		//used in scoring stage
		alg.setIncludeLoops(includeLoops);
		alg.setDegreeCutOff(degreeCutOff);
		//used in cluster finding stage
		alg.setMaxDepthFromStart(maxDepthFromSeed);
		alg.setNodeScoreCutOff(nodeScoreCutOff);
		alg.setFluff(fluff);
		alg.setHaircut(haircut);
		alg.setFluffNodeDensityCutOff(fluffNodeDensityCutOff);
		//used in directed mode
		alg.setPreprocessNetwork(preprocessNetwork);
	}

	private class OKAction extends AbstractAction {
		private JDialog dialog;

		OKAction(JDialog popup) {
			super();
			this.dialog = popup;
		}

		public void actionPerformed(ActionEvent e) {
			saveParams(alg);
			dialog.dispose();
		}
	}

	private class cancelAction extends AbstractAction {
		private JDialog dialog;

		cancelAction(JDialog popup) {
			super();
			this.dialog = popup;
		}

		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	}

	private class includeLoopsCheckBoxAction implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				includeLoops = false;
			} else {
				includeLoops = true;
			}
		}
	}

    private class formattedTextFieldAction implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            Object source = e.getSource();
            if (source == degreeCutOffFormattedTextField) {
                Number value = (Number) degreeCutOffFormattedTextField.getValue();
                if ((value != null) && (value.intValue() > 1)) {
                    degreeCutOff = value.intValue();
                }
            } else if (source == maxDepthFormattedTextField) {
                Number value = (Number) maxDepthFormattedTextField.getValue();
                if ((value != null) && (value.intValue() > 0)) {
                    maxDepthFromSeed = value.intValue();
                }
            } else if (source == nodeScoreCutOffFormattedTextField) {
                Number value = (Number) nodeScoreCutOffFormattedTextField.getValue();
                if ((value != null) && (value.doubleValue() >= 0.0)) {
                    nodeScoreCutOff = value.doubleValue();
                }
            } else if (source == fluffNodeDensityCutOffFormattedTextField) {
                Number value = (Number) fluffNodeDensityCutOffFormattedTextField.getValue();
                if ((value != null) && (value.doubleValue() >= 0.0)) {
                    fluffNodeDensityCutOff = value.doubleValue();
                }
            }
        }
    }

	private class haircutCheckBoxAction implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				haircut = false;
			} else {
				haircut = true;
			}
		}
	}

	private class fluffCheckBoxAction implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				fluff = false;
			} else {
				fluff = true;
			}
            fluffNodeDensityCutOffFormattedTextField.setEnabled(fluff);
		}
	}

	private class processCheckBoxAction implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				preprocessNetwork = false;
			} else {
				preprocessNetwork = true;
			}
		}
	}
}
