import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * This is a class for creating a maze.
 * 
 * @author Qing Bai
 * @version 06/03/2015
 */
public class Maze {
	
	/**
	 * This is a random number generator.
	 */
	private final static Random RAND = new Random();
	
	/**
	 * This stores a list of vertices that form the maze.
	 */
	private final List<Vertex> myMaze;
	
	/**
	 * This stores steps of building a maze.
	 */
	private final List<String> mySteps;
	
	/**
	 * This is the height of the maze.
	 */
	private final int myHeight;
	
	/**
	 * This is the width of the maze.
	 */
	private final int myWidth;
	
	/**
	 * This shows whether or not this is in a debugging mode.
	 */
	private final boolean myDebug;
	
	/**
	 * This is a constructor for this class.
	 * 
	 * @param theHeight is height of a maze
	 * @param theWidth is width of a maze
	 * @param theDebug shows whether or not this is in a debugging mode
	 */
	public Maze(final int theWidth, final int theHeight, final boolean theDebug) {
		myMaze = new ArrayList<Vertex>();
		mySteps = new ArrayList<String>();
		myHeight = theHeight;
		myWidth = theWidth;
		myDebug = theDebug;
		buildGraph();
		buildMaze();
	}
	
	/**
	 * This creates a graph that is used for building a maze.
	 */
	private void buildGraph() {
		// creates two more rows and cols for borders but won't include them in maze
		final Vertex[][] graph = new Vertex[myHeight + 2][myWidth + 2];
		final List<Vertex> tempNeighbors = new ArrayList<Vertex>();
		Vertex tempVertex;
		int label = 0;
		
		// sets isVisited to true for vertices at the borders
		for (int col = 0; col < myWidth + 2; col++) {
			graph[0][col] = new Vertex(0);
			graph[0][col].isVisited = true;
			graph[myHeight + 1][col] = new Vertex(0);
			graph[myHeight + 1][col].isVisited = true;
		}
		
		for (int row = 1; row < myHeight + 1; row++) {
			for (int col = 0; col < myWidth + 2; col++) {
				tempVertex = new Vertex(0);
				
				// sets isVisited to true for vertices at the borders
				if (col == 0 || col == myWidth + 1) {
					tempVertex.isVisited = true;
				} else { // assigns 0, 1, 2, 3 to vertices in the center
					tempVertex.myLabel = label++;
				}
				
				graph[row][col] = tempVertex;
			}
		}
		
		for (int row = 1; row < myHeight + 1; row++) {
			for (int col = 1; col < myWidth + 1; col++) {
				tempNeighbors.add(graph[row][col - 1]);
				tempNeighbors.add(graph[row][col + 1]);
				tempNeighbors.add(graph[row - 1][col]);
				tempNeighbors.add(graph[row + 1][col]);
				tempVertex = graph[row][col];
				
				// add neighbors that aren't from the borders
				for (Vertex vertex : tempNeighbors) {
					if (!vertex.isVisited) {
						tempVertex.myNeighbors.add(vertex);
					}
				}
				
				myMaze.add(tempVertex);
				tempNeighbors.clear();
			}
		}
	}
	
	/**
	 * This builds a maze from an existing graph.
	 */
	private void buildMaze() {
		// start point is at top left and end point is at bottom right
		myMaze.get(0).hasNorthWall = false;
		myMaze.get(myWidth * myHeight - 1).hasSouthWall = false;
		buildMazeHelper(myMaze.get(0));
	}
	
	/**
	 * This is a helper method for buildMaze()
	 * 
	 * @param theVertex
	 */
	private void buildMazeHelper(final Vertex theVertex) {
		theVertex.isVisited = true;
		theVertex.myContent = 'V';
		Vertex nextVertex;
		int index;
		int difference;

		if (myDebug) {
			mySteps.add(toString());
		}

		while (hasUnvisited(theVertex.myNeighbors)) {
			// keep running until getting an unvisited vertex
			while (true) {
				index = RAND.nextInt(theVertex.myNeighbors.size());
				nextVertex = theVertex.myNeighbors.get(index);
		
				if (!nextVertex.isVisited) {
					difference = theVertex.myLabel - nextVertex.myLabel;
					
					if (difference == 1) { // travel west
						theVertex.hasWestWall = false;
						nextVertex.hasEastWall = false;
					} else if (difference == -1) { // travel east
						theVertex.hasEastWall = false;
						nextVertex.hasWestWall = false;
					} else if (difference == myWidth) { // travel north
						theVertex.hasNorthWall = false;
						nextVertex.hasSouthWall = false;
					} else { // difference == -myWidth so travel south
						theVertex.hasSouthWall = false;
						nextVertex.hasNorthWall = false;
					}
					
					buildMazeHelper(nextVertex);
					break;	
				}
			}
		}
	}
	
