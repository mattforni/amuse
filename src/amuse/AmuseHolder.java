package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: AmuseHolder
 * Purpose:	This holder keeps track of the currently selected song.
 * it has an accessor and mutator method to get and change the 
 * currently selected unique ID.   
 * 
 */

public class AmuseHolder {
	
	private int _selectId;
	private AmuseGui _gui;
	
	public AmuseHolder(AmuseGui gui) {
		_selectId = -1;
		_gui = gui;
	}
	
	public void setSelectId(int id) {
		_selectId = id;
		_gui.createLibrary();
		//System.out.println("we've set it");
	}
	
	public int getSelectedId() {
		return _selectId;
	}

}
