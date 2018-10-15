/**
 * @author huDong
 */
package com.hu.security.browser.support;

/**
 * @author Administrator
 *
 */
public class SimpleResponse {
	
	public SimpleResponse(Object content) {
		this.content = content;
		
	}
	
	private Object content;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	

}
