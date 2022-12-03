class sortingAlgos {
    fun bubbleSort(bookList: MutableList<Book>) {
        for (i in 0 until bookList.size - 1) {
            for (j in 0 until bookList.size - i - 1) {
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
    }


    fun mergeSort(list: List<Book>): List<Book> {
        if (list.size <= 1) {
            return list
        }

        val middle = list.size / 2
        val left = list.subList(0,middle);
        val right = list.subList(middle,list.size);

        return merge(mergeSort(left), mergeSort(right))
    }

    private fun merge(left: List<Book>, right: List<Book>): List<Book>  {
        var indexLeft = 0
        var indexRight = 0
        val newList : MutableList<Book> = mutableListOf()


        while (indexLeft < left.count() && indexRight < right.count()) {
            val book1 = left[indexLeft]
            val book2 = right[indexRight]
            if(book1.author < book2.author) {
                newList.add(book1)
                indexLeft++
            } else if (book2.author < book1.author) {
                newList.add(book2)
                indexRight++
            } else { // authors are the same
                if(book1.title < book2.title) {
                    newList.add(book1)
                    indexLeft++
                } else if (book2.title < book1.title) {
                    newList.add(book2)
                    indexRight++
                }
            }
        }

        while (indexLeft < left.size) {
            newList.add(left[indexLeft])
            indexLeft++
        }

        while (indexRight < right.size) {
            newList.add(right[indexRight])
            indexRight++
        }
        return newList;
    }
}