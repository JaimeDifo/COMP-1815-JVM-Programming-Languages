class BubbleSort {
    fun bubbleSort(bookList: MutableList<Book>): Int {
        var ticks : Int
        ticks = 0
        for (i in 0 until bookList.size - 1) {
            for (j in 0 until bookList.size - i - 1) {
                ticks ++
                val book1 = bookList.get(j)
                val title1: String = book1.title.lowercase()
                val author1 = book1.author.lowercase()
                val book2 = bookList.get(j + 1)
                val title2: String = book2.title.lowercase()
                val author2 = book2.author.lowercase()

                if (author1 > author2) {
                    val swap: Book = book1
                    bookList[j] = book2
                    bookList[j + 1] = swap
                } else if (author1 == author2) {
                    if (title1 > title2) {
                        val swap: Book = book1
                        bookList[j] = book2
                        bookList[j + 1] = swap
                    }

                }
            }
        }
        println("BubbleSort - number of ticks $ticks")
        return ticks
    }
}