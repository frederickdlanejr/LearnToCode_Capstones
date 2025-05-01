package com.pluralsight;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionGUI {
    private static JTextArea ledgerTextArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TransactionGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Transaction GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Home", createHomePanel());
        tabs.addTab("Ledger", createLedgerPanel());
        tabs.addTab("Reports", createReportPanel());

        frame.getContentPane().add(tabs);
        frame.setVisible(true);
    }

    private static JPanel createHomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setBackground(new Color(240, 240, 230));

        JButton depositButton = new JButton("Add Deposit");
        depositButton.addActionListener(e -> showTransactionDialog(true));

        JButton paymentButton = new JButton("Make Payment (Debit)");
        paymentButton.addActionListener(e -> showTransactionDialog(false));

        JButton ledgerButton = new JButton("Ledger");
        ledgerButton.addActionListener(e -> showLedger());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(depositButton);
        panel.add(paymentButton);
        panel.add(ledgerButton);
        panel.add(exitButton);
        return panel;
    }

    private static JPanel createLedgerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        ledgerTextArea = new JTextArea();
        ledgerTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ledgerTextArea.setEditable(false);
        panel.add(new JScrollPane(ledgerTextArea), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Ledger");
        refreshButton.addActionListener(e -> showLedger());
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private static JPanel createReportPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setBackground(new Color(240, 240, 230));

        String[] reportOptions = {
                "Month To Date", "Previous Month", "Year To Date", "Previous Year", "Search by Vendor", "Custom Search"
        };

        for (String label : reportOptions) {
            JButton btn = new JButton(label);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(e -> runReportOption(label));
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(btn);
        }
        return panel;
    }

    private static void runReportOption(String type) {
        LocalDate now = LocalDate.now();
        LocalDate start = null;
        LocalDate end = now;
        Predicate<Transaction> filter = t -> true;

        switch (type) {
            case "Month To Date" -> {
                start = now.withDayOfMonth(1);
                end = now;
            }
            case "Previous Month" -> {
                LocalDate first = now.minusMonths(1).withDayOfMonth(1);
                start = first;
                end = first.with(TemporalAdjusters.lastDayOfMonth());
            }
            case "Year To Date" -> {
                start = now.withDayOfYear(1);
                end = now;
            }
            case "Previous Year" -> {
                LocalDate first = now.minusYears(1).withDayOfYear(1);
                start = first;
                end = first.with(TemporalAdjusters.lastDayOfYear());
            }
            case "Search by Vendor" -> {
                String vendor = JOptionPane.showInputDialog("Enter Vendor:");
                if (vendor != null && !vendor.isBlank()) {
                    filter = t -> t.getVendor().equalsIgnoreCase(vendor);
                } else {
                    return;
                }
            }
            case "Custom Search" -> {
                JTextField startField = new JTextField();
                JTextField endField = new JTextField();
                JTextField descField = new JTextField();
                JTextField vendorField = new JTextField();
                JTextField amountField = new JTextField();

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Start date (yyyy-MM-dd) or blank:")); panel.add(startField);
                panel.add(new JLabel("End date (yyyy-MM-dd) or blank:")); panel.add(endField);
                panel.add(new JLabel("Description contains (optional):")); panel.add(descField);
                panel.add(new JLabel("Vendor (optional):")); panel.add(vendorField);
                panel.add(new JLabel("Amount (optional):")); panel.add(amountField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Custom Search", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String startStr = startField.getText();
                        String endStr = endField.getText();
                        String desc = descField.getText();
                        String vendor = vendorField.getText();
                        String amtStr = amountField.getText();

                        LocalDate sDate = startStr.isBlank() ? null : LocalDate.parse(startStr);
                        LocalDate eDate = endStr.isBlank() ? null : LocalDate.parse(endStr);
                        Double amt = amtStr.isBlank() ? null : Double.parseDouble(amtStr);

                        filter = t ->
                                (sDate == null || !t.getDate().isBefore(sDate)) &&
                                        (eDate == null || !t.getDate().isAfter(eDate)) &&
                                        (desc.isBlank() || t.getDescription().toLowerCase().contains(desc.toLowerCase())) &&
                                        (vendor.isBlank() || t.getVendor().equalsIgnoreCase(vendor)) &&
                                        (amt == null || t.getAmount() == amt);
                    } catch (DateTimeParseException | NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        if (start != null) {
            LocalDate finalStart = start;
            LocalDate finalEnd = end;
            filter = t -> !t.getDate().isBefore(finalStart) && !t.getDate().isAfter(finalEnd);
        }

        List<Transaction> transactions = TransactionFileManager.load().stream()
                .filter(filter)
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());

        ledgerTextArea.setText("");
        for (Transaction t : transactions) {
            ledgerTextArea.append(t.toCSV() + "\n");
        }
    }

    private static void showTransactionDialog(boolean isDeposit) {
        JTextField descriptionField = new JTextField();
        JTextField vendorField = new JTextField();
        JTextField amountField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Vendor:"));
        panel.add(vendorField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                isDeposit ? "Add Deposit" : "Make Payment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String description = descriptionField.getText();
                String vendor = vendorField.getText();
                double amount = Double.parseDouble(amountField.getText());
                if (!isDeposit) amount *= -1;

                Transaction t = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
                TransactionFileManager.save(t);
                JOptionPane.showMessageDialog(null, "Transaction Saved.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void showLedger() {
        List<Transaction> transactions = TransactionFileManager.load();
        ledgerTextArea.setText("");
        for (Transaction t : transactions) {
            ledgerTextArea.append(t.toCSV() + "\n");
        }
    }
}
