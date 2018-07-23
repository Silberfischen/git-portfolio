package logic.document.element.text;

import logic.document.Style;

/**
 * This class represents a phrase which will be interpreted as a simple string
 * with a style
 * 
 * @author Thorsten Jojart
 */
public class Phrase extends TextElement {

    public Phrase() {
        super();
    }

    public Phrase(String text) {
        super(text);
    }    
    
    public Phrase(String text,Style s) {
        super(text,s);
    }
}