package com.pluralsight;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
//imports

//make everything we think we need so far
public static class TransactionApp {
    private static List<Transaction> transactions;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        transactions = TransactionManager.loadTransactions();
        while (true) showHomeScreen();
    }
//homescreen while loop(trty to make it as clean as possible bruh [use the throws thing]
    private static void showHomeScreen() throws IOException {
        System.out.println("\nD) Add Deposit\nP) Make Payment\nL) Ledger\nX) Exit");
        switch (scanner.nextLine().toUpperCase()) {
            case "D" -> addTransaction(true);
            case "P" -> addTransaction(false);
            case "L" -> showLedgerScreen();
            case "X" -> System.exit(0);
            default -> System.out.println("Invalid choice.");
        }
    }
//add whatever metthods you need here and consolidate them later into a seperate class
    private static void addTransaction(boolean isDeposit) throws IOException {
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (!isDeposit) amount = -Math.abs(amount);

        Transaction transaction = new Transaction(LocalDate.now(), java.time.LocalTime.now(), description, vendor, amount);
        transactions.add(transaction);
        TransactionManager.saveTransaction(transaction);
    }
//finishthisfinishthisfinishthisfinishthis and fix it
    private static void showLedgerScreen() throws IOException {
        System.out.println("\nA) All\nD) Deposits\nP) Payments\nR) Reports\nH) Home");
        switch (scanner.nextLine().toUpperCase()) {
            case "A":
                for (Transaction t : transactions) {
                    System.out.println(t);
                }
                break;

            case "D":
                for (Transaction t : transactions) {
                    if (t.getAmount() > 0) {
                        System.out.println(t);
                    }
                }
                break;

            case "P":
                for (com.pluralsight.Transaction t : transactions) {
                    if (t.getAmount() < 0) {
                        System.out.println(t);
                    }
                }
                break;


            default -> System.out.println("Invalid choice.");
        }
    }

    private static void showReportsScreen() throws IOException {
        System.out.println("\n1) Month To Date\n2) Previous Month\n3) Year To Date\n4) Previous Year\n5) Search by Vendor\n0) Back");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                for (Transaction t : transactions) {
                    if (t.getDate().getMonth() == LocalDate.now().getMonth()) {
                        System.out.println(t);
                    }
                }
                break;

            case "2":
                for (Transaction t : transactions) {
                    if (t.getDate().getMonth() == LocalDate.now().minusMonths(1).getMonth()) {
                        System.out.println(t);
                    }
                }
                break;

            case "3":
                for (Transaction t : transactions) {
                    if (t.getDate().getYear() == LocalDate.now().getYear()) {
                        System.out.println(t);
                    }
                }
                break;

            case "4":
                for (Transaction t : transactions) {
                    if (t.getDate().getYear() == LocalDate.now().minusYears(1).getYear()) {
                        System.out.println(t);
                    }
                }
                break;

            case "5":
                System.out.print("Vendor: ");
                {

                }}}