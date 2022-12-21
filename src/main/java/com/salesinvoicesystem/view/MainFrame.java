/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salesinvoicesystem.view;

import com.salesinvoicesystem.model.Invoice;
import com.salesinvoicesystem.model.Item;
import com.salesinvoicesystem.service.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Abd-Elrhman
 */
public class MainFrame extends javax.swing.JFrame {

    ArrayList<Invoice> invoices;

    public MainFrame() {
        initComponents();
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }

    private void loadFile() {
        setInvoices(Service.loadFile());
    }

    private void fireInvoiceTable() {

        Object row_data[] = new Object[4];
        DefaultTableModel model = (DefaultTableModel) invoices_table.getModel();
        model.setRowCount(0);

        getInvoices().forEach(invoice -> {

            row_data[3] = invoice.getInvoiceTotal();
            row_data[2] = invoice.getCustomer();
            row_data[1] = invoice.getInvoiceDate();
            row_data[0] = invoice.getInvoiceNumer();

            model.addRow(row_data);

        });
    }

    private void fireItemTable(ArrayList<Item> items) {

        Object row_data[] = new Object[5];
        DefaultTableModel model = (DefaultTableModel) items_table.getModel();
        model.setRowCount(0);

        int counter = 1;

        for (Item item : items) {

            row_data[4] = item.getItemTotal();
            row_data[3] = item.getItemCount();
            row_data[2] = item.getItemPrice();
            row_data[1] = item.getItemName();
            row_data[0] = counter++;

            model.addRow(row_data);

        }
    }

