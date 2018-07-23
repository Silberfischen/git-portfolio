package logic.document.element.misc;

import logic.document.element.text.TextElement;

/**
 * When this element is added to the document writer a new page will be created
 *
 * @author Thorsten Jojart
 */
public class NewPage extends TextElement {

    /**
     *
     * @param sheetName this param is currently only needed for excel sheets
     */
    public NewPage(String sheetName) {
        this.text = sheetName;
    }

    public NewPage() {
    }
}
