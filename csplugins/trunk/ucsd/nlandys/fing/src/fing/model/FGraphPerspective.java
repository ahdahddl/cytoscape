package fing.model;

import cytoscape.graph.dynamic.DynamicGraph;
import cytoscape.graph.dynamic.util.DynamicGraphFactory;
import cytoscape.util.intr.IntArray;
import cytoscape.util.intr.IntEnumerator;
import cytoscape.util.intr.IntIntHash;

import giny.filter.Filter;
import giny.model.Edge;
import giny.model.GraphPerspective;
import giny.model.GraphPerspectiveChangeListener;
import giny.model.Node;
import giny.model.RootGraph;
import giny.model.RootGraphChangeEvent;
import giny.model.RootGraphChangeListener;

import java.util.Iterator;

// Package visible class.
class FGraphPerspective implements GraphPerspective
{

  public void addGraphPerspectiveChangeListener
    (GraphPerspectiveChangeListener listener)
  { // This method is not thread safe; synchronize on an object to make it so.
    m_lis[0] = GraphPerspectiveChangeListenerChain.add(m_lis[0], listener);
  }

  public void removeGraphPerspectiveChangeListener
    (GraphPerspectiveChangeListener listener)
  { // This method is not thread safe; synchronize on an object to make it so.
    m_lis[0] = GraphPerspectiveChangeListenerChain.remove(m_lis[0], listener);
  }

  // The object returned shares the same RootGraph with this object.
  public Object clone()
  {
    final IntEnumerator nativeNodes = m_graph.nodes();
    final IntEnumerator rootGraphNodeInx = new IntEnumerator() {
        public int numRemaining() { return nativeNodes.numRemaining(); }
        public int nextInt() {
          return m_nativeToRootNodeInxMap.getIntAtIndex
            (nativeNodes.nextInt()); } };
    final IntEnumerator nativeEdges = m_graph.edges();
    final IntEnumerator rootGraphEdgeInx = new IntEnumerator() {
        public int numRemaining() { return nativeEdges.numRemaining(); }
        public int nextInt() {
          return m_nativeToRootEdgeInxMap.getIntAtIndex
            (nativeEdges.nextInt()); } };
    return new FGraphPerspective(m_root, rootGraphNodeInx, rootGraphEdgeInx);
  }

  public RootGraph getRootGraph()
  {
    return m_root;
  }

  public int getNodeCount()
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getEdgeCount()
  {
    throw new IllegalStateException("not implemented yet");
  }

