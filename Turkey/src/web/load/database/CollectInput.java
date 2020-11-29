package web.load.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CollectInput {
	// for table
	int num = 90;
	HashMap<Integer, String> titles = new HashMap<Integer, String>();

	String printError(String h, int j) {
		if (j < 5)
			j = 0;
		else
			j -= 5;
		h = h.substring(j, Math.min(j + 15, h.length()));
		return "Error on expression " + h;
	}

	boolean is_english(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	boolean is_int(char c) {
		return c >= '0' && c <= '9';
	}

	String getForTitles(String inp) {
		if (titles.size() != 0) {
			titles.clear();
		}
		if (inp == null)
			return "Ok";
		String h = "";
		for (int i = 0; i < inp.length() + 1; i++) {
			if (i == inp.length() || inp.charAt(i) == ';') {
				if (h.isEmpty())
					continue;
				int num_col = 0;
				h = h.trim();
				int j;
				for (j = 0; j < h.length(); j++) {
					if (h.charAt(j) >= '0' && h.charAt(j) <= '9') {
						num_col = num_col * 10 + h.charAt(j) - '0';
						if (num_col > num) {
							return printError(h, j);
						}
					} else {
						break;
					}
				}
				if (num_col == 0) {
					return printError(h, j);
				}
				while (j < h.length() && h.charAt(j) != '-') {
					j++;
				}
				if (j == h.length())
					return printError(h, 0);
				h = h.substring(j + 1).trim();
				if (h.charAt(0) != '_' && !is_english(h.charAt(0))) {
					return printError(h, j);
				}
				for (; j < h.length(); j++) {
					if (h.charAt(j) == ' ') {
						h = h.substring(0, j + 1);
						break;
					}
					if (h.charAt(j) != '_' && !is_english(h.charAt(j)) && !is_int(h.charAt(j))) {
						return printError(h, j);
					}

				}
				if (titles.containsKey(num_col) || titles.containsValue(h)) {
					return "two equals";
				}
				titles.put(num_col, h);
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

	// for activity
	String title;
	String date_m, date_e;
	int type;
	String places;
	HashMap<String, String> act_add = new HashMap<String, String>();

	String getForAct(String inp) {
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

	String setAct(String title, String date_m, String date_e, int type, String places, String add) {
		this.title = title;
		this.type = type;
		this.date_m = date_m;
		this.date_e = date_e;
		this.places = places;
		return getForAct(add);
	}

	// for people

	boolean go_far = false;
	int[] param = new int[8];
	HashMap<Integer, String> peo_add = new HashMap<Integer, String>();

	String getForHomo(String inp) {
		if (peo_add.size() != 0) {
			peo_add.clear();
		}
		if (inp == null)
			return "Ok";
		String h = "";
		for (int i = 0; i < inp.length() + 1; i++) {
			if (i == inp.length() || inp.charAt(i) == ';') {
				if (h.isEmpty())
					continue;
				int num_col = 0;
				h = h.trim();
				int j;
				for (j = 0; j < h.length(); j++) {
					if (h.charAt(j) >= '0' && h.charAt(j) <= '9') {
						num_col = num_col * 10 + h.charAt(j) - '0';
						if (num_col > num) {
							return printError(h, j);
						}
					} else {
						break;
					}
				}
				if (num_col == 0) {
					return printError(h, j);
				}
				if (j == h.length()) {
					if (peo_add.containsKey(num_col)) {
						return "two equals";
					}
					peo_add.put(num_col, null);
					h = "";
					continue;
				}
				while (j < h.length() && h.charAt(j) != '-') {
					j++;
				}
				if (j == h.length())
					return printError(h, 0);
				h = h.substring(j + 1).trim();
				if (peo_add.containsKey(num_col) || peo_add.containsValue(h)) {
					return "two equals";
				}
				peo_add.put(num_col, h);
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

	String setHomo(int[] param, String add) {
		go_far = true;
		this.param = param;
		return getForHomo(add);
	}

	String print_me() {
		// this function only for testing
		String all = "Helowelowelou<br>";
		if (num == 0) {
			all += "there isn't a table";
		} else {
			if (titles.isEmpty()) {
				all += "there is a table and it has titles";
			} else {
				all += "there is a table but without titles<br>";
				for (Entry<Integer, String> entry : titles.entrySet()) {
					all += entry.getKey() + " : " + entry.getValue() + "<br>";
				}
			}
		}
		all += "<br> Now about activity<br>";
		all += title + "<br>" + date_m + "<br>" + date_e + "<br>" + type + "<br>" + places + "<br>";
		for (Map.Entry<String, String> entry : act_add.entrySet()) {
			all += entry.getKey() + " : " + entry.getValue() + "<br>";
		}
		if (go_far) {
			all += "Now about people<br>";
			for (int i = 0; i < param.length; i++) {
				all += param[i] + "  ";
			}
			for (Map.Entry<Integer, String> entry : peo_add.entrySet()) {
				all += entry.getKey() + " : " + entry.getValue() + "<br>";
			}
		}
		return all;
	}

}
