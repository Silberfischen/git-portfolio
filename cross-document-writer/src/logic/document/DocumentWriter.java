package logic.document;

import logic.document.element.Element;
import logic.document.element.text.Phrase;
import org.primefaces.model.StreamedContent;

/**
 * This class represents an abstracted document writer whose subclasses will be
 * able to generate different formats like Word, Excel and PDF.
 *
 * Its methods are as abstract as possible to ensure the easy adapting of a new
 * format.
 *
 * @author Thorsten Jojart
 */
public interface DocumentWriter {

    /**
     * Document Format UPRIGHT (Hochformat)
     */
    public final static int FORMAT_UPRIGHT = 0;
    
    /**
     * Document Format Landscape (Querformat)
     */
    public final static int FORMAT_LANDSCAPE = 1;

    /**
     * This method will add the specific element to the document
     *
     * @param e the element which will be added to the document (with all
     * sub-elements)
     * @throws DocumentWriterException
     */
    public void addElement(Element e) throws DocumentWriterException;

    /**
     * Method to add a specific footer to the document. Some formats like excel
     * and word won't support this feature
     *
     * @param p the phrase which will be displayed in the footer
     * @throws DocumentWriterException
     */
    public void addFooter(Phrase p) throws DocumentWriterException;

    /**
     * Method to add a specific header to the document. Some formats like excel
     * and word won't support this feature
     *
     * @param p the phrase which will be displayed in the header
     * @throws DocumentWriterException
     */
    public void addHeader(Phrase p) throws DocumentWriterException;

    /**
     * Method to add specific meta-information to the document like author
     *
     * @param meta the metainformation object which should not be null
     * @throws DocumentWriterException
     */
    public void provideMetaData(MetaInformation meta) throws DocumentWriterException;

    /**
     * The standard style which will be used when the style attribute of an
     * element is not set
     *
     * @param s the style object which will be used as a standard style
     * @throws DocumentWriterException
     */
    public void setStandardStyle(Style s) throws DocumentWriterException;

    /**
     * Maps the file as a StreamedContent object which will be used for download
     * purpose in the primefaces library.<br /> To ensure that it returns the
     * updated content this method will call
     * <code> this.writeToDisk() </code>
     *
     * @throws DocumentWriterException
     * @return a StreamedContent object which will point to the updated document
     */
    public StreamedContent streamDocument() throws DocumentWriterException;

    /**
     * This method writes the document into a file specified in the documents
     * file attribute
     *
     * @throws DocumentWriterException
     */
    public void writeToDisk() throws DocumentWriterException;
    
    /**
     * Sets the orientation format of the document either to upright
     * (Hochformat) or landscape (Querformat).
     *
     * @param orientation
     */
    public void setOrientation(int orientation);
    
    /**
     * This method analyzes the actual elements contained by this class and
     * builds the entire document file from the scratch
     * 
     * @throws DocumentWriterException
     */
    public void buildDocument() throws DocumentWriterException;
    
    /**
     * This method resets the entire document to default values
     * IMPORTANT - FILE PROPERTY (Path of the file) WON'T BE TOUCHED
     */
    public void resetDocument();
}