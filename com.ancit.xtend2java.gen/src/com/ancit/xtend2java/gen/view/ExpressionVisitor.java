package com.ancit.xtend2java.gen.view;

import java.util.Stack;

import org.eclipse.internal.xtend.expression.ast.AbstractVisitor;
import org.eclipse.internal.xtend.expression.ast.BooleanLiteral;
import org.eclipse.internal.xtend.expression.ast.BooleanOperation;
import org.eclipse.internal.xtend.expression.ast.Case;
import org.eclipse.internal.xtend.expression.ast.Cast;
import org.eclipse.internal.xtend.expression.ast.ChainExpression;
import org.eclipse.internal.xtend.expression.ast.CollectionExpression;
import org.eclipse.internal.xtend.expression.ast.ConstructorCallExpression;
import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.expression.ast.FeatureCall;
import org.eclipse.internal.xtend.expression.ast.GlobalVarExpression;
import org.eclipse.internal.xtend.expression.ast.ISyntaxElement;
import org.eclipse.internal.xtend.expression.ast.IfExpression;
import org.eclipse.internal.xtend.expression.ast.IntegerLiteral;
import org.eclipse.internal.xtend.expression.ast.LetExpression;
import org.eclipse.internal.xtend.expression.ast.ListLiteral;
import org.eclipse.internal.xtend.expression.ast.NullLiteral;
import org.eclipse.internal.xtend.expression.ast.OperationCall;
import org.eclipse.internal.xtend.expression.ast.RealLiteral;
import org.eclipse.internal.xtend.expression.ast.StringLiteral;
import org.eclipse.internal.xtend.expression.ast.SwitchExpression;
import org.eclipse.internal.xtend.expression.ast.TypeSelectExpression;

public class ExpressionVisitor extends AbstractVisitor {

	private Stack<ExpressionModel> stack = new Stack<ExpressionModel>();
	
	public Stack<ExpressionModel> getStack() {
		return stack;
	}
	
	public ExpressionVisitor(ExpressionModel expressionModel) {
		// TODO Auto-generated constructor stub
		stack.push(expressionModel);
	}
	
	private void accept(Expression expression) {
		System.out.println(expression);
		ExpressionModel expressionModel = new ExpressionModel();
		expressionModel.setExpression(expression);
		expressionModel.setName(expression.getClass().getName());
		
		if (!stack.isEmpty()) {
			stack.peek().getExpressions().add(expressionModel);
		}
		stack.push(expressionModel);
	}

	protected Object visitBooleanOperation(BooleanOperation node) {
		if (node.getLeft() != null) {
			node.getLeft().accept(this);
		}
		if (node.getRight() != null) {
			node.getRight().accept(this);
		}
		return node;
	}

	protected Object visitCast(Cast node) {
		if (node.getTarget() != null) {
			node.getTarget().accept(this);
		}
		return node;
	}

	protected Object visitConstructorCallExpression(ConstructorCallExpression node) {
		return node;
	}

	protected Object visitGlobalVarExpression(GlobalVarExpression node) {
		return node;
	}

	protected Object visitChainExpression(ChainExpression ce) {
		if (ce.getFirst() != null) {
			ce.getFirst().accept(this);
		}
		if (ce.getNext() != null) {
			ce.getNext().accept(this);
		}
		return ce;
	}

	protected Object visitFeatureCall(FeatureCall fc) {
		if (fc.getTarget() != null) {
			fc.getTarget().accept(this);
		}
		return fc;
	}

	protected Object visitCollectionExpression(CollectionExpression node) {
		if (node.getClosure() != null) {
			node.getClosure().accept(this);
		}
		if (node.getTarget() != null) {
			node.getTarget().accept(this);
		}
		return node;
	}

	protected Object visitOperationCall(OperationCall oc) {
		if (oc.getTarget() != null) {
			oc.getTarget().accept(this);
		}
		if (oc.getParamsAsList() != null) {
			for (Expression expr : oc.getParamsAsList()) {
				expr.accept(this);
			}
		}
		return oc;
	}

	protected Object visitTypeSelectExpression(TypeSelectExpression node) {
		if (node.getTarget() != null) {
			node.getTarget().accept(this);
		}
		return node;
	}

	protected Object visitIfExpression(IfExpression node) {
		if (node.getCondition() != null) {
			node.getCondition().accept(this);
		}
		if (node.getThenPart() != null) {
			node.getThenPart().accept(this);
		}
		if (node.getElsePart() != null) {
			node.getElsePart().accept(this);
		}
		return node;
	}

