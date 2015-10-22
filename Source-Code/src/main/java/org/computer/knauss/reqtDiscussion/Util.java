package org.computer.knauss.reqtDiscussion;

import java.sql.Date;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.computer.knauss.reqtDiscussion.io.util.DateParser;
import org.computer.knauss.reqtDiscussion.model.Discussion;
import org.computer.knauss.reqtDiscussion.model.DiscussionEvent;

public class Util {
	private static Random random;

	public static Date parseDate(String dateString) {
		return DateParser.getInstance().parseDate(dateString);
	}

	public static String idsToKey(int[] ids) {
		StringBuffer sb = new StringBuffer();
		for (int id : ids) {
			sb.append(id);
			sb.append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static int[] stringToID(String idString) {
		String[] tokens = idString.substring(1).split(",");

		int[] tmp = new int[tokens.length];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = Integer.parseInt(tokens[i].trim());
		}
		return tmp;
	}

	public static void sortByID(Discussion[] discussions) {
		Arrays.sort(discussions, new Comparator<Discussion>() {

			@Override
			public int compare(Discussion o1, Discussion o2) {
				return ((Integer) o1.getID()).compareTo(o2.getID());
			}

		});
	}

	public static void sortByDate(DiscussionEvent[] des) {
		Arrays.sort(des, new Comparator<DiscussionEvent>() {
			@Override
			public int compare(DiscussionEvent o1, DiscussionEvent o2) {
				return o1.getCreationDate().compareTo(o2.getCreationDate());
			}
		});
	}

	public synchronized static Random getRandom() {
		if (random == null) {
			long currentTimeMillis = System.currentTimeMillis();
			random = new Random(currentTimeMillis);
			System.out.println("Random seed: " + currentTimeMillis);
		}
		return random;
	}

	public static void useSeed(long seed) {
		getRandom().setSeed(seed);
		System.out.println("Random seed: " + seed);
	}
}
