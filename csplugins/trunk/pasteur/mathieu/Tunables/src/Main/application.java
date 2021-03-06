package Main;

import GuiInterception.*;
import Props.LoadPropsInterceptor;
import Props.StorePropsInterceptor;
import Command.*;

import java.awt.event.*;
import java.util.Properties;
import HandlerFactory.*;
import javax.swing.*;




public class application <T extends Handler>{

	static Properties InitProps = new Properties();
	static Properties store = new Properties();

    static int action;
	
    static TunableInterceptor ti;
    static TunableInterceptor lpi = null;
	static TunableInterceptor spi = null;
	
	public static void main(String[] args) {
                createAndShowGUI();
        }; 

    private static void createAndShowGUI() {
		JFrame frame = new JFrame("Tunable Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		ti = new GuiTunableInterceptor(frame);
		

		JPanel p = new JPanel();
		p.add( new JButton(new MyAction("Print Something", new PrintSomething(), ti, lpi,spi)));
		p.add( new JButton(new MyAction("Abstract Active", new AbstractActive(), ti, lpi,spi)));
		p.add( new JButton(new MyAction("Input Test", new input(), ti,lpi,spi)));
		p.add( new JButton(new MyAction("Tunable Sampler", new TunableSampler(), ti,lpi,spi)));
		p.add( new JButton(new MyAction("File Choose", new fileChoose(), ti,lpi,spi)));
		p.add( new JButton(new MyAction("URL Choose", new URLChoose(), ti,lpi,spi)));
        frame.setContentPane(p);
        frame.pack();
        frame.setVisible(true);
    }

	@SuppressWarnings("serial")
	private static class MyAction extends AbstractAction {
		command com;
		TunableInterceptor ti;
		TunableInterceptor lpi;
		TunableInterceptor spi;
		
		MyAction(String title, command com, TunableInterceptor ti, TunableInterceptor lpi,TunableInterceptor spi) {
			super(title);
			this.com = com;
			this.ti = ti;
			this.lpi = lpi;
			this.spi = spi;
		}
		public void actionPerformed(ActionEvent a) {

			// set the initial properties
			InitProps = new Properties();
			store = new Properties();
			lpi = new LoadPropsInterceptor(InitProps);
			spi = new StorePropsInterceptor(store);
			
			
			lpi.loadTunables(com);
			lpi.createProperties(com);
			System.out.println("InputProperties of "+com.getClass().getSimpleName()+ " = "+ InitProps);
			
			// intercept the command ,modify any tunable fields, and return the button clicked
			ti.loadTunables(com);
			if ( com instanceof HandlerController )
				((HandlerController)com).controlHandlers(ti.getHandlers(com));
		

			// create the UI based on the object
			action = ti.createUI(com);
			

			switch(action){
				case 0: ti.interceptandDisplayResults(com);spi.loadTunables(com);spi.createProperties(com);break;	//To reset the inputdefault Parameters	lpi.processProperties(com);ti.interceptAndReinitializeObjects(com);break;		
				case 1: spi.loadTunables(com);spi.createProperties(com);lpi.processProperties(com);spi.processProperties(com);ti.loadTunables(com);break;//ti.interceptAndReinitializeObjects(com);break;
			}
			System.out.println("OutputProperties of "+com.getClass().getSimpleName()+ " = "+ store);

			
			// execute the command
			com.execute();
		}
	}
}
