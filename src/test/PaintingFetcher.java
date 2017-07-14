package database.test;

import java.io.File;
import java.util.LinkedList;

public class PaintingFetcher {
	
	private AuthorInfo[] authors;
	private PaintingInfo[] paints;
	
	public PaintingFetcher() {
		AuthorInfo a = new AuthorInfo("Zou", "Chinese");
		AuthorInfo a2 = new AuthorInfo("Leonardo", "Italian");
		
		PaintingInfo p1 = new PaintingInfo(
				0, "Gioconda", new File("src/paints/Gioconda.jpg"), 1.155f, 0.743f, a2, 1500 );
		
		PaintingInfo p2 = new PaintingInfo(
				1, "Chou Fang", new File("src/paints/ChouFang.jpg"), 1.024f, 0.858f, a, 1000);
		
		authors = new AuthorInfo[]{a, a2};
		paints = new PaintingInfo[]{p1,p2};
	}
	
	public AuthorInfo[] getAllAuthors() {
		return authors;
	}
	
	public PaintingInfo[] getPaintingsFromAuthor(AuthorInfo author) {
		if(author == null) {
			return paints;
		} else {
			LinkedList<PaintingInfo> l = new LinkedList<PaintingInfo>();
			for(PaintingInfo p : paints)
				if(p.getAuthor() == author)
					l.add(p);
			PaintingInfo[] res = new PaintingInfo[l.size()];
			l.toArray(res);
			return res;
		}
	}
}
