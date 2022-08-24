package com.ancit.xtend2java.gen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.internal.xpand2.ast.Advice;
import org.eclipse.internal.xpand2.ast.Definition;
import org.eclipse.internal.xpand2.ast.ErrorStatement;
import org.eclipse.internal.xpand2.ast.ExpandStatement;
import org.eclipse.internal.xpand2.ast.ExpressionStatement;
import org.eclipse.internal.xpand2.ast.FileStatement;
import org.eclipse.internal.xpand2.ast.ForEachStatement;
import org.eclipse.internal.xpand2.ast.IfStatement;
import org.eclipse.internal.xpand2.ast.ImportDeclaration;
import org.eclipse.internal.xpand2.ast.Statement;
import org.eclipse.internal.xpand2.ast.Template;
import org.eclipse.internal.xpand2.ast.TextStatement;
import org.eclipse.internal.xpand2.model.XpandDefinition;
import org.eclipse.internal.xtend.expression.ast.CollectionExpression;
import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.expression.ast.FeatureCall;
import org.eclipse.internal.xtend.expression.ast.IfExpression;
import org.eclipse.internal.xtend.expression.ast.OperationCall;
import org.eclipse.internal.xtend.xtend.ast.AbstractExtensionDefinition;
import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.internal.xtend.xtend.ast.ExtensionFile;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xpand.ui.core.IXpandResource;
import org.eclipse.xpand.ui.editor.XpandEditor;
import org.eclipse.xtend.shared.ui.Activator;
import org.eclipse.xtend.shared.ui.core.IXtendXpandResource;

public class XtendToJavaViewPart extends ViewPart implements ISelectionListener {

	private TextViewer textViewer;
	private Map<String, Definition> definitionMap;

	public XtendToJavaViewPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {

		textViewer = new TextViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP | SWT.READ_ONLY);
		textViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		textViewer.getFindReplaceTarget();

