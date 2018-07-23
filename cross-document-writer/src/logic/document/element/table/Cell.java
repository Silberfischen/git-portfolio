package logic.document.element.table;

import logic.document.Style;
import logic.document.element.text.TextElement;

/**
 * This class represents a cell of a table which can also contain other elements
 * like images
 *
 * @author Thorsten Jojart
 */
public class Cell extends TextElement {

    public Cell() {
        super();
    }

    public Cell(String text) {
        super(text);
    }

    public Cell(String text, Style s) {
        super(text, s);
    }
}