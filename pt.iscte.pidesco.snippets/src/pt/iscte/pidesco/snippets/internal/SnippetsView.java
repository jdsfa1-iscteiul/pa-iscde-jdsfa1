package pt.iscte.pidesco.snippets.internal;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.snippets.model.Snippet;
import pt.iscte.pidesco.snippets.model.SnippetGroup;
import pt.iscte.pidesco.snippets.model.SnippetType;
import pt.iscte.pidesco.snippets.service.SnippetsListener;
import pt.iscte.pidesco.snippets.service.SnippetsServices;

public class SnippetsView implements PidescoView{
	
	private TreeViewer tree;
	private Image snippetGroupImage;
	private Image snippetImage;
	private SnippetsServices snippetsService;
	
	public SnippetsView() {
		ServiceReference<SnippetsServices> ref = SnippetsActivator.getInstance().getServiceRef();
		if(ref != null) {
			snippetsService = SnippetsActivator.getContext().getService(ref);
		}
	}

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		
		snippetImage = imageMap.get("snippet2.png");
		snippetGroupImage = imageMap.get("snippet.png");
		viewArea.setSize(500, 500);
		tree = new TreeViewer(viewArea, SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setContentProvider(new TreeContentProvider());
		
		TreeViewerColumn viewerColumn = new TreeViewerColumn(tree, SWT.NONE);
	    viewerColumn.getColumn().setWidth(250);
		
		tree.setLabelProvider(new LabelProvider() {
			@Override
	        public String getText(Object element) {
	            return element.toString();
	        }
			
			@Override
			public Image getImage(Object element) {
				if (element instanceof Snippet) {
					return snippetImage;
				} else if (element instanceof SnippetGroup) {
					return snippetGroupImage;
				}
				return null;
			}
			
		});
		
		constructForm(viewArea);
		viewArea.pack();
		
		addDoubleClickListener();
		try {
			refresh();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void constructForm(Composite viewArea) {
		GridLayout gridLayout = new GridLayout();

	    gridLayout.numColumns = 1;

	    viewArea.setLayout(gridLayout);
	    
	    Button insertButton = new Button(viewArea, SWT.PUSH);
	    insertButton.setText("Use Snippet");
	    GridData gridData3 = new GridData(GridData.HORIZONTAL_ALIGN_END);
	    gridData3.horizontalSpan = 3;
	    insertButton.setLayoutData(gridData3);
	    insertButton.addListener(SWT.Selection, new Listener() {
	    	
			@Override
			public void handleEvent(Event event) {
				IStructuredSelection s = (IStructuredSelection) tree.getSelection();
				if(s.size() == 1 && (s.getFirstElement() instanceof Snippet)) {
					Snippet snippetSelected = (Snippet)s.getFirstElement();
					try {
						snippetsService.insertSnippetAtCursorByName(snippetSelected.getName());
						for(SnippetsListener l : SnippetsActivator.getInstance().getListeners()) {
							l.snippetUsed(snippetSelected);
						}
						refresh();
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
			}
	    	
	    });
	    
	    Button deleteButton = new Button(viewArea, SWT.PUSH);
	    deleteButton.setText("Delete Snippet");
	    GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_END);
	    gridData2.horizontalSpan = 3;
	    deleteButton.setLayoutData(gridData2);
	    deleteButton.addListener(SWT.Selection, new Listener() {
	    	 
			@Override
			public void handleEvent(Event event) {
				IStructuredSelection s = (IStructuredSelection) tree.getSelection();
				if(s.size() == 1 && (s.getFirstElement() instanceof Snippet)) {
					Snippet snippetSelected = (Snippet)s.getFirstElement();
					snippetsService.deleteSnippetByName(snippetSelected.getName());
					for(SnippetsListener l : SnippetsActivator.getInstance().getListeners()) {
						l.snippetDeleted(snippetSelected);
					}
				}
				
				try {
					refresh();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
	    	
	    });
	    
	    new Label(viewArea, SWT.NULL).setText("Name:");
	    Text snippetName = new Text(viewArea, SWT.SINGLE | SWT.BORDER);
	    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	    gridData.horizontalSpan = 3;
	    snippetName.setLayoutData(gridData);
	    
	    new Label(viewArea, SWT.NULL).setText("Type:");
	    Combo comboBox = new Combo(viewArea, SWT.NULL);
	    comboBox.setItems(new String [] {SnippetType.conditional.toString(), SnippetType.cycle.toString(), SnippetType.custom.toString()});
	    comboBox.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
	    
	    new Label(viewArea, SWT.NULL).setText("Content:");
	    Text snippetContent = new Text(viewArea, SWT.MULTI | SWT.BORDER);
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	    gridData.horizontalSpan = 3;
	    gridData.heightHint= 100;
	    snippetContent.setLayoutData(gridData);
	    
	    Button enter = new Button(viewArea, SWT.PUSH);
	    enter.setText("Save Snippet");
	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
	    gridData.horizontalSpan = 3;
	    enter.setLayoutData(gridData);
	    enter.addListener(SWT.Selection, new Listener() {
	    	
			@Override
			public void handleEvent(Event event) {
				SnippetType type = null;
				String typeString = comboBox.getText();
				switch(typeString) {
					case "conditional":
						type = SnippetType.conditional;
						break;
					case "cycle":
						type = SnippetType.cycle;
						break;
					case "custom":
						type = SnippetType.custom;
						break;
				}
				if(snippetName.getText() != null && snippetContent.getText() != null && type != null) {
					try {
						snippetsService.saveNewSnippet(type, snippetName.getText(), snippetContent.getText());
						for(SnippetsListener l : SnippetsActivator.getInstance().getListeners()) {
							l.snippetSaved(new Snippet(type, snippetName.getText(), snippetContent.getText()));
						}
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				snippetName.setText("");
				snippetContent.setText("");
				try {
					refresh();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
	    	
	    });
	    
	}
	
	public void refresh() throws ClassNotFoundException, IOException {
		SnippetGroup root = SnippetsActivator.getInstance().getRoot();
		tree.setInput(root);
		tree.expandAll();
		tree.refresh();
	}
	
	private void addDoubleClickListener() {
		tree.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection s = (IStructuredSelection) tree.getSelection();
				if(s.size() == 1 && (s.getFirstElement() instanceof Snippet)) {
					Snippet snippetSelected = (Snippet)s.getFirstElement();
					try {
						snippetsService.insertSnippetAtCursorByName(snippetSelected.getName());
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					for(SnippetsListener l : SnippetsActivator.getInstance().getListeners()) {
						l.snippetUsed(snippetSelected);
					}
				}
			}
		});
	}
	
}
