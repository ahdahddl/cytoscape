package SessionExporterPlugin;

import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;

import cytoscape.Cytoscape;
import cytoscape.CyNetwork;
import cytoscape.view.CyNetworkView;
import ding.view.DGraphView;
import cytoscape.visual.VisualStyle;
import cytoscape.task.ui.JTaskConfig;
import cytoscape.task.Task;
import cytoscape.task.TaskMonitor;
import cytoscape.task.util.TaskManager;
import cytoscape.data.writers.InteractionWriter;
import cytoscape.data.writers.CytoscapeSessionWriter;

public class HTMLSessionExporter implements SessionExporter
{
	private static final int MAX_COLUMNS = 3;

	private static final String IMAGE_TYPE			= "png";
	private static final String FILE_IMAGE_SUFFIX		= "." + IMAGE_TYPE;
	private static final String FILE_THUMBNAIL_SUFFIX	= "_thumbnail." + IMAGE_TYPE;
	private static final String FILE_LEGEND_SUFFIX		= "_legend." + IMAGE_TYPE;
	private static final String FILE_SIF_SUFFIX		= ".sif";
	private static final String FILE_SESSION_NAME		= "session.cys";
	
	public void export(final File directory)
	{
		Task task = new Task()
		{
			TaskMonitor monitor = null;
			boolean needToHalt = false;

			public String getTitle()
			{
				return "HTML Session Exporter: Exporting session...";
			}

			public void setTaskMonitor(TaskMonitor monitor)
			{
				this.monitor = monitor;
			}

			public void halt()
			{
				needToHalt = true;
			}

			public void run()
			{
				try
				{
					ArrayList<String> networkIDs = new ArrayList<String>();

					//
					// Write the session
					//

					File sessionFile = new File(directory, FILE_SESSION_NAME);
					CytoscapeSessionWriter sessionWriter = new CytoscapeSessionWriter(sessionFile.toString());
					sessionWriter.writeSessionToDisk();

					//
					// Generate all the images, thumbnails, legends, and sif files
					//

					GraphViewToImage graphViewToImage = new GraphViewToImage(1.0, true, true);
					Map viewMap = Cytoscape.getNetworkViewMap();
					Iterator iterator = viewMap.keySet().iterator();
					while (iterator.hasNext())
					{
						String networkID = (String) iterator.next();
						networkIDs.add(networkID);
						DGraphView view = (DGraphView) viewMap.get(networkID);

						File imageFile = new File(directory, networkID + FILE_IMAGE_SUFFIX);
						BufferedImage image = graphViewToImage.convert(view);
						ImageIO.write(image, IMAGE_TYPE, imageFile);

						File thumbnailFile = new File(directory, networkID + FILE_THUMBNAIL_SUFFIX);
						BufferedImage thumbnail = Thumbnails.createThumbnail(image);
						ImageIO.write(thumbnail, IMAGE_TYPE, thumbnailFile);
						
						File legendFile = new File(directory, networkID + FILE_LEGEND_SUFFIX);
						VisualStyle visualStyle = Cytoscape.getNetworkView(networkID).getVisualStyle();
						BufferedImage legend = VisualStyleToImage.convert(visualStyle);
						ImageIO.write(legend, IMAGE_TYPE, legendFile);

						File sifFile = new File(directory, networkID + FILE_SIF_SUFFIX);
						FileWriter sifWriter = new FileWriter(sifFile);
						CyNetwork network = Cytoscape.getNetwork(networkID);
						InteractionWriter.writeInteractions(network, sifWriter, monitor);
						sifWriter.close();
					}

					//
					// Generate HTML file
					//

					File htmlFile = new File(directory, "index.html");
					PrintWriter htmlWriter = new PrintWriter(htmlFile);
					htmlWriter.println("<html>");
					htmlWriter.println("<body>");
					htmlWriter.println("<table border=\"0\" cellspacing=\"10\" cellpadding=\"0\">");
					int i = 0;
					for (String networkID : networkIDs)
					{
						String title = Cytoscape.getNetwork(networkID).getTitle();

						if ((i % MAX_COLUMNS) == 0)
							htmlWriter.println("<tr>");
						htmlWriter.println("<td align=center>");
						htmlWriter.println("<img src=\"" + networkID + FILE_THUMBNAIL_SUFFIX + "\">");
						
						String imageLink = "<a href=\"" + networkID + FILE_IMAGE_SUFFIX + "\">image</a>";
						String sifLink = "<a href=\"" + networkID + FILE_SIF_SUFFIX + "\">sif</a>";
						htmlWriter.print("<font size=-1><br>" + title + " (" + imageLink + " | " + sifLink + ")</font>");
						htmlWriter.println("</td>");
						if ((i + 1) % MAX_COLUMNS == 0)
							htmlWriter.println("</tr>");
						i++;
					}
					if ((i + 1) % MAX_COLUMNS != 0)
						htmlWriter.println("</tr>");
					htmlWriter.println("</table>");
					htmlWriter.println("</body>");
					htmlWriter.println("</html>");
					htmlWriter.close();

				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(Cytoscape.getDesktop(), e.getMessage(),
									"HTML Session Exporter",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		};

		JTaskConfig jTaskConfig = new JTaskConfig();
		jTaskConfig.displayCancelButton(true);
		jTaskConfig.displayCloseButton(false);
		jTaskConfig.displayStatus(true);
		jTaskConfig.displayTimeElapsed(true);
		jTaskConfig.displayTimeRemaining(false);
		jTaskConfig.setAutoDispose(true);
		jTaskConfig.setModal(true);
		jTaskConfig.setOwner(Cytoscape.getDesktop());
		TaskManager.executeTask(task, jTaskConfig);
	}
}
