package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: Playlists
 * Purpose:	contains a vector of playlists, which keep in mind are
 * essentially vectors themself.  thus, we have a list of our lists
 * and we can iterate between them to update the currently selected 
 * playlist.  
 * 
 * this class also knows how to build its own display component
 * and does so at the request of the gui, returning the panel
 * with the currently selected playlist highlighted and two entries
 * for each playlist, the name and number of songs.
 * 
 */

import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class Playlists implements Listeners {
	
	private Vector<Playlist> _pls;
	private SpringLayout _layout;
	private JPanel _playlistDisp;
	private AmuseGui _gui;
	private Playlist _currPlaylist;
	private Database _db;
	private JTextField _msg;
	
	public Playlists(AmuseGui gui, Database db, JTextField msg) {
		_gui = gui;
		_db = db;
		_msg = msg;
		_pls = new Vector<Playlist>();
		newPlaylist(ALL_PL);
		_currPlaylist = _pls.get(0);
	}
	
	public Vector<String> getAvailablePlaylists() {
		Vector<String> names = new Vector<String>();
		for (Playlist list : _pls) {
			if (!list.getName().equals(ALL_PL))
				names.add(list.getName());
		}
		return names;
	}
	
	public void newPlaylist(String name) {
		String full = name;
		if (full == "[untitled]") {
			int count = 1;
			full = name+"_"+count;
			while ( plExists(full) == true ) {
				count++;
				full = name+"_"+count;
			}
		}
		_pls.add(new Playlist(full, _db));
	}
	
	public boolean plExists(String pl) {
		boolean exists = false;
		for (Playlist list : _pls) {
			if (list.getName().equals(pl)) {
				exists = true;
			}
		}
		return exists;
	}
	
	public void addSong(Song song) {
		String pl, pls = song.getPlaylists();
		
		if (pls.equals(ALL_PL))
			add(ALL_PL, song);
		else {
			while (pls.length() > 1) {
				pl = pls.substring(0, pls.indexOf(','));
				add(pl, song);
				pls = pls.substring(pls.indexOf(',')+1, pls.length());
			}
		}
	}
	
	public void add(String pl, Song song) {
		Playlist list = getPlaylist(pl);
		/**
		 * if the playlist does not exist [ie. for loading]
		 * 		create a new playlist
		 * else
		 * ====>
		 */
		if (list == null) {
			//System.err.println("we have to make playlist "+pl);
			_pls.add(new Playlist(pl, _db));
			getPlaylist(pl).add(song);
		} else {
			/** 
			 * if the playlist does not already contain the song
			 * 		add the song
			 */
			if (!list.contains(song)) {
				list.add(song);
				//System.out.println("added "+song.getTitle()+" -to-> "+list.getName());
				//System.out.println(list.size());
			} else {
				//System.err.println("song already in playlist");
			}
		}
	}
	
	public JPanel createPlaylists() {
		_layout = new SpringLayout(); 
		_playlistDisp = new JPanel(_layout);	
		
		JTextField pls = new JTextField("playlists.", PLAY_WIDTH_CHAR);
		pls.setEditable(false);
		_layout.putConstraint(SpringLayout.WEST, pls, PLAY_BORDER, SpringLayout.WEST, _playlistDisp);
		_layout.putConstraint(SpringLayout.NORTH, pls, PLAY_BORDER, SpringLayout.NORTH, _playlistDisp);
		_playlistDisp.add(pls);
		
		int count = 1;
		for (Playlist list : _pls) {
			JTextField pl = new JTextField(list.getName(), PLAY_INDENT_CHAR);
			JTextField pl_size = new JTextField(""+(list.getSongs()).size(), 4);
			
			if (list == _currPlaylist) {
				pl.setBackground(SELECTED_COLOR);
				pl_size.setBackground(SELECTED_COLOR);
			}
			PlaylistLstnr plLstnr = new PlaylistLstnr(this, _msg);
			pl.addMouseListener(plLstnr);
			if(!list.getName().equals(ALL_PL)) {
				pl.addActionListener(plLstnr);
				pl.addFocusListener(plLstnr);
			}

			pl.setEditable(false);
			_layout.putConstraint(SpringLayout.WEST, pl, (PLAY_BORDER + PLAY_INDENT_PX), SpringLayout.WEST, _playlistDisp);
			_layout.putConstraint(SpringLayout.NORTH, pl, PLAY_BORDER + PLAY_HEIGHT*count, SpringLayout.NORTH, _playlistDisp);
			_playlistDisp.add(pl);
			
			pl_size.setEditable(false);
			_layout.putConstraint(SpringLayout.WEST, pl_size, (PLAY_BORDER + PLAY_INDENT_PX + 115), SpringLayout.WEST, _playlistDisp);
			_layout.putConstraint(SpringLayout.NORTH, pl_size, PLAY_BORDER + PLAY_HEIGHT*count, SpringLayout.NORTH, _playlistDisp);
			_playlistDisp.add(pl_size);
			
			count++;
		}
		return _playlistDisp;
	}
	
	public void setCurrPlaylist(String playlist) {
		_currPlaylist = getPlaylist(playlist);
		_gui.createLibrary();
		_gui.createPlaylists();
		//System.out.println("PLAYLIST: "+_currPlaylist);
	}
	
	public Playlist getCurrPlaylist() {
		return _currPlaylist;
	}
	
	public void flush() {
		_pls.clear();
		newPlaylist(ALL_PL);
	}
	
	public Playlist get(int i) {
		return _pls.get(i);
	}
	
	public Playlist getPlaylist(String name) {
		for (Playlist list : _pls) {
			if (list.getName().equals(name))
				return list;
		}
		return null;
	}
	
	public String getLastName() {
		return _pls.lastElement().getName();
	}

}
