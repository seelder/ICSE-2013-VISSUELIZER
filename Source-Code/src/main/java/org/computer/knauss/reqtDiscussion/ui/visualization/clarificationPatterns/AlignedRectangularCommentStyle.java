package org.computer.knauss.reqtDiscussion.ui.visualization.clarificationPatterns;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedHashSet;
import java.util.Set;

import org.computer.knauss.reqtDiscussion.model.ModelElement;
import org.computer.knauss.reqtDiscussion.model.partition.IDiscussionOverTimePartition;
import org.computer.knauss.reqtDiscussion.ui.visualization.IVisualizationStyle;

public class AlignedRectangularCommentStyle extends AbstractVisualizationStyle
		implements IVisualizationStyle {

	public static final int COMMENT_HEIGHT = 20;
	public static final int VERTICAL_COMMENT_GAP = 5;
	public static final int HORIZONTAL_COMMENT_GAP = 5;

	// private static final Color IN_CLASS_COLOR = new Color(0x9A0008);
	// private static final Color NOT_IN_CLASS_COLOR = new Color(0x00569A);
	private static final Color IN_CLASS_COLOR = new Color(215, 25, 28);
	private static final Color NOT_IN_CLASS_COLOR = new Color(43, 131, 186);
	private static final Color NOT_CLASSIFIED_COLOR = Color.GRAY;

	private IDiscussionOverTimePartition partition;
	private int xOffset;
	private int yOffset;
	private Set<ModelElement>[] elementsInClassPerPartition;
	private Set<ModelElement>[] elementsNotInClassPerPartition;

	@Override
	public void setDiscussionOverTimePartition(
			IDiscussionOverTimePartition partition, int xOffset, int yOffset) {
		this.partition = partition;
		this.xOffset = xOffset;
		this.yOffset = yOffset;

		reset();
	}

	@Override
	public Shape[] getShape(ModelElement comment) {
		// System.out.print("getShape: ");
		int partition = this.partition.getPartitionForModelElement(comment);
		// System.out.print("partition: " + partition + "("
		// + this.partition.getPartitionCount() + ") ");

		// compute w(idth)
		double partitionWidth = (double) X_SIZE
				/ (double) this.partition.getPartitionCount();
		double w = partitionWidth - HORIZONTAL_COMMENT_GAP;
		// System.out.print("width: <" + w + "> ");

		// Compute the x
		double x = this.xOffset + partition * partitionWidth
				+ ((double) HORIZONTAL_COMMENT_GAP) / (double) 2;
		// System.out.print("x: <" + x + "> ");

		// Compute the y
		double y = 0;
		if (this.partition.isInClass(comment)) {
			y = getYPositionInPartition(comment,
					this.elementsInClassPerPartition[partition]);
			y = this.yOffset + (y - 1)
					* (COMMENT_HEIGHT + VERTICAL_COMMENT_GAP)
					+ VERTICAL_COMMENT_GAP;
		} else {
			y = getYPositionInPartition(comment,
					this.elementsNotInClassPerPartition[partition]);
			y = this.yOffset - y * (COMMENT_HEIGHT + VERTICAL_COMMENT_GAP);
		}
		// System.out.println("y: <" + y + "> ");

		double arcw = 15;
		double arch = 15;
		return new Shape[] { new RoundRectangle2D.Double(x, y, w,
				COMMENT_HEIGHT, arcw, arch) };
	}

	private double getYPositionInPartition(ModelElement comment,
			Set<ModelElement> elementsInPartition) {
		double y = 0;
		if (elementsInPartition.contains(comment)) {
			// figure out the position of this comment in the set.
			for (ModelElement wc : elementsInPartition) {
				y++;
				if (wc.equals(comment))
					break;
			}
		} else {
			elementsInPartition.add(comment);
			y = elementsInPartition.size();
		}
		return y;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reset() {
		if (this.elementsInClassPerPartition == null
				|| this.elementsInClassPerPartition.length != this.partition
						.getPartitionCount()) {
			this.elementsInClassPerPartition = new Set[this.partition
					.getPartitionCount()];
			this.elementsNotInClassPerPartition = new Set[this.partition
					.getPartitionCount()];
			for (int i = 0; i < this.elementsInClassPerPartition.length; i++) {
				this.elementsInClassPerPartition[i] = new LinkedHashSet<ModelElement>();
				this.elementsNotInClassPerPartition[i] = new LinkedHashSet<ModelElement>();
			}
		}

		if (this.elementsInClassPerPartition != null)
			for (Set<ModelElement> s : this.elementsInClassPerPartition)
				if (s != null)
					s.clear();
		if (this.elementsNotInClassPerPartition != null)
			for (Set<ModelElement> s : this.elementsNotInClassPerPartition)
				if (s != null)
					s.clear();
	}

	@Override
	public Color getFillColor(ModelElement comment, int i) {
		if (!this.partition.isClassified(comment))
			return NOT_CLASSIFIED_COLOR;

		if (this.partition.isInClass(comment))
			// return Color.MAGENTA;
			return IN_CLASS_COLOR;
		// return Color.BLACK;
		return NOT_IN_CLASS_COLOR;
	}

	@Override
	public Color getDrawColor(ModelElement comment, int i) {
		return null;
	}

}
