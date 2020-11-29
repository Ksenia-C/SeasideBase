package web.asking.database;

import java.util.HashMap;

public class CollectParameter {
	String printError(String h, int j) {
		if (j < 5)
			j = 0;
		else
			j -= 5;
		h = h.substring(j, Math.min(j + 15, h.length()));
		return "Error on expression " + h;
	}

	HashMap<String, String> peo_add = new HashMap<String, String>();

	String getForpeo(String inp) {
		if (inp == null) {
			return "Ok";
		}
		if (!peo_add.isEmpty()) {
			peo_add.clear();
		}
		String h = "";
		for (int i = 0; i <= inp.length(); i++) {
			if (i == inp.length() || inp.charAt(i) == ';') {
				if (h.isEmpty())
					continue;
				h = h.trim();
				int j = 0;
				while (j < h.length() && h.charAt(j) != '-') {
					j++;
				}
				if (j == h.length())
					return printError(h, 0);
				String k = h.substring(0, j).trim(), v = h.substring(j + 1).trim();
				if (peo_add.containsKey(k)) {
					return "two equals";
				}
				peo_add.put(k, v);
				h = "";
				continue;
			}
			if (inp.indexOf(i) == '\\') {
				h = h + inp.charAt(i + 1);
				i++;
			} else {
				h = h + inp.charAt(i);
			}
		}
		return "Ok";
	}

	HashMap<String, String> act_add = new HashMap<String, String>();

	String getForact(String inp) {
		if (inp == null) {
			return "Ok";
		}
		if (!act_add.isEmpty()) {
			act_add.clear();
		}
		String h = "";
		for (int i = 0; i <= inp.length(); i++) {
			if (i == inp.length() || inp.charAt(i) == ';') {
				if (h.isEmpty())
					continue;
				h = h.trim();
				int j = 0;
				while (j < h.length() && h.charAt(j) != '-') {
					j++;
				}
				if (j == h.length())
					return printError(h, 0);
				String k = h.substring(0, j).trim(), v = h.substring(j + 1).trim();
				if (act_add.containsKey(k)) {
					return "two equals";
				}
				act_add.put(k, v);
				h = "";
				continue;
			}
			if (inp.indexOf(i) == '\\') {
				h = h + inp.charAt(i + 1);
				i++;
			} else {
				h = h + inp.charAt(i);
			}
		}
		return "Ok";
	}

}
