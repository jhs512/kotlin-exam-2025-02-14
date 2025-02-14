package com.ll

fun main() {
    println("== 명언 앱 ==")

    while (true) {
        print("명언) ")

        val input = readlnOrNull()!!.trim()

        if (input == "종료")
            break
    }
}