package doctorsim;

import doctorsim.models.Hospital;

/**
 * Provides the starting point for the program.
 * 
 * @author Michael Rees
 */
public class Application {
	public static void main(String[] args) {
		new Hospital().start();
	}
}