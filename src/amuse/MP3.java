package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: MP3
 * Purpose:	This class is an extension of the thread class, which means
 * that it is self-relient and it can run on its own.  however, this
 * thread doesn't know about any other threads, so it must be controlled
 * by an executor pattern (see AmusePlayer).  
 * 
 * This class is powered by the three extraneous libraries <*.jar> 
 * included in the build path which allow us to acquire a line in stream
 * from a valid mp3 file and feed it to the systems line out stream.
 * 
 * the run method enters a while loop that will continue to read from
 * the line in to the line out until the stream is paused (pause/play button),
 * the song is changed (combination of differently selected song and currently
 * playing song and pause/play button), or the line in stream ends.
 * 
 */

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.JProgressBar;

public class MP3 extends Thread implements Constants {
	
	private String _path;
	private int _id;
	private boolean _paused, _quit, _exited;
	private AmuseGui _gui;
	private SourceDataLine _lineOut;
	private AudioInputStream _lineIn;
	private File _mp3;
	private JProgressBar _prog;
	
	public MP3(String path, int id, AmuseGui gui, JProgressBar prog, boolean exited) {
		_path = path;
		if (_path != null && !_path.equals(""))
			_mp3 = new File(_path);
		_id = id;
		_paused = false;
		_gui = gui;
		_quit = false;
		_prog = prog;
		_exited = exited;
	}
	
	public void togglePause() {
		_paused = !_paused;
	}
	
	public long getFileSize() {
		return _mp3.length();
	}
	
	public boolean isPaused() {
		return _paused;
	}
	
	public int getID() {
		return _id;
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
		//System.out.println("running");
		if (_mp3 != null) {
			_lineIn = null;
			AudioInputStream in;
			try {
				in = AudioSystem.getAudioInputStream(_mp3);
				
				AudioFormat baseFormat = in.getFormat();
				AudioFormat decodedFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
						baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
						false);
				_lineIn = AudioSystem.getAudioInputStream(decodedFormat, in);
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
				
				SourceDataLine _lineOut = (SourceDataLine) AudioSystem.getLine(info);
				
				if(_lineOut != null) {
					_lineOut.open(decodedFormat);
					byte[] data = new byte[4096];
					// Start
					_lineOut.start();
					
					int nBytesRead = 0;
					while (nBytesRead != -1) {	
						if (_quit) {
							_lineOut.flush();
							break;
						}
						if (!_paused) {
							if (!_prog.isIndeterminate())
								_prog.setIndeterminate(true);
							nBytesRead = _lineIn.read(data, 0, data.length);
							_lineOut.write(data, 0, nBytesRead);
							//System.out.println("writing");
						} else {
							_lineOut.flush();
							_prog.setIndeterminate(false);
						}
					}
					stopMP3();
				}	
			} catch (Exception e) {
				/** to be printed out to the gui **/
				e.printStackTrace();
				System.err.println(e.getMessage());
				
				_gui.setMessageBox("<SONG ERROR>: Song type not supported.");
			}
		}
		_exited = true;
	}

	@SuppressWarnings("deprecation")
	private void stopMP3() {
		if (_lineOut != null && _lineIn != null) {
			_lineOut.drain();
			_lineOut.stop();
			_lineOut.close();
		
			try {
				_lineIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean getExited() {
		return _exited;
	}
	
	public void quit() {
		_quit = true;
	}
}
