package oy.interact.tira.student.graph;

import java.util.Map;
import java.util.Set;
import java.util.PriorityQueue;


import oy.interact.tira.student.graph.Edge.EdgeType;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

// Omien pino- ja jono-tietorakenteiden importit
import oy.interact.tira.student.StackImplementation;
import oy.interact.tira.student.ArrayQueue;

/**
 * Implementation of the graph data structure and associated algorithms.
 * <p>
 * This class implements the graph data structure and various
 * algorithms like breadth-first search, depth-first search and the Dijkstra
 * path finding algorithm.
 * <p>
 * The class needs your attention, dear student. Implement the methods
 * marked TODO in the comments, based on what you have learned about
 * graphs.
 * <p>
 * The Graph as a generic (template) class can use any data types conforming to 
 * the Comparable interface.
 * <p>
 * This implementation uses edge lists to store the graph vertices
 * and edges.
 * 
 * @author Antti Juustila
 * @version 1.0
 */
public class Graph<T> {

   /** A listener that must be told whenever a search algorithm meets a vertex
    * it has not met before. This must be done in BFS and DFS algorithms.
    */
   private GraphPathListener<T> listener;

   /** The edge list of the graph. Select and instantiate
    * a suitable type of Map, depending on application needs.
    */
   private Map<Vertex<T>, List<Edge<T>>> edgeList = null;
   
   /**
    * Constructor instantiates a suitable Map data structure.
    *
    * A listener is provided as a parameter. Graph must notify the
    * listener when searching the graph, by calling listener.pathChanged(),
    * whenever a new vertex is met on the search path.
    */
   public Graph(GraphPathListener<T> listener) {
      this.listener = listener;
      // STUDENT TODO: remove throw and initialize the edge list for the graph
      // Alustus Hashmapilla
      this.edgeList = new HashMap<>();
   }

   /**
    * Clears the graph from all data.
    */
   public void clear() {
      // STUDENT TODO
      // Jos LIsta ei ole tyhjä, kutsutaan Javan Hashmapin clear-metodia
      if (edgeList != null) {
         edgeList.clear();
      }
   }

   /**
    * Returns the map of vertices with their edges.
    *
    * @return The edge list for the graph.
    */
   public Map<Vertex<T>, List<Edge<T>>> getEdgeList() {
      return edgeList;
   }

   /**
    * Creates a vertex holding the dataItem (node in a vertex) in the graph.
    * Use this method always to add vertices to the graph.
    *
    * Create the vertex object with the data item, then create an empty
    * list of edges, and put the vertex and the empty list to the Map.
    *
    * The newly created vertex must have an empty list of edges.
    * 
    * @param element The data item to put in the vertex of the graph.
    * @return Returns the created vertex, placed in the graph's edge list.
    */
   public Vertex<T> createVertexFor(T element) {
      // STUDENT TODO
      // Luodaan uusi Vertex-olio
      Vertex<T> newVertex = new Vertex<>(element);

      // Lisätään uusi Vertex-olio Map-rakenteeseen, putIfAbsent-metodi varmistaa ettei olemassa olevaa solmua
      // vahingossa ylikirjoiteta
      edgeList.putIfAbsent(newVertex, new ArrayList<>());

      return newVertex;
   }

   /**
    * Get all the vertices of the graph in a Set.

    * @return A Set with all the vertices of the graph.
    */
   public Set<Vertex<T>> getVertices() {
      // STUDENT TODO
      // Javan Map-luokan keySet-metodi palauttaa kaikki avaimet eli verkon solmut
      return edgeList.keySet();
   }

   /**
    * Adds an edge to the graph. Note that the vertices MUST have been created 
    * earlier by calling {@code createVertexFor(T)} and are already in the graph.
    *
    * @param type The type of the edge, either directed or undirected.
    * @param source The source vertex of the edge.
    * @param destination The destination vertex of the edge.
    * @param weight The weight of the edge.
    */
   public void addEdge(Edge.EdgeType type, Vertex<T> source, Vertex<T> destination, double weight) {
      // STUDENT TODO
      // Lisätään reuna aina lähteestä kohteeseen
      addDirectedEdge(source, destination, weight);

      // Jos kyseessä on suuntaamaton verkko, lisätään myös käänteinen reuna kohde->lähde
      if (type == Edge.EdgeType.UNDIRECTED) {
         addDirectedEdge(destination, source, weight);
      }
   }

