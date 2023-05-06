package com.vrickey123.painting.ui

import com.vrickey123.model.api.MetObject
import com.vrickey123.model.state.UIState

data class PaintingUIState(
    override val data: MetObject = MetObject(
        objectID = "",
        isHighlight = false,
        isPublicDomain = false,
        primaryImage = "",
        primaryImageSmall = "",
        department = "",
        objectName = "",
        title = "",
        artistDisplayName = "",
        artistDisplayBio = "",
        artistNationality = "",
        objectDate = "",
        medium = "",
        dimensions = "",
        objectURL = "",
        GalleryNumber = ""
    ),
    override val loading: Boolean = false,
    override val error: Throwable? = null
) : UIState
