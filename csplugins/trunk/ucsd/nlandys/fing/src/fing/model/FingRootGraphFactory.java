package fing.model;

import giny.model.RootGraph;

/**
 * This class defines static methods that provide new instances
 * of giny.model.RootGraph objects.
 */
public final class FingRootGraphFactory
{

  // "No constructor".
  private FingRootGraphFactory() { }

  /**
   * Returns a new instance of giny.model.RootGraph.  Obviously, a new
   * RootGraph instance contains no nodes or edges.<p>
   * A secret feature is that the returned object not only implements
   * RootGraph - it also implements cytoscape.graph.dynamic.DynamicGraph.
   * In other words, you can cast the return value to DynamicGraph.  The
   * relationship between RootGraph node/edge indices and DynamicGraph nodes
   * and edges is they are complements of each other.  Complement is '~' in
   * Java.<p>
   * In addition to the RootGraph secretly implementing DyanmicGraph,
   * all of the GraphPerspective objects generated by the returned RootGraph
   * secretly implement cytoscape.graph.fixed.FixedGraph.  In other words,
   * all GraphPerspective objects that are part of this RootGraph system
   * can be cast to FixedGraph.  The relationship between GraphPerspective
   * node/edge indices (which are identical to RootGraph indices) and
   * FixedGraph nodes and edges is they are complements of each other.
   */
  public final static RootGraph instantiateRootGraph()
  {
    return new FRootGraph();
  }

}
