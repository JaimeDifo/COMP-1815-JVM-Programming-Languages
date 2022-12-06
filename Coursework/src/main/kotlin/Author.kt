class Author(public var id: Int, public var firstname: String, public var lastname: String){
    override fun toString(): String {
        return "$id $firstname $lastname"
    }
}