import java_cup.runtime.*;

%%

%class Lexer
%line
%column
%unicode
%cup

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

Identifier = [:jletter:] [:jletterdigit:]*
DittoInteger = 0 | [1-9][0-9]*

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
CommentContent       = ( [^*] | \*+ [^/*] )*

Comment = {TraditionalComment} | {EndOfLineComment}

Trips = [\"][\"][\"]

%state STRING
%state TRIPSTRING

%%

/* White Space */
{WhiteSpace} {/* ignore */}

/* Keywords */
<YYINITIAL> "class" {/*System.out.println(yyline+1 + " CLASS " + "\"" + yytext() + "\"");*/ return symbol(sym.CLASS, yytext());}
<YYINITIAL> "def" {/*System.out.println(yyline+1 + " DEF " + "\"" + yytext() + "\"");*/ return symbol(sym.DEF, yytext());}
<YYINITIAL> "extends" {/*System.out.println(yyline + " EXTENDS " + "\"" + yytext() + "\"");*/ return symbol(sym.EXTENDS, yytext());}
<YYINITIAL> "if" {/*System.out.println(yyline+1 + " IF " + "\"" + yytext() + "\"");*/ return symbol(sym.IF, yytext());}
<YYINITIAL> "elif" {/*System.out.println(yyline+1 + " ELIF " + "\"" + yytext() + "\"");*/ return symbol(sym.ELIF, yytext());}
<YYINITIAL> "else" {/*System.out.println(yyline+1 + " ELSE " + "\"" + yytext() + "\"");*/ return symbol(sym.ELSE, yytext());}
<YYINITIAL> "while" {/*System.out.println(yyline+1 + " WHILE " + "\"" + yytext() + "\"");*/ return symbol(sym.WHILE, yytext());}
<YYINITIAL> "return" {/*System.out.println(yyline+1 + " RETURN " + "\"" + yytext() + "\"");*/ return symbol(sym.RETURN, yytext());}
/* <YYINITIAL> "String" {/*System.out.println(yyline+1 + " STRING " + "\"" + yytext() + "\"");*/ return symbol(sym.STRING, yytext());} */
/* <YYINITIAL> "Integer" {/*System.out.println(yyline+1 + " INTEGER " + "\"" + yytext() + "\"");*/ return symbol(sym.INTEGER, yytext());} */
<YYINITIAL> "and" {/*System.out.println(yyline+1 + " AND " + "\"" + yytext() + "\"");*/ return symbol(sym.AND, yytext());}
<YYINITIAL> "or" {/*System.out.println(yyline+1 + " OR " + "\"" + yytext() + "\"");*/ return symbol(sym.OR, yytext());}
<YYINITIAL> "not" {/*System.out.println(yyline+1 + " NOT " + "\"" + yytext() + "\"");*/ return symbol(sym.NOT, yytext());}
<YYINITIAL> "none" {/*System.out.println(yyline+1 + " none " + "\"" + yytext() + "\"");*/ return symbol(sym.none, yytext());}


