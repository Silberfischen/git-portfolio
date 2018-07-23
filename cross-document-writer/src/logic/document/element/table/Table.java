package logic.document.element.table;

import java.util.ArrayList;
import java.util.List;
import logic.document.Style;
import logic.document.element.Element;

/**
 * This class represents a table which contains cells and has a fixed size of
 * columns.
 *
 * @author Thorsten Jojart
 */
public class Table implements Element {
    
    /**
     * List of all Cells inside this table object
     */
    private List<Cell> cellElements = new ArrayList<>();
    
    /**
     * Number of columns of the table
     */
    private int columns;

    /**
     * Creates a new table object with a specific column count
     *
     * @param columns the column count of the table which should be greater then
     * 0
     */
    public Table(int columns) {
        this.setColumnCount(columns);
    }

    @Override
    public List<? extends Element> getContainingElements() {
        return this.cellElements;
    }

    /**
     * Sets the column count of the table
     *
     * @param columns must be > 0
     */
    public void setColumnCount(int columns) {
        if (columns > 0) {
            this.columns = columns;
        } else {
            throw new IllegalArgumentException("Columns mustn't be lesser or equal zero");
        }
    }

    /**
     * Returns the column count of the table
     *
     * @return the column count as an int
     */
    public int getColumnCount() {
        return columns;
    }

    @Override
    public Style getStyle() {
        return null;
    }

    /**
     * Behaves a bit different like the normal addElement. It only accepts Cell
     * object, otherwise it will throw an
     * <code>IllegalArgumentException</code>.
     *
     *
     * @param e The (hopefully) Cell that will be added
     * @return The new element so it can be method-chained
     */
    @Override
    public Element addElement(Element e) {
        if (!(e instanceof Cell)) {
            throw new IllegalArgumentException("The element must be a cell");
        }
        //cast to cell
        Cell c = (Cell) e;

        //test whether the cell is null
        if (e != null) {
            this.cellElements.add(c);
            return this;
        } else {
            throw new IllegalArgumentException("Element mustn't be null");
        }
    }
}