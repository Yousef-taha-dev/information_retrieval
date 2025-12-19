package RI;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class OneFileWebUI {

    public static void start(Searcher searcher) throws IOException {
        // إنشاء سيرفر محلي على بورت 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String keyword = "";
            String resultsHtml = "";

            // معالجة البحث عند إرسال الكلمة المفتاحية
            if (query != null && query.contains("q=")) {
                keyword = java.net.URLDecoder.decode(query.split("=")[1], StandardCharsets.UTF_8);
                try {
                    // استدعاء ميثود البحث من كلاس Searcher الخاص بك
                    List<SearchResult> results = searcher.search(keyword, 10);
                    resultsHtml = buildResultsTemplate(results);
                } catch (Exception e) {
                    resultsHtml = "<p style='color:white;'>Error during search process.</p>";
                }
            }

            // استدعاء بناء الصفحة مع تمرير المتغيرات المطلوبة
            String fullPage = buildFullPage(keyword, resultsHtml);
            sendResponse(exchange, fullPage);
        });

        server.start();
        System.out.println("✅ UI is running! Open: http://localhost:8080");
    }

    private static String buildFullPage(String keyword, String resultsHtml) {
        // رابط الصورة المباشر من Pinterest التي اخترتها
        String pinterestImage = "https://i.pinimg.com/originals/4a/7d/93/4a7d932df3d461331fc6615e87a1fe73.jpg";

        return "<html><head><meta charset='UTF-8'><title>Jordan Trip Finder</title><style>" +
                "body { margin:0; font-family: 'Arial', sans-serif; " +
                "background: url('" + pinterestImage + "') no-repeat center center fixed; " +
                "background-size: cover; color: white; direction: ltr; }" +
                ".overlay { background: rgba(0,0,0,0.5); min-height: 100vh; display: flex; flex-direction: column; align-items: center; padding-top: 100px; }" +
                "h1 { font-size: 50px; margin: 0; text-shadow: 2px 2px 10px rgba(0,0,0,0.8); }" +
                ".subtitle { font-size: 22px; margin-bottom: 40px; opacity: 0.9; text-shadow: 1px 1px 5px rgba(0,0,0,0.5); }" +
                ".search-bar { background: white; padding: 5px; display: flex; width: 700px; box-shadow: 0 10px 30px rgba(0,0,0,0.5); border-radius: 5px; }" +
                "input { border: none; padding: 18px; flex: 1; font-size: 18px; outline: none; border-radius: 5px 0 0 5px; color: #333; }" +
                "button { background: #2ecc71; border: none; color: white; padding: 0 45px; cursor: pointer; font-weight: bold; font-size: 18px; text-transform: uppercase; border-radius: 0 5px 5px 0; transition: 0.3s; }" +
                "button:hover { background: #27ae60; }" +
                ".results { width: 700px; margin-top: 50px; padding-bottom: 100px; }" +
                ".card { background: rgba(255,255,255,0.95); color: #333; padding: 25px; margin-bottom: 20px; position: relative; text-align: left; border-radius: 8px; box-shadow: 0 5px 15px rgba(0,0,0,0.3); }" +
                ".card h3 { margin: 0 0 10px 0; color: #27ae60; font-size: 24px; }" +
                ".score { position: absolute; top: 20px; right: 20px; background: #2c3e50; color: white; padding: 5px 12px; font-size: 12px; font-weight: bold; border-radius: 4px; }" +
                ".loc { color: #7f8c8d; font-size: 14px; display: block; margin-bottom: 10px; font-weight: bold; }" +
                ".desc { line-height: 1.6; font-size: 16px; color: #444; }" +
                "</style></head><body><div class='overlay'>" +
                "<h1>Explore for the best holiday</h1>" +
                "<p class='subtitle'>Find trips and activities around Jordan</p>" +
                "<form class='search-bar' action='/' method='get'>" +
                "<input type='text' name='q' placeholder='Search for a destination...' value='" + keyword + "'>" +
                "<button type='submit'>SEARCH</button></form>" +
                "<div class='results'>" + resultsHtml + "</div>" +
                "</div></body></html>";
    }

    private static String buildResultsTemplate(List<SearchResult> results) {
        if (results.isEmpty()) return "<div class='card' style='text-align:center;'>No results found for your search.</div>";
        StringBuilder sb = new StringBuilder();
        for (SearchResult res : results) {
            sb.append("<div class='card'>")
                    // عرض السكور من كلاس SearchResult الخاص بك
                    .append("<span class='score'>Score: ").append(String.format("%.2f", res.getScore())).append("</span>")
                    .append("<h3>").append(res.getName()).append("</h3>")
                    .append("<span class='loc'>📍 ").append(res.getLocation()).append("</span>")
                    .append("<p class='desc'>").append(res.getDescription()).append("</p>")
                    .append("</div>");
        }
        return sb.toString();
    }

    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
