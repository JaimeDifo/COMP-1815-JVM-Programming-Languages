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
    DefaultListModel<String> model = new DefaultListModel<>();
    public JList<String> MasterList;
    private JPanel GUI_Window;
    private JButton showRecordsButton;
    private JLabel recordLabel;
    private JButton loadButton;
    private JButton deleteButton;
    private JButton addButton;
    private JButton editButton;
    private JTable MasterTable;
    static Connection con;
    static Statement stmt;
    public static int index;
    public static boolean isEditing = false;
    List<Book> bookList =  new ArrayList<>();

    public void Connect()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + databaseLocationTextfield.getText());
            System.out.println("Success");
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void showRecords()
    {
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Books;" );

            model.removeAllElements();

            String column[]={"id", "title", "author", "publisher", "subject", "year"};
            String data[][]={};
            DefaultTableModel tableModel = new DefaultTableModel(data, column);

            while(rs.next()){
                int id = rs.getInt("ID");
                String name = rs.getString("Title");
                String author = rs.getString("Author");
                String publisher = rs.getString("Publisher");
                String subject = rs.getString("Subject");
                int year = rs.getInt("Year");

                System.out.println(id);
                System.out.println(name);
                System.out.println(author);
                System.out.println(publisher);
                System.out.println(subject);
                System.out.println(year);

                tableModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Publisher"),
                        rs.getString("Subject"),
                        rs.getInt("Year")});

                Book newBook = new Book(id,name,author,publisher,subject,year);
                bookList.add(newBook);
                model.addElement(newBook.toString());
                MasterList.setModel(model);
            }

            //TABLE PART

//            String data[][]={ {"101","Amit","670000"},
//                    {"102","Jai","780000"},
//                    {"101","Sachin","700000"}};
            //MasterTable = new JTable(new DefaultTableModel(data, column));
            MasterTable.setModel(tableModel);
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }
    }
    public GUI() {
        showRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRecords();
            }
        });
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
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletionPopup deletionPopup = new deletionPopup();
                deletionPopup.setLocationRelativeTo(null);
                deletionPopup.pack();
                deletionPopup.show();
            }
        });
        MasterList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                index = MasterList.getSelectedIndex() + 1;
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


