package pt.iscte.pidesco.snippets.service;

import java.io.IOException;

import pt.iscte.pidesco.snippets.model.Snippet;

public interface SnippetsServices {
	/**
	 * 
	 * @param snippetName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	
	boolean isSnippet(String snippetName) throws ClassNotFoundException, IOException ;
	
	/**
	 * 
	 * @param snippet
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	
	ServiceOperationResult saveNewSnippet(Snippet snippet) throws IOException, ClassNotFoundException;
	
	/**
	 * 
	 * @param snippet
	 * @return
	 */
	
	ServiceOperationResult insertSnippetAtCursor(Snippet snippet);
	
	/**
	 * 
	 * @param snippetName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	
	ServiceOperationResult insertSnippetAtCursorByName(String snippetName) throws ClassNotFoundException, IOException;
	
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
