package org.example;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BuildFieldProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith =
                roundEnv.getElementsAnnotatedWith(BuilderField.class);

        List<Element> fields = elementsAnnotatedWith.stream()
                .map(element -> ((VariableElement) element.asType())).collect(Collectors.toList());

        String className = ((TypeElement) fields.get(0).getEnclosingElement()).getQualifiedName().toString();

        List<String> fieldNames = fields
                .stream()
                .map(field -> field.getSimpleName().toString()).toList();

        try {
            createBuilderClass(className, fieldNames);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void createBuilderClass(String className, List<String> fieldNames) throws IOException {
        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String simpleClassName = className.substring(lastDot + 1);
        String builderClassName = className + "Builder";
        String builderSimpleClassName = builderClassName.substring(lastDot + 1);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(builderClassName);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            out.print("public class ");
            out.print(builderSimpleClassName);
            out.println(" {");
            out.println();

            out.print("    private ");
            out.print(simpleClassName);
            out.print(" object = new ");
            out.print(simpleClassName);
            out.println("();");
            out.println();

            out.print("    public ");
            out.print(simpleClassName);
            out.println(" build() {");
            out.println("        return object;");
            out.println("    }");
            out.println();

            fieldNames.forEach(field -> {
                String methodName = field;
               // String argumentType = field.getValue();

                out.print("    String ");
//                out.print(builderSimpleClassName);
//                out.print(" ");
                out.print(methodName);

                out.print(" = ");

 //               out.print(argumentType);
                out.println(" value;");
//                out.print("        object.");
//                out.print(methodName);
//                out.println("(value);");
//                out.println("        return this;");
//                out.println("    }");
//                out.println();
            });

            out.println("}");
        }
    }
}

