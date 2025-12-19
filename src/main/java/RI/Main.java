package RI;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. بناء الفهرس
        Index index = new Index();
        index.createIndex();

        // 2. تجهيز محرك البحث
        Searcher searcher = new Searcher("index");

        // 3. استقبال كلمة البحث
        Scanner scanner = new Scanner(System.in);
        System.out.print("Search: ");
        String keyword = scanner.nextLine();

        List<SearchResult> results = searcher.search(keyword, 10);

        // 4. عرض النتائج مع الـ ID
        System.out.println("\n--- Search Results ---");
        for (SearchResult res : results) {
            System.out.println("ID: " + res.getId()); // 🔹 استدعاء الـ ID
            System.out.println("Name: " + res.getName());
            System.out.println("Location: " + res.getLocation());
            System.out.println("Score: " + res.getScore());
            System.out.println("----------------------");
        }

        scanner.close();
    }
}
