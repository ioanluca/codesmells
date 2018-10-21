import com.github.javaparser.ast.Node;
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
        return node instanceof Statement ?
                soFar + 1 :
                soFar;
    }

}
