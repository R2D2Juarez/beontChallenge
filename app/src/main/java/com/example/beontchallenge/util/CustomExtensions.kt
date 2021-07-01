package com.example.beontchallenge.util

fun Int.getPercentage(totalValue: Int): Int {
    return (this * 100) / totalValue
}