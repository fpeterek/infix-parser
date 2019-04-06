class LiteralParser(private val values: List<Value>) {

    init {
        handle()
    }

    private fun handle() {

        values.forEach {
            if (it.token.type == TokenType.Literal) {
                it.expr = Number(it.str.toInt())
                it.precedence = -1
            }
        }

    }

}