package web.change.database;

import java.util.List;

public class CollectChanges {
	List<String> addPl(String nam, List<String>f ) {
		for(String s:nam.split(";")) {
			if(s=="")continue;
			f.add(s.trim());
		}
		return f;
	}
	
	List<String> delPl(String nam, List<String>f ) {
		for(String s:nam.split(";")) {
			if(s=="")continue;
			f.remove(s.trim());
		}
		return f;
	}
	
	
	
	
}
