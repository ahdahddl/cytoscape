package cytoscape.actions;

import java.awt.event.ActionEvent;
import javax.swing.*;
import phoebe.PGraphView;
import cytoscape.view.NetworkView;

public class SquiggleAction extends JMenu {
  private JMenuItem squiggleMode;
  private JMenuItem clearSquiggle;
  private boolean enabled;

  public SquiggleAction ( final NetworkView networkview ) {
    super("Squiggle");

    squiggleMode = new JMenuItem( new AbstractAction( "Enable" ) {
	  public void actionPerformed ( ActionEvent e ) {
	    // Do this in the GUI Event Dispatch thread...
	    SwingUtilities.invokeLater( new Runnable() {
	      public void run() {
		    PGraphView view = (PGraphView)networkview.getView();
		    if (enabled) {
              view.getSquiggleHandler().beginSquiggling();
              squiggleMode.setText("Disable");
            } else {
              view.getSquiggleHandler().stopSquiggling();
              squiggleMode.setText("Enable");
            }
            clearSquiggle.setEnabled(enabled);
            enabled = !enabled;
	  } } ); } } ) ;
    add(squiggleMode);

    clearSquiggle =  new JMenuItem( new AbstractAction( "Clear" ) {
      public void actionPerformed ( ActionEvent e ) {
        // Do this in the GUI Event Dispatch thread...
        SwingUtilities.invokeLater( new Runnable() {
          public void run() {
            PGraphView view = (PGraphView)networkview.getView();
              view.getSquiggleHandler().clearSquiggles();
      } } ); } } );
    clearSquiggle.setEnabled(false);
    add(clearSquiggle);
  }
}
