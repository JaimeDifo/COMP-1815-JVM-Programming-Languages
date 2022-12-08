import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;
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
    static Statement stmt;
    static Integer index;
    public static boolean isEditing = false;
    Supplier<Integer> dataType = () -> {
        return dataCategory.getSelectedIndex();
    };

    public GUI() {

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.Connect(databaseLocationTextfield.getText());
                Main.showRecords(MasterTable);
                String[] fields = {"Books", "Authors", "Publishers"};
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fields);
                dataCategory.setModel(model);
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
                Main.showRecords(MasterTable);;
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!MasterTable.getSelectionModel().isSelectionEmpty())
                {
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
                    Main.showRecords(MasterTable);
                }
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
                if (!MasterTable.getSelectionModel().isSelectionEmpty())
                {
                    deletionPopup deletionPopup = new deletionPopup();
                    deletionPopup.delTargetTable = (String) dataCategory.getSelectedItem();
                    deletionPopup.setLocationRelativeTo(null);
                    deletionPopup.pack();
                    deletionPopup.show();
                    Main.showRecords(MasterTable);;
                }
                else
                {
                    errorBox errorBox = new errorBox();
                    errorBox.setLocationRelativeTo(null);
                    errorBox.pack();
                    errorBox.show();
                }
            }
        });
        MasterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {
                    if (MasterTable.getSelectedRow() >= 0) {
                        index = Integer.parseInt((MasterTable.getValueAt(MasterTable.getSelectedRow(), 0)).toString());
                    }
                }
            }
        });
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Bubble sort" == sortCategory.getSelectedItem()) {
                    Main.bubbleSort();
                    Main.saveAllRecord();
                    Main.showRecords(MasterTable);
//                    System.out.println(sortCategory.getSelectedItem());
                }
                if ("Merge sort" == sortCategory.getSelectedItem()) {
                    Main.mergeSort();
                    Main.saveAllRecord();
                    Main.showRecords(MasterTable);
//                    System.out.println(sortCategory.getSelectedItem());
                }
                if ("Radix sort" == sortCategory.getSelectedItem()) {
//                    System.out.println(sortCategory.getSelectedItem());
                    Main.radixSort();
                    Main.saveAllRecord();
                    Main.showRecords(MasterTable);
                }
                ticksLabel.setText(Main.Ticks);
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.searchedItem = searchField.getText();
                Main.showRecords(MasterTable);
            }
        });
        dataCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                Main.showRecords(MasterTable);
            }
        });
        searchCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.searchedColumn = searchCategory.getSelectedIndex()+1;
            }
        });
    }
}


