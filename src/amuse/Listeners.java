package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: Listeners
 * Purpose:	handles all of the interation between the user and the 
 * program.  these listeners define how all of the various component
 * respond the mouse clicks, key events and button pushes.  the following
 * listeners are fairly intuitively named, but just in case ive added
 * a short description before most listeners.  
 * 
 */

import java.awt.FlowLayout;
import java.awt.event.*;

import javax.swing.*;

public interface Listeners extends Constants {
	
	// the add - to -> playlist listener
	public class AddPlaylistLstnr implements MouseListener {
		
		private Playlists _pls;
		private Database _db;
		private AmuseGui _gui;
		private JTextField _msg;
		
		public AddPlaylistLstnr(Playlists pls, Database db, JTextField msg, AmuseGui gui) {
			_pls = pls;
			_db = db;
			_msg = msg;
			_gui = gui;
		}
		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON2) {
				String id = ((JTextField)(e.getSource())).getText();
				//System.out.println("ID "+id);
				
				JFrame frame = new JFrame("add ..");
				JPanel pnl = new JPanel(new FlowLayout());
				JComboBox combo = new JComboBox(_pls.getAvailablePlaylists()); 
				combo.setVisible(true);
				pnl.add(combo);
				
				JButton add = new JButton("[+]");
				add.setVisible(true);
				add.addActionListener(new AddListener(combo, frame, id, _pls, _db, _gui, _msg));
				pnl.add(add);
				pnl.setVisible(true);
				
				frame.add(pnl);
				frame.pack();
				frame.setVisible(true);
				
				frame.setFocusable(true);
			}
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}
	
	public class AddListener implements ActionListener {
		private JComboBox _combo;
		private JFrame _frame;
		private String _id;
		private Playlists _pls;
		private Database _db;
		private AmuseGui _gui;
		private JTextField _msg;
		
		public AddListener(JComboBox combo, JFrame frame, String id, Playlists pls, Database db, AmuseGui gui, JTextField msg) {
			_combo = combo;
			_frame = frame;
			_id = id;
			_pls = pls;
			_db = db;
			_gui = gui;
			_msg = msg;
		}
		public void actionPerformed(ActionEvent arg0) {
			String pl = (String)_combo.getSelectedItem();
			_pls.add(pl, _db.getSong(Field.ID, _id));
			int index = _db.findIndex(Field.ID, _id);
			_db.set(Field.PLAYLISTS, (_db.get(Field.PLAYLISTS, index)+pl+","), index);
			_frame.dispose();
			_gui.createPlaylists();
			_gui.createLibrary();
			_msg.setText("<ADDED>: "+_db.get(Field.TITLE, index)+" to "+pl);
		}
	}
	
	public class ChangeFieldLstnr implements MouseListener, ActionListener {
		private Database _db;
		private Field _field;
		private int _index;
		public ChangeFieldLstnr(Database db) {
			_db = db;
		}
		
		public void mouseClicked(MouseEvent arg0) {
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent e) {
			JTextField src = (JTextField)e.getSource();
			if (e.getButton() == MouseEvent.BUTTON3) {
				String field_val = src.getText();
				
				// if the field is an artist
				int index = -1;
				if ((index = _db.find(Field.ARTIST, field_val)) != -1) {
					src.setEditable(true);
					src.selectAll();
					_field = Field.ARTIST;
					_index = index;
				} else if ((index = _db.find(Field.TITLE, field_val)) != -1) {
					src.setEditable(true);
					src.selectAll();
					_field = Field.TITLE;
					_index = index;
				} else {
					System.err.println("listeners error 1");
				}
			}
		}

		public void mouseReleased(MouseEvent arg0) {
		}

		public void actionPerformed(ActionEvent e) {
			JTextField src = (JTextField)e.getSource();
			src.setEditable(false);
			_db.set(_field, src.getText(), _index);
		}
		
	}
	
	// this is applied to the id, plays and path fields 
	// it keeps track of which song is currently selected
	public class SelectLstnr implements MouseListener {
		private AmuseHolder _ah;
		public SelectLstnr(Database db, AmuseHolder sh) {
			_ah = sh;
		}
		
		public void mouseClicked(MouseEvent e) {
			JTextField src = (JTextField)e.getSource();
			if (e.getButton() == MouseEvent.BUTTON1) {
				SongPanel pnl = (SongPanel)src.getParent();
				int id = Integer.parseInt(pnl.getID());
				_ah.setSelectId(id);
			}
		}
		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}
	
	// playlist text field listener
	public class PlaylistLstnr implements MouseListener, ActionListener, FocusListener {
		
		private Playlists _lists;
		private String _text;
		private JTextField _msg;
		
		public PlaylistLstnr(Playlists lists, JTextField msg) {
			_lists = lists;
			_text = "";
			_msg = msg;
		}
		
		public void mouseClicked(MouseEvent e) {
			JTextField src = (JTextField)e.getSource();
			if (e.getButton() ==  MouseEvent.BUTTON1) {
				//System.out.println("left_clicking");
				_lists.setCurrPlaylist(src.getText());
				_lists.createPlaylists();
				_msg.setText("<SELECTED>: "+src.getText());
			} else if (e.getButton() ==  MouseEvent.BUTTON3) {
				_text = src.getText();
				if (!_text.equals(ALL_PL)) {
					src.setEditable(true);
					src.selectAll();
				}
			}
		}

		public void mouseEntered(MouseEvent arg0) {			
		}

		public void mouseExited(MouseEvent arg0) {			
		}

		public void mousePressed(MouseEvent arg0) {			
		}

		public void mouseReleased(MouseEvent arg0) {
		}

		public void actionPerformed(ActionEvent e) {
			JTextField src = (JTextField)e.getSource();
			Playlist pl = _lists.getPlaylist(_text);
			_text = src.getText();
			pl.setName(_text);
			src.setEditable(false);
		}

		public void focusGained(FocusEvent arg0) {
		}

		public void focusLost(FocusEvent e) {
			JTextField src = (JTextField)e.getSource();
			if (src.isEditable()) {
				Playlist pl = _lists.getPlaylist(_text);
				_text = src.getText();
				pl.setName(_text);
				src.setEditable(false);
			}
			
		}
	}
	
	/** being add song listeners **/
	// add song listener
	public class AddLstnr implements ActionListener {
		
		private JTextField _title, _artist, _path;
		private AmuseGui _gui;
		private Database _db;
		private Playlists _pls;
		
		public AddLstnr(JTextField title, JTextField artist, JTextField path, AmuseGui gui, Database db, Playlists pls) {
			_title = title;
			_artist = artist;
			_path = path;
			_gui = gui;
			_db = db;
			_pls = pls;
		}
		
		public void actionPerformed(ActionEvent e) {
			String title = _title.getText();
			String artist = _artist.getText();
			String path = _path.getText();
			
			if (!path.equals("")) {
				if (!path.contains("/Amuse/music"))
					_gui.setMessageBox("<Song Add Error>: Please move the file to '/Amuse/music' and try adding again.");
				else {
				
					if (title.equals(""))
						title = "<unknown>";
					if (artist.equals(""))
						artist = "<unknown>";
					
					int index = path.indexOf("music");
					path = path.substring(index);
					
					Song song = new Song(""+_db.next(), title, artist, path, ""+0, ALL_PL+",");
					
					_db.addSong(song);
					_pls.addSong(song);
					//System.out.println("<ADDED>: \""+title+"\" - "+artist+" @-> "+path);
					
					//_title.setText("");
					//_artist.setText("");
					//_path.setText("");
					_gui.createLibrary();
					_gui.setMessageBox("<ADDED>: "+song.getTitle()+" - "+song.getArtist()+" @ '"+song.getPath()+"'");
				}
			}
		}
	}
	
	// add _ filepath listener
	public class SelectAddFileLstnr implements ActionListener {
		
		private JFileChooser _fc;
		private JButton _bb;
		private JTextField _path;
		private AmuseGui _gui;
		
		public SelectAddFileLstnr(JButton bb, JTextField path, AmuseGui gui) {
			_fc = new JFileChooser();
			_fc.addChoosableFileFilter(new Mp3Filter());

			//_fc.setFileHidingEnabled(false);
			_bb = bb;
			_path = path;
			_gui = gui;
		}
		
		public void actionPerformed(ActionEvent e) {
		    // respond only to the browse button
		    if (e.getSource() == _bb) {
		        int returnVal = _fc.showOpenDialog(_gui);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            System.out.println("path: "+_fc.getSelectedFile().getAbsolutePath());
		            String path = _fc.getSelectedFile().getAbsolutePath();
		            _path.setText(path);
		        }
		   }
		}
	}
	/** end add song listeners**/
	
	/** begin save and load listeners **/
	public class SaveLstnr implements ActionListener {
		private Database _db;
		private JTextField _msg;
		public SaveLstnr(Database db, JTextField msg) {
			_db = db;
			_msg = msg;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			// build a gui to display to select save location ?
			
			XMLParser.save(LIB_PATH, _db);	
			_msg.setText("<SAVED>: "+LIB_PATH);
		}
	}
	
	public class LoadLstnr implements ActionListener {
		private Database _db;
		private Playlists _pls;
		private JTextField _msg;
		private AmuseGui _gui;
		public LoadLstnr(Database db, Playlists pls, JTextField msg, AmuseGui gui) {
			_db = db;
			_pls = pls;
			_msg = msg;
			_gui = gui;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			// build a gui to display first?
			
			XMLParser.load(LIB_PATH, _db, _pls, _msg);
			_pls.setCurrPlaylist(ALL_PL);
			_gui.createLibrary();
			_gui.createPlaylists();
			_msg.setText("<LOADED>: "+LIB_PATH);
		}
	}
	/** end save and load listeners **/
	
	//** menu listeners ****
	public class QuitLstnr implements ActionListener {
		private Database _db;
		public QuitLstnr(Database db) {
			_db = db;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			// this dumps a library just in case
			// executes when the program exits
			XMLParser.save("quit_library", _db);
			System.exit(0);
		}
	}

	public class NewPlaylistLstnr implements ActionListener {
		private Playlists _pls;
		private AmuseGui _gui;
		private JTextField _msg;
		public NewPlaylistLstnr(Playlists pls, AmuseGui gui, JTextField msg) {
			_pls = pls;
			_gui = gui;
			_msg = msg;
			
		}
		
		public void actionPerformed(ActionEvent arg0) {
			_pls.newPlaylist("[untitled]");
			_gui.createLibrary();
			_gui.createPlaylists();

			_msg.setText("<PLAYLIST CREATED>: "+_pls.getLastName());
		}
	}

}
