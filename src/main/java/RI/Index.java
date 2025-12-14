package RI;
//تحويل JSON من ملف أو String إلى كائن Java (fromJson)
import com.google.gson.Gson;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

// ----------------------------
// 1) Class Place
// ----------------------------
class Place {
    public int id;
    public String name;
    public String location;
    public String description;
    public List<String> keywords;
}

// ----------------------------
// 2) Wrapper class للـ JSON
// ----------------------------
class TripsWrapper {
    public List<Place> trips;
}

// ----------------------------
// 3) Class Index
// ----------------------------
public class Index {

    public void createIndex() {
        try {
            // -------------------- Load JSON from resources --------------------
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("places.json");
            if (inputStream == null) {
                throw new RuntimeException("File corps.json not found in resources!");
            }
            InputStreamReader reader = new InputStreamReader(inputStream);

            // Parse JSON using Wrapper
            TripsWrapper wrapper = new Gson().fromJson(reader, TripsWrapper.class);
            List<Place> places = wrapper.trips;

            // -------------------- Lucene Index setup --------------------
            FSDirectory dir = FSDirectory.open(Paths.get("index"));
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(dir, config);

            // -------------------- Add documents to index --------------------
            for (Place p : places) {
                Document doc = new Document();

                doc.add(new StringField("id", String.valueOf(p.id), Field.Store.YES));
                doc.add(new TextField("name", p.name, Field.Store.YES));
                doc.add(new TextField("location", p.location, Field.Store.YES));
                doc.add(new TextField("description", p.description, Field.Store.YES));

                if (p.keywords != null && !p.keywords.isEmpty()) {
                    String keywordText = String.join(" ", p.keywords);
                    doc.add(new TextField("keywords", keywordText, Field.Store.YES));
                }

                writer.addDocument(doc);
            }

            writer.close();
            System.out.println("✔ Index Created Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
