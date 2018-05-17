package java.org.datavyu.models.datastore.spreadsheet.cell;

import java.org.datavyu.models.datastore.Code;
import java.util.List;

/**
 * Represents a cell in the Datavyu spreadsheet.
 */
public interface Cell {

    /**
     * Create a Default Cell with a list of code got from {@Column}
     * @param codes
     * @return A new Cell
     */
    Cell createDefaultCell(final List<Code> codes);

    /**
     * @return The unique identifier of the cell
     */
    int getCellID();

    /**
     * @return The onset timestamp in milliseconds. Returns -1 if the offset cannot be resolved.
     */
    long getOnset();

    /**
     * Sets the onset for this cell.
     *
     * @param time The new onset timestamp in milliseconds to use for this
     *                 cell.
     * @return A new Cell
     */
    Cell setOnset(final long time);

    /**
     * @return The offset timestamp in milliseconds. Returns -1 if the offset cannot be resolved.
     */
    long getOffset();

    /**
     * Sets the offset for this cell.
     *
     * @param time The new offset timestamp in milliseconds to use for this cell.
     * @return A new Cell
     */
    Cell setOffset(final long time);

    /**
     * List of codes in a Cell
     *
     * @return
     */
    List<Code> getCodes();

    /**
     * This methods is used to validate a cell after adding or removing a cell
     *
     * @return the number of codes in the cell
     */
    int getCodesSize();

    /**
     * Adds a new code to the cell.
     *
     * @param index index - the index of the code in the cells list of codes
     * @param code - The value to set the code to
     * @return A new Cell
     */
    Cell addCode(final int index, final Code code);

    /**
     * Removes a code from the cell.
     *
     * @param code - the index of code to clear from the list
     * @return A new Cell
     */
    Cell removeCode(final Code code);

    /**
     * Change the position of a code within a cell
     * @param oldPos - Old Index
     * @param newPos - new Index
     * @param code - Code to be moved
     * @return A new Cell with the correct code order
     */
    Cell moveCode(final int oldPos, final int newPos, final Code code);
}
