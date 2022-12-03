import scala.collection.mutable
import scala.collection.mutable.TreeMap
import scala.jdk.CollectionConverters._

class RadixSortV2 {
  def initRadixSort(books: java.util.List[Book]): java.util.List[Book] = {
    val scalaBooks = books.asScala.toArray
    var result = new Array[Book](books.size())
    result = radixSort(scalaBooks)
    result.toList.asJava
  }

  def radixSort(books: Array[Book]): Array[Book] = {
    // Get starting data
    val maxDigit = findMaxDigits(books)           // Find the longest value in the list of books.
    val paddedData = padString(books, maxDigit)   // Convert all the strings in the list to the same length.

    var previous = new Array[Array[Book]](books.length)
    previous = Array[Array[Book]](paddedData)
    var current = new Array[Array[Book]](books.length)

    // Start from the right hand side of all the strings and slowly move to the left.
    for (letter <- maxDigit - 1 to 0 by -1) {
      val treeMap: mutable.TreeMap[Char, Array[Book]] = mutable.TreeMap()
      var currentData = ""
      // Go through each book and add the book to their appropriate groups.
      paddedData.foreach(book => {
        currentData = book.getTitle

        // Checks if the letter that we're looking at already exists in the TreeMap, if it doesn't, create a new key.
        if (treeMap.contains(currentData(letter))) {
          // Grabs the current data stored in the treemap under that key.
          var list: Array[Book] = treeMap.apply(currentData(letter))
          // Appends the new book to the current data for that key.
          list = list :+ book
          // Puts the new data back into the TreeMap for that key.
          treeMap.put(currentData(letter), list)
        } else {
          // Create a new key in the TreeMap and put the book there.
          val temp = Array[Book](book)
          treeMap.put(currentData(letter), temp)
        }
      })
      current = treeMap.values.toArray
      previous = current
    }

    // Converts the list back into a normal list instead of Array[Array[Book]]
    var temp = Array[Book]()
    current.foreach(subarray => subarray.foreach(book => temp = temp :+ book))

    val result = unpadString(temp)
    result
  }


  /**
   * Finds the maximum length of digits in a string for authors.
   */
  def findMaxDigits(books: Array[Book]): Int = {
    var maxDigit = 0
    // Get all authors provided by books, compare the string length and find the highest value.
    books.foreach(book => {
      if (book.getTitle.length > maxDigit) {
        maxDigit = book.getTitle.length
      }
    })
    maxDigit
  }

  /**
   * Converts all the strings in the list to an equal length.
   */
  def padString(books: Array[Book], maxDigit: Int): Array[Book] = {
    val padded = books
    for (book <- padded.indices) {
      var currentData = ""
      currentData = padded(book).getTitle
      padded(book).setTitle(currentData.padTo(maxDigit, ' '))
    }
    padded
  }

  /**
   * Removes the excess digits from the padding function.
   */
  def unpadString(books: Array[Book]): Array[Book] = {
    val unpadded = books
    for (book <- books.indices) {
      var currentData = ""
      currentData = unpadded(book).getTitle
      unpadded(book).setTitle(currentData.trim)
    }
    unpadded
  }
}
