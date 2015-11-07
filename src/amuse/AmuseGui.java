package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: AmuseGui
 * Purpose:	This class handles all of the construction and updating
 * of the user interface.  it builds each component of the amuse gui
 * and then adds it to itself, constraining the positions of the components.
 * essentially this class is everything that you see and do.
 * 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.*;

@SuppressWarnings("serial")
public class AmuseGui extends JFrame implements Constants, ScrollPaneConstants, Listeners {
	
	private JMenuBar _menu;
	private JPanel _controls, _progress, _add, _libraryDisp, _playDisplay, _messageBox;
	private JScrollPane _library, _playlists;
	private Database _db;
	private Playlists _pl;
	private AmusePlayer _player;
	private AmuseHolder _holder;
	private JTextField _msgField;
	private AddPlaylistLstnr _plLstnr;
	
	@SuppressWarnings("unused")
	private Point _search_tl, _controls_tl, _progress_tr;
	private Point _playlists_tl, _library_tl;
	private Point _add_br, _message_br;
	private Dimension _libSize, _plSize;
	private int _width, _height;
	
	private JProgressBar _prog;
	
	
	public AmuseGui() {
		super("Amuse");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(1, 1));
		setResizable(false);
		_db = new Database();	
		createMessageBox();
		_pl = new Playlists(this, _db, _msgField);
		
		createProgress();
		
		_player = new AmusePlayer(_db, this, _pl, _prog);
		_holder = _player.getHolder();
		
		SpringLayout total_spring = new SpringLayout();
		JPanel total = new JPanel(total_spring);
		
		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		_width = (int)(screen_size.width*0.8);
		_height = (int)(screen_size.height*0.8);
		
		screen_size.setSize(_width, _height);
		total.setSize(screen_size);
		total.setPreferredSize(screen_size);
		
		createMenu();
		createControls();
		
		_library = new JScrollPane(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
		_library.setAutoscrolls(true);
		_playlists = new JScrollPane(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);		
		
		_libSize = new Dimension((int)(_width*0.75), (int)(_height-TOP_PNL_HEIGHT-2*BOT_PNL_HEIGHT-20));
		_library.setSize(_libSize);
		_library.setPreferredSize(_libSize);
		
		_plSize = new Dimension((int)(_width*0.25-20), (int)(_height-TOP_PNL_HEIGHT-2*BOT_PNL_HEIGHT-20));
		_playlists.setSize(_plSize);
		_playlists.setPreferredSize(_plSize);
		
		createLibrary();
		createPlaylists();	

		createAdd();
		
		createLocations();
		
		total.add(_controls);
		total_spring.putConstraint(SpringLayout.WEST, _controls, _controls_tl.x, SpringLayout.WEST, total);
		total_spring.putConstraint(SpringLayout.NORTH, _controls, _controls_tl.y, SpringLayout.NORTH, total);		
		
		total.add(_progress);
		total_spring.putConstraint(SpringLayout.EAST, _progress, _progress_tr.x, SpringLayout.EAST, total);
		total_spring.putConstraint(SpringLayout.NORTH, _progress, _progress_tr.y, SpringLayout.NORTH, total);		
				
		total.add(_playlists);
		total_spring.putConstraint(SpringLayout.WEST, _playlists, _playlists_tl.x, SpringLayout.WEST, total);
		total_spring.putConstraint(SpringLayout.NORTH, _playlists, _playlists_tl.y, SpringLayout.NORTH, total);	
		
		
		total.add(_library);
		total_spring.putConstraint(SpringLayout.WEST, _library, _library_tl.x, SpringLayout.WEST, total);
		total_spring.putConstraint(SpringLayout.NORTH, _library, _library_tl.y, SpringLayout.NORTH, total);	

		
		JPanel add_pnl = new JPanel(new FlowLayout());
		add_pnl.add(new JLabel("add ====> "));
		total.add(add_pnl);
		total_spring.putConstraint(SpringLayout.WEST, add_pnl, 0, SpringLayout.WEST, total);
		total_spring.putConstraint(SpringLayout.SOUTH, add_pnl, TOP_PNL_HEIGHT+10, SpringLayout.SOUTH, total);	
		
		total.add(_add);
		total_spring.putConstraint(SpringLayout.EAST, _add, _add_br.x, SpringLayout.EAST, total);
		total_spring.putConstraint(SpringLayout.SOUTH, _add, _add_br.y, SpringLayout.SOUTH, total);	
		
		total.add(_messageBox);
		total_spring.putConstraint(SpringLayout.EAST, _messageBox, _message_br.x, SpringLayout.EAST, total);
		total_spring.putConstraint(SpringLayout.SOUTH, _messageBox, _message_br.y, SpringLayout.SOUTH, total);	
		
		
		setJMenuBar(_menu);
		
		_msgField.setText("welcome to A M U S E.");
		
		total.setFocusable(true);

		add(total);
		
		
		pack();
		setVisible(true);
	}
	
