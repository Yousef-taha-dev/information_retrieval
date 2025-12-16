package RI;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        // 🔁 إعادة بناء الفهرس
        Index index = new Index();
        index.createIndex();

        // 🔍 البحث
        Searcher searcher = new Searcher("index");

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter search keyword: ");
        String keyword = sc.nextLine();

        List<SearchResult> results = searcher.search(keyword, 10);

        System.out.println("\n--- Search Results ---");
        for (SearchResult r : results) {
            System.out.println(r);
        }
    }
}
