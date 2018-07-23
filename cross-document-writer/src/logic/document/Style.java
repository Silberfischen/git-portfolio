package logic.document;

import java.awt.Color;

/**
 * This class represents a standard style which contains the most used
 * properties to style an element
 *
 * @author Thorsten Jojart
 */
public class Style {

    //FONT-FAMILIES BEGIN
    /**
     * font Times New Roman
     */
    public final static int TIMES_NEW_ROMAN = 0;
    /**
     * font Helvetica
     */
    public final static int HELVETICA = 1;
    /**
     * font Courier New
     */
    public final static int COURIER_NEW = 2;
    //FONT-FAMILIES END
    
    //ALIGNMENTS BEGIN
    /**
     * text alignment CENTER
     */
    public final static int ALIGNMENT_CENTER = 0;
    /**
     * text alignment LEFT
     */
    public final static int ALIGNMENT_LEFT = 1;
    /**
     * text alignment RIGHT
     */
    public final static int ALIGNMENT_RIGHT = 2;
    //ALIGNMENTS END
    
    /**
     * The actual font family
     */
    private int fontFamily = HELVETICA;
    /**
     * The actual font size
     */
    private double fontSize = 12.0;
    /**
     * Is the element bold?
     */
    private boolean bold = false;
    /**
     * Is the element italic?
     */
    private boolean italic = false;
    /**
     * The actual font-color
     */
    private Color foregroundColor = Color.BLACK;
    /**
     * The actual bacground color
     */
    private Color backgroundColor = Color.WHITE;
    /**
     * The actual text alignment
     */
    private int alignment = 1;

    /**
     * The constructor which contains all the information for creating a
     * complete style element
     *
     * @param fontFamily
     * @param fontSize
     * @param bold
     * @param italic
     * @param foregroundColor
     * @param backgroundColor
     * @param alignment
     */
    public Style(int fontFamily, double fontSize, boolean bold, boolean italic, Color foregroundColor, Color backgroundColor, int alignment) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.bold = bold;
        this.italic = italic;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.alignment = alignment;
    }

    public Style() {
    }

    public int getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(int fontFamily) {
        this.fontFamily = fontFamily;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }
}
