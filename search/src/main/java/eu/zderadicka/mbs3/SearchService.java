package eu.zderadicka.mbs3;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import eu.zderadicka.mbs3.data.Ebook;
import eu.zderadicka.mbs3.data.SearchResults;
import io.quarkus.logging.Log;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SearchService {

    private Directory index;
    private Analyzer analyzer = new StandardAnalyzer();
    private IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
    private IndexWriter indexWriter;

    @Inject
    public SearchService(@ConfigProperty(name = "index.directory") String indexPath) {
        try {
            Log.info(String.format("Index path is %s", indexPath));
            var path = Path.of(indexPath);
            var indexLocation = FSDirectory.open(path);
            init(indexLocation);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public SearchService(Directory indexLocation) {
        init(indexLocation);
    }

    private void init(Directory indexLocation) {
        try {
            index = indexLocation;
            indexWriter = new IndexWriter(index, cfg);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    void destroy() {

        try {
            indexWriter.close();
            index.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addOrUpdateDocuments(List<Ebook> ebooks) {
        try {
            for (var ebook : ebooks) {
                var document = DocumentAdapter.fromEbook(ebook);
                indexWriter.updateDocuments(LongPoint.newExactQuery("id", ebook.id), Collections.singleton(document));
            }
            indexWriter.commit();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SearchResults search(String phrase) {
        return search(phrase, 10);
    }

    public SearchResults search(String phrase, Integer resultsSize) {

        try (var indexReader = DirectoryReader.open(indexWriter)) {

            var searcher = new IndexSearcher(indexReader);
            var query = new QueryParser("text", analyzer).parse(phrase);
            var result = searcher.search(query, resultsSize);

            SearchResults finalResults = new SearchResults();
            finalResults.approximateTotal = result.totalHits.value;
            var items = Arrays.stream(result.scoreDocs).map(item -> {
                SearchResults.OneResult res = new SearchResults.OneResult();
                try {
                    var doc = searcher.storedFields().document(item.doc);
                    res.ebook = DocumentAdapter.toEbook(doc);
                    res.score = item.score;

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                return res;
            }).collect(Collectors.toList());
            finalResults.results = items;
            return finalResults;

        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Search system error", e);
        }

    }

}
