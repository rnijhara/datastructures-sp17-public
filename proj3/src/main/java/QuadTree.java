import java.util.ArrayDeque;

/**
 * Created by XWEN on 4/12/2017.
 */
public class QuadTree {
    Node root;
    ArrayDeque<Node> q = new ArrayDeque<>();

    private double half(double a, double b) {
        return (a + b) / 2;
    }

    public QuadTree() {
        Node r = new Node(null, 0, 0, MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT,
                MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT,
                (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / 256);
        root = r;
        r.imgFile = "img/root.png";
        r.adjEast = null;
        r.adjNorth = null;
        r.adjSouth = null;
        r.adjWest = null;
        q.addLast(r);
        while (!q.isEmpty()) {
            populate(q.removeFirst());
        }
    }

    class Node {

        Node parent;
        int id;
        int depth;
        double ulLon, ulLat, lrLon, lrLat, lonDPP;
        String imgFile;
        Node childNW, childNE, childSW, childSE;
        Node adjNorth, adjSouth, adjEast, adjWest;

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
        }

        public void findNeighbors() {
            if (id % 10 == 1) {
                Node p = parent;
                adjSouth = parent.childSW;
                adjEast = parent.childNE;
                if (p.adjNorth != null) {
                    adjNorth = parent.adjNorth.childSW;
                } else {
                    adjNorth = null;
                }
                if (p.adjWest != null) {
                    adjWest = parent.adjWest.childNE;
                } else {
                    adjWest = null;
                }
            } else if (id % 10 == 2) {
                Node p = parent;
                adjSouth = parent.childSE;
                adjWest = parent.childNW;
                if (p.adjNorth != null) {
                    adjNorth = parent.adjNorth.childSE;
                } else {
                    adjNorth = null;
                }
                if (p.adjEast != null) {
                    adjEast = parent.adjEast.childNW;
                } else {
                    adjEast = null;
                }
            } else if (id % 10 == 3) {
                Node p = parent;
                adjNorth = parent.childNW;
                adjEast = parent.childSE;
                if (p.adjSouth != null) {
                    adjSouth = parent.adjSouth.childNW;
                } else {
                    adjSouth = null;
                }
                if (p.adjWest != null) {
                    adjWest = parent.adjWest.childSE;
                } else {
                    adjWest = null;
                }
            } else if (id % 10 == 4) {
                Node p = parent;
                adjNorth = parent.childNE;
                adjWest = parent.childSW;
                if (p.adjSouth != null) {
                    adjSouth = parent.adjSouth.childNE;
                } else {
                    adjSouth = null;
                }
                if (p.adjEast != null) {
                    adjEast = parent.adjEast.childSW;
                } else {
                    adjEast = null;
                }
            }
        }

        boolean inResolution(double maxDPP) {
            return (lonDPP <= maxDPP) || (depth == 7);
        }

        boolean inTile(double lon, double lat) {
            return (ulLon < lon) && (lrLon > lon) && (ulLat > lat) && (lrLat < lat);
        }

        Node nextQuad(double lon, double lat) {
            if (childNW.inTile(lon, lat)) {
                return childNW;
            } else if (childNE.inTile(lon, lat)) {
                return childNE;
            } else if (childSE.inTile(lon, lat)) {
                return childSE;
            } else if (childSW.inTile(lon, lat)) {
                return childSW;
            }
            return this;
        }
    }

    private void populate(Node n) {
        if (n.depth != 0) {
            n.findNeighbors();
        }

        if (n.depth < 7) {
            Node nw = new Node(n, n.id * 10 + 1, n.depth + 1, n.ulLon, n.ulLat,
                    half(n.ulLon, n.lrLon), half(n.ulLat, n.lrLat), n.lonDPP / 2);
            n.childNW = nw;

            Node ne = new Node(n, n.id * 10 + 2, n.depth + 1, half(n.ulLon, n.lrLon),
                    n.ulLat, n.lrLon, half(n.ulLat, n.lrLat), n.lonDPP / 2);
            n.childNE = ne;

            Node sw = new Node(n, n.id * 10 + 3, n.depth + 1, n.ulLon, half(n.ulLat, n.lrLat),
                    half(n.ulLon, n.lrLon), n.lrLat, n.lonDPP / 2);
            n.childSW = sw;

            Node se = new Node(n, n.id * 10 + 4, n.depth + 1, half(n.ulLon, n.lrLon),
                    half(n.ulLat, n.lrLat), n.lrLon, n.lrLat, n.lonDPP / 2);
            n.childSE = se;

            q.addLast(nw);
            q.addLast(ne);
            q.addLast(sw);
            q.addLast(se);
        }
    }

    public Node zoom(double dppQuery, double ullonQuery, double
            ullatQuery, double lrlonQuery, double lrlatQuery) {
        Node zoomed = root;
        double lonMid = half(lrlonQuery, ullonQuery);
        double latMid = half(lrlatQuery, ullatQuery);
        while (!zoomed.inResolution(dppQuery) || !zoomed.inTile(lonMid, latMid)) {
            zoomed = zoomed.nextQuad(lonMid, latMid);
        }
        return zoomed;
    }

}