	protected Object visitLetExpression(LetExpression node) {
		if (node.getTargetExpression() != null) {
			node.getTargetExpression().accept(this);
		}
		if (node.getVarExpression() != null) {
			node.getVarExpression().accept(this);
		}
		return node;
	}

	protected Object visitSwitchExpression(SwitchExpression node) {
		for (Case caze : node.getCases()) {
			if (caze.getCondition() != null) {
				caze.getCondition().accept(this);
			}
			if (caze.getThenPart() != null) {
				caze.getThenPart().accept(this);
			}
		}
		if (node.getSwitchExpr() != null) {
			node.getSwitchExpr().accept(this);
		}
		if (node.getDefaultExpr() != null) {
			node.getDefaultExpr().accept(this);
		}
		return node;
	}

	protected Object visitListLiteral(ListLiteral node) {
		return node;
	}

	protected Object visitBooleanLiteral(BooleanLiteral node) {
		return node;
	}

	protected Object visitIntegerLiteral(IntegerLiteral node) {
		return node;
	}

	protected Object visitNullLiteral(NullLiteral node) {
		return node;
	}

	protected Object visitRealLiteral(RealLiteral node) {
		return node;
	}

	protected Object visitStringLiteral(StringLiteral node) {
		return node;
	}

	@Override
	public final Object visit(final ISyntaxElement ele) {
		Object result = null;
		if (ele instanceof BooleanOperation) {
			accept((BooleanOperation) ele);
			result = visitBooleanOperation((BooleanOperation) ele);
			stack.pop();
		}
		if (result == null && ele instanceof Cast) {
			accept((Expression) ele);
			result = visitCast((Cast) ele);
			stack.pop();
		}
		if (result == null && ele instanceof ConstructorCallExpression) {
			accept((Expression) ele);
			result = visitConstructorCallExpression((ConstructorCallExpression) ele);
			stack.pop();
		}
		if (result == null && ele instanceof GlobalVarExpression) {
			accept((GlobalVarExpression) ele);
			result = visitGlobalVarExpression((GlobalVarExpression) ele);
			stack.pop();
		}
		if (result == null && ele instanceof ChainExpression) {
			accept((ChainExpression) ele);
			result = visitChainExpression((ChainExpression) ele);
			stack.pop();
		}
		if (result == null && ele instanceof CollectionExpression) {
			accept((CollectionExpression) ele);
			result = visitCollectionExpression((CollectionExpression) ele);
			stack.pop();
		}
		if (result == null && ele instanceof OperationCall) {
			accept((OperationCall) ele);
			result = visitOperationCall((OperationCall) ele);
			stack.pop();
		}
		if (result == null && ele instanceof TypeSelectExpression) {
			accept((TypeSelectExpression) ele);
			result = visitTypeSelectExpression((TypeSelectExpression) ele);
			stack.pop();
		}
		if (result == null && ele instanceof FeatureCall) {
			accept((FeatureCall) ele);
			result = visitFeatureCall((FeatureCall) ele);
			stack.pop();
		}
		if (result == null && ele instanceof IfExpression) {
			accept((IfExpression) ele);
			result = visitIfExpression((IfExpression) ele);
			stack.pop();
		}
		if (result == null && ele instanceof LetExpression) {
			accept((LetExpression) ele);
			result = visitLetExpression((LetExpression) ele);
			stack.pop();
		}
		if (result == null && ele instanceof SwitchExpression) {
			accept((SwitchExpression) ele);
			result = visitSwitchExpression((SwitchExpression) ele);
			stack.pop();
		}
		if (result == null && ele instanceof ListLiteral) {
			accept((ListLiteral) ele);
			result = visitListLiteral((ListLiteral) ele);
			stack.pop();
		}
		if (result == null && ele instanceof BooleanLiteral) {
			accept((BooleanLiteral) ele);
			result = visitBooleanLiteral((BooleanLiteral) ele);
			stack.pop();
		}
		if (result == null && ele instanceof IntegerLiteral) {
			accept((IntegerLiteral) ele);
			result = visitIntegerLiteral((IntegerLiteral) ele);
			stack.pop();
		}
		if (result == null && ele instanceof NullLiteral) {
			accept((NullLiteral) ele);
			result = visitNullLiteral((NullLiteral) ele);
			stack.pop();
		}
		if (result == null && ele instanceof RealLiteral) {
			accept((RealLiteral) ele);
			result = visitRealLiteral((RealLiteral) ele);
			stack.pop();
		}
		if (result == null && ele instanceof StringLiteral) {
			accept((StringLiteral) ele);
			result = visitStringLiteral((StringLiteral) ele);
			stack.pop();
		}
		return result;
	}

	
	

}
