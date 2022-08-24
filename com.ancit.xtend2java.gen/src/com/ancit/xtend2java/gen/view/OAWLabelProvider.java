package com.ancit.xtend2java.gen.view;

import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.internal.xtend.xtend.ast.ExtensionImportStatement;
import org.eclipse.internal.xtend.xtend.ast.NamespaceImportStatement;
import org.eclipse.jface.viewers.LabelProvider;

public class OAWLabelProvider extends LabelProvider{


	
	@Override
	public String getText(Object element) {
		if (element instanceof NamespaceImportStatement) {
			NamespaceImportStatement nsImportStatement = (NamespaceImportStatement) element;
			return "import ns " + nsImportStatement.getImportedId().toString();
		} else if (element instanceof ExtensionImportStatement) {
			ExtensionImportStatement extImportStatement = (ExtensionImportStatement) element;
			return "import  ext " + extImportStatement.getImportedId().toString();
		} else if (element instanceof Extension) {
			Extension extension = (Extension)element;
			return extension.getName();
		} else if (element instanceof Expression) {
			Expression expression = (Expression) element;
			return expression.toString();
		} else if (element instanceof ExpressionModel) {
			ExpressionModel expressionModel = (ExpressionModel) element;
			return expressionModel.getName();
		}
		return super.getText(element);
	}
}

	
