package fing.model.test;

import fing.model.FingRootGraphFactory;
import giny.model.Edge;
import giny.model.GraphPerspective;
import giny.model.Node;
import giny.model.RootGraph;

import java.util.Iterator;
import java.util.List;

public final class AllGraphPerspectiveMethodsTest
{

  // No constructor.
  private AllGraphPerspectiveMethodsTest() { }

  public static final void main(String[] args)
  {
    final RootGraph root = FingRootGraphFactory.instantiateRootGraph();
    final GraphPerspective persp =
      root.createGraphPerspective((int[]) null, (int[]) null);
    int[] nodeInx = new int[5];
    for (int i = 0; i < nodeInx.length - 1; i++)
      nodeInx[i] = root.createNode();
    int[] edgeInx = new int[7];
    edgeInx[0] = root.createEdge(nodeInx[0], nodeInx[1], true);
    edgeInx[1] = root.createEdge(nodeInx[1], nodeInx[2], false);
    edgeInx[2] = root.createEdge(nodeInx[2], nodeInx[0], true);
    edgeInx[3] = root.createEdge(nodeInx[2], nodeInx[2], true);
    edgeInx[4] = root.createEdge(nodeInx[1], nodeInx[1], false);
    edgeInx[5] = root.createEdge(nodeInx[1], nodeInx[0], true);
    edgeInx[6] = root.createEdge(nodeInx[3], nodeInx[2], true);
    nodeInx[nodeInx.length - 1] = root.createNode
      (null, new int[] { edgeInx[6], edgeInx[2] });
    if (!(root.addNodeMetaChild(nodeInx[0], nodeInx[1]) &&
          root.addNodeMetaChild(nodeInx[0], nodeInx[4]) &&
          root.addNodeMetaChild(nodeInx[3], nodeInx[1]) &&
          root.addNodeMetaChild(nodeInx[4], nodeInx[4]) &&
          root.addEdgeMetaChild(nodeInx[3], edgeInx[6]) &&
          root.addEdgeMetaChild(nodeInx[3], edgeInx[0]) &&
          root.addEdgeMetaChild(nodeInx[0], edgeInx[4])))
      throw new IllegalStateException("unable to create meta relationship");
    for (int i = 0; i < nodeInx.length; i++)
      if (persp.restoreNode(nodeInx[i]) != nodeInx[i])
        throw new IllegalStateException("unable to restore node");
    for (int i = 0; i < edgeInx.length; i++)
      if (persp.restoreEdge(edgeInx[i]) != edgeInx[i])
        throw new IllegalStateException("unable to restore edge");
    int minNodeInx = 0;
    for (int i = 0; i < nodeInx.length; i++)
      minNodeInx = Math.min(minNodeInx, nodeInx[i]);
    int minEdgeInx = 0;
    for (int i = 0; i < edgeInx.length; i++)
      minEdgeInx = Math.min(minEdgeInx, edgeInx[i]);
    RootGraph root2 = FingRootGraphFactory.instantiateRootGraph();
    root2.createNode();
    root2.createEdge
      (((Node) root2.nodesIterator().next()).getRootGraphIndex(),
       ((Node) root2.nodesIterator().next()).getRootGraphIndex());
    final Node root2Node = (Node) root2.nodesIterator().next();
    final Edge root2Edge = (Edge) root2.edgesIterator().next();
    final Node nodeNotInPersp = root.getNode(root.createNode());
    final Edge edge1NotInPersp = root.getEdge
      (root.createEdge(nodeInx[1], nodeInx[0], true));
    final Edge edge2NotInPersp = root.getEdge
      (root.createEdge(nodeInx[2], nodeNotInPersp.getRootGraphIndex(), false));

    // Not testing GraphPerspectiveChangeListener methods.

    // clone().
    final GraphPerspective persp2 = (GraphPerspective) persp.clone();
    if (persp2.getNodeCount() != persp.getNodeCount() ||
        persp2.getEdgeCount() != persp.getEdgeCount())
      throw new IllegalStateException("clone has different topology");
    int[] edgeInxArr = persp2.getEdgeIndicesArray();
    for (int i = 0; i < edgeInxArr.length; i++)
      if (persp2.hideEdge(edgeInxArr[i]) != edgeInxArr[i])
        throw new IllegalStateException("cannot hide edge in clone");
    if (persp2.getEdgeCount() != 0)
      throw new IllegalStateException("some edges in clone remaining");
    if (persp2.getNodeCount() != persp.getNodeCount())
      throw new IllegalStateException("node counts should still be the same");
    int[] nodeInxArr = persp2.getNodeIndicesArray();
    for (int i = 0; i < nodeInxArr.length; i++)
      if (persp2.hideNode(nodeInxArr[i]) != nodeInxArr[i])
        throw new IllegalStateException("cannot hide node in clone");
    if (persp2.getNodeCount() != 0 || persp2.getEdgeCount() != 0)
      throw new IllegalStateException("nodes or edges remaining");

    // getRootGraph().
    if (persp.getRootGraph() != root || persp2.getRootGraph() != root)
      throw new IllegalStateException("incorrect RootGraph");

    // getNodeCount().
    if (persp.getNodeCount() != 5)
      throw new IllegalStateException("wrong number of nodes");

    // getEdgeCount().
    if (persp.getEdgeCount() != 7)
      throw new IllegalStateException("wrong number of edges");

    // nodesIterator().
    Iterator nodesIter = persp.nodesIterator();
    Node[] twoNodes = new Node[] { (Node) nodesIter.next(),
                                   (Node) nodesIter.next() };

    // nodesList().
    List nodesList = persp.nodesList();
    if (nodesList.size() != 5)
      throw new IllegalStateException("incorrect node List size");
    for (int i = 0; i < nodesList.size(); i++) {
      Node n = (Node) nodesList.get(i); }

    // getNodeIndicesArray().
    int[] nodeIndicesArray = persp.getNodeIndicesArray();
    if (nodeIndicesArray.length != nodesList.size() + 1)
      throw new IllegalStateException
        ("size of nodes List and length of node indices array don't match");
    if (nodeIndicesArray[0] != 0)
      throw new IllegalStateException("expected 0 at index 0");
    for (int j = 0; j < nodeInx.length; j++) {
      for (int i = 1;; i++) { if (nodeIndicesArray[i] == nodeInx[j]) break; } }

    // edgesIterator().
    Iterator edgesIter = persp.edgesIterator();
    Edge[] twoEdges = new Edge[] { (Edge) edgesIter.next(),
                                   (Edge) edgesIter.next() };

    // edgesList().
    List edgesList = persp.edgesList();
    if (edgesList.size() != 7)
      throw new IllegalStateException("incorrect edge List size");
    for (int i = 0; i < edgesList.size(); i++) {
      Edge e = (Edge) edgesList.get(i); }

    // getEdgeIndicesArray().
    int[] edgeIndicesArray = persp.getEdgeIndicesArray();
    if (edgeIndicesArray.length != edgesList.size() + 1)
      throw new IllegalStateException
        ("size of edges List and length of edge indices array don't match");
    for (int j = 0; j < edgeInx.length; j++) {
      for (int i = 1;; i++) { if (edgeIndicesArray[i] == edgeInx[j]) break; } }

    // getEdgeIndicesArray(int, int, boolean, boolean).
    int[] connEdges;
    connEdges = persp.getEdgeIndicesArray(nodeInx[1], nodeInx[0], false, true);
    if (connEdges.length != 2)
      throw new IllegalStateException("not 2 connecting edges");
    for (int i = 0;; i++) if (connEdges[i] == edgeInx[0]) break;
    for (int i = 0;; i++) if (connEdges[i] == edgeInx[5]) break;
    connEdges = persp.getEdgeIndicesArray(nodeInx[0], nodeInx[3], true, true);
    if (connEdges.length != 0)
      throw new IllegalStateException("not 0 connecting edges");
    connEdges = persp.getEdgeIndicesArray(nodeInx[1], nodeInx[2], false, true);
    if (connEdges.length != 0)
      throw new IllegalStateException("not 0 connecting edges");
    connEdges = persp.getEdgeIndicesArray(nodeInx[2], nodeInx[1], true, false);
    if (connEdges.length != 1)
      throw new IllegalStateException("not 1 connecting edge");
    for (int i = 0;; i++) if (connEdges[i] == edgeInx[1]) break;
    connEdges =
      persp.getEdgeIndicesArray(nodeInx[2], nodeInx[2], false, false);
    if (connEdges.length != 1)
      throw new IllegalStateException("not 1 connecting edge");
    for (int i = 0;; i++) if (connEdges[i] == edgeInx[3]) break;
    connEdges = persp.getEdgeIndicesArray(nodeInx[2], nodeInx[2], true, true);
    if (connEdges.length != 1)
      throw new IllegalStateException("not 1 connecting edge");
    for (int i = 0;; i++) if (connEdges[i] == edgeInx[3]) break;
    connEdges =
      persp.getEdgeIndicesArray(nodeInx[2], nodeInx[3], false, false);
    if (connEdges.length != 0)
      throw new IllegalStateException("not 0 connecting edges");
    connEdges =
      persp.getEdgeIndicesArray(nodeInx[3], nodeInx[2], false, false);
    if (connEdges.length != 1)
      throw new IllegalStateException("not 1 connecting edge");
    for (int i = 0;; i++) if (connEdges[i] == edgeInx[6]) break;
    connEdges = persp.getEdgeIndicesArray(nodeInx[4], nodeInx[0], true, true);
    if (connEdges.length != 0)
      throw new IllegalStateException("not 0 connecting edges");
    connEdges = persp.getEdgeIndicesArray(99, 0, true, true);
    if (connEdges != null) throw new IllegalStateException("not null");
    connEdges = persp.getEdgeIndicesArray(nodeInx[0], minNodeInx - 1,
                                         true, false);
    if (connEdges != null) throw new IllegalStateException("not null");
    if (persp.getEdgeIndicesArray(Integer.MAX_VALUE, Integer.MIN_VALUE,
                                 true, false) != null ||
        persp.getEdgeIndicesArray(Integer.MIN_VALUE, Integer.MAX_VALUE,
                                 false, false) != null)
      throw new IllegalStateException("not null");

    // hide/restore mothods are tested elsewhere.

    // containsNode(Node).
    if (!persp.containsNode(twoNodes[1]))
      throw new IllegalStateException("GraphPersp does not contain node");
    if (persp.containsNode(root2Node))
      throw new IllegalStateException("GraphPersp contains node from other");
    if (persp.containsNode(nodeNotInPersp) ||
        !persp.getRootGraph().containsNode(nodeNotInPersp))
      throw new IllegalStateException("GraphPerspective contains node");

    // containsEdge(Edge).
    if (!persp.containsEdge(twoEdges[1]))
      throw new IllegalStateException("GraphPersp does not contain edge");
    if (persp.containsEdge(root2Edge))
      throw new IllegalStateException("GraphPersp contains edge from other");
    if (persp.containsEdge(edge1NotInPersp) ||
        persp.containsEdge(edge2NotInPersp) ||
        !(persp.getRootGraph().containsEdge(edge1NotInPersp) &&
          persp.getRootGraph().containsEdge(edge2NotInPersp)))
      throw new IllegalStateException("GraphPerspective contains edge");
  }

}
