package com.ancit.xtend2java.gen.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.internal.xtend.expression.ast.Expression;

public class ExpressionModel {
	
	List<ExpressionModel> expressions = new ArrayList<ExpressionModel>();
	Expression expression;
	String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public List<ExpressionModel> getExpressions() {
		return expressions;
	}
	
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	public Expression getExpression() {
		return expression;
	}

}
