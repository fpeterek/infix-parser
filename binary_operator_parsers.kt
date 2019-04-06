import java.lang.RuntimeException

abstract class BinaryParser(protected val values: MutableList<Value>, protected val precedence: Int) {

    protected fun isBinaryOperator(i: Int) =
        values[i].isOperator && Operators.isBinary(values[i].str)

    protected fun checkOperand(value: Value) {
        if (!value.parsed) {
            throw RuntimeException("Error: Invalid operand \"${value.str}\"")
        }
    }

    protected fun checkBounds(i: Int) {
        if (i == 0 || i >= values.size - 1) {
            throw RuntimeException("Error: \"${values[i].str}\" is a binary operator")
        }
    }

    protected fun precedenceMatch(value: Value) = value.precedence == precedence

    protected open fun operator(i: Int) {
        checkBounds(i)

        val right = values[i+1]
        val left = values[i-1]

        checkOperand(right)
        checkOperand(left)

        values[i].expr = binaryExpr(values[i].str, left.expr!!, right.expr!!)
        values[i].precedence = -1
        values.removeAt(i + 1)
        values.removeAt(i - 1)

    }

}

open class LeftToRightParser(values: MutableList<Value>,
                             precedence: Int) :
    BinaryParser(values, precedence) {

    init {
        handle()
    }

    protected fun handle() {

        var it = 0
        // A forEach expression cannot be used
        // While loop must be used instead to avoid access outside list bounds
        while (it < values.size) {
            if (isBinaryOperator(it) && precedenceMatch(values[it])) {
                operator(it)
                it -= 2 // 2 elements were removed from list
            }
            ++it
        }
    }
}

open class RightToLeftParser(values: MutableList<Value>,
                             precedence: Int) :
    BinaryParser(values, precedence) {

    init {
        handle()
    }

    protected fun handle() {

        values.indices.reversed().forEach {
            if (isBinaryOperator(it) && precedenceMatch(values[it])) {
                operator(it)
            }
        }

    }

}

class AssignmentParser(values: MutableList<Value>,
                       precedence: Int) :
    RightToLeftParser(values, precedence) {

    init {
        handle()
    }

    override fun operator(i: Int) {
        checkBounds(i)

        val right = values[i+1]
        val left = values[i-1]

        checkIsLvalue(left)
        checkOperand(right)
        checkOperand(left)

        values[i].expr = Assignment(left.expr!! as Variable, right.expr!!)
        values[i].precedence = -1
        values.removeAt(i + 1)
        values.removeAt(i - 1)

    }

    private fun checkIsLvalue(value: Value) {
        if (value.token.type != TokenType.Identifier || value.expr !is Variable) {
            throw RuntimeException("Error: Assignment to Rvalue not possible")
        }
    }

}
