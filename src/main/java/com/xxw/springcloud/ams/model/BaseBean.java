package com.xxw.springcloud.ams.model;

import java.io.Serializable;

public class BaseBean implements Serializable{
	
	private static final long serialVersionUID = 9009603142797628197L;
	
	public Header header;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}
	
}
