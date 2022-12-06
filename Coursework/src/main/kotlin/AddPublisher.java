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
        if (GUI.isEditing){
            try {
                ResultSet rs = GUI.stmt.executeQuery( "SELECT * FROM Publishers WHERE ID = " + GUI.index + ";" );
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
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void addRecord(){
        try{
            int rs = GUI.stmt.executeUpdate( "INSERT INTO Publishers (Name)" +
                    "VALUES ('" + nameField.getText() + "');" );
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }
    }

    public void editRecord(){
        try{
            int rs = GUI.stmt.executeUpdate( "UPDATE Publishers SET Name = '" + nameField.getText() + "' WHERE ID = "+GUI.index+" ");
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }
    }

    private void onOK() {
        if(GUI.isEditing){
            editRecord();
        }
        else {
            addRecord();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        AddPublisher dialog = new AddPublisher();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}