package com.fappslab.imselling.domain

import com.fappslab.imselling.domain.type.ErrorType

interface ErrorHandler {

    fun getError(throwable: Throwable): ErrorType
}
