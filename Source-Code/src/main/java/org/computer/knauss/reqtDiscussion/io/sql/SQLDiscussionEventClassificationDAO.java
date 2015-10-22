package org.computer.knauss.reqtDiscussion.io.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.computer.knauss.reqtDiscussion.io.DAOException;
import org.computer.knauss.reqtDiscussion.io.IDiscussionEventClassificationDAO;
import org.computer.knauss.reqtDiscussion.model.DiscussionEvent;
import org.computer.knauss.reqtDiscussion.model.DiscussionEventClassification;

/**
 * Assume the following schema: discussionEventClassification(discussionEventId,
 * classifiedBy, classification, confidence, comment)
 * 
 * @author eknauss
 * 
 */
public class SQLDiscussionEventClassificationDAO extends AbstractSQLDAO
		implements IDiscussionEventClassificationDAO {

	public static final String DISCUSSION_EVENT_CLASSIFICATION_TABLE_NAME = "DISCUSSION_EVENT_CLASSIFICATION_TABLE_NAME";
	public static final String SELECT_DISCUSSION_EVENT_CLASSIFICATION_BY_DISCUSSION_EVENT_ID = "SELECT_DISCUSSION_EVENT_CLASSIFICATION_BY_DISCUSSION_EVENT_ID";
	public static final String INSERT_DISCUSSION_EVENT_CLASSIFICATION = "INSERT_DISCUSSION_EVENT_CLASSIFICATION";
	public static final String UPDATE_DISCUSSION_EVENT_CLASSIFICATION = "UPDATE_DISCUSSION_EVENT_CLASSIFICATION";
	public static final String EXISTS_DISCUSSION_EVENT_CLASSIFICATION = "EXISTS_DISCUSSION_EVENT_CLASSIFICATION";
	public static final String CREATE_DISCUSSION_EVENT_CLASSIFICATION_TABLE = "CREATE_DISCUSSION_EVENT_CLASSIFICATION_TABLE";
	public static final String DROP_DISCUSSION_EVENT_CLASSIFICATION_TABLE = "DROP_DISCUSSION_EVENT_CLASSIFICATION_TABLE";

	@Override
	public DiscussionEventClassification[] getClassificationsForDiscussionEvent(
			DiscussionEvent de) throws DAOException {
		try {
			if (!existsTable(getConfiguration().getProperty(
					DISCUSSION_EVENT_CLASSIFICATION_TABLE_NAME)))
				return new DiscussionEventClassification[0];
			PreparedStatement stat = getPreparedStatement(getConfiguration()
					.getProperty(
							SELECT_DISCUSSION_EVENT_CLASSIFICATION_BY_DISCUSSION_EVENT_ID));
			List<DiscussionEventClassification> res = new LinkedList<DiscussionEventClassification>();
			if (stat.getParameterMetaData().getParameterCount() == 1) {
				stat.setInt(1, de.getID());
			} else if (stat.getParameterMetaData().getParameterCount() == 2) {
				stat.setInt(1, de.getDiscussionID());
				stat.setInt(2, de.getID());
			} else {
				throw new DAOException("Statement '" + stat.toString()
						+ "' has wrong number of parameters.");
			}
			ResultSet rs = stat.executeQuery();
			while (rs.next()) {
				DiscussionEventClassification dec = new DiscussionEventClassification();
				dec.setDiscussionID(de.getDiscussionID());
				dec.setDiscussionEventID(rs.getInt("discussionEventId"));
				dec.setClassifiedby(rs.getString("classifiedBy"));
				dec.setClassification(rs.getString("classification"));
				dec.setConfidence(rs.getDouble("confidence"));
				dec.setComment(rs.getString("comment"));
				res.add(dec);
			}
			return res.toArray(new DiscussionEventClassification[0]);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}
	}

	@Override
	public void storeDiscussionEventClassification(
			DiscussionEventClassification classification) throws DAOException {
		try {
			if (!existsTable(getConfiguration().getProperty(
					DISCUSSION_EVENT_CLASSIFICATION_TABLE_NAME)))
				createSchema();

			PreparedStatement stat = getPreparedStatement(getConfiguration()
					.getProperty(EXISTS_DISCUSSION_EVENT_CLASSIFICATION));
			if (stat.getParameterMetaData().getParameterCount() == 2) {
				stat.setInt(1, classification.getDiscussionEventID());
				stat.setString(2, classification.getClassifiedby());
			} else if (stat.getParameterMetaData().getParameterCount() == 3) {
				stat.setInt(1, classification.getDiscussionID());
				stat.setInt(2, classification.getDiscussionEventID());
				stat.setString(3, classification.getClassifiedby());
			} else {
				throw new DAOException("Statement '" + stat.toString()
						+ "' has wrong number of parameters.");
			}
			ResultSet rs = stat.executeQuery();
			int rows = 0;
			if (rs.next()) {
				rows = rs.getInt(1);
			}

			if (rows > 0) {
				System.out.println("UPDATE");
				stat = getPreparedStatement(getConfiguration().getProperty(
						UPDATE_DISCUSSION_EVENT_CLASSIFICATION));
				if (stat.getParameterMetaData().getParameterCount() == 5) {
					stat.setString(1, classification.getClassification());
					stat.setDouble(2, classification.getConfidence());
					stat.setString(3, classification.getComment());
					stat.setInt(4, classification.getDiscussionEventID());
					stat.setString(5, classification.getClassifiedby());
				} else if (stat.getParameterMetaData().getParameterCount() == 6) {
					stat.setString(1, classification.getClassification());
					stat.setDouble(2, classification.getConfidence());
					stat.setString(3, classification.getComment());
					stat.setInt(4, classification.getDiscussionID());
					stat.setInt(5, classification.getDiscussionEventID());
					stat.setString(6, classification.getClassifiedby());
				} else {
					throw new DAOException("Statement '" + stat.toString()
							+ "' has wrong number of parameters.");
				}
			} else {
				System.out.println("INSERT");
				stat = getPreparedStatement(getConfiguration().getProperty(
						INSERT_DISCUSSION_EVENT_CLASSIFICATION));
				if (stat.getParameterMetaData().getParameterCount() == 5) {
					stat.setInt(1, classification.getDiscussionEventID());
					stat.setString(2, classification.getClassifiedby());
					stat.setString(3, classification.getClassification());
					stat.setDouble(4, classification.getConfidence());
					stat.setString(5, classification.getComment());
				} else if (stat.getParameterMetaData().getParameterCount() == 6) {
					stat.setInt(1, classification.getDiscussionID());
					stat.setInt(2, classification.getDiscussionEventID());
					stat.setString(3, classification.getClassifiedby());
					stat.setString(4, classification.getClassification());
					stat.setDouble(5, classification.getConfidence());
					stat.setString(6, classification.getComment());

				} else {
					throw new DAOException("Statement '" + stat.toString()
							+ "' has wrong number of parameters.");
				}
			}
			int executeUpdate = stat.executeUpdate();
			if (1 != executeUpdate) {
				// stat.close();
				// ConnectionManager.getInstance().closeConnection();
				throw new DAOException(
						"INSERT/UPDATE should only affect one row but did affect "
								+ executeUpdate + " rows.");
			}
			// stat.close();
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			// ConnectionManager.getInstance().closeConnection();
		}
	}

	public void createSchema() throws SQLException, DAOException {
		getPreparedStatement(
				getConfiguration().getProperty(
						CREATE_DISCUSSION_EVENT_CLASSIFICATION_TABLE))
				.executeUpdate();
		System.out.println("Created Table "
				+ getConfiguration().getProperty(
						DISCUSSION_EVENT_CLASSIFICATION_TABLE_NAME) + ".");
	}

	public void dropSchema() throws SQLException, DAOException {
		getPreparedStatement(
				getConfiguration().getProperty(
						DROP_DISCUSSION_EVENT_CLASSIFICATION_TABLE)).execute();
		System.out.println("Dropped Table "
				+ getConfiguration().getProperty(
						DISCUSSION_EVENT_CLASSIFICATION_TABLE_NAME) + ".");
	}

	@Override
	protected Properties getDefaultProperties() {
		Properties p = new Properties();

		p.setProperty(DISCUSSION_EVENT_CLASSIFICATION_TABLE_NAME, "");
		p.setProperty(
				SELECT_DISCUSSION_EVENT_CLASSIFICATION_BY_DISCUSSION_EVENT_ID,
				"");
		p.setProperty(INSERT_DISCUSSION_EVENT_CLASSIFICATION, "");
		p.setProperty(UPDATE_DISCUSSION_EVENT_CLASSIFICATION, "");
		p.setProperty(EXISTS_DISCUSSION_EVENT_CLASSIFICATION, "");
		p.setProperty(CREATE_DISCUSSION_EVENT_CLASSIFICATION_TABLE, "");
		p.setProperty(DROP_DISCUSSION_EVENT_CLASSIFICATION_TABLE, "");

		return p;
	}

	@Override
	protected Map<String, String> getMandatoryPropertiesAndHints() {
		Map<String, String> ret = new HashMap<String, String>();

		ret.put(DISCUSSION_EVENT_CLASSIFICATION_TABLE_NAME,
				"Name of the table that holds the discussion event classfications");
		ret.put(SELECT_DISCUSSION_EVENT_CLASSIFICATION_BY_DISCUSSION_EVENT_ID,
				"SQL statement that selects a classification by event id");
		ret.put(INSERT_DISCUSSION_EVENT_CLASSIFICATION,
				"SQL statement that inserts a classification");
		ret.put(UPDATE_DISCUSSION_EVENT_CLASSIFICATION,
				"SQL statement that updates a classification");

		return ret;
	}
}
