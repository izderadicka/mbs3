package eu.zderadicka.mbs3.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomAnalyzerFactory implements AnalyzerFactory {

    @Override
    public Analyzer create() {
        return new CustomAnalyzer();
    }
    
}