/* Punctuation */
[+] {/*System.out.println(yyline+1 + " PLUS " + "\"" + yytext() + "\"");*/ return symbol(sym.PLUS, yytext());}
[-] {/*System.out.println(yyline+1 + " MINUS " + "\"" + yytext() + "\"");*/ return symbol(sym.MINUS, yytext());}
[*] {/*System.out.println(yyline+1 + " TIMES " + "\"" + yytext() + "\"");*/ return symbol(sym.TIMES, yytext());}
[/] {/*System.out.println(yyline+1 + " DIVIDE " + "\"" + yytext() + "\"");*/ return symbol(sym.DIVIDE, yytext());}
[\(] {/*System.out.println(yyline+1 + " LPAREN " + "\"" + yytext() + "\"");*/ return symbol(sym.LPAREN, yytext());}
[\)] {/*System.out.println(yyline+1 + " RPAREN " + "\"" + yytext() + "\"");*/ return symbol(sym.RPAREN, yytext());}
[\[] {/*System.out.println(yyline+1 + " LBRACE " + "\"" + yytext() + "\"");*/ return symbol(sym.LBRACE, yytext());}
[\]] {/*System.out.println(yyline+1 + " RBRACE " + "\"" + yytext() + "\"");*/ return symbol(sym.RBRACE, yytext());}
[\{] {/*System.out.println(yyline+1 + " LBRACKET " + "\"" + yytext() + "\"");*/ return symbol(sym.LBRACKET, yytext());}
[\}] {/*System.out.println(yyline+1 + " RBRACKET " + "\"" + yytext() + "\"");*/ return symbol(sym.RBRACKET, yytext());}
[,] {/*System.out.println(yyline+1 + " COMMA " + "\"" + yytext() + "\"");*/ return symbol(sym.COMMA, yytext());}
[;] {/*System.out.println(yyline+1 + " SEMICOLON " + "\"" + yytext() + "\"");*/ return symbol(sym.SEMICOLON, yytext());}
[.] {/*System.out.println(yyline+1 + " DOT " + "\"" + yytext() + "\"");*/ return symbol(sym.DOT, yytext());}
[:] {/*System.out.println(yyline+1 + " COLON " + "\"" + yytext() + "\"");*/ return symbol(sym.COLON, yytext());}
<YYINITIAL> "=" {/*System.out.println(yyline+1 + " EQUALS " + "\"" + yytext() + "\"");*/ return symbol(sym.EQUALS, yytext());}
<YYINITIAL> "==" {/*System.out.println(yyline+1 + " EQUALS " + "\"" + yytext() + "\"");*/ return symbol(sym.EQUIV, yytext());}
<YYINITIAL> "<=" {/*System.out.println(yyline+1 + " ATMOST " + "\"" + yytext() + "\"");*/ return symbol(sym.ATMOST, yytext());}
<YYINITIAL> "<" {/*System.out.println(yyline+1 + " LESS " + "\"" + yytext() + "\"");*/ return symbol(sym.LESS, yytext());}
<YYINITIAL> ">=" {/*System.out.println(yyline+1 + " ATLEAST " + "\"" + yytext() + "\"");*/ return symbol(sym.ATLEAST, yytext());}
<YYINITIAL> ">" {/*System.out.println(yyline+1 + " MORE " + "\"" + yytext() + "\"");*/ return symbol(sym.MORE, yytext());}

/* Identifiers */
<YYINITIAL> {Identifier} {/*System.out.println(yyline+1 + " IDENT " + "\"" + yytext() + "\"");*/ return symbol(sym.IDENT, yytext());}


/* Ditto Integers */
<YYINITIAL> {DittoInteger} {/*System.out.println(yyline+1 + " INT " + "\"" + yytext() + "\"");*/ return symbol(sym.INT, new Integer(Integer.parseInt(yytext())));}


/* Comments */
{Comment} { /* ignore */ }


/* Strings */

<YYINITIAL> [\"] {string.setLength(0); yybegin(STRING);}
<YYINITIAL> {Trips} {string.setLength(0); yybegin(STRING);}


<STRING> {
  \" {yybegin(YYINITIAL); /*System.out.println(yyline+1 + " STRING " + "\"" + string.toString() + "\"");*/ return symbol(sym.STRING, yytext());}
  {Trips} {yybegin(YYINITIAL); /*System.out.println(yyline+1 + " STRING " + "\"" + string.toString() + "\"");*/ return symbol(sym.STRING, yytext());}
  [^\n\r\"\\]+ { string.append( yytext() ); }
  \\0 {string.append('\0');}
  \\b {string.append('\b');}
  \\t {string.append('\t');}
  \\n {string.append('\n');}
  \\r {string.append('\r');}
  \\f {string.append('\f');}
  \\\" {string.append('\"');}
  \\  {string.append('\\');}
}




