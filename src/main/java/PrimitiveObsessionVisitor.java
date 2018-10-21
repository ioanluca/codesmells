import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.utils.Log;

public class PrimitiveObsessionVisitor extends VoidVisitorAdapter<JavaParserFacade> {
    @Override
    public void visit(ClassOrInterfaceDeclaration n, JavaParserFacade arg) {
        super.visit(n, arg);
        if (n.getFields().isEmpty()) {
            return;
        }
        var primitives =
                n.getFields()
                        .stream()
                        .filter(f -> {
                            if (f.isStatic() && f.isFinal()) {
                                return false; // is a constant then its ok
                            }
                            if (f.getElementType().isArrayType()) {
                                return false; // usually ok
                            }
                            return f.getElementType().isPrimitiveType();
                        })
                        .count();

        var total = n.getFields().size();
        var ratio = (double) primitives / total;
        if (primitives >= 3 && ratio >= 0.3) {
            Log.info("PRIMITIVE OBSESSION! in CLASS %s --> %d primitives" +
                            " out of %d fields => that is %.2f %% primitives"
                    , n.getNameAsString(), primitives, total, ratio * 100);
        }
    }

    public void visit(MethodDeclaration n, JavaParserFacade arg) {
        super.visit(n, arg);
        final int[] total = {n.getParameters().size()};

        final long[] primitives = {n.getParameters().stream()
                .filter(p -> {
                    if (p.getType().isArrayType()) return false;
                    return p.getType().isPrimitiveType();
                }).count()};

        n.getBody().ifPresent(body ->
        {
            primitives[0] += CodeSmellsUtils.countPrimitiveVariables(
                    body,
                    arg
            );
            total[0] += CodeSmellsUtils.countVariables(body);
        });

        var ratio = (double) primitives[0] / total[0];
        if (primitives[0] >= 3 && ratio >= 0.3) {
            Log.info("PRIMITIVE OBSESSION! in METHOD %s --> %d primitives" +
                            " out of %d fields => that is %.2f %% primitives"
                    , n.getNameAsString(), primitives[0], total[0], ratio * 100);
        }
    }
}
