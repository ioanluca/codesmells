import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

public class CodeSmellsUtils {

    public static int countStatements(Node node) {
        var soFar =
                node
                        .getChildNodes()
                        .stream()
                        .map(CodeSmellsUtils::countStatements)
                        .mapToInt(Integer::intValue)
                        .sum();
        return (node instanceof Statement) &&
                !(node instanceof BlockStmt) ?
                soFar + 1 :
                soFar;
    }

    public static int countPrimitiveVariables(Node node, JavaParserFacade jpf) {
        var soFar =
                node
                        .getChildNodes()
                        .stream()
                        .map(node1 -> countPrimitiveVariables(node1, jpf))
                        .mapToInt(Integer::intValue)
                        .sum();

        if (!(node instanceof VariableDeclarator) &&
                !(node instanceof VariableDeclarationExpr)) {
            return soFar;
        }

        ResolvedType t = null;
        try {
            t = jpf.getType(node);
        } catch (Exception ex) {
        }
        if (t == null) return soFar;
        if (t.isArray()) return soFar;
        if (!t.isPrimitive()) return soFar;
        return soFar + 1;
    }

    public static int countVariables(Node node) {
        var soFar =
                node
                        .getChildNodes()
                        .stream()
                        .map(CodeSmellsUtils::countVariables)
                        .mapToInt(Integer::intValue)
                        .sum();

        return soFar + (node instanceof VariableDeclarator ||
                node instanceof VariableDeclarationExpr ? 1 : 0);
    }

}
