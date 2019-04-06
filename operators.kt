object Operators {

    const val assignment = 0
    const val comparison = assignment + 1
    const val addition = comparison + 1
    const val multiplication = addition + 1
    const val division = multiplication + 1
    const val bitwise = division + 1
    const val power = bitwise + 1
    const val unary = power + 1

    private val unaryOperators = listOf(
        "~", "-"
    )

    fun isUnary(op: String) = op in unaryOperators

    private val binaryOperators = listOf(
        "==", "!=", ">", ">=", "<", "<=",
        "+", "-", "*", "/", "%", "**",
        ">>", "<<", "^", "&", "|",
        "="
    )

    fun isBinary(op: String) = op in binaryOperators

    fun isOperator(op: String) = isUnary(op) || isBinary(op)

    private val precedence = mapOf(
        Pair("=", assignment),

        Pair("==", comparison), Pair("!=", comparison), Pair(">", comparison),
        Pair(">=", comparison), Pair("<", comparison), Pair("<=", comparison),

        Pair("+", addition), Pair("-", addition),

        Pair("*", multiplication),

        Pair("/", division), Pair("%", division),

        Pair(">>", bitwise), Pair("<<", bitwise), Pair("^", bitwise),
        Pair("&", bitwise), Pair("|", bitwise),

        Pair("**", power),

        Pair("~", unary)
    )

    fun precedenceOf(op: String) = precedence[op]!!

}
