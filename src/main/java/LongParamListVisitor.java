import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.Log;

public class LongParamListVisitor extends VoidVisitorAdapter<Void> {
    private int maxLimitInclusive;

    public LongParamListVisitor(int maxLimitInclusive) {
        this.maxLimitInclusive = maxLimitInclusive;
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, Void arg) {
        if (methodDeclaration
                .getParameters()
                .size() >= maxLimitInclusive) {
            Log.info("Method %s has more than %d parameters!",
                    methodDeclaration.getNameAsString(),
                    maxLimitInclusive);
        }
        super.visit(methodDeclaration, arg);
    }
}
