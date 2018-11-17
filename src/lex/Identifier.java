package lex;

import Parse.Sym;
import java_cup.runtime.Symbol;

public class Identifier extends Token {
  String identifier;
  public Identifier(String identifier) { this.identifier=identifier; }

  public String toString() { return "Identifier <"+identifier+">"; }

  Symbol token() { return new Symbol(Sym.IDENTIFIER, identifier); }
}
