import edu.princeton.cs.algs4.Stopwatch;
import java.util.HashMap;

/**
 * Created by XWEN on 4/15/2017.
 */
public class QTreeRecursive {
    HashMap<Integer, Node> nodes = new HashMap<>();
    Node root = new Node(null, 0, 0, MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT,
            MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT,
            (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / 256);

    private double half(double a, double b) {
        return (a + b) / 2;
    }

    public QTreeRecursive() {
        Stopwatch c = new Stopwatch();
        root.imgFile = "img/root.png";
        root = fill(root);
        root.adjEast = -1;
        root.adjNorth = -1;
        root.adjSouth = -1;
        root.adjWest = -1;
        System.out.println("Constructor: " + c.elapsedTime());
    }

    class Node {

        Node parent;
        int id;
        int depth;
        double ulLon, ulLat, lrLon, lrLat, lonDPP;
        String imgFile;
        Node childNW, childNE, childSW, childSE;
        int adjNorth, adjSouth, adjEast, adjWest;

        Node(Node p, int i, int d, double ullon, double ullat, double lrlon,
             double lrlat, double ldpp) {
            parent = p;
            id = i;
            depth = d;
            ulLon = ullon;
            ulLat = ullat;
            lrLon = lrlon;
            lrLat = lrlat;
            lonDPP = ldpp;
            imgFile = "img/" + Integer.toString(id) + ".png";
            if (depth > 0 && p != null) {
                if (id % 10 == 1) {
                    adjEast = id + 1;
                    adjSouth = id + 2;
                    if (p.adjNorth != -1) {
                        adjNorth = p.adjNorth * 10 + 3;
                    } else {
                        adjNorth = -1;
                    }
                    if (p.adjWest != -1) {
                        adjWest = p.adjEast * 10 + 2;
                    } else {
                        adjWest = -1;
                    }
                } else if (id % 10 == 2) {
                    adjWest = id - 1;
                    adjSouth = id + 2;
                    if (p.adjNorth != -1) {
                        adjNorth = p.adjNorth * 10 + 4;
                    } else {
                        adjNorth = -1;
                    }
                    if (p.adjEast != -1) {
                        adjEast = p.adjEast * 10 + 1;
                    } else {
                        adjEast = -1;
                    }
                } else if (id % 10 == 3) {
                    adjNorth = id - 2;
                    adjEast = id + 1;
                    if (p.adjWest != -1) {
                        adjWest = p.adjWest * 10 + 4;
                    } else {
                        adjWest = -1;
                    }
                    if (p.adjSouth != -1) {
                        adjSouth = p.adjSouth * 10 + 2;
                    } else {
                        adjSouth = -1;
                    }
                } else if (id % 10 == 4) {
                    adjNorth = id - 2;
                    adjWest = id - 1;
                    if (p.adjEast != -1) {
                        adjEast = p.adjEast * 10 + 3;
                    } else {
                        adjEast = -1;
                    }
                    if (p.adjSouth != -1) {
                        adjSouth = p.adjSouth * 10 + 2;
                    } else {
                        adjSouth = -1;
                    }
                }
            }
        }
    }

    private Node fill(Node n) {
        nodes.put(n.id, n);
        if (n.depth < 7) {
            n.childNW = fill(new Node(n, n.id * 10 + 1, n.depth + 1, n.ulLon, n.ulLat,
                    half(n.ulLon, n.lrLon), half(n.ulLat, n.lrLat), n.lonDPP / 2));

            n.childNE = fill(new Node(n, n.id * 10 + 2, n.depth + 1, half(n.ulLon, n.lrLon),
                    n.ulLat, n.lrLon, half(n.ulLat, n.lrLat), n.lonDPP / 2));

            n.childSW = fill(new Node(n, n.id * 10 + 3, n.depth + 1, n.ulLon, half(n.ulLat, n.lrLat),
                    half(n.ulLon, n.lrLon), n.lrLat, n.lonDPP / 2));

            n.childSE = fill(new Node(n, n.id * 10 + 4, n.depth + 1, half(n.ulLon, n.lrLon),
                    half(n.ulLat, n.lrLat), n.lrLon, n.lrLat, n.lonDPP / 2));
        }
        return n;
    }

    public static void main(String[] args) {
        Stopwatch qtr = new Stopwatch();
        QTreeRecursive q = new QTreeRecursive();
        System.out.println("Recursive construction: " + qtr.elapsedTime());
        Node r = q.root;
        for (int i = 0; i < 3; i++) {
            r = r.childNE;
        }
        for (int i = 0; i < 4; i++) {
            r = r.childSW;
        }

        System.out.println(r.id);
        System.out.println(r.adjNorth);
        System.out.println(r.adjSouth);
        System.out.println(r.adjEast);
        System.out.println(r.adjWest);
    }
}
