import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.Log;

public class PrimitiveObsessionVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        super.visit(n, arg);
        if (n.getFields().isEmpty()) {
            return;
        }
        var primitives =
                n.getFields()
                        .stream()
                        .filter(field -> {
                            var t = field.getElementType();
                            if (field.isStatic() && field.isFinal()) {
                                return false; // is a constant then its ok
                            }
                            if (t.isArrayType()) {
                                return false; // usually ok
                            }
                            var isString = t.asString().equals("String");
                            return t.isPrimitiveType() || isString;
                        })
                        .map(field -> field.getVariables().size())
                        .mapToInt(Integer::intValue)
                        .sum();

        var total = n
                .getFields()
                .stream()
                .map(field -> field.getVariables().size())
                .mapToInt(Integer::intValue)
                .sum();

        var ratio = (double) primitives / total;
        if (primitives >= 4 && ratio >= 0.37) {
            Log.info("PRIMITIVE OBSESSION! in CLASS %s --> %d primitives" +
                            " out of %d fields => that is %.2f %% primitives"
                    , n.getNameAsString(), primitives, total, ratio * 100);
        }
    }

    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);
        int total = n.getParameters().size();

        long primitives = n.getParameters().stream()
                .filter(p -> {
                    if (p.getType().isArrayType()) return false;
                    return p.getType().isPrimitiveType() ||
                            p.getType().asString().equals("String");
                })
                .count();

        if (n.getBody().isPresent()) {
            var body = n.getBody().get();
            primitives += CodeSmellsUtils.countPrimitiveVariables(
                    body
            );
            total += CodeSmellsUtils.countVariables(body);
        }

        var ratio = (double) primitives / total;
        if (primitives >= 4 && ratio >= 0.37) {
            Log.info("PRIMITIVE OBSESSION! in METHOD %s --> %d primitives" +
                            " out of %d variables => that is %.2f %% primitives"
                    , n.getNameAsString(), primitives, total, ratio * 100);
        }
    }
}
