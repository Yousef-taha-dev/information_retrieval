package RI;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. إنشاء الفهرس من ملف JSON
        new Index().createIndex();

        // 2. تجهيز محرك البحث (Lucene)
        Searcher searcher = new Searcher("index");

        // 3. تشغيل الواجهة المستوحاة من الصورة
        OneFileWebUI.start(searcher);
    }
}
