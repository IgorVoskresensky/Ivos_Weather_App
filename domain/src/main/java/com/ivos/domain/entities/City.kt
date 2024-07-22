package com.ivos.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Int = 0,
    val name: String = "",
    val country: String = "",
) : Parcelable
