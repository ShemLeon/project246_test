package com.leoevg.project246.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Song(
    val id: Long,
    val title: String?,
    val artist: String?,
    val data: String,
    val albumId: Long
): Parcelable