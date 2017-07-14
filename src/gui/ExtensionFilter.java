package gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

class ExtensionFilter extends FileFilter {
	
	private String[] extensions;
	private String descr;
	
	ExtensionFilter(String descr, String allowedExtension) {
		this(descr, new String[]{allowedExtension});
	}
	
	ExtensionFilter(String descr, String[] allowedExtensions) {
		this.extensions = allowedExtensions;
		this.descr = descr;
	}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		String extension = getExtension(f);
		for(String ext : extensions)
			if(extension.equals(ext))
				return true;
		return false;
	}

	@Override
	public String getDescription() {
		String d = descr + " (";
		for(int i = 0 ; i < extensions.length - 1 ; i++)
			d += "*." + extensions[i] + ", ";
		if(extensions.length > 0)
			d += "*." + extensions[extensions.length - 1];
		d += ")";
		return d;
	}
	
	static String getExtension(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

}
