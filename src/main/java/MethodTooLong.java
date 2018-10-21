import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.Log;

public class MethodTooLong extends VoidVisitorAdapter<Void> {
    protected int maxLimit;

    public MethodTooLong(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        var stmts = CodeSmellsUtils.countStatements(method);
        super.visit(method, arg);
        if (stmts <= maxLimit) {
//            if ((stmts <= maxLimit) || (p != null && !(p instanceof ClassOrInterfaceDeclaration))) {
            return;
        }
        Log.info("Method %s is TOO LONG! --> it has %d which is more than %d!",
                method.getNameAsString(),
                stmts,
                maxLimit);
    }
}
