package com.vrickey123.the_met_impressionist_showcase

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application(), ImageLoaderFactory {

    // Coil Image Cache. It is referenced under the hood by AsyncImage's
    // LocalContext.current.imageLoader.
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .build()
    }
}