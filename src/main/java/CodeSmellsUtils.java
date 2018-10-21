import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

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

    public static int countPrimitiveVariables(Node node) {
        var soFar =
                node
                        .getChildNodes()
                        .stream()
                        .map(CodeSmellsUtils::countPrimitiveVariables)
                        .mapToInt(Integer::intValue)
                        .sum();

        if (!(node instanceof VariableDeclarationExpr)) {
            return soFar;
        }

        var t = ((VariableDeclarationExpr) node).getElementType();
        if (t.isArrayType()) return soFar;
        if (!t.isPrimitiveType() && !t.asString().equals("String")) return soFar;
        return soFar + ((VariableDeclarationExpr) node).getVariables().size();
    }

    public static int countVariables(Node node) {
        var soFar =
                node
                        .getChildNodes()
                        .stream()
                        .map(CodeSmellsUtils::countVariables)
                        .mapToInt(Integer::intValue)
                        .sum();

        return soFar +
                (node instanceof VariableDeclarationExpr ?
                        ((VariableDeclarationExpr) node).getVariables().size() : 0);
    }

}
