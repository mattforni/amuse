package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: Library
 * Purpose:	The Library class is just an extension of jpanel
 * that implements scrollable (so that it may scroll within its panel).
 * 
 * This class is responsible for managing the library display in the gui.
 * It should display songs based on the current playlist and highlight the
 * currently selected song if it is in the current playlist.  The Library
 * class simply build a SongPanel for each of the song ids in the current
 * playlist.
 * 
 */

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

public class Library extends JPanel implements Scrollable, SwingConstants, Constants {

	private static final long serialVersionUID = 1L;
	private Dimension _scrollDim;
	
	public Library(LayoutManager layout, Dimension scrollDim) {
		super(layout);
		_scrollDim = scrollDim;
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public int getScrollableBlockIncrement(Rectangle rect, int orient, int direction) {
        int rtn = 0;
		if (orient == HORIZONTAL) {
            rtn = rect.width - UNIT_INC;
        } else {
            rtn = rect.height - UNIT_INC;
        }
		System.out.println("block inc: "+rtn);
		return rtn;
	}

	public boolean getScrollableTracksViewportHeight() {
		return true;
	}

	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	public int getScrollableUnitIncrement(Rectangle rect, int orient, int dir) {
		  int currPos = 0, newPos = 0, rtn = 0;
		  
		  // if we are working with a horizontal scroll
		  if (orient == SwingConstants.HORIZONTAL) {
			  // if we are trying to scroll left
			  if (dir < 0) {
				  System.out.println("scrolling left?");
				  currPos = rect.x;
		    	  newPos = currPos - UNIT_INC;
		    	  System.out.println(newPos+" < 0 ? 0 : "+UNIT_INC);
		    	  if (newPos < 0) 
		    		  rtn = 0;
		    	  else
		    		  rtn = UNIT_INC;
		      } else {
		    	  System.out.println("scrolling right?");
				  currPos = rect.x + rect.width;
		    	  newPos = currPos + UNIT_INC;
		    	  System.out.println(newPos+" > "+_scrollDim.width+" ? 0 : "+UNIT_INC);
				  if (newPos > _scrollDim.width)
					  rtn = 0; 
				  else
					  rtn = UNIT_INC;
		      }
		  } else  {
			  // if we are trying to scroll down
			  if (dir > 0) {
				  System.out.println("scrolling down?");
				  currPos = rect.y + rect.height;
		    	  newPos = currPos + UNIT_INC;
		    	  System.out.println(newPos+" > "+_scrollDim.height+" ? 0 : "+UNIT_INC);
		    	  if (newPos > _scrollDim.height) 
		    		  rtn = 0;
		    	  else
		    		  rtn = UNIT_INC;
		      } else {
		    	  System.out.println("scrolling up?");
				  currPos = rect.y;
		    	  newPos = currPos - UNIT_INC;
		    	  System.out.println(newPos+" < 0 ? 0 : "+UNIT_INC);
				  if (newPos < 0)
					  rtn = 0; 
				  else
					  rtn = UNIT_INC;
		      }
		  }
		  System.out.println("increment: "+rtn);
		  return rtn;
	}

}
