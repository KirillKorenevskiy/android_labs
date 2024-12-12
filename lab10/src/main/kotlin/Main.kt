
val constantValue: Int = 42
var variableValue: Double = 3.14
var anotherVariable: String = "Hello, Kotlin"

val implicitValue = 100
var implicitString = "Learning Kotlin"


fun main() {
    println("------------ex 2-------------")

    variableValue += 1.0
    anotherVariable = "Hello, World!"

    println("const: $constantValue")
    println("changable variable: $variableValue")
    println("String: $anotherVariable")

    val constantValue: Int = 10

    var userInput: Int? = null

    println("Enter number (or leave empty): ")
    val input = readLine()

    userInput = if (input.isNullOrEmpty()) {
        null
    } else {
        input.toIntOrNull()
    }

    println("const: $constantValue")
    println("userInput: $userInput")

    println("------------ex 3------------- ")

    val email = "example@mail.com"
    val password = "mypassword"

    if (isValid(email, password)) {
        println("Login and password ok")
    } else {
        println("wrong login and password")
    }

    val day = 1
    val month = 1
    val year = 2024
    val result = checkDay(day, month, year)
    println("Date: $day.$month.$year -  $result")


    try {
        println("Result: ${doOperation(10, 5, '+')}")
    } catch (e: Exception) {
        println(e.message)
    }


    val array1 = intArrayOf(1, 3, 5, 7, 9)
    val array2 = intArrayOf(2, 4, 6, 8, 6)
    val array3 = intArrayOf(1, 1, 1)
    val array4 = intArrayOf()
    println("max in array1: ${array1.indexOfMax()}")
    println("max in array2: ${array2.indexOfMax()}")
    println("max in array: ${array3.indexOfMax()}")
    println("max in array4: ${array4.indexOfMax()}")


    val str1 = "abcdef"
    val str2 = "abcfgh"
    println("Amount of coincidences: ${str1.coincidence(str2)}")


    println("------------ex 4------------- ")

    val number1 = 5
    val factorialResult1 = factorial(number1)
    println("factorial $number1 (iterable): $factorialResult1")

    val number2 = 6
    val factorialResult2 = factorialRecursive(number2)
    println("factorial $number2 (recursive): $factorialResult2")

    try {
        val negativeNumber = -3
        println("factorial $negativeNumber: ${factorial(negativeNumber)}")
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
    }

    try {
        val anotherNegativeNumber = -4
        println("factorial $anotherNegativeNumber: ${factorialRecursive(anotherNegativeNumber)}")
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
    }

    val primeList = mutableListOf<Int>()
    val primeArray = IntArray(10)
    var primeArrayIndex = 0
    var a = 0;

    for (i in 2 until 10000) {
        if (isPrime(i)) {
            if (primeList.size < 20) {
                primeList.add(i)
            } else if (primeArrayIndex < 10) {
                primeArray[primeArrayIndex] = i
                primeArrayIndex++
            }
            a++
        }
    }

    println("Amount of simple numbers less than 10,000: $a")
    println("First 20 simple numbers in List: $primeList")
    println("Next 10 s n in array: ${primeArray.filter { it != 0 }}")



    println("------------ex 5------------- ")

    // Часть a
    val numbers = listOf(1, 2, 3, 4, 5)
    println(containsIn(numbers, 3))
    println(containsIn(numbers, 6))

    // Часть b
    val numberList = mutableListOf(1, 2, 3, 4, 5)
    numberList += listOf(3, 5, 7, 9, 11)
    val uniqueOddNumbers = numberList.distinct().filter { it % 2 != 0 }
    uniqueOddNumbers.forEach { println(it) }
    println("Simple numbers: ${uniqueOddNumbers.filter(::isPrime)}")

    val firstOddNumber = uniqueOddNumbers.find { it > 5 }
    println("The first nechet number is more than 5: $firstOddNumber")

    val grouped = uniqueOddNumbers.groupBy { it % 3 }
    println("Grouping by the remainder of the division by 3: $grouped")

    val allOdd = uniqueOddNumbers.all { it % 2 != 0 }
    println("all numbers nechet: $allOdd")

    val anyGreaterThan10 = uniqueOddNumbers.any { it > 10 }
    println("there are numbers more than 10: $anyGreaterThan10")

    val (first, second) = uniqueOddNumbers.take(2)
    println("first element: $first, second element: $second")

    // Часть c
    val results = mapOf(
        "Korenevskiy" to 40,
        "Lagun" to 35,
        "Yuhnevich" to 20,
        "Bernovskiy" to 5
    )

    val grades = results.mapValues { (name, score) ->
        when (score) {
            40 -> 10
            39 -> 9
            38 -> 8
            in 35..37 -> 7
            in 32..34 -> 6
            in 29..31 -> 5
            in 25..28 -> 4
            in 22..24 -> 3
            in 19..21 -> 2
            in 0..18 -> 1
            else -> 0
        }
    }

    val gradeCounts = grades.values.groupingBy { it }.eachCount()
    println("marks: $gradeCounts")

    val hasUnsatisfactory = grades.values.any { it < 3 }
    println("there are bad marks: $hasUnsatisfactory")
}

