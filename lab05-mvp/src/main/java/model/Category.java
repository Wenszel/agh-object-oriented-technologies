package model;

public class Category {
	private String name;
	private Category parent;
	
	
	public Category(String name, Category parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public Category(String name) {
		this(name, null);
	}

	public final String getName() {
		return name;
	}

	public final Category getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return name;
	}
}
