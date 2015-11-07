package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: AmusePlayer
 * Purpose:	This class acts as a very simple thread executor of
 * fixed size one.  it keeps track of the currently playing
 * mp3 (which extends thread) and controls flow from thread to thread.
 * 
 * *NOTE*
 * Since each mp3 has its own play capabilities, this class
 * properly and cleanly exits the current mp3 before starting
 * the next.  
 * 
 */

import javax.swing.JProgressBar;

public class AmusePlayer {

	private AmuseHolder _ah;
	private MP3 _mp3;
	private boolean _playing;
	private Database _db;
	private AmuseGui _gui;
	private Playlists _pls;
	private JProgressBar _prog;
	
	public AmusePlayer(Database db, AmuseGui gui, Playlists pls, JProgressBar prog) {
		_db = db;
		_gui = gui;
		_ah = new AmuseHolder(_gui);
		_playing = false;
		_mp3 = new MP3(null, -1, _gui, prog, true);
		_pls = pls;
		_prog = prog;
	}
	
	public AmuseHolder getHolder() {
		return _ah;
	}
	
	public boolean playing() {
		return _playing;
	}
	
	@SuppressWarnings("deprecation")
	public void play() {
		int selected_id = _ah.getSelectedId();
		//System.out.println("selected ID "+selected_id);
		/* if the selected song is different than the currently playing song
		 * 	create a new mp3 and start it
		 * else
		 * 	-->
		 */
		if (selected_id != _mp3.getID()) {
			_mp3.quit();	
			int index = _db.findIndex(Field.ID, ""+selected_id);
			_mp3 = new MP3(_db.get(Field.PATH, index), selected_id, _gui, _prog, false);
			String plays = ""+(Integer.parseInt(_db.get(Field.PLAYS, index))+1);
			//System.out.println(plays);
			_db.set(Field.PLAYS, plays, index);
			_gui.createLibrary();
			_mp3.start();
			_playing = true;
		} else {
			//System.out.println("should be here");
			/* if the currently playing song is paused
			 * 	resume
			 * else
			 * 	pause it
			 */
			if (_mp3.isPaused())
				_mp3.resume();
			_mp3.togglePause();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void pause() {
		_mp3.togglePause();
	}
	
	public void prev() {
		// create a new mp3 with the previous song file
		// update the play holder		
		Playlist pl = _pls.getCurrPlaylist();
		int p_id = pl.prevID(_mp3.getID());
		int p_index = _db.findIndex(Field.ID, ""+p_id);
		if (p_index != -1) {
			_ah.setSelectId(p_id);
			//System.out.println("Setting selection to ID: "+p_id);
			play();
		}		
	}
	
	
	public void next() {
		// create a new mp3 with the next song file
		// update the play holder
		Playlist pl = _pls.getCurrPlaylist();
		int n_id = pl.nextID(_mp3.getID());
		//System.out.println("looking for ID "+n_id);
		int n_index = _db.findIndex(Field.ID, ""+n_id);
		if (n_index != -1) {
			_ah.setSelectId(n_id);
			//System.out.println("Setting selection to ID: "+n_id);
			play();
		}
		
	}
	
	
	
}
