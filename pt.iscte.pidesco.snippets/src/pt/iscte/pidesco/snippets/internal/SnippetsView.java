package pt.iscte.pidesco.snippets.internal;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.swt.widgets.TreeItem;

import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.snippets.model.Snippet;
import pt.iscte.pidesco.snippets.model.SnippetGroup;
import pt.iscte.pidesco.snippets.model.SnippetType;
import pt.iscte.pidesco.snippets.service.SnippetsServices;

public class SnippetsView implements PidescoView{
	
	private TreeViewer tree;
	private Image snippetGroupImage;
	private Image snippetImage;
	private SnippetsServices services;
	
	public SnippetsView() {
		services = new SnippetsServicesImplementation(SnippetsActivator.getInstance().getFileManager());
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
	    
	    Button enter1 = new Button(viewArea, SWT.PUSH);
	    enter1.setText("Delete Snippet");
	    GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_END);
	    gridData2.horizontalSpan = 3;
	    enter1.setLayoutData(gridData2);
	    enter1.addListener(SWT.Selection, new Listener() {
	    	
			@Override
			public void handleEvent(Event event) {
				IStructuredSelection s = (IStructuredSelection) tree.getSelection();
				if(s.size() == 1 && (s.getFirstElement() instanceof Snippet)) {
					Snippet snippetSelected = (Snippet)s.getFirstElement();
					services.deleteSnippetByName(snippetSelected.getName());
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
				try {
					services.saveNewSnippet(type, snippetName.getText(), snippetContent.getText());
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
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
						services.insertSnippetAtCursorByName(snippetSelected.getName());
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
}
