class main {
     fun bubbleSort(bookList: MutableList<Book>) {
        for (i in 0 until bookList.size - 1) {
            for (j in 0 until bookList.size - i - 1) {
                val title_1: String = bookList.get(j).title.lowercase()
                val title_2: String = bookList.get(j + 1).title.lowercase()
                if (title_1>title_2) {
                    val swap: Book = bookList.get(j)
                    bookList.set(j, bookList.get(j + 1))
                    bookList.set(j + 1, swap)
                }
            }
        }
    }

//    fun mergeSort(a: IntArray, n: Int) {
//        if (n < 2) {
//            return
//        }
//        val mid = n / 2
//        val l = IntArray(mid)
//        val r = IntArray(n - mid)
//        for (i in 0 until mid) {
//            l[i] = a[i]
//        }
//        for (i in mid until n) {
//            r[i - mid] = a[i]
//        }
//        mergeSort(l, mid)
//        mergeSort(r, n - mid)
//        merge(a, l, r, mid, n - mid)
//    }
//    fun merge(
//            a: MutableList<Book>, l: MutableList<Book>, r: MutableList<Book>, left: Book, right: Book) {
//        var i = 0
//        var j = 0
//        var k = 0
//        while (i < left && j < right) {
//            if (l[i] <= r[j]) {
//                a[k++] = l[i++]
//            } else {
//                a[k++] = r[j++]
//            }
//        }
//        while (i < left) {
//            a[k++] = l[i++]
//        }
//        while (j < right) {
//            a[k++] = r[j++]
//        }
//    }
}