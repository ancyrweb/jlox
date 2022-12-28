package com.ancyr.lox;

import com.ancyr.lox.Expr;

import java.io.IOException;

public class AstTraverser implements Expr.Visitor<String> {
  public static void main(String[] args) throws IOException {
    Expr expression = new Expr.Binary(
        new Expr.Unary(
            new Token(TokenType.MINUS, "-", null, 1),
            new Expr.Literal(10)
        ),
        new Token(TokenType.PLUS, "+", null, 1),
        new Expr.Grouping(new Expr.Literal(2.71))
    );

    System.out.println(new AstTraverser().print(expression));
  }

  public String print(Expr expression) {
    return expression.accept(this);
  }

  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return parenthesize(
        expr.operator.lexeme,
        expr.left,
        expr.right
    );
  }

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return parenthesize("group", expr.expression);
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null) {
      return "nil";
    }

    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return parenthesize(
        expr.operator.lexeme,
        expr.right
    );
  }

  @Override
  public String visitVariableExpr(Expr.Variable expr) {
    return null;
  }

  @Override
  public String visitAssignExpr(Expr.Assign expr) {
    return null;
  }

  @Override
  public String visitLogicalExpr(Expr.Logical expr) {
    return null;
  }

  @Override
  public String visitCallExpr(Expr.Call expr) {
    return null;
  }

  @Override
  public String visitGetExpr(Expr.Get expr) {
    return null;
  }

  @Override
  public String visitSetExpr(Expr.Set expr) {
    return null;
  }

  @Override
  public String visitThisExpr(Expr.This expr) {
    return null;
  }

  public String parenthesize(String name, Expr...exprs) {
    StringBuilder builder = new StringBuilder();

    builder
        .append("(")
        .append(name);

    for (Expr expr : exprs) {
      builder.append(" ");
      builder.append(expr.accept(this));
    }

    builder.append(")");
    return builder.toString();
  }


}
