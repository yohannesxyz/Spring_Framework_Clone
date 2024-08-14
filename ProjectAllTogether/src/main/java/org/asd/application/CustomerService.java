package org.asd.application;

import org.asd.frameWork.*;

@Service
public class CustomerService implements ICustomerService {



	public Customer addCustomer(String name, String email, String street, String city, String zip) {
		Customer customer = new Customer(name, email);
		Address address = new Address(street, city, zip);
		customer.setAddress(address);
		return customer;
	}



}
