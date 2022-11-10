class Book(val id: Int, val author: String, val publisher: String, val subject: String, val title: String, val year: Int){
    override fun toString(): String {
        return "$id $author $publisher $subject $title $year"
    }
}