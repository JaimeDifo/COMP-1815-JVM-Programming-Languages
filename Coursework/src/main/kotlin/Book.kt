class Book(public var id: Int, public var author: String, public var publisher: String, public var subject: String, public var title: String, public var year: Int){
    override fun toString(): String {
        return "$id $author $publisher $subject $title $year"
    }
}