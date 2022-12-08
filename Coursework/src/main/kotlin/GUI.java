import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GUI {
    private JTextField databaseLocationTextfield;
    public JPanel GUI_Window;
    private JButton loadButton;
    private JButton deleteButton;
    private JButton addButton;
    private JButton editButton;
    private JTable MasterTable;
    private JButton sortButton;
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox searchCategory;
    private JComboBox sortCategory;
    public JComboBox dataCategory;
    private JPanel displayTime;
    static Connection con;
    static Statement stmt;
    static Integer index;
    //index = Integer.parseInt((MasterTable.getValueAt(MasterTable.getSelectedRow(), 0)).toString());
    public static boolean isEditing = false;
    List<Book> bookList = new ArrayList<>();
    List<Author> authorList = new ArrayList<>();
    List<Publisher> publisherList = new ArrayList<>();
    Supplier<String> searchedItem = () -> {
        return searchField.getText();
    };
    Supplier<Integer> searchedColumn = () -> {
        return searchCategory.getSelectedIndex() + 1;
    };
    Supplier<Integer> dataType = () -> {
        return dataCategory.getSelectedIndex();
    };
    Integer sortColumn = 1;

    public void Connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + databaseLocationTextfield.getText());
//            con = DriverManager.getConnection("jdbc:sqlite:src/main/kotlin/mydatabase.db");
            System.out.println("Success");
            showRecords();

            String[] fields = {"Books", "Authors", "Publishers"};
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fields);
            dataCategory.setModel(model);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // BubbleSort invoke
    public void bubbleSort() {
        BubbleSort bubble = new BubbleSort();
        bubble.bubbleSort(bookList);
        System.out.println("BubbleSort - number of ticks " + bubble.getTicks());
    }

    // MergeSort invoke
    public void mergeSort() {
        MergeSort merge = new MergeSort();
        bookList = merge.mergeSort(bookList);
        System.out.println("MergeSort - number of ticks = " + merge.getTicks());
    }

    public void radixSort() {
        RadixSort radix = new RadixSort();
        bookList = radix.initRadixSort(bookList);
        System.out.println("RadixSort - number of ticks = " + radix.ticks());
    }

    public void showRecords() {
        try {
            bookList = new ArrayList<Book>();
            stmt = con.createStatement();
            String data[][] = {};

            if (dataType.get() == 0){
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

                    Object[] newRow = new Object[]{
                            rs.getInt("ID"),
                            rs.getString("Title"),
                            rs.getString("Author"),
                            rs.getString("Publisher"),
                            rs.getString("Subject"),
                            rs.getInt("Year")};

                    if (!searchedItem.get().equals("")) {
                        if (searchedItem.get().equals(newRow[searchedColumn.get()].toString())) {
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
            else if (dataType.get() == 1) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM Authors;");

                String column[] = {"id", "firstname", "lastname"};
                DefaultTableModel tableModel = new DefaultTableModel(data, column);

                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String firstname = rs.getString("Firstname");
                    String lastname = rs.getString("Lastname");

                    Object[] newRow = new Object[]{
                            rs.getInt("ID"),
                            rs.getString("Firstname"),
                            rs.getString("Lastname")};

                    if (!searchedItem.get().equals("")) {
                        if (searchedItem.get().equals(newRow[searchedColumn.get()].toString())) {
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
            else if (dataType.get() == 2) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM Publishers;");

                String column[] = {"id", "name"};
                DefaultTableModel tableModel = new DefaultTableModel(data, column);

                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String name = rs.getString("Name");

                    Object[] newRow = new Object[]{
                            rs.getInt("ID"),
                            rs.getString("Name")};

                    if (!searchedItem.get().equals("")) {
                        if (searchedItem.get().equals(newRow[searchedColumn.get()].toString())) {
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
    public void saveAllRecord() {
        try {
            // Clearing all rows from table
            stmt.executeUpdate("DELETE FROM Books");
            // Make auto increment restart from 1
            stmt.executeUpdate("UPDATE sqlite_sequence SET seq=0 WHERE name='Books'");
            for (Book book : bookList) {
                int rs = GUI.stmt.executeUpdate("INSERT INTO Books (Title, Author, Publisher, Subject, Year)" +
                        "VALUES ('" + book.getTitle() + "', '" + book.getAuthor() + "', '" + book.getPublisher() +
                        "', '" + book.getSubject() + "', '" + book.getYear() + "')");
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void search() {
        showRecords();
    }

//    public void sort() {
//        sortColumn = sortCategory.getSelectedIndex() + 1;
//        System.out.println(sortColumn);
//        showRecords();
//    }

    //PARSE THE TABLE MODEL AS AN ARRAY
//    public Object[][] getTableData(JTable table) {
//        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
//        int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
//        Object[][] tableData = new Object[nRow][nCol];
//        for (int i = 0; i < nRow; i++)
//            for (int j = 0; j < nCol; j++)
//                tableData[i][j] = dtm.getValueAt(i, j);
//        return tableData;
//    }

    public GUI() {

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connect();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = false;

                if (dataType.get() == 0){
                    addRecordPopup addRecordPopup = new addRecordPopup();
                    addRecordPopup.setLocationRelativeTo(null);
                    addRecordPopup.pack();
                    addRecordPopup.show();
                }
                else if (dataType.get() == 1){
                    addAuthor addRecordPopup = new addAuthor();
                    addRecordPopup.setLocationRelativeTo(null);
                    addRecordPopup.pack();
                    addRecordPopup.show();
                }
                else if (dataType.get() == 2){
                    AddPublisher addRecordPopup = new AddPublisher();
                    addRecordPopup.setLocationRelativeTo(null);
                    addRecordPopup.pack();
                    addRecordPopup.show();
                }
                showRecords();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = true;

                if(dataType.get() == 0){
                    addRecordPopup editPopup = new addRecordPopup();
                    editPopup.setLocationRelativeTo(null);
                    editPopup.pack();
                    editPopup.show();
                }
                else if(dataType.get() == 1){
                    addAuthor editPopup = new addAuthor();
                    editPopup.setLocationRelativeTo(null);
                    editPopup.pack();
                    editPopup.show();
                }
                else if(dataType.get() == 2){
                    AddPublisher editPopup = new AddPublisher();
                    editPopup.setLocationRelativeTo(null);
                    editPopup.pack();
                    editPopup.show();
                }
                showRecords();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletionPopup deletionPopup = new deletionPopup();
                deletionPopup.delTargetTable = (String) dataCategory.getSelectedItem();
                deletionPopup.setLocationRelativeTo(null);
                deletionPopup.pack();
                deletionPopup.show();
                showRecords();
            }
        });
        MasterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {
                    if (MasterTable.getSelectedRow() >= 0) {
                        index = Integer.parseInt((MasterTable.getValueAt(MasterTable.getSelectedRow(), 0)).toString());
                        System.out.println(index);
                    }
                }
            }
        });
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Bubble sort" == sortCategory.getSelectedItem()) {
                    bubbleSort();
                    saveAllRecord();
                    showRecords();
//                    System.out.println(sortCategory.getSelectedItem());
                }
                if ("Merge sort" == sortCategory.getSelectedItem()) {
                    mergeSort();
                    saveAllRecord();
                    showRecords();
//                    System.out.println(sortCategory.getSelectedItem());
                }
                if ("Radix sort" == sortCategory.getSelectedItem()) {
//                    System.out.println(sortCategory.getSelectedItem());
                    radixSort();
                    saveAllRecord();
                    showRecords();
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        dataCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dataType.get() == 0){
                    String[] fields = {"Title", "Author", "Publisher", "Subject", "Year"};
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fields);
                    searchCategory.setModel(model);
                }
                else if (dataType.get() == 1){
                    String[] fields = {"Firstname", "Lastname"};
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fields);
                    searchCategory.setModel(model);
                }
                else if (dataType.get() == 2){
                    String[] fields = {"Name"};
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fields);
                    searchCategory.setModel(model);
                }
                showRecords();
            }
        });


    }
}


