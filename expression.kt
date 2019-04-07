import java.lang.RuntimeException
import kotlin.math.pow

abstract class Expression {
    abstract fun getValue(): Int
    override fun toString(): String = "${getValue()}"
}

private object VariableRegister {

    private val varRegister = mutableMapOf<String, Int>()

    fun getVariable(variable: String) =
        varRegister[variable] ?: throw RuntimeException("Undefined variable $variable")

    fun setVariable(variable: String, value: Int): Int {
        varRegister[variable] = value
        return value
    }

}


class Number(private val value: Int) : Expression() {
    override fun getValue() = value
}

class Variable(private val value: String) : Expression() {
    override fun getValue() = VariableRegister.getVariable(value)
    fun variable() = value
}

open class Unary(protected val value: Expression) : Expression() {
    override fun getValue(): Int {
        throw NotImplementedError()
    }
}

class Negation(value: Expression) : Unary(value) {
    override fun getValue() = value.getValue() * -1
}

class Complement(value: Expression) : Unary(value) {
    override fun getValue() = value.getValue().inv()
}

fun unaryExpr(op: String, value: Expression) =
    when (op) {
        "-" -> Negation(value)
        "~" -> Complement(value)
        else -> throw RuntimeException("Error: \"op\" is not a valid unary operator")
    }

fun binaryExpr(op: String, left: Expression, right: Expression) =
    when (op) {
        "==" -> Equality(left, right)
        "!=" -> NotEqual(left, right)
        ">"  -> GreaterThan(left, right)
        ">=" -> GreaterEqual(left, right)
        "<"  -> LessThan(left, right)
        "<=" -> LessEqual(left, right)
        "+"  -> Addition(left, right)
        "-"  -> Subtraction(left, right)
        "*"  -> Multiplication(left, right)
        "/"  -> Division(left, right)
        "%"  -> Mod(left, right)
        "**" -> Power(left, right)
        ">>" -> RShift(left, right)
        "<<" -> LShift(left, right)
        "^"  -> Xor(left, right)
        "&"  -> BitAnd(left, right)
        "|"  -> BitOr(left, right)
        "="  -> Assignment(
            if (left is Variable) left
            else throw RuntimeException("Cannot assign to an Lvalue"),
            right
        )
        else -> throw RuntimeException("Error: \"op\" is not a valid unary operator")
    }

open class Binary(protected val left: Expression, protected val right: Expression) : Expression() {
    override fun getValue(): Int {
        throw NotImplementedError()
    }
}

class Equality(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = if (left.getValue() == right.getValue()) 1 else 0
}

class NotEqual(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = if (left.getValue() != right.getValue()) 1 else 0
}

class GreaterThan(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = if (left.getValue() > right.getValue()) 1 else 0
}

class GreaterEqual(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = if (left.getValue() >= right.getValue()) 1 else 0
}

class LessThan(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = if (left.getValue() < right.getValue()) 1 else 0
}

class LessEqual(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = if (left.getValue() <= right.getValue()) 1 else 0
}

class Addition(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue() + right.getValue()
}

class Subtraction(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue() - right.getValue()
}

class Multiplication(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue() * right.getValue()
}

class Division(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue() / right.getValue()
}

class Mod(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue() % right.getValue()
}

class Power(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue().toDouble().pow(right.getValue()).toInt()
}

class LShift(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue() shl right.getValue()
}

class RShift(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue() shr right.getValue()
}

class Xor(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue() xor right.getValue()
}

class BitAnd(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue() and right.getValue()
}

class BitOr(left: Expression, right: Expression) : Binary(left, right) {
    override fun getValue() = left.getValue() or right.getValue()
}

class Assignment(left: Variable, right: Expression) : Binary(left, right) {
    override fun getValue() =
        VariableRegister.setVariable((left as Variable).variable(), right.getValue())
}
