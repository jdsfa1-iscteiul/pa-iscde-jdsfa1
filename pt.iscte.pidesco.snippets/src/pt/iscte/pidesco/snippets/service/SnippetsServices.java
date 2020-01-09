package pt.iscte.pidesco.snippets.service;

import java.io.IOException;
import java.util.ArrayList;

import pt.iscte.pidesco.snippets.model.Snippet;
import pt.iscte.pidesco.snippets.model.SnippetType;

public interface SnippetsServices {
	/**
	 * Returns true if a snippet with the specified name exists. Else returns false;
	 * @param snippetName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */ 
	
	boolean isSnippet(String snippetName) throws ClassNotFoundException, IOException ;
	
	/**
	 * Returns a list of snippets that have a name starting with the specified chars - to be called by autocomplete
	 * @param startingChars
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	
	ServiceOperationResult<ArrayList<Snippet>> getSnippetsStartingWith(String startingChars) throws ClassNotFoundException, IOException;
	
	/**
	 * Add a new snippet to the 'database'
	 * @param snippet
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	
	ServiceOperationResult<Boolean> saveNewSnippet(SnippetType snippetType, String snippetName, String content) throws IOException, ClassNotFoundException;
	
	/**
	 * Deletes an existing snippet
	 * @param snippet
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	ServiceOperationResult<Boolean> deleteSnippetByName(String snippetName);
	
	
	/**
	 * Inserts a snippet at the cursor location by name
	 * @param snippetName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	
	ServiceOperationResult<Snippet> insertSnippetAtCursorByName(String snippetName) throws ClassNotFoundException, IOException;
	
	/**
	 * Adds a Snippets listener. If the listener already exists, does nothing.
	 * @param listener (non-null) reference to a listener.
	 */
	void addListener(SnippetsListener listener);

	/**
	 * Removes a Snippets listener. If the listener is not registered, does nothing.
	 * @param listener (non-null) reference to a listener.
	 */
	void removeListener(SnippetsListener listener);
}
