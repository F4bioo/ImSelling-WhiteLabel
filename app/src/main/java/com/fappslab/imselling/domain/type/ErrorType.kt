package com.fappslab.imselling.domain.type

sealed class ErrorType {
    object AccessDenied: ErrorType()
    object Unknown: ErrorType()
}
