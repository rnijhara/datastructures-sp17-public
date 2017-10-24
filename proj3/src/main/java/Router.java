import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest, 
     * where the longs are node IDs.
     */
    public static LinkedList<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                                double destlon, double destlat) {
        LinkedList<Long> path = new LinkedList<>();
        long start = g.closest(stlon, stlat);
        long end = g.closest(destlon, destlat);
        double totalDistance = g.distance(start, end);
        PathNode initial = new PathNode(null, start, end, 0, totalDistance);
        PriorityQueue<PathNode> minPQ = new PriorityQueue<>();
        HashSet<Long> marked = new HashSet<>();
        minPQ.add(initial);
        boolean arrived = false;

        while (!arrived) {
            PathNode next = minPQ.poll();
            marked.add(next.thisPoint);
            if (next.arrived()) {
                arrived = true;
                for (PathNode n = next; n != null; n = n.prev) {
                    path.addFirst(n.thisPoint);
                }
            } else {
                for (long neighbor : g.adjacent(next.thisPoint)) {
                    if (!marked.contains(neighbor)) {
                        double neighborDistFromStart = next.distFromStart
                                + g.distance(next.thisPoint, neighbor);
                        double neighborDistFromFinish = g.distance(neighbor, next.endPoint);
                        PathNode nextNode = new PathNode(next, neighbor,
                                next.endPoint, neighborDistFromStart, neighborDistFromFinish);
                        if (next.prev == null || neighbor != next.prev.thisPoint) {
                            minPQ.add(nextNode);
                        }
                    }
                }
            }
        }
        return path;
    }

    public static class PathNode implements Comparable<PathNode> {
        PathNode prev;
        double distFromStart;
        double distfromFinish;
        double priority;
        long thisPoint;
        long endPoint;

        public PathNode(PathNode p, long t, long f, double fromS, double fromF) {
            prev = p;
            thisPoint = t;
            endPoint = f;
            distFromStart = fromS;
            distfromFinish = fromF;
            priority = distFromStart + distfromFinish;
        }

        public boolean arrived() {
            return thisPoint == endPoint;
        }

        @Override
        public int compareTo(PathNode other) {
            if (priority == other.priority) {
                return 0;
            } else if (priority < other.priority) {
                return -1;
            } else {
                return 1;
            }
        }
    }

}
