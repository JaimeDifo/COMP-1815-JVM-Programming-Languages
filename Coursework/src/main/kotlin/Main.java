import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // The SQLite connection cursor
    static Connection con;
    // The SQLite statement
    static Statement stmt;
    // A list to store all the books
    static List<Book> bookList = new ArrayList<>();
    // A list to store all the authors
    static List<Author> authorList = new ArrayList<>();
    // A list to store all the publishers
    static List<Publisher> publisherList = new ArrayList<>();
    static String searchedItem = "";
    static Integer searchedColumn = 1;
    static Integer dataType = 0;
    static String Ticks = null;

    public static void main(String[] args) {
        // The instantiation of the GUI
        JFrame frame = new JFrame("Library Manager: Admin");
        frame.setContentPane(new GUI().GUI_Window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
    // The function to connect to the database
    public static void Connect(String dbLocation) {
        try {
            // This is a safe statement to attempt to connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            // The string parameter indicates the supposed path to the database
            con = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
        } catch (SQLException | ClassNotFoundException e) {
            // A special type of exception specific to the SQLite will be displayed in case an error occurs
            // Errors a directly taken from SQLite
            throw new RuntimeException(e);
        }
    }
    // The function to display or refresh the records
    public static void showRecords(JTable MasterTable) {
        // This is a safe statement, to prevent detrimental damage in case the statements do not match
        try {
            // The list of the book class is emptied to be filled again with the up-to-date records if not done so
            bookList = new ArrayList<Book>();
            // The statement has been primed to execute SQL statements
            stmt = con.createStatement();
            String data[][] = {};

            // The data will be retrieved based on the current data type being utilised
            if (dataType == 0){
                // All records from the books table are retrieved and stored in a result set
                ResultSet rs = stmt.executeQuery("SELECT *  FROM Books;");

                // The table initially begins as a blank table with the following column labels
                String column[] = {"id", "title", "author", "publisher", "subject ", "year"};
                DefaultTableModel tableModel = new DefaultTableModel(data, column);

                // The following loop will keep iterating as long as there are row left on the result set
                while (rs.next()) {
                    // Every field of a row is stored within a variable
                    int id = rs.getInt("ID");
                    String title = rs.getString("Title");
                    String author = rs.getString("Author");
                    String publisher = rs.getString("Publisher");
                    String subject = rs.getString("Subject");
                    int year = rs.getInt("Year");

                    // A new row containing all the variables is declared
                    Object[] newRow = new Object[]{id, title, author, publisher, subject, year};

                    // If the searched item query is a non-empty string, only the records corresponding to the search will be displayed
                    // The search is not case-sensitive
                    if (!searchedItem.equals("")) {
                        if (searchedItem.toLowerCase().equals(newRow[searchedColumn].toString().toLowerCase())) {
                            tableModel.addRow(newRow);
                        }
                    } else {
                        tableModel.addRow(newRow);
                    }

                    // A new book is declared from the variables and added into the list
                    Book newBook = new Book(id, title, author, publisher, subject, year);
                    bookList.add(newBook);
                }
                // The table is updated
                MasterTable.setModel(tableModel);
            }
            else if (dataType == 1) {
                // All records from the authors table are retrieved and stored in a result set
                ResultSet rs = stmt.executeQuery("SELECT * FROM Authors;");

                // The table initially begins as a blank table with the following column labels
                String column[] = {"id", "firstname", "lastname"};
                DefaultTableModel tableModel = new DefaultTableModel(data, column);

                // The following loop will keep iterating as long as there are row left on the result set
                while (rs.next()) {
                    // Every field of a row is stored within a variable
                    int id = rs.getInt("ID");
                    String firstname = rs.getString("Firstname");
                    String lastname = rs.getString("Lastname");

                    // A new row containing all the variables is declared
                    Object[] newRow = new Object[]{id,firstname,lastname};

                    // If the searched item query is a non-empty string, only the records corresponding to the search will be displayed
                    // The search is not case-sensitive
                    if (!searchedItem.equals("")) {
                        if (searchedItem.equals(newRow[searchedColumn].toString())) {
                            tableModel.addRow(newRow);
                        }
                    } else {
                        tableModel.addRow(newRow);
                    }

                    // A new author is declared from the variables and added into the list
                    Author newAuthor = new Author(id, firstname, lastname);
                    authorList.add(newAuthor);
                }
                // The table is updated
                MasterTable.setModel(tableModel);
            }
            else if (dataType == 2) {
                // All records from the publishers table are retrieved and stored in a result set
                ResultSet rs = stmt.executeQuery("SELECT * FROM Publishers;");

                // The table initially begins as a blank table with the following column labels
                String column[] = {"id", "name"};
                DefaultTableModel tableModel = new DefaultTableModel(data, column);

                // The following loop will keep iterating as long as there are row left on the result set
                while (rs.next()) {
                    // Every field of a row is stored within a variable
                    int id = rs.getInt("ID");
                    String name = rs.getString("Name");

                    // A new row containing all the variables is declared
                    Object[] newRow = new Object[]{id,name};

                    // If the searched item query is a non-empty string, only the records corresponding to the search will be displayed
                    // The search is not case-sensitive
                    if (!searchedItem.equals("")) {
                        if (searchedItem.equals(newRow[searchedColumn].toString())) {
                            tableModel.addRow(newRow);
                        }
                    } else {
                        tableModel.addRow(newRow);
                    }

                    // A new author is declared from the variables and added into the list
                    Publisher newPublisher = new Publisher(id, name);
                    publisherList.add(newPublisher);
                }
                // The table is updated
                MasterTable.setModel(tableModel);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
    // The function to save the current state of the records in the SQL database
    public static void saveAllRecord() {
        try {
            // This safe statement deletes all the records from the database, resets the sequence counter to 0
            // Once the counter resets, ID numbers begin to be assigned from 1 again
            stmt.executeUpdate("DELETE FROM Books");
            stmt.executeUpdate("UPDATE sqlite_sequence SET seq=0 WHERE name='Books'");

            // Lambda function to insert all the books in their current form (preferably after sorting)
            bookList.forEach(book -> {
                try {
                    int rs = stmt.executeUpdate("INSERT INTO Books (Title, Author, Publisher, Subject, Year)" +
                            "VALUES ('" + book.getTitle() + "', '" + book.getAuthor() + "', '" + book.getPublisher() +
                            "', '" + book.getSubject() + "', '" + book.getYear() + "')");
                }
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    // Sorting algorithm functions to sort the list of books
    // All of them yield the number of ticks as well
    public static void bubbleSort() {
        BubbleSort.INSTANCE.bubbleSort(bookList);
        Ticks = "BubbleSort - number of ticks " + BubbleSort.INSTANCE.getTicks();
    }
    public static void mergeSort() {
        MergeSort.INSTANCE.mergeSort(bookList);
        Ticks = "MergeSort - number of ticks = " + MergeSort.INSTANCE.getTicks();
    }
    public static void radixSort() {
        RadixSort.initRadixSort(bookList);
        Ticks = "RadixSort - number of ticks = " + RadixSort.ticks();
    }
}
