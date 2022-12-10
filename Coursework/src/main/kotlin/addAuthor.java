import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addAuthor extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField firstnameField;
    private JTextField lastnameField;



    public addAuthor() {
        /**
         *  Adding Authors into database
         */

        if (GUI.isEditing) {
            try {
                ResultSet rs = Main.stmt.executeQuery("SELECT * FROM Authors WHERE ID = " + GUI.index + ";");
                firstnameField.setText(rs.getString("Firstname"));
                lastnameField.setText(rs.getString("Lastname"));
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent e) {
                /**
                 * Perform action on OK button
                 */
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /**
                 * Perform action on CANCEL button
                 */
                onCancel();
            }
        });

        /**
         * call onCancel() when cross is clicked
         */
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                /**
                 * Close window on CANCEL button.
                 */
                onCancel();
            }
        });


        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /**
                 * call onCancel() on ESCAPE
                 */
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void addRecord() {
        /**
         * add Author Firstname and Lastname into Authors Table
         */
        try {
            int rs = Main.stmt.executeUpdate("INSERT INTO Authors (Firstname, Lastname)" +
                    "VALUES ('" + firstnameField.getText() + "', '" + lastnameField.getText() + "');");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void editRecord() {
        /**
         * add Author Firstname and Lastname into Authors Table
         */
        try {
            int rs = Main.stmt.executeUpdate("UPDATE Authors SET Firstname = '" + firstnameField.getText() +
                    "', Lastname = '" + lastnameField.getText() + "' WHERE ID = " + GUI.index + " ");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void onOK() {
        /**
         * Perform operations on OK
         */
        if (GUI.isEditing) {
            editRecord();
        } else {
            addRecord();
        }
        dispose();
    }

    private void onCancel() {

        /**
         * Perform action on CANCEL button
         */

        dispose();
    }

    public static void main(String[] args) {
        addAuthor dialog = new addAuthor();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
