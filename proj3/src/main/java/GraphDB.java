import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    HashMap<Long, Vertex> vertexRefs = new HashMap<>();
    HashSet<Long> cleanThese = new HashSet<>();
    Way nextWay = null;
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        for (long v : cleanThese) {
            vertexRefs.remove(v);
        }
        cleanThese.clear();
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        return vertexRefs.keySet();
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        Vertex vertex = vertexRefs.get(v);
        return vertex.adj.keySet();
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        return distHelper(lon(w) - lon(v), lat(w) - lat(v));
    }

    private double distHelper(double a, double b) {
        return Math.sqrt((a * a) + (b * b));
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        double min = Double.POSITIVE_INFINITY;
        long closest = -1;
        for (long v : vertices()) {
            if (distHelper(lon - lon(v), lat - lat(v)) < min) {
                closest = v;
                min = distHelper(lon(closest) - lon, lat(closest) - lat);
            }
        }
        return closest;
    }

    /** Longitude of vertex v. */
    double lon(long v) {
        return vertexRefs.get(v).lon;
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return vertexRefs.get(v).lat;
    }

    void addVertex(long id, double lon, double lat) {
        Vertex v = new Vertex(id, lon, lat);
        vertexRefs.put(id, v);
        cleanThese.add(id);
    }

    void addEdge(long v, long w) {
        Vertex a = vertexRefs.get(v);
        Vertex b = vertexRefs.get(w);
        a.connect(b);
        b.connect(a);
        cleanThese.remove(a.id);
        cleanThese.remove(b.id);
    }

    void processNextWay() {
        if (nextWay != null) {
            while (nextWay.toConnect.size() > 1) {
                long first = nextWay.toConnect.removeFirst();
                long second = nextWay.toConnect.peekFirst();
                addEdge(first, second);
            }
        }
    }

    void queueWay() {
        nextWay = new Way();
    }

    public class Vertex {
        long id;
        double lon, lat;
        HashMap<Long, Vertex> adj;

        public Vertex(long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            adj = new HashMap<>();
        }

        void connect(Vertex o) {
            adj.put(o.id, o);
        }
    }

    public class Way {
        ArrayDeque<Long> toConnect;
        boolean valid;

        public Way() {
            toConnect = new ArrayDeque<>();
            valid = false;
        }

        public void validate() {
            valid = true;
        }

        public void addNode(Long id) {
            toConnect.addLast(id);
        }

        public boolean isValid() {
            return valid;
        }
    }
}
