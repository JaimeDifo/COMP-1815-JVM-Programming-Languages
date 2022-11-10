import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private JTextField SearchField;
    DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> MasterList;
    private JPanel GUI_Window;
    private JButton showRecordsButton;
    private JLabel recordLabel;
    private JButton connectButton;

    Connection con;
    Statement stmt;
    List<Book> bookList =  new ArrayList<>();

    public void Connect()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\ZAFER\\OneDrive - University of Greenwich\\Year3\\JVM\\Coursework\\SQLite Database\\mydatabase.db");
            System.out.println("Success");
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public GUI() {
        showRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery( "SELECT * FROM Books;" );

                    while(rs.next()){
                        int id = rs.getInt("ID");
                        String name = rs.getString("Title");
                        String author = rs.getString("Author");
                        String publisher = rs.getString("Publisher");
                        String subject = rs.getString("Subject");
                        int year = rs.getInt("Year");

                        Book newBook = new Book(id,name,author,publisher,subject,year);
                        bookList.add(newBook);

                        model.addElement(newBook.toString());
                        MasterList.setModel(model);
                    }
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connect();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().GUI_Window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}


