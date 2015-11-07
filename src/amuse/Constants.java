package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: Constants
 * Purpose:	Keeps track of the various constants needed
 * to build and operation Amuse
 * 
 */

import java.awt.Color;
import java.awt.Dimension;

public interface Constants {
	
	public static final String LIB_TAG = "library"; 
	public static final String SONG_TAG = "song"; 
	
	public static final String ID_ATTR = "id";
	public static final String TITLE_ATTR = "title";
	public static final String ARTIST_ATTR = "artist";
	public static final String PATH_ATTR = "file_path";
	public static final String PLAYS_ATTR = "plays";
	public static final String PLAYLISTS_ATTR = "playlists";
	
	public static final String LIB_PATH = "library.xml";
	
	public static final String ALL_PL = "all";
	
	public static int SAVELOAD_SIZE = 12;
	public static String[] SEARCH_TYPES = {"title.", "artist."}; 
	public static String[] DISPLAY_TYPES = {"id.", "title.", "artist.", "plays.", "path."}; 
	
	public static int TITLE_SIZE = 12;
	public static int ARTIST_SIZE = 12;
	public static int PATH_SIZE = 12;
	
	public static String FILE_BTN = ">>";
	public static String ADD_BTN = "[+]";
	public static String X_BTN = "[x]";
	
	public static int DB_ENTRY_LEN = 18;
	public static int DB_ENTRY_LEN_SHORT = 4;
	public static int DB_ENTRY_LEN_LONG = 19;
	
	public static int CONTROL_WIDTH = 250;
	
	public static int DB_ENTRY_HEIGHT = 25;
	
	public static int TOP_PNL_HEIGHT = 30;
	public static int BOT_PNL_HEIGHT = 30;
	
	public static int TRANS_RATE = 4096;
	
	public static int PLAY_BORDER = 10;
	
	public static int PLAY_WIDTH_CHAR = 14;
	public static int PLAY_INDENT_CHAR = 10;
	public static int PLAY_INDENT_PX = (PLAY_WIDTH_CHAR-PLAY_INDENT_CHAR)*11;
	public static int ARROW_WIDTH_PX = 27;
	public static int PLAY_HEIGHT = 23;
	
	public static int UNIT_INC = 10;
	
	// the progress bar constants
	public static int PROG_INC = 1;
	public static int PROG_MAX = 1000;
	
	public static String IMG_PATH = "images/";
	
	public static int MSG_SIZE_CHAR = 40;
	
	public static Dimension SELECT_PL_PNL = new Dimension(250, 100);
	public static Color SELECTED_COLOR = new Color(238,232,205);
	
}
