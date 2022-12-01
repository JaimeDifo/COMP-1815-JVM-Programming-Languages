import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private JTextField databaseLocationTextfield;
    private JPanel GUI_Window;
    private JButton loadButton;
    private JButton deleteButton;
    private JButton addButton;
    private JButton editButton;
    private JTable MasterTable;
    private JComboBox comboBox1;
    private JButton sortButton;
    private JTextField textField1;
    private JButton searchButton;
    static Connection con;
    static Statement stmt;
    public static int index;
    public static boolean isEditing = false;
    List<Book> bookList = new ArrayList<>();

    public void Connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + databaseLocationTextfield.getText());
            System.out.println("Success");
            showRecords();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void bubbleSort() {
        main test = new main();
        test.bubbleSort(bookList);
    }

    public void mergeSort() {
        main test = new main();
        bookList = test.mergeSort(bookList);
    }
    public void showRecords() {
        try {
            bookList.clear();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Books;");

            String column[] = {"id", "title", "author", "publisher", "subject", "year"};
            String data[][] = {};
            DefaultTableModel tableModel = new DefaultTableModel(data, column);
            //MasterTable.setRowSelectionInterval(0, 0);

            while (rs.next()) {
                int id = rs.getInt("ID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String publisher = rs.getString("Publisher");
                String subject = rs.getString("Subject");
                int year = rs.getInt("Year");

                tableModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Publisher"),
                        rs.getString("Subject"),
                        rs.getInt("Year")});

                Book newBook = new Book(id, title, author, publisher, subject, year);
                bookList.add(newBook);
            }
            MasterTable.setModel(tableModel);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void saveAllRecord() {
        //DELETE FROM Customers;
        try {
            // Clearing all rows from table
            stmt.executeUpdate("DELETE FROM Books");
            // Make auto increment restart from 1
            stmt.executeUpdate("UPDATE sqlite_sequence SET seq=0 WHERE name='Books'");
            for (Book book : bookList) {
                int rs = GUI.stmt.executeUpdate("INSERT INTO Books (Title, Author, Publisher, Subject, Year)" +
                        "VALUES ('" + book.getTitle() + "', '" + book.getAuthor() + "', '" + book.getPublisher() + "', '" + book.getSubject() + "', '" + book.getYear() + "')");
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public GUI() {
//        showRecordsButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                showRecords();
//            }
//        });

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

                addRecordPopup addRecordPopup = new addRecordPopup();
                addRecordPopup.setLocationRelativeTo(null);
                addRecordPopup.pack();
                addRecordPopup.show();
                showRecords();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = true;

                addRecordPopup editPopup = new addRecordPopup();
                editPopup.setLocationRelativeTo(null);
                editPopup.pack();
                editPopup.show();
                showRecords();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletionPopup deletionPopup = new deletionPopup();
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
                    if (MasterTable.getSelectedRow() > 0) {
                        index = Integer.parseInt((MasterTable.getValueAt(MasterTable.getSelectedRow(), 0)).toString());
                        System.out.println(index);
                    }
                }
            }
        });
        sortButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // get selected algo from UI (bubble, merge, radix)
//                String algo = "bubble";
//                switch (algo) {
//                    "bubble":
//                    bubbleSort();
//
//                }merge();
                mergeSort();
                saveAllRecord();
                showRecords();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Library Manager: Admin");
        frame.setContentPane(new GUI().GUI_Window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

    }
}


