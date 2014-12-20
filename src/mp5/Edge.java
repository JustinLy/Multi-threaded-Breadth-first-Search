package mp5;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Edge implements Comparable<Object> {

	private final Set<String> vertexPair;
	private final String edgeLabel;
	/**
	 * Creates an edge between the pair of vertices and labels it with edgeLabel
	 * @param vertexPair - The pair of vertices you are creating an edge between
	 * @param edgeLabel - The name of the edge between the vertices
	 * @return an edge connecting the vertices in vertexPair
	 * @throws IllegalArgumentException - If size of vertexPair != 2
	 */
	public Edge( Set<String> vertexPair, String edgeLabel ) 
	{
		if( vertexPair.size() != 2 )
			throw new IllegalArgumentException();
		this.vertexPair = vertexPair;
		this.edgeLabel = edgeLabel;
	}
	
    /**
    Returns a set containing the 2 vertices of the edge
    @return - a COPY of the set containing the 2vertices of the edge
    */
    public Set<Object> getVertices()
    {
        return new HashSet<Object>( vertexPair );
    }
    
    /**
     * Return label associated with this edge
     * @return - the label associated with this edge
     */
    public String getLabel()
    {
    	return edgeLabel;
    }
    
	/**
    Returns the vertex connected to "v1" on this edge
    @param v1 - the vertex whose partner you want to obtain
    @return - The vertex connected to "v1" on this edge
    @throws IllegalArgumentException - if v1 is not connected to this edge
    */
    public String getPartner( String v1 ) throws IllegalArgumentException
    {
        //Make copy of vertices so you can safely remove v1 from it
        List<String> copyVertices = new ArrayList<String>( vertexPair );
        if( copyVertices.remove(v1) )
        	return copyVertices.get(0); //V2 the partner vertex is all that's left in list
        else
        	throw new IllegalArgumentException();     
    }
    
    /**
     * Compares the edge labels between this edge and "obj" using lexicographic order
     * @param obj - Edge you are comparing this to
     * @return - neg, zero, or pos if this edge's order is less than, equal or greater than "obj"
     * @throws NullPointerException - if obj is null
     * @throws ClassCastException - if obj is not of type Edge
     */
	@Override
	public int compareTo(Object obj) {
		if( obj == null  )
			throw new NullPointerException();
		else if( !(obj instanceof Edge ))
			throw new ClassCastException();
		else 
		{
			Edge edge2 = (Edge) obj;
			return edgeLabel.compareTo(edge2.edgeLabel);
		}
	}

	/**Auto-generated hashCode() method **/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((edgeLabel == null) ? 0 : edgeLabel.hashCode());
		result = prime * result
				+ ((vertexPair == null) ? 0 : vertexPair.hashCode());
		return result;
	}

	/**
	 * Checks if the specified Edge is equal to this by comparing edgeLabel and vertexPair
	 *@param obj - the Edge you are comparing this to for equality.
	 *@return true if obj's edgeLabel and vertexPair are equal to the ones in this, false if not, 
	 *			 or obj not of type Edge
	 */
	@Override
	public boolean equals(Object obj) {
		if( hashCode() == obj.hashCode() && obj instanceof Edge )
		{
		Edge edge2 = (Edge) obj;
		if( edgeLabel.equals(edge2.edgeLabel) && vertexPair.equals(edge2.vertexPair))
			return true;
		}
			return false;
	}
	
}
