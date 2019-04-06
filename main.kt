
fun getInput(): String? {

    print("$ ")
    return readLine()

}

fun main(args: Array<String>) {

    val parser = ExpressionParser()

    val expr = "((5+2) - 1) * ((10+6) / (2*2))"
    println("$expr = ${parser.parse(expr)}")

    val expr2 = "5 * 5 + 5 - 25 / 5 ** 2 * -1"
    println("$expr2 = ${parser.parse(expr2)}")


    while (true) {
        val input = getInput()
        if (input.isNullOrBlank()) {
            continue
        }
        println("> ${parser.parse(input)}")
    }


}