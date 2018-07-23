package logic.document.element.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import logic.document.Style;
import logic.document.element.Element;

/**
 * This class represents an image which can be added to the document
 *
 * @author Thorsten Jojart
 */
public final class Image implements Element {

    /**
     * The file which is behind the image
     */
    private File imgFile;
    /**
     * For the case that someone wants to add the data directly
     */
    private byte[] data;
    /**
     * The type of the image which will be used
     */
    private String fileType;
    /**
     * The style of this element
     */
    private Style s;
    /**
     * The list of sub-elements which will be recursivly added to the document
     * in the writer classes
     */
    protected List<Element> subElements = new ArrayList<>();

    public Image(File imgFile, Style s) {
        if (!this.isFileValid(imgFile)) {
            throw new IllegalArgumentException("File is not valid!");
        } else {
            this.imgFile = imgFile;
            this.s = s;
            //Get the filetype
            String[] splitted = imgFile.getName().split(".");
            this.fileType = splitted[splitted.length];
        }
    }

    public Image(File imgFile) {
        this(imgFile, null);
    }

    public Image(byte[] data, String type) {
        if (this.data != null) {
            System.out.println(data.length);
            if (this.data.length < 1) {
                throw new IllegalArgumentException("No data was given!");
            } else {
                this.data = data;
                this.fileType = type;
            }
        }
    }

    /**
     * Method to test the correctness of the file
     *
     * @param imgFile the file which will be checked
     * @return true if the file is valid false otherwise
     */
    private boolean isFileValid(File imgFile) {
        return (imgFile.canRead() && imgFile.exists() && imgFile.isFile() && imgFile.length() > 0);
    }

    /**
     * Returns the image file which can be null if only a byte[] was specified
     *
     * @return the file which should be an image
     */
    public File getImageFile() {
        return this.imgFile;
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
        return this.s;
    }

    public String getFileType() {
        return this.fileType;
    }

    public byte[] getData() {
        return this.data;
    }
}