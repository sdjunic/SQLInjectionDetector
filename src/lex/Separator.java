package lex;

import Parse.Sym;
import java_cup.runtime.Symbol;

class Separator extends Token {
  char which;
  Separator(char which) { this.which = which; }

  Symbol token() {
    switch(which) {
    case '(': return new Symbol(Sym.LPAREN, which);
    case ')': return new Symbol(Sym.RPAREN, which);
    case '{': return new Symbol(Sym.LBRACE, which);
    case '}': return new Symbol(Sym.RBRACE, which);
    case '[': return new Symbol(Sym.LBRACK, which);
    case ']': return new Symbol(Sym.RBRACK, which);
    case ';': return new Symbol(Sym.SEMICOLON, which);
    case ',': return new Symbol(Sym.COMMA, which);
    case '.': return new Symbol(Sym.DOT, which);
    case '\u2026':  return new Symbol(Sym.ELLIPSIS, which);
    default:
      throw new Error("Invalid separator.");
    }
  }

  public String toString() {
    return "Separator <"+which+">";
  }
}
