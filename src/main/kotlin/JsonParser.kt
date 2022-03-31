import com.google.gson.Gson
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

const val filePath = "./noteLogger3.json"

object JsonParser{

    fun readJsonObject(): MutableList<NoteDefault>? {
        var notesList: MutableList<NoteDefault>? = null

        try {
            println("Reading--------------------------------")
            println(filePath)
            val noteString = File(filePath).inputStream().bufferedReader().use { it.readText() }
            notesList = Gson().fromJson(noteString, Array<NoteDefault>::class.java).toMutableList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return notesList
    }

    fun mainWrite(note: NoteDefault) {
        var noteList = readJsonObject()?.add(NoteDefault)?: note

        try {

            PrintWriter(FileWriter(filePath)).use {
                val gson = Gson()
                val jsonString = gson.toJson(noteList)
                println("JSON: $jsonString")
                it.append(jsonString)
                it.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }
}