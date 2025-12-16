package RI;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.ClassicSimilarity;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Searcher {

    private final Analyzer analyzer;
    private final String indexPath;

    public Searcher(String indexPath) {
        this.indexPath = indexPath;
        this.analyzer = new StandardAnalyzer();
    }

    public List<SearchResult> search(String keyword, int maxResults) throws Exception {
        List<SearchResult> results = new ArrayList<>();

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher searcher = new IndexSearcher(reader);

        // 🔹 استخدام Cosine Similarity (TF-IDF)
        searcher.setSimilarity(new ClassicSimilarity());

        // 🔹 البحث في description
        QueryParser parserDesc = new QueryParser("description", analyzer);
        Query queryDesc = parserDesc.parse(keyword);

        // 🔹 البحث في keywords
        QueryParser parserKeywords = new QueryParser("keywords", analyzer);
        Query queryKeywords = parserKeywords.parse(keyword);

        // 🔹 دمج الاستعلامين
        BooleanQuery query = new BooleanQuery.Builder()
                .add(queryDesc, BooleanClause.Occur.SHOULD)
                .add(queryKeywords, BooleanClause.Occur.SHOULD)
                .build();

        TopDocs topDocs = searcher.search(query, maxResults);

        for (ScoreDoc sd : topDocs.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            results.add(new SearchResult(
                    doc.get("name"),
                    doc.get("location"),
                    sd.score
            ));
        }

        reader.close();
        return results;
    }
}
