package com.consult.listen;

import org.springframework.context.ApplicationEvent;

public class MyEvent extends ApplicationEvent {
	
	String param1;
	
	String param2;

	public MyEvent(Object source) {
		super(source);
	}
	
	public MyEvent(Object source,String param1,String param2) {
		super(source);
		this.param1 = param1;
		this.param2 = param2;
	}

	@Override
	public Object getSource() {
		// TODO Auto-generated method stub
		return super.getSource();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

}
