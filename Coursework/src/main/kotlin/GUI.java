import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

public class GUI {
    public JTextField databaseLocationTextfield;
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
    private JLabel ticksLabel;
    static Integer index;
    public static boolean isEditing = false;
    Supplier<Integer> dataType = () -> {
        return dataCategory.getSelectedIndex(); // A lambda function which will return the index number of the current data type
    };

    public GUI() {

        //The Load Database button
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Once the button is pressed, sql connection to the database will be established
                Main.Connect(databaseLocationTextfield.getText());
                // The records to be displayed on the Table
                Main.showRecords(MasterTable);
                // The combo box initially contains "Not loaded" which is changed to the following once the database is loaded
                String[] fields = {"Books", "Authors", "Publishers"};
                // This is to prevent the user from switching tables whenever the database is not loaded which would result in an exception
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fields);
                dataCategory.setModel(model);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Since both Add and Edit features utilise the same dialogue box, there's a boolean indicating the current purpose of the dialogue box
                isEditing = false;

                // A dialogue box of different number of fields will initialise based on the datatype, since the number of fields tend to differ
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
                // The records shown on the table refreshed to include the potential changes
                Main.showRecords(MasterTable);;
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // The Edit feature requires a row to be selected to function
                if (!MasterTable.getSelectionModel().isSelectionEmpty())
                {
                    // The boolean variable is set to true, indicating that the dialogue box should utilise functions specific to editing
                    isEditing = true;

                    // The data type check has been implemented similar to the Add feature
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
                    // The records shown on the table refreshed to include the potential changes
                    Main.showRecords(MasterTable);
                }
                // In case selection has not been made, an error box will appear instead
                else {
                    errorBox errorBox = new errorBox();
                    errorBox.setLocationRelativeTo(null);
                    errorBox.pack();
                    errorBox.show();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // A row on the table has to be selected for Delete feature to function
                if (!MasterTable.getSelectionModel().isSelectionEmpty())
                {
                    // This feature does not utilise datatype check
                    // The following dialogue box will appear asking the user to confirm the deletion
                    deletionPopup deletionPopup = new deletionPopup();
                    deletionPopup.delTargetTable = (String) dataCategory.getSelectedItem();
                    deletionPopup.setLocationRelativeTo(null);
                    deletionPopup.pack();
                    deletionPopup.show();
                    // The records shown on the table refreshed to include the potential changes
                    Main.showRecords(MasterTable);;
                }
                // An error box will appear unless a row has been selected
                else
                {
                    errorBox errorBox = new errorBox();
                    errorBox.setLocationRelativeTo(null);
                    errorBox.pack();
                    errorBox.show();
                }
            }
        });
        // A selection listener for the Table, this section triggers whenever a selection has been made
        MasterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {
                    // Since the selection index cannot be outside the bounds, the following check prevents further update unless the value is valid and non-negative
                    if (MasterTable.getSelectedRow() >= 0) {
                        // The following integer is to store the ID number of the currently selected item
                        index = Integer.parseInt((MasterTable.getValueAt(MasterTable.getSelectedRow(), 0)).toString());
                    }
                }
            }
        });
        // The sort button
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // The type of the sorting algorithm is checked, and an appropriate method is called
                // The table also gets refreshed after the algorithm has run
                if ("Bubble sort" == sortCategory.getSelectedItem()) {
                    Main.bubbleSort();
                    Main.saveAllRecord();
                    Main.showRecords(MasterTable);
                }
                if ("Merge sort" == sortCategory.getSelectedItem()) {
                    Main.mergeSort();
                    Main.saveAllRecord();
                    Main.showRecords(MasterTable);
                }
                if ("Radix sort" == sortCategory.getSelectedItem()) {
                    Main.radixSort();
                    Main.saveAllRecord();
                    Main.showRecords(MasterTable);
                }
                // The time it took to complete the sorting is displayed on the label
                ticksLabel.setText(Main.Ticks);
            }
        });
        // The search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // The searched item will be determined based on the input given on the search field
                Main.searchedItem = searchField.getText();
                // The records which match the searched item will be displayed on the table
                Main.showRecords(MasterTable);
            }
        });
        // Combo box displaying the data types (books, authors, publishers)
        // It initially has only one item, "Not Loaded", to prevent the user from interacting with it
        // Considering that the interacting with it requires the database to be loaded
        // Otherwise, there could have been an error
        dataCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Based on the current datatype, column labels for the table will change accordingly
                Main.dataType = dataCategory.getSelectedIndex();
                if (Main.dataType == 0){
                    String[] fields = {"Title", "Author", "Publisher", "Subject", "Year"};
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fields);
                    searchCategory.setModel(model);
                }
                else if (Main.dataType == 1){
                    String[] fields = {"Firstname", "Lastname"};
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fields);
                    searchCategory.setModel(model);
                }
                else if (Main.dataType == 2){
                    String[] fields = {"Name"};
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fields);
                    searchCategory.setModel(model);
                }
                // The updates are displayed
                Main.showRecords(MasterTable);
            }
        });
        // Search category indicates the column to be searched in a form of a combo box
        searchCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // The index of the searched column is modified to match the index of the selected item of the combo box
                Main.searchedColumn = searchCategory.getSelectedIndex()+1;
            }
        });
    }
}


