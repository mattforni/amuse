package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: Playlist
 * Purpose:	This class is an extension of the vector class,
 * containing a vector of songs that are 'in' it.
 * 
 * there is also a field for name of the playlists, which is applied
 * to each of the songs upon saving.
 * 
 */

import java.util.Vector;

public class Playlist {

	private String _name;
	private Vector<Song> _songs;
	private Database _db;
	
	public Playlist(String name, Database db) {
		_name = name;
		_db = db;
		_songs = new Vector<Song>();
	}
	
	public void add(Song song) {
		_songs.add(song);
	}
	
	public boolean contains(Song song) {
		boolean contains = false;
		for (Song song_check : _songs) {
			if (song_check.getID().equals(song.getID()))
				contains = true;
		}
		return contains;
	}
	
	public void setName(String name) {
		for (Song song : _songs) {
			int index = _db.findIndex(Field.ID, song.getID());
			String pls = _db.get(Field.PLAYLISTS, index);
			//System.out.println(pls);
			int n_index = pls.indexOf(_name);
			String pls_1 = pls.substring(0, n_index);
			String pls_2 = pls.substring(n_index+_name.length(), pls.length());
			pls = pls_1+name+pls_2;
			_db.set(Field.PLAYLISTS, pls, index);
			//System.out.println(pls);
		}
		
		_name = name;
	}
	
	public int prevID(int id) {
		Song prev = null;
		for (Song song : _songs) {
			if (Integer.parseInt(song.getID()) == id) {
				if (prev != null)
					return Integer.parseInt(prev.getID());
				else 
					return -1;
			}
			prev = song;
		}
		return -1;
	}
	
	public int nextID(int id) {
		int index = -1, found = -2;
		for (Song song : _songs) {
			index++;
			int currId = Integer.parseInt(song.getID());
			if (currId == id) {
				found = index;
				//System.out.println("found ID "+id+" @ "+found);
				break;
			}
		}
		int rtn = -1;
	
		if (found < _songs.size()-1)
			rtn = Integer.parseInt((_songs.get(found+1)).getID());
		
		return rtn;
	}
	
	public String getName() {
		return _name;
	}
	
	public Vector<Song> getSongs() {
		return _songs;
	}

	public int size() {
		return _songs.size();
	}

}
