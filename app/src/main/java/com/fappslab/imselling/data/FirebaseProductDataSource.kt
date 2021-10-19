package com.fappslab.imselling.data

import android.net.Uri
import com.fappslab.imselling.BuildConfig
import com.fappslab.imselling.domain.model.Product
import com.fappslab.imselling.utils.COLLECTION_PRODUCTS
import com.fappslab.imselling.utils.COLLECTION_ROOT
import com.fappslab.imselling.utils.STORAGE_IMAGES
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class FirebaseProductDataSource
@Inject
constructor(
    firebaseFirestore: FirebaseFirestore,
    firebaseStorage: FirebaseStorage
) : ProductDataSource {

    private val documentReference = firebaseFirestore
        .document("$COLLECTION_ROOT/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}")

    private val storageReference = firebaseStorage.reference

    override suspend fun getProducts(): List<Product> {
        return suspendCoroutine { continuation ->
            val productReference = documentReference.collection(COLLECTION_PRODUCTS)
            productReference.get().addOnSuccessListener { documents ->
                documents.map { it.toObject(Product::class.java) }.apply {
                    continuation.resumeWith(Result.success(this))
                }
            }

            productReference.get().addOnFailureListener { exception ->
                continuation.resumeWith(Result.failure(exception))
            }
        }
    }

    override suspend fun uploadProductImage(id: String, imageUri: Uri): String {
        return suspendCoroutine { continuation ->
            childReference(id)
                .putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val path = uri.toString()
                        continuation.resumeWith(Result.success(path))
                    }
                }.addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun saveProduct(product: Product): Product {
        return suspendCoroutine { continuation ->
            documentReference
                .collection(COLLECTION_PRODUCTS)
                .document(product.id)
                .set(product)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(product))
                }.addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun deleteProductImage(id: String): String {
        return suspendCoroutine { continuation ->
            childReference(id)
                .delete()
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(id))
                }.addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun deleteProduct(product: Product): Product {
        return suspendCoroutine { continuation ->
            documentReference
                .collection(COLLECTION_PRODUCTS)
                .document(product.id)
                .delete()
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(product))
                }.addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    private fun childReference(id: String): StorageReference {
        return storageReference.child(
            "$STORAGE_IMAGES/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/$id"
        )
    }
}
