package com.kisman.cc.module.combat.autorer.util

import com.kisman.cc.util.MathUtil
import kotlin.math.pow

enum class Easing {
    OUT_QUART {
        override val opposite: Easing
            get() = OUT_QUART

        override fun inc0(x: Float): Float {
            return (1.0f - (1.0f - x).pow(2f))
        }
    },
    OUT_CUBIC {
        override val opposite: Easing
            get() = IN_CUBIC

        override fun inc0(x: Float): Float {
            return (1.0f - (1.0f - x).pow(2f))
        }
    },
    IN_CUBIC {
        override val opposite: Easing
            get() = OUT_CUBIC

        override fun inc0(x: Float): Float {
            return x.pow(2f)
        }
    };

    @Suppress("NAME_SHADOWING")
    fun inc(x: Float, min: Float, max: Float): Float {
        var min = min
        var max = max

        if (max == min) {
            return 0.0f
        } else if (max < min) {
            val oldMax = max
            max = min
            min = oldMax
        }

        if (x <= 0.0f) {
            return min
        } else if (x >= 1.0f) {
            return max
        }

        return MathUtil.lerp(min.toDouble(), max.toDouble(), inc0(x).toDouble()).toFloat()
    }

    fun inc(x: Float, max: Float): Float {
        if (max == 0.0f) {
            return 0.0f
        }

        if (x <= 0.0f) {
            return 0.0f
        } else if (x >= 1.0f) {
            return max
        }

        return inc0(x) * max
    }

    fun inc(x: Float): Float {
        if (x <= 0.0f) {
            return 0.0f
        } else if (x >= 1.0f) {
            return 1.0f
        }

        return inc0(x)
    }

    @Suppress("NAME_SHADOWING")
    fun dec(x: Float, min: Float, max: Float): Double {
        var min = min
        var max = max

        if (max == min) {
            return 0.0
        } else if (max < min) {
            val oldMax = max
            max = min
            min = oldMax
        }

        if (x <= 0.0f) {
            return max.toDouble()
        } else if (x >= 1.0f) {
            return min.toDouble()
        }

        return MathUtil.lerp(min.toDouble(), max.toDouble(), dec0(x).toDouble())
    }

    fun dec(x: Float, max: Float): Float {
        if (max == 0.0f) {
            return 0.0f
        }

        if (x <= 0.0f) {
            return max
        } else if (x >= 1.0f) {
            return 0.0f
        }

        return dec0(x) * max
    }

    fun dec(x: Float): Float {
        if (x <= 0.0f) {
            return 1.0f
        } else if (x >= 1.0f) {
            return 0.0f
        }

        return dec0(x)
    }

    abstract val opposite: Easing

    protected abstract fun inc0(x: Float): Float

    private fun dec0(x: Float): Float {
        return 1.0f - inc0(x)
    }

    companion object {
        @JvmStatic
        fun toDelta(start: Long, length: Float): Float {
            return (toDelta(start).toFloat() / length).coerceIn(0.0f, 1.0f)
        }

        @JvmStatic
        fun toDelta(start: Long): Long {
            return System.currentTimeMillis() - start
        }
    }
}