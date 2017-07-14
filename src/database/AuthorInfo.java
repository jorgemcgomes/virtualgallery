package database;

public class AuthorInfo {
	
	private String name;
	private String nationality;
	private int id;

	public void setName(String name) {
		this.name = name;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	public void setID(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getNationality() {
		return nationality;
	}
	
	public int getID() {
		return id;
	}
	
	

}
