import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.Log;

public class ClassTooLongVisitor extends VoidVisitorAdapter<Void> {
    protected int maxLimit;

    public ClassTooLongVisitor(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration method, Void arg) {
        var stmts = CodeSmellsUtils.countStatements(method);
        if (stmts > maxLimit) {
            Log.info("CLASS TOO LONG at %s => it has %d which is more than %d!",
                    method.getNameAsString(),
                    stmts,
                    maxLimit);
        }
        super.visit(method, arg);
    }

    @Override
    public void visit(LocalClassDeclarationStmt n, Void arg) {
        var stmts = CodeSmellsUtils.countStatements(n);
        if (stmts > maxLimit) {
            Log.info("ANONYMOUS CLASS TOO LONG => it has %d which is more than %d!",
                    stmts,
                    maxLimit);
        }
        super.visit(n, arg);
    }
}
