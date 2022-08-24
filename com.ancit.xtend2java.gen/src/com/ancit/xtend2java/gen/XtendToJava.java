package com.ancit.xtend2java.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.internal.xtend.expression.ast.BooleanLiteral;
import org.eclipse.internal.xtend.expression.ast.BooleanOperation;
import org.eclipse.internal.xtend.expression.ast.Case;
import org.eclipse.internal.xtend.expression.ast.Cast;
import org.eclipse.internal.xtend.expression.ast.ChainExpression;
import org.eclipse.internal.xtend.expression.ast.CollectionExpression;
import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.expression.ast.FeatureCall;
import org.eclipse.internal.xtend.expression.ast.GlobalVarExpression;
import org.eclipse.internal.xtend.expression.ast.Identifier;
import org.eclipse.internal.xtend.expression.ast.IfExpression;
import org.eclipse.internal.xtend.expression.ast.IntegerLiteral;
import org.eclipse.internal.xtend.expression.ast.LetExpression;
import org.eclipse.internal.xtend.expression.ast.ListLiteral;
import org.eclipse.internal.xtend.expression.ast.OperationCall;
import org.eclipse.internal.xtend.expression.ast.StringLiteral;
import org.eclipse.internal.xtend.expression.ast.SwitchExpression;
import org.eclipse.internal.xtend.expression.ast.TypeSelectExpression;
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
import org.eclipse.xtend.shared.ui.Activator;
import org.eclipse.xtend.shared.ui.core.IXtendXpandResource;
import org.eclipse.xtend.ui.core.IXtendResource;
import org.eclipse.xtend.ui.editor.XtendEditor;

import com.ancit.xtend2java.gen.model.OAWModel;

public class XtendToJava extends ViewPart implements ISelectionListener {
	public XtendToJava() {
	}

	private String objectNameReplace;
	private TextViewer textViewer;
	private ArrayList<String> arrayBukets = new ArrayList<String>();
	private ArrayList<String> variableBukets = new ArrayList<String>();
	private HashMap<String, OAWModel> loadMethods = ReadCSVData.readCSV();;
	private boolean isThis = false;
	private XtendEditor xtendEditor;
	private StringBuffer xtendContent;
	private IEditorInput input;
	private ExtensionFile xr;

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

