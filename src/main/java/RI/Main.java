package RI;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter keyword to search:");
            String keyword = scanner.nextLine().trim();

            // مسار الإندكس المخزن على القرص
            String indexPath = "index";

            Searcher searcher = new Searcher(indexPath);

            // البحث وأخذ أعلى 10 نتائج
            List<SearchResult> results = searcher.search(keyword, 10);

            if (results.isEmpty()) {
                System.out.println("No results found for: " + keyword);
            } else {
                System.out.println("Search results for '" + keyword + "':");
                for (SearchResult res : results) {
                    System.out.println(res);
                }
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

