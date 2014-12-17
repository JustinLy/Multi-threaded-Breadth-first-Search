package mp5;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Edge {

	private final Set<String> vertexPair;
	private final String edgeLabel;
	
	public Edge( Set<String> vertexPair, String edgeLabel )
	{
		this.vertexPair = vertexPair;
		this.edgeLabel = edgeLabel;
	}
	
    /**
    Returns a set containing the 2 vertices of the edge
    @return - a set containing the 2vertices of the edge
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
	
}
