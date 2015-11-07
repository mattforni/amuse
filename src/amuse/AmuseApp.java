package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: AmuseApp
 * Purpose:	This is the wrapper class.  it starts everything.
 * 
 */

public class AmuseApp {

	public AmuseApp() {
		new AmuseGui();
	}
	
	public static void main(String[] args) {
		new AmuseApp();
	}

}