    private void fireRightSide() {

        getInvoices().stream().forEach(invoice -> {
            if (invoice.getInvoiceNumer() == Integer.parseInt(invoices_table.getValueAt(invoices_table.getSelectedRow(), 0).toString())) {
                fireItemTable(invoice.getItems());
                invoiceNumber_lbl.setText(String.valueOf(invoice.getInvoiceNumer()));
                edit_customerName_txt.setText(invoice.getInvoiceCustomerName());
                invoiceTotal_lbl.setText(String.valueOf(invoice.getInvoiceTotal()));
                try {
                    edit_invoiceDate_dateChooser.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(invoice.getInvoiceDate()));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void saveNewInvoice() {

        String invoiceNum = "1";
        if (invoices_table.getRowCount() != 0) {
            invoiceNum = String.valueOf(Integer.parseInt(invoices_table.getValueAt(invoices_table.getRowCount() - 1, 0).toString()) + 1);
        }

        Invoice newInvoice = new Invoice(Integer.parseInt(invoiceNum),
                new SimpleDateFormat("dd-MM-yyyy").format(newInvoiceDate_dateChooser.getDate()),
                newCustomerName_txt.getText());

        getInvoices().add(newInvoice);

        fireInvoiceTable();
    }

    private void saveNewItem() {

        for (Invoice invoice : getInvoices()) {
            if (invoice.getInvoiceNumer() == Integer.parseInt(invoiceNumber_lbl.getText())) {
                Item itemObj = new Item(invoice.getInvoiceNumer(), newItemName_txt.getText(),
                        Double.parseDouble(newItemPrice_txt.getText()), Integer.parseInt(newItemCount_txt.getText()));
                itemObj.setItemTotal(itemObj.getItemTotal());

                invoice.getItems().add(itemObj);

                fireRightSide();

                break;
            }
        }
    }

    private void deleteInvoice() {
        if (invoices_table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "please select an Invoice to delete");
        } else {

            int row = invoices_table.getSelectedRow();
            Invoice invoiceObj = new Invoice(Integer.parseInt(invoices_table.getValueAt(row, 0).toString()),
                    invoices_table.getValueAt(row, 1).toString(),
                    invoices_table.getValueAt(row, 2).toString());

            for (Invoice OldInvoice : getInvoices()) {

                if (Objects.equals(OldInvoice.getInvoiceNumer(), invoiceObj.getInvoiceNumer())) {
                    getInvoices().remove(invoiceObj);
                    fireInvoiceTable();
                    clearRightSide();
                    break;
                }
            }
        }
    }

    private void deleteItem() {
        if (items_table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "please select an item to delete");
        } else {

            int row = items_table.getSelectedRow();
            Item itemObj = new Item(Integer.parseInt(invoiceNumber_lbl.getText()), items_table.getValueAt(row, 1).toString(),
                    Double.parseDouble(items_table.getValueAt(row, 2).toString()),
                    Integer.parseInt(items_table.getValueAt(row, 3).toString()));
            itemObj.setItemTotal(itemObj.getItemTotal());

            for (Invoice invoice : getInvoices()) {
                if (invoice.getInvoiceNumer() == Integer.parseInt(invoiceNumber_lbl.getText())) {
                    for (Item oldItem : invoice.getItems()) {

                        if (oldItem.equals(itemObj)) {
                            invoice.getItems().remove(itemObj);
                            fireRightSide();
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void openNewInvoiceDialog() {
        newInvoice_dialog.setSize(420, 220);
        newInvoice_dialog.setLocationRelativeTo(null);
        newInvoice_dialog.setVisible(true);
    }

    private void openNewItemDialog() {
        if (invoices_table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "please select an invoice");
        } else {
            String itemNum = "1";
            if (items_table.getRowCount() != 0) {
                itemNum = String.valueOf(Integer.parseInt(items_table.getValueAt(items_table.getRowCount() - 1, 0).toString()) + 1);
            }
            newItemNum_lbl.setText(itemNum);
            newItem_dialog.setSize(430, 330);
            newItem_dialog.setLocationRelativeTo(null);
            newItem_dialog.setVisible(true);
        }
    }

    private void clearRightSide() {
        invoiceNumber_lbl.setText("00000");
        edit_customerName_txt.setText("");
        invoiceTotal_lbl.setText("00000");
        
        DefaultTableModel model = (DefaultTableModel) items_table.getModel();
        model.setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newInvoice_dialog = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        newInvoiceDate_dateChooser = new com.toedter.calendar.JDateChooser();
        newCustomerName_txt = new javax.swing.JTextField();
        addInvoice_btn = new javax.swing.JButton();
        cencelNewInvocie_btn = new javax.swing.JButton();
        newItem_dialog = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        newItemNum_lbl = new javax.swing.JLabel();
        newItemName_txt = new javax.swing.JTextField();
        newItemPrice_txt = new javax.swing.JTextField();
        newItemCount_txt = new javax.swing.JTextField();
        saveNewItem_btn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        invoiceNumber_lbl = new javax.swing.JLabel();
        invoiceTotal_lbl = new javax.swing.JLabel();
        edit_invoiceDate_dateChooser = new com.toedter.calendar.JDateChooser();
        edit_customerName_txt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        items_table = new javax.swing.JTable();
        deleteItem_btn = new javax.swing.JButton();
        createItem_btn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        invoices_table = new javax.swing.JTable();
        newInvoice_btn = new javax.swing.JButton();
        deleteInvoice_btn = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        loadFile_menuItem = new javax.swing.JMenuItem();
        saveFile_menuItem = new javax.swing.JMenuItem();

        newInvoice_dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        newInvoice_dialog.setTitle("New Invoice");
        newInvoice_dialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Date");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Customer Name");

        newInvoiceDate_dateChooser.setDateFormatString("dd-MM-yyyy");

        newCustomerName_txt.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        addInvoice_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        addInvoice_btn.setText("Add");
        addInvoice_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addInvoice_btnActionPerformed(evt);
            }
        });

        cencelNewInvocie_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cencelNewInvocie_btn.setText("Cancel");
        cencelNewInvocie_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cencelNewInvocie_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newCustomerName_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newInvoiceDate_dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(addInvoice_btn)
                        .addGap(72, 72, 72)
                        .addComponent(cencelNewInvocie_btn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(newInvoiceDate_dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel5)))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(newCustomerName_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addInvoice_btn)
                    .addComponent(cencelNewInvocie_btn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout newInvoice_dialogLayout = new javax.swing.GroupLayout(newInvoice_dialog.getContentPane());
        newInvoice_dialog.getContentPane().setLayout(newInvoice_dialogLayout);
        newInvoice_dialogLayout.setHorizontalGroup(
            newInvoice_dialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        newInvoice_dialogLayout.setVerticalGroup(
            newInvoice_dialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        newItem_dialog.setTitle("New Item");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setText("NO.");

        jLabel8.setText("Item Name");

        jLabel9.setText("Item Price");

        jLabel10.setText("Count");

        newItemNum_lbl.setText("0000");

        newItemCount_txt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                newItemCount_txtKeyTyped(evt);
            }
        });

        saveNewItem_btn.setText("Save");
        saveNewItem_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveNewItem_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(84, 84, 84)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveNewItem_btn)
                    .addComponent(newItemCount_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newItemPrice_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newItemNum_lbl)
                    .addComponent(newItemName_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(newItemNum_lbl))
                .addGap(36, 36, 36)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(newItemName_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(newItemPrice_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(newItemCount_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(saveNewItem_btn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout newItem_dialogLayout = new javax.swing.GroupLayout(newItem_dialog.getContentPane());
        newItem_dialog.getContentPane().setLayout(newItem_dialogLayout);
        newItem_dialogLayout.setHorizontalGroup(
            newItem_dialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        newItem_dialogLayout.setVerticalGroup(
            newItem_dialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIG");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Invoice Number ");

        jLabel2.setText("Invoice Date");

        jLabel3.setText("Customer Name");

        jLabel4.setText("Invoice Total");

        invoiceNumber_lbl.setText("00000");

        invoiceTotal_lbl.setText("00000");

        edit_invoiceDate_dateChooser.setDateFormatString("dd-MM-yyyy");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(55, 55, 55)
                        .addComponent(invoiceNumber_lbl))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(78, 78, 78)
                        .addComponent(edit_invoiceDate_dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(56, 56, 56)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(invoiceTotal_lbl)
                            .addComponent(edit_customerName_txt))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(invoiceNumber_lbl))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(edit_invoiceDate_dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(edit_customerName_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(invoiceTotal_lbl))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Invoice Items", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));

        items_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NO.", "Item Name", "Item Price", "Count", "Item total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(items_table);

        deleteItem_btn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        deleteItem_btn.setText("Delete Item");
        deleteItem_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItem_btnActionPerformed(evt);
            }
        });

        createItem_btn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        createItem_btn.setText("create New Item");
        createItem_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createItem_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(createItem_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteItem_btn)
                        .addGap(54, 54, 54))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createItem_btn)
                    .addComponent(deleteItem_btn))
                .addGap(39, 39, 39))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Invoices Table", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 16))); // NOI18N

        invoices_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Date", "Customer", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        invoices_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoices_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(invoices_table);

        newInvoice_btn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        newInvoice_btn.setText("Create New Invoice");
        newInvoice_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newInvoice_btnActionPerformed(evt);
            }
        });

        deleteInvoice_btn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        deleteInvoice_btn.setText("Delete Invoice");
        deleteInvoice_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteInvoice_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(newInvoice_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(deleteInvoice_btn)
                .addGap(95, 95, 95))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newInvoice_btn)
                    .addComponent(deleteInvoice_btn))
                .addGap(39, 39, 39))
        );

        jMenu1.setText("File");

        loadFile_menuItem.setText("Load File");
        loadFile_menuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFile_menuItemActionPerformed(evt);
            }
        });
        jMenu1.add(loadFile_menuItem);

        saveFile_menuItem.setText("Save File");
        saveFile_menuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFile_menuItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveFile_menuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void newInvoice_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newInvoice_btnActionPerformed
        openNewInvoiceDialog();
    }//GEN-LAST:event_newInvoice_btnActionPerformed

    private void cencelNewInvocie_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cencelNewInvocie_btnActionPerformed
        newInvoice_dialog.dispose();
    }//GEN-LAST:event_cencelNewInvocie_btnActionPerformed

    private void loadFile_menuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFile_menuItemActionPerformed
        loadFile();
        fireInvoiceTable();
    }//GEN-LAST:event_loadFile_menuItemActionPerformed

    private void invoices_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoices_tableMouseClicked
        fireRightSide();
    }//GEN-LAST:event_invoices_tableMouseClicked

    private void createItem_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createItem_btnActionPerformed
        openNewItemDialog();
    }//GEN-LAST:event_createItem_btnActionPerformed

    private void saveNewItem_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveNewItem_btnActionPerformed
        saveNewItem();
        newItem_dialog.dispose();
    }//GEN-LAST:event_saveNewItem_btnActionPerformed

    private void newItemCount_txtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newItemCount_txtKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLetter(c) && !evt.isAltDown()) {
            evt.consume();
        }
    }//GEN-LAST:event_newItemCount_txtKeyTyped

    private void deleteItem_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItem_btnActionPerformed
        deleteItem();
    }//GEN-LAST:event_deleteItem_btnActionPerformed

    private void saveFile_menuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFile_menuItemActionPerformed
        Service.saveFile(getInvoices());
    }//GEN-LAST:event_saveFile_menuItemActionPerformed

    private void addInvoice_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addInvoice_btnActionPerformed
        saveNewInvoice();
        newInvoice_dialog.dispose();
    }//GEN-LAST:event_addInvoice_btnActionPerformed

    private void deleteInvoice_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteInvoice_btnActionPerformed
        deleteInvoice();
    }//GEN-LAST:event_deleteInvoice_btnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addInvoice_btn;
    private javax.swing.JButton cencelNewInvocie_btn;
    private javax.swing.JButton createItem_btn;
    private javax.swing.JButton deleteInvoice_btn;
    private javax.swing.JButton deleteItem_btn;
    private javax.swing.JTextField edit_customerName_txt;
    private com.toedter.calendar.JDateChooser edit_invoiceDate_dateChooser;
    private javax.swing.JLabel invoiceNumber_lbl;
    private javax.swing.JLabel invoiceTotal_lbl;
    private javax.swing.JTable invoices_table;
    private javax.swing.JTable items_table;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem loadFile_menuItem;
    private javax.swing.JTextField newCustomerName_txt;
    private com.toedter.calendar.JDateChooser newInvoiceDate_dateChooser;
    private javax.swing.JButton newInvoice_btn;
    private javax.swing.JDialog newInvoice_dialog;
    private javax.swing.JTextField newItemCount_txt;
    private javax.swing.JTextField newItemName_txt;
    private javax.swing.JLabel newItemNum_lbl;
    private javax.swing.JTextField newItemPrice_txt;
    private javax.swing.JDialog newItem_dialog;
    private javax.swing.JMenuItem saveFile_menuItem;
    private javax.swing.JButton saveNewItem_btn;
    // End of variables declaration//GEN-END:variables
}
