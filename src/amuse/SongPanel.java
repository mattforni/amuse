package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: SongPanel
 * Purpose:	an instance of this class will be created for every entry in 
 * the currently selected play list, whenever the gui's .createLibrary()
 * method is called.
 * 
 * These jpanel subclasses simply build the entries that the user
 * sees in the library panel.  
 * 
 * 
 */

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SongPanel extends JPanel implements Constants, Listeners {

	private static final long serialVersionUID = 1L;
	private JTextField _id, _title, _artist, _plays, _path;
	
	public SongPanel(int index, Database db, AmuseHolder ah, Song song, AddPlaylistLstnr plLstnr) {
		super(new FlowLayout());
		
		if (index % 2 == 0)
			setBackground(new Color(193,205,193));
		else
			setBackground(new Color(131,139,131));
		
		int db_index = db.findIndex(Field.ID, song.getID());
		
		_id = new JTextField(DB_ENTRY_LEN_SHORT);
		_id.addMouseListener(new SelectLstnr(db, ah));
		_id.addMouseListener(plLstnr);
		_title = new JTextField(DB_ENTRY_LEN);
		ChangeFieldLstnr titleChange = new ChangeFieldLstnr(db);
		_title.addMouseListener(titleChange);
		_title.addActionListener(titleChange);
		//_title.addMouseListener(new SelectLstnr(db, ah));
		_artist = new JTextField(DB_ENTRY_LEN);
		ChangeFieldLstnr artistChange = new ChangeFieldLstnr(db);
		_artist.addMouseListener(artistChange);
		_artist.addActionListener(artistChange);
		//_artist.addMouseListener(new SelectLstnr(db, ah));
		_plays = new JTextField(DB_ENTRY_LEN_SHORT);
		_plays.addMouseListener(new SelectLstnr(db, ah));
		_path = new JTextField(DB_ENTRY_LEN_LONG);
		_path.addMouseListener(new SelectLstnr(db, ah));
		
		//int s_index = db.findIndex(Field.ID, song.getID());
		_id.setText(db.get(Field.ID, db_index));
		_id.setEditable(false);
		_title.setText(db.get(Field.TITLE, db_index));
		_title.setEditable(false);
		_artist.setText(db.get(Field.ARTIST, db_index));
		_artist.setEditable(false);
		_plays.setText(db.get(Field.PLAYS, db_index));
		_plays.setEditable(false);
		_path.setText(db.get(Field.PATH, db_index));
		_path.setEditable(false);
		
		add(_id);
		add(_title);
		add(_artist);
		add(_plays);
		add(_path);
	}
	
	public String getID() {
		return _id.getText();
	}
	
	public void setFields(Color color) {
		_id.setBackground(color);
		_title.setBackground(color);
		_artist.setBackground(color);
		_plays.setBackground(color);
		_path.setBackground(color);
	}
}
