/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salesinvoicesystem.service;

import com.salesinvoicesystem.model.Invoice;
import com.salesinvoicesystem.model.Item;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Abd-Elrhman
 */
public class Service {

    public static ArrayList<Invoice> loadFile() {

        try {
            JFileChooser fc = new JFileChooser();

            ArrayList<Invoice> invoicesList = null;

            int result = fc.showOpenDialog(fc);
            if (result == JFileChooser.APPROVE_OPTION) {

                File headerFile = fc.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> invoicesFile = Files.readAllLines(headerPath);

                invoicesList = new ArrayList<>();

                for (String invoice : invoicesFile) {
                    try {
                        String[] headerParts = invoice.split(",");
                        int invoiceNum = Integer.parseInt(headerParts[0]);
                        String invoiceDate = headerParts[1];
                        String customerName = headerParts[2];

                        Invoice invoiceObj = new Invoice(invoiceNum, invoiceDate, customerName);
                        invoicesList.add(invoiceObj);

                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                System.out.println("////////////////////////////");

                result = fc.showOpenDialog(fc);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> items = Files.readAllLines(linePath);

                    for (String item : items) {
                        try {
                            String lineParts[] = item.split(",");
                            int invoiceNum = Integer.parseInt(lineParts[0]);
                            String itemName = lineParts[1];
                            double itemPrice = Double.parseDouble(lineParts[2]);
                            int count = Integer.parseInt(lineParts[3]);

                            for (Invoice invoice : invoicesList) {
                                if (invoice.getInvoiceNumer() == invoiceNum) {
                                    Item itemObj = new Item(invoice.getInvoiceNumer(), itemName, itemPrice, count);
                                    itemObj.setItemTotal(itemObj.getItemTotal());

                                    invoice.getItems().add(itemObj);

                                    break;
                                }
                            }

                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

            }
            return invoicesList;
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static void saveFile(ArrayList<Invoice> invoices) {

        try {

            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(fc);

            if (result == JFileChooser.APPROVE_OPTION) {
                String headers = "", items = "";

                for (Invoice invoice : invoices) {
                    String invCSV = invoice.getAsCSV();
                    headers += invCSV;
                    headers += "\n";

                    for (Item item : invoice.getItems()) {
                        String itemCSV = item.getAsCSV();
                        items += itemCSV;
                        items += "\n";
                    }
                }

                File invoicesFile = fc.getSelectedFile();
                try (FileWriter hfw = new FileWriter(invoicesFile)) {
                    hfw.write(headers);
                    hfw.flush();
                }

                result = fc.showSaveDialog(fc);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File itemFile = fc.getSelectedFile();
                    try (FileWriter lfw = new FileWriter(itemFile)) {
                        lfw.write(items);
                        lfw.flush();
                    }
                }
                JOptionPane.showMessageDialog(null, "Data Saved successfully");
            }

        } catch (HeadlessException | IOException ex) {
            ex.printStackTrace();
            System.out.println("Error");
        }
    }

}
