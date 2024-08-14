package org.asd.application;

import org.asd.frameWork.Aspect;
import org.asd.frameWork.Before;

@Aspect
public class NewCustomerEvent {
	private Customer customer;

	public NewCustomerEvent(Customer customer) {
		super();
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
