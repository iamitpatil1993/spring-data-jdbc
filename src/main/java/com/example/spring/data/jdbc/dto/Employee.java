/**
 * 
 */
package com.example.spring.data.jdbc.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author amipatil
 *
 */
public class Employee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7354573401209029760L;
	
	private String employeeId;
	private String firstName;
	private String lastName;
	private Date dateOfJoining;
	private String designation;
	private List<Address> address;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}
}
