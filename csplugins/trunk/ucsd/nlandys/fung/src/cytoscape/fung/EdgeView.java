package cytoscape.fung;

import cytoscape.render.immed.GraphGraphics;
import java.awt.Color;
import java.awt.Paint;

public final class EdgeView
{

  public final static byte ARROW_NONE = GraphGraphics.ARROW_NONE;
  public final static byte ARROW_DELTA = GraphGraphics.ARROW_DELTA;
  public final static byte ARROW_DIAMOND = GraphGraphics.ARROW_DIAMOND;
  public final static byte ARROW_DISC = GraphGraphics.ARROW_DISC;
  public final static byte ARROW_TEE = GraphGraphics.ARROW_TEE;

  Fung m_fung; // Not final so that we can destroy reference.
  private final int m_edge;

  EdgeView(final Fung fung, final int edge)
  {
    m_fung = fung;
    m_edge = edge;
  }

  public final int getEdge()
  {
    return m_edge;
  }

  public final Color getColorLowDetail()
  {
    synchronized (m_fung.m_lock) {
      return m_fung.m_edgeDetails.colorLowDetail(m_edge); }
  }

  public final void setColorLowDetail(final Color colorLowDetail)
  {
    synchronized (m_fung.m_lock) {
      m_fung.m_edgeDetails.overrideColorLowDetail(m_edge, colorLowDetail); }
  }

  public final byte getSourceArrow()
  {
    synchronized (m_fung.m_lock) {
      return m_fung.m_edgeDetails.sourceArrow(m_edge); }
  }

  public final void setSourceArrow(final byte arrow)
  {
    synchronized (m_fung.m_lock) {
      switch (arrow) {
      case ARROW_NONE:
      case ARROW_DELTA:
      case ARROW_DIAMOND:
      case ARROW_DISC:
      case ARROW_TEE:
        break;
      default:
        throw new IllegalArgumentException("arrow is not recognized"); }
      { // Reconcile arrow size if not ARROW_NONE.
        if (arrow != ARROW_NONE) {
          final double segmentThickness = getSegmentThickness();
          final double sourceArrowSize = getSourceArrowSize();
          if (!(sourceArrowSize >= segmentThickness)) {
            setSourceArrowSize(segmentThickness); } }
      }
      m_fung.m_edgeDetails.overrideSourceArrow(m_edge, arrow); }
  }

  public final double getSourceArrowSize()
  {
    synchronized (m_fung.m_lock) {
      return m_fung.m_edgeDetails.sourceArrowSize(m_edge); }
  }

  public final void setSourceArrowSize(final double arrowSize)
  {
    synchronized (m_fung.m_lock) {
      float fArrowSize = (float) arrowSize;
      if (!(fArrowSize >= 0.0f)) {
        throw new IllegalArgumentException
          ("arrowSize must be positive or zero"); }
      { // Reconcile arrow size.
        if (getSourceArrow() != ARROW_NONE) {
          final double segmentThickness = getSegmentThickness();
          if (!(fArrowSize >= segmentThickness)) {
            fArrowSize = (float) segmentThickness; } }
      }
      m_fung.m_edgeDetails.overrideSourceArrowSize(m_edge, fArrowSize); }
  }

  public final Paint getSourceArrowPaint()
  {
    synchronized (m_fung.m_lock) {
      return m_fung.m_edgeDetails.sourceArrowPaint(m_edge); }
  }

  public final void setSourceArrowPaint(final Paint paint)
  {
    synchronized (m_fung.m_lock) {
      m_fung.m_edgeDetails.overrideSourceArrowPaint(m_edge, paint); }
  }

  public final double getSegmentThickness()
  {
    synchronized (m_fung.m_lock) {
      return m_fung.m_edgeDetails.segmentThickness(m_edge); }
  }

}
