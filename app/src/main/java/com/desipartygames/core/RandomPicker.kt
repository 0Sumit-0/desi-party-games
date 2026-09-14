package com.desipartygames.core

import kotlin.random.Random

fun <T> randomDifferentFrom(items: List<T>, previous: T?): T {
    require(items.isNotEmpty()) { "Cannot choose from an empty list" }
    if (items.size == 1 || previous == null) return items.random()
    return items.filterNot { it == previous }.random(Random)
}
