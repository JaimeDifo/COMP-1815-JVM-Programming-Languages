object BubbleSort { //Bubble Sort Algo//
    var ticks = 0       //Counts the number of movements in code.//
    fun bubbleSort(bookList: MutableList<Book>): Int {
        ticks = 0
        for (i in 0 until bookList.size - 1) {  
            for (j in 0 until bookList.size - i - 1) { //iteration to loop through the total number of the booklist.//
                ticks ++
                val book1 = bookList.get(j)  //initialization of variables, use in swapping.//
                val title1: String = book1.title.lowercase()
                val author1 = book1.author.lowercase()
                val book2 = bookList.get(j + 1)
                val title2: String = book2.title.lowercase()
                val author2 = book2.author.lowercase()

                if (author1 > author2) { //Swapping based on author's name.//
                    val swap: Book = book1
                    bookList[j] = book2
                    bookList[j + 1] = swap
                } else if (author1 == author2) { //if the authors name are the same swapping based on title of the book.//
                    if (title1 > title2) {
                        val swap: Book = book1
                        bookList[j] = book2
                        bookList[j + 1] = swap
                    }

                }
            }
        }

        return ticks //Returns the number of ticks to be displayed in the GUI.//
    }
}