   /**
    * Adds a directed edge to the graph. Note that the vertices MUST have been created 
    * earlier by calling  {@code createVertexFor(T)}.
    *
    * @param source The source vertex of the edge.
    * @param destination The destination vertex of the edge.
    * @param weight The weight of the edge.
    */
   public void addDirectedEdge(Vertex<T> source, Vertex<T> destination, double weight) {
      // STUDENT TODO
      // Haetaan lähtösolmun reunuslista
      List<Edge<T>> edges = edgeList.get(source);
      // Varmistus että solmu on olemassa,
      if (edges != null) {
         // Luodaan uusi solmu ja lisätään se listaan
         Edge<T> edge = new Edge<>(source, destination, weight);
         edges.add(edge);
      }
   }

   /**
    * Gets the edges of the specified vertex. The vertex must be
    * already in the graph.
    *
    * @param source The vertex edges of which we wish to get.
    * @return Returns the edges of the vertex or an empty list if no edges from the source.
    */
   public List<Edge<T>> getEdges(Vertex<T> source) {
      // STUDENT TODO
      // Palautetaan lista tai tyhjä lista, mikäli solmua ei ole
      if (edgeList.containsKey(source)) {
         return edgeList.get(source);
      }
      return new ArrayList<>();
   }

   /**
    * Gets a vertex for the specified node (contents) in a vertex, if found.
    * If the vertex with the node value is not found, returns null.
    * Use `equals` to search for the element from the vertices.
    *
    * @param element The value of T that is in some Vertex in the graph.
    * @return The vertex containing the node, or null if no vertex contains the element.
    */
   public Vertex<T> getVertexFor(T element) {
      // STUDENT TODO

      for (Vertex<T> vertex : edgeList.keySet()) {
         if (vertex.getElement().equals(element)) {
            return vertex;
         }
      }
      return null;
   }

   /**
    * Does a breadth first search on the graph.
    *
    * Algorithm MUST call listener.pathChanged(vertex) whenever a 
    * new vertex is visited when doing the search.
    *
    * If target is null, search is done for the whole graph. Otherwise,
    * search MUST be stopped when the target vertex is found.
    *
    * Returns a list of vertices in the order they were met in the graph.
    * If graph is empty, returns an empty list.
    * 
    * @param from   The vertex where the search is started from.
    * @param target An optional ending vertex, null if not given.
    * @return Returns all the visited vertices traversed while doing BFS, in order
    *         they were found.
    */
   public List<Vertex<T>> breadthFirstSearch(Vertex<T> from, Vertex<T> target) {
      // STUDENT TODO
      List<Vertex<T>> visitedOrder = new ArrayList<>();
      Set<Vertex<T>> visitedSet = new HashSet<>();

      // Jos aloitus solmu puuttu palautetaan tyhjä lista
      if (from == null) return visitedOrder;

      //Käytetään omaa ArrayQueue-toteutusta
      ArrayQueue<Vertex<T>> queue = new ArrayQueue<>();

      // Aloitus
      visitedSet.add(from);
      queue.enqueue(from);

      // Esiehdon avulla silmukka pyörii vain jos jono ei ole tyhjä
      while (!queue.isEmpty()) {
         // Esiehdon ansiosta dequeue turvallista
         Vertex<T> current = queue.dequeue();

         visitedOrder.add(current);
         if (listener != null) listener.pathChanged(current);

         // Jos kohde löytyy katkaistaan etsintä
         if (target != null && current.equals(target)) {
            break;
         }

         // Käydään naapurit läpi
         List<Edge<T>> edges = getEdges(current);
         if (edges != null) {
            for (Edge<T> edge : edges) {
               Vertex<T> neighbor = edge.getDestination();
               if (!visitedSet.contains(neighbor)) {
                  visitedSet.add(neighbor);
                  queue.enqueue(neighbor);
               }
            }
         }
      }

      return visitedOrder;
   }

