//package com.pluralsight;
//import java.time.LocalDate;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Scanner;
//import java.util.stream.Collectors;
//
//public class TransactionReport {
//    private static final Scanner scanner = new Scanner(System.in);
//
//    public static void showAll() {
//        showFiltered(t -> true);
//    }
//
//    public static void showDeposits() {
//        showFiltered(t -> t.getAmount() > 0);
//    }
//
//    public static void showPayments() {
//        showFiltered(t -> t.getAmount() < 0);
//    }

//    public static void showReportMenu() {
//        while (true) {
//            System.out.println("\n--- Reports ---");
//            System.out.println("1) Month To Date");
//            System.out.println("2) Previous Month");
//            System.out.println("3) Year To Date");
//            System.out.println("4) Previous Year");
//            System.out.println("5) Search by Vendor");
//            System.out.println("6) Custom Search");
//            System.out.println("0) Back");
//            System.out.print("Choose an option: ");
//            switch (scanner.nextLine()) {
//                case "1" -> filterByDate(LocalDate.now().withDayOfMonth(1), LocalDate.now());
//                case "2" -> {
//                    LocalDate first = LocalDate.now().minusMonths(1).withDayOfMonth(1);
//                    LocalDate last = first.withDayOfMonth(first.lengthOfMonth());
//                    filterByDate(first, last);
//                }
//                case "3" -> filterByDate(LocalDate.now().withDayOfYear(1), LocalDate.now());
//                case "4" -> {
//                    LocalDate first = LocalDate.now().minusYears(1).withDayOfYear(1);
//                    LocalDate last = first.withDayOfYear(first.lengthOfYear());
//                    filterByDate(first, last);
//                }
//                case "5" -> {
//                    System.out.print("Enter vendor: ");
//                    String vendor = scanner.nextLine();
//                    showFiltered(t -> t.getVendor().equalsIgnoreCase(vendor));
//                }
//                case "6" -> customSearch();
//                case "0" -> { return; }
//                default -> System.out.println("Invalid option.");
//            }
//        }
//    }
//
//    private static void showFiltered(java.util.function.Predicate<Transaction> filter) {
//        List<Transaction> transactions = TransactionFileManager.load().stream()
//                .filter(filter)
//                .sorted(Comparator.comparing(Transaction::getDate).reversed())
//                .collect(Collectors.toList());
//
//        transactions.forEach(t -> System.out.println(t.toCSV()));
//    }
//
//    private static void filterByDate(LocalDate start, LocalDate end) {
//        showFiltered(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end));
//    }
//
//    private static void customSearch() {
//        System.out.print("Start date (yyyy-MM-dd) or blank: ");
//        String startInput = scanner.nextLine();
//        System.out.print("End date (yyyy-MM-dd) or blank: ");
//        String endInput = scanner.nextLine();
//        System.out.print("Description contains (optional): ");
//        String description = scanner.nextLine();
//        System.out.print("Vendor (optional): ");
//        String vendor = scanner.nextLine();
//        System.out.print("Amount (optional): ");
//        String amountInput = scanner.nextLine();
//
//        LocalDate start = startInput.isBlank() ? null : LocalDate.parse(startInput);
//        LocalDate end = endInput.isBlank() ? null : LocalDate.parse(endInput);
//        Double amount = amountInput.isBlank() ? null : Double.parseDouble(amountInput);
//
//        showFiltered(t ->
//                (start == null || !t.getDate().isBefore(start)) &&
//                        (end == null || !t.getDate().isAfter(end)) &&
//                        (description.isBlank() || t.getDescription().toLowerCase().contains(description.toLowerCase())) &&
//                        (vendor.isBlank() || t.getVendor().equalsIgnoreCase(vendor)) &&
//                        (amount == null || t.getAmount() == amount)
//        );
//    }
//}
