import java.lang.RuntimeException

class StringTokenizer {

    companion object {
        fun isValidIdChar(char: Char) = char.isLetterOrDigit() || char == '_'
        fun isValidFirstIdChar(char: Char) = char.isLetter() || char == '_'
        fun isValidOperatorChar(char: Char) = char in "+-*/%!=^~<>&|"
        fun isValidOperator(op: String) = Operators.isOperator(op)
    }

    private var i = 0
    private var str = ""

    private fun id(): Token {

        var id = ""

        while (i < str.length && isValidIdChar(str[i])) {
            id += str[i]
            ++i
        }

        return Token(id, TokenType.Identifier)

    }

    private fun literal(): Token {

        var lit = ""

        while (i < str.length && str[i].isDigit()) {
            lit += str[i]
            ++i
        }

        return Token(lit, TokenType.Literal)

    }

    private fun checkOperator(op: String) {

        if (!isValidOperator(op)) {
            throw RuntimeException("Invalid Operator: \"$op\"")
        }

    }

    private fun operator(): Token {

        var op = ""

        while (i < str.length && isValidOperatorChar(str[i])) {
            op += str[i]
            ++i
        }

        checkOperator(op)

        return Token(op, TokenType.Operator)

    }

    fun tokenize(string: String): List<Token> = tokenize(splitByParens(string))

    private fun tokenize(list: List<String>): List<Token> {

        val tokens = mutableListOf<Token>()

        list.forEach {
            if (it[0] == '(') {
                tokens.add(Token(it, TokenType.Parenthesis))
            } else {
                tokens.addAll(tokenizeStr(it))
            }
        }

        return tokens

    }

    private fun tokenizeStr(string: String): List<Token> {

        str = string
        i = 0
        val tokens = mutableListOf<Token>()

        while (i < str.length) {

            when {
                isValidFirstIdChar(str[i])  -> tokens.add(id())
                str[i].isDigit()            -> tokens.add(literal())
                isValidOperatorChar(str[i]) -> tokens.add(operator())
                str[i].isWhitespace() -> ++i
                else -> throw RuntimeException("Invalid character '${str[i]}' on position $i")
            }

        }

        return tokens

    }

    private fun findMatchingParen(str: String, parenIndex: Int): Int {

        var parenCounter = 0
        var i = parenIndex

        while (i < str.length) {
            when {
                str[i] == '('     -> ++parenCounter
                str[i] == ')'     -> --parenCounter
            }
            if (parenCounter == 0) { return i }
            ++i
        }

        throw Exception("Invalid expression: Unmatched parenthesis on position $parenIndex")

    }

    private fun findNextParen(str: String, index: Int): Int {

        for (i in index until str.length) {
            if (str[i] == '(') {
                return i
            }
        }

        return -1

    }

    private fun splitOnIndices(str: String, indices: ArrayList<Int>): List<String> {

        val arr = mutableListOf<String>()
        var prev = 0

        indices.forEach {
            arr.add(str.substring(prev, it))
            prev = it
        }

        arr.add(str.substring(prev))

        return arr.filter { it.isNotBlank() }

    }

    private fun splitByParens(str: String): List<String> {

        var paren = findNextParen(str, 0)
        val parens = ArrayList<Int>()

        while (paren != -1) {

            val matching = findMatchingParen(str, paren)

            parens.add(paren)
            parens.add(matching + 1)

            paren = findNextParen(str, matching + 1)

        }

        return splitOnIndices(str, parens)

    }

}
