package pt.iscte.pidesco.snippets.service;

import pt.iscte.pidesco.snippets.model.Snippet;

public interface SnippetsListener {
	
	/**
	 * 
	 * @param snippet
	 */
	
	default void doubleClick(Snippet snippet) { }

}
