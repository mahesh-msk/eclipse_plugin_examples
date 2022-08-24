package com.ancit.xtend2java.gen.view;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.internal.xtend.xtend.ast.ExtensionFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtend.shared.ui.Activator;
import org.eclipse.xtend.shared.ui.core.IXtendXpandResource;
import org.eclipse.xtend.ui.core.IXtendResource;
import org.eclipse.xtend.ui.editor.XtendEditor;

public class OAWViewPart extends ViewPart {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtNewText;
	
	private ExtensionFile xr;

	public OAWViewPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		
		Section sctnNewSection = formToolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText("New Section");
		sctnNewSection.setExpanded(true);
		
		SashForm sashForm = new SashForm(sctnNewSection, SWT.NONE);
		formToolkit.adapt(sashForm);
		formToolkit.paintBordersFor(sashForm);
		sctnNewSection.setClient(sashForm);
		
		TreeViewer treeViewer = new TreeViewer(sashForm, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		formToolkit.paintBordersFor(tree);
		
//		TreeColumn signalColumns = new TreeColumn(tree, SWT.CENTER);
//		signalColumns.setAlignment(SWT.LEFT);
//		signalColumns.setText("Signal Name");
//		signalColumns.setWidth(155);
		
		txtNewText = formToolkit.createText(sashForm, "New Text", SWT.MULTI);
		
		treeViewer.setContentProvider(new OAWContentProvider());
		treeViewer.setLabelProvider(new OAWLabelProvider());
		
		
		sashForm.setWeights(new int[] {1, 2});
		
		getSite().getPage().addPartListener(new IPartListener() {

			

			@Override
			public void partOpened(IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partClosed(IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partActivated(IWorkbenchPart part) {

				if (part instanceof XtendEditor) {

					XtendEditor xtendEditor = (XtendEditor) part;
					IEditorInput input = ((IEditorPart) xtendEditor).getEditorInput();
					System.out.println(input);

					StringBuffer xtendContent = new StringBuffer();

					IStorage file = input.getAdapter(IStorage.class);
					// somehow AdapterManager returns null for IStorage, so fall back to
					// IFile
					if (file == null) {
						file = input.getAdapter(IFile.class);
					}

					IXtendXpandResource findExtXptResource = Activator.getExtXptModelManager().findExtXptResource(file);

					if (findExtXptResource instanceof IXtendResource) {
						xr = (ExtensionFile) ((IXtendResource) findExtXptResource).getExtXptResource();

					}
				}
			}
		});
		
		Action action = new Action("Load") {
			@Override
			public void run() {
				treeViewer.setInput(xr);
			}
		};
		getViewSite().getActionBars().getToolBarManager().add(action);
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Object firstElement = ((TreeSelection)event.getStructuredSelection()).getFirstElement();
				if (firstElement instanceof ExpressionModel) {
					ExpressionModel expressionModel = (ExpressionModel) firstElement;
					txtNewText.setText(expressionModel.getExpression().toString());
				} else {
				txtNewText.setText(firstElement.toString());
				}
			}
		});
	

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
