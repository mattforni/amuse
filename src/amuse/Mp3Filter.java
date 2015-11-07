package amuse;

/** <project> Amuse
 * 
 * <@author mattf>
 * 
 * Class: Mp3Filter
 * Purpose:	very simple class that filters the files
 * seen in the add bar file chooser to be all of type '.mp3',
 * so that only valid mp3 files may be added to the database.
 * 
 */

import java.io.File;
import javax.swing.filechooser.*;

public class Mp3Filter extends FileFilter {

    public boolean accept(File f) {
        String ext = ext(f);
        
        if (f.isDirectory()) {
            return true;
        }

        //System.out.println(ext);
        if (ext.equals(".mp3")) {
                return true;
        } else {
            return false;
        }
    }
    
    public String ext(File f) {
    	String path = f.getAbsolutePath();
    	return path.substring(path.length()-4, path.length());
    }

    //The description of this filter
    public String getDescription() {
        return ".mp3";
    }
}
