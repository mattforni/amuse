package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: XMLParser
 * Purpose:	responsible for all of the saving and loading.
 * its a very simple xml parser with a library root element
 * and song child nodes with several parameters.  however,
 * in the future i hope to restructure into playlists and
 * build in other values, etc.  
 * 
 * saving
 * when saving, we iterate through all elements of the
 * database and create a song child node for each of 
 * the songs
 * 
 * loading
 * for each song child node we build an entry in the database
 * and tell the playlists manager pattern to add the song
 * to all appropriate playlists
 */

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTextField;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XMLParser {
	
	public static void save(String path, Database db) {
		try {
		    // first of all we request out DOM-implementation:
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    // then we have to create document-loader:
		    DocumentBuilder loader = factory.newDocumentBuilder();

		    // createing a new DOM-document...
		    Document document = loader.newDocument();

		    // create root-element
		    Element library = document.createElement(Constants.LIB_TAG.toUpperCase());
		    document.appendChild(library);
		    
		    Element song = null;
		    for (int song_num = 0; song_num < db.dbSize(); song_num++) {
		    	song = document.createElement(Constants.SONG_TAG.toUpperCase());
		    	
		    	song.setAttribute(Constants.ID_ATTR, db.get(Field.ID, song_num));
		    	song.setAttribute(Constants.TITLE_ATTR, db.get(Field.TITLE, song_num));
		    	song.setAttribute(Constants.ARTIST_ATTR, db.get(Field.ARTIST, song_num));
		    	song.setAttribute(Constants.PATH_ATTR, db.get(Field.PATH, song_num));
		    	song.setAttribute(Constants.PLAYS_ATTR, db.get(Field.PLAYS, song_num));
		    	song.setAttribute(Constants.PLAYLISTS_ATTR, db.get(Field.PLAYLISTS, song_num));
		    	
		    	library.appendChild(song);
		    }
		    
		    // use specific Xerces class to write DOM-data to a file:
		    XMLSerializer serializer = new XMLSerializer();
		    FileWriter writer = new FileWriter(path);
		    serializer.setOutputCharStream(writer);
		    serializer.serialize(document);
		    
		    //System.out.println("<SAVED>: "+path);
		} catch (Exception ex) {
		      ex.printStackTrace();
		}
	}
	
	public static void load(String path, Database db, Playlists pl, JTextField msg) {
		// before loading we need to clean out the Database and
		// Playlist objects so that we may build to them
		
		db.flush();
		//System.out.println(db.empty()+" : empty?");
		pl.flush();		
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder loader = factory.newDocumentBuilder();

		    Document document = loader.parse(path);
		    Element library = document.getDocumentElement();
		    
		    NodeList songs = library.getChildNodes();
		    for (int node_id = 0; node_id < songs.getLength(); node_id++ ) {
		    	Node node = songs.item(node_id);
		    	
		    	NamedNodeMap attrs = node.getAttributes();
		    	String id = attrs.getNamedItem(Constants.ID_ATTR).getNodeValue();
		    	String title = attrs.getNamedItem(Constants.TITLE_ATTR).getNodeValue();
		    	String artist = attrs.getNamedItem(Constants.ARTIST_ATTR).getNodeValue();
		    	String song_path = attrs.getNamedItem(Constants.PATH_ATTR).getNodeValue();
		    	String plays = attrs.getNamedItem(Constants.PLAYS_ATTR).getNodeValue();
		    	String playlists = attrs.getNamedItem(Constants.PLAYLISTS_ATTR).getNodeValue();
		    	
		    	Song song = new Song(id, title, artist, song_path, plays, playlists);
		    	
		    	db.addSong(song);
		    	pl.addSong(song);
		    	//System.out.println("after load pl '"+pl.get(0).getName()+"': "+pl.get(0).getSongs());
		    }
		    //msg.setText("<LOADED>: "+path);
		} catch (IOException e) {
	    	e.printStackTrace();
	    } catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
