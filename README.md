# Infix Parser

Mathemathical expression parser capable of parsing expressions written using infix notation. 
Supports variables, basic arithmetic operations, comparison between numbers and bitwise operations. 
Only supports integer values. Expressions written in parenthesis `()` are evaluated first.

## Supported operators

Ordered by precedence, from lowest to highest

0: `=`

1: `==`, `!=`, `>`, `>=`, `<`, `<=`

2: `+`, `-`

3: `*`

4: `/`, `%`

5: `>>`, `<<`, `^`, `&`, `|`

6: `**`

7: `-` (unary), `~`

## Variables

Variable names can contain any number of characters. Number of variables is only limited by your device's memory.
All variables are mutable, hence the name `variable`. Variable names can contain letters `a-z/A-Z`, 
numbers `0-9` and underscores `_`, but must begin with a letter or underscore. Variable names are case sensitive.
