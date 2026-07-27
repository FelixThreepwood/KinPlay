package com.kinplay.app.wyr

import org.json.JSONArray
import org.json.JSONObject

/** String-only boundary that can later be backed by SharedPreferences. */
interface WouldYouRatherStateStorage {
    fun read(): String?
    fun write(value: String)
}

class InMemoryWouldYouRatherStateStorage(
    var value: String? = null,
) : WouldYouRatherStateStorage {
    override fun read(): String? = value

    override fun write(value: String) {
        this.value = value
    }
}

/** Pure persistence adapter for deck snapshots; Android wiring stays outside the domain lane. */
class WouldYouRatherStore(
    private val storage: WouldYouRatherStateStorage,
) {
    fun load(): WouldYouRatherDeckState = storage.read()?.let(WouldYouRatherStateJson::decode)
        ?: WouldYouRatherDeckState()

    fun save(state: WouldYouRatherDeckState) {
        storage.write(WouldYouRatherStateJson.encode(state))
    }
}

object WouldYouRatherStateJson {
    private const val VERSION = 1

    fun encode(state: WouldYouRatherDeckState): String = JSONObject()
        .put("version", VERSION)
        .put("remainingIdsByCategory", stringListMapToJson(state.remainingIdsByCategory))
        .put("lastIdByCategory", stringMapToJson(state.lastIdByCategory))
        .put("randomStateByCategory", longMapToJson(state.randomStateByCategory))
        .put("libraryIdsByCategory", stringListMapToJson(state.libraryIdsByCategory))
        .toString()

    /** A persisted snapshot is disposable progress, so corrupt or newer data starts a fresh deck. */
    fun decode(json: String): WouldYouRatherDeckState = try {
        decodeSupportedState(json)
    } catch (_: Exception) {
        WouldYouRatherDeckState()
    }

    private fun decodeSupportedState(json: String): WouldYouRatherDeckState {
        val root = JSONObject(json)
        require(root.strictInt("version") == VERSION) { "Unsupported Would You Rather state version" }
        return WouldYouRatherDeckState(
            remainingIdsByCategory = root.strictObject("remainingIdsByCategory").toStringListMap(),
            lastIdByCategory = root.strictObject("lastIdByCategory").toStringMap(),
            randomStateByCategory = root.strictObject("randomStateByCategory").toLongMap(),
            libraryIdsByCategory = root.strictObject("libraryIdsByCategory").toStringListMap(),
        )
    }

    private fun stringListMapToJson(values: Map<String, List<String>>): JSONObject = JSONObject().also { json ->
        values.toSortedMap().forEach { (key, list) -> json.put(key, JSONArray(list)) }
    }

    private fun stringMapToJson(values: Map<String, String>): JSONObject = JSONObject().also { json ->
        values.toSortedMap().forEach { (key, value) -> json.put(key, value) }
    }

    private fun longMapToJson(values: Map<String, Long>): JSONObject = JSONObject().also { json ->
        values.toSortedMap().forEach { (key, value) -> json.put(key, value) }
    }

    private fun JSONObject.toStringListMap(): Map<String, List<String>> = keys().asSequence().sorted().associateWith { key ->
        val values = get(key)
        require(values is JSONArray) { "$key must be an array" }
        (0 until values.length()).map { index ->
            val value = values.get(index)
            require(value is String) { "$key[$index] must be a string" }
            value
        }
    }

    private fun JSONObject.toStringMap(): Map<String, String> =
        keys().asSequence().sorted().associateWith { key ->
            val value = get(key)
            require(value is String) { "$key must be a string" }
            value
        }

    private fun JSONObject.toLongMap(): Map<String, Long> =
        keys().asSequence().sorted().associateWith { key ->
            when (val value = get(key)) {
                is Byte -> value.toLong()
                is Short -> value.toLong()
                is Int -> value.toLong()
                is Long -> value
                else -> throw IllegalArgumentException("$key must be an integer")
            }
        }

    private fun JSONObject.strictInt(name: String): Int {
        val value = get(name)
        require(value is Int) { "$name must be an integer" }
        return value
    }

    private fun JSONObject.strictObject(name: String): JSONObject {
        val value = get(name)
        require(value is JSONObject) { "$name must be an object" }
        return value
    }
}
