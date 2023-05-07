package com.kisman.cc.util.enums

enum class ShadowRectSectorSides(val parentSides: List<RectSides>) {
    LeftTop(listOf(RectSides.Left, RectSides.Top)),
    RightTop(listOf(RectSides.Right, RectSides.Top)),
    RightBottom(listOf(RectSides.Right, RectSides.Bottom)),
    LeftBottom(listOf(RectSides.Left, RectSides.Bottom));

    companion object {
        fun findSides(side: RectSides) : List<ShadowRectSectorSides> {
            var list : List<ShadowRectSectorSides> = ArrayList()
            for (sectorSide in values()) {
                if (sectorSide.parentSides.contains(side)) list = list + sectorSide
            }
            return list
        }
    }
}