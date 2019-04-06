import java.lang.RuntimeException

class UnaryOperatorParser(private val values: MutableList<Value>) {

    init {
        handle()
    }

    private fun handle() {

        values.indices.reversed().forEach {
            if (isUnaryOperator(it)) {
                operator(it)
            }
        }

    }

    private fun isUnaryOperator(i: Int) =
        values[i].isOperator &&
                Operators.isUnary(values[i].str) &&
                (i == 0 || values[i-1].isOperator)

    private fun operator(i: Int) {

        val right = values[i+1]
        checkOperand(right)

        values[i].expr = unaryExpr(values[i].str, right.expr!!)
        values[i].precedence = -1
        values.removeAt(i + 1)

    }

    private fun checkOperand(value: Value) {
        if (!value.parsed) {
            throw RuntimeException("Error: Invalid operand \"${value.str}\"")
        }
    }

}