   /**
    * Does depth first search (DFS) of the graph starting from a vertex.
    *
    * Algorithm MUST call listener.pathChanged(vertex) whenever a
    * new vertex is visited when doing the search.
    *
    * If target is null, search is done for the whole graph. Otherwise,
    * search MUST be stopped when the target vertex is found.
    *
    * Returns a list of vertices in the order they were met in the graph.
    * If graph is empty, returns an empty list.
    *
    * @param from   The vertex where the search is started from.
    * @param target An optional ending vertex, null if not given.
    * @return Returns all the visited vertices traversed while doing DFS.
    */
   public List<Vertex<T>> depthFirstSearch(Vertex<T> from, Vertex<T> target) {
      // STUDENT TODO
      List<Vertex<T>> visitedOrder = new ArrayList<>();
      Set<Vertex<T>> visitedSet = new HashSet<>();

      if (from == null) return visitedOrder;

      // Käytetään omaa StackImplementation-toteutusta
      StackImplementation<Vertex<T>> stack = new StackImplementation<>();

      // Aloitus
      stack.push(from);
      visitedSet.add(from);
      visitedOrder.add(from);

      if (listener != null) listener.pathChanged(from);

      // Taas esiehdolla varmistetaan että silmukka pyörii vain jos pino ei ole tyhjä
      while (!stack.isEmpty()) {
         // Katsotaan pinon päällimäinen poistamatta sitä
         Vertex<T> current = stack.peek();

         if (target != null && current.equals(target)) {
            break;
         }

         List<Edge<T>> edges = getEdges(current);
         boolean foundUnvisited = false;

         if (edges != null) {
            for (Edge<T> edge : edges) {
               Vertex<T> neighbor = edge.getDestination();

               // Etsitään ensimmäinen vierailematon naapuri
               if (!visitedSet.contains(neighbor)) {
                  // Merkitään käydyksi ja lisätään pinoon heti
                  visitedSet.add(neighbor);
                  visitedOrder.add(neighbor);
                  if (listener != null) listener.pathChanged(neighbor);

                  stack.push(neighbor);

                  // Katkaistaan silmukka ja palataan while-silmukan alkuun käsittelemään uusi naapuri
                  foundUnvisited = true;
                  break;
               }
            }
         }
         // Jos ei enään vierailemattomia naapureita peruutetaan
         if (!foundUnvisited) {
            stack.pop();
         }
      }
      return visitedOrder;
   }
   
   // Dijkstra starts here.

   /**
    * The result of the Dijkstra's search.
    */
   public class DijkstraResult<E> {
      public boolean pathFound = false;
      public List<E> path;
      public int steps = 0;
      public double totalWeigth = 0.0;

