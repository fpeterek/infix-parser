import java.lang.RuntimeException

class ExpressionParser {


    private var tokenizer = StringTokenizer()

    fun parse(expr: String): Expression {

        val tokens = tokenizer.tokenize(expr)
        checkTokens(tokens)
        val precedences = checkOperators(tokens)
        val values = toValues(tokens, precedences)

        runParsers(values)

        if (values.size != 1) {
            throw RuntimeException("Error: Invalid expression")
        }

        return values.first().expr!!
    }

    private fun runParsers(values: MutableList<Value>) {

        LiteralParser(values)
        VariableParser(values)
        UnaryOperatorParser(values)

        LeftToRightParser(values, Operators.power)
        LeftToRightParser(values, Operators.bitwise)
        LeftToRightParser(values, Operators.division)
        LeftToRightParser(values, Operators.multiplication)
        LeftToRightParser(values, Operators.addition)
        LeftToRightParser(values, Operators.comparison)

        AssignmentParser(values, Operators.assignment)

    }

    private fun checkTokens(tokens: List<Token>) {
        if (tokens.isEmpty()) {
            throw RuntimeException("Error: Cannot evaluate empty expression")
        }
    }

    private fun checkOperators(tokens: List<Token>): List<Pair<Int, Int>> {

        checkOperatorValidity(tokens)
        val indices = getOperatorIndices(tokens)
        return getPrecedences(indices, tokens)

    }

    private fun toValues(tokens: List<Token>, precedences: List<Pair<Int, Int>>): MutableList<Value> {

        val values = tokens.map {
            val value = Value()
            value.str = it.value
            value.token = it
            if (value.token.type == TokenType.Parenthesis) {
                value.expr = parse(value.str.drop(1).dropLast(1))
            }
            value
        }

        precedences.forEach {
            values[it.first].precedence = it.second
        }

        return values.toMutableList()

    }

    private fun getPrecedences(indices: List<Int>, tokens: List<Token>): List<Pair<Int, Int>>
            = indices.map { Pair(it, Operators.precedenceOf(tokens[it].value)) }


    private fun getOperatorIndices(tokens: List<Token>): List<Int> {

        val indices = mutableListOf<Int>()

        for ((index, token) in tokens.withIndex()) {
            if (token.type == TokenType.Operator) {
                indices.add(index)
            }
        }

        return indices

    }

    private fun checkOperatorValidity(tokens: List<Token>) {
        tokens.forEach {
            if (it.type == TokenType.Operator && !Operators.isOperator(it.value)) {
                throw RuntimeException("Invalid operator: \"${it.value}\"")
            }
        }
        if (tokens.last().type == TokenType.Operator) {
            throw RuntimeException("Missing operand for operator \"${tokens.last().value}\"")
        }
    }

}
