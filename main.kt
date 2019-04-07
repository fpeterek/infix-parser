
fun getInput(): String {

    while (true) {
        print("$ ")
        val input = readLine()
        if (input.isNullOrBlank()) {
            continue
        }
        return input
    }

}

fun main(args: Array<String>) {

    val parser = ExpressionParser()

    val expr = "((5+2) - 1) * ((10+6) / (2*2))"
    println("$expr = ${parser.parse(expr)}")

    val expr2 = "5 * 5 + 5 - 25 / 5 ** 2 * -1"
    println("$expr2 = ${parser.parse(expr2)}")


    while (true) {
        try {
            val input = getInput()
            println("> ${parser.parse(input)}")
        } catch (e: RuntimeException) {
            println(e.message ?: "Error: Invalid input")
        }
    }

}
