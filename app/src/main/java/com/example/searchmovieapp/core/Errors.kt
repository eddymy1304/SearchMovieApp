package com.example.searchmovieapp.core

class IdNotFound(message: String? = null) : Exception(message)
class NotDataFound(message: String? = null) : Exception(message)
class NetworkError(message: String? = null) : Exception(message)