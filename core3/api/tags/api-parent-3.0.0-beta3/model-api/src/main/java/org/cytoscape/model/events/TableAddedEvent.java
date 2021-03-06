package org.cytoscape.model.events;

import org.cytoscape.event.AbstractCyEvent;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableManager;

/**
 * This event will be fired when new table is added to {@link CyTableManager}.
 * @CyAPI.Final.Class
 */
public final class TableAddedEvent extends AbstractCyEvent<CyTableManager> {
	
	private final CyTable table;
	
	/**
	 * Constructs the table added event.
	 * @param source  the table manager
	 * @param table   the table added to the table manager
	 */
	public TableAddedEvent(final CyTableManager source, final CyTable table) {
		super(source, TableAddedListener.class);
		this.table = table;
	}


	/**
	 * Returns the table added to the table manager.
	 * @return the table added to the table manager.
	 */
	public final CyTable getTable() {
		return table;
	}
}
