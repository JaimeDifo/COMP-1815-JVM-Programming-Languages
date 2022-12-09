object MergeSort {

    var ticks = 0

    fun mergeSort(list: List<Book>): List<Book> {
        if (list.size <= 1) {
            return list
        }

        val middle = list.size / 2
        val left = list.subList(0, middle);
        val right = list.subList(middle, list.size);
        ticks = 0


        return merge(mergeSort(left), mergeSort(right))



    }

    private fun merge(left: List<Book>, right: List<Book>): MutableList<Book> {
        var indexLeft = 0
        var indexRight = 0
        val newList: MutableList<Book> = mutableListOf()




        while (indexLeft < left.count() && indexRight < right.count()) {
            val book1 = left[indexLeft]
            val book2 = right[indexRight]
            ticks ++


            if (book1.author.lowercase() < book2.author.lowercase()) {
                newList.add(book1)
                indexLeft++


            } else if (book2.author < book1.author) {
                newList.add(book2)
                indexRight++


            } else { // authors are the same
                if (book1.title < book2.title) {
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
            ticks ++


        }

        while (indexRight < right.size) {
            newList.add(right[indexRight])
            indexRight++
            ticks ++

        }


        return newList
    }

}