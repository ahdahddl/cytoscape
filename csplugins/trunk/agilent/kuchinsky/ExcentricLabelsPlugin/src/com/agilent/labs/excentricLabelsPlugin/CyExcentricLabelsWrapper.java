package com.agilent.labs.excentricLabelsPlugin;

import infovis.visualization.magicLens.ExcentricLabels;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * AJK: 07/22/06 substitute for Visualization because I can't seem to be able to add
 * Visualization to a JComponent.  Extends JPanel.
 */
public class CyExcentricLabelsWrapper extends JPanel {

    private ExcentricLabels excentric;
    protected CyExcentricVisualizationInteractor interactor;
    protected JComponent parent;
    private boolean isInstalled = false;
    private CyLabeledComponent labeledComponent;

    /**
     * Empty-Arg Constructor.
     */
    public CyExcentricLabelsWrapper () {
        super();
    }

    /**
     * Constructor.
     *
     * @param excentric        InfoViz Excentric Labels Object.
     * @param parent           Parent Component.
     * @param labeledComponent InfoViz Labeled Component Object.
     */
    public CyExcentricLabelsWrapper (ExcentricLabels excentric,
            JComponent parent, CyLabeledComponent labeledComponent) {
        super();
        this.setParent(parent);
        this.excentric = excentric;
        this.labeledComponent = labeledComponent;
        interactor = new CyExcentricVisualizationInteractor(this);
        this.setEnabled(true);
        this.setVisible(true);
    }

    /**
     * Paint the JPanel.
     *
     * @param g Graphics Object.
     */
    public void paint (Graphics g) {

        //  Conditionally call setVisible and setEnabled.
        if (!excentric.isVisible()) {
            excentric.setVisible(true);
        }
        if (!excentric.isEnabled()) {
            excentric.setEnabled(true);
        }
        //		Rectangle2D hBox = labeledComponent.getHitBox();
        //		if (hBox != null) {
        //			g.setClip(Math.max(((int) hBox.getMinX()) - 50, 0), Math.max(
        //					((int) hBox.getMinY()) - 50, 0), 100, 100);
        //		}
        System.out.println("Paint:  " + new Date());
        excentric.paint((Graphics2D) g, this.bounds());
    }

    /**
     * Gets the Excentric Label Object.
     *
     * @return InfoViz Excentric Label Object.
     */
    public ExcentricLabels getExcentric () {
        return excentric;
    }

    /**
     * Gets the Parent Component.
     *
     * @param parent Parent Component.
     */
    public void setParent (JComponent parent) {
        this.parent = parent;
    }

    /**
     * Gets the CyExcentricVisualizationInteractor Object.
     *
     * @return CyExcentricVisualizationInteractor Object.
     */
    public CyExcentricVisualizationInteractor getInteractor () {
        return interactor;
    }

    public boolean isInstalled () {
        return isInstalled;
    }

    public void setInstalled (boolean isInstalled) {
        this.isInstalled = isInstalled;
    }
}