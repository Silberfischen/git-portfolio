package logic.document.element.text;

import logic.document.Style;

/**
 * This class represents a simple paragraph which will be interpreted as a
 * phrase with a linefeed afterwards
 *
 * @author Thorsten Jojart
 */
public class Paragraph extends TextElement {

    public Paragraph() {
        super();
    }

    public Paragraph(String text) {
        super(text);
    }

    public Paragraph(String text, Style s) {
        super(text, s);
    }
}
