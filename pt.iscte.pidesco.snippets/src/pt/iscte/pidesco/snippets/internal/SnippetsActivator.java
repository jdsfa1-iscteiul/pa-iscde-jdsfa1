package pt.iscte.pidesco.snippets.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.snippets.model.Snippet;
import pt.iscte.pidesco.snippets.model.SnippetGroup;
import pt.iscte.pidesco.snippets.model.SnippetType;
import pt.iscte.pidesco.snippets.service.SnippetsListener;
import pt.iscte.pidesco.snippets.service.SnippetsServices;

public class SnippetsActivator implements BundleActivator {

	private static SnippetsActivator instance;
	private static BundleContext context;
	private SnippetsFileManager snippetsFileManager;
	private ArrayList<Snippet> snippetsList;
	private SnippetGroup root;
	private JavaEditorServices javaEditorServices;
	private Set<SnippetsListener> listeners;
	private ServiceRegistration<SnippetsServices> service;

	static BundleContext getContext() {
		return context ;
	} 
 
	public void start(BundleContext bundleContext) throws Exception {
		instance = this; 
		listeners = new HashSet<>();
		
		SnippetsActivator.context = bundleContext;
		String path = "/Users/joaoduarte/Documents/ISCTE/PA/git/pt.iscte.pidesco.snippets/pt.iscte.pidesco.snippets/snippets";
		this.snippetsFileManager = new SnippetsFileManager(path);
		
		service = context.registerService(SnippetsServices.class, new SnippetsServicesImplementation(this.snippetsFileManager), null);
		
		ServiceReference<JavaEditorServices> ref = bundleContext.getServiceReference(JavaEditorServices.class);
		if(ref != null) {
			javaEditorServices = context.getService(ref);
		}
		
		readSnippetsFile();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		SnippetsActivator.context = null;
		service.unregister();
		listeners.clear();
	}

	private void readSnippetsFile() throws ClassNotFoundException, IOException {
		this.snippetsList = snippetsFileManager.readSnippetsFromDir();
		organizeSnippetsByType();
	}
	
	private void organizeSnippetsByType() {
		this.root = new SnippetGroup(true, null, null, null);
		SnippetGroup cond = new SnippetGroup(false, root, SnippetType.conditional, "Conditional");
		SnippetGroup cyc = new SnippetGroup(false, root, SnippetType.cycle, "Cycle");
		SnippetGroup cus = new SnippetGroup(false, root, SnippetType.custom, "Custom");
		
		for (Snippet snippet : snippetsList) 
		{  
		    switch(snippet.getType()) {
		    	case conditional:
		    		snippet.setParent(cond);
		    		cond.addChildren(snippet);
		    		break;
		    	case cycle:
		    		snippet.setParent(cyc);
		    		cyc.addChildren(snippet);
		    		break;
		    	case custom:
		    		snippet.setParent(cus);
		    		cus.addChildren(snippet);
		    		break;
		    };
		}
		
		root.addChildren(cond);
		root.addChildren(cyc);
		root.addChildren(cus);
		
	}
	
	public static SnippetsActivator getInstance() {
		return instance;
	}
	
	public SnippetGroup getRoot() throws ClassNotFoundException, IOException {
		readSnippetsFile();
		return this.root;
	}
	
	public ArrayList<Snippet> getSnippets() throws ClassNotFoundException, IOException{
		readSnippetsFile();
		return this.snippetsList;
	}
	
	public SnippetsFileManager getFileManager() {
		return this.snippetsFileManager;
	}

	public JavaEditorServices getJavaEditorServices() {
		return javaEditorServices;
	}

	public void setJavaEditorServices(JavaEditorServices javaEditorServices) {
		this.javaEditorServices = javaEditorServices;
	}
	
	public void addListener(SnippetsListener l) {
		listeners.add(l);
	}

	public void removeListener(SnippetsListener l) {
		listeners.remove(l);
	}
	
	public Set<SnippetsListener> getListeners(){
		return listeners;
	}
	
	public ServiceReference<SnippetsServices> getServiceRef(){
		return this.service.getReference();
	}
	
}

