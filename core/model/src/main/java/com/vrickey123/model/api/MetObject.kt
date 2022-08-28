package com.vrickey123.model.api

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MetObject(
    @PrimaryKey
    val objectID: String,
    val isHighlight: Boolean,
    val isPublicDomain: Boolean,
    val primaryImage: String,
    val primaryImageSmall: String,
    val department: String,
    val objectName: String,
    val title: String,
    val artistDisplayName: String,
    val artistDisplayBio: String,
    val artistNationality: String,
    val objectDate: String,
    val medium: String,
    val dimensions: String,
    val objectURL: String,
    val GalleryNumber: String
)
