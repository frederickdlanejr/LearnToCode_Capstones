package com.pluralsight;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionReport {
    public static void showReportMenu(List<Transaction> transactions, Scanner scanner) {
        while (true) {
            System.out.println("\nReports Menu:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    filterAndPrint(transactions, t -> t.getDate().getMonth() == LocalDate.now().getMonth() && t.getDate().getYear() == LocalDate.now().getYear());
                    break;
                case "2":
                    LocalDate now = LocalDate.now();
                    LocalDate firstOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
                    LocalDate endOfLastMonth = firstOfLastMonth.withDayOfMonth(firstOfLastMonth.lengthOfMonth());
                    filterAndPrint(transactions, t -> !t.getDate().isBefore(firstOfLastMonth) && !t.getDate().isAfter(endOfLastMonth));
                    break;
                case "3":
                    filterAndPrint(transactions, t -> t.getDate().getYear() == LocalDate.now().getYear());
                    break;
                case "4":
                    filterAndPrint(transactions, t -> t.getDate().getYear() == LocalDate.now().minusYears(1).getYear());
                    break;
                case "5":
                    System.out.print("Vendor: ");
                    String vendor = scanner.nextLine();
                    filterAndPrint(transactions, t -> t.getVendor().equalsIgnoreCase(vendor));
                    break;
                case "6":
                    customSearch(transactions, scanner);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void customSearch(List<Transaction> transactions, Scanner scanner) {
        System.out.print("Start date (yyyy-MM-dd) or blank: ");
        String startInput = scanner.nextLine();
        System.out.print("End date (yyyy-MM-dd) or blank: ");
        String endInput = scanner.nextLine();
        System.out.print("Description contains: ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        String amountInput = scanner.nextLine();

        Predicate<Transaction> filter = t -> true;

        if (!startInput.isBlank()) {
            LocalDate start = LocalDate.parse(startInput);
            filter = filter.and(t -> !t.getDate().isBefore(start));
        }
        if (!endInput.isBlank()) {
            LocalDate end = LocalDate.parse(endInput);
            filter = filter.and(t -> !t.getDate().isAfter(end));
        }
        if (!description.isBlank()) {
            filter = filter.and(t -> t.getDescription().toLowerCase().contains(description.toLowerCase()));
        }
        if (!vendor.isBlank()) {
            filter = filter.and(t -> t.getVendor().equalsIgnoreCase(vendor));
        }
        if (!amountInput.isBlank()) {
            try {
                double amount = Double.parseDouble(amountInput);
                filter = filter.and(t -> t.getAmount() == amount);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format.");
            }
        }

        filterAndPrint(transactions, filter);
    }

    private static void filterAndPrint(List<Transaction> transactions, Predicate<Transaction> filter) {
        List<Transaction> filtered = transactions.stream()
                .filter(filter)
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());

        for (Transaction t : filtered) {
            System.out.println(t.toCSV());
        }
    }
}
