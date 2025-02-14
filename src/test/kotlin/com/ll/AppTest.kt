package com.ll

import kotlin.test.Test
import kotlin.test.assertContains

class AppTest {
    @Test
    fun `명언 작성`() {
        val result = TestRunner.run(
            """
            등록
            나의 죽음을 적들에게 알리지 말라.
            충무공 이순신
        """
        )

        assertContains(result, "명언 : ")
        assertContains(result, "작가 : ")
        assertContains(result, "1번 명언이 등록되었습니다.")
    }
}