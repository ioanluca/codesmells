import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.Log;

public class LongParamListVisitor extends VoidVisitorAdapter<Void> {
    private int maxLimit;

    public LongParamListVisitor(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        var params = method
                .getParameters()
                .size();

        if (params > maxLimit) {
            Log.info("LONG PARAMETER LIST at %s => it has %d which is more than %d!",
                    method.getNameAsString(),
                    params,
                    maxLimit);
        }
        super.visit(method, arg);
    }
}
