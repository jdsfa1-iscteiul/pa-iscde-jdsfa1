package pt.iscte.pidesco.snippets.service;

import pt.iscte.pidesco.snippets.model.Snippet;

public interface SnippetsListener {
	
	/**
	 * Notifys whenever a snippet is inserted
	 * @param snippet
	 */
	
	default void snippetUsed(Snippet snippet) { }
	
	/**
	 * Notifys whener a snippet is deleted
	 * @param snippet
	 */
	
	default void snippetDeleted(Snippet snippet) { }
	
	/**
	 * Notifys whenever a new snippet is saved
	 * @param snippet  
	 */
	
	default void snippetSaved(Snippet snippet) { }

}
