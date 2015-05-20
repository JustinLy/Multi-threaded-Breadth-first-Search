

### SimpleGraph.java 

This is the graph ADT, with the BFS method.

### MarvelGenerator.java

This class is used to generate a SimpleGraph object using the same file format as "labeled_edges.tsv"

### MarvelMain.java

This class actually creates the big Marvel Graph from the "labeled_edges.tsv". It also calls the BFS a few times to show you that it works on the required data set.

### MP5.java

This class is to create a SimpleGraph from a file and do a search with the provided COMMAND-LINE ARGUMENTS.



### GraphBuildingTest.java

JUnit tests to test simple functionality of the SimpleGraph ADT such as adding vertices and edges

### MarvelGeneratorTest.java

A single JUnit test to make sure the MarvelGenerator class is building graphs from datasets properly. 

### SinglethreadSearchTest.java

JUnit tests to test the Breadth-First Search method of the SimpleGraph ADT USING ONLY 1 SEARCH THREAD. It uses data from "dc.tsv" (small DC dataset I made up for testing)

### MultithreadSearchTest.java

JUnit tests to test the Breadth-First Search using MULTIPLE SEARCH THREADS. In particular, I used 2 threads for most of the tests, and 3 for a few.
 A majority of these tests are the exact same as the single-threaded ones, except they use multiple threads.

