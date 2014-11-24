Machine Problem 5: Concurrent Breadth First Search
===

### Background 

In this machine problem, you will work with a graph that represents the Marvel comic book universe. Each vertex in the graph will represent a character, e.g., Black Widow or Quicksilver. An edge between the vertices representing Black Widow and Quicksilver indicates that Black Widow and Quicksilver appeared in a comic book together. (You do not need to have reflexive edges from characters to themselves.)

One of the main goals in this assignment is to find shortest paths from a vertex to another vertex. (This is an unweighted graph so you do not need a more sophisticated algorithm such as Dijkstra’s algorithm.) In other words, to find a sequence of books that connects one character to another. Somewhat abstractly, the path from Character A to Character D could be something like this: Character A and Character B appeared together in Book X; Character B and Character C appeared together in Book Y; Character C and Character D appeared together in Book Z.

### Obtain the Marvel Universe Dataset

There is a file in the repository named `labeled_edges.tsv`. This is a tab-separated value file that you can open in any text editor. Each line in this file is of the form:
“Character”	“Book”
where Character is the name of a character and Book represents a comic book that the character appeared in, and the two fields are separated by a tab space.

### Creating the Graph of the Marvel Universe

You should read the data in the TSV file that you downloaded to create a graph that represents the Marvel character universe. It is good to test the data parsing and graph building operations in isolation.

The data should be stored in a graph ADT. You may choose to store a multigraph (where you maintain a separate edge for each comic book that two characters appeared together in) or a simple graph (where at most one edge exists between each pair of characters) and edges are labeled using a list of books that the two characters appeared together in.

Write specifications for the API (application programming interface) for the class or classes that you will use in your implementation of the graph ADT. Write the representation invariant and the abstraction function for your ADT.

You can use the fact that edges in the Marvel universe are undirected.

### Finding Shortest Paths

The central aspect of this MP is to find paths between two characters in the graph. Given the name of two characters, your should write a method that searches for and returns a path through the graph connecting them. How the path is subsequently used, or the format in which it is printed out, is left to you.

Your program should return the shortest path found via a multithreaded breadth-first search (BFS) implementation. A BFS from node *u* to node *v* visits all of *u*’s neighbors first, then all of *u*’s neighbors' neighbors', then all of *u*’s neighbors' neighbors', and so on until *v* is found or all nodes with a path from u have been visited. Below is a general BFS pseudocode to find the shortest path between two nodes in a multigraph *G*. 

For readability, you should use more descriptive variable names in your actual code than are needed in the pseudocode. The pseudocode for single-threaded BFS is as follows:

```
    start = starting node
    dest = destination node
    Q = queue, or "worklist", of nodes to visit: initially empty
    M = map from nodes to paths: initially empty.
        // Each key in M is a visited node.
        // Each value is a path from start to that node.
        // A path is a list; you decide whether it is a list of nodes, or edges,
        // or node data, or edge data, or nodes and edges, or something else.
    
    Add start to Q
    Add start->[] to M (start mapped to an empty list)
    while Q is not empty:
        dequeue next node n
        if n is dest
            return the path associated with n in M
        for each edge e=⟨n,m⟩:
            if m is not in M, i.e. m has not been visited:
                let p be the path n maps to in M
                let p' be the path formed by appending e to p
                add m->p' to M
                add m to Q
```            
    If the loop terminates, then no path exists from `start` to `dest`. The implementation should indicate this to the client.
 Here are some facts about the algorithm.

+ It is a loop invariant that every element of Q is a key in M.
+ If the graph were not a multigraph, the for loop could have been equivalently expressed as for each neighbour m of n.
+ If a path exists from start to dest, then the algorithm returns a shortest path.

Many character pairs will have multiple paths. Your program should return the lexicographically (alphabetically) least path. More precisely, it should pick the lexicographically first character at each next step in the path, and if those characters appear in several comic books together, it should print the lexicographically lowest title of a comic book that they both appear in. The BFS algorithm above can be easily modified to support this ordering: in the for-each loop, visit edges in increasing order of m's character name, with edges to the same character visited in increasing order of comic book title.

You may want to think about whether you should hardcode this behaviour into your ADT or not. What are the advantages/disadvantages of hardcoding this lexicographic ordering? Although there is no penalty in this MP for hardcoding this choice, you should think about the implications of that choice.

You should also provide, in your code, a thready safety argument to establish the correctness of your concurrent BFS implementation.

### Using the Command Line

It should be possible to use your implementation to find the shortest path between two Marvel characters from the command line. You should implement a main( ) method in a file called MP5.java. This main( ) method should allow a user to supply as input the names of two characters (from the keyboard or standard input) and your program should produce the path between the two characters as output to standard output. Output a suitable message if no path is found. There is no rigid spec for input/output formats. 

The command line use of your implementation should be as follows:

` $ java MP5 <filename> <vertex1> <vertex2> <numThreads>`

where `<filename>` refers to the TSV file that contains the graph data (such as `labeled_edges.tsv`) and `<vertex1>` and `<vertex2>` refer to character names per the Marvel data set. `<numThreads>` indicates how many threads to use for your concurrent BFS implementation. 

This command line format allows one to use your implementation for any graph where the file format is the same as the Marvel data set format. **Pay attention to this requirement. We will test your implementation with more than just the Marvel data  set.**

You will have to read about using command line arguments, which is actually a straightforward use of the `argv` `String` array.

### Centrality

Additionally, *for an A+*, find the character that is most central to the Marvel universe. To formally define the measure of centrality we are interested in, let us define *d(u,v)* to be the minimum distance between character *u* and *v*. Let *d<sub>u</sub>* be the maximum value of *d(u,v)* for all possible characters *v*. The most central character is the character has the smallest of all *d<sub>u</sub>* values. *You may have to make some reasonable assumptions to identify the central character.* Your submission can output the central most character to the standard output. You should implement a multithreaded version for this aspect as well.

### Submitting your work

You will submit your work by pushing all your code to your group’s private BitBucket repository named `mp5`. Make sure you provide read/write access to the TA for your lab section.