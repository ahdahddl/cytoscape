package giny.util;

import giny.filter.Filter;
import giny.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Calculates the all-pairs-shortest-paths (APSP) of a set of <code>giny.model.Node</code>
 * objects' indices that reside in a <code>giny.model.GraphPerspective</code>.
 * @see giny.util.NodeDistances
 */

/** @deprecated Yell loudly (cytoscape-discuss@googlegroups.com) if you want to keep this.
    If we do keep it, it will move somewhere else.  If no one yells, it'll be removed 10/2007 */
public class IntNodeDistances implements MonitorableTask {

  public static final int INFINITY = Integer.MAX_VALUE;
  
  protected int[] nodesArray;
  protected GraphPerspective perspective;
  protected int[][] distances;
  protected boolean directed;
  
  // Keep track of progress:
  protected int currentProgress;
  protected int lengthOfTask;
  protected boolean done;
  protected boolean canceled;
  protected String statusMessage;
  
  /**
   * Constructor, it assumes that the graph is undirected.
   *
   * @param nodes_array an array of <code>giny.model.GraphPerspective</code> node indices for which
   * distances will be calculated
   * @param distances the 2D array where distances will be stored (possibly null)
   * @param perspective the <code>giny.model.GraphPerspective</code> in which the nodes reside
   */
  public IntNodeDistances ( int[] nodes_array, int[][] distances, GraphPerspective perspective ) {
    this( nodes_array, distances, perspective, false );
  }//IntNodeDistances

  /**
   * Constructor, specifies whether the graph should be trated as a directed graph or not.
   *
   * @param nodes_array an array of <code>giny.model.GraphPerspective</code> node indices for which
   * distances will be calculated
   * @param distances the 2D array where distances will be stored (possibly null)
   * @param perspective the <code>giny.model.GraphPerspective</code> in which the nodes reside
   * @param directed if true, the graph is treated as a directed graph, else, it is treated as 
   * an undirected graph
   */
  public IntNodeDistances (
                           int[] nodes_array,
                           int[][] distances,
                           GraphPerspective perspective,
                           boolean directed
                           ) {
    this.directed = directed;
    this.perspective = perspective;
    nodesArray = nodes_array;
    if( distances == null ) {
      this.distances = new int[ nodesArray.length - 1 ][];
    } else {
      this.distances = distances;
    }
  }//IntNodeDistances
  
  /**
   * @return <code>true</code> if the task is done, false otherwise
   */
  public boolean isDone (){
    return this.done;
  }//isDone

  /**
   * @return the current progress
   */
  public int getCurrentProgress (){
    return this.currentProgress;
  }//getCurrentProgress

  /**
   * @return the total length of the task
   */
  public int getLengthOfTask (){
    return this.lengthOfTask;
  }//getLengthOfTask

  /**
   * @return a <code>String</code> describing the task being performed
   */
  public String getTaskDescription (){
    return "Calculating Node Distances";
  }//getTaskDescription
  
  /**
   * @return a <code>String</code> status message describing what the task
   * is currently doing (example: "Completed 23% of total.", "Initializing...", etc).
   */
  public String getCurrentStatusMessage (){
    return this.statusMessage;
  }//getCurrentStatusMessage
  
  
  /**
   * Stops the task if it is currently running.
   */
  public void stop(){
    this.canceled = true;
    this.statusMessage = null;
  }//stop
  
  /**
   * @return <code>true</code> if the task was canceled before it was done
   * (for example, by calling <code>MonitorableSwingWorker.stop()</code>, 
   * <code>false</code> otherwise
   */
  // TODO: Not sure if needed
  public boolean wasCanceled (){
    return this.canceled;
  }//wasCanceled
  
