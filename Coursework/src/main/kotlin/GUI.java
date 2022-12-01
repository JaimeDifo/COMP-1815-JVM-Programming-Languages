import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private JTextField databaseLocationTextfield;
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

            String column[]={"id", "title", "author", "publisher", "subject", "year"};
            String data[][]={};
            DefaultTableModel tableModel = new DefaultTableModel(data, column);
            //MasterTable.setRowSelectionInterval(0, 0);

            while(rs.next()){
                int id = rs.getInt("ID");
                String name = rs.getString("Title");
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

                Book newBook = new Book(id,name,author,publisher,subject,year);
                bookList.add(newBook);
            }
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
        MasterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if(event.getValueIsAdjusting()) {
                    if(MasterTable.getSelectedRow() > 0) {
                        index = Integer.parseInt((MasterTable.getValueAt(MasterTable.getSelectedRow(), 0)).toString());
                        System.out.println(index);
                    }
                }
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


