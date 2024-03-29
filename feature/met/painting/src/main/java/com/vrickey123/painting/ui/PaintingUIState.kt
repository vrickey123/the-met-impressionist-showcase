package com.vrickey123.painting.ui

import com.vrickey123.met_api.MetObject
import com.vrickey123.state.UIState

data class PaintingUIState(
    override val data: MetObject = MetObject(
        objectID = -1,
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
