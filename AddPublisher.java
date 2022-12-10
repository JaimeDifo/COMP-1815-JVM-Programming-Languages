import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddPublisher extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameField;

    public AddPublisher() {

        /**
         * Add Publisher record to the Publisher table
         */

        if (GUI.isEditing){
            try {
                ResultSet rs = Main.stmt.executeQuery( "SELECT * FROM Publishers WHERE ID = " + GUI.index + ";" );
                nameField.setText(rs.getString("Name"));
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /**
                 * Add Publisher on OK button
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

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                /**
                 * Close window on CANCEL button
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

    public void addRecord(){
        /**
         * add Publisher record into Publisher table
         */
        try{
            int rs = Main.stmt.executeUpdate( "INSERT INTO Publishers (Name)" +
                    "VALUES ('" + nameField.getText() + "');" );
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }
    }

    public void editRecord(){
        /**
         * edit selcted Publisher record in Publisher table
         */
        try{
            int rs = Main.stmt.executeUpdate( "UPDATE Publishers SET Name = '" + nameField.getText() + "' WHERE ID = "+GUI.index+" ");
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }
    }

    private void onOK() {
        /**
         * Perform operations on OK
         */
        if(GUI.isEditing){
            editRecord();
        }
        else {
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
        AddPublisher dialog = new AddPublisher();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
