package pt.iscte.pidesco.snippets.internal;

import org.eclipse.jface.viewers.ITreeContentProvider;

import pt.iscte.pidesco.snippets.model.Snippet;
import pt.iscte.pidesco.snippets.model.SnippetGroup;

public class TreeContentProvider implements ITreeContentProvider {
	
	private static final Object[] EMPTY = new Object[0];
	
    @Override
    public boolean hasChildren(Object element) {
        if(element instanceof SnippetGroup) {
        	SnippetGroup s = (SnippetGroup) element;
        	return (s.getChildren().size() > 0);
        }
    	return false;
    }

    @Override
    public Object getParent(Object element) {
    	if(element instanceof Snippet) {
        	Snippet s = (Snippet) element;
        	return s.getParent();
        }
    	return null;
    }

    @Override
    public Object[] getElements(Object inputElement) {
    	return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parentElement) {
    	if (parentElement instanceof SnippetGroup)
			return ((SnippetGroup)parentElement).getChildren().toArray();
		else
			return EMPTY;
    }
}