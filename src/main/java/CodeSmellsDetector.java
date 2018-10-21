import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.ProjectRoot;
import com.google.common.base.Strings;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class CodeSmellsDetector {

    private final ProjectRoot projectRoot;
    private final Set<CompilationUnit> compilationUnits;
    private final JavaParserFacade javaParserFacade;

    public CodeSmellsDetector(Path projectRootPath) {
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        this.projectRoot =
                new SymbolSolverCollectionStrategy()
                        .collect(projectRootPath);
        compilationUnits = new HashSet<>();
        javaParserFacade = JavaParserFacade.get(new CombinedTypeSolver(
                new ReflectionTypeSolver(),
                new JavaParserTypeSolver(projectRootPath)));
    }

    public void runDetectors() {
        compilationUnits
                .forEach(
                        cu -> cu
                                .getTypes()
                                .forEach(tydecl -> {
                                    var msg = String.format("\nin type %s->>",
                                            tydecl.getName().asString());
                                    Log.info(msg);
                                    Log.info(Strings.repeat("=", msg.length()));
                                    tydecl.accept(new LongParamListVisitor(5), null);
                                    tydecl.accept(new ClassTooLongVisitor(100), null);
                                    tydecl.accept(new MethodTooLongVisitor(10), null);
                                    tydecl.accept(new SwitchDetector(), javaParserFacade);
                                    tydecl.accept(new PrimitiveObsessionVisitor(), null);
                                    tydecl.accept(new MiddleManVisitor(tydecl), null);
                                    Log.info(Strings.repeat("=", msg.length()));
                                })
                );
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
        codeSmellDetector.runDetectors();
    }
}
