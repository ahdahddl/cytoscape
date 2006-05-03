
//============================================================================
// 
//  file: DualLayout.java
// 
//  Copyright (c) 2006, University of California San Diego 
// 
//  This program is free software; you can redistribute it and/or modify it 
//  under the terms of the GNU General Public License as published by the 
//  Free Software Foundation; either version 2 of the License, or (at your 
//  option) any later version.
//  
//  This program is distributed in the hope that it will be useful, but 
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
//  for more details.
//  
//  You should have received a copy of the GNU General Public License along 
//  with this program; if not, write to the Free Software Foundation, Inc., 
//  59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// 
//============================================================================



package nct.visualization.cytoscape.dual;

import cytoscape.CytoscapeInit;
import cytoscape.Cytoscape;
import cytoscape.CyNetwork;
import cytoscape.data.CyAttributes;
import cytoscape.giny.CytoscapeRootGraph;
import cytoscape.giny.PhoebeNetworkView;
import cytoscape.visual.VisualMappingManager;
import cytoscape.visual.VisualStyle;

import phoebe.PGraphView;
import giny.view.NodeView;

import java.util.Iterator;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.GraphicsEnvironment;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import nct.graph.Graph;
import nct.graph.Edge;
import nct.visualization.cytoscape.CytoscapeConverter;

/**
 * Constructs several Cytoscape objects based on an input {@link Graph} and
 * renders the graph in Dual Layout mode.
 */
public class DualLayout {

	/**
	 * Creates a dual layout PNG graphic of the specified graph.
	 * @param graph The graph to layout.
	 * @param style The vizmap style to use for the layout.
	 * @param outFile The output file. 
	 * @param vizmapFileLoc The location of the vizmap.props file to use. 
	 */
	public static void create(Graph<String,Double> graph, String style, String outFile, String outThumbFile, String vizmapFileLoc ) {

		CyNetwork cyNetwork  = CytoscapeConverter.convert(graph);
		CyNetwork splitGraph = Cytoscape.createNetwork("splitGraph"); 
		DualLayoutTask task = new DualLayoutTask(cyNetwork);
		task.splitNetwork(splitGraph);
		PhoebeNetworkView view = new PhoebeNetworkView(splitGraph,"Title");
		task.layoutNetwork(view);
	
		// CHANGE STYLE HERE
		VisualMappingManager manager = new VisualMappingManager(view, vizmapFileLoc);
		VisualStyle chosen = manager.getCalculatorCatalog().getVisualStyle(style);
		manager.setVisualStyle(chosen);
		manager.applyNodeAppearances();
		manager.applyEdgeAppearances();

		try {
			BufferedImage fullImage = (BufferedImage)view.getCanvas().getLayer().toImage();
			ImageIO.write(fullImage,"png",new File(outFile));  

			double ratio = (double)fullImage.getWidth()/(double)fullImage.getHeight();
			int thumbWidth = 150; 
			int thumbHeight = (int)((double)thumbWidth/ratio);
			
			//BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
			BufferedImage thumbImage=GraphicsEnvironment.getLocalGraphicsEnvironment().
							getDefaultScreenDevice().
							getDefaultConfiguration().
							createCompatibleImage(thumbWidth,thumbHeight,
							Transparency.BITMASK);

			Graphics2D thumbGraphics = thumbImage.createGraphics();
			thumbGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			thumbGraphics.setBackground(Color.white);

			thumbGraphics.drawImage(fullImage, 0, 0, thumbWidth, thumbHeight, null);
			ImageIO.write(thumbImage,"png",new File(outThumbFile));

		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
}
