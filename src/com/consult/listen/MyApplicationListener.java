package com.consult.listen;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class MyApplicationListener implements ApplicationListener {

	public void onApplicationEvent(ApplicationEvent arg0) {
		if(arg0 instanceof MyEvent) {
			MyEvent event = (MyEvent)arg0;
			System.out.println(event.getSource());
			System.out.println(event.getParam1());
			System.out.println(event.getParam2());
		}
	}
}