fun isValid(login: String, password: String): Boolean {
    fun notNull(value: String?): Boolean = if (value.isNullOrEmpty()) false else true
    return notNull(login) && notNull(password) &&
            login.contains("@") && login.contains(".") &&
            password.length in 6..12 && !password.contains(" ")
}

enum class Holiday(val day: Int, val month: Int) {
    NEW_YEAR(1, 1),
    CHRISTMAS(7, 1),
    WOMEN_DAY(8, 3),
    LABOR_DAY(1, 5),
    VICTORY_DAY(9, 5),
    INDEPENDENCE_DAY(24, 8);
}

fun checkDay(day: Int, month: Int, year: Int?): String {
    if (year == null) return "Incorrect data"
    val isHoliday = Holiday.values().any { it.day == day && it.month == month }
    val isWeekend = when (java.time.LocalDate.of(year, month, day).dayOfWeek) {
        java.time.DayOfWeek.SATURDAY, java.time.DayOfWeek.SUNDAY -> true
        else -> false
    }
    return when {
        isHoliday -> "Holiday"
        isWeekend -> "Weekend"
        else -> "WorkDay"
    }
}

fun doOperation(a: Int, b: Int, operation: Char): Double {
    return when (operation) {
        '+' -> (a + b).toDouble()
        '-' -> (a - b).toDouble()
        '*' -> (a * b).toDouble()
        '/' -> if (b != 0) a.toDouble() / b else throw IllegalArgumentException("division by zero")
        else -> throw IllegalArgumentException("bad operation: $operation")
    }
}

fun IntArray.indexOfMax(): Int? {
    if (isEmpty()) return null

    val max = maxOrNull() ?: return null
    val indices = filter { it == max }

    return if (indices.size > 1) null else indexOf(max)
}


fun String.coincidence(other: String): Int {
    return this.zip(other).count { (a, b) -> a == b }
}


fun factorial(n: Int): Double {
    if (n < 0) throw IllegalArgumentException("factorial is not defined for negatives")
    var result = 1.0
    for (i in 1..n) {
        result *= i
    }
    return result
}

fun factorialRecursive(n: Int): Double {
    if (n < 0) throw IllegalArgumentException("factorial is not defined for integers")
    return if (n == 0) 1.0 else n * factorialRecursive(n - 1)
}

fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    if (n == 2) return true
    if (n % 2 == 0) return false

    val limit = Math.sqrt(n.toDouble()).toInt()
    for (i in 3..limit step 2) {
        if (n % i == 0) return false
    }
    return true
}


fun containsIn(collection: Collection<Int>, value: Int): Boolean = collection.any { it == value }