	private void createLocations() {
		//_search_tl, _controls_tl, _progress_tl, _playlists_tl, _library_tl,_add_br;
		_search_tl = new Point(0, 0);
		_controls_tl = new Point((int)(_width/2.0-CONTROL_WIDTH/2.0), 0);
		_progress_tr = new Point(0, (int)(TOP_PNL_HEIGHT/2.0 - TOP_PNL_HEIGHT/4.0));
		_library_tl = new Point((int)(_width*0.25), (int)(TOP_PNL_HEIGHT+10));
		_playlists_tl = new Point(0, (int)(TOP_PNL_HEIGHT+10));
		_add_br = new Point(0, -TOP_PNL_HEIGHT);
		_message_br = new Point(0, 0);
	}

	public void createLibrary() {	
		SpringLayout layout = new SpringLayout(); 
		_libraryDisp = new Library(layout, _libSize);
		
		// build the title panel
		JPanel title_row = new JPanel(new FlowLayout());
		for (int disp = 0; disp < Constants.DISPLAY_TYPES.length; disp++) {
			JTextField txt = null;
			if (disp == Constants.DISPLAY_TYPES.length-1) 
				txt = new JTextField(DB_ENTRY_LEN_LONG);
			else if (disp == Constants.DISPLAY_TYPES.length-2 || disp == 0) 
				txt = new JTextField(DB_ENTRY_LEN_SHORT);
			else
				txt = new JTextField(DB_ENTRY_LEN);
			txt.setText(Constants.DISPLAY_TYPES[disp]);
			txt.setEditable(false);
			title_row.add(txt);
		}
		layout.putConstraint(SpringLayout.WEST, title_row, 0, SpringLayout.WEST, _libraryDisp);
		layout.putConstraint(SpringLayout.NORTH, title_row, 0, SpringLayout.NORTH, _libraryDisp);
		// add the title panel to the panel
		_libraryDisp.add(title_row);
		
		//System.out.println("Building Library '"+_pl.getCurrPlaylist().getName()+"' Panel ...");
		Vector<Song> _songs = _pl.getCurrPlaylist().getSongs();
		
		

		_plLstnr = new AddPlaylistLstnr(_pl, _db, _msgField, this);
		for (int pl_index = 0; pl_index < _songs.size(); pl_index++) {
			Song song = _songs.get(pl_index);
			SongPanel s_panel = new SongPanel(pl_index, _db, _holder, _songs.get(pl_index), _plLstnr);
			//s_panel.addMouseListener(new SelectedLstnr());
			layout.putConstraint(SpringLayout.WEST, s_panel, 0, SpringLayout.WEST, _libraryDisp);
			layout.putConstraint(SpringLayout.NORTH, s_panel, (pl_index+1)*DB_ENTRY_HEIGHT, SpringLayout.NORTH, _libraryDisp);
			if (Integer.parseInt(song.getID()) == _holder.getSelectedId())
				s_panel.setFields(SELECTED_COLOR);
			_libraryDisp.add(s_panel);
		}
		
		_library.setViewportView(_libraryDisp);
		_library.setAutoscrolls(true);
	}
	
