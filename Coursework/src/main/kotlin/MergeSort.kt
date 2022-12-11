object MergeSort { //Merge Sort//

    var ticks = 0 //Counts the number of movements in code.//

    fun mergeSort(list: List<Book>): List<Book> { //Check to ensure that the booklist has more than one record.//
        if (list.size <= 1) {
            return list
        }

        val middle = list.size / 2 //splitting the list into 2 for sorting.//
        val left = list.subList(0, middle); //Obtaining the left half of the split list.//
        val right = list.subList(middle, list.size); //Obtaining the right half of the split list.//
        ticks = 0


        return merge(mergeSort(left), mergeSort(right)) //Returning the left and right halves of the list//



    }

    private fun merge(left: List<Book>, right: List<Book>): MutableList<Book> { //initialization of variables, that wiull be used in swapping.//
        var indexLeft = 0
        var indexRight = 0
        val newList: MutableList<Book> = mutableListOf()

        while (indexLeft < left.count() && indexRight < right.count()) {
            val book1 = left[indexLeft]
            val book2 = right[indexRight]
            ticks ++

            if(book1.author < book2.author) {
                newList.add(book1)
                indexLeft++
            } else if (book2.author < book1.author) {
                newList.add(book2)
                indexRight++
            } else { // In case authors are the same, comparison by title is preferable
                if(book1.title < book2.title) {
                    newList.add(book1)
                    indexLeft++
                } else if (book2.title < book1.title) {
                    newList.add(book2)
                    indexRight++
                } else if (book1.title.equals(book2.title)) { // If these two fields are identical, both are added and the algorithm carries on
                    newList.add(book1)
                    newList.add(book2)
                    indexRight++
                    indexLeft++
                }
            }
            System.out.println("1");
        }

        while (indexLeft < left.size) { //While the left side of list is still less than its total continue to loop//
            newList.add(left[indexLeft])
            indexLeft++
            ticks ++
            System.out.println("2");


        }

        while (indexRight < right.size) { //While the right side of list is still less than its total continue to loop//
            newList.add(right[indexRight])
            indexRight++
            ticks ++
            System.out.println("3");

        }


        return newList //Return the sorted list//
    }

}
