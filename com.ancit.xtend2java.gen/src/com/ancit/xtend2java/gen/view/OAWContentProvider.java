package com.ancit.xtend2java.gen.view;

import java.util.ArrayList;

import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.xtend.ast.AbstractExtensionDefinition;
import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.internal.xtend.xtend.ast.ExtensionFile;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class OAWContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		ArrayList children = new ArrayList<>();
		if (inputElement instanceof ExtensionFile) {
			ExtensionFile xFile = (ExtensionFile) inputElement;
			
			children.addAll(xFile.getNsImports());
			children.addAll(xFile.getExtImports());
			children.addAll(xFile.getExtensions());
			
		}
		return children.toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Extension) {
			AbstractExtensionDefinition abstractExtensionDefinition = (AbstractExtensionDefinition) parentElement;
			Expression expression = abstractExtensionDefinition.getExpression();			
			return new Object[] {expression};
		} else if (parentElement instanceof Expression) {
			Expression expression = (Expression) parentElement;
			ExpressionModel expressionModel = new ExpressionModel();
			expressionModel.setExpression(expression);
			ExpressionVisitor visitor = new ExpressionVisitor(expressionModel);
			Object accept = expression.accept(visitor);
			visitor.getStack().pop();
			
			return new Object[] {expressionModel};
		} else if (parentElement instanceof ExpressionModel) {
			ExpressionModel expressionModel = (ExpressionModel) parentElement;
			return expressionModel.getExpressions().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return ((Object) element);
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		System.out.println(element);
		if(element instanceof Extension) {
			return true;
		} else if (element instanceof Expression) {
			return true;
		} else if (element instanceof ExpressionModel) {
			return true;
		}
		return false;
	}

}
