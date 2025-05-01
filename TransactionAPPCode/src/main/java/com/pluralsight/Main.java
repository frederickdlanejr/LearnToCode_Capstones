package com.pluralsight;

import org.w3c.dom.ls.LSOutput;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TransactionApp {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            showHomeScreen();
        }
    }

    private static void showHomeScreen() {
        System.out.println("\n--- Welcome Back ---");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.print("Choose an option: ");
        switch (scanner.nextLine().toUpperCase()) {
            case "D" -> addTransaction(true);
            case "P" -> addTransaction(false);
            case "L" -> showLedgerScreen();
            case "X" -> System.exit(0);
            default -> System.out.println("Invalid option.");
        }
    }

    private static void addTransaction(boolean isDeposit) {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (!isDeposit) amount *= -1;

        Transaction t = new Transaction(LocalDate.now(), java.time.LocalTime.now(), description, vendor, amount);
        TransactionFileManager.save(t);
        System.out.println("Transaction saved.");
    }

    private static void showLedgerScreen() {
        while (true) {
            System.out.println("\n--- Ledger ---");
            System.out.println("A) All");
            System.out.println("D) Deposits Only");
            System.out.println("P) Payments Only");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");
            switch (scanner.nextLine().toUpperCase()) {
                case "A" -> TransactionReport.showAll();
                case "D" -> TransactionReport.showDeposits();
                case "P" -> TransactionReport.showPayments();
                case "R" -> TransactionReport.showReportMenu();
                case "H" -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
