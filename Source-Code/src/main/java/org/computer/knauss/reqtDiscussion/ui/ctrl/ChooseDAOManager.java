package org.computer.knauss.reqtDiscussion.ui.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.computer.knauss.reqtDiscussion.io.DAORegistry;

public class ChooseDAOManager extends AbstractCommand {

	private static final long serialVersionUID = 1L;

	public ChooseDAOManager() {
		super("Select data source");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String[] sources = DAORegistry.getInstance().availableDAOManagers();
		String current = DAORegistry.getInstance().getNameForDAOManager(
				DAORegistry.getInstance().getSelectedDAOManager());
		String choice = (String) JOptionPane.showInputDialog(null,
				"Available datasources:", "Choose data source",
				JOptionPane.QUESTION_MESSAGE, null, sources, current);
		DAORegistry.getInstance().selectDAOManager(choice);
		// Probably a good idea to close everything and start with fresh connections:
		DAORegistry.getInstance().closeAllConnections();
	}

}
