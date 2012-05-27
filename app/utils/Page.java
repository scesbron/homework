package utils;

import java.util.List;

public class Page<T> {
	public int number;
	public int pageSize;
	public String query;

	public List<T> data;

	public boolean hasPrevious() {
		return number > 1;
	}

	public boolean hasNext() {
		return data != null && data.size() == pageSize;
	}

	public Page(int number, String query, int pageSize) {
		this.number = number > 0 ? number : 1;
		this.query = query;
		this.pageSize = pageSize;
	}

	public int startPos() {
		return ((number - 1) * pageSize) + 1;
	}

	public int endPos() {
		return startPos() + data.size() - 1;
	}
}
