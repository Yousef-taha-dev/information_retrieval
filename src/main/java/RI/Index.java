package RI;

import com.google.gson.Gson;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
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
            // 🔹 تحميل ملف JSON
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("places.json");
            if (inputStream == null) {
                throw new RuntimeException("places.json not found in resources!");
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            TripsWrapper wrapper = new Gson().fromJson(reader, TripsWrapper.class);
            List<Place> places = wrapper.trips;

            // 🔹 إعداد Lucene
            FSDirectory dir = FSDirectory.open(Paths.get("index"));
            Analyzer analyzer = new EnglishAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // 🔥 الحل

            IndexWriter writer = new IndexWriter(dir, config);

            // 🔹 إضافة المستندات
            for (Place p : places) {
                Document doc = new Document();

                doc.add(new StringField("id", String.valueOf(p.id), Field.Store.YES));
                doc.add(new TextField("name", p.name, Field.Store.YES));
                doc.add(new TextField("location", p.location, Field.Store.YES));
                doc.add(new TextField("description", p.description, Field.Store.YES));

                if (p.keywords != null && !p.keywords.isEmpty()) {
                    doc.add(new TextField(
                            "keywords",
                            String.join(" ", p.keywords),
                            Field.Store.YES
                    ));
                }

                writer.addDocument(doc);
            }

            writer.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
