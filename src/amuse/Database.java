package amuse;

import java.util.Vector;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: Databse
 * Purpose:	This class is fairly self explanatory.  The structure of this database
 * is an enum-based vector of vectors, such that the number of columns is locked to the
 * the values of the enum, while the actual fields are growable and deleteable.  
 * 
 * 	this is the field of interest
 *  ie. (_db.get(Field.ID))
 * 		(_db.get(Field.ID)).get(index);
 * 		this returns the value of the field for index
 * 
 * *NOTE* Although the user is not allowed to search the database
 * the search functionality exists; there simply is no mechanism for
 * user interaction.
 */

public class Database {
	
	private Vector<Vector<String>> _db;
	private int _toAdd;
	
	public Database() {
		_toAdd = 0;
		// initialize the databse structure
		// the number of fields is fixed 
		int num_fields = Field.values().length;
		_db = new Vector<Vector<String>>(num_fields);
		
		//System.out.println("Size / Cap:\t"+_db.size()+" / "+_db.capacity());
		for (int field = 0; field < _db.capacity(); field++) {
			_db.add(new Vector<String>());
		}
		//System.out.println("Size / Cap:\t"+_db.size()+" / "+_db.capacity());
	}
	
	public boolean isField(Field id, String val) {
		return (find(id, val) == -1);
	}
	
	public int find(Field field, String val) {
		Vector<String> vals = _db.get(field.ordinal());
		int id = 0;
		for (String value : vals) {
			if (value.equals(val))
				return id;
			id++;
		}
		return -1;
	}
	
	public int dbSize() {
		return _db.get(0).size();
	}
	
	public boolean empty() {
		for (Vector<String> vect : _db) {
			if (!vect.isEmpty())
				return false;
		}
		return true;
	}
	
	public String get(Field field, int song_num) {
		if (!empty() && (song_num < dbSize()))
			return _db.get(field.ordinal()).get(song_num);
		else
			return null;
	}
	
	public Song getSong(Field field, String val) {
		Vector<String> vals = _db.get(field.ordinal());
		int index = 1;
		index = vals.indexOf(val);
		
		String id = _db.get(Field.ID.ordinal()).get(index);
		String title = _db.get(Field.TITLE.ordinal()).get(index);
		String artist = _db.get(Field.ARTIST.ordinal()).get(index);
		String path = _db.get(Field.PATH.ordinal()).get(index);
		String plays = _db.get(Field.PLAYS.ordinal()).get(index);
		String playlists = _db.get(Field.PLAYLISTS.ordinal()).get(index);
		
		return new Song(id, title, artist, path, plays, playlists);
	}
	
	public int findIndex(Field field, String val) {
		Vector<String> vals = _db.get(field.ordinal());
		int index = 1;
		index = vals.indexOf(val);
		return index;
	}
	
	public void set(Field field, String val, int song_num) {
		_db.get(field.ordinal()).set(song_num, val);
	}
	
	public void addSong(Song song) {
		//System.out.println("adding "+song.getArtist());
		String id = song.getID(), title = song.getTitle(), artist = song.getArtist();
		_db.get(Field.ID.ordinal()).add(id);
		_db.get(Field.TITLE.ordinal()).add(title);
		_db.get(Field.ARTIST.ordinal()).add(artist);
		_db.get(Field.PATH.ordinal()).add(song.getPath());
		_db.get(Field.PLAYS.ordinal()).add(""+song.getPlays());
		if (song.getPlaylists().length() < Constants.ALL_PL.length())
			System.err.println("UH-OH");
		else
			_db.get(Field.PLAYLISTS.ordinal()).add(song.getPlaylists());
		_toAdd++;
	}
	
	public void remove(int id) {
		if(!empty()) {
			String dlt_msg = "<DELETED>: "+_db.get(Field.TITLE.ordinal()).get(id)+
			" - "+_db.get(Field.ARTIST.ordinal()).get(id);
			for (Field field : Field.values())
			_db.get(field.ordinal()).remove(id);
			System.out.println(dlt_msg);
		}
			/*
			System.out.println("looking for :"+id);
			int index = _db.get(Field.ID.ordinal()).indexOf(""+id);
			//System.out.println(index+"better not equal -1);
			if (index == -1)
				System.err.println("<Database Error>: Song to delete not found.");
			else {
				String dlt_msg = "<DELETED>: "+_db.get(Field.TITLE.ordinal()).get(index)+
					" - "+_db.get(Field.ARTIST.ordinal()).get(index);
				for (Field field : Field.values())
					_db.get(field.ordinal()).remove(index);
				System.out.println(dlt_msg);
			}*/
	}
	
	public void flush() {
		int size = dbSize();
		for (int i = 0; i < size; i++) {
			for (Field field : Field.values())
				_db.get(field.ordinal()).remove(0);
		}
		_toAdd = 0;
	}
	
	public void printLine(int song_num) {
		int id = Integer.parseInt(_db.get(Field.ID.ordinal()).get(song_num));
		String title = _db.get(Field.TITLE.ordinal()).get(song_num);
		String artist = _db.get(Field.ARTIST.ordinal()).get(song_num);
		String path = _db.get(Field.PATH.ordinal()).get(song_num);
		int plays = Integer.parseInt(_db.get(Field.PLAYS.ordinal()).get(song_num));
		String playlists = _db.get(Field.PLAYLISTS.ordinal()).get(song_num);
		
		System.out.println(id+"\t| "+title+"\t| "+artist+"\t| "+path+"\t| "+plays+"\t| "+playlists);
	}
	
	public void printDb() {
		System.out.println("\nid\t| title\t| artist\t| path\t| plays\t| playlists");
		for (int line = 0; line < dbSize(); line++) {
			printLine(line);
		}
		System.out.println();
	}
	
	public int next() {
		int rtn = _toAdd;
		_toAdd++;
		return rtn;
	}

}
