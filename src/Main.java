/**
 * This is an utility class used to start this program.
 * 
 * @author Qing Bai
 * @version 06/03/2015
 */
public class Main {
	
    /**
     * The start point of this program.
     * 
     * @param theArgs command line arguments, ignored in this program
     */
	public static void main(String[] theArgs) {
		System.out.println("5 x 5 Maze with debugging:\n");
		Maze maze1 = new Maze(5, 5, true);
		maze1.display();
		System.out.println("6 x 12 Maze with debugging off:\n");
		Maze maze2 = new Maze(6, 12, false);
		maze2.display();
		// this is for components test
		test();
	}
	
	/**
	 * This tests maze components by checking different mazes with
	 * different sizes.
	 */
	public static void test() {
		System.out.println("The following mazes for test only:\n");
		System.out.println("First maze test:\n");
		Maze maze1 = new Maze(3, 5, true);
		maze1.display();
		System.out.println("Second maze test:\n");
		Maze maze2 = new Maze(6, 3, true);
		maze2.display();
		System.out.println("Third maze test:\n");
		Maze maze3 = new Maze(10, 5, true);
		maze3.display();
	}
}
