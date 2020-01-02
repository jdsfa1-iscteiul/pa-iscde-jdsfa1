package pt.iscte.pidesco.snippets.model;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SnippetGroup implements SnippetChildren {
	
	private boolean isSuperParent;
	private SnippetGroup parent;
	private SnippetType groupType;
	private String name;
	private SortedSet<SnippetChildren> children;
	

	private static class SnippetSorter implements Comparator<SnippetChildren> {
		@Override
		public int compare(SnippetChildren a, SnippetChildren b) {
			return 1;
		}
	}
	
	public SnippetGroup(boolean isSuperParent, SnippetGroup parent, SnippetType groupType, String name) {
		super();
		this.isSuperParent = isSuperParent;
		this.parent = parent;
		this.groupType = groupType;
		this.name = name;
		this.children = new TreeSet<SnippetChildren>(new SnippetSorter());
	}

	public SnippetGroup getParent() {
		return parent;
	}

	public void setParent(SnippetGroup parent) {
		this.parent = parent;
	}

	public boolean isSuperParent() {
		return isSuperParent;
	}

	public void setSuperParent(boolean isSuperParent) {
		this.isSuperParent = isSuperParent;
	}
	
	public SnippetType getGroupType() {
		return this.groupType;
	}

	public void setGroupType(SnippetType groupType) {
		this.groupType = groupType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SortedSet<SnippetChildren> getChildren() {
		return children;
	}

	public void setChildren(SortedSet<SnippetChildren> children) {
		this.children = children;
	}
	
	public String toString() {
		return this.getName();
	}
	
	public void addChildren(SnippetChildren child) {
		children.add(child);
	}
	
}