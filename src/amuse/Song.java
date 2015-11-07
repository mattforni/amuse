package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: Song
 * Purpose:	A fairly simple wrapper class to temporarily store and move
 * values from the databse.  as you can see, the strings match the fields
 * in the enumeration.
 * 
 * 
 */

public class Song {
	
	private String _id, _title, _artist, _path, _plays, _playlists;

	public Song(String id, String title, String artist, String path, String plays, String playlists) {
		_id = id;
		_title = title;
		_artist = artist;
		_path = path;
		_plays = plays;
		_playlists = playlists;
	}
	
	public String getID() {
		return _id;
	}
	
	public String getTitle() {
		return _title;
	}

	public String getArtist() {
		return _artist;
	}
	
	public String getPath() {
		return _path;
	}
	
	public String getPlays() {
		return _plays;
	}
	
	public String getPlaylists() {
		return _playlists;
	}
	
	public void setTitle(String title) {
		_title = title;
	}
	
	public void setArtist(String artist) {
		_artist = artist;
	}
	
	public void setPath(String path) {
		_path = path;
	}
	
	public void setPlays(String plays) {
		_plays = plays;
	}
	
	public void setPlaylists(String playlists) {
		_playlists = playlists;
	}
	
}
