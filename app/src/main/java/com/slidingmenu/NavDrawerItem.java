package com.slidingmenu;

public class NavDrawerItem {

	private String title;
	private int icon;
	private String count = "0";
	private boolean isCounterVisible = false;

	public NavDrawerItem() {
	}

	public NavDrawerItem(String title, int icon) {
		this.setTitle(title);
		this.setIcon(icon);
	}

	public NavDrawerItem(String title, int icon, boolean isCounterVisible,
			String count) {
		this.setTitle(title);
		this.setIcon(icon);
		this.setCounterVisibility(isCounterVisible);
		this.setCount(count);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public boolean getCounterVisibility() {
		return isCounterVisible;
	}

	public void setCounterVisibility(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}
}
