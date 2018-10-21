import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.utils.Log;

public class SwitchDetector extends VoidVisitorAdapter<JavaParserFacade> {
    @Override
    public void visit(SwitchStmt n, JavaParserFacade jpf) {
        super.visit(n, jpf);
        var sel = n.getSelector();
        var t = jpf.getType(sel);
        if (t.isReferenceType()) {
            var tt = t.asReferenceType();
            Node nn = n;
            while (nn.getParentNode().isPresent() &&
                    !(nn instanceof MethodDeclaration)) nn = nn.getParentNode().get();
            if (tt.getTypeDeclaration().isEnum()) {
                Log.info("SWITCH ON ENUM %s at %s"
                        , tt.getQualifiedName(), ((MethodDeclaration) nn).getNameAsString());
            }
        }
    }

}
