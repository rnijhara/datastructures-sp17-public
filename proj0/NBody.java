public class NBody {

    /** Given a filename returns the universe radius */
    public static double readRadius(String filename) {
        In planetsIn = new In(filename);
        planetsIn.readInt();
        return planetsIn.readDouble();
    }

    /** Given a filename returns an array of Planets described
     *  by the file */
    public static Planet[] readPlanets(String filename) {
        In planetsIn = new In(filename);
        Planet[] planets = new Planet[planetsIn.readInt()];
        planetsIn.readDouble();
        int i = 0;
        while (i < planets.length) {
            double xP = planetsIn.readDouble();
            double yP = planetsIn.readDouble();
            double xV = planetsIn.readDouble();
            double yV = planetsIn.readDouble();
            double m = planetsIn.readDouble();
            String img = planetsIn.readString();
            planets[i] = new Planet(xP, yP, xV, yV, m, img);
            i += 1;
        }
        return planets;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double r = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        StdDraw.setScale(-r, r);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");
        for (Planet planet : planets) {
            planet.draw();
        }
        double elapsedTime = 0;
        int N = planets.length;
        while (elapsedTime < T) {
            double[] xForces = new double[N];
            double[] yForces = new double[N];
            for (int k=0; k < planets.length; k += 1) {
                xForces[k] = planets[k].calcNetForceExertedByX(planets);
                yForces[k] = planets[k].calcNetForceExertedByY(planets);
            }
            for (int k=0; k < planets.length; k += 1) {
                planets[k].update(dt, xForces[k], yForces[k]);
            }
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet planet : planets) {
                planet.draw();
            }
            StdDraw.show(10);
            elapsedTime += dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", r);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel, planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
