package pt.iscte.pidesco.snippets.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import pt.iscte.pidesco.snippets.model.Snippet;

public class SnippetsFileManager {
	private String path;
	
	public SnippetsFileManager(String path) {
		this.path = path;
	}
	
	public ArrayList<Snippet> readSnippetsFromDir() throws ClassNotFoundException, IOException {
		ArrayList<Snippet> snippets = new ArrayList<Snippet>();
		File dir = new File(path);
		for (File file : dir.listFiles()) {
			FileInputStream fi = new FileInputStream(file);
			ObjectInputStream oi = new ObjectInputStream(fi);
			
			Snippet snip = (Snippet) oi.readObject();
		    snippets.add(snip);
		    
		    fi.close();
		    oi.close();
		}
		
		return snippets;
	}
	
	public void writeSnippetInFile(Snippet snippet) throws IOException{
		File file = new File(path+"/"+snippet.getName()+".ser");
		boolean created = file.createNewFile();
		if(created) {
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream o = new ObjectOutputStream(f);

			o.writeObject(snippet);

			o.close();
			f.close();
		}
	}
	
	public boolean deleteSnippetByName(String name) {
		File file = new File(path+"/"+name+".ser");
		return file.delete();
	}

}
