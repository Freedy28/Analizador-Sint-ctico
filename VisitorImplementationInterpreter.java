package mx.ipn.escom.k.interpreter;

import mx.ipn.escom.k.core.*;
import mx.ipn.escom.k.core.exception.RuntimeError;
import mx.ipn.escom.k.core.expression.*;
import mx.ipn.escom.k.core.statement.*;
import mx.ipn.escom.k.core.token.TokenId;
import mx.ipn.escom.k.core.token.TokenName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitorImplementationInterpreter implements VisitorExpression<Object>, VisitorStatement<Void> {

    private final Environment globals;
    private Environment environment;
    private final Map<Expression, Integer> locals = new HashMap<>();

    public VisitorImplementationInterpreter(Environment environment) {
        this.globals = environment;
        this.environment = globals;
    }

    private Object evaluate(Expression expression){
        return expression.accept(this);
    }

    @Override
    public Object visitGroupingExpression(GroupingExpression expression) {
        return evaluate(expression.expression());
    }

    @Override
    public Object visitLiteralExpression(LiteralExpression expression) {
        return expression.value();
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression expression) {
        Object result = evaluate(expression.right());

        if(expression.operator().getTokenName() == TokenName.MINUS
                && result instanceof Number){
            if(result instanceof Integer)
                return -(Integer)result;
            if(result instanceof Double)
                return -(Double)result;
        }
        else if(expression.operator().getTokenName() == TokenName.BANG &&
                result instanceof Boolean){
            return !(Boolean)result;
        }

        if(expression.operator().getTokenName() == TokenName.BANG){
            throw new RuntimeError(expression.operator(), "Operand must be a bool.");
        }

        throw new RuntimeError(expression.operator(), "Operand must be a number.");
    }

    // Faltan métodos por implementar pero esos les toca a ustedes.
}
