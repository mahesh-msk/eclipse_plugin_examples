package com.ancit.xtend2java.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.internal.xpand2.ast.Template;
import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.xtend.ast.AbstractExtensionDefinition;
import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.internal.xtend.xtend.ast.ExtensionFile;
import org.eclipse.internal.xtend.xtend.ast.ExtensionImportStatement;
import org.eclipse.internal.xtend.xtend.ast.NamespaceImportStatement;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.FindReplaceAction;
import org.eclipse.xpand.ui.core.IXpandResource;
import org.eclipse.xpand.ui.editor.XpandEditor;
import org.eclipse.xtend.shared.ui.Activator;
import org.eclipse.xtend.shared.ui.core.IXtendXpandResource;
import org.eclipse.xtend.ui.core.IXtendResource;

import com.ancit.xtend2java.gen.model.OAWModel;

public class XPTToJavaView extends ViewPart implements ISelectionListener {
	public XPTToJavaView() {
	}

	private String objectNameReplace;
	private TextViewer textViewer;
	private ArrayList<String> arrayBukets = new ArrayList<String>();
	private ArrayList<String> variableBukets = new ArrayList<String>();
	private HashMap<String, OAWModel> loadMethods = ReadCSVData.readCSV();;
	private boolean isThis = false;
	private XpandEditor xpandEditor;
	private StringBuffer xpandContent;
	private IEditorInput input;
	private Template xr;

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (IFindReplaceTarget.class.equals(adapter)) {
			if (textViewer != null) {
				return (T) textViewer.getFindReplaceTarget();
			}
		}
		return super.getAdapter(adapter);
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		composite.setLayout(new GridLayout(1, false));

		Group group = new Group(composite, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new GridLayout(4, false));
		group.setText("Generate Class");

		Button buttonActionClass = new Button(group, SWT.RADIO);
		buttonActionClass.setText("ActionClass");

		Combo combo = new Combo(group, SWT.DROP_DOWN);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button buttonUtilClass = new Button(group, SWT.RADIO);
		buttonUtilClass.setText("UtilClass");

		Button buttonGenerate = new Button(group, SWT.PUSH);
		buttonGenerate.setText("Generate");

		textViewer = new TextViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP | SWT.READ_ONLY);
		textViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		textViewer.getFindReplaceTarget();
		buttonGenerate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				if (null != xpandContent && null != xr && xpandContent.capacity() == 16) {
					if (!combo.getText().isEmpty() && buttonActionClass.getSelection()) {
						// xtendContent.append("package src-java;");

//						handleNsImports(xpandContent, xr);

//						handleExtImports(xpandContent, xr);

						arrayBukets.clear();
						variableBukets.clear();

//						List<Extension> extensions = handleExtMethods(xr, combo.getText());
//						for (int i = 0; i < variableBukets.size(); i++) {
//							xpandContent.append(variableBukets.get(i));
//						}

						xpandContent.append("\n}");
						Document doc = new Document(xpandContent.toString());
						textViewer.setDocument(doc);

					} else if (buttonUtilClass.getSelection()) {
						// xtendContent.append("package src-java;");

//						handleNsImports(xpandContent, xr);

//						handleExtImports(xpandContent, xr);

						String name = input.getName();
						xpandContent.append(
								"\npublic class " + name.substring(0, name.indexOf(".")).replaceAll("_", "") + " {");

						arrayBukets.clear();
						variableBukets.clear();

//						List<Extension> extensions = handleExtMethods(xr, null);
//						for (int i = 0; i < variableBukets.size(); i++) {
//							xpandContent.append(variableBukets.get(i));
//						}

						for (int i = 0; i < arrayBukets.size(); i++) {
							if (!arrayBukets.get(i).trim().startsWith("int ")) {
								xpandContent.append(arrayBukets.get(i));
							}
						}
						// Pattern.compile("_").matcher(xtendContent).replaceAll("");
						// System.out.println(extensions);
						xpandContent.append("\n}");
						Document doc = new Document(xpandContent.toString());
						textViewer.setDocument(doc);
					}
				}
			}
		});

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
				if (part instanceof XpandEditor) {

					xpandEditor = (XpandEditor) part;
					input = ((IEditorPart) xpandEditor).getEditorInput();
					System.out.println(input);

					xpandContent = new StringBuffer();

					IStorage file = input.getAdapter(IStorage.class);
					// somehow AdapterManager returns null for IStorage, so fall back to
					// IFile
					if (file == null) {
						file = input.getAdapter(IFile.class);
					}

					IXtendXpandResource findExtXptResource = Activator.getExtXptModelManager().findExtXptResource(file);

//					if (findExtXptResource instanceof IXpandResource) {
//						xr = (Template) ((IXpandResource) findExtXptResource).getExtXptResource();
//
//						List<String> names = xr.getExtensions().stream().map(Extension::getName)
//								.collect(Collectors.toList());
//
//						combo.setItems(names.toArray(new String[] {}));
//
//					}

				}
			}
		});

		activateFindReplaceAction();
	}

	private void activateFindReplaceAction() {
		FindReplaceAction findAction = new FindReplaceAction(
				ResourceBundle.getBundle("org.eclipse.ui.texteditor.ConstructedTextEditorMessages"), null, this);
		IHandlerService handlerService = getSite().getService(IHandlerService.class);
		IHandler handler = new AbstractHandler() {
			public Object execute(ExecutionEvent event) throws ExecutionException {
				if (textViewer != null && textViewer.getDocument() != null)
					findAction.run();
				return null;
			}
		};
		handlerService.activateHandler("org.eclipse.ui.edit.findReplace", handler);
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sSelection = (IStructuredSelection) selection;
			System.out.println(sSelection.getFirstElement());

		}
	}

	private void handleNsImports(StringBuffer xtendContent, ExtensionFile xr) {
		List<NamespaceImportStatement> nsImports = xr.getNsImports();
		for (NamespaceImportStatement namespaceImportStatement : nsImports) {
			// xtendContent.append(
			// "\nimport " +
			// namespaceImportStatement.getImportedId().toString().replaceAll("::", ".") +
			// ".*;");
		}
	}

	private void handleExtImports(StringBuffer xtendContent, ExtensionFile xr) {
		List<ExtensionImportStatement> extImports = xr.getExtImports();
		for (ExtensionImportStatement extensionImportStatement : extImports) {
			// xtendContent.append(
			// "\nimport " +
			// extensionImportStatement.getImportedId().toString().replaceAll("::", ".") +
			// ".*;");
		}
	}

	boolean isTry = false;

	private List<Extension> handleExtMethods(/* StringBuffer xtendContent, */ ExtensionFile xr, String methodName) {
		List<Extension> extensions = xr.getExtensions();
		for (Extension extension : extensions) {

			if (methodName != null && methodName.equals(extension.getName().toString())) {
			} else {
			}
			if (!isTry) {
				arrayBukets.add("\n\t try{\n");
			} else {
				isTry = false;
			}
			AbstractExtensionDefinition abstractExtensionDefinition = (AbstractExtensionDefinition) extension;
			Expression expression = abstractExtensionDefinition.getExpression();

		}
		return extensions;
	}
}