	/**
	 * This checks whether or not there is an unvisited neighbor.
	 * 
	 * @param theNeighbors is a list of neighbors of a vertex
	 * @return true when there is an unvisited neighbor. otherwise, false
	 */
	private boolean hasUnvisited(final List<Vertex> theNeighbors) {
		boolean result = false;
		
		for (Vertex vertex : theNeighbors) {
			if (!vertex.isVisited) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * This solves the maze using depth first search.
	 */
	private void solveMaze() {
		cleanMaze();
		// end point is at bottom right and start point is at top left
		final Vertex endPoint = myMaze.get(myWidth * myHeight - 1);
		Vertex curPoint = myMaze.get(0);
		curPoint.isVisited = true;
		Vertex nextPoint;
		final Stack<Vertex> path = new Stack<Vertex>();
		
		while (!curPoint.equals(endPoint)) {
			if (!curPoint.hasEastWall && !myMaze.get(curPoint.myLabel + 1).isVisited) { // travel east
				nextPoint = myMaze.get(curPoint.myLabel + 1);
			} else if (!curPoint.hasSouthWall && !myMaze.get(curPoint.myLabel + myWidth).isVisited) { // travel south
				nextPoint = myMaze.get(curPoint.myLabel + myWidth);
			} else if (!curPoint.hasWestWall && !myMaze.get(curPoint.myLabel - 1).isVisited) { // travel west
				nextPoint = myMaze.get(curPoint.myLabel - 1);
			} else if (!curPoint.hasNorthWall && !myMaze.get(curPoint.myLabel - myWidth).isVisited) { // travel north
				nextPoint = myMaze.get(curPoint.myLabel - myWidth);
			} else { // dead end, return to last visited vertex
				curPoint = path.pop();
				continue;
			}
			
			path.push(curPoint);
			curPoint = nextPoint;
			curPoint.isVisited = true;
		}
		
		// add the end point to the path
		path.push(curPoint);
		labelPath(path);	
	}
	
	/**
	 * This resets the maze to its initial state that is isVisited = false
	 * and myContent = ' ' for all vertices.
	 */
	private void cleanMaze() {
		for (Vertex vertex : myMaze) {
			vertex.isVisited = false;
			vertex.myContent = ' ';
		}
	}
	
	/**
	 * This marks all vertices on the path with '+'.
	 * 
	 * @param thePath is a stack that stores all vertices on the path
	 */
	private void labelPath(final Stack<Vertex> thePath) {
		Vertex curPoint;
		
		while (!thePath.isEmpty()) {
			curPoint = thePath.pop();
			curPoint.myContent = '+';
		}
	}
	
	/**
	 * This displays the maze using 'X' for wall.
	 */
	public void display() {
		if (myDebug) {
			
			for (String step : mySteps) {
				System.out.println(step);
			}
			
		} else {
			System.out.println(toString());
		}
		
		solveMaze();
		System.out.println(toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		final StringBuilder result = new StringBuilder();
		final int rowNum = 2 * myHeight + 1;
		final int colNum = 2 * myWidth + 1;
		int index;
		
		// this is just for the first row
		for (int row = 0; row < 1; row++) {
			for (int col = 0; col < colNum - 1; col++) {
				if (col % 2 == 0 || myMaze.get(col / 2).hasNorthWall) {
					result.append("X ");
				} else { // put a space when there is one cell with no north wall
					result.append("  ");
				}
			}
		}
		
		result.append("X\n");
		// this is for the rest of rows
		for (int row = 1; row < rowNum; row++) {
			for (int col = 0; col < colNum; col++) {
				index = col / 2 + ((row - 1) / 2) * myWidth;
				
				if (col == 2 * myWidth) {
					result.append("X\n");
				} else if (row % 2 == 1 && col % 2 == 0 && myMaze.get(index).hasWestWall) {
					result.append("X ");
				} else if (row % 2 == 1 && col % 2 == 1 && myMaze.get(index).isVisited) {
					result.append(myMaze.get(index).myContent + " ");
				} else if (row % 2 == 0 && col % 2 == 0) {
					result.append("X ");
				} else if (row % 2 == 0 && col % 2 == 1 && myMaze.get(index).hasSouthWall) {
					result.append("X ");
				} else {
					result.append("  ");
				}
			}
		}
		
		return result.toString();
	}
	
	/**
	 * This is a private class for vertex in a graph.
	 */
	private class Vertex {
		
		/**
		 * This is label of this vertex.
		 */
		public int myLabel;
		
		/**
		 * This stores special characters like ' ', 'V', or '+'.
		 */
		public char myContent;
		
		/**
		 * This stores neighbors of this vertex.
		 */
		public List<Vertex> myNeighbors;
		
		/**
		 * This shows whether or not this vertex has been visited.
		 */
		public boolean isVisited;
		
		/**
		 * This shows whether or not this vertex has a wall in the north.
		 */
		public boolean hasNorthWall;
		
		/**
		 * This shows whether or not this vertex has a wall in the east.
		 */
		public boolean hasEastWall;
		
		/**
		 * This shows whether or not this vertex has a wall in the south.
		 */
		public boolean hasSouthWall;
		
		/**
		 * This shows whether or not this vertex has a wall in the west.
		 */
		public boolean hasWestWall;
		
		/**
		 * This is a constructor of this class.
		 * 
		 * @param theLabel is the label of this vertex
		 */
		public Vertex(final int theLabel) {
			myLabel = theLabel;
			myNeighbors = new ArrayList<Vertex>();
			myContent = ' ';
			isVisited = false;
			hasNorthWall = true;
			hasEastWall = true;
			hasSouthWall = true;
			hasWestWall = true;
		}
	}
}
