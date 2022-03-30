/*
Simple command based note logger in different category
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
    object Error: NoteOptions()
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

//Source For Storage or Reading
//interface NoteViewer{
//    fun getNoteAsset():NoteOptions
//}
// App container or Starting point

class CallLogger(private val note:NoteDefault){
    fun getNoteAsset(state:NoteState){
        return when(state){
            is NoteState.OnStart -> getAsset(NoteOptions.Reader(note.title))
            is NoteState.OnCreate -> getAsset(NoteOptions.Writer(note.title, note.noteBody))
//            is NoteState.OnPause -> getAsset(NoteOptions.Writer(note.title, note.noteBody))
            is NoteState.OnExit -> getAsset(NoteOptions.Writer(note.title, note.noteBody))
        }
    }
    private fun getAsset(options: NoteOptions){
        when(options){
            is NoteOptions.Writer -> println("Note Writer View !!")
            is NoteOptions.Reader -> println("Note Reader View, Listing all note records !!")
            is NoteOptions.Error -> println("Note Error View !!")
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
        print("Select an option for:\n\t 1: Create New\n\t 2: List All\n Note Categories::\n\t" +
                "m: Meeting Agenda\n\t s:SideNote \n\t v: VariousNote \n o:  Other\n")
        println("============Enter Your Selection===========".uppercase())
    }
    private val userInput: String? = readLine()
    fun inputReader(): String? {
        return InputReader.userInput
    }
}

fun lunchNote(input:String?){
    return when(input){
        "1" -> println("Your Input value is :: $input") // Listing Notes
        "2" -> {
            println("===========New Note=============")  // Creating Notes
            println("Title:")
            val noteTile = readLine()
            println("Note Body:")
            val noteBody = readLine()
            Start.noteContent.title = noteTile?: "Title"
            Start.noteContent.noteBody = noteBody?: "Note Body"
            Start.note.getNoteAsset(NoteState.OnCreate)
        }
        else -> println("HU..! Unable to process !!")
    }
}

fun main() {
    InputReader.inputReader()
    lunchNote(InputReader.inputReader())
}