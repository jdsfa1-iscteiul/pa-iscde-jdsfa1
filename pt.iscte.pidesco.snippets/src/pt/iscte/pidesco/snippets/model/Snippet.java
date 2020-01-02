package pt.iscte.pidesco.snippets.model;

import java.io.Serializable;

public class Snippet implements Serializable, SnippetChildren {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SnippetType type;
	private String name;
	private String content;
	private SnippetGroup parent;
	
	public Snippet(SnippetType type, String name, String content) {
		super();
		this.type = type;
		this.name = name;
		this.content = content;
	}
	

	public SnippetType getType() {
		return type;
	}

	public void setType(SnippetType type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public void setType(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString() {
		return getName();
	}
	
	public void setParent(SnippetGroup parent) {
		this.parent = parent;
	}
	
	public SnippetGroup getParent() {
		return parent;
	}
}
