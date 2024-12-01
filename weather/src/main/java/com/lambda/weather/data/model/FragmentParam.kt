package com.lambdaweather.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FragmentParam(
    var news: String? = null,
    var alert: String? = null,
    var wain: String? = null,
    var from_source: String? = null
) :
    Parcelable