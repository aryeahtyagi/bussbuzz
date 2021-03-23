package com.fortradestudio.busbuzz.room

import android.location.Address
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Locations(
    @PrimaryKey(autoGenerate = true)
    val id : Int=0,
    val address:String,
    val latitude:Double,
    val longitude:Double
) {
}