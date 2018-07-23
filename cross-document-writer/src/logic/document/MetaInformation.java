package logic.document;

/**
 * This class represents the meta information of a standard document format like
 * work, excel or pdf.
 *
 * It contains the most common infos.
 *
 * @author Thorsten Jojart
 */
public class MetaInformation {

    /**
     * Represents the author of the document
     */
    private String author;
    /**
     * Represents different keywords which will be different programms to
     * classify the document
     */
    private String keywords;
    /**
     * The subject of the document
     */
    private String subject;
    /**
     * The title of the document 
     */
    private String title;
    /**
     * Comments for additional info which is not specific
     */
    private String comments;

    public MetaInformation(String author, String keywords, String subject, String title, String comments) {
        this.author = author;
        this.keywords = keywords;
        this.subject = subject;
        this.title = title;
        this.comments = comments;
    }

    public MetaInformation() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