  /**
   * Calculates the distances.
   *
   * @return the <code>int[][]</code> array of distances, or null if the task was canceled or
   * there was an error
   */
  public int[][] calculate () {
    
    this.currentProgress = 0;
    this.lengthOfTask = distances.length;
    this.done = false;
    this.canceled = false;
    
    int[] nodes = new int[ nodesArray.length -1 ];

    // TODO: REMOVE
    //System.err.println( "Calculating all node distances.. for: "
    // + (nodesArray.length - 1)+" and "+(nodes.length ));

    // We don't have to make new Integers all the time, so we store the index
    // Objects in this array for reuse.
    Integer[] integers = new Integer[ nodes.length ];

    // Fill the nodes array with the nodes in their proper index locations.
    int index;
    int from_node;
    for( int i = 0; i <= nodes.length; i++ ) {

      from_node = nodesArray[ i ];
      if( from_node == 0 ) {
        continue;
      }
      index = perspective.getNodeIndex( from_node ) - 1;
      
      if( ( index < 0 ) || ( index >= nodes.length ) ) {
        System.err.println( "WARNING: GraphNode \"" + from_node +
                            "\" has an index value that is out of range: " +
                            index +
                            ".  Graph indices should be maintained such " +
                            "that no index is unused." );
        return null;
      }
      if( nodes[ index ] != 0 ) {
        System.err.println(   "WARNING: GraphNode \"" + from_node +
                            "\" has an index value ( " + index + " ) that is the same as " +
                            "that of another GraphNode ( \"" + nodes[ index ] +
                            "\" ).  Graph indices should be maintained such " +
                            "that indices are unique." );
        return null;
      }
      nodes[ index ] = from_node;
      integers[ index ] = new Integer( index );
    }

    LinkedList queue = new LinkedList();
    boolean[] completed_nodes = new boolean[ nodes.length ];
    int[] neighbors;
    int to_node;
    int neighbor;
    int neighbor_index;
    int to_node_distance;
    int neighbor_distance;
    for( int from_node_index = 0;
         from_node_index < nodes.length;
         from_node_index++ ) {
      
      if(this.canceled){
        return null;
      }
      
      from_node = nodes[ from_node_index ];

      if( from_node == 0 ) {
        // Make the distances in this row all Integer.MAX_VALUE.
        if( distances[ from_node_index ] == null ) {
          distances[ from_node_index ] = new int[ nodes.length ];
        }
        Arrays.fill( distances[ from_node_index ], Integer.MAX_VALUE );
        continue;
      }

      // TODO: REMOVE
      //  System.err.print( "Calculating node distances from graph node " +
      //                  from_node );
      //System.err.flush();
      
      // Make the distances row and initialize it.
      if( distances[ from_node_index ] == null ) {
        distances[ from_node_index ] = new int[ nodes.length ];
      }
      Arrays.fill( distances[ from_node_index ], Integer.MAX_VALUE );
      distances[ from_node_index ][ from_node_index ] = 0;

      // Reset the completed nodes array.
      Arrays.fill( completed_nodes, false );

      // Add the start node to the queue.
      queue.add( integers[ from_node_index ] );

      while( !( queue.isEmpty() ) ) {
        
        if(this.canceled){
          return null;
        }
        
        index = ( ( Integer )queue.removeFirst() ).intValue();
        if( completed_nodes[ index ] ) {
          continue;
        }
        completed_nodes[ index ] = true;

        to_node = nodes[ index ];
        to_node_distance = distances[ from_node_index ][ index ];

        if( index < from_node_index ) {
          // Oh boy.  We've already got every distance from/to this node.
          int distance_through_to_node;
          for( int i = 0; i < nodes.length; i++ ) {
            if( distances[ index ][ i ] == Integer.MAX_VALUE ) {
              continue;
            }
            distance_through_to_node =
              to_node_distance + distances[ index ][ i ];
            if( distance_through_to_node <=
                distances[ from_node_index ][ i ] ) {
              // Any immediate neighbor of a node that's already been
              // calculated for that does not already have a shorter path
              // calculated from from_node never will, and is thus complete.
              if( distances[ index ][ i ] == 1 ) {
                completed_nodes[ i ] = true;
              }
              distances[ from_node_index ][ i ] =
                distance_through_to_node;
            }
          } // End for every node, update the distance using the distance from
            // to_node.
          // So now we don't need to put any neighbors on the queue or
          // anything, since they've already been taken care of by the previous
          // calculation.
          continue;
        } // End if to_node has already had all of its distances calculated.

        neighbors = perspective.neighborsArray( to_node );

        //while( neighbors.hasNext() ) {
        //   neighbor = ( int )neighbors.next();
        for ( int i = 0; i < neighbors.length; ++i ) {
          
          if(this.canceled){
            return null;
          }
          
          neighbor = neighbors[i];

          //neighbor_index = perspective.getNodeIndex( neighbor ) - 1;
          neighbor_index = neighbor - 1;


          // If this neighbor was not in the incoming List, we cannot include
          // it in any paths.
          if( nodes[ neighbor_index ] == 0 ) {
            distances[ from_node_index ][ neighbor_index ] =
              Integer.MAX_VALUE;
            continue;
          }

          if( completed_nodes[ neighbor_index ] ) {
            // We've already done everything we can here.
            continue;
          }

          neighbor_distance = distances[ from_node_index ][ neighbor_index ];

          if( ( to_node_distance != Integer.MAX_VALUE ) &&
              ( neighbor_distance > ( to_node_distance + 1 ) ) ) {
            distances[ from_node_index ][ neighbor_index ] =
              ( to_node_distance + 1 );
            queue.addLast( integers[ neighbor_index ] );
          }

          // TODO: REMOVE
          //System.out.print( "." );
          //System.out.flush();
          

        } // For each of the next nodes' neighbors
        // TODO: REMOVE
        //System.out.print( "|" );
        //System.out.flush();
        

      } // For each to_node, in order of their (present) distances

      // TODO: REMOVE
      /*
      System.err.println( "done." );
      */
      this.currentProgress++;
      double percentDone = (this.currentProgress*100)/this.lengthOfTask;
      this.statusMessage = "Completed " + percentDone + "%.";
    } // For each from_node
  
  // TODO: REMOVE
  //System.err.println( "..Done calculating all node distances." );

    this.done = true;
    this.currentProgress = this.lengthOfTask; // why?
    return distances;
  }//calculate

  /**
   * @returns the <code>int[][]</code> array of calculated distances, 
   * or null if not calculated yet
   */
  public int[][] getDistances () {
    return this.distances;
  }//getDistances

  /**
   * Starts doing the task in a separate thread so that the GUI stays responsive
   *
   * @param return_when_done if <code>true</code>, then this method will return only when
   * the task is done, else, it will return immediately after spawning the thread that
   * performs the task
   */
  public void start (boolean return_when_done){
    final SwingWorker worker = new SwingWorker(){
        public Object construct (){
          return new IntNodeDistancesTask();
        }//construct
      };
    worker.start();
    if(return_when_done){
      worker.get(); // maybe use finished() instead
    }
  }//starts
  
  class IntNodeDistancesTask {
    IntNodeDistancesTask(){
      calculate();
    }//IntNodeDistancesTask
  }//class IntNodeDistancesTask
  

}//class IntNodeDistances
