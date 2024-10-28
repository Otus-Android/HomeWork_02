package otus.homework.coroutines

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.coroutineContext

interface ImageLoader {
    fun load(uri: Uri): Flow<ImageResult>
}

sealed interface ImageResult {
    class Success(val image: Bitmap): ImageResult
    class Error(val error: String) : ImageResult
}

class ImageLoaderImpl : ImageLoader {
    override fun load(uri: Uri): Flow<ImageResult> {
        return callbackFlow {
            val callback = object: Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap == null) {
                        trySendBlocking(ImageResult.Error("Request was success but no bitmap received from a Picasso"))
                    } else {
                        trySendBlocking(ImageResult.Success(bitmap))
                    }
                    close()
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    trySendBlocking(ImageResult.Error(e.toString()))
                    close()
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            }
            Picasso.get()
                .load(uri)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(callback)
            awaitClose {
                Picasso.get().cancelRequest(callback)
            }
        }
    }
}