package com.zbyte.nftsdk

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

private class Config {

    private var configuration: Properties? = null
    private var configFile = "env.ini"

    init {
        configuration = Properties()
    }

    fun load(): Boolean {
        var retrieved = false

        try {
            configuration!!.load(FileInputStream(this.configFile))
            retrieved = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return retrieved
    }

    fun store(): Boolean {
        var retrieved = false
        try {
            configuration!!.store(FileOutputStream(this.configFile), null)
            retrieved = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return retrieved
    }

    fun set(key: String, value: String) {
        configuration!!.setProperty(key, value)
    }

    fun get(key: String): String {
        return configuration?.getProperty(key)!!
    }
}