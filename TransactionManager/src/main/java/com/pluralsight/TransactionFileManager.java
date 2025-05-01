package com.pluralsight;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileManager {
    private static final String FILE_NAME = "transactions.csv";

    public static List<Transaction> load() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(Transaction.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        return transactions;
    }

    public static void save(Transaction t) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(t.toCSV());
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }
}
