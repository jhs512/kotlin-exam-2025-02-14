package com.ll

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream

object TestRunner {
    private val originalIn: InputStream = System.`in`
    private val originalOut: PrintStream = System.out

    fun run(input: String): String {
        System.setIn(
            ByteArrayInputStream(
                input
                    .trimIndent()
                    .trim()
                    .let { "$it\n종료" }
                    .toByteArray()
            )
        )

        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)

        System.setOut(printStream)

        App().run()

        val result = outputStream
            .toString()
            .trim()
            .replace(Regex("\\r\\n"), "\n")

        System.setIn(originalIn)
        System.setOut(originalOut)

        return result
    }
}