## Project 3: Bear Maps, version 2.0 ##

Table of Contents
-----------------

-   [Introduction](http://datastructur.es/sp17/materials/proj/proj3/proj3.html#introduction)
-   [Overview](http://datastructur.es/sp17/materials/proj/proj3/proj3.html#overview)
-   [Getting the Skeleton
    Files](http://datastructur.es/sp17/materials/proj/proj3/proj3.html#getting-the-skeleton-files)
-   [Rastering](http://datastructur.es/sp17/materials/proj/proj3/proj3.html#map-rastering)
-   [Routing](http://datastructur.es/sp17/materials/proj/proj3/proj3.html#routing)
-   [Autocompletion and
    Search](http://datastructur.es/sp17/materials/proj/proj3/proj3.html#autocompletion-and-search)
-   [Acknowledgements](http://datastructur.es/sp17/materials/proj/proj3/proj3.html#acknowledgements)

Introduction
------------

This project was originally created by former TA Alan Yao (now at
AirBNB). It is a web mapping application inspired by Alan's time on the
Google Maps team and the [OpenStreetMap](http://www.openstreetmap.org/)
project, from which the tile images and map feature data was downloaded.
You are working with real-world mapping data here that is freely
available - after you've finished this project, you can even extend your
code to support a wider range of features. You will be writing the back
end - the web server that powers the API that the front end makes
requests to. The [project 3 video
playlist](https://www.youtube.com/playlist?list=PL8FaHk7qbOD7rrjWbRN-2W0EpxXK5CNW-)
starts with an introductory video that should help you get started. The
slides used in the project 3 video playlist are [available
here](https://docs.google.com/presentation/d/1lZ6Sm9DExLWZuYKUpmpkPPzW_gyWJfyhqaoPWXbnHts/edit?usp=sharing).

This project is a **solo project**. You should not work with a partner.
One of our biggest goals for 61B is to develop your independence as a
programmer, and this project is a great milestone for you to complete on
your own before you go on to bigger and better things.

The point breakdown for this 75 point project are as follows: 50 points
for Part I. 25 points for Parts II and III (exact distribution TBA). You
can also earn 3 extra credit points for submitting to the extra credit
autograder by Sunday 4/16/2017, which will cover only Part I. 10 gold
points can be earned for completing Autocomplete.

By the end of this project, with some extra work, you can even host your
application as a web app. More details will be provided at the end of
this spec.

There is a reference version of the project solution running on Amazon
AWS here. While we can't guarantee that the performance will be good,
you'll likely get response times that are only slightly slower than your
locally hosted version. Why? Because we're using
[Quilt](http://quilt.io/) to deploy the solution in such a way that all
your requests will be load balanced across \~10 separate instances of
BearMaps. That being said, they're still running on free instances, so
it might slow up under high load. If you are experiencing a lot of
latency, shrink your window. Your project cannot perform poorly though,
and should have sub-0.5s response times, especially since you are
hosting locally.

#### Meta Advice

*This spec is not meant to be complete.* Many design decisions are left
to you, although with suggestions given. Many implementation details are
not given; you are expected to read the entirety of the skeleton (which
is well-commented or self-explanatory) and the javadoc to determine how
to proceed. You will especially want to read all the constants defined.

However, the spec is written in a way so that you can proceed linearly
down - that is, while each feature is partially dependent on the
previous one, your design decisions, as long as they are generally
reasonable, should not obstruct how you think about implementing the
following parts. You **are required to read the entire spec section
before asking questions**. If your question is answered in the spec, we
will only direct you to the spec.

Getting the Skeleton Files
--------------------------

For this project we **very strongly** recommend using IntelliJ. If
IntelliJ doesn't work on your computer, or is too slow, consider using
IntelliJ on the lab machines. If you insist, you can also use the
command-line / terminal on your personal machine as further described in
`Addendum for Terminal users`.

Pull the skeleton using the command `git pull skeleton master`. Then,
please download [this zip
file](https://people.eecs.berkeley.edu/~jhug/p3imgs.zip); it is the
image tile dataset. Unzip it into your proj3/ folder such that there is
an img/ directory, with all the png files in it. There are around 50,000
files in this folder, so it might take a bit of time to unzip. Don't use
the **-f** flag with GitHub for this project unless you're sure that
you're not adding these .png files to your rebo as a result. If you
don't know what we're talking about, the basic idea is that you
shouldn't use **-f** unless you have a really good reason.

Project 3 uses [Apache Maven](https://maven.apache.org/) as its build
system; it integrates with IntelliJ. You will want to create a new
IntelliJ project for project 3. In IntelliJ, go to New -\> Project from
Existing Sources. Then:

1.  Select your proj3 folder, press next, and make sure to select
    "Import project from external model" and select Maven. Press next.
2.  At the Import Project window, check: "Import Maven projects
    automatically".
3.  Press next until the end.
4.  It is possible that IntelliJ will not "mark" your folders correctly:
    Make sure to mark, if not done so already, the`src/main/java`
    directory as a sources root, the `src/static` directory as a sources
    root, and the `src/test/java` directory as a test sources root. To
    do this, right click on the folder in the explorer window (the tab
    on the left), and towards the bottom of the options you'll see "Mark
    Directory As".
5.  **Do not** add the course javalib to your IntelliJ library. You will
    not need it and it will cause conflicts. This also means that **you
    cannot use any libraries outside the Java standard library and the
    ones already included in the project**. Doing so will immediately
    cause a compilation error on the autograder. Notably, we are not
    accommodating usage of the Princeton libraries as they are
    unnecessary.

Build the project, run `MapServer.java` and navigate your browser
(Chrome preferred; errors in other browsers will not be supported) to
`localhost:4567`. This should load up `map.html`; by default, there
should be a blank map. You can also run `MapServer.java` and then open
up `src/static/page/map.html` manually by right clicking and going to
Open In Browser in IntelliJ. Once you've done this, you should see
something like the window below, where the front end is patiently
waiting on your back end (not yet implementing) to provide image data.

![startup](./proj3/startup.png)

If you get a 404 error, make sure you have marked your folders as
described in step 4 above.

**Absolutely make sure to end your instance of `MapServer` before
re-running or starting another up.** Not doing so will cause a
`java.net.BindException: Address already in use`. If you end up getting
into a situation where you computer insists the address is already in
use, either figure out how to kill all java processes running on your
machine (using Stack Overflow or similar) or just reboot your computer.

#### Addendum for Terminal users

If you do want to use it through the command line here are some basic
instructions: Windows users: Follow the instructions
[here](https://maven.apache.org/guides/getting-started/windows-prerequisites.html),
making sure to adjust them to your machine which should already have
JDK8 installed. Use command prompt, not git bash. Mac users:
`brew install maven` Ubuntu users: `sudo apt-get install maven`.

You can then use the `mvn compile` and
`mvn exec:java -Dexec.mainClass="MapServer"` targets to run `MapServer`,
after patching your pom.xml to include `src/static` as a sources root.
Do so by renaming `pom_alternate.xml` to `pom.xml`. You can also run the
tests with `mvn test`. Choosing to work through terminal may slow down
your development cycle considerably as you will not have access to the
debugger.

Overview
--------

### Application Structure

Your job for this project is to implement the back end of a web server.
To use your program, a user will open an html file in their web browser
that displays a map of the city of Berkeley, and the interface will
support scrolling, zooming, and route finding (similar to Google Maps).
We've provided all of the ["front
end"](https://en.wikipedia.org/wiki/Front_and_back_ends) code. Your code
will be the "back end" which does all the hard work of figuring out what
data to display in the web browser.

At its heart, the project works like this: The user's web browser
provides a URL to your Java program, and your Java program will take
this URL and generate the appropriate output, which will displayed in
the browser. For example, suppose your program were running at
bloopblorpmaps.com, the URL might be:

    bloopblorpmaps.com/raster&ullat=37.89&ullon=-122.29&lrlat=37.83&lrlon=-122.22&w=700&h=300

The job of the back end server (i.e. your code) is take this URL and
generate an image corresponding to a map of the region specified inside
the URL (more on how regions are specified later). This image will be
passed back to the front end for display in the web browser.

We'll not only be providing the front end (in the form of .html and
javascript files), but we'll also provide a great deal of starter code
(in the form of .java files) for the back end. This starter code will
handle all URL parsing and communication for the front end. This code
uses the [Java
Spark](http://sparkjava.com/documentation.html#getting-started) as the
server framework. You don't need to worry about the internals of this as
we are providing the skeleton code to handle the endpoints.

### Assignment Overview

You will implement three required classes for this project, in addition
to any helper classes that you decide to create. These three classes are
`Rasterer`, `GraphDB`, and `Router`. They are described very briefly
below. More verbose descriptions follow.

The `Rasterer` class will take as input an upper left latitude and
longitude, a lower right latitude and longitude, a window width, and a
window height. Using these six numbers, it will produce a 2D array of
filenames corresponding to the files to be rendered. This will be the
most confusing and time consuming part of the project.

The `GraphDB` class will read in the Open Street Map dataset and store
it as a graph. Each node in the graph will represent a single
intersection, and each edge will represent a road. How you store your
graph is up to you. This will be the strangest part of the project.

The `Router` class will take as input a `GraphDB`, a starting latitude
and longitude, and a destination latitude and longitude, and it will
produce a list of nodes (i.e. intersections) that you get from the start
point to the end point. This part will be similar to the 8Puzzle
assignment, since you'll be implementing A\* again, but now with an
explicit graph object (that you build in `GraphDB`).

Warning: Unlike prior assignments in your CS classes, we'll be working
with somewhat messy real world data. This is going to be frustrating at
times, but it's an important skill and something that we think will
serve you well moving forwards. If you're someone who thinks of yourself
as a weaker programmer, make sure to start ASAP.

The biggest challenges in this assignment will be understanding what
rastering is supposed to do (it's tricky!), as well as figuring out how
to parse XML files for `GraphDB`.

Map Rastering (Part I Overview)
-------------------------------

Rastering is the job of converting information into a pixel-by-pixel
image. In the `Raster` class you will take a user's desired viewing
rectangle and generate an image for them.

The user's desired input will be provided to you as a
`Map<String, Double> params`, and the main goal of your rastering code
will be to create a `String[][]` that corresponds to the files that
should be displayed in response to this query.

As a specific example (given as "testTwelveImages.html" in the skeleton
files), the user might specify that they want the following information:

    {lrlon=-122.2104604264636, ullon=-122.30410170759153, w=1085.0, h=566.0, ullat=37.870213571328854, lrlat=37.8318576119893}

This means that the user wants the area of earth delineated by the
rectangle between longitudes -122.2104604264636 and -122.30410170759153
and latitudes 37.870213571328854 and 37.8318576119893, and that they'd
like them displayed in a window roughly 1085 x 566 pixels in size (width
x height). We call the user's desired display location on earth the
**query box**.

To display the requested information, you need street map pictures,
which we have provided in the p3imgs.zip file (not posted yet, but if
you want to get a sneak peak, you can download it
[here](https://people.eecs.berkeley.edu/~jhug/p3imgs.zip)) All of the
images provided are 256 x 256 pixels. Each image is at various levels of
zoom. For example, the files `1.png`, `2.png`, `3.png` and `4.png` are
at the lowest resoution (i.e. lowest level of zoom). Together they
constitute a very zoomed out picture of Berkeley, with 1 in the
northwest, 2 in the northeast, 3 in the southwest, and 4 in the
southeast.

For every file, there are four higher resolution images of the same
area. For example `11.png`, `12.png`, `13.png` and `14.png`
corresponding to the northwest, northeast, southwest, and southeast
corners of `1.png`.

The job of your rasterer class is decide, given a user's query, which
files to serve up. For the example above, your code should return the
following 2D array of strings:

    [[img/13.png, img/14.png, img/23.png, img/24.png],
    [img/31.png, img/32.png, img/41.png, img/42.png],
    [img/33.png, img/34.png, img/43.png, img/44.png]]

This means that the browser should display `13.png` in the top left,
then `14.png` to the right of `13`, and so forth. Thus our "rastered"
image is actually a combination of 12 images arranged in 3 rows of 4
images each.

Our `MapServer` code will take your 2D array of strings and display the
appropriate image in the browser. If you're curious how this works, see
`writeImagesToOutputStream`.

Since each image is 256 x 256 pixels, the overall image given above will
be 1024 x 768 pixels. There are many combinations of images that cover
the query box. For example, we could instead use higher resolution
images of the exact same areas of Berkeley. Thus, instead of using
`13.png`, we could have used `131.png`, `132.png`, `133.png`, `134.png`
while also substituting `14.png` by `141.png`, `142.png`, etc. The
result would be total of 48 images arranged in 6 rows of 8 images each
(make sure this makes sense!). This would result in a 2048 x 1536 pixel
image.

You might be tempted to say that a 2048 x 1536 image is more
appropriate, since the user requested 1085 x 556 since 1024 x 768 is too
small in the width direction. However, that's not the way that we decide
which images to use.

Instead, to rigorously determine which images to use, we will define the
**longitudinal distance per pixel (LonDPP)** as follows: Given a query
box or image, the LonDPP of that box or image is the (lower right
longitude - upper left longitude) / (width of the image/box in pixels).
For example, for the query box in the example in this section, the
LonDPP is (-122.2104604264636 + 122.30410170759153) / (1085) or
0.00008630532 units of longitude per pixel. At Berkeley's latitude, this
is very roughly 25 feet of distance per pixel.

The images that you return as a `String[][]` when rastering must be
those that:

-   Include any region of the query box. (an earlier version of the spec
    said this in a more confusing way)
-   Have the greatest LonDPP that is less than or equal to the LonDPP of
    the query box (as zoomed out as possible).

For `1.png`, the LonDPP is `0.000171661376953125` which is greater than
the LonDPP of the query box, and is thus unusable. This makes intuitive
sense: If the user wants an image which covers roughly 25 feet per
pixel, we shouldn't use an image that covers \~50 feet per pixel because
the resolution is too poor. For `13.png`, the LonDPP is
0.0000858306884765625, which is better (i.e. smaller) than the user
requested, so this is fine to use. For `131.png`, the LonDPP is
0.00004291534423828125. This is also smaller than the desired LonDPP,
but using it is overkill since `13.png` is already good enough. In my
head, I find it useful to think of LonDPP as "blurriness", i.e. `1.png`
is the blurriest (most zoomed out/highest LonDPP).

As an example of an intersection query, consider the image below, which
summarizes key parameter names and concepts. In this example search, the
query box intersects 9 of the 16 tiles at depth 2.

![rastering\_example](./proj3/rastering_example.png)

For a demo of how the provided images are arranged, see this
[FileDisplayDemo](http://datastructur.es/sp17/materials/proj/proj3/FileDisplayDemo.html).
Try typing in a filename, and it will show what region of the map this
file corresponds to, as well as the exact coordinates of its corners, in
addition to the distance per pixel.

One natural question is: Why not use the best quality images available
(i.e. smallest LonDPP, e.g. 7 character filenames like `4443414.png`)?
The answer is that the front end doesn't do any rescaling, so if we used
ultra low LonDPPs for all queries, we'd end up with absolutely gigantic
images displayed in our web browser.

Map Rastering (API)
-------------------

In Java, you will implement the Rasterer by filling in a single method:

    public Map<String, Object> getMapRaster(Map<String, Double> params)

The given params are described in the skeleton code. An example is
provided in the "Map Rastering (Overview)" section above, and you can
always try opening up one of our provided html files and simply printing
out `params` when this method is called to see what you're given.

Your code should return a `Map<String, Object>` as described in the
Javadocs (the /\*\* \*/ comments in the skeleton code). We do this as a
way to hack around the fact that Java methods can only return one value.
This map includes not just the two dimensional array of strings, but
also a variety of other useful information that will be used by your web
browser to display the image nicely (e.g. "raster\_width" and
"raster\_height"). See the Javadocs for more details.

### Suggested Approach for Rastering: QuadTree

A *quadtree* is a tree data structure typically used to represent
spatial data, described during [lecture
25](https://docs.google.com/presentation/d/1ifkiC-l0DfQRXEHFfQpg_AcZkaUyj9CCEUKOYPuyBZ0/edit#slide=id.g11f3cf3f77_0_195).
A node in a Quadtree is a square in the plane; for this project, each
square node will be called a tile interchangeably, and is defined by its
upper left and lower right points. Unlike a Binary Tree, a node has four
children; each child is a subdivided fourth of its parent, as shown
below.

![Quadtree](./proj3/qt.png)

As discussed above, you are provided map data in the `img` directory as
a large set of 256x256 png image files, which we'll call tiles. The
filename determines the relationship between one tile and another, as
shown above. The easiest way to complete the rastering part of this
project is to build a quad-tree where each quad-tree node corresponds to
an image tile. `11.png,  12.png, 13.png, 14.png` are the four quadrant
subdivisions of `1.png` and so on. The longitudes and latitudes of the
root node, which is to be subdivided, are given to you as constants
`ROOT_ULLAT, ROOT_ULLON, ROOT_LRLAT, ROOT_LRLON`.

For example, the upper left child of the root, represented by `1.png`,
shares an upper left longitude and latitude with the root, but has a
lower right longitude and latitude that is at the center of the root
tile, and so on - the structure is defined recursively. If a tile has no
children, for example `4444444.png`, there are no valid files
`44444441.png` and so on.

This helps construct the map since all tiles are the same resolution;
you might think of traversing to a child of a node as "zooming in" on
that quadrant. Each level of the quadtree has its own LonDPP, where the
root has a relatively large LonDPP, and as you go deeper the LonDPP gets
smaller (since the images are more zoomed in and thus you get less
distance per pixel).

The problem of finding the correct images for a given query is thus
equivalent to going to the shallowest level whose LonDPP is less than or
equal to the query box, and finding all images at that level that
intersect the query box.

This will all be pretty confusing at first. We highly recommend playing
around with the
[FileDisplayDemo](http://datastructur.es/sp17/materials/proj/proj3/FileDisplayDemo.html)
to gain more intuition.

### Extra Details and Corner Cases

When the client makes a call to `/raster` with the required parameters,
the request handler will validate that all the required parameters are
present (as declared in `REQUIRED_RASTER_REQUEST_PARAMS`. Then, in the
Map `params`, those parameters are keys that you will be able to get the
value of: for example, if I wanted to know the upper left point's
longitude of the query rectangle, I could call `params.get("ullon")`.

You may end up with an image, for some queries, that ends up not filling
the query box and that is okay - this arises when your latitude and
longitude query do not intersect enough tiles to fit the query box. You
can imagine this happening with a query very near the edge (in which
case you just don't collect tiles that go off the edge); a query window
that is very large, larger than the entire dataset itself; or a query
window in lat and lon that is not proportional to its size in pixels.
For example, if you are extremely zoomed in, you have no choice but to
collect the leaf tiles and cannot traverse deeper.

You can also imagine that the user might drag the query box to a
location that is completely outside of the root longitude/latitudes. In
this case, there is nothing to raster, `raster_ul_lon`, `raster_ul_lat`,
etc. are arbitrary, and you should set `query_success: false`. If the
server receives a query box that doesn't make any sense, eg.
`ullon, ullat` is located to the right of `lrlon, lrlat`, you should
also ensure `query_success` is set to false.

#### Runtime

Your constructor should take time linear in the number of tiles given.

You may not iterate through / explore all tiles to search for
intersections. Suppose there are `k` tiles intersecting a query box, and
`n` tiles total. Your entire query must run in `O(k log k + log n)` time
(theoretically, on a tree of unbounded depth and size), including
constructing the image. This can be broken up into two parts: `O(log n)`
time to traverse the quadtree to where we begin collecting, and
`O(k log k)` time to collect and arrange the intersected tiles. This
should correspond to the standard quadtree traversal. It can be done
faster than this, but remember that big-O is an upper bound.

#### Addendum

You will get latitude and longitude mixed up at least once. Make sure to
check for that!

Routing & Location Data (Part II) {#routing-amp-location-data-part-ii}
---------------------------------

In this part of the project, you'll use a real world dataset combined
with an industrial strength dataset parser to construct a graph. This is
similar to tasks you'll find yourself doing in the real world, where you
are given a specific tool and a dataset to use, and you have to figure
out how they go together. It'll feel shaky at first, but once you
understand what's going on it won't be so bad.

Routing and location data is provided to you in the `berkeley.osm` file.
This is a subset of the full planet's routing and location data, pulled
from [here](http://download.bbbike.org/osm/). The data is presented in
the [OSM XML file format](http://wiki.openstreetmap.org/wiki/OSM_XML). \
 XML is a markup language for encoding data in a document. Open up the
`berkeley.osm` file for an example of how it looks. Each element looks
like an HTML tag, but for the OSM XML format, the content enclosed is
(optionally), more elements. Each element has attributes, which give
information about that element, and sub-elements, which can give
additional information and whose name tell you what kind of information
is given.

The first step of this part of the project is to build a graph
representation of the contents of `berkeley.osm`. We have chosen to use
a SAX parser, which is an "event-driven online algorithm for parsing XML
documents". It works by iterating through the elements of the XML file.
At the beginning and end of each element, it calls the `startElement`
and `endElement` callback methods with the appropriate parameters.

Your job will be to override the `startElement` and `endElement` methods
so that when the SAX parser has completed, you have built a graph.
Understanding how the SAX parser works is going to be tricky.

You will find the Javadocs for
[GraphDB](http://datastructur.es/sp17/materials/proj/proj3/doc/GraphDB.html)
and
[GraphBuildingHandler](http://datastructur.es/sp17/materials/proj/proj3/doc/GraphBuildingHandler.html)
helpful, as well as the example code in `GraphBuildingHandler.java`,
which gives a basic parsing skeleton example. There is an example of a
completed handler in the `src/main/java/example` folder called
`CSCourseDBHandler.java` that you might find helpful to look at.

Read through the OSM wiki documentation on the various relevant
elements: the [idea of a tag](http://wiki.openstreetmap.org/wiki/Tags),
the [highway key](http://wiki.openstreetmap.org/wiki/Key:highway), [the
way element](http://wiki.openstreetmap.org/wiki/Way), and [the node
element](http://wiki.openstreetmap.org/wiki/Node). You will need to use
all of these elements, along with their attribute's values, to construct
your graph for routing.

The `node`:
![node](./proj3/node_xml.jpg)

comprises the backbone of the map; the lat, lon, and id are required
attributes of each node. They may be anything from locations to points
on a road. If a node is a location, a tag element, with key "name" will
tell you what location it is - above, we see an example.

The `way`:
![way](./proj3/way_xml.jpg)

is a road or path and defines a list of `node`s, with name `nd` and the
attribute `ref` referring to the node id, all of which are connected in
linear order. Tags in the `way` will tell you what kind of road it is -
if it has a key of "highway", then the value corresponds to the road
type. See the Javadoc on `ALLOWED_HIGHWAY_TYPES` for restrictions on
which roads we care about. **You should ignore all one-way tags and
pretend all roads are two-way** (this means your directions are not safe
to use, but there are some inaccuracies in the OSM data anyway).

Part of your job will be decide how to store the graph itself in your
`GraphDB` class. Note that the final step of graph construction is to
"clean" the graph, i.e. to destroy all nodes that are disconnected.
Unlike the Princeton graph implementation, your `GraphDB` will need to
permit the insertion and deletion of nodes.

We make one simplifying assumption for this part of the assignment: the
world is flat. In a real world mapping application, calculating the
Euclidean distance should take into account the fact that latitude and
longitude are in slightly different scales (at our latitude, 1 degree of
latitude is \~364,000 feet and 1 degree of longitude is \~288,000 feet),
and should also take into account that as you move north or south, these
two scales change slightly.

For the purpose of computing the Euclidean distance, simply assume that
lon and lat are simply x and y coordinates, i.e. do not take into the
difference in scale between latitude and longitude that results from the
fact that the world is secretly round. This is a reasonable assumption
because our algorithm does not take into account speed limits or
turn-taking time in computing routes, which will be dominant effect.

Spec modification (4/14/17): The `distance` method should return the
euclidean distance, not the longitudinal distance. This is simply the
sqrt(londiff\^2 + latdiff\^2). Contrary to the comment in the spec, it
should not be in units of longitude.

Note: You don't need to actually store the `maxSpeed` anywhere since we
ignore the speed limits when calculating the route in part III. We've
provided this in the skeleton in case you want to play around with this,
but unfortunately the provided data set is missing a bunch of speed
limits so didn't turn out to be particularly useful.

Route Search (Part III)
-----------------------

The `/route` endpoint (kinda like a method in web programming) receives
four values for input: the start point's longitude and latitude, and the
end point's longitude and latitude. Implement `shortestPath` in your
`Router` class so that it satisfies the requirements in the Javadoc.

Your route should be the shortest path that starts from the closest
connected node to the start point and ends at the closest connected node
to the endpoint. Distance between two nodes is defined as the Euclidean
distance between their two points (lon1, lat1) and (lon2, lat2). The
length of a path is the sum of the distances between the ordered nodes
on the path. We do not take into account driving time (speed limits).

#### Runtime & A\* {#runtime-amp-a}

Let `d` be the distance between the start and end node. You cannot
explore all nodes within distance `d` from the start node and expect to
pass the autograder (for long paths, this could be more than half the
nodes in the map).

While Dijkstra's algorithm for finding shortest paths works well, in
practice if we can, we use A\* search. Dijkstra's is a Uniform-Cost
search algorithm - you visit all nodes at a distance `d` or less from
the start node. However, in cases like this, where we know the direction
that we should be searching in, we can employ that information as a
heuristic.

Let `n` be some node on the search fringe (a min priority queue), `s` be
the start node, and `t` be the destination node. A\* differs from
Dijkstra's in that it uses a heuristic `h(n)` for each node `n` that
tells us how close it is to `t`. The priority associated with `n` should
be `f(n) = g(n) + h(n)`, where `g(n)` is the shortest known path
distance from `s` and `h(n)` is the heuristic distance, the Euclidean
distance from `n` to `t`, and thus the value of `h(n)` should shrink as
`n` gets closer to `t`. This helps prevent Dijkstra from exploring too
far in the wrong direction.

This amounts to only a small change in code from the Dijkstra's version
(for us, one line).

#### Supplemental Information

For an example of how A\* works until I record my Part III tips, you
might consider [Professor Abbeel's
video](https://www.youtube.com/watch?v=DhtSZhakyOo), where he goes over
an example of A\* on a general search space. This goes into a bit more
detail that my lecture for 61B.

You can also watch his [CS188 lecture on Informed
Search](https://www.youtube.com/watch?v=8pTjoFiICg8&t=6m40s),
particularly starting from
[here](https://www.youtube.com/watch?v=8pTjoFiICg8&t=16m54s), from back
when I took CS188. Some of the ideas expressed are outside the scope of
what you need for this project - you don't need to worry about the part
where he starts explaining about optimality guarantees and heuristic
admissibility if you don't want; in a graph embedded in the plane like
the one we're working with, Euclidean distance is always admissible and
consistent, and thus it guarantees optimality.

Autocompletion and Search (10 gold points)
------------------------------------------

These gold points are all-or-nothing. You must pass both the timing and
correctness parts to get credit. Tests will be available by 4/15/2017.

### Locations

In the `berkeley.osm` file, we consider all nodes with a name tag a
location. This name is not necessarily unique and may contain things
like road intersections.

### Autocomplete

We would like to implement an Autocomplete system where a user types in
a partial query string, like "Mont", and is returned a list of locations
that have "Mont" as a prefix. To do this, implement
`getLocationsByPrefix`, where the prefix is the partial query string.
The prefix will be a *cleaned* name for search that is: (1) everything
except characters A through Z and spaces removed, and (2) everything is
lowercased. The method will return a list containing the *full names* of
all locations whose cleaned names share the cleaned query string prefix,
without duplicates.

![Autocomplete](./proj3/autocomplete.png)

I recommend using a [Trie](http://www.wikiwand.com/en/Trie). You can
traverse to the node that matches the prefix (if it exists) and then
collect all valid words that are a descendant of that node. We'll
discuss Tries in the class later, but this Gold points opportunity
assumes you'll either find resources or online or read ahead in the
class (by looking at the Spring 2016 website).

#### Runtime {#runtime}

Assuming that the lengths of the names are bounded by some constant,
querying for prefix of length `s` should take `O(k)` time where `k` is
the number of words sharing the prefix.

### Search

The user should also be able to search for places of interest. Implement
`getLocations` which collects a `List` of `Map`s containing information
about the matching locations - that is, locations whose cleaned name
match the cleaned query string exactly. This is *not* a unique list and
should contain duplicates if multiple locations share the same name
(i.e. Top Dog, Bongo Burger). See the Javadocs for the information each
`Map` should contain.

![Selection](./proj3/selection.png)

Implementing this method correctly will allow the web application to
draw red dot markers on each of the matching locations. Note that
because the location data is not very accurate, the markers may be a bit
off from their real location. For example, the west side top dog is on
the wrong side of the street!

#### Runtime {#runtime}

Suppose there are `k` results. Your query should run in `O(k)`.

Acknowledgements
----------------

Data made available by OSM under the [Open Database
License](http://datastructur.es/sp17/materials/proj/proj3/www.openstreetmap.org/copyright).
JavaSpark web framework and Google Gson library.

Alan Yao for creating the original version of this project.