				if (null != xtendContent && null != xr && xtendContent.capacity() == 16) {
					if (!combo.getText().isEmpty() && buttonActionClass.getSelection()) {

						handleNsImports(xtendContent, xr);

						handleExtImports(xtendContent, xr);

						xtendContent.append("\nimport com.bosch.bct.javaaction.context.interfaces.IContext;");
						xtendContent.append("\nimport com.bosch.bct.javaaction.context.interfaces.ILog;");

						xtendContent.append("\nimport com.bosch.bct.javaaction.context.interfaces.IJavaAction;");
						// Preparator
						String name = input.getName();
						if (name.substring(name.indexOf("_"), name.length()).toLowerCase().contains("prepare")) {
							xtendContent.append("\n\npublic class " + name.substring(0, name.indexOf("_"))
									+ "Preparator implements IJavaAction" + " {");
						} else {
							xtendContent.append("\n\npublic class " + name.substring(0, name.indexOf("_"))
									+ " implements IJavaAction" + " {");
						}

						arrayBukets.clear();
						variableBukets.clear();

						List<Extension> extensions = handleExtMethods(xr, combo.getText());
						for (int i = 0; i < variableBukets.size(); i++) {
							xtendContent.append(variableBukets.get(i));
						}

						for (int i = 0; i < arrayBukets.size(); i++) {
							if (!arrayBukets.get(i).trim().startsWith("int ")) {
								if (arrayBukets.get(i).trim().equalsIgnoreCase(
										"EcucValues ecucvalues = context.getModel(EcucValues.class);")) {
									String[] splitCondition = arrayBukets.get(i + 2).trim().split("!=");
									arrayBukets.add(1 + i,
											("\n\t" + getFirstCapitailize(splitCondition[0].trim()) + " "
													+ splitCondition[0] + " = ecucvalues.get"
													+ getFirstCapitailize(splitCondition[0].trim()) + "();"));
								}
								xtendContent.append(arrayBukets.get(i));
							}
						}

						// replaceAll(xtendContent);
						// Pattern.compile("_").matcher(xtendContent).replaceAll("");
						System.out.println(extensions);
						xtendContent.append("\n}");
						Document doc = new Document(xtendContent.toString());
						textViewer.setDocument(doc);

					} else if (buttonUtilClass.getSelection()) {
						// xtendContent.append("package src-java;");

						handleNsImports(xtendContent, xr);

						handleExtImports(xtendContent, xr);

						String name = input.getName();
						xtendContent.append(
								"\npublic class " + name.substring(0, name.indexOf(".")).replaceAll("_", "") + " {");

						arrayBukets.clear();
						variableBukets.clear();

						handleExtMethods(xr, null);
						xtendContent.append("\n}");
						Document doc = new Document(xtendContent.toString());
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
				if (part instanceof XtendEditor) {

					xtendEditor = (XtendEditor) part;
					input = ((IEditorPart) xtendEditor).getEditorInput();
					System.out.println(input);

					xtendContent = new StringBuffer();

					IStorage file = input.getAdapter(IStorage.class);
					// somehow AdapterManager returns null for IStorage, so fall back to
					// IFile
					if (file == null) {
						file = input.getAdapter(IFile.class);
					}

					IXtendXpandResource findExtXptResource = Activator.getExtXptModelManager().findExtXptResource(file);

					if (findExtXptResource instanceof IXtendResource) {
						xr = (ExtensionFile) ((IXtendResource) findExtXptResource).getExtXptResource();

						List<String> names = xr.getExtensions().stream().map(Extension::getName)
								.collect(Collectors.toList());

						combo.setItems(names.toArray(new String[] {}));

					}
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
			variableBukets.clear();
			arrayBukets.clear();

			if (methodName != null && methodName.equals(extension.getName().toString())) {
				isTry = true;
				arrayBukets.add("\nprivate IContext context;");
				arrayBukets.add("\nprivate ILog log;");
				arrayBukets.add("\n\n@Override");
				arrayBukets.add("\npublic void run(IContext context) {");
				arrayBukets.add("\n\t try{\n");
				arrayBukets.add("\n\tthis.context = context;");
				arrayBukets.add("\n\tlog  = context.getLog();");
				arrayBukets.add("\n\tEcucValues ecucvalues = context.getModel(EcucValues.class);");
				setObjectNameReplace("");
				setThis(false);
			} else {
				if (extension.getReturnTypeIdentifier() == null) {
					arrayBukets.add("\npublic String " + replaceUnderscore(extension.toString()) + " {");
				} else if (null != extension.getReturnTypeIdentifier()
						&& extension.getReturnTypeIdentifier().toString().contains("[")
						&& extension.getReturnTypeIdentifier().toString().contains("]")) {
					arrayBukets.add("\npublic "
							+ replaceUnderscore(replaceBrackets(extension.getReturnTypeIdentifier().toString())) + " "
							+ extension.getName() + "("
							+ replaceBrackets(extension.getFormalParameters().get(0).toString()) + ") {");
				} else if (null != extension.getReturnTypeIdentifier()
						&& extension.getReturnTypeIdentifier().toString().equalsIgnoreCase("String")
						&& !extension.toString().trim().startsWith("String")) {
					if (extension.isPrivate()) {
						arrayBukets.add("\nprivate String " + replaceUnderscore(extension.toString()) + " {");
					} else {
						arrayBukets.add("\npublic String " + replaceUnderscore(extension.toString()) + " {");
					}

				} else if (null != extension.getReturnTypeIdentifier()
						&& extension.getReturnTypeIdentifier().toString().equalsIgnoreCase("Integer")
						&& !extension.toString().trim().startsWith("Integer")) {
					arrayBukets.add("\npublic Integer" + replaceUnderscore(extension.toString()) + " {");

				} else {
					String replace = extension.toString().replace("Void", "void");
					arrayBukets.add("\npublic " + replaceUnderscore(replace) + " {");
				}
				if (extension.getFormalParameters().size() > 0
						&& null != extension.getFormalParameters().get(0).getType()) {
					if (extension.getFormalParameters().get(0).getName().toString().trim().equalsIgnoreCase("this")
							&& !extension.getFormalParameters().get(0).getType().toString().trim().startsWith("List")) {
						setObjectNameReplace(
								getFirstLowerCase(extension.getFormalParameters().get(0).getType().toString()));
						setThis(true);
					} else if (extension.getFormalParameters().get(0).getType().toString().trim().startsWith("List")) {

						String[] split = extension.getFormalParameters().get(0).getType().toString().trim()
								.split("\\[");
						setObjectNameReplace(
								getFirstLowerCase(split[1].subSequence(0, split[1].length() - 1).toString()));
						setThis(true);

					} else {
						setThis(false);
						setObjectNameReplace(extension.getFormalParameters().get(0).getName().toString().trim());
					}

				} else {
					setThis(false);
					setObjectNameReplace("");
				}

			}
			if (!isTry) {
				arrayBukets.add("\n\t try{\n");
			} else {
				isTry = false;
			}
			AbstractExtensionDefinition abstractExtensionDefinition = (AbstractExtensionDefinition) extension;
			Expression expression = abstractExtensionDefinition.getExpression();
			recursiveExpressionCheck(expression, /* content, */
					extension.getReturnTypeIdentifier());
			arrayBukets.add("\n\t } catch (Exception e) {");
			arrayBukets.add("\n\t\t e.printStackTrace();");
			arrayBukets.add("\n\t }");
			arrayBukets.add("\n}");
			String parentReplacewithbreakets = "";
			for (int i = 0; i < arrayBukets.size(); i++) {
				if (arrayBukets.get(i).trim().contains("this") && !arrayBukets.get(i).trim().contains("this.context")
						&& null != getObjectNameReplace() && !getObjectNameReplace().trim().isEmpty()) {
					if (arrayBukets.get(i).trim().startsWith("public")
							&& (arrayBukets.get(i).trim().startsWith("public String")
									|| arrayBukets.get(i).trim().startsWith("private String")
									|| arrayBukets.get(i).trim().startsWith("public Integer")
									|| arrayBukets.get(i).trim().startsWith("public List")
									|| arrayBukets.get(i).trim().startsWith("public Boolean"))) {

						String replace = arrayBukets.get(i + 2).replace("\n", "");
						if (!arrayBukets.get(i + 2).trim().startsWith("switch")
								&& !arrayBukets.get(i + 2).trim().startsWith("if")
								&& !arrayBukets.get(i + 2).trim().startsWith("Object")) {
							arrayBukets.add(2 + i, "\n return ");
							arrayBukets.set(i + 3, replace);
						}
					}

					if (isThis()) {
						arrayBukets.set(i, arrayBukets.get(i).replace("this", getObjectNameReplace()));
						arrayBukets.set(i, arrayBukets.get(i).replace("getThis()", getObjectNameReplace()));
					}
				}
				if (arrayBukets.get(i).trim().startsWith("}else") && arrayBukets.get(i + 2).trim().startsWith("if")) {
					String replace = arrayBukets.get(i + 2).replace("\n", "");
					arrayBukets.set(2 + i, replace);
				}
				arrayBukets.set(i, arrayBukets.get(i).replace("this", getObjectNameReplace()));
				arrayBukets.set(i, arrayBukets.get(i).replace(".getThis()", getObjectNameReplace()));
				arrayBukets.set(i, arrayBukets.get(i).replace("getThis()", getObjectNameReplace()));
				arrayBukets.set(i, arrayBukets.get(i).replace("..", "."));
				arrayBukets.set(i, arrayBukets.get(i).replace("getAddAll", "addAll"));
				arrayBukets.set(i, arrayBukets.get(i).replace("getaddAll", "addAll"));

				if (isContain(arrayBukets.get(i), "parent")) {
					parentReplacewithbreakets = onlysetGetter(arrayBukets.get(i));
					parentReplacewithbreakets = parentReplacewithbreakets.replace(";", "");
					arrayBukets.set(i, arrayBukets.get(i).replace(arrayBukets.get(i), parentReplacewithbreakets + ";"));
				}
			}
			changeUnderscore();

			for (int i = 0; i < variableBukets.size(); i++) {
				xtendContent.append(variableBukets.get(i));
			}

			for (int i = 0; i < arrayBukets.size(); i++) {
				if (!arrayBukets.get(i).trim().startsWith("int ")) {
					xtendContent.append(arrayBukets.get(i));
				}
			}
		}
		return extensions;
	}

	private String recursiveExpressionCheck(Expression extension, /* StringBuffer buffer1, */
			Identifier returnTypeIdentifier) {
		if (extension instanceof OperationCall) {
			OperationCall operationCall = (OperationCall) extension;
			Expression[] params = operationCall.getParams();
			if (params.length > 0) {
				boolean flag_for_Else = false;
				for (Expression paramexp : params) {
					if (params.length > 0 && (!(paramexp instanceof IntegerLiteral))) {
						if (null != operationCall.getTarget() && (operationCall.getTarget().toString().equals("get")
								|| operationCall.getTarget().toString().equals("set"))) {
							arrayBukets.add("\nreturn " + operationCall.getTarget());
						} else if (paramexp instanceof OperationCall && null != ((OperationCall) paramexp)
								&& ((OperationCall) paramexp).getTarget() instanceof OperationCall
								&& null != ((OperationCall) ((OperationCall) paramexp).getTarget())
								&& (((OperationCall) ((OperationCall) paramexp).getTarget())
										.getTarget()) instanceof OperationCall
								&& null != ((OperationCall) ((OperationCall) ((OperationCall) paramexp).getTarget())
										.getTarget())
								&& ((OperationCall) ((OperationCall) ((OperationCall) paramexp).getTarget())
										.getTarget()).getParams() != null) {
							arrayBukets.add("\t" + operationCall.getTarget() + "."
									+ changeFWDtoCreate(operationCall.getName().toString()) + "("
									+ ((OperationCall) ((OperationCall) ((OperationCall) paramexp).getTarget())
											.getTarget()).getParams()[0].toString().replace("\"", "")
									+ ");".toString());
						} else if (operationCall instanceof OperationCall
								&& operationCall.getTarget() instanceof OperationCall
								&& (((OperationCall) ((OperationCall) operationCall).getTarget()))
										.getTarget() instanceof CollectionExpression) {
							recursiveExpressionCheck(
									((CollectionExpression) (((OperationCall) ((OperationCall) operationCall)
											.getTarget())).getTarget()),
									returnTypeIdentifier);
						} else if (null != operationCall.getTarget()) {
							if (operationCall.getName().toString().contains("indexOf")) {
								arrayBukets
										.add("\n\t\t" + onlysetGetter(operationCall.getTarget().toString()) + "."
												+ changeFWDtoCreate(operationCall.getName().toString()) + "("
												+ onlysetGetter(((operationCall.getParams().length > 0
														? operationCall.getParams()[0]
														: null).toString()))
												+ ");");

							} else if (operationCall.getName().toString().contains("add")) {

								arrayBukets.add("\n\t\t" + onlysetGetter(operationCall.getTarget().toString()) + "."
										+ operationCall.getName().toString() + "("
										+ (operationCall.getParams().length > 0 ? operationCall.getParams()[0] : null)
												.toString()
										+ ");");

							} else {
								arrayBukets.add("\n\t\t" + onlysetGetter(operationCall.getTarget().toString()) + " "
										+ changeFWDtoCreate(operationCall.getName().toString()) + " "
										+ (operationCall.getParams().length > 0 ? operationCall.getParams()[0] : null)
												.toString());
							}
						} else {
							if (paramexp instanceof FeatureCall) {
								flag_for_Else = true;
							}
						}

						if (paramexp instanceof OperationCall) {
							OperationCall operation = (OperationCall) paramexp;
							recursiveExpressionCheck(operation.getTarget(), returnTypeIdentifier);
						}

					} else if (paramexp instanceof IntegerLiteral && operationCall.getTarget() instanceof FeatureCall
							&& ((FeatureCall) operationCall.getTarget()).getTarget() instanceof CollectionExpression) {
						if (((CollectionExpression) ((FeatureCall) operationCall.getTarget()).getTarget()).getName()
								.toString().equalsIgnoreCase("select")) {
							String decodeSortBy = decodeSelect(
									(CollectionExpression) ((FeatureCall) operationCall.getTarget()).getTarget());
							arrayBukets.add(onlysetGetter(
									((CollectionExpression) ((FeatureCall) operationCall.getTarget()).getTarget())
											.getTarget().toString())
									+ ".");
							arrayBukets.add(decodeSortBy);
						}

					} else {
						if (null != operationCall.getTarget()) {
							arrayBukets.add("\t" + onlysetGetter(operationCall.getTarget().toString()) + " "
									+ changeFWDtoCreate(operationCall.getName().toString()) + " "
									+ (operationCall.getParams().length > 0 ? operationCall.getParams()[0] : null)
									+ ";".toString());
						}
					}
					continue;
				}
				if (flag_for_Else) {
					flag_for_Else = false;
					arrayBukets.add(
							operationCall.toString().trim().substring(operationCall.toString().trim().indexOf("_") + 1,
									operationCall.toString().trim().length()));
				}
			} else {
				if (operationCall.toString().trim().contains(".info(\"")) {
					String appliedGetter = getLoggerInfo(operationCall.toString());
					arrayBukets.add(appliedGetter);
				} else if (operationCall.getTarget() instanceof OperationCall && null != operationCall.getTarget()
						&& !((OperationCall) operationCall.getTarget()).getName().toString().equalsIgnoreCase("")
						&& ((OperationCall) operationCall.getTarget()).getName().toString().contains("counterInc")) {

					OperationCall opCall = ((OperationCall) operationCall.getTarget());
					arrayBukets.add("\n \t\t" + opCall.getParams()[0].toString().replace("\"", "") + " ++;");
				} else if (null != operationCall
						.getTarget()/* && !buffer.toString().contains(operationCall.toString()) */) {
					arrayBukets.add(
							operationCall.getName() + "(" + onlysetGetter(operationCall.getTarget().toString()) + ");");
				}

			}
		} else if (extension instanceof LetExpression) {
			LetExpression letExpression = (LetExpression) extension;
			if (letExpression.getName().equals("sortBy")) {
				System.out.println(letExpression.getName());
			} else if (letExpression.getName().equals("select")) {
				System.out.println(letExpression.getName());
			} else if (letExpression.getVarExpression() instanceof FeatureCall) {
				FeatureCall featureCall = (FeatureCall) letExpression.getVarExpression();
				if (featureCall.toString().contains("sortBy")
						&& featureCall.getTarget() instanceof CollectionExpression) {
					arrayBukets.add("\n\t\tObject " + letExpression.getName() + " = ");
					arrayBukets.add("\t" + getFirstLowerCase(getObjectNameReplace()) + ".");
					String decodeSortBy = decodeSortBy((CollectionExpression) featureCall.getTarget());
					arrayBukets.add(decodeSortBy + ";");

				} else if (letExpression.getVarExpression() instanceof CollectionExpression
						&& letExpression.getVarExpression().toString().contains("sortBy")
						&& featureCall.getTarget() instanceof OperationCall) {

					arrayBukets.add("\n\t\tObject " + letExpression.getName() + " = ");
					arrayBukets.add("\t"
							+ onlysetGetter(
									((CollectionExpression) letExpression.getVarExpression()).getTarget().toString())
							+ ".");
					String decodeSortBy = decodeSortBy((CollectionExpression) letExpression.getVarExpression());
					arrayBukets.add(decodeSortBy + ";");

				} else if (letExpression.getVarExpression() instanceof CollectionExpression
						&& letExpression.getVarExpression().toString().contains("selectFirst")
						&& featureCall.getTarget() instanceof FeatureCall) {

					arrayBukets.add("\n\t\tObject " + letExpression.getName() + " = ");
					arrayBukets.add("\t"
							+ onlysetGetter(
									((CollectionExpression) letExpression.getVarExpression()).getTarget().toString())
							+ ".");
					String decodeSortBy = decodeSelectFirst((CollectionExpression) letExpression.getVarExpression());
					arrayBukets.add(decodeSortBy + ";");

				} else if (letExpression.getVarExpression() instanceof CollectionExpression
						&& letExpression.getVarExpression().toString().contains("select")
						&& featureCall.getTarget() instanceof FeatureCall) {
					String decodeSelect = decodeSelect((CollectionExpression) featureCall);
					arrayBukets.add("\n\t\tObject " + letExpression.getName() + " = ");
					arrayBukets.add("\t" + featureCall.getTarget().toString() + ".");
					arrayBukets.add(decodeSelect + ";");
					arrayBukets.add("\n");

				} else if (letExpression.getVarExpression() instanceof FeatureCall
						&& letExpression.getTargetExpression().toString().contains("selectFirst")
						&& letExpression.getTargetExpression() instanceof IfExpression) {

					arrayBukets.add("\n\t\tObject " + letExpression.getName() + " = ");
					arrayBukets.add("\t"
							+ onlysetGetter(((FeatureCall) letExpression.getVarExpression()).getTarget().toString())
							+ ".");
					if (letExpression.getTargetExpression() instanceof IfExpression) {
						String decodeSortBy = onlysetGetter(letExpression.getTargetExpression().toString());
						arrayBukets.add(decodeSortBy + ";");
					} else {
						String decodeSortBy = decodeSelectFirst(
								(CollectionExpression) letExpression.getTargetExpression());
						arrayBukets.add(decodeSortBy + ";");
					}

				} else {
					if (letExpression.getVarExpression() instanceof CollectionExpression) {
						arrayBukets.add("\n\t\tObject " + letExpression.getName() + " = ");
						arrayBukets.add("\t" + onlysetGetter(
								((CollectionExpression) letExpression.getVarExpression()).getTarget().toString())
								+ ".");
						String decodeSortBy = decodeSortBy((CollectionExpression) letExpression.getVarExpression());
						arrayBukets.add(decodeSortBy + ";");

					} else {
						arrayBukets.add("\n\t\tObject " + letExpression.getName() + " = "
								+ onlysetGetter(letExpression.getVarExpression().toString()) + ";");
					}
				}

				if (letExpression.getTargetExpression() instanceof LetExpression) {
					LetExpression letExp = (LetExpression) letExpression.getTargetExpression();
					if (letExp.getVarExpression() instanceof ListLiteral) {
						arrayBukets.add("\n\t\tList<Object> " + letExp.getName() + "= new List<Object>();");
					} else {
						arrayBukets.add("\n\t\tObject " + letExp.getName() + "= null;");
					}
				}

				recursiveExpressionCheck(letExpression.getTargetExpression(), returnTypeIdentifier);

			} else if (letExpression.getTargetExpression() instanceof LetExpression) {

				recursiveExpressionCheck(letExpression.getTargetExpression(), returnTypeIdentifier);
			} else if (letExpression.getTargetExpression() instanceof ChainExpression) {

				if (letExpression.getVarExpression() instanceof ListLiteral
						&& letExpression.getVarExpression().toString().trim().equalsIgnoreCase("{}")) {
					if (letExpression.getVarExpression() instanceof ListLiteral) {
						arrayBukets.add("\n\t\tList<Object> " + letExpression.getName() + "= new List<Object>();");
					} else {
						arrayBukets.add("\n\t\tObject " + letExpression.getName() + "= null;");
					}
				}
				recursiveExpressionCheck(letExpression.getTargetExpression(), returnTypeIdentifier);

			} else if (letExpression.getVarExpression() instanceof ListLiteral) {

				if (letExpression.getVarExpression() instanceof ListLiteral
						&& letExpression.getVarExpression().toString().trim().equalsIgnoreCase("{}")) {
					if (letExpression.getVarExpression() instanceof ListLiteral) {
						arrayBukets.add("\n\t\tList<Object> " + letExpression.getName() + "= new List<Object>();");
					}
				}
				recursiveExpressionCheck(letExpression.getTargetExpression(), returnTypeIdentifier);

			} else {
				recursiveExpressionCheck(letExpression.getVarExpression(), returnTypeIdentifier);
				recursiveExpressionCheck(letExpression.getTargetExpression(), returnTypeIdentifier);
			}
		} else if (extension instanceof FeatureCall) {
			FeatureCall featureCall = (FeatureCall) extension;
			if (featureCall.getName().getFirstSegment().equals("sortBy")) {

				if (!featureCall.getTarget().toString().contains("select")) {
					arrayBukets.add("\n\t\t" + getFirstLowerCase(getObjectNameReplace()) + ".");
				} else {
					recursiveExpressionCheck(featureCall.getTarget(), returnTypeIdentifier);
					if (featureCall.getTarget() instanceof FeatureCall
							&& null != ((FeatureCall) (featureCall.getTarget())).getName()
							&& !((FeatureCall) (featureCall.getTarget())).getName().toString()
									.equalsIgnoreCase("select")) {
						arrayBukets.add(((FeatureCall) (featureCall.getTarget())).getName() + ".");
					}

				}

				String decodeSortBy = decodeSortBy((CollectionExpression) featureCall);
				arrayBukets.add(decodeSortBy + ".");

			} else if (featureCall.getName().getFirstSegment().equals("select")) {

				if (featureCall.getTarget() instanceof FeatureCall
						&& ((FeatureCall) featureCall.getTarget()).getTarget() instanceof CollectionExpression
						&& ((CollectionExpression) ((FeatureCall) featureCall.getTarget()).getTarget()).getName()
								.getFirstSegment().equals("select")) {
					String decodeSelect = decodeSelect(
							(CollectionExpression) ((FeatureCall) featureCall.getTarget()).getTarget());
					arrayBukets.add(
							onlysetGetter((((CollectionExpression) ((FeatureCall) featureCall.getTarget()).getTarget())
									.getTarget().toString())) + ".");
					arrayBukets.add(decodeSelect + ".");
					arrayBukets.add(onlysetGetter(((FeatureCall) featureCall.getTarget()).getName().toString()) + ".");

					decodeSelect = decodeSelect((CollectionExpression) featureCall);
					arrayBukets.add(decodeSelect + ".");
					arrayBukets.add("\n");
				} else {
					String decodeSelect = decodeSelect((CollectionExpression) featureCall);
					arrayBukets.add("\n\t\t" + featureCall.getTarget().toString() + ".");
					arrayBukets.add(decodeSelect + ".");
					arrayBukets.add("\n");
				}

				System.out.println(featureCall.getName());
			} else if (featureCall.getTarget() instanceof CollectionExpression
					&& ((CollectionExpression) featureCall.getTarget()).getName().toString().trim().equals("select")) {
				CollectionExpression collectionObj = (CollectionExpression) featureCall.getTarget();
				String decodeSelect = decodeSelect(collectionObj);
				if (collectionObj.getTarget() instanceof FeatureCall) {
					FeatureCall featureCallObj = (FeatureCall) collectionObj.getTarget();
					arrayBukets.add("\n\t\t" + onlysetGetter(featureCallObj.toString()) + ".");
				} else {
					arrayBukets.add("\n\t\t" + collectionObj.getTarget().toString() + ".");
				}

				arrayBukets.add(decodeSelect + ".");
				arrayBukets.add("\n");
			} else if (featureCall.getTarget() instanceof CollectionExpression
					&& ((CollectionExpression) featureCall.getTarget()).getName().toString().trim()
							.equals("selectFirst")) {
				CollectionExpression collectionObj = (CollectionExpression) featureCall.getTarget();
				String decodeSelect = decodeSelectFirst(collectionObj);
				if (collectionObj.getTarget() instanceof FeatureCall) {
					FeatureCall featureCallObj = (FeatureCall) collectionObj.getTarget();
					arrayBukets.add("\n\t\t" + onlysetGetter(featureCallObj.toString()) + ".");
				} else {
					arrayBukets.add("\n\t\t" + collectionObj.getTarget().toString() + ".");
				}

				arrayBukets.add(decodeSelect + ";");
				arrayBukets.add("\n");
				System.out.println(featureCall.getName());
			} else if (featureCall.getName().getFirstSegment().equalsIgnoreCase("forAll")) {
				decodeForLoop((CollectionExpression) featureCall);
				arrayBukets.add("\n");
			} else if (featureCall.getName().getFirstSegment().equalsIgnoreCase("exists")) {
				if (featureCall.getTarget() instanceof FeatureCall
						&& ((FeatureCall) featureCall.getTarget()).getTarget() instanceof CollectionExpression
						&& ((CollectionExpression) ((FeatureCall) featureCall.getTarget()).getTarget()).getName()
								.getFirstSegment().equalsIgnoreCase("select")) {

					String decodeSelect = decodeSelect(
							(CollectionExpression) ((FeatureCall) featureCall.getTarget()).getTarget());

					arrayBukets.add(
							onlysetGetter(((CollectionExpression) ((FeatureCall) featureCall.getTarget()).getTarget())
									.getTarget().toString()));
					arrayBukets.add(decodeSelect);

					decodeSelect = decodeExistCheck((CollectionExpression) featureCall);
					arrayBukets.add("\n\t\t" + decodeSelect + "");
					arrayBukets.add("\n");
				}else {
					String decodeSelect = decodeExistCheck((CollectionExpression) featureCall);
					arrayBukets.add("\n\t\t" + decodeSelect + "");
					arrayBukets.add("\n");
				}
				

			} else if (null != featureCall.getTarget()) {
				
				if(featureCall.getTarget() instanceof FeatureCall) {
					
					FeatureCall featureTarget = (FeatureCall) featureCall.getTarget();
					if (featureTarget.getTarget() instanceof CollectionExpression) {
						recursiveExpressionCheck(featureTarget.getTarget(), returnTypeIdentifier);
						if (featureTarget instanceof FeatureCall && null != featureTarget.getName()) {
							arrayBukets.add(onlysetGetter(featureTarget.getName().toString()) + ".");
						}
					} else {
						arrayBukets.add(featureCall.getTarget() + "." + featureCall.getName() + ";");
					}
				}else {
					arrayBukets.add(featureCall.getTarget() + "." + featureCall.getName() + ";");
				}
				

			} else {
				arrayBukets.add("\n \t\treturn " + featureCall.getName() + ";");
			}
		} else if (extension instanceof CollectionExpression) {
			CollectionExpression collectionExpression = (CollectionExpression) extension;
			if (collectionExpression.getName().getFirstSegment().equals("sortBy")) {
				System.out.println(collectionExpression.getName());
			} else if (collectionExpression.getName().getFirstSegment().equals("select")) {
				System.out.println(collectionExpression.getName());
			} else {
				recursiveExpressionCheck(collectionExpression.getTarget(), returnTypeIdentifier);
			}
		} else if (extension instanceof TypeSelectExpression) {
			TypeSelectExpression typeSelectExpression = (TypeSelectExpression) extension;
			if (typeSelectExpression.getName().getFirstSegment().equals("sortBy")) {
				System.out.println(typeSelectExpression.getName());
			} else if (typeSelectExpression.getName().getFirstSegment().equals("select")) {
				System.out.println(typeSelectExpression.getName());
			} else {
				recursiveExpressionCheck(typeSelectExpression.getTarget(), returnTypeIdentifier);
			}
		} else if (extension instanceof Cast) {
			Cast cast = (Cast) extension;
			recursiveExpressionCheck(cast.getTarget(), returnTypeIdentifier);
		} else if (extension instanceof IfExpression) {
			IfExpression ifExpression = (IfExpression) extension;
			arrayBukets.add("\n \tif(");
			if (ifExpression.getCondition() instanceof BooleanOperation) {
				recursiveExpressionCheck(ifExpression.getCondition(), returnTypeIdentifier);

			} else if (ifExpression.getCondition() instanceof OperationCall) {
				recursiveExpressionCheck(ifExpression.getCondition(), returnTypeIdentifier);
				if (ifExpression.getCondition().toString().endsWith("size>0")) {
					if (arrayBukets.get(arrayBukets.size() - 1).trim().endsWith("size > 0;")) {
						arrayBukets.set(arrayBukets.size() - 1,
								arrayBukets.get(arrayBukets.size() - 1).toString().replace("size > 0;", "size() > 0"));
					} else if (arrayBukets.get(arrayBukets.size() - 1).trim().endsWith("size > 0")) {
						arrayBukets.set(arrayBukets.size() - 1,
								arrayBukets.get(arrayBukets.size() - 1).toString().replace("size > 0", "size() > 0"));
					} else {
						arrayBukets.add("size() > 0");
					}

				}
			} else if (ifExpression.getCondition() instanceof FeatureCall) {

				FeatureCall tmpFeatureCall = (FeatureCall) ifExpression.getCondition();
				if (null != tmpFeatureCall.getTarget()) {
					arrayBukets.add("(");
					arrayBukets.add("" + onlysetGetter(tmpFeatureCall.getTarget().toString()) + "."
							+ tmpFeatureCall.getName() + "()");
					arrayBukets.add(") ");
				}

			} else {
				// buffer.append(ifExpression.getCondition());
				arrayBukets.add(ifExpression.getCondition().toString());
			}
			arrayBukets.add(" ) {");
			if (null != returnTypeIdentifier && returnTypeIdentifier.toString().equals("Void")) {
				arrayBukets.add("\n\t\t");
			} else {
				arrayBukets.add("\n\t\treturn ");
			}

			if (ifExpression.getThenPart() instanceof Expression) {
				recursiveExpressionCheck(ifExpression.getThenPart(), returnTypeIdentifier);
			} else if (ifExpression.getThenPart() instanceof OperationCall) {
				recursiveExpressionCheck(ifExpression.getThenPart(), returnTypeIdentifier);
			}
			arrayBukets.add(";");

			if (null != ifExpression.getElsePart() && ifExpression.getElsePart() instanceof IfExpression) {
				arrayBukets.add("\n \t}else ");
				recursiveExpressionCheck(ifExpression.getElsePart(), returnTypeIdentifier);

			} else if (null != ifExpression.getElsePart()) {
				arrayBukets.add("\n \t}else{");
				if (null != ifExpression.getElsePart()
						&& ifExpression.getElsePart().toString().trim().contains(".info(\"")) {
					String appliedGetter = getLoggerInfo(ifExpression.getElsePart().toString());
					returnCheck(returnTypeIdentifier, appliedGetter);
				} else if (null != ifExpression.getElsePart()
						&& ifExpression.getElsePart().toString().trim().contains(".error(\"")) {
					String appliedGetter = getLoggerError(ifExpression.getElsePart().toString());
					returnCheck(returnTypeIdentifier, appliedGetter);
				} else if (null != ifExpression.getElsePart()) {
					returnCheck(returnTypeIdentifier, ifExpression.getElsePart().toString());
				}
			}

			arrayBukets.add("\n \t}");
		} else if (extension instanceof SwitchExpression) {
			SwitchExpression switchExp = (SwitchExpression) extension;
			arrayBukets.add("\n \tswitch(" + switchExp.getSwitchExpr() + ") {");
			List<Case> switchcases = switchExp.getCases();
			for (Case cases : switchcases) {
				arrayBukets.add("\n \tcase " + replaceDot(cases.getCondition().toString()) + ":");
				Expression thenPart = cases.getThenPart();
				if (thenPart instanceof IfExpression) {
					recursiveExpressionCheck(thenPart, returnTypeIdentifier);
				} else {
					arrayBukets.add("\n \t\t return " + onlysetGetter(cases.getThenPart().toString()) + ";");
				}
			}
			arrayBukets.add("\n \tdefault :");
			arrayBukets.add("\n \t\t return " + onlysetGetter(switchExp.getDefaultExpr().toString()) + ";");
			arrayBukets.add("\n\t}");
		} else if (extension instanceof ChainExpression) {
			ChainExpression chExpresion = (ChainExpression) extension;

			if (chExpresion.getFirst() instanceof ChainExpression && chExpresion.getNext() instanceof OperationCall) {
				OperationCall chOppCall = (OperationCall) chExpresion.getNext();
				if (null != chOppCall.getTarget()) {
					String tmpTarget = replaceDot(chOppCall.getTarget().toString());
					tmpTarget = replaceWithLoadMethod(tmpTarget);
					tmpTarget = onlysetGetter(tmpTarget);

					arrayBukets.add("\n \t\t\t " + chOppCall.getName() + "(" + tmpTarget + ");");
					arrayBukets.add("\n\t\t\t}");
					arrayBukets.add("\n\t\t}");
				} else {

					Expression[] params = chOppCall.getParams();
					for (Expression expression : params) {
						if (!(expression instanceof IntegerLiteral)) {
							variableBukets.add("\n \t\tint " + (expression.toString().replace("\"", "")) + "= 0;");
						}
					}
				}
				recursiveExpressionCheck(chExpresion.getFirst(), returnTypeIdentifier);
			} else if (chExpresion.getFirst() instanceof OperationCall
					&& chExpresion.getNext() instanceof OperationCall) {
				OperationCall chOppCall = (OperationCall) chExpresion.getNext();
				if (null != chOppCall.getTarget()) {
					String tmpTarget = replaceDot(chOppCall.getTarget().toString());
					tmpTarget = replaceWithLoadMethod(tmpTarget);
					tmpTarget = onlysetGetter(tmpTarget);

					arrayBukets.add("\n \t\t " + chOppCall.getName() + "(" + tmpTarget + ");");
					arrayBukets.add("\n\t\t}");
					arrayBukets.add("\n\t\t\t}");
				}
				OperationCall intParamCall = (OperationCall) chExpresion.getFirst();
				if (null == intParamCall.getTarget()) {
					Expression[] params = intParamCall.getParams();
					for (Expression expression : params) {
						if (!(expression instanceof IntegerLiteral)) {
							variableBukets.add("\n  \t\tint " + (expression.toString().replace("\"", "")) + "= 0;");
						}
					}
				}
			} else if (chExpresion.getFirst() instanceof ChainExpression
					&& chExpresion.getNext() instanceof IfExpression) {

				recursiveExpressionCheck(chExpresion.getFirst(), returnTypeIdentifier);
				recursiveExpressionCheck(chExpresion.getNext(), returnTypeIdentifier);
			} else if (chExpresion.getFirst() instanceof CollectionExpression
					&& chExpresion.getNext() instanceof FeatureCall) {

				decodeAddAll((CollectionExpression) chExpresion.getFirst());

			} else if (chExpresion.getFirst() instanceof IfExpression
					&& chExpresion.getNext() instanceof BooleanLiteral) {

				recursiveExpressionCheck(chExpresion.getFirst(), returnTypeIdentifier);
				recursiveExpressionCheck(chExpresion.getNext(), returnTypeIdentifier);

			} else if (chExpresion instanceof ChainExpression && chExpresion.getFirst() instanceof OperationCall
					&& ((OperationCall) chExpresion.getFirst()).getParams().length > 0
					&& ((OperationCall) chExpresion.getFirst()).getParams()[0] instanceof CollectionExpression) {
				recursiveExpressionCheck(((OperationCall) chExpresion.getFirst()).getParams()[0], returnTypeIdentifier);
			} else {
				String firstChainExp = replaceGetterandbreakets(chExpresion.getFirst());
				String secondChainExp = replaceGetterandbreakets(chExpresion.getNext());
				arrayBukets.add("\n \t\t " + firstChainExp);
				arrayBukets.add("\n \t\t " + secondChainExp);

			}

		} else if (extension instanceof StringLiteral) {
			StringLiteral stringLiteral = (StringLiteral) extension;
			// buffer.append(stringLiteral + ";");
			arrayBukets.add(stringLiteral + ";");
		} else if (extension instanceof BooleanOperation) {
			BooleanOperation boolenExpresion = (BooleanOperation) extension;

			if (boolenExpresion.getLeft() instanceof BooleanOperation
					&& boolenExpresion.getRight() instanceof OperationCall) {

				if (boolenExpresion.getRight() instanceof OperationCall) {
					arrayBukets.add("(");
					OperationCall rightCall = (OperationCall) boolenExpresion.getRight();
					arrayBukets.add("" + onlysetGetter(rightCall.getTarget().toString()) + " " + rightCall.getName()
							+ " " + rightCall.getParams()[0] + "");

					arrayBukets.add(") ");
				}
				arrayBukets.add(" " + boolenExpresion.getOperator() + " ");

				recursiveExpressionCheck(boolenExpresion.getLeft(), returnTypeIdentifier);

			} else if (boolenExpresion.getLeft() instanceof BooleanOperation
					&& boolenExpresion.getRight() instanceof FeatureCall) {

				if (boolenExpresion.getRight() instanceof FeatureCall) {
					arrayBukets.add("(");
					FeatureCall rightCall = (FeatureCall) boolenExpresion.getRight();

					arrayBukets.add(
							"" + onlysetGetter(rightCall.getTarget().toString()) + "." + rightCall.getName() + "()");
					arrayBukets.add(") ");
				}
				arrayBukets.add(" " + boolenExpresion.getOperator() + " ");

				recursiveExpressionCheck(boolenExpresion.getLeft(), returnTypeIdentifier);

			} else if (boolenExpresion.getLeft() instanceof OperationCall
					&& boolenExpresion.getRight() instanceof OperationCall) {

				if (boolenExpresion.getLeft() instanceof OperationCall) {
					arrayBukets.add("( ");
					OperationCall leftCall = (OperationCall) boolenExpresion.getLeft();
					arrayBukets.add("" + onlysetGetter(leftCall.getTarget().toString()) + " " + leftCall.getName() + " "
							+ leftCall.getParams()[0] + "");
					arrayBukets.add(") ");
				}

				arrayBukets.add(" " + boolenExpresion.getOperator() + " ");

				if (boolenExpresion.getRight() instanceof OperationCall) {
					OperationCall rightCall = (OperationCall) boolenExpresion.getRight();
					if (null != rightCall.getTarget()) {
						arrayBukets.add("(");
						arrayBukets.add("" + onlysetGetter(rightCall.getTarget().toString()) + "." + rightCall.getName()
								+ rightCall.getParams()[0]);
						arrayBukets.add(") ");
					}
				}
			} else if (boolenExpresion.getLeft() instanceof OperationCall
					&& boolenExpresion.getRight() instanceof FeatureCall) {

				if (boolenExpresion.getLeft() instanceof OperationCall) {
					arrayBukets.add("( ");
					OperationCall leftCall = (OperationCall) boolenExpresion.getLeft();
					arrayBukets.add("" + onlysetGetter(leftCall.getTarget().toString()) + " " + leftCall.getName() + " "
							+ leftCall.getParams()[0] + "");
					arrayBukets.add(") ");
				}

				arrayBukets.add(" " + boolenExpresion.getOperator() + " ");

				if (boolenExpresion.getRight() instanceof FeatureCall) {
					FeatureCall rightCall = (FeatureCall) boolenExpresion.getRight();
					if (null != rightCall.getTarget()) {
						arrayBukets.add("(");
						arrayBukets.add("" + onlysetGetter(rightCall.getTarget().toString()) + "." + rightCall.getName()
								+ "()");
						arrayBukets.add(") ");
					}
				}
			} else if (boolenExpresion.getLeft() instanceof BooleanOperation) {
				recursiveExpressionCheck(boolenExpresion.getLeft(), returnTypeIdentifier);
				arrayBukets.add(" " + boolenExpresion.getOperator() + " ");
				if (boolenExpresion.getRight() instanceof BooleanOperation) {
					recursiveExpressionCheck(boolenExpresion.getRight(), returnTypeIdentifier);
				} else if (boolenExpresion.getLeft() instanceof BooleanOperation) {
					recursiveExpressionCheck(boolenExpresion.getLeft(), returnTypeIdentifier);
				}
			} else if (boolenExpresion.getLeft() instanceof OperationCall
					&& boolenExpresion.getRight() instanceof BooleanOperation) {

				if (boolenExpresion.getLeft() instanceof OperationCall) {
					arrayBukets.add("(");
					OperationCall leftCall = (OperationCall) boolenExpresion.getLeft();
					arrayBukets.add("" + onlysetGetter(leftCall.getTarget().toString()) + " " + leftCall.getName() + " "
							+ leftCall.getParams()[0] + " ");
				}
				arrayBukets.add(" " + boolenExpresion.getOperator() + " ");

				recursiveExpressionCheck(boolenExpresion.getRight(), returnTypeIdentifier);
				arrayBukets.add(")");

			} else if (boolenExpresion.getLeft() instanceof FeatureCall
					&& boolenExpresion.getRight() instanceof FeatureCall) {

				if (boolenExpresion.getLeft() instanceof FeatureCall) {
					FeatureCall rightCall = (FeatureCall) boolenExpresion.getLeft();
					if (null != rightCall.getTarget()) {
						arrayBukets.add("(");
						arrayBukets.add("" + onlysetGetter(rightCall.getTarget().toString()) + "." + rightCall.getName()
								+ "()");
						arrayBukets.add(") ");
					}
				}
				arrayBukets.add(" " + boolenExpresion.getOperator() + " ");

				if (boolenExpresion.getRight() instanceof FeatureCall) {
					FeatureCall rightCall = (FeatureCall) boolenExpresion.getRight();
					if (null != rightCall.getTarget()) {
						arrayBukets.add("(");
						arrayBukets.add("" + onlysetGetter(rightCall.getTarget().toString()) + "." + rightCall.getName()
								+ "()");
						arrayBukets.add(") ");
					}
				}
			} else {

				if (boolenExpresion.getLeft() instanceof OperationCall
						&& boolenExpresion.getRight() instanceof OperationCall) {
					arrayBukets.add(" (");
					OperationCall leftCall = (OperationCall) boolenExpresion.getLeft();
					arrayBukets.add("" + onlysetGetter(leftCall.getTarget().toString()) + " " + leftCall.getName() + " "
							+ leftCall.getParams()[0] + "");
					arrayBukets.add(" " + boolenExpresion.getOperator() + " ");

					OperationCall rightCall = (OperationCall) boolenExpresion.getRight();

					arrayBukets.add("" + onlysetGetter(rightCall.getTarget().toString()) + " " + rightCall.getName()
							+ " " + rightCall.getParams()[0] + "");
					arrayBukets.add(") ");
				}
			}
		} else if (extension instanceof GlobalVarExpression) {
			GlobalVarExpression globExp = (GlobalVarExpression) extension;
			arrayBukets.add("\n \t\t" + globExp.getVarName() + ";");
		}
		return "";
	}

	private String decodeExistCheck(CollectionExpression featureCall) {
		// TODO Auto-generated method stub

		if (null != featureCall && null != featureCall.getClosure()) {
			Expression closure = featureCall.getClosure();
			if (closure instanceof BooleanOperation) {
				BooleanOperation booleanOperation = (BooleanOperation) closure;
				String firstSegment = booleanOperation.toString();
				String sortBy = ".stream().anyMatch(e->(" + firstSegment + "));";
				return sortBy;
			}else if (closure instanceof OperationCall) {

				OperationCall booleanOperation = (OperationCall) closure;
				String firstSegment = booleanOperation.toString();
				String sortBy = ".stream().anyMatch(e->(" + firstSegment + "));";
				return sortBy;
			
			}else {

			String srt = "";
			srt = featureCall.getTarget().toString() + ".stream().anyMatch(e->(" + featureCall.getClosure().toString()
					+ "));";
			return srt;
			}
		}
		return null;

	}

	private String decodeForLoop(CollectionExpression colctionObj) {
		// TODO Auto-generated method stub
		String srt = "";
		srt += onlysetGetter(colctionObj.getTarget().toString());
		srt = srt + ".stream().forEach((e) -> {";
		arrayBukets.add(srt);
		if (colctionObj.getClosure() instanceof OperationCall) {
			arrayBukets.add("\n \t\t" + onlysetGetter(colctionObj.getClosure().toString()) + ";");
		}
		arrayBukets.add("\n \t\t});");
		System.out.println(colctionObj);
		return null;
	}

	private void returnCheck(Identifier returnTypeIdentifier, String appliedGetter) {
		if (returnTypeIdentifier.toString().equals("Void")) {
			arrayBukets.add("\n \t\t " + appliedGetter + ";");
		} else {
			arrayBukets.add("\n \t\t return " + appliedGetter + ";");
		}
	}

	private String decodeSortBy(CollectionExpression collectionExpression) {
		Expression closure = collectionExpression.getClosure();
		if (closure instanceof FeatureCall) {
			FeatureCall featureCall = (FeatureCall) closure;
			String firstSegment = featureCall.getName().getFirstSegment();
			firstSegment = toFirstUpper(firstSegment);
			String sortBy = "sort((" + getFirstCapitailize(getObjectNameReplace()) + " o1, "
					+ getFirstCapitailize(getObjectNameReplace()) + " o2)->o1.get" + firstSegment
					+ "().compareTo(o2.get" + firstSegment + "()))";
			return sortBy;
		}
		return null;
	}

	private String decodeSortBy(FeatureCall closure) {
		if (closure instanceof FeatureCall) {
			FeatureCall featureCall = (FeatureCall) closure;
			String firstSegment = featureCall.getName().getFirstSegment();
			firstSegment = toFirstUpper(firstSegment);
			String sortBy = "sort((" + getFirstCapitailize(getObjectNameReplace()) + " o1, "
					+ getFirstCapitailize(getObjectNameReplace()) + " o2)->o1.get" + firstSegment
					+ "().compareTo(o2.get" + firstSegment + "()))";
			return sortBy;
		}
		return null;
	}

	private String toFirstUpper(String firstSegment) {
		return firstSegment.substring(0, 1).toUpperCase() + firstSegment.substring(1);
	}

	private String decodeSelect(CollectionExpression collectionExpression) {
		if (null != collectionExpression && null != collectionExpression.getClosure()) {
			Expression closure = collectionExpression.getClosure();
			if (closure instanceof BooleanOperation) {
				BooleanOperation booleanOperation = (BooleanOperation) closure;
				String firstSegment = booleanOperation.toString();
				String sortBy = "stream().filter(e->" + firstSegment + ").collect(Collectors.toList())";
				return sortBy;
			} else if (closure instanceof OperationCall) {
				OperationCall operationCall = (OperationCall) closure;
				String firstSegment = operationCall.toString();
				String sortBy = "stream().filter(e->" + firstSegment + ").collect(Collectors.toList())";
				return sortBy;
			}
		}
		return null;
	}

	private String decodeSelectFirst(CollectionExpression collectionExpression) {
		if (null != collectionExpression && null != collectionExpression.getClosure()) {
			Expression closure = collectionExpression.getClosure();
			if (closure instanceof BooleanOperation) {
				String returnStr = "";
				BooleanOperation booleanOperation = (BooleanOperation) closure;
				if (((BooleanOperation) closure).getLeft() instanceof BooleanOperation
						&& ((BooleanOperation) closure).getRight() instanceof CollectionExpression) {
					String sortBy = "";
					if ((((CollectionExpression) ((BooleanOperation) closure).getRight())).getName().toString()
							.contains("exists")) {
						CollectionExpression righ = ((CollectionExpression) ((BooleanOperation) closure).getRight());
						String firstSegment = righ.toString();
						sortBy = onlysetGetter(righ.getTarget().toString()) + ".stream().filter(e->" + firstSegment
								+ ").collect(Collectors.toList()).findFirst()";
					}
					if ((((BooleanOperation) ((BooleanOperation) closure).getLeft())).toString().contains("exists")) {
						String firstSegment = booleanOperation.toString();
						sortBy = sortBy + "stream().filter(e->" + firstSegment
								+ ").collect(Collectors.toList()).findFirst()";
					}
					String firstSegment = booleanOperation.toString();
					returnStr = "stream().filter(e->" + firstSegment + sortBy
							+ ").collect(Collectors.toList()).findFirst()";
					return returnStr;
				} else {
					String firstSegment = booleanOperation.toString();
					String sortBy = "stream().filter(e->" + firstSegment + ").collect(Collectors.toList()).findFirst()";
					return sortBy;
				}

			} else if (closure instanceof OperationCall) {
				OperationCall operationCall = (OperationCall) closure;
				String firstSegment = operationCall.toString();
				String sortBy = "stream().filter(e->" + firstSegment + ").collect(Collectors.toList()).findFirst()";
				return sortBy;
			}
		}
		return null;
	}

	private String decodeAddAll(CollectionExpression collectionExpression) {
		String tmpStr = "";
		if (null != collectionExpression && null != collectionExpression.getClosure()
				&& collectionExpression.getClosure() instanceof ChainExpression) {
			tmpStr += "\n\t\t" + collectionExpression.getTarget().toString();
			tmpStr = tmpStr + ".stream().forEach((e) -> {";

			ChainExpression closure = (ChainExpression) collectionExpression.getClosure();
			OperationCall operationCall = (OperationCall) closure.getFirst();
			tmpStr = tmpStr + "\n\t\t\t" + operationCall.getTarget().toString() + "." + operationCall.getName() + "(";
			arrayBukets.add(tmpStr);
			Expression[] params = operationCall.getParams();
			if (params.length > 0) {

				for (Expression paramexp : params) {
					System.out.println(paramexp);

					if (paramexp instanceof CollectionExpression) {

						arrayBukets.add(onlysetGetter(((CollectionExpression) paramexp).getTarget().toString()) + ".");
						if (((CollectionExpression) paramexp).getName().toString().equalsIgnoreCase("select")) {
							String decodeSortBy = decodeSelect((CollectionExpression) paramexp);
							arrayBukets.add(decodeSortBy + ";");
						} else if (((CollectionExpression) paramexp).getName().toString().equalsIgnoreCase("sortby")) {
							String decodeSortBy = decodeSortBy((CollectionExpression) paramexp);
							arrayBukets.add(decodeSortBy + ";");
						} else {
							String decodeSortBy = decodeSortBy((CollectionExpression) paramexp);
							arrayBukets.add(decodeSortBy + ";");
						}
					}
				}
			}
			arrayBukets.add(")");
			arrayBukets.add("\n\t\t\t});");
			System.out.println(closure);
		}
		return null;
	}

	private String replaceGetterandbreakets(Expression expression) {
		String chExpreplace = expression.toString().replace("()", "");
		if (!chExpreplace.endsWith(")")) {
			chExpreplace = chExpreplace + "()";
		}

		chExpreplace = replaceDot(chExpreplace);
		chExpreplace = chExpreplace.toString().replace(".", "().get");
		chExpreplace = chExpreplace.toString().replace("->().", "->");

		return chExpreplace;
	}

	private String replaceDot(String stringId) {
		if (stringId.startsWith(".")) {
			stringId = stringId.replaceFirst(".", "");
		}
		return stringId;
	}

	private String onlysetGetter(String tmpString) {
		String[] split = tmpString.split("\\.");
		String tmp = "";
		String tmpObj = "";
		if (split.length > 0) {
			String ss = "";
			if (Pattern.compile("\\+").matcher(split[0]).find()) {
				String[] subSplit = split[0].split("\\+");
				ss = subSplit[0];
				for (int j = 1; j < subSplit.length; j++) {
					tmp = getterSetterEngin(subSplit[j]);
					if (ss.endsWith("+") && tmp.startsWith("+")) {
						ss = ss + tmp.substring(1, tmp.length());
					} else if (ss.endsWith(".") && tmp.startsWith(".")) {
						ss = ss + tmp.substring(1, tmp.length());
					} else if (tmp.endsWith("+") && tmp.startsWith("+")) {
						ss = ss + tmp;
					} else if (ss.endsWith("+")) {
						ss = ss + tmp.substring(0, tmp.length());
					} else {
						ss = ss + "+" + tmp;
					}
				}
			} else {
				ss = split[0];
			}

			for (int i = 1; i < split.length; i++) {
				tmp = "";
				if (Pattern.compile("\\+").matcher(split[i]).find()) {

					String[] subSplit = split[i].split("\\+");
					tmp = getterSetterEngin(subSplit[0]);
					if (ss.endsWith(".") && tmp.startsWith(".")) {
						ss = ss + tmp.substring(1, tmp.length());
					} else {
						ss = ss + getterSetterEngin(subSplit[0]);
					}
					for (int j = 1; j < subSplit.length; j++) {
						tmp = getterSetterEngin(subSplit[j]);
						if (ss.endsWith("+") && tmp.startsWith("+")) {
							ss = ss + tmp.substring(1, tmp.length());
						} else if (ss.endsWith(".") && tmp.startsWith(".")) {
							ss = ss + tmp.substring(1, tmp.length());
						} else if (tmp.endsWith("+") && tmp.startsWith("+")) {
							ss = ss + tmp;
						} else if (ss.endsWith("+")) {
							ss = ss + tmp.substring(0, tmp.length());
						} else {
							ss = ss + "+" + tmp;
						}
					}

				} else {
					tmpObj = getterSetterEngin(split[i]);
					if (!ss.endsWith(".") && !tmpObj.startsWith(".")) {
						ss = ss + "." + getterSetterEngin(split[i]);
					} else {
						ss = ss + getterSetterEngin(split[i]);
					}
				}
			}
			if (ss.toString().startsWith(".")) {
				return ss.toString().substring(1, ss.toString().length());
			} else {
				return ss.toString();
			}

		} else {
			return tmpString;
		}

	}

	private String getterSetterEngin(String item) {
		String ss = "";
		if (item.trim().contains("_") && item.trim().endsWith("()")
				|| item.trim().contains("_") && item.trim().endsWith("()")
				|| item.trim().contains("_") && item.trim().endsWith("(e")) {
			ss = ss + item.trim().substring(item.trim().indexOf("_") + 1, item.trim().length());
		} else if (!item.trim().equalsIgnoreCase(getObjectNameReplace())) {
			if (item.trim().startsWith("\"") && item.trim().endsWith("\"") && !item.trim().endsWith("(e")) {
				ss = ss + "+" + item + "+";
			} else if (!item.contains(getObjectNameReplace()) && !item.trim().endsWith("(e")) {
				if (!item.contains(")") && !item.contains("size")) {
					ss = ss + ".get" + getFirstCapitailize(item) + "()";
				} else if (item.startsWith(".get") && !item.contains("size")) {
					ss = ss + ".get" + getFirstCapitailize(item);
				} else if (item.startsWith("get(")) {
					ss = ss + "." + item + ".";
				} else {
					ss = ss + item;
				}
			} else {
				if (!item.contains(")") && !item.contains("size")) {
					ss = ss + ".get" + getFirstCapitailize(item) + "()";
				} else if (item.startsWith(".get") && !item.contains("size")) {
					ss = ss + ".get" + getFirstCapitailize(item);
				} else if (item.startsWith("get(")) {
					ss = ss + "." + item + ".";
				} else {
					ss = ss + item;
				}
			}
		} else {
			return item + ".";
		}
		return ss;
	}

	@SuppressWarnings("rawtypes")
	private String replaceWithLoadMethod(String str) {
		for (Map.Entry mapElement : loadMethods.entrySet()) {
			if (((str.startsWith(".") && str.substring(1, str.length() - 1).startsWith(mapElement.getKey().toString()))
					|| str.contains(mapElement.getKey().toString()))
					&& ((OAWModel) mapElement.getValue()).getRetuneType().contains("List")) {
				arrayBukets.add("\n\t\t for(" + ((OAWModel) mapElement.getValue()).getRetunObjectName()
						+ "	tmpObj : " + getObjectNameReplace() + "."
						+ ((OAWModel) mapElement.getValue()).getMethodName() + " ){");
				str = str.replace(mapElement.getKey().toString(), "tmpObj");
				arrayBukets.add("\n\t\t\t if(tmpObj." + nullCheckerWithreplace(str) + " != null){");
			}
		}

		return str;
	}

	private String nullCheckerWithreplace(String str) {
		String[] split = str.split("\\.");
		if (split.length > 1) {
			String tmp = "get" + getFirstCapitailize(split[1]) + "()";
			return tmp;
		} else {
			return str;
		}
	}

	private String getLoggerInfo(String tmpString) {
		tmpString = tmpString.toString().replace(".info(\"", "log.info(\"");
		return tmpString;
	}

	private String getLoggerError(String tmpString) {
		tmpString = tmpString.toString().replace(".error(\"", "log.warn(\"");
		return tmpString;
	}

	private String getFirstCapitailize(String str) {
		StringBuffer s = new StringBuffer();
		char ch = ' ';
		for (int i = 0; i < str.length(); i++) {

			if (ch == ' ' && str.charAt(i) != ' ')
				s.append(Character.toUpperCase(str.charAt(i)));
			else
				s.append(str.charAt(i));
			ch = str.charAt(i);
		}
		return s.toString().trim();
	}

	private String getFirstLowerCase(String str) {
		StringBuffer s = new StringBuffer();
		char ch = ' ';
		for (int i = 0; i < str.length(); i++) {

			if (ch == ' ' && str.charAt(i) != ' ')
				s.append(Character.toLowerCase(str.charAt(i)));
			else
				s.append(str.charAt(i));
			ch = str.charAt(i);
		}
		return s.toString().trim();
	}

	private String replaceBrackets(String str) {
		str = str.replace("[", "<");
		str = str.replace("]", ">");

		return str;
	}

	private String changeFWDtoCreate(String str) {
		if (str.startsWith("fwd") || str.startsWith(".fwd")) {
			str = str.replace("fwd", "create");
		}
		return str;
	}

	private String replaceUnderscore(String str) {
		StringBuffer sb = new StringBuffer();
		String[] splitItems = str.split(" ");
		for (String item : splitItems) {
			if ((item.trim().contains("_") && item.trim().endsWith("()"))
					|| (item.trim().contains("_") && item.trim().contains("("))) {
				sb.append(item.trim().substring(item.trim().indexOf("_") + 1, item.trim().length()));
				sb.append(" ");
			} else {
				sb.append(item.replace("_", ""));
				sb.append(" ");
			}
		}

		return sb.toString();
	}

	public String getObjectNameReplace() {
		return objectNameReplace;
	}

	public void setObjectNameReplace(String objectNameReplace) {
		this.objectNameReplace = objectNameReplace;
	}

	public boolean isThis() {
		return isThis;
	}

	public void setThis(boolean isThis) {
		this.isThis = isThis;
	}

	private void changeUnderscore() {
		char[] charArray;
		boolean flag = false;
		int countDelet = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < arrayBukets.size(); i++) {
			countDelet = 0;
			sb = new StringBuffer();
			sb.append(arrayBukets.get(i));
			charArray = sb.toString().toCharArray();
			for (int j = 0; j < charArray.length; j++) {

				if (charArray[j] == '\"' || flag) {
					if (charArray[j] == '\"' && flag) {
						flag = false;
					} else {
						flag = true;
					}
				} else {
					if (charArray[j] == '_') {

						sb.deleteCharAt(j + countDelet);
						countDelet++;

					}
				}
			}
			arrayBukets.set(i, sb.toString());
		}
	}

	private static boolean isContain(String source, String subItem) {
		String pattern = "\\b" + subItem + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}
}