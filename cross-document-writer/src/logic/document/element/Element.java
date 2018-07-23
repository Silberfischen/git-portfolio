package logic.document.element;

import java.util.List;
import logic.document.Style;

/**
 * The most abstract definition of an element which can be added to a document
 *
 * @author Thorsten Jojart
 */
public interface Element {

    /**
     * Adds an element as a sub-element to this element
     * @param e the element which will be added as a sub-element
     * @return the new element so you can do method chaining
     */
    public Element addElement(Element e);

    /**
     * Returns the actual style of the element
     *
     * @return the style object
     */
    public Style getStyle();

    /**
     * Returns all sub-elements of this element
     *
     * @return a generic list of elements
     */
    public List<? extends Element> getContainingElements();
}