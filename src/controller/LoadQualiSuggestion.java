package controller;

import java.util.ArrayList;
import java.util.List;
import database.Model;
import model.Uc_qualifications;

public class LoadQualiSuggestion {
	Model m;
	List<Uc_qualifications> ucQualiList;
	public LoadQualiSuggestion() {
		m = new Model();
		ucQualiList = new ArrayList<Uc_qualifications>();
	}
	public List<String> getData(String query) {
		query = query.toLowerCase();
		ucQualiList = (List<Uc_qualifications>)(Object) m.selectData("model.Uc_qualifications", "1");
		List<String> matched = new ArrayList<String>();
		if(!ucQualiList.isEmpty()) {
			for(int i=0 ; i<ucQualiList.size() ; i++) {
				if(ucQualiList.get(i).toString().toLowerCase().startsWith(query)) matched.add(ucQualiList.get(i).toString());
			}
		}
		if(matched.isEmpty()) {
			matched.add("No suggestions found");
		}
		System.out.println(matched.size());
		return matched;
	}
}
