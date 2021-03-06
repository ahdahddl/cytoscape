package org.cytoscape.task.internal.loaddatatable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.cytoscape.event.CyEventHelper;
import org.cytoscape.io.read.CyTableReaderManager;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.task.internal.table.UpdateAddedNetworkAttributes;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.TunableSetter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoadAttributesURLTaskFactoryImplTest {
	
	@Mock
	private CyTableReaderManager rmgr;
	
	@Mock
	private TaskMonitor tm;
	
	@Mock
	TunableSetter ts;
	
	@Mock
	CyNetworkManager netMgr;
	
	@Mock
	CyTableManager tabMgr;
	
	@Mock 
	UpdateAddedNetworkAttributes updateAddedNetworkAttributes;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		
	}
	
	@Test(expected = NullPointerException.class)
	public void testLoadAttributesURLTaskFactory() throws Exception {

		final LoadAttributesURLTaskFactoryImpl factory = new LoadAttributesURLTaskFactoryImpl(rmgr, ts, netMgr, tabMgr, updateAddedNetworkAttributes);
		
		final TaskIterator ti = factory.createTaskIterator();
		assertNotNull(ti);
		
		assertTrue( ti.hasNext() );
		Task t = ti.next();
		assertNotNull( t );	
		
		((LoadAttributesURLTask)t).url = new URL("http://chianti.ucsd.edu/kono/data/galFiltered.sif");
		t.run(tm);
	}
}
