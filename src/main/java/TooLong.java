import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.Log;

public class TooLong extends VoidVisitorAdapter<Void> {
    private int maxLimit;

    public TooLong(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        var stmts = CodeSmellsUtils.countStatements(method);
        if (stmts > maxLimit) {
            Log.info("Method %s is TOO LONG! --> it has %d which is more than %d!",
                    method.getNameAsString(),
                    stmts,
                    maxLimit);
        }
        super.visit(method, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        super.visit(n, arg);
    }

    @Override
    public void visit(LocalClassDeclarationStmt n, Void arg) {
        super.visit(n, arg);
    }
}
