package com.ancyr.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GenerateAST {
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("Usage: generate_ast <output directory>");
      System.exit(64);
    }

    System.out.println(System.getProperty("user.dir"));

    String outputDir = args[0];
    defineAST(outputDir, "Expr", Arrays.asList(
        "Assign   : Token name, Expr value",
        "Binary   : Expr left, Token operator, Expr right",
        "Logical  : Expr left, Token operator, Expr right",
        "Grouping : Expr expression",
        "Literal  : Object value",
        "Unary    : Token operator, Expr right",
        "Variable : Token name",
        "Call     : Expr callee, Token paren, List<Expr> arguments",
        "Get      : Expr object, Token name",
        "Set      : Expr object, Token name, Expr value",
        "This     : Token keyword",
        "Super    : Token keyword, Token method"
    ));

    defineAST(outputDir, "Stmt", Arrays.asList(
        "Expression : Expr expression",
        "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
        "Print      : Expr expression",
        "Var        : Token name, Expr initializer",
        "While      : Expr condition, Stmt body",
        "Block      : List<Stmt> statements",
        "Function   : Token name, List<Token> params, List<Stmt> body",
        "Return     : Token keyword, Expr value",
        "Class      : Token name, Expr.Variable superclass, List<Stmt.Function> methods"
    ));
  }

  private static void defineAST(String outputDir, String baseName, List<String> types) throws IOException {
    String path = outputDir + "/" + baseName + ".java";
    PrintWriter writer = new PrintWriter(path, "UTF-8");

    writer.println("package com.ancyr.lox;");
    writer.println();
    writer.println("import java.util.List;");
    writer.println();
    writer.println("abstract class " + baseName + " {");

    defineVisitor(writer, baseName, types);

    // The base accept() method.
    writer.println();
    writer.println("  abstract <R> R accept(Visitor<R> visitor);");

    for (String type : types) {
      String className = type.split(":")[0].trim();
      String fields = type.split(":")[1].trim();
      defineType(writer, baseName, className, fields);
    }

    writer.println("}");
    writer.close();
  }

  private static void defineType(
      PrintWriter writer, String baseName,
      String className, String fieldList
  ) {
    String[] fields = fieldList.split(", ");

    writer.println("  static class " + className + " extends " +
        baseName + " {");

    // Fields.
    for (String field : fields) {
      writer.println("    final " + field + ";");
    }
    writer.println();

    // Constructor.
    writer.println("    " + className + "(" + fieldList + ") {");

    // Store parameters in fields.
    for (String field : fields) {
      String name = field.split(" ")[1];
      writer.println("      this." + name + " = " + name + ";");
    }

    writer.println("    }");
    writer.println("");

    // The overriden visitor
    writer.println("    @Override");
    writer.println("    <R> R accept(Visitor<R> visitor) {");
    writer.println("      return visitor.visit" + className + baseName + "(this);");
    writer.println("    }");
    writer.println("  }");
    writer.println();
  }

  private static void defineVisitor(
      PrintWriter writer, String baseName, List<String> types
  ) {
    writer.println("  interface Visitor<R> {");

    for (String type : types) {
      String typeName = type.split(":")[0].trim();
      writer.println("    R visit" + typeName + baseName + "(" +
          typeName + " " + baseName.toLowerCase() + ");");
    }

    writer.println("  }");
  }
}