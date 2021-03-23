package com.fortradestudio.busbuzz.fileUtils

import android.content.Context

interface FileRepository {

     fun getCitiesFromFile(context: Context,onCitiesFetched:(List<String>)->Unit)

}