  public Iterator nodesIterator()
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List nodesList()
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] getNodeIndicesArray()
  {
    throw new IllegalStateException("not implemented yet");
  }

  public Iterator edgesIterator()
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List edgesList()
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] getEdgeIndicesArray()
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] getEdgeIndicesArray(int perspFromNodeInx,
                                   int perspToNodeInx,
                                   boolean includeUndirected,
                                   boolean includeBothDirections)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public Node hideNode(Node node)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int hideNode(int perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List hideNodes(java.util.List nodes)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] hideNodes(int[] perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public Node restoreNode(Node node)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int restoreNode(int perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List restoreNodes(java.util.List nodes)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List restoreNodes(java.util.List nodes,
                                     boolean restoreIncidentEdges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] restoreNodes(int[] perspNodeInx,
                            boolean restoreIncidentEdges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] restoreNodes(int[] perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public Edge hideEdge(Edge edge)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int hideEdge(int perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List hideEdges(java.util.List edges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] hideEdges(int[] perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public Edge restoreEdge(Edge edge)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int restoreEdge(int perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List restoreEdges(java.util.List edges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] restoreEdges(int[] perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public boolean containsNode(Node node)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public boolean containsNode(Node node, boolean recurse)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public boolean containsEdge(Edge edge)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public boolean containsEdge(Edge edge, boolean recurse)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public GraphPerspective join(GraphPerspective persp)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public GraphPerspective createGraphPerspective(Node[] nodes, Edge[] edges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public GraphPerspective createGraphPerspective(int[] perspNodeInx,
                                                 int[] perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public GraphPerspective createGraphPerspective(Filter filter)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List neighborsList(Node node)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] neighborsArray(int perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public boolean isNeighbor(Node aNodel, Node anotherNode)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public boolean isNeighbor(int perspNodeInx, int perspAnotherNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public boolean edgeExists(Node from, Node to)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public boolean edgeExists(int perspFromNodeInx, int perspToNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getEdgeCount(Node from, Node to, boolean countUndirectedEdges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getEdgeCount(int perspFromNodeInx, int perspToNodeInx,
                          boolean countUndirectedEdges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List edgesList(Node from, Node to)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List edgesList(int perspFromNodeInx,
                                  int perspToNodeInx,
                                  boolean includeUndirectedEdges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] getEdgeIndicesArray(int perspFromNodeInx,
                                   int perspToNodeInx,
                                   boolean includeUndirectedEdges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getInDegree(Node node)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getInDegree(int perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getInDegree(Node node, boolean countUndirectedEdges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getInDegree(int perspNodeInx, boolean countUndirectedEdges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getOutDegree(Node node)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getOutDegree(int perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getOutDegree(Node node, boolean countUndirectedEdges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getOutDegree(int perspNodeInx, boolean countUndirectedEdges)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getDegree(Node node)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getDegree(int perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getIndex(Node node)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getNodeIndex(int rootGraphNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getRootGraphNodeIndex(int perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public Node getNode(int perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getIndex(Edge edge)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getEdgeIndex(int rootGraphEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getRootGraphEdgeIndex(int perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public Edge getEdge(int perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getEdgeSourceIndex(int perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int getEdgeTargetIndex(int perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public boolean isEdgeDirected(int perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public boolean isMetaParent(Node child, Node parent)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public boolean isNodeMetaParent(int perspChildNodeInx,
                                  int perspParentNodeInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public java.util.List metaParentsList(Node node)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public java.util.List nodeMetaParentsList(int perspNodeInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public int[] getNodeMetaParentIndicesArray(int perspNodeInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public boolean isMetaChild(Node parent, Node child)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public boolean isNodeMetaChild(int perspNodeInx, int perspChildInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public java.util.List nodeMetaChildrenList(Node node)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public java.util.List nodeMetaChildrenList(int perspParentInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public int[] getNodeMetaChildIndicesArray(int perspNodeInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public boolean isMetaParent(Edge child, Node parent)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public boolean isEdgeMetaParent(int perspChildEdgeInx,
                                  int perspParentNodeInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public java.util.List metaParentsList(Edge edge)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public java.util.List edgeMetaParentsList(int perspEdgeInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public int[] getEdgeMetaParentIndicesArray(int perspEdgeInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public boolean isMetaChild(Node parent, Edge child)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public boolean isEdgeMetaChild(int perspParentNodeInx,
                                 int perspChildEdgeInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public java.util.List edgeMetaChildrenList(Node node)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public java.util.List edgeMetaChildrenList(int perspParentNodeInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public int[] getEdgeMetaChildIndicesArray(int perspParentNodeInx)
  {
    throw new UnsupportedOperationException("meta nodes not yet supported");
  }

  public java.util.List getAdjacentEdgesList(Node node,
                                             boolean undirected,
                                             boolean incoming,
                                             boolean outgoing)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] getAdjacentEdgeIndicesArray(int perspNodeInx,
                                           boolean undirected,
                                           boolean incoming,
                                           boolean outgoing)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public java.util.List getConnectingEdges(java.util.List nodes)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] getConnectingEdgeIndicesArray(int[] perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public int[] getConnectingNodeIndicesArray(int[] perspEdgeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  public GraphPerspective createGraphPerspective(int[] perspNodeInx)
  {
    throw new IllegalStateException("not implemented yet");
  }

  // Nodes and edges in this graph are called "native indices" throughout
  // this class.
  private final DynamicGraph m_graph =
    DynamicGraphFactory.instantiateDynamicGraph();

  private final FRootGraph m_root;

  // This is an array of length 1 - we need an array as an extra reference
  // to a reference because some other inner classes need to know what the
  // current listener is.
  private final GraphPerspectiveChangeListener[] m_lis =
    new GraphPerspectiveChangeListener[1];

  private int m_numNodes;
  private int m_numEdges;

  // RootGraph indices are negative in these arrays.
  private final IntArray m_nativeToRootNodeInxMap = new IntArray();
  private final IntArray m_nativeToRootEdgeInxMap = new IntArray();

  // RootGraph indices are ~ (complements) of the real RootGraph indices
  // in these hashtables.
  private final IntIntHash m_rootToNativeNodeInxMap = new IntIntHash();
  private final IntIntHash m_rootToNativeEdgeInxMap = new IntIntHash();

  // Package visible constructor.  rootGraphNodeInx
  // must contain all endpoint nodes corresponding to edges in
  // rootGraphEdgeInx.  All indices must correspond to existing nodes
  // and edges.  The indices lists must be non-repeating.
  FGraphPerspective(FRootGraph root,
                    IntEnumerator rootGraphNodeInx,
                    IntEnumerator rootGraphEdgeInx)
  {
    m_root = root;
    m_numNodes = rootGraphNodeInx.numRemaining();
    m_numEdges = rootGraphEdgeInx.numRemaining();
    for (int i = 0; i < m_numNodes; i++) {
      final int rootNodeInx = rootGraphNodeInx.nextInt();
      final int nativeNodeInx = m_graph.createNode();
      m_nativeToRootNodeInxMap.setIntAtIndex(rootNodeInx, nativeNodeInx);
      m_rootToNativeNodeInxMap.put(~rootNodeInx, nativeNodeInx); }
    for (int i = 0; i < m_numEdges; i++) {
      final int rootEdgeInx = rootGraphEdgeInx.nextInt();
      final int rootEdgeSourceInx = m_root.getEdgeSourceIndex(rootEdgeInx);
      final int rootEdgeTargetInx = m_root.getEdgeTargetIndex(rootEdgeInx);
      final boolean rootEdgeDirected = m_root.isEdgeDirected(rootEdgeInx);
      final int nativeEdgeSourceInx =
        m_rootToNativeNodeInxMap.get(~rootEdgeSourceInx);
      final int nativeEdgeTargetInx =
        m_rootToNativeNodeInxMap.get(~rootEdgeTargetInx);
      final int nativeEdgeInx =
        m_graph.createEdge(nativeEdgeSourceInx, nativeEdgeTargetInx,
                           rootEdgeDirected);
      m_nativeToRootEdgeInxMap.setIntAtIndex(rootEdgeInx, nativeEdgeInx);
      m_rootToNativeEdgeInxMap.put(~rootEdgeInx, nativeEdgeInx); }
  }

  // Cannot have any recursize reference to a FGraphPerspective in this
  // object instance - we want to allow garbage collection of unused
  // GraphPerspective objects.
  private final static class RootGraphChangeSniffer
    implements RootGraphChangeListener
  {

    public final void rootGraphChanged(RootGraphChangeEvent evt)
    {
    }

  }

  // An instance of this class cannot have any recursive reference to a
  // FGraphPerspective object.  The idea behind this class is to allow
  // garbage collection of unused GraphPerspective objects.  This class
  // is used by the RootGraphChangeSniffer to remove nodes/edges from
  // a GraphPerspective; this class is also used by this GraphPerspective
  // implementation itself.
  private final static class GraphWeeder
  {

    private final DynamicGraph m_graph;

    // This is an array of length 1 - we need an array as an extra reference
    // to a reference because the surrounding GraphPerspective will be
    // modifying the entry at index 0 in this array.
    private final GraphPerspectiveChangeListener[] m_lis;

    private GraphWeeder(DynamicGraph graph,
                        GraphPerspectiveChangeListener[] listener)
    {
      m_graph = graph;
      m_lis = listener;
    }

    private final int hideNode(int rootGraphNodeInx)
    {
      return 0;
    }

    private final int[] hideNodes(int[] rootGraphNodeInx)
    {
      return null;
    }

    private final int hideEdge(int rootGraphNodeInx)
    {
      return 0;
    }

    private final int[] hideEdges(int[] rootGraphNodeInx)
    {
      return null;
    }

  }

}
