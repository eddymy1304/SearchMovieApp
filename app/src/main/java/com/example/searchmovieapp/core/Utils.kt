package com.example.searchmovieapp.core

fun String?.validateNull() = if (isNullOrBlank()) null else this