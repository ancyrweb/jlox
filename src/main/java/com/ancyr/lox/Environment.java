package com.ancyr.lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
  final Environment enclosing;

  private final Map<String, Object> values = new HashMap<>();

  Environment() {
    this.enclosing = null;
  }

  Environment(Environment enclosing) {
    this.enclosing = enclosing;
  }

  void define(String name, Object value) {
    this.values.put(name, value);
  }

  void assign(Token name, Object value) {
    if (this.values.containsKey(name.lexeme)) {
      this.values.put(name.lexeme, value);
      return;
    }

    if (enclosing != null) {
      enclosing.assign(name, value);
      return;
    }

    throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
  }

  Object get(Token name) {
    if (this.values.containsKey(name.lexeme)) {
      return values.get(name.lexeme);
    }

    if (enclosing != null) {
      return enclosing.get(name);
    }

    throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
  }

  Object getAt(Integer distance, String name) {
    return ancestor(distance).values.get(name);
  }

  Object assignAt(Integer distance, Token name, Object value) {
    return ancestor(distance).values.put(name.lexeme, value);
  }

  Environment ancestor(Integer distance) {
    Environment environment = this;
    for (int i = 0; i < distance; i++) {
      environment = environment.enclosing;
    }
    return environment;
  }
}
