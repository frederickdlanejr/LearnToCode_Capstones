package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class TransactionApp {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n-----Welcome to Your Ledger Account, Freddy!------");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Please Choose an Option: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "D" -> addTransaction(true);
                case "P" -> addTransaction(false);
                case "L" -> showLedgerMenu();
                case "X" -> {
                    System.out.println("Have a nice day! Goodbye!");
                    return;
                }
                default -> System.out.println("ERROR: Invalid Choice!");
            }
        }
    }

    private static void addTransaction(boolean isDeposit) {
        System.out.print("Item(s): ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (!isDeposit) amount *= -1;

        Transaction transaction = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
        TransactionFileManager.save(transaction);
        System.out.println("Transaction Added.");
    }

    private static void showLedgerMenu() {
        List<Transaction> transactions = TransactionFileManager.load();

        while (true) {
            System.out.println("\nLedger Menu:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A" -> printTransactions(transactions);
                case "D" -> printTransactions(transactions.stream().filter(t -> t.getAmount() > 0).toList());
                case "P" -> printTransactions(transactions.stream().filter(t -> t.getAmount() < 0).toList());
                case "R" -> TransactionReport.showReportMenu(transactions, scanner);
                case "H" -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void printTransactions(List<Transaction> transactions) {
        transactions.stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .forEach(t -> System.out.println(t.toCSV()));
    }
}
