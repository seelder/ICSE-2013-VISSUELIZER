package org.computer.knauss.reqtDiscussion.io.jazz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.computer.knauss.reqtDiscussion.io.DAOException;
import org.computer.knauss.reqtDiscussion.io.IDAOManager;
import org.computer.knauss.reqtDiscussion.io.IDiscussionDAO;
import org.computer.knauss.reqtDiscussion.io.IDiscussionEventClassificationDAO;
import org.computer.knauss.reqtDiscussion.io.IDiscussionEventDAO;
import org.computer.knauss.reqtDiscussion.io.IIncidentDAO;
import org.computer.knauss.reqtDiscussion.io.jazz.rest.JazzJDOMDAO;
import org.computer.knauss.reqtDiscussion.io.jazz.util.ui.DialogBasedJazzAccessConfiguration;

public class JazzDAOManager implements IDAOManager {

	private JazzJDOMDAO dao;

	@Override
	public IDiscussionDAO getDiscussionDAO() throws DAOException {
		return getDAO();
	}

	@Override
	public IDiscussionEventDAO getDiscussionEventDAO() throws DAOException {
		return getDAO();
	}

	private JazzJDOMDAO getDAO() throws DAOException {
		if (this.dao == null) {
			Properties p = new Properties();

			try {
				URL propertyResource = getClass().getResource(
						"jazz-properties.txt");
				if (propertyResource == null)
					System.err.println("Could not locate property file!");
				else
					p.load(new FileInputStream(propertyResource.getFile()));
			} catch (FileNotFoundException e) {
				System.err.println("Could not find property file: "
						+ e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Could not read property file: "
						+ e.getMessage());
			}

			DialogBasedJazzAccessConfiguration config = new DialogBasedJazzAccessConfiguration();
			config.configure(p);
			this.dao = new JazzJDOMDAO(config);
			this.dao.setProjectArea("Rational Team Concert");
		}
		return this.dao;
	}

	@Override
	public IDiscussionEventClassificationDAO getDiscussionEventClassificationDAO()
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeAllConnections() {
		// TODO Auto-generated method stub

	}

	@Override
	public IIncidentDAO getIncidentDAO() throws DAOException {
		return getDAO();
	}

}
