package java.org.datavyu.models.datastore;

import java.org.datavyu.models.datastore.spreadsheet.cell.Cell;
import java.org.datavyu.models.datastore.spreadsheet.column.Column;
import java.org.datavyu.models.datastore.spreadsheet.Spreadsheet;
import java.util.List;

/**
 * An interface which abstracts away from the specific underlying database,
 * with methods to access columns and cells in an intuitive manner.
 */
public interface DataStore {

    /**
     * Create a Default Data Store
     *
     * @return an empty Data Store
     */
    DataStore createDefaultDataStore();

    /**
     * Create a Spreadsheet in the Data store
     *
     * @param title - name of the Spreadsheet
     * @return
     */
    DataStore createSpreadsheet(final String title);

    /**
     * The Current Data Store
     *
     * @return the Current Data Store
     */
    DataStore getDataStore();

    /**
     * Add a Spreadsheet to the Data Store
     *
     * @param spreadsheet
     * @return the Current Data Store
     */
    DataStore addSpreadsheet(final Spreadsheet spreadsheet);

    /**
     * Remove a Spreadsheet from the Data Store
     * @param spreadsheet
     * @return the current Data Store
     */
    DataStore removeSpreadsheet(final Spreadsheet spreadsheet);

    /**
     *
     * @return a list a spreadsheet
     */
    List<Spreadsheet> getSpreadsheets();

    /**
     * Fetch the Spreadsheet that holds the column, will call contains method in each .
     *
     * @param column - The column that need to be found in the spreadsheet
     * @return a Spreadsheet
     */
    Spreadsheet getSpreadsheet(final Column column);

    /**
     * Fetch the Spreadsheet that holds the cell, this will
     * ask the contains methods of all the columns.
     *
     * @param cell
     * @return
     */
    Spreadsheet getSpreadsheet(final Cell cell);
}
