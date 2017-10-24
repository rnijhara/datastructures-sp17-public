public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    /** Planet constructor */
    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    /** Makes a copy of Planet p */
    public Planet(Planet p) {
        this(p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
    }

    /** Calculates distance between target Planet and Planet p2 */
    public double calcDistance(Planet p2) {
        double dX = p2.xxPos - xxPos;
        double dY = p2.yyPos - yyPos;
        return Math.sqrt(dX * dX + dY * dY);
    }

    /** Calculates force exerted by Planet p2 onto target Planet */
    public double calcForceExertedBy(Planet p2) {
        double G = 6.67e-11;
        double r = calcDistance(p2);
        return G * mass * p2.mass / (r * r);
    }

    /** Calculates force in X direction exerted by Planet p2
     *  onto target Planet */
    public double calcForceExertedByX(Planet p2) {
        double xComp = (p2.xxPos - xxPos) / calcDistance(p2);
        return calcForceExertedBy(p2) * xComp;
    }

    /** Calculates force in Y direction exerted by Planet p2
     *  onto target Planet */
    public double calcForceExertedByY(Planet p2) {
        double yComp = (p2.yyPos - yyPos) / calcDistance(p2);
        return calcForceExertedBy(p2) * yComp;
    }

    /** Calculates net force in X direction exerted by all
     *  planets non-identical to target Planet in array
     *  Planet[] planets onto target Planet */
    public double calcNetForceExertedByX(Planet[] planets) {
        double netForceX = 0;
        for (Planet planet : planets) {
            if (!equals(planet)) {
                netForceX += calcForceExertedByX(planet);
            }
        }
        return netForceX;
    }

    /** Calculates net force in Y direction exerted by all
     *  planets non-identical to target Planet in array
     *  Planet[] planets onto target Planet */
    public double calcNetForceExertedByY(Planet[] planets) {
        double netForceY = 0;
        for (Planet planet : planets) {
            if (!equals(planet)) {
                netForceY += calcForceExertedByY(planet);
            }
        }
        return netForceY;
    }

    /** Updates target Planet's position given a small dt
     *  and X and Y net forces */
    public void update(double dt, double fX, double fY) {
        double aX = fX / mass;
        double aY = fY / mass;
        xxVel += aX * dt;
        yyVel += aY * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }

    /**Draws Planet img at Planet's location */
    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }
}