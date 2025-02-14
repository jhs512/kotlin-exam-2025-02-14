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
        return tableDirPath
            .toFile()
            .listFiles()
            ?.filter { it.extension == "json" }
            ?.map { WiseSaying.fromJson(it.readText()) }
            ?: emptyList()
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

        tableDirPath.resolve("${wiseSaying.id}.json").toFile().delete()
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

    private fun mkdirDbDir() {
        tableDirPath.toFile().run {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    private fun saveOnDisk(wiseSaying: WiseSaying) {
        mkdirDbDir()

        val wiseSayingFile = tableDirPath.resolve("${wiseSaying.id}.json")
        wiseSayingFile.toFile().writeText(wiseSaying.json)
    }

    internal fun saveLastId(lastId: Int) {
        mkdirDbDir()

        tableDirPath.resolve("lastId.txt")
            .toFile()
            .writeText(lastId.toString())
    }

    internal fun loadLastId(): Int {
        return try {
            tableDirPath.resolve("lastId.txt")
                .toFile()
                .readText()
                .toInt()
        } catch (e: Exception) {
            0
        }
    }
}