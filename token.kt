enum class TokenType {
    Literal,
    Identifier,
    Operator,
    Parenthesis
}

class Token(val value: String, val type: TokenType)
