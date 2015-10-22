package org.computer.knauss.reqtDiscussion.model;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.computer.knauss.reqtDiscussion.Util;

public class ComplexDiscussion extends Discussion {

	private Discussion[] discussions;
	private DiscussionEvent[] events;
	private Incident[] incidentCache;

	public ComplexDiscussion(Discussion[] discussions) {
		this.discussions = discussions;
		Util.sortByID(this.discussions);
	}

	@Override
	public int getID() {
		return this.discussions[0].getID();
	}

	@Override
	public String getSummary() {
		StringBuffer ret = new StringBuffer();
		for (Discussion d : this.discussions) {
			ret.append(d.getID());
			ret.append(",");
		}
		return ret.toString();
	}

	@Override
	public void setSummary(String summary) {
		throw new RuntimeException(
				"Not supported; Try editing child discussions instead");
	}

	@Override
	public String getDescription() {
		return "complex discussion";
	}

	@Override
	public void setDescription(String description) {
		throw new RuntimeException(
				"Not supported; Try editing child discussions instead");
	}

	@Override
	public String getType() {
		return "complex discussion";
	}

	@Override
	public void setType(String type) {
		throw new RuntimeException(
				"Not supported; Try editing child discussions instead");
	}

	@Override
	public Date getCreationDate() {
		Date ret = new Date(Long.MAX_VALUE);

		for (Discussion d : this.discussions) {
			if (d.getCreationDate().before(ret))
				ret = d.getCreationDate();
		}

		return ret;
	}

	@Override
	public void setCreationDate(Date dateCreated) {
		throw new RuntimeException(
				"Not supported; Try editing child discussions instead");
	}

	@Override
	public String getCreator() {
		return "complex discussion";
	}

	@Override
	public void setCreator(String creator) {
		throw new RuntimeException(
				"Not supported; Try editing child discussions instead");
	}

	@Override
	public String getStatus() {
		return "complex discussion";
	}

	@Override
	public void setStatus(String status) {
		throw new RuntimeException(
				"Not supported; Try editing child discussions instead");
	}

	@Override
	public void setId(int id) {
		throw new RuntimeException(
				"Not supported; Try editing child discussions instead");
	}

	@Override
	public DiscussionEvent[] getDiscussionEvents() {
		if (this.events == null) {
			List<DiscussionEvent> tmp = new LinkedList<DiscussionEvent>();

			for (Discussion d : this.discussions)
				for (DiscussionEvent de : d.getDiscussionEvents())
					tmp.add(de);

			this.events = tmp.toArray(new DiscussionEvent[0]);
		}
		return this.events;
	}

	@Override
	public void addDiscussionEvents(DiscussionEvent[] des) {
		throw new RuntimeException(
				"Not supported; Try adding discussion events to child discussions instead");
	}

	@Override
	public Incident[] getIncidents() {
		if (this.incidentCache == null) {
			List<Incident> tmp = new LinkedList<Incident>();

			for (Discussion d : this.discussions) {
				for (Incident de : d.getIncidents()) {
					tmp.add(de);
				}
			}
			this.incidentCache = tmp.toArray(new Incident[0]);
		}
		return this.incidentCache;
	}

	@Override
	public void addIncidents(Incident[] incidents) {
		throw new RuntimeException("Not implemented yet");
	}

}
