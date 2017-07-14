package modules.art2d;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import database.AuthorInfo;
import database.DBConnection;

class Fetcher {

	private DBConnection db;

	Fetcher(DBConnection dbConn) throws SQLException {
		this.db = dbConn;
		initDB();
	}

	// TODO
	private void initDB() throws SQLException {

	}

	String[] getArtTypes() throws SQLException {
		String query = "SELECT * FROM ArtType";
		ResultSet rs = db.query(query);
		LinkedList<String> types = new LinkedList<String>();
		while(rs.next())
			types.add(rs.getString("type_name"));
		String[] result = new String[types.size()];
		types.toArray(result);
		return result;
	}

	AuthorInfo[] getAuthors(String type) throws SQLException {
		String query;
		if(type == null) {
			query = 
				"SELECT * " +
				"FROM Authors U " +
				"WHERE EXISTS 	(SELECT * " +
				"FROM Artwork2D A " +
				"WHERE A.author = U.author_id); ";
		} else {
			query =
				"SELECT * " +
				"FROM Authors U " +
				"WHERE EXISTS 	(SELECT * " +
				"FROM Artwork2D A, ArtType T " +
				"WHERE A.author = U.author_id " +
				"AND A.type = T.type_id " +
				"AND T.type_name = '" +type + "'); ";
		}

		ResultSet rs = db.query(query);
		LinkedList<AuthorInfo> temp = new LinkedList<AuthorInfo>();
		while(rs.next()) {
			AuthorInfo a = new AuthorInfo();
			a.setID(rs.getInt("author_id"));
			a.setName(rs.getString("author_name"));
			a.setNationality(rs.getString("author_nationality"));
			temp.add(a);
		}
		rs.close();
		AuthorInfo[] auths = new AuthorInfo[temp.size()];
		temp.toArray(auths);
		return auths;
	}

	Artwork2DInfo[] getArtwork2D(AuthorInfo author, String type) throws SQLException {
		String query;
		query = "SELECT * " +
				"FROM Artwork2D A, Authors U, ArtType T " +
				"WHERE A.author = U.author_id " +
				"AND A.type = T.type_id ";

		if(author != null)
			query += "AND A.author = " + author.getID() + " ";

		if(type != null)
			query += "AND T.type_name = '" + type + "';";

		ResultSet rs = db.query(query);
		LinkedList<Artwork2DInfo> temp = new LinkedList<Artwork2DInfo>();

		while(rs.next()) {
			temp.add(fillArt(rs, author));
		}
		rs.close();
		Artwork2DInfo[] arts = new Artwork2DInfo[temp.size()];
		temp.toArray(arts);
		return arts;
	}

	Artwork2DInfo getArtById(int id) throws SQLException {
		String query = 	"SELECT * " +
		"FROM Artwork2D A, Authors U, ArtType T " +
		"WHERE A.art_id = " + id;

		ResultSet rs = db.query(query);
		if(rs.next()) {
			return fillArt(rs, null);
		} else
			throw new SQLException();
	}

	private Artwork2DInfo fillArt(ResultSet rs, AuthorInfo author) {
		Artwork2DInfo a = new Artwork2DInfo();
		try {
			a.setId(rs.getInt("art_id"));
			a.setYear(rs.getInt("year"));
			a.setWidth(rs.getFloat("width"));
			a.setHeight(rs.getFloat("height"));
			a.setTitle(rs.getString("title"));
			a.setImage(new File(rs.getString("image_path")));
			a.setType(rs.getString("type_name"));

			if(author == null) {
				author = new AuthorInfo();
				author.setID(rs.getInt("author_id"));
				author.setName(rs.getString("author_name"));
				author.setNationality(rs.getString("author_nationality"));
			}
			a.setAuthor(author);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return a;
	}
}
