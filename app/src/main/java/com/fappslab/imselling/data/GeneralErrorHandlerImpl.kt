package com.fappslab.imselling.data

import com.fappslab.imselling.domain.ErrorHandler
import com.fappslab.imselling.domain.type.ErrorType
import com.google.firebase.firestore.FirebaseFirestoreException
import javax.inject.Inject

class GeneralErrorHandlerImpl
@Inject
constructor(
): ErrorHandler {

    override fun getError(throwable: Throwable): ErrorType {
        return when (throwable) {
            is FirebaseFirestoreException -> {
                if (throwable.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                    ErrorType.AccessDenied
                } else ErrorType.Unknown
            }
            else -> ErrorType.Unknown
        }
    }
}
