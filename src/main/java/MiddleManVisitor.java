import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MiddleManVisitor extends VoidVisitorAdapter<Void> {
    private final Set<String> methodNames;

    public MiddleManVisitor(Visitable visitable) {
        this.methodNames = new HashSet<>();
        visitable.accept(new MethodNamesVisitor(), methodNames);
    }

    private static class MethodNamesVisitor extends VoidVisitorAdapter<Set<String>> {
        @Override
        public void visit(MethodDeclaration n, Set<String> arg) {
            super.visit(n, arg);
            arg.add(n.getNameAsString());
        }
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);
        n.getBody().ifPresent(
                body -> {
                    if (body.getStatements().size() != 1) {
                        return;
                    }
                    var stmt = body.getStatement(0);

                    Consumer<MethodCallExpr> lam =
                            (methodCallExpr -> {
                                if (methodNames.contains(methodCallExpr
                                        .getName().asString())) {
                                    Log.info("MIDDLEMAN!!! at method %s", n.getNameAsString());
                                }
                            });

                    stmt.ifExpressionStmt(expressionStmt ->
                            expressionStmt.getExpression()
                                    .ifMethodCallExpr(lam));
                    stmt.ifReturnStmt(returnStmt ->
                            returnStmt.getExpression()
                                    .ifPresent(e -> e.ifMethodCallExpr(lam)));
                }
        );
    }
}
