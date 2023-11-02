package eu.zderadicka.mbs3.analysis;

import org.apache.lucene.analysis.Analyzer;

public interface AnalyzerFactory {
    Analyzer create();
}
