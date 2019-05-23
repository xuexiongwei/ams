package com.xxw.springcloud.ams.model;

import java.io.Serializable;

public class SysMenuBean extends SysMenu implements Serializable {

	private int isAssignMenu;

	public int getIsAssignMenu() {
		return isAssignMenu;
	}

	public void setIsAssignMenu(int isAssignMenu) {
		this.isAssignMenu = isAssignMenu;
	}
}
