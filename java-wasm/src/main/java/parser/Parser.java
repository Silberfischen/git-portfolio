package parser;

import ast.Module;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public interface Parser {

    /**
     * Parses the given InputStream until EOF or an error occured
     */
    public Module parse(InputStream is) throws ParserException, IOException;
}