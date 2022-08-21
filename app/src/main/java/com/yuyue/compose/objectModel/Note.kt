package com.yuyue.compose.objectModel

import java.util.*
import kotlin.random.Random

data class Point(
    var x: Float,
    var y: Float
) {
    operator fun plus(p: Point): Point {
        return Point(x + p.x, y + p.y)
    }

    operator fun plusAssign(p: Point) {
        x += p.x
        y += p.y
    }

    override fun equals(other: Any?): Boolean {
        if(other !is Point) return false
        return other.x == x && other.y == y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result.toInt()
    }
}

data class Color(
    val color: Long
) {
    companion object {
        val HotPink = Color(0xFFFF7EB9)
        val Aquamarine = Color(0xFF7AFCFF)
        val PaleCanary =Color(0xFFFEFF9C)
        val Gorse = Color(0xFFFFF740)

        val defaultColors = listOf(HotPink, Aquamarine, PaleCanary, Gorse)
    }

    override fun equals(other: Any?): Boolean {
        if (other as? Color == null) return false
        if (other.color == color) return true
        return false
    }

    override fun hashCode(): Int {
        return color.hashCode()
    }
}

data class Note(
    val id: String,
    var text: String,
    var position: Point,
    val color: Color,
)  {
    companion object {
        const val SIDE_LENGTH: Int = 108
        fun generateRandomNote(): Note {
            val id = UUID.randomUUID().toString()
            val position =
                Point(
                    Random.nextFloat() * 60,
                    Random.nextFloat() * 60)
            val color = Color.defaultColors[Random.nextInt(0, 4)]
            return Note(id, "Content", position, color)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other as? Note == null) return false
        if(other.id == id && other.text == text && other.position == position && other.color == color) return true
        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }
}