      @Override
      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append(String.format("Dikstra result:\n- Path found: %s%n", (pathFound ? "yes" : "no")));
         if (pathFound) {
            builder.append(String.format("- steps: %d%n", steps));
            builder.append(String.format("- total edge weights: %.2f%n", totalWeigth));
            if (null != path) {
               builder.append(String.format("- path: %s", path.toString()));
            } else {
               builder.append("Path not found\n");
            }
         }
         return builder.toString();
      }
   }

   // Distance-apumetodi käsittelemään etäisyyksiä Dijksran-algorimissa. Metodi palauttaa solmun etäisyyden
   // paths-taulukosta, jos solmua ei ole löydetty etäisyys on Double.MAX_VALUE
   private double distance(Vertex<T> node, Map<Vertex<T>, Visit<T>> paths) {
      Visit<T> visit = paths.get(node);
      return (visit != null) ? visit.distance : Double.MAX_VALUE;
   }

   /**
    * Finds the shortest path from start to end using Dijkstra's algorithm.
    * 
    * NOTE: Always when you add an element to the path found,
    * you MUST call listener.pathChanged with the vertex of the element.
    *
    * Call listener.pathChanged from the route's source and destination vertices,
    * as you find them. Ask help if you do not know how to do it, or the
    * animation in the GUI does not look right.
    *
    * The return value contains information about the shortest path found.
    *
    * @param start The vertex to start from.
    * @param end The vertex to search the shortest path to.
    * @return An object containing information about the result of the search.
    */

   public DijkstraResult<T> shortestPathDijkstra(Vertex<T> start, Vertex<T> end) {
      DijkstraResult<T> result = new DijkstraResult<>();
      result.pathFound = false; // set this to true if a path is found in search
      result.path = new ArrayList<>();
      result.steps = 0;
      result.totalWeigth = 0.0;
      // This current path will then be returned with DijkstraResult as the path found
      ArrayList<T> currentPath = new ArrayList<>();
      // STUDENT: TODO implement the algorithm below

      if (start == null || end == null) return result;

      // Lasketaan lyhimmät polut kaikkiin saavutettaviin solmuihin
      Map<Vertex<T>, Visit<T>> paths = shortestPathsFrom(start);

      // Tarkistetaan löytyykö reittiä maalisolmuun
      if (paths.containsKey(end)) {
         // Rakennetaan reitti takaperin
         List<Edge<T>> edges = route(end, paths);

         // Validointi, eli reitti on olemassa jos lista ei ole tyhjä tai ollaan jo perillä
         if (!edges.isEmpty() || start.equals(end)) {
            result.pathFound = true;
            result.steps = edges.size();
            result.totalWeigth = distance(end, paths);

            // täytetään palautettava lista
            currentPath.add(start.getElement());
            for (Edge<T> edge : edges) {
               currentPath.add(edge.getDestination().getElement());
               // Visualisointi
               if (listener != null) {
                  listener.pathChanged(edge.getDestination());
               }
            }
         }
      }

      // STUDENT: implement the algorithm above under the previous comment.
      if (result.pathFound) {
         result.path = currentPath;
      }
      return result;
   }


   /**
    * Finds a route to a destination using paths already constructed.
    * Before calling this method, cal {@link shortestPathsFrom} to construct
    * the paths from the staring vertex of Dijkstra algorithm.
    *
    * A helper method for implementing the Dijkstra algorithm.
    * 
    * @param toDestination The destination vertex to find the route to.
    * @param paths The paths to search the destination.
    * @return Returns the vertices forming the route to the destination.
    */
   public List<Edge<T>> route(Vertex<T> toDestination, Map<Vertex<T>, Visit<T>> paths) {
      List<Edge<T>> path = new ArrayList<>();
      // STUDENT: TODO implement the algorithm below

      if (toDestination == null || !paths.containsKey(toDestination)) {
         return path;
      }

      Visit<T> visit = paths.get(toDestination);

      // Hyödynnetään omaa pino toteutusta reitin kääntämiseen
      StackImplementation<Edge<T>> stack = new StackImplementation<>();

      // Backtracking-silmukka jossa seurataan Visit-olioiden linkkejä taaksepäin. Silmukka lopetetaan kun
      // aloitus solmu saavitetaan
      while (visit != null && visit.type != Visit.Type.START) {
         if (visit.edge != null) {
            stack.push(visit.edge);
            Vertex<T> previous = visit.edge.getSource();
            visit = paths.get(previous);
         } else {
            break;
         }
      }

      // Tyhjennetään saatu pino listaan jolloin järjestys kääntyy
      while (!stack.isEmpty()) {
         path.add(stack.pop());
      }

      return path;
   }
   
   /**
    * Finds the shortest paths in the graph from the starting vertex.
    *
    * In doing Dijkstra, first call this method, then call {@link route}
    * with the paths collected using this method, to get the shortest path to the destination.
    *
    * @param start The starting vertex for the path searching.
    * @return Returns the visits from the starting vertex.
    * @see oy.tol.tira.graph.Graph#route(Vertex, Map)
    */
   public Map<Vertex<T>, Visit<T>> shortestPathsFrom(Vertex<T> start) {
      Map<Vertex<T>, Visit<T>> paths = new HashMap<>();

      // Määritetään visited
      Set<Vertex<T>> visited = new HashSet<>();

      // Alustus
      Visit<T> startVisit = new Visit<>();
      startVisit.type = Visit.Type.START;
      startVisit.distance = 0;
      paths.put(start, startVisit);

      // STUDENT: TODO implement the algorithm below using:
      // - a priority queue
      // - helper method distance you implement

      // Luodaan prioriteetti jono hyödyntäen distance-apumetodia vertailuun, pienimmän etäisyyden omaava solmu nousee
      // jonon kärkeen
      PriorityQueue<Vertex<T>> queue = new PriorityQueue<>(new Comparator<Vertex<T>>() {
         @Override
         public int compare(Vertex<T> v1, Vertex<T> v2) {
            return Double.compare(distance(v1, paths), distance(v2, paths));
         }
      });

      // Lisätään aloitussolmu jonoon
      queue.add(start);

      while (!queue.isEmpty()) {
         // Otetaan solmu jolla pienin etäisyys
         Vertex<T> current = queue.poll();

         // Jos solmju on jo käsitelty ohitetaan se
         if (visited.contains(current)) {
            continue;
         }
         visited.add(current);

         // Haetaan nykyinen etäisyys apumetodilla
         double currentDistance = distance(current, paths);

         // Käydään läpi kaikki naapurit
         List<Edge<T>> edges = getEdges(current);
         if (edges != null) {
            for (Edge<T> edge : edges) {
               Vertex<T> neighbor = edge.getDestination();

               // Lasketaan uusi etäisyys naapuriin tämän reunan kautta
               double newDistance = currentDistance + edge.getWeigth();

               // Jos uusi reitti on lyhempi kuin aiemmin tunnettu, päivitetään uusi eotäisyys ja talletaaan
               // uusi paras reitti Mappiin
               if (newDistance < distance(neighbor, paths)) {
                  Visit<T> newVisit = new Visit<>();
                  newVisit.type = Visit.Type.EDGE;
                  newVisit.edge = edge;
                  newVisit.distance = newDistance;

                  paths.put(neighbor, newVisit);

                  // Lisätään naapuri jonoon uudella prioriteetilla
                  queue.add(neighbor);
               }

            }
         }
      }
      return paths;
   }

   // STUDENTS: TODO: Uncomment the code below and use it as a sample on how
   // to interate over vertices and edges in one situation.
   // If you use some other name for your edge list than edgeList, then
   // rename that in the code below! Otherwise you will have compiler errors.
   /**
    * Provides a string representation of the graph, printing  out the vertices and edges.
    * <p>
    * Quite useful if you need to debug issues with algorithms. You can see is the graph
    * what it is supposed to be like.
    * <p>
    * Simple graph as a string would look like this:<br/>
    * <pre>
    * Created simple undirected graph where integers are vertice values:
    * [1] -> [ 2 ]
    * [2] -> [ 1, 3, 4, 5 ]
    * [3] -> [ 2, 4, 5 ]
    * [4] -> [ 2, 3, 5 ]
    * [5] -> [ 2, 3, 4 ]
    * </pre> 
    * @return The graph as a string.
    */
   @Override
   public String toString() {
      StringBuilder output = new StringBuilder();
      for (Map.Entry<Vertex<T>, List<Edge<T>>> entry : edgeList.entrySet()) {
          output.append("[");
          output.append(entry.getKey().toString());
          output.append("] -> [ ");
          int counter = 0;
          int count = entry.getValue().size();
          for (Edge<T> edge : entry.getValue()) {
             output.append(edge.getDestination().toString());
             if (counter < count - 1) {
                output.append(", ");
             }
             counter++;
          }
          output.append(" ]\n");
       }
      return output.toString();
   }
}
