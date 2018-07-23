package logic.document.element.text;

import java.util.ArrayList;
import java.util.List;
import logic.document.Style;
import logic.document.element.Element;

/**
 * An abstract class that contains all then necessary methods to represent an
 * element which displays text
 *
 * @author Thorsten Jojart
 */
public abstract class TextElement implements Element {

    /**
     * List of all Elements, that get added to this TextElement
     */
    protected List<Element> subElements = new ArrayList<>();
    
    /**
     * Text of the TextElement
     */
    protected String text;
    
    /**
     * Style of the TextElement
     */
    protected Style style;

    protected TextElement() {
    }

    protected TextElement(String text) {
        this.text = text;
    }

    public TextElement(String text, Style style) {
        this.text = text;
        this.style = style;
    }

    public void setText(String text) {
        if (text != null) {
            this.text = text;
        }
    }

    @Override
    public Element addElement(Element e) {
        if (e != null) {
            this.subElements.add(e);
            return this;
        } else {
            throw new IllegalArgumentException("Element mustn't be null!");
        }
    }

    @Override
    public List<Element> getContainingElements() {
        return this.subElements;
    }

    @Override
    public Style getStyle() {
        return this.style;
    }

    public String getContent() {
        return this.text;
    }
}