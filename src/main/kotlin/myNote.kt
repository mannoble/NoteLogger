/*
Simple command based note logger in different category
JSON for storing and Reading from it
 */
//Contains Category Tpe
sealed class CategoryType{
    class MeetingAgenda(val priority: String):CategoryType()
    object SideNote: CategoryType()
    object VariousNote : CategoryType()
    class Reminder(val date:String): CategoryType()
    object Other: CategoryType()
}
sealed class NoteState{
    object OnStart: NoteState() // Application on start, Listing the name of all
    object OnExit: NoteState() // Confirmation either to save or Discard
    object OnCreate: NoteState() // Creating a new note
}
sealed class NoteOptions{
//    object Error: NoteOptions()
    data class Reader(val keyWord:String = NoteDefault.title): NoteOptions()
    data class Writer(val title:String = NoteDefault.title, val noteBody:String = NoteDefault.noteBody):NoteOptions()
}
interface NoteProperties{
    var title:String
    var noteBody:String
    var category:CategoryType

    fun displayNoteContent(note: NoteProperties)
}
object NoteDefault:NoteProperties{
    override var title = "Title"
    override var noteBody = "Not Content"
    override var category: CategoryType =CategoryType.VariousNote

    override fun displayNoteContent(note: NoteProperties) = println("Display Note content !!")
}

class CallLogger(private val note:NoteDefault){
    fun getNoteAsset(state:NoteState){
        return when(state){
            is NoteState.OnStart -> getAsset(NoteOptions.Reader())
            is NoteState.OnCreate -> getAsset(NoteOptions.Writer())
            is NoteState.OnExit -> getAsset(NoteOptions.Writer(note.title, note.noteBody))
        }
    }
    private fun getAsset(options: NoteOptions){
        when(options){
            is NoteOptions.Writer -> {
                                    println("===========New Note=============")  // Creating Notes
                                        println("Title:")
                            val noteTile = readLine()
                            println("Note Body:")
                            val noteBody = readLine()
                            Start.noteContent.title = noteTile?: "Title"
                            Start.noteContent.noteBody = noteBody?: "Note Body"
                            JsonParser.mainWrite(Start.noteContent)
                          }
            is NoteOptions.Reader ->{
                                        val lists = JsonParser.readJsonObject()
                                         lists?.forEach{ it ->
                                             println("List of Records -------------")
                                             println("Title: $it.title, Category: " +
                                                            "${it.category} \n " +
                                                            "body:: ${it.noteBody}")  }
                               }

        }
    }
}

object Start{
//    private val setNoteOptions:NoteOptions = NoteOptions.Writer()
    val noteContent = NoteDefault
    val note = CallLogger(noteContent)
}

object InputReader {
    init{
        println("============Daily Note Logger!! ============".uppercase())
        print("Select an option for:\n\t 1: List New\n\t 2: Create All\n Note Categories::\n\t" +
                "m: Meeting Agenda\n\t s:SideNote \n\t v: VariousNote \n o:  Other\n")
        println("============Enter Your Selection===========".uppercase())
    }
    private val userInput: String? = readLine()
    fun inputReader(): String? {
        return userInput
    }
}

fun lunchNote(input:String?){
    when(input){
        "1" -> Start.note.getNoteAsset(NoteState.OnStart)  // Listing Notes
        "2" -> Start.note.getNoteAsset(NoteState.OnCreate) // Creating
        else -> println("HU..! Unable to process !!")
    }
}

fun main() {
    InputReader.inputReader()
    lunchNote(InputReader.inputReader())
}
