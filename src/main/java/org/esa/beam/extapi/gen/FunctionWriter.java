package org.esa.beam.extapi.gen;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Norman Fomferra
 */
public class FunctionWriter {
    final GeneratorContext context;
    final PrintWriter writer;

    public FunctionWriter(GeneratorContext context, PrintWriter writer) {
        this.context = context;
        this.writer = writer;
    }

    public void writeFunctionDeclaration(FunctionGenerator generator) {
        writer.printf("%s;\n", generator.generateFunctionSignature(context));
    }

    public void writeFunctionDefinition(FunctionGenerator functionGenerator) throws IOException {
        writer.printf("%s\n", functionGenerator.generateFunctionSignature(context));
        writer.print("{\n");
        writeFunctionBodyCode(functionGenerator.generateLocalVarDeclarations(context));
        for (ParameterGenerator parameterGenerator : functionGenerator.getParameterGenerators()) {
            writeFunctionBodyCode(parameterGenerator.generateTargetArgDeclaration(context));
            writeFunctionBodyCode(parameterGenerator.generateJniArgDeclaration(context));
        }
        writeFunctionBodyCode(functionGenerator.generateTargetResultDeclaration(context));
        writeFunctionBodyCode(functionGenerator.generateJniResultDeclaration(context));
        writeFunctionBodyCode(functionGenerator.generateEnterCode(context));
        writeFunctionBodyCode(functionGenerator.generateTargetArgsFromParsedParamsAssignment(context));
        for (ParameterGenerator parameterGenerator : functionGenerator.getParameterGenerators()) {
            writeFunctionBodyCode(parameterGenerator.generateJniArgFromTransformedTargetArgAssignment(context));
        }
        writeFunctionBodyCode(functionGenerator.generateJniResultFromJniCallAssignment(context));
        writeFunctionBodyCode(functionGenerator.generateTargetResultFromTransformedJniResultAssignment(context));
        for (ParameterGenerator parameterGenerator : functionGenerator.getParameterGenerators()) {
            writeFunctionBodyCode(parameterGenerator.generateTargetArgFromTransformedJniArgAssignment(context));
            writeFunctionBodyCode(parameterGenerator.generateJniArgDeref(context));
        }
        writeFunctionBodyCode(functionGenerator.generateJniResultDeref(context));
        writeFunctionBodyCode(functionGenerator.generateExitCode(context));
        writeFunctionBodyCode(functionGenerator.generateReturnStatement(context));
        writer.print("}\n");
    }

    private void writeFunctionBodyCode(String code) {
        String[] callCode = generateLines(code);
        for (String line : callCode) {
            writer.printf("    %s\n", line);
        }
    }

    private static String[] generateLines(String code) {
        if (code == null || code.length() == 0) {
            return new String[0];
        }
        return code.split("\n");
    }

}
