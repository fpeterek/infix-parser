class VariableParser(private val values: MutableList<Value>) {

    init {
        handle()
    }

    private fun handle() {

        values.forEach {
            if (it.token.type == TokenType.Identifier) {
                it.expr = Variable(it.str)
                it.precedence = -1
            }
        }

    }

}
