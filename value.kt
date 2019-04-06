class Value {

    val parsed: Boolean
        get() = expr != null

    val isOperator:Boolean
        get() = precedence != -1

    var expr: Expression? = null
    var str: String = ""
    var token: Token = Token("", TokenType.Literal)
    var precedence = -1

}
