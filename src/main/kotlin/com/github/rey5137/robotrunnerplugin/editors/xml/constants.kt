package com.github.rey5137.robotrunnerplugin.editors.xml

import java.io.File

const val TAG_ROBOT = "robot"
const val TAG_SUITE = "suite"
const val TAG_TEST = "test"
const val TAG_KEYWORD = "kw"
const val TAG_STATUS = "status"

const val TAG_GENERATOR = "generator"
const val TAG_GENERATED = "generated"
const val TAG_ID = "id"
const val TAG_NAME = "name"
const val TAG_SOURCE = "source"
const val TAG_LIBRARY = "library"
const val TAG_ARGUMENTS = "arguments"
const val TAG_TAGS = "tags"
const val TAG_ARGUMENT = "arg"
const val TAG_TAG = "tag"
const val TAG_DOC = "doc"
const val TAG_MESSAGE = "msg"
const val TAG_TIMESTAMP = "timestamp"
const val TAG_LEVEL = "level"
const val TAG_START_TIME = "starttime"
const val TAG_END_TIME = "endtime"
const val TAG_ASSIGN = "assign"
const val TAG_VAR = "var"
const val TAG_TYPE = "type"

const val LOG_LEVEL_INFO = "INFO"
const val LOG_LEVEL_DEBUG = "DEBUG"
const val LOG_LEVEL_TRACE= "TRACE"
const val LOG_LEVEL_FAIL= "FAIL"
const val LOG_LEVEL_ERROR= "ERROR"

const val KEYWORD_TYPE_SETUP = "SETUP"
const val KEYWORD_TYPE_TEARDOWN = "TEARDOWN"
const val KEYWORD_TYPE_FOR = "FOR"
const val KEYWORD_TYPE_FORITEM = "FORITEM"

interface Element

interface HasCommonField {

    val name: String

    val status: StatusElement

    val parent: Element?
}

interface HasTagsField {

    val tags: List<String>

}

enum class DataType {
    STRING,
    INTEGER,
    NUMBER,
    BOOL,
    NONE,
    DICT,
    ARRAY
}

enum class ArgumentType {
    SINGLE,
    DICT,
    ARRAY,
    PYTHON
}

enum class AssignmentType {
    SINGLE,
    ARRAY
}

data class Argument<T>(
    val name: String = "",
    val value: T,
    val dataType: DataType,
    val argumentType: ArgumentType,
    val rawValue: String,
) {

    fun isFilePath(): Boolean {
        if(dataType != DataType.STRING)
            return false
        return try {
            File(value.toString()).exists()
        } catch(ex: Exception) {
            false
        }
    }

}

data class Variable<T>(
    val name: String = "",
    val value: T,
    val type: DataType,
    val childOrdered: Boolean = false
) {

    fun isFilePath(): Boolean {
        if(type != DataType.STRING)
            return false
        return try {
            File(value.toString()).exists()
        } catch(ex: Exception) {
            false
        }
    }
}

data class InputArgument(
    val name: String? = null,
    val value: String,
    val rawInput: String,
)

data class Assignment<T>(
    val name: String = "",
    val value: T,
    val dataType: DataType,
    val assignmentType: AssignmentType,
    val hasValue: Boolean = true,
) {

    fun isFilePath(): Boolean {
        if(dataType != DataType.STRING)
            return false
        return try {
            File(value.toString()).exists()
        } catch(ex: Exception) {
            false
        }
    }
}

val ARGUMENT_EMPTY = Argument<Any?>(value = null, dataType = DataType.NONE, argumentType = ArgumentType.SINGLE, rawValue = "")
val VARIABLE_EMPTY = Variable<Any?>(type = DataType.NONE, value = null)
val INPUT_EMPTY = InputArgument(value = "", rawInput = "")