	public AddPlaylistLstnr getPlaylistLstnr() {
		return _plLstnr;
	}
	
	public void createPlaylists() {
		_playDisplay = _pl.createPlaylists();
		_playlists.setViewportView(_playDisplay);
	}
	
	private void createMenu() {
		_menu = new JMenuBar();
		
		JMenu file = new JMenu("file +");
		file.setMnemonic('F');
		
		JMenuItem playlist = new JMenuItem("new playlist.");
		playlist.addActionListener(new NewPlaylistLstnr(_pl, this, _msgField));
		playlist.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		
		JMenuItem quit = new JMenuItem("quit.");
		quit.addActionListener(new QuitLstnr(_db));
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		file.add(playlist);
		file.add(quit);
		_menu.add(file);
		
		JMenu library = new JMenu("library +");
		library.setMnemonic('L');
		
		JMenuItem save = new JMenuItem("save library.");
		save.addActionListener(new SaveLstnr(_db, _msgField));
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		JMenuItem load = new JMenuItem("load library.");
		load.addActionListener(new LoadLstnr(_db, _pl, _msgField, this));
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		
		library.add(save);
		library.add(load);
		_menu.add(library);
	}
	
	private void createControls() {
		_controls = new JPanel(new FlowLayout());
		
		//JButton back = new JButton(new ImageIcon(IMG_PATH+"rewind.png") );
		//JButton play = new JButton(new ImageIcon(IMG_PATH+"play.png"));
		//JButton next = new JButton(new ImageIcon((IMG_PATH+"forward.png")));

		JButton back = new JButton("<<");
		JButton play = new JButton("> \\ ||");
		JButton next = new JButton(">>");
		
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("back");
				_player.prev();
			}
		});

		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("play/pause");
				_player.play();
			}
		});
		
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("next");
				_player.next();
			}
		});
		
		_controls.add(back);
		_controls.add(play);
		_controls.add(next);
	}
	
	private void createProgress() {
		_progress = new JPanel(new FlowLayout()); 
		
		_prog = new JProgressBar(0, PROG_MAX);
		_prog.setValue(PROG_MAX);
		//progress.setStringPainted(true);
		
		_progress.add(_prog);
	}
	
	
	private void createAdd() {
		_add = new JPanel(new FlowLayout());
		
		JLabel title, artist, path;
		title = new JLabel("title |");
		artist = new JLabel("artist |");
		path = new JLabel("path |");
		
		JTextField t_field, a_field, p_field;
		t_field = new JTextField(TITLE_SIZE);
		a_field = new JTextField(ARTIST_SIZE);
		p_field = new JTextField(PATH_SIZE);
		
		JButton add = new JButton(ADD_BTN);
		add.addActionListener(new AddLstnr(t_field, a_field, p_field, this, _db, _pl));
		
		JButton browse = new JButton(FILE_BTN);
		browse.addActionListener(new SelectAddFileLstnr(browse, p_field, this));
		
		_add.add(title); _add.add(t_field);
		_add.add(artist); _add.add(a_field);
		_add.add(path); _add.add(p_field);
		p_field.setEditable(false);
		
		_add.add(browse);
		_add.add(add);
	}
	
	private void createMessageBox() {
		_messageBox = new JPanel(new FlowLayout());
		
		_msgField = new JTextField(MSG_SIZE_CHAR);
		_msgField.setEditable(false);
		_msgField.setBackground(Color.LIGHT_GRAY);
		
		_messageBox.add(_msgField);
	}
	
	public void setMessageBox(String string) {
		_msgField.setText(string);
	}
		
}
