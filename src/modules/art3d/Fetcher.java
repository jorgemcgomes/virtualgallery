package modules.art3d;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.vecmath.Point3f;

import core.Gallery;
import core.StatusEvent;

import database.AuthorInfo;
import database.DBConnection;

class Fetcher {
	
	private DBConnection db;

	Fetcher(DBConnection dbConn) throws SQLException {
		this.db = dbConn;
	}
	
	AuthorInfo[] getAuthors() throws SQLException {
		String query = 
				"SELECT * " +
				"FROM Authors U " +
				"WHERE EXISTS 	(SELECT * " +
								"FROM Artwork3D A " +
								"WHERE A.author = U.author_id); ";

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

	Artwork3DInfo[] getArtwork3D(AuthorInfo author) throws SQLException {
		String query;
		query = "SELECT * " +
				"FROM Artwork3D A, Authors U " +
				"WHERE A.author = U.author_id ";

		if(author != null)
			query += "AND A.author = " + author.getID() + " ";

		ResultSet rs = db.query(query);
		LinkedList<Artwork3DInfo> temp = new LinkedList<Artwork3DInfo>();
	
		while(rs.next()) {
			Artwork3DInfo a = fillArt(rs, author);
			temp.add(a);
		}
		
		rs.close();
		Artwork3DInfo[] objs = new Artwork3DInfo[temp.size()];
		temp.toArray(objs);
		return objs;
	}
	
	void insertBounds(Artwork3DInfo a, Point3f upper, Point3f lower) {
		String upStr = upper.x + " " + upper.y + " " + upper.z;
		String loStr = lower.x + " " + lower.y + " " + lower.z;
		String query =	"UPDATE Artwork3D " +
						"SET bbox_upper = '" + upStr + "', bbox_lower = '" + loStr + "' " +
						"WHERE art_id = " + a.getID();
		try {
			db.update(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	Artwork3DInfo getArtById(int id) throws SQLException {
		String query = 	"SELECT * " +
						"FROM Artwork3D A, Authors U " +
						"WHERE A.art_id = " + id;

		ResultSet rs = db.query(query);
		if(rs.next()) {
			Artwork3DInfo a = fillArt(rs, null);
			return a;
		} else
			throw new SQLException();
	}
	
	private Artwork3DInfo fillArt(ResultSet rs, AuthorInfo author) throws SQLException {
		Artwork3DInfo a = new Artwork3DInfo(this);
		a.setID(rs.getInt("art_id"));
		a.setName(rs.getString("title"));
		a.setYear(rs.getInt("year"));
		a.setModel(new File(rs.getString("model_path")));
		a.setBounds(rs.getString("bbox_upper"), rs.getString("bbox_lower"));
		String t = rs.getString("image_path");
		if(t != null)
			a.setThumbnail(new File(t));
		a.setDimensions(rs.getFloat("height"), rs.getFloat("width"), rs.getFloat("length"));
		
		if(author == null) {
			author = new AuthorInfo();
			author.setID(rs.getInt("author_id"));
			author.setName(rs.getString("author_name"));
			author.setNationality(rs.getString("author_nationality"));
		}
		a.setAuthor(author);
		return a;
	}

}
