package com.fortradestudio.busbuzz.fileUtils

import android.content.Context
import android.renderscript.ScriptGroup
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.nio.Buffer
import kotlin.jvm.Throws


class FileRepositoryImpl : FileRepository {

    @Throws(IOException::class)
    override fun getCitiesFromFile(
        context: Context,
        onCitiesFetched: (List<String>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val fileName = "cities.txt"
            withContext(Dispatchers.IO) {
                val reader = context.assets.open(fileName).bufferedReader()
                val data :String = reader.use {
                    it.readText()
                }
                onCitiesFetched(data.split("\n"))
            }
        }
    }


}