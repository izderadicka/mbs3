package eu.zderadicka.mbs3.analysis;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class CustomAnalyzer extends StopwordAnalyzerBase {

    static final int MAX_TOKEN_SIZE = 255;

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final StandardTokenizer src = new StandardTokenizer();
    src.setMaxTokenLength(MAX_TOKEN_SIZE);
    TokenStream tok = normalize(fieldName, src);
    tok = new StopFilter(tok, stopwords);
    return new TokenStreamComponents(
        src,
        tok);
    }

    public CustomAnalyzer() {
        super();
    }

    public CustomAnalyzer(CharArraySet stopWords) {
        super(stopWords);
    }



    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        var lowerCase = new LowerCaseFilter(in);
        var ascii = new ASCIIFoldingFilter(lowerCase);
        return ascii;
    }
    
}
