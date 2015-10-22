package org.computer.knauss.reqtDiscussion.model;

import java.util.List;
import java.util.Vector;

public class FilteredDiscussionEventList implements
		IFilteredDiscussionEventList {

	private List<IDiscussionEventFilter> filters = new Vector<IDiscussionEventFilter>();
	private List<DiscussionEvent> unfilteredList = new Vector<DiscussionEvent>();
	private List<DiscussionEvent> filteredList = new Vector<DiscussionEvent>();

	@Override
	public List<DiscussionEvent> getFilteredDiscussionEventList() {
		return filteredList;
	}

	@Override
	public DiscussionEvent getWorkitemComment(int i) {
		return this.filteredList.get(i);
	}

	@Override
	public void addFilter(IDiscussionEventFilter filter) {
		this.filters.add(filter);
		updateFilteredList();
	}

	@Override
	public void removeFilter(IDiscussionEventFilter filter) {
		this.filters.remove(filter);
		updateFilteredList();
	}

	private void updateFilteredList() {
		this.filteredList.clear();
		for (DiscussionEvent cwc : this.unfilteredList) {
			boolean accept = true;
			for (IDiscussionEventFilter filter : this.filters) {
				if (!filter.accept(cwc))
					accept = false;
			}
			if (accept)
				this.filteredList.add(cwc);
		}
	}

	@Override
	public void clear() {
		this.unfilteredList.clear();
		this.filteredList.clear();
	}

	@Override
	public IDiscussionEventFilter[] getFilters() {
		return this.filters.toArray(new IDiscussionEventFilter[0]);
	}

	@Override
	public void setWorkitemCommentList(List<DiscussionEvent> data) {
		this.unfilteredList = data;
		updateFilteredList();
	}

	@Override
	public void add(DiscussionEvent event) {
		this.unfilteredList.add(event);
		updateFilteredList();
	}
}
