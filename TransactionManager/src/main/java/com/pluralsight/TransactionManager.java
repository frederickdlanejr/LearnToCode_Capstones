package com.pluralsight;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private static final String FILE_NAME = "transactions.csv";

    public static List<Transaction> loadTransactions() throw IOException {
        List<Transaction> transactions = new ArrayList<>();
        if (!Files.exists(Path.of(FILE_NAME))) return transactions;

        for (String line : Files.readAllLines(Path.of(FILE_NAME))) {
            String[] parts = line.split("\\|");
            transactions.add(new Transaction(LocalDate.parse(parts[0]), LocalTime.parse(parts[1]), parts[2], parts[3], Double.parseDouble(parts[4])));
        }
        return transactions;
    }

    public static void saveTransaction(Transaction transaction) throws IOException {
        Files.writeString(Path.of(FILE_NAME),
                String.format("%s|%s|%s|%s|%.2f\n",
                        transaction.getDate(), transaction.getTime().withNano(0),
                        transaction.getDescription(), transaction.getVendor(), transaction.getAmount()
                ),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND
    }
}
