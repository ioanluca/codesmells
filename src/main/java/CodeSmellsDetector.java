import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.ParserCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class CodeSmellsDetector {

    private final ProjectRoot projectRoot;
    private final Set<CompilationUnit> compilationUnits;

    public CodeSmellsDetector(Path projectRootPath) {
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        this.projectRoot =
                new ParserCollectionStrategy()
                        .collect(projectRootPath);
        compilationUnits = new HashSet<>();
    }

    private void runVisitor(VoidVisitor voidVisitor) {
        compilationUnits
                .forEach(cu -> cu.accept(voidVisitor, null));
    }

    private void longParameterList(int maxLimit) {
        var longParamListVisitor = new LongParamListVisitor(maxLimit);
        runVisitor(longParamListVisitor);
    }

    private void parse(String startPackage) {
        startPackage = startPackage == null ? "" : startPackage;
        final var finalStartPackage = startPackage; // java technicality
        projectRoot
                .getSourceRoots()
                .forEach(sourceRoot -> {
                    try {
                        var parseResults = sourceRoot.tryToParse(finalStartPackage);
                        parseResults
                                .forEach(parseResult -> {
                                    parseResult
                                            .getProblems()
                                            .forEach(p -> Log.error(p.getVerboseMessage()));
                                    parseResult.ifSuccessful(compilationUnits::add);
                                });
                    } catch (IOException e) {
                        Log.error("could not parse SourceRoot - %s", sourceRoot);
                        Log.error(e);
                    }
                });
    }


    public static void main(String[] args) throws Exception {
        var projectRootPath = Paths.get("/home/ioanluca/workspace/codesmells/src/main/java");
        var codeSmellDetector = new CodeSmellsDetector(projectRootPath);
        codeSmellDetector.parse("test");
        codeSmellDetector.longParameterList(5);
    }
}