		getSite().getPage().addSelectionListener(IPageLayout.ID_OUTLINE, this);
		getSite().getPage().addPartListener(new IPartListener() {

			private Template xr;

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
					XpandEditor xtendEditor = (XpandEditor) part;
					IEditorInput input = ((IEditorPart) xtendEditor).getEditorInput();

					StringBuffer xtendContent = new StringBuffer();

					IStorage file = (IStorage) input.getAdapter(IStorage.class);
					// somehow AdapterManager returns null for IStorage, so fall back to
					// IFile
					if (file == null) {
						file = (IStorage) input.getAdapter(IFile.class);
					}
					IXtendXpandResource findExtXptResource = Activator.getExtXptModelManager().findExtXptResource(file);

					if (findExtXptResource instanceof IXpandResource) {
						xr = (Template) ((IXpandResource) findExtXptResource).getExtXptResource();
						handleNsImports(xtendContent, xr);
						XpandDefinition[] definitions = xr.getDefinitions();

						definitionMap = new HashMap<String, Definition>();
						for (XpandDefinition xpandDefinition : definitions) {
							if (xpandDefinition instanceof Definition) {
								Definition definition = (Definition) xpandDefinition;
								definitionMap.put(toFirstLower(definition.getName()), definition);
							}
						}

						for (XpandDefinition xpandDefinition : definitions) {
							if (xpandDefinition instanceof Definition) {
								Definition definition = (Definition) xpandDefinition;
								Statement[] body = definition.getBody();
								extractStatment(xtendContent, body); // Loops
							}
							break;
						}

						Document doc = new Document(xtendContent.toString());
						textViewer.setDocument(doc);
					}
				}
			}
		});
	}

	private void extractStatment(StringBuffer xtendContent, Statement[] body) {
		for (Statement statement : body) {
			if (statement instanceof ExpandStatement) {
				ExpandStatement expandStatement = (ExpandStatement) statement;
				String statementDefintion = toFirstLower(expandStatement.getDefinition().toString());
				String smtContDef = toFirstLower(expandStatement.getContainingDefinition().getName());
				if (expandStatement.isForeach()) {
					String listString = "<#list " + smtContDef + "."
							+ expandStatement.getTarget().toString().replace("\\.", "") + " as " + statementDefintion
							+ ">";
					xtendContent.append(listString);
					System.out.println(listString);
					Definition object = definitionMap.get(statementDefintion);
					extractStatment(xtendContent, object.getBody());
					String endList = "</#list>";
					xtendContent.append(endList);
					System.out.println(endList);
				} else {
					Definition object = definitionMap.get(statementDefintion);
					if (object != null) {
//						String elseListString = "<#list " + smtContDef + "."
//								+ expandStatement.getTarget().toString().replace("\\.", "") + " as "
//								+ statementDefintion + ">";
//						xtendContent.append(elseListString);
//						System.out.println(elseListString);

						extractStatment(xtendContent, object.getBody());
//						xtendContent.append("</#list>");
//						System.out.println("</#list>");
					}
				}
			} else if (statement instanceof ExpressionStatement) {
				ExpressionStatement expressionStatement = (ExpressionStatement) statement;
				if (expressionStatement.getExpression().toString().contains("setter")) {
					String setString = "set" + "${" + expressionStatement.getContainingDefinition().getName()
							+ ".name?cap_first" + "}()";
					xtendContent.append(setString);
					System.out.println(setString);
				} else if (expressionStatement.getExpression().toString().contains("getter")) {
					String getString = "get" + "${" + expressionStatement.getContainingDefinition().getName()
							+ ".name?cap_first" + "}()";
					xtendContent.append(getString);
					System.out.println(getString);
				} else {
					expandExpression(xtendContent, statement, expressionStatement.getExpression());
				}
			} else if (statement instanceof TextStatement) {
				TextStatement textStatement = (TextStatement) statement;
				String value = textStatement.getValue();
				if (!System.lineSeparator().contains(value.toString())) {
					xtendContent.append(value);
					System.out.println(value);
				}
			} else if (statement instanceof FileStatement) {
				if (statement instanceof FileStatement) {
					FileStatement fileStatement = (FileStatement) statement;
					extractStatment(xtendContent, fileStatement.getBody());
				}
			} else if (statement instanceof IfStatement) {
				IfStatement ifstatement = (IfStatement) statement;
				if (!ifstatement.getCondition().toString().contains("lastIteration")) {
					String ifStatementString = "";
					if (ifstatement.getCondition().toString().substring(0, 1).equals(".")) {
						ifStatementString = "<#if " + toFirstLower(ifstatement.getContainingDefinition().getName())
								+ ifstatement.getCondition().toString() + ">";
						if (ifstatement.getCondition().toString().contains("==true")) {
							ifStatementString = "<#if " + toFirstLower(ifstatement.getContainingDefinition().getName())
									+ ifstatement.getCondition().toString().replaceFirst("==true", "??") + ">";
						} else if (ifstatement.getCondition().toString().contains("!=null")) {
							ifStatementString = "<#if " + toFirstLower(ifstatement.getContainingDefinition().getName())
									+ ifstatement.getCondition().toString().replaceFirst("!=null", "??") + ">";
						} else if (ifstatement.getCondition().toString().contains("==null")) {
							ifStatementString = "<#if " + toFirstLower(ifstatement.getContainingDefinition().getName())
									+ ifstatement.getCondition().toString().replaceFirst("==null", "!") + ">";
						}
					} else if (ifstatement.getCondition().toString().contains("==true")) {
						ifStatementString = "<#if " + ifstatement.getCondition().toString().replaceFirst("==true", "??")
								+ ">";
					} else if (ifstatement.getCondition().toString().contains("!=null")) {
						ifStatementString = "<#if " + ifstatement.getCondition().toString().replaceFirst("!=null", "??")
								+ ">";
					} else if (ifstatement.getCondition().toString().contains("==null")) {
						ifStatementString = "<#if " + ifstatement.getCondition().toString().replaceFirst("==null", "!")
								+ ">";
					} else {
						ifStatementString = "<#if " + ifstatement.getCondition().toString() + ">";
					}
					xtendContent.append(ifStatementString);
					System.out.println(ifStatementString);
					if (ifstatement.getBody() != null) {
						extractStatment(xtendContent, ifstatement.getBody());
					}
					if (ifstatement.getElseIf() != null) {
						String endIfStatementString = "<#else>";
						xtendContent.append(endIfStatementString);
						System.out.println(endIfStatementString);
						extractStatment(xtendContent, ifstatement.getElseIf().getBody());

					}
					String endIfStatementString = "</#if>";
					xtendContent.append(endIfStatementString);
					System.out.println(endIfStatementString);
				}

			} else if (statement instanceof ForEachStatement) {
				ForEachStatement forEachStatement = (ForEachStatement) statement;
				String forEachListString = "<#list ";
				xtendContent.append(forEachListString);
				System.out.println(forEachListString);
				expandExpression(xtendContent, statement, forEachStatement.getTarget());
				String listContentString = " as " + forEachStatement.getVariable().toString() + ">";
				xtendContent.append(listContentString);
				System.out.println(listContentString);
				extractStatment(xtendContent, forEachStatement.getBody());
				String ifElseString = "<#if " + forEachStatement.getVariable().toString() + "?is_last><#else>,</#if>";
				xtendContent.append(ifElseString);
				System.out.println(ifElseString);
				String endListString = "</#list>";
				xtendContent.append(endListString);
				System.out.println(endListString);
			}
		}

	}

	private void expandExpression(StringBuffer xtendContent, Statement statement, Expression expression) {
		if (expression instanceof CollectionExpression) {
			CollectionExpression collectionExperssion = (CollectionExpression) expression;
			if (collectionExperssion.getName().toString().equals("sortBy")) {
				String collectionExpressionString = collectionExperssion.getTarget() + "?sort_by(\"";
				xtendContent.append(collectionExpressionString);
				System.out.println(collectionExpressionString);
				expandExpression(xtendContent, statement, collectionExperssion.getClosure());
				String endBraceString = "\")";
				xtendContent.append(endBraceString);
				System.out.println(endBraceString);
			}
		} else if (expression instanceof OperationCall) {
			OperationCall operationCall = (OperationCall) expression;
			String statementDefinition = toFirstLower(statement.getContainingDefinition().getName());
			String operationCallString = operationCall.toString().replaceFirst("this", statementDefinition);
			if (!statement.toString().contains("IF") && !statement.toString().contains("EXPRESSION: if")) {
				String openParentheses = "${";
				xtendContent.append(openParentheses);
				System.out.println(openParentheses);
				xtendContent.append(operationCallString);
				System.out.println(operationCallString);
				String parentheses = "}";
				xtendContent.append(parentheses);
				System.out.println(parentheses);
			} else {
				if (operationCallString.contains("==true")) {
					operationCallString = operationCallString.replaceFirst("==true", "??");
				}
				xtendContent.append(operationCallString);
				System.out.println(operationCallString);
			}

		} else if (expression instanceof FeatureCall) {
			FeatureCall featureCall = (FeatureCall) expression;
			if (featureCall.getTarget() != null) {
				if (featureCall.getTarget().toString().contains("e\\.")) {
					String featureCallName = featureCall.getName().toString();
					xtendContent.append(featureCallName);
					System.out.println(featureCallName);
				} else {
					featureWithoutTarget(xtendContent, statement, featureCall);
				}
			} else {
				featureWithoutTarget(xtendContent, statement, featureCall);

			}
		} else if (expression instanceof IfExpression) {
			IfExpression ifExpression = (IfExpression) expression;
			String ifString = "<#if ";
			xtendContent.append(ifString);
			System.out.println(ifString);
			expandExpression(xtendContent, statement, ifExpression.getCondition());
			String greaterThan = ">";
			xtendContent.append(greaterThan);
			System.out.println(greaterThan);
			String thenPart = ifExpression.getThenPart().toString();
			xtendContent.append(thenPart);
			System.out.println(thenPart);
			String elseString = "<#else>";
			xtendContent.append(elseString);
			System.out.println(elseString);
			String elsePart = ifExpression.getElsePart().toString();
			xtendContent.append(elsePart);
			System.out.println(elsePart);
			String endIf = "</#if>";
			xtendContent.append(endIf);
			System.out.println(endIf);

		}
	}

	private void featureWithoutTarget(StringBuffer xtendContent, Statement statement, FeatureCall featureCall) {
		String featureCallString = featureCall.toString().replaceFirst("this",
				toFirstLower(statement.getContainingDefinition().getName()));
		String featureWithDot = "";
		if (!statement.toString().contains("FOREACH")) {
			String openParentheses = "${";
			xtendContent.append(openParentheses);
			System.out.println(openParentheses);
			if (featureCallString.substring(0, 1).equals(".")) {
				featureWithDot = toFirstLower(statement.getContainingDefinition().getName()) + featureCallString;
				xtendContent.append(featureWithDot);
				System.out.println(featureWithDot);
			} else {
				xtendContent.append(featureCallString);
				System.out.println(featureCallString);
			}
			String parnetheses = "}";
			xtendContent.append(parnetheses);
			System.out.println(parnetheses);
		} else {
			xtendContent.append(featureCallString);
			System.out.println(featureCallString);
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sSelection = (IStructuredSelection) selection;
//			System.out.println(sSelection.getFirstElement());

		}
	}

	private void handleNsImports(StringBuffer xtendContent, Template xr) {
		ImportDeclaration[] imports = xr.getImports();
		for (ImportDeclaration namespaceImportStatement : imports) {
//			xtendContent.append("\nimport " + namespaceImportStatement.getImportString() + ".*");
		}
	}

	private String toFirstLower(String firstSegment) {
		return firstSegment.substring(0, 1).toLowerCase() + firstSegment.substring(1);
	}

	private List<Extension> handleExtMethods(StringBuffer xtendContent, ExtensionFile xr) {
		List<Extension> extensions = xr.getExtensions();
		for (Extension extension : extensions) {

			StringBuffer returnType = new StringBuffer();
			if (extension.getReturnTypeIdentifier() == null) {
				returnType.append("String");
			} else {
				returnType.append(extension.getReturnTypeIdentifier().toString());
			}

			xtendContent.append("\n" + returnType.toString() + " " + extension + " {");
			AbstractExtensionDefinition abstractExtensionDefinition = (AbstractExtensionDefinition) extension;
			xtendContent.append("\n" + abstractExtensionDefinition.getExpression());
			xtendContent.append("\n}");
		}
		return extensions;
	}
}
