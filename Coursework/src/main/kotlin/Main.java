import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static Connection con;
    static Statement stmt;
    static Integer index;
    public static boolean isEditing = false;
    static List<Book> bookList = new ArrayList<>();
    static List<Author> authorList = new ArrayList<>();
    static List<Publisher> publisherList = new ArrayList<>();
    static String searchedItem = "";
    static Integer searchedColumn = 1;
    static Integer dataType = 0;
    static String Ticks = null;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Library Manager: Admin");
        frame.setContentPane(new GUI().GUI_Window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
    public static void Connect(String dbLocation) {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void showRecords(JTable MasterTable) {
        try {
            bookList = new ArrayList<Book>();
            stmt = con.createStatement();
            String data[][] = {};

            if (dataType == 0){
                ResultSet rs = stmt.executeQuery("SELECT *  FROM Books;");

                String column[] = {"id", "title", "author", "publisher", "subject ", "year"};
                DefaultTableModel tableModel = new DefaultTableModel(data, column);

                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String title = rs.getString("Title");
                    String author = rs.getString("Author");
                    String publisher = rs.getString("Publisher");
                    String subject = rs.getString("Subject");
                    int year = rs.getInt("Year");

                    Object[] newRow = new Object[]{id, title, author, publisher, subject, year};

                    if (!searchedItem.equals("")) {
                        if (searchedItem.toLowerCase().equals(newRow[searchedColumn].toString().toLowerCase())) {
                            tableModel.addRow(newRow);
                        }
                    } else {
                        tableModel.addRow(newRow);
                    }

                    Book newBook = new Book(id, title, author, publisher, subject, year);
                    bookList.add(newBook);


                }
                MasterTable.setModel(tableModel);
            }
            else if (dataType == 1) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM Authors;");

                String column[] = {"id", "firstname", "lastname"};
                DefaultTableModel tableModel = new DefaultTableModel(data, column);

                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String firstname = rs.getString("Firstname");
                    String lastname = rs.getString("Lastname");

                    Object[] newRow = new Object[]{id,firstname,lastname};

                    if (!searchedItem.equals("")) {
                        if (searchedItem.equals(newRow[searchedColumn].toString())) {
                            tableModel.addRow(newRow);
                        }
                    } else {
                        tableModel.addRow(newRow);
                    }

                    Author newAuthor = new Author(id, firstname, lastname);
                    authorList.add(newAuthor);
                }
                MasterTable.setModel(tableModel);
            }
            else if (dataType == 2) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM Publishers;");

                String column[] = {"id", "name"};
                DefaultTableModel tableModel = new DefaultTableModel(data, column);

                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String name = rs.getString("Name");

                    Object[] newRow = new Object[]{id,name};

                    if (!searchedItem.equals("")) {
                        if (searchedItem.equals(newRow[searchedColumn].toString())) {
                            tableModel.addRow(newRow);
                        }
                    } else {
                        tableModel.addRow(newRow);
                    }

                    Publisher newPublisher = new Publisher(id, name);
                    publisherList.add(newPublisher);
                }
                MasterTable.setModel(tableModel);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
    public static void saveAllRecord() {
        try {
            // Clearing all rows from table
            stmt.executeUpdate("DELETE FROM Books");
            // Make auto increment restart from 1
            stmt.executeUpdate("UPDATE sqlite_sequence SET seq=0 WHERE name='Books'");

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

    public static void bubbleSort() {
        BubbleSort bubble = new BubbleSort();
        bubble.bubbleSort(bookList);
        Ticks = "BubbleSort - number of ticks " + bubble.getTicks();
    }
    public static void mergeSort() {
        MergeSort merge = new MergeSort();
        bookList = merge.mergeSort(bookList);
        Ticks = "MergeSort - number of ticks = " + merge.getTicks();
    }
    public static void radixSort() {
        RadixSort radix = new RadixSort();
        bookList = radix.initRadixSort(bookList);
        Ticks = "RadixSort - number of ticks = " + radix.ticks();
    }
}
