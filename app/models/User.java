package models;
import com.google.gson.annotations.SerializedName;

public class User {
	//@SerializedName("user")
	private String firstName;
	private String lastName;
	private String occupation;

	public User() {

	}

	

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}	

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	public String getOccupation() {
		return occupation;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	@Override
	public String toString(){
		return firstName + " - " + lastName + " - " + occupation ;
	}
}
