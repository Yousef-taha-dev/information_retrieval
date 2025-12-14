package RI;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

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

        // ---------------- TF-IDF Manual ----------------
        int totalDocs = reader.numDocs();
        int docFreq = reader.docFreq(new Term("description", keyword.toLowerCase()));
        double idf = Math.log((double)(totalDocs + 1) / (docFreq + 1)) + 1.0;

        // ---------------- Lucene Query ----------------
        QueryParser parser = new QueryParser("description", analyzer);
        Query query = parser.parse(keyword);

        TopDocs topDocs = searcher.search(query, maxResults);

        for (ScoreDoc sd : topDocs.scoreDocs) {
            Document doc = searcher.doc(sd.doc);

            // حساب TF داخل المستند
            String desc = doc.get("description").toLowerCase();
            int tf = 0;
            for (String word : desc.split("\\W+")) {
                if (word.equals(keyword.toLowerCase())) tf++;
            }

            double tfidfScore = tf * idf;
            results.add(new SearchResult(doc.get("name"), doc.get("location"), tfidfScore));
        }

        reader.close();
        return results;
    }
}
