package pt.iscte.pidesco.snippets.internal;

import java.io.IOException;
import java.util.ArrayList;

import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.snippets.model.Snippet;
import pt.iscte.pidesco.snippets.service.ServiceOperationResult;
import pt.iscte.pidesco.snippets.service.SnippetsListener;
import pt.iscte.pidesco.snippets.service.SnippetsServices;

public class SnippetsServicesImplementation implements SnippetsServices {
	
	private SnippetsFileReaderWriter writer;
	
	public SnippetsServicesImplementation(SnippetsFileReaderWriter writer){
		this.writer = writer;
	}
	
	@Override
	public boolean isSnippet(String snippetName) throws ClassNotFoundException, IOException {
		Snippet snippet = getSnippetByName(snippetName);
		if(snippet == null) {
			return false;
		}
		return true;
	}

	@Override
	public ServiceOperationResult saveNewSnippet(Snippet snippet) throws IOException, ClassNotFoundException {
		if(isSnippet(snippet.getName())) {
			return ServiceOperationResult.Failure("A snippet with that name already exists");
		}
		if(writer != null) {
			this.writer.writeSnippetInFile(snippet);
			return ServiceOperationResult.Success();
		}
		return ServiceOperationResult.Failure("Ups! Something went wrong");
	}

	@Override
	public ServiceOperationResult insertSnippetAtCursor(Snippet snippet) {
		JavaEditorServices javaEditorServ = SnippetsActivator.getInstance().getJavaEditorServices();
		if(javaEditorServ != null) {
			javaEditorServ.insertTextAtCursor(snippet.getContent());
			return ServiceOperationResult.Success();
		}
		return ServiceOperationResult.Failure("Ups! Something went wrong");
	}

	@Override
	public ServiceOperationResult insertSnippetAtCursorByName(String snippetName) throws ClassNotFoundException, IOException {
		if(isSnippet(snippetName)) {
			Snippet snippet = getSnippetByName(snippetName);
			return insertSnippetAtCursor(snippet);
		}
		return ServiceOperationResult.Failure("Ups! Something went wrong");
	}
			
	private Snippet getSnippetByName(String snippetName) throws ClassNotFoundException, IOException {
		ArrayList<Snippet> snippetsAvailable = SnippetsActivator.getInstance().getSnippets();
		for(Snippet snippet: snippetsAvailable) {
			if(snippet.getName() == snippetName) {
				return snippet;
			}
		}
		return null;
	}

	@Override
	public void addListener(SnippetsListener listener) {
		SnippetsActivator.getInstance().addListener(listener);
	}

	@Override
	public void removeListener(SnippetsListener listener) {
		SnippetsActivator.getInstance().removeListener(listener);
	}
}