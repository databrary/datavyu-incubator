package java.org.datavyu.models.datastore.spreadsheet.column;

import java.org.datavyu.models.datastore.spreadsheet.cell.Cell;
import java.org.datavyu.models.datastore.Code;
import java.util.List;

public interface Column {

    /**
     * Create default column, with a default code
     *
     * @param title - name of the column
     * @return a fresh column
     */
    Column createDefaultColumn(final String title);

    /**
     * Create a column with a list of codes
     *
     * @param title
     * @param codes
     * @return a column with codes
     */
    Column createColumn(final String title, final List<Code> codes);

    /**
     * Create a column with a list of codes and a list of cells
     *
     * @param title
     * @param codes
     * @param cells
     * @return a column with codes and cells
     */
    Column createColumn(final String title, final List<Code> codes, final List<Cell> cells);

    /**
     * A unique identifier
     *
     * @return
     */
    int getID();

    /**
     * Get the name of the column
     *
     * @return
     */
    String getTitle();

    /**
     * Change the name of the column
     *
     * @param title
     * @return
     */
    Column setTitle(String title);

    /**
     * add a code to the actual code definition of the column
     *
     * @param code
     * @return
     */
    Column addCode(final Code code);

    /**
     * Remove code from the column, this call will update all the cells in the column
     *
     * @param code
     * @return
     */
    Column removeCode(final Code code);

    /**
     * Move the code in all the child cells
     *
     * @param oldPos
     * @param newPos
     * @param code
     * @return a new updated column
     */
    Column moveCode(final int oldPos, final int newPos, final Code code);

    /**
     *
     *
     * @return
     */
    List<Code> getCodes();

    /**
     * add a default cell with the current code definition of the column
     *
     * @return
     */
    Column createDefaultCell();

    /**
     * Remove the cell from the column
     *
     * @param cell
     * @return
     */
    Column removeCell(final Cell cell);

    /**
     *
     * @param cells
     * @return
     */
    Column removeCells(final List<Cell> cells);

    /**
     *
     * @param cell
     * @return
     */
    Column addCell(final Cell cell);

    /**
     *
     * @param cells
     * @return
     */
    Column addCells(final List<Cell> cells);

    /**
     *
     * @return
     */
    List<Cell> getCells();

    /**
     * @param cell The cell to check if it exists in this variable.
     * @return True if this variable contains the supplied cell, false otherwise.
     */
    boolean contains(final Cell cell);
}
