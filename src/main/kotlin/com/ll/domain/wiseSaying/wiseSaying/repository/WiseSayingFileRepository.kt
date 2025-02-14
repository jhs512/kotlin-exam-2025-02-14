package com.ll.domain.wiseSaying.wiseSaying.repository

import com.ll.domain.wiseSaying.wiseSaying.entity.WiseSaying
import com.ll.global.app.AppConfig
import java.nio.file.Path

class WiseSayingFileRepository : WiseSayingRepository {
    private var lastId = 0
    private val wiseSayings = mutableListOf<WiseSaying>()

    override fun save(wiseSaying: WiseSaying): WiseSaying {
        if (wiseSaying.isNew()) {
            wiseSaying.id = ++lastId
            wiseSayings.add(wiseSaying)
        }

        saveOnDisk(wiseSaying)

        return wiseSaying
    }

    override fun isEmpty(): Boolean {
        return wiseSayings.isEmpty()
    }

    override fun findAll(): List<WiseSaying> {
        return wiseSayings
    }

    override fun findById(id: Int): WiseSaying? {
        return tableDirPath
            .toFile()
            .listFiles()
            ?.find { it.name == "${id}.json" }
            ?.let { WiseSaying.fromJson(it.readText()) }
    }

    override fun delete(wiseSaying: WiseSaying) {
        wiseSayings.remove(wiseSaying)
    }

    override fun clear() {
        lastId = 0
        wiseSayings.clear()

        tableDirPath.toFile().deleteRecursively()
    }

    val tableDirPath: Path
        get() {
            return AppConfig.dbDirPath.resolve("wiseSaying")
        }

    private fun saveOnDisk(wiseSaying: WiseSaying) {
        tableDirPath.toFile().run {
            if (!exists()) {
                mkdirs()
            }
        }

        val wiseSayingFile = tableDirPath.resolve("${wiseSaying.id}.json")
        wiseSayingFile.toFile().writeText(wiseSaying.json)
    }
}