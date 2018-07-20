package base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StringTableFile")
@XmlAccessorType(XmlAccessType.FIELD)
class StringTableFile {
	private String Name;
	private int NextEntryID;
	private int EntryCount;
	@XmlElementWrapper
	@XmlElement(name = "Entry")
	private List<Entry> Entries = new ArrayList<>();

	public List<Entry> getEntries() {
		return Entries;
	}

	public void setEntries(List<Entry> entries) {
		Entries = entries;
	}

	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	public int getEntryCount() {
		return EntryCount;
	}
	
	public void setEntryCount(int entryCount) {
		EntryCount = entryCount;
	}
	
	public int getNextEntryID() {
		return NextEntryID;
	}
	
	public void setNextEntryID(int nextEntryID) {
		NextEntryID = nextEntryID;
	}
	
}

@XmlRootElement(name = "Entry")
@XmlAccessorType(XmlAccessType.FIELD)
class Entry {
	private int ID;
	private String DefaultText;
	private String FemaleText;

	public int getID() {
		return ID;
	}
	
	public String getDefaultText() {
		return DefaultText;
	}

	public Pair<Long, Long> cleanAnführungszeichen() {
		long unbalanciert = 0, tausch = 0;
		long count = DefaultText.chars().filter(ch -> ch == '"').count();
		if (count % 2 == 1 && !DefaultText.contains("=")) {
			String[] parts = DefaultText.split("\n");
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].startsWith("\"") && parts[i].chars().filter(ch -> ch == '"').count() == 1) {
					parts[i] += "\"";
					unbalanciert++;
				}
			}
			DefaultText = String.join("\n", parts);
		}

		while (DefaultText.contains("”")) {
			DefaultText = DefaultText.replaceFirst("”", "“"); // Englische Anführungszeichen
			tausch++;
		}

		while (DefaultText.contains("\"") && !DefaultText.contains("=")) {
			DefaultText = DefaultText.replaceFirst("\"", "„").replaceFirst("\"", "“");
			tausch++;
		}

		count = FemaleText.chars().filter(ch -> ch == '"').count();
		if (count % 2 == 1 && !FemaleText.contains("=")) {
			String[] parts = FemaleText.split("\n");
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].startsWith("\"") && parts[i].chars().filter(ch -> ch == '"').count() == 1) {
					parts[i] += "\"";
					unbalanciert++;
				}
			}
			FemaleText = String.join("\n", parts);
		}

		while (FemaleText.contains("”")) {
			FemaleText = FemaleText.replaceFirst("”", "“"); // Englische Anführungszeichen
			tausch++;
		}

		while (FemaleText.contains("\"") && !FemaleText.contains("=")) {
			FemaleText = FemaleText.replaceFirst("\"", "„").replaceFirst("\"", "“");
			tausch++;
		}

		return new Pair<Long, Long>(unbalanciert, tausch);
	}

	public Pair<Long, Long> repairTags() {
		long tokenFehler = 0, tausch = 0;
		if (DefaultText.contains("=„")) {
			int i = 0;
			i = DefaultText.indexOf("link=", 0);
			if (i > 0)
				DefaultText = DefaultText.substring(0, i + 5)
						+ DefaultText.substring(i + 5).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = DefaultText.indexOf("color=", 0);
			if (i > 0)
				DefaultText = DefaultText.substring(0, i + 6)
						+ DefaultText.substring(i + 6).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = DefaultText.indexOf("sprite=", 0);
			if (i > 0)
				DefaultText = DefaultText.substring(0, i + 6)
						+ DefaultText.substring(i + 6).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = DefaultText.indexOf("name=", 0);
			if (i > 0)
				DefaultText = DefaultText.substring(0, i + 5)
						+ DefaultText.substring(i + 5).replaceFirst("„", "\"").replaceFirst("“", "\"");

			tokenFehler++;
		}

		if (DefaultText.contains("=“")) {
			int i = 0;
			i = DefaultText.indexOf("link=", 0);
			if (i > 0)
				DefaultText = DefaultText.substring(0, i + 5)
						+ DefaultText.substring(i + 5).replaceFirst("“", "\"").replaceFirst("„", "\"");
			i = DefaultText.indexOf("color=", 0);
			if (i > 0)
				DefaultText = DefaultText.substring(0, i + 6)
						+ DefaultText.substring(i + 6).replaceFirst("“", "\"").replaceFirst("„", "\"");
			i = DefaultText.indexOf("sprite=", 0);
			if (i > 0)
				DefaultText = DefaultText.substring(0, i + 6)
						+ DefaultText.substring(i + 6).replaceFirst("“", "\"").replaceFirst("„", "\"");
			i = DefaultText.indexOf("name=", 0);
			if (i > 0)
				DefaultText = DefaultText.substring(0, i + 5)
						+ DefaultText.substring(i + 5).replaceFirst("“", "\"").replaceFirst("„", "\"");

			tokenFehler++;
		}

		if (DefaultText.contains("/xg")) {
			DefaultText = DefaultText.replaceFirst("xg", "g").replaceFirst("xg", "g");

			tokenFehler++;
		}
		if (FemaleText.contains("/xg")) {
			FemaleText = FemaleText.replaceFirst("xg", "g").replaceFirst("xg", "g");

			tokenFehler++;
		}

		if (FemaleText.contains("=„")) {
			int i = 0;
			i = FemaleText.indexOf("link=", 0);
			if (i > 0)
				FemaleText = FemaleText.substring(0, i + 5)
						+ FemaleText.substring(i + 5).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = FemaleText.indexOf("color=", 0);
			if (i > 0)
				FemaleText = FemaleText.substring(0, i + 6)
						+ FemaleText.substring(i + 6).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = FemaleText.indexOf("sprite=", 0);
			if (i > 0)
				FemaleText = FemaleText.substring(0, i + 6)
						+ FemaleText.substring(i + 6).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = FemaleText.indexOf("name=", 0);
			if (i > 0)
				FemaleText = FemaleText.substring(0, i + 5)
						+ FemaleText.substring(i + 5).replaceFirst("„", "\"").replaceFirst("“", "\"");

			tokenFehler++;
		}

		if (FemaleText.contains("=“")) {
			int i = 0;
			i = FemaleText.indexOf("link=", 0);
			if (i > 0)
				FemaleText = FemaleText.substring(0, i + 5)
						+ FemaleText.substring(i + 5).replaceFirst("“", "\"").replaceFirst("„", "\"");
			i = FemaleText.indexOf("color=", 0);
			if (i > 0)
				FemaleText = FemaleText.substring(0, i + 6)
						+ FemaleText.substring(i + 6).replaceFirst("“", "\"").replaceFirst("„", "\"");
			i = FemaleText.indexOf("sprite=", 0);
			if (i > 0)
				FemaleText = FemaleText.substring(0, i + 6)
						+ FemaleText.substring(i + 6).replaceFirst("“", "\"").replaceFirst("„", "\"");
			i = FemaleText.indexOf("name=", 0);
			if (i > 0)
				FemaleText = FemaleText.substring(0, i + 5)
						+ FemaleText.substring(i + 5).replaceFirst("“", "\"").replaceFirst("„", "\"");

			tokenFehler++;
		}
		return new Pair<Long, Long>(tokenFehler, tausch);
	}

	public Pair<Long, Long> cleanPunkte() {
		long ellipsen = 0, satzzeichen = 0;
		while (DefaultText.contains("...")) {
			DefaultText = DefaultText.replaceFirst("\\.\\.\\.", "…");
			ellipsen++;
		}
		while (DefaultText.contains("..")) {
			DefaultText = DefaultText.replaceFirst("\\.\\.", ".");
			satzzeichen++;
		}

		while (FemaleText.contains("...")) {
			FemaleText = FemaleText.replaceFirst("\\.\\.\\.", "…");
			satzzeichen++;
		}
		while (FemaleText.contains("..")) {
			FemaleText = FemaleText.replaceFirst("\\.\\.", ".");
			satzzeichen++;
		}

		return new Pair<Long, Long>(ellipsen, satzzeichen);
	}

	public Pair<Long, Long> cleanLeerzeichen() {
		long leerzeichen = 0, satzzeichen = 0;
		while (DefaultText.contains("  ")) {
			DefaultText = DefaultText.replaceFirst("  ", " ");
			leerzeichen++;
		}
		while (DefaultText.contains(" .")) {
			DefaultText = DefaultText.replaceFirst(" \\.", ". ");
			satzzeichen++;
		}
		while (DefaultText.contains(" !")) {
			DefaultText = DefaultText.replaceFirst(" !", "! ");
			satzzeichen++;
		}
		while (DefaultText.contains(" ?")) {
			DefaultText = DefaultText.replaceFirst(" \\?", "? ");
			satzzeichen++;
		}
		while (DefaultText.contains(" ,")) {
			DefaultText = DefaultText.replaceFirst(" ,", ", ");
			satzzeichen++;
		}
		while (DefaultText.contains(" :")) {
			DefaultText = DefaultText.replaceFirst(" :", ": ");
			satzzeichen++;
		}
		while (DefaultText.contains(" “")) {
			DefaultText = DefaultText.replaceFirst(" “", "“ ");
			satzzeichen++;
		}
		while (DefaultText.contains("““")) {
			DefaultText = DefaultText.replaceFirst("““", "“");
			satzzeichen++;
		}
		while (DefaultText.contains("„ ")) {
			DefaultText = DefaultText.replaceFirst("„ ", " „");
			satzzeichen++;
		}

		while (FemaleText.contains("  ")) {
			FemaleText = FemaleText.replaceFirst("  ", " ");
			leerzeichen++;
		}
		while (FemaleText.contains(" .")) {
			FemaleText = FemaleText.replaceFirst(" \\.", ". ");
			satzzeichen++;
		}
		while (FemaleText.contains(" !")) {
			FemaleText = FemaleText.replaceFirst(" !", "! ");
			satzzeichen++;
		}
		while (FemaleText.contains(" ?")) {
			FemaleText = FemaleText.replaceFirst(" \\?", "? ");
			satzzeichen++;
		}
		while (FemaleText.contains(" ,")) {
			FemaleText = FemaleText.replaceFirst(" ,", ", ");
			satzzeichen++;
		}
		while (FemaleText.contains(" :")) {
			FemaleText = FemaleText.replaceFirst(" :", ": ");
			satzzeichen++;
		}
		while (FemaleText.contains(" “")) {
			FemaleText = FemaleText.replaceFirst(" “", "“ ");
			satzzeichen++;
		}
		while (FemaleText.contains("““")) {
			FemaleText = FemaleText.replaceFirst("““", "“");
			satzzeichen++;
		}
		while (FemaleText.contains("„ ")) {
			FemaleText = FemaleText.replaceFirst("„ ", " „");
			satzzeichen++;
		}

		return new Pair<Long, Long>(leerzeichen, satzzeichen);
	}

	// mind. 2 heißt es ist keine Auslassung, meistens jedenfalls
	public Pair<Long, Long> cleanApostroph() {
		long apostroph = 0, keineSchachtel = 0;
		/*
		 * while(DefaultText.chars().filter(ch -> ch == '\'').count() >= 2) {
		 * DefaultText = DefaultText.replaceFirst("\'", "‚").replaceFirst("\'", "‘");
		 * apostroph++; } while(FemaleText.chars().filter(ch -> ch == '\'').count() >=
		 * 2) { FemaleText = FemaleText.replaceFirst("\'", "‚").replaceFirst("\'", "‘");
		 * apostroph++; }
		 */

		// Teil 3. der Funktion.
		// DefaultText = DefaultText.replaceAll("\'", "’");
		// FemaleText = FemaleText.replaceAll("\'", "’");

		while (DefaultText.contains("‚") && DefaultText.contains("‘") && !DefaultText.contains("„")
				&& !DefaultText.contains("“")) {
			DefaultText = DefaultText.replaceFirst("‚", "„").replaceFirst("‘", "“");
			keineSchachtel++;
		}

		while (FemaleText.contains("‚") && FemaleText.contains("‘") && !FemaleText.contains("„")
				&& !FemaleText.contains("“")) {
			FemaleText = FemaleText.replaceFirst("‚", "„").replaceFirst("‘", "“");
			keineSchachtel++;
		}

		return new Pair<Long, Long>(apostroph, keineSchachtel);
	}

	public Pair<Long, Long> cleanBindestrich() {
		long bindestrich = 0;
		while (DefaultText.contains(" - ")) {
			DefaultText = DefaultText.replaceFirst(" - ", " – ");
			bindestrich++;
		}
		while (FemaleText.contains(" - ")) {
			FemaleText = FemaleText.replaceFirst(" - ", " – ");
			bindestrich++;
		}

		return new Pair<Long, Long>(bindestrich, 0L);
	}

	public Pair<Long, Long> tagBalanceCheck1() {
		long eckige = 0, runde = 0;

		if (DefaultText.chars().filter(ch -> ch == '(').count() != DefaultText.chars().filter(ch -> ch == ')')
				.count()) {
			runde++;
		}
		if (DefaultText.chars().filter(ch -> ch == '[').count() != DefaultText.chars().filter(ch -> ch == ']')
				.count()) {
			eckige++;
		}

		if (FemaleText.chars().filter(ch -> ch == '(').count() != FemaleText.chars().filter(ch -> ch == ')').count()) {
			runde++;
		}
		if (FemaleText.chars().filter(ch -> ch == '[').count() != FemaleText.chars().filter(ch -> ch == ']').count()) {
			eckige++;
		}

		return new Pair<Long, Long>(runde, eckige);
	}

	public Pair<Long, Long> tagBalanceCheck2() {
		long geschweift = 0, html = 0;

		if (DefaultText.chars().filter(ch -> ch == '{').count() != DefaultText.chars().filter(ch -> ch == '}')
				.count()) {
			geschweift++;
		}
		if (DefaultText.split("<", -1).length - 1 != DefaultText.split(">", -1).length - 1) {
			html++;

		}
		if ((DefaultText.split("<", -1).length - 1) % 2 != 0) {
			html++;

		}
		// Todo unvollständige Tags
		if (DefaultText.matches("<.+?>")) {
			// es gibt öffnenden und schließenden tag
			String pattern = "(<(.+?)>).*?(</(.+?)>)";

			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = r.matcher(DefaultText);
			if (m.find() && !m.group(3).startsWith("/") && !m.group(2).startsWith(m.group(4))) {
				System.out.println("Found value: " + m.group(0));
				html++;
			}
		}

		if (FemaleText.chars().filter(ch -> ch == '{').count() != FemaleText.chars().filter(ch -> ch == '}').count()) {
			geschweift++;
		}
		if (FemaleText.split("<", -1).length - 1 != FemaleText.split(">", -1).length - 1) {
			html++;
			System.out.println(FemaleText);
		}
		if ((FemaleText.split("<", -1).length - 1) % 2 != 0) {
			html++;
		}

		if (FemaleText.matches("<.+?>")) {
			// es gibt öffnenden und schließenden tag
			String pattern = "(<(.+?)>).*?(</(.+?)>)";

			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = r.matcher(FemaleText);
			if (m.find() && !m.group(3).startsWith("/") && !m.group(2).startsWith(m.group(4))) {
				System.out.println("Found value: " + m.group(0));
				html++;
			}
		}

		return new Pair<Long, Long>(geschweift, html);
	}

	public Pair<Long, Long> tagBalanceCheck3() {
		long double_ = 0, single = 0;

		if (DefaultText.chars().filter(ch -> ch == '„').count() != DefaultText.chars().filter(ch -> ch == '“')
				.count()) {
			double_++;
		}
		int index1 = DefaultText.indexOf("„");
		int index2 = DefaultText.indexOf("„", index1 + 1);
		int index3 = DefaultText.indexOf("“");
		if (index2 > 0 && index2 < index3) {
			double_++;
		}

		if (DefaultText.chars().filter(ch -> ch == '‚').count() != DefaultText.chars().filter(ch -> ch == '‘')
				.count()) {
			single++;
		}
		index1 = DefaultText.indexOf("‚");
		index2 = DefaultText.indexOf("‚", index1 + 1);
		index3 = DefaultText.indexOf("‘");
		if (index2 > 0 && index2 < index3) {
			single++;
		}

		if (FemaleText.chars().filter(ch -> ch == '„').count() != FemaleText.chars().filter(ch -> ch == '“').count()) {
			double_++;
		}
		index1 = FemaleText.indexOf("„");
		index2 = FemaleText.indexOf("„", index1 + 1);
		index3 = FemaleText.indexOf("“");
		if (index2 > 0 && index2 < index3) {
			double_++;
		}
		if (FemaleText.chars().filter(ch -> ch == '‚').count() != FemaleText.chars().filter(ch -> ch == '‘').count()) {
			single++;
		}
		index1 = FemaleText.indexOf("‚");
		index2 = FemaleText.indexOf("‚", index1 + 1);
		index3 = FemaleText.indexOf("‘");
		if (index2 > 0 && index2 < index3) {
			single++;
		}

		return new Pair<Long, Long>(double_, single);
	}

	public Pair<Long, Long> unwanted() {
		long hochkomma = 0, single = 0;

		if (DefaultText.contains("'")) {
			hochkomma++;
		}

		if (FemaleText.contains("'")) {
			hochkomma++;
		}

		return new Pair<Long, Long>(hochkomma, single);
	}

	public void DocuHelper() {
		if (DefaultText.contains("<")) {
			// System.out.println(DefaultText);
		}

		// Non Break Space
		if (FemaleText.contains(" ")) {
			System.out.println(FemaleText);
		}

		if (DefaultText.contains(" ")) {
			System.out.println(DefaultText);
		}
	}

	public Set<String> tagViewHelper() {
		Set<String> tagSet = new HashSet<>();
		String pattern = "(\\[.*?\\])";
		Pattern r = Pattern.compile(pattern);

		if (DefaultText.contains("[")) {
			Matcher m = r.matcher(DefaultText);
			while (m.find()) {
				tagSet.add(m.group(1));
			}
		}
		if (FemaleText.contains("[")) {
			Matcher m = r.matcher(FemaleText);
			while (m.find()) {
				tagSet.add(m.group(1));
			}
		}

		return tagSet;
	}

	public void compareTags(Entry lang2) {
		if (DefaultText.chars().filter(ch -> ch == '{').count() != lang2.DefaultText.chars().filter(ch -> ch == '{')
				.count()) {
			System.out.println("A: " + DefaultText);
			System.out.println("B: " + lang2.DefaultText);
		}
		if (DefaultText.chars().filter(ch -> ch == '[').count() != lang2.DefaultText.chars().filter(ch -> ch == '[')
				.count()) {
			System.out.println("A: " + DefaultText);
			System.out.println("B: " + lang2.DefaultText);
		}
		if (DefaultText.chars().filter(ch -> ch == '(').count() != lang2.DefaultText.chars().filter(ch -> ch == '(')
				.count()) {
			System.out.println("A: " + DefaultText);
			System.out.println("B: " + lang2.DefaultText);
		}
		if (DefaultText.chars().filter(ch -> ch == '}').count() != lang2.DefaultText.chars().filter(ch -> ch == '}')
				.count()) {
			System.out.println("A: " + DefaultText);
			System.out.println("B: " + lang2.DefaultText);
		}
		if (DefaultText.chars().filter(ch -> ch == ']').count() != lang2.DefaultText.chars().filter(ch -> ch == ']')
				.count()) {
			System.out.println("A: " + DefaultText);
			System.out.println("B: " + lang2.DefaultText);
		}
		if (DefaultText.chars().filter(ch -> ch == ')').count() != lang2.DefaultText.chars().filter(ch -> ch == ')')
				.count()) {
			System.out.println("A: " + DefaultText);
			System.out.println("B: " + lang2.DefaultText);
		}
		if (DefaultText.chars().filter(ch -> ch == '<').count() != lang2.DefaultText.chars().filter(ch -> ch == '<')
				.count()) {
			System.out.println("A: " + DefaultText);
			System.out.println("B: " + lang2.DefaultText);
		}
		if (DefaultText.chars().filter(ch -> ch == '>').count() != lang2.DefaultText.chars().filter(ch -> ch == '>')
				.count()) {
			System.out.println("A: " + DefaultText);
			System.out.println("B: " + lang2.DefaultText);
		}

		if ((FemaleText.chars().filter(ch -> ch == '{').count() != lang2.FemaleText.chars().filter(ch -> ch == '{')
				.count()) && lang2.FemaleText.length() > 0) {
			System.out.println("A: " + FemaleText);
			System.out.println("B: " + lang2.FemaleText);
		}
		if ((FemaleText.chars().filter(ch -> ch == '[').count() != lang2.FemaleText.chars().filter(ch -> ch == '[')
				.count()) && lang2.FemaleText.length() > 0) {
			System.out.println("A: " + FemaleText);
			System.out.println("B: " + lang2.FemaleText);
		}
		if ((FemaleText.chars().filter(ch -> ch == '(').count() != lang2.FemaleText.chars().filter(ch -> ch == '(')
				.count()) && lang2.FemaleText.length() > 0) {
			System.out.println("A: " + FemaleText);
			System.out.println("B: " + lang2.FemaleText);
		}
		if ((FemaleText.chars().filter(ch -> ch == '}').count() != lang2.FemaleText.chars().filter(ch -> ch == '}')
				.count()) && lang2.FemaleText.length() > 0) {
			System.out.println("A: " + FemaleText);
			System.out.println("B: " + lang2.FemaleText);
		}
		if ((FemaleText.chars().filter(ch -> ch == ']').count() != lang2.FemaleText.chars().filter(ch -> ch == ']')
				.count()) && lang2.FemaleText.length() > 0) {
			System.out.println("A: " + FemaleText);
			System.out.println("B: " + lang2.FemaleText);
		}
		if ((FemaleText.chars().filter(ch -> ch == ')').count() != lang2.FemaleText.chars().filter(ch -> ch == ')')
				.count()) && lang2.FemaleText.length() > 0) {
			System.out.println("A: " + FemaleText);
			System.out.println("B: " + lang2.FemaleText);
		}
		if ((FemaleText.chars().filter(ch -> ch == '<').count() != lang2.FemaleText.chars().filter(ch -> ch == '<')
				.count()) && lang2.FemaleText.length() > 0) {
			System.out.println("A: " + FemaleText);
			System.out.println("B: " + lang2.FemaleText);
		}
		if ((FemaleText.chars().filter(ch -> ch == '>').count() != lang2.FemaleText.chars().filter(ch -> ch == '>')
				.count()) && lang2.FemaleText.length() > 0) {
			System.out.println("A: " + FemaleText);
			System.out.println("B: " + lang2.FemaleText);
		}

	}

	public int compareTags2() {
		String italic = "(<i>)([\\s\\S]+?)(</i>)";
		String bold = "(<b>)([\\s\\S]+?)(</b>)";
		String underline = "(<u>)([\\s\\S]+?)(</u>)";
		String german = "(<g>)([\\s\\S]+?)(</g>)";
		String link = "(<link=\"(glossary|stringtooltip|neutralvalue|gamedata)?://.*>)([\\s\\S]+?)(</link>)";
		String color = "(<#[0-9a-fA-F]{6}>)([\\s\\S]+?)(</color>)";
		String ispeech = "(<ispeech>)([\\s\\S]+?)(</ispeech>)";
		String sprite = "(<sprite=.* tint=[0-9]>)";

		String i1 = "<i>";
		String i2 = "</i>";
		String b1 = "<b>";
		String b2 = "</b>";
		String u1 = "<u>";
		String u2 = "</u>";
		String g1 = "<g>";
		String g2 = "</g>";
		String l1 = "<link=";
		String l2 = "</link>";
		String c1 = "<#";
		String c2 = "</color>";
		String p1 = "<ispeech>";
		String p2 = "</ispeech>";
		String s1 = "<sprite>";
		String s2 = "";

		String[] tagList = new String[] { italic, bold, underline, german, link, color, ispeech, sprite };
		String[] tagListA = new String[] { i1, b1, u1, g1, l1, c1, p1, s1 };
		String[] tagListE = new String[] { i2, b2, u2, g2, l2, c2, p2, s2 };

		for (int i = 0; i < tagList.length; i++) {
			// Part 1 opening tag hat closing tag
			int start = 0;
			while (start >= 0 && start < DefaultText.length()
					&& (DefaultText.subSequence(start, DefaultText.length()).toString().contains(tagListA[i])
							|| DefaultText.subSequence(start, DefaultText.length()).toString().contains(tagListE[i]))) {
				if (DefaultText.substring(start).contains(tagListA[i])
						&& !DefaultText.substring(start).contains(tagListE[i])
						|| !DefaultText.substring(start).contains(tagListA[i])
								&& DefaultText.substring(start).contains(tagListE[i]) && i < tagList.length - 1) {
					// System.out.println(DefaultText);
				}
				start = Math.max(DefaultText.indexOf(tagListA[i], start), DefaultText.indexOf(tagListE[i], start)) + 1;
			}
			start = 0;
			while (start >= 0 && start < FemaleText.length()
					&& (FemaleText.subSequence(start, FemaleText.length()).toString().contains(tagListA[i])
							|| FemaleText.subSequence(start, FemaleText.length()).toString().contains(tagListE[i]))) {
				if (FemaleText.substring(start).contains(tagListA[i])
						&& !FemaleText.substring(start).contains(tagListE[i])
						|| !FemaleText.substring(start).contains(tagListA[i])
								&& FemaleText.substring(start).contains(tagListE[i]) && i < tagList.length - 1) {
					System.out.println(FemaleText);
				}
				start = Math.max(FemaleText.indexOf(tagListA[i], start), FemaleText.indexOf(tagListE[i], start)) + 1;
			}

			if (DefaultText.contains(tagListA[i]) && DefaultText.contains(tagListE[i])) {
				Pattern r = Pattern.compile(tagList[i]);
				Matcher m = r.matcher(DefaultText);
				// Part 2, match it
				if (m.find() && m.group(1).length() > 0 && (i < tagList.length - 1 && m.group(2).length() > 0)
						&& (i < tagList.length - 1 && m.group(3).length() > 0)) {
					for (int j = i; j < tagList.length + i; j++) {
						if (m.group(2).contains(tagListA[j % tagList.length])
								&& !m.group(2).contains(tagListE[j % tagList.length])
								|| !m.group(2).contains(tagListA[j % tagList.length])
										&& m.group(2).contains(tagListE[j % tagList.length])
										&& j < tagList.length - 1) {
							System.out.println(DefaultText);
						}
						if (m.group(2).contains(tagListA[j % tagList.length])
								&& m.group(2).contains(tagListE[j % tagList.length])) {
							Pattern rr = Pattern.compile(tagList[j % tagList.length]);
							Matcher mm = rr.matcher(m.group(2));
							mm.find();
							if (mm.group(1).length() > 0 && (j < tagList.length + i - 1 && mm.group(2).length() > 0)
									&& (j < tagList.length + i - 1 && mm.group(3).length() > 0)) {
								// ok
							} else {
								System.out.println(DefaultText);
							}
						}
					}
				} else {
					System.out.println(DefaultText);
				}
			}

			if (FemaleText.contains(tagListA[i]) && FemaleText.contains(tagListE[i])) {
				Pattern r = Pattern.compile(tagList[i]);
				Matcher m = r.matcher(FemaleText);
				// Part 2, match it
				if (m.find() && m.group(1).length() > 0 && (i < tagList.length - 1 && m.group(2).length() > 0)
						&& (i < tagList.length - 1 && m.group(3).length() > 0)) {
					for (int j = i; j < tagList.length + i; j++) {
						if (m.group(2).contains(tagListA[j % tagList.length])
								&& !m.group(2).contains(tagListE[j % tagList.length])
								|| !m.group(2).contains(tagListA[j % tagList.length])
										&& m.group(2).contains(tagListE[j % tagList.length])
										&& j < tagList.length - 1) {
							System.out.println(FemaleText);
						}
						if (m.group(2).contains(tagListA[j % tagList.length])
								&& m.group(2).contains(tagListE[j % tagList.length])) {
							Pattern rr = Pattern.compile(tagList[j % tagList.length]);
							Matcher mm = rr.matcher(m.group(2));
							mm.find();
							if (mm.group(1).length() > 0 && (j < tagList.length + i - 1 && mm.group(2).length() > 0)
									&& (j < tagList.length + i - 1 && mm.group(3).length() > 0)) {
								// ok
							} else {
								System.out.println(FemaleText);
							}
						}
					}
				} else {
					System.out.println(FemaleText);
				}
			}
		}

		return 1;
	}

	public void compareIsTranslated(Entry other) {
		if(DefaultText == null && other.DefaultText != null) {
			System.out.println(other.DefaultText);
		}
		if(FemaleText == null && other.FemaleText != null) {
			System.out.println(other.FemaleText);
		}
	}
	
	public Set<Character> counter() {
		Set<Character> target = new TreeSet<>();
		char[] chars = DefaultText.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			target.add(chars[i]);
		}
		chars = FemaleText.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			target.add(chars[i]);
		}
		return target;
	}

	public Map<String, Integer> wordCount() {
		Map<String, Integer> target = new TreeMap<>();
		String[] words = DefaultText.split("[\\., !?…\\-–„“‚‘\\n\\t()]");

		for (String word : words) {
			target.merge(word, 1, (x, y) -> x + y);
		}
		words = FemaleText.split("[\\., !?…\\-–„“‚‘\\n\\t()]");
		for (String word : words) {
			target.merge(word, 1, (x, y) -> x + y);
		}
		return target;
	}
	
	// 12 * 3 = 36 per language
	final static String[] replacementWords = new String[] { 
			// EN
			// constitution
			"(?<!xg>)(Fit)", "(?<!xg>)(Hardy)", "(?<!xg>)(Robust)",
			"(?<!xg>)(Sicken)(\\b|ed|ing)", "(?<!xg>)(Weaken)(\\b|s|ed|ing)", "(?<!xg>)(Enfeebl)(\\b|ed)",
			// resolve
			"(?<!xg>)(Steadfast)", "(?<!xg>)(Resolute)", "(?<!xg>)(Courageous)",
			"(?<!xg>)(Shaken)(\\b|s|ed|ing)", "(?<!xg>)(Frighten)(\\b|s|ed|ing)", "(?<!xg>)(Terrif)(\\b|ies|ied|ying|y)",
			// dexterity
			"(?<!xg>)(Quick\\b)", "(?<!xg>)(Nimble)", "(?<!xg>)(Swift)",
			"(?<!xg>)(Hobbl)(\\b|ed|es|e|ing)", "(?<!xg>)(Immobiliz)(\\b|ed|e|ing)", "(?<!xg>)(Paralyz)(\\b|ed|es|e|ing|ation)",
			// intellect
			"(?<!xg>)(Smart)", "(?<!xg>)(Acute)", "(?<!xg>)(Brilliant)",
			"(?<!xg>)(Confus)(\\b|ed|es|e|ing)", "(?<!xg>)(Charm)(\\b|ed|ing)", "(?<!xg>)(Dominat)(\\b|ed|es|e|ing)",
			// might
			"(?<!xg>)(Strong)", "(?<!xg>)(Tenacious)", "(?<!xg>)(Energized)",
			"(?<!xg>)(Stagger)(\\b|ed|ing)", "(?<!xg>)(Daz)(\\b|ed|es|e|ing)", "(?<!xg>)(Stun)(\\b|s|ned|ning)",
			// perception
			"(?<!xg>)(Insightful)", "(?<!xg>)(Aware)", "(?<!xg>)(Intuitive)",
			"(?<!xg>)(Distract)(\\b|ed|s|ing)", "(?<!xg>)(Disorient)(\\b|ed|ing)", "(?<!xg>)(Blind(?!\\)))(\\b|ed|s|ing|ness)",
			
			
			// DE_patch
			// Verfassung
			"(?<!xg>)(Fit(te)?)", "(?<!xg>)(Zäh(e)?\\b)", "(?<!xg>)(Robust(e)?)",
			"(?<!xg>)([Ee]rkrank)(te|t|en)", "(?<!xg>)(\\b|Ge)(schwächt(e)?)", "(?<!xg>)(Entkräftet)(e|\\b)",
			// Entschlossenheit
			"(?<!xg>)(Standhaft(e)?)", "(?<!xg>)(Resolut(e)?)", "(?<!xg>)(Mutig(e)?)",
			"(?<!xg>)(Geschockt)(e|\\b)", "(?<!xg>)(Verängstig)(te|t|en)", "(?<!xg>)([Ee]rschütter)(ter|t|n)",
			// Gewandheit
			"(?<!xg>)(Schnelle?n?)", "(?<!xg>)(Flink(e)?)", "(?<!xg>)(Schwungvoll(e)?)",
			"(?<!xg>)(Humpeln)(\\b|d\\b|de)", "(?<!xg>)(Bewegungsunfähig)(e|\\b)", "(?<!xg>)(G?e?[Ll]ähmt)(er|e|\\b)",
			// Intellekt
			"(?<!xg>)(Klug(e)?)", "(?<!xg>)(Scharfsinnig(e)?)", "(?<!xg>)(Brillant(e)?)",
			"(?<!xg>)(Verwirrt)(e|\\b)", "(?<!xg>)(Bezaubert)(er|e|\\b)", "(?<!xg>)(Beherrscht)(e|\\b)",
			// Macht
			"(?<!xg>)(Stark(e)?)\\b", "(?<!xg>)(Hartnäckig(e)?)", "(?<!xg>)(Munter(e)?)",
			"(?<!xg>)(Taumeln)(de|d|\\b)", "(?<!xg>)(Benommen)(e|heit|\\b)", "(?<!xg>)(Betäub)(en|te|t)",
			// Wahrnehmung
			"(?<!xg>)(Einsichtig(e)?)", "(?<!xg>)(Aufmerksam(e)?)", "(?<!xg>)(Intuitiv(e)?)",
			"(?<!xg>)(Abgelenkt)(er|e|\\b)", "(?<!xg>)(Desorientier)(te|t|render|\\b)", "(?<!xg>)(G?e?blende|[Ee]rblinde)(te|t|n(?!d))",
			
			
			// FR
			// constitution
			"(?<!xg>)(Dynamisme)", "(?<!xg>)(Vigueur)", "(?<!xg>)(Robustesse)",
			"(?<!xg>)(Intoxication)(\\b)", "(?<!xg>)(Affaiblissement)(\\b)", "(?<!xg>)(Diminution)(\\b)",
			
			// Résolution
			"(?<!xg>)(Détermination)", "(?<!xg>)(Opiniâtreté)", "(?<!xg>)(Courage)",
			"(?<!xg>)(Frisson)(\\b)", "(?<!xg>)(Effroi)(\\b)", "(?<!xg>)(Terreur)(\\b)",
						
			// Dextérité
			"(?<!xg>)(Célérité)", "(?<!xg>)(Agilité)", "(?<!xg>)(Vélocité)",
			"(?<!xg>)(Entrave)(\\b)", "(?<!xg>)(Immobilisation)(\\b)", "(?<!xg>)(Paralysie)(\\b)",
			
			// Intelligence
			"(?<!xg>)(Sagacité)", "(?<!xg>)(Astuce)", "(?<!xg>)(Perspicacité)",
			"(?<!xg>)(Confusion)(\\b)", "(?<!xg>)(Charme)(\\b)", "(?<!xg>)(Domination)(\\b)",
			
			// Puissance
			"(?<!xg>)(Force)", "(?<!xg>)(Ténacité)", "(?<!xg>)(Tonus)",
			"(?<!xg>)(Stupéfaction)(\\b)", "(?<!xg>)(Étourdissement)(\\b)", "(?<!xg>)(Assommement)(\\b)",
			
			//Perception
			"(?<!xg>)(Subtilité)", "(?<!xg>)(Conscience)", "(?<!xg>)(Intuition)",
			"(?<!xg>)(Distraction)(\\b)", "(?<!xg>)(Désorientation)(\\b)", "(?<!xg>)(Aveuglement)(\\b)",
			
			
			// IT
			// Costituzione
			"(?<!xg>)(In forma)", "(?<!xg>)(Poderoso)", "(?<!xg>)(Robusto)",
			"(?<!xg>)(Nausea)(ndo|to)", "(?<!xg>)(Indebolit)(i|o)", "(?<!xg>)(Debilitato)(\\b)",
			
			//Risolutezza
			"(?<!xg>)(Nerbo)", "(?<!xg>)(Risoluto)", "(?<!xg>)(Coraggioso)",
			"(?<!xg>)(Scoss)(o|i)", "(?<!xg>)(Spaventa)(ndo|to)", "(?<!xg>)(Terrorizza)(ndo|to)",
			
			// Destrezza
			"(?<!xg>)(Veloce)", "(?<!xg>)(Lesto)", "(?<!xg>)(Rapido)",
			"(?<!xg>)(Azzoppato)(\\b)", "(?<!xg>)(Immobilizza)(ndo|to)", "(?<!xg>)(Paralizza)(ndo|to)",
			
			// Acume
			"(?<!xg>)(Arguto)", "(?<!xg>)(Perspicace)", "(?<!xg>)(Brillante)",
			"(?<!xg>)(Confus)(ione|o)", "(?<!xg>)(Charme|Incantato)(\\b)", "(?<!xg>)(Dominato)(\\b)",
			
			// Vigore
			"(?<!xg>)(Forte)", "(?<!xg>)(Tenace)", "(?<!xg>)(Energico)",
			"(?<!xg>)(Sorpreso)(\\b)", "(?<!xg>)(Disorienta)(mento|to)", "(?<!xg>)(Stord)(endo|ito|imento)",
			
			// Percezione
			"(?<!xg>)(Profondo)", "(?<!xg>)(Consapevole)", "(?<!xg>)(Intuitivo)",
			"(?<!xg>)(Distratto)(\\b)", "(?<!xg>)(Spaesa)(to|ti)", "(?<!xg>)(Cecità|Accecato)(\\b)",
			
			
			// ES
			// Constitución
			"(?<!xg>)(En forma)", "(?<!xg>)(Resistente)", "(?<!xg>)(Robusto)",
			"(?<!xg>)(Enfermo)(\\b)", "(?<!xg>)(Debilitado)(\\b)", "(?<!xg>)(Desalentado)(\\b)",
			
			// Determinación
			"(?<!xg>)(Resuelto)", "(?<!xg>)(Firme)", "(?<!xg>)(Valiente)",
			"(?<!xg>)(Alterado)(\\b)", "(?<!xg>)(Asustado)(\\b)", "(?<!xg>)(Aterrorizado)(\\b)",
			
			// Destreza
			"(?<!xg>)(Rápido)", "(?<!xg>)(Sagaz)", "(?<!xg>)(Veloz)",
			"(?<!xg>)(Atrapado)(\\b)", "(?<!xg>)(Inmovilizado)(\\b)", "(?<!xg>)(Paralizado)(\\b)",
			
			// Intelecto
			"(?<!xg>)(Inteligente)", "(?<!xg>)(Agudo)", "(?<!xg>)(Brillante)",
			"(?<!xg>)(Confuso)(\\b)", "(?<!xg>)(Encantado)(\\b)", "(?<!xg>)(Dominado)(\\b)",
			
			// Fuerza
			"(?<!xg>)(Fuerte)", "(?<!xg>)(Tenaz)", "(?<!xg>)(Enérgico)",
			"(?<!xg>)(Tambaleante)(\\b)", "(?<!xg>)(Desorientado)(\\b)", "(?<!xg>)(Aturdido)(\\b)",
			
			// Percepción
			"(?<!xg>)(Perspicaz)", "(?<!xg>)(Consciente)", "(?<!xg>)(Intuitivo)",
			"(?<!xg>)(Distraído)(\\b)", "(?<!xg>)(Desorientado)(\\b)", "(?<!xg>)(Cegado)(\\b)",
			
			
			// PT
			// Constituição
			"(?<!xg>)(Em Forma)", "(?<!xg>)(Resistente)", "(?<!xg>)(Robusto)",
			"(?<!xg>)(Adoecido)(\\b)", "(?<!xg>)(Enfraquecido)(\\b)", "(?<!xg>)(Debilitado)(\\b)",
			
			// Determinação
			"(?<!xg>)(Firme)", "(?<!xg>)(Resoluto)", "(?<!xg>)(Corajoso)",
			"(?<!xg>)(Abalado)(\\b)", "(?<!xg>)(Amedrontado)(\\b)", "(?<!xg>)(Aterrorizado)(\\b)",
			
			// Destreza
			"(?<!xg>)(Rápido)", "(?<!xg>)(Ágil)", "(?<!xg>)(Veloz)",
			"(?<!xg>)(Mancando)(\\b)", "(?<!xg>)(Imobilizado)(\\b)", "(?<!xg>)(Paralisado)(\\b)",
			
			// Intelecto
			"(?<!xg>)(Esperto)", "(?<!xg>)(Sagaz)", "(?<!xg>)(Brilhante)",
			"(?<!xg>)(Confuso)(\\b)", "(?<!xg>)(Encantado)(\\b)", "(?<!xg>)(Dominado)(\\b)",
			
			// Força
			"(?<!xg>)(Forte)", "(?<!xg>)(Tenaz)", "(?<!xg>)(Energizado)",
			"(?<!xg>)(Desconcertado)(\\b)", "(?<!xg>)(Ofuscado)(\\b)", "(?<!xg>)(Atordoad)(\\b)",
			
			// Percepção
			"(?<!xg>)(Perspicaz)", "(?<!xg>)(Ciente)", "(?<!xg>)(Intuitivo)",
			"(?<!xg>)(Distraído)(\\b)", "(?<!xg>)(Desorientado)(\\b)", "(?<!xg>)(Cego)(\\b)",
			
			
		
			// PL inspiracje/przypadlosci
            // kondycji
            "(?<!xg>)([sS]prawnoś(ć|ci))", "(?<!xg>)([dD]zielnoś(ć|ci))", "(?<!xg>)([kK]rzepkoś(ć|ci))",
            "(?<!xg>)([mM]dłości)(ami|\\b)", "(?<!xg>)([oO]słabi)(eni(a|em|e)|ając go|ającej)", "(?<!xg>)([wW]ycieńcz)(enia|enie|ona)",
           
            // stanowczości
            "(?<!xg>)(wytrwałoś(ć|ci))", "(?<!xg>)(niezłomnoś(ć|ci))", "(?<!xg>)(odwagi)",
            "(?<!xg>)(roztrzęsieni)(e|a)", "(?<!xg>)(przestraszeni)(a|e)", "(?<!xg>)(przerażeni)(e|a)",
           
            // zręczności
            "(?<!xg>)(szybkoś(ć|ci))(?! ataku)", "(?<!xg>)(lotnoś(c|ci))", "(?<!xg>)(zwinnoś(ć|ci))",
            "(?<!xg>)(okulawieni)(e|a)", "(?<!xg>)(unieruch)(omieni(\\b|e|a)|omion(e|y)|amiając|amia(\\b|ją))", "(?<!xg>)([sS]?parali)(ż|żowany|żując|żuje|żują|żowanie)",
           
            // intelektu
            "(?<!xg>)(mądroś(ć|ci))", "(?<!xg>)(bystroś(ć|ci))", "(?<!xg>)(geniusz(u))",
            "(?<!xg>)(zamęt)(u|em)", "(?<!xg>)(zaurocz)(enie|yć|eni|enia|ona)", "(?<!xg>)(z?domin)(owan(ie|ia|i|y)|acji|uje|any|acja)",
           
            // siły
            "(?!=inspiracj|przypadłość|przypadłości) (?<!xg>)(moc\\b)", "(?<!xg>)(zawziętoś(ci|ć))", "(?<!xg>)(pobudzeni(e|a))",
            "(?<!xg>)(wytrącen[aei])( z równowagi)", "(?<!xg>)(oszołomienie)(\\b)", "(?<!xg>)(ogłuszenie)(\\b)",
           
            // percepcji
            "(?<!xg>)(wnikliwość)", "(?<!xg>)(świadomość)", "(?<!xg>)(intuicja)",
            "(?<!xg>)(rozkojarzenie)(\\b)", "(?<!xg>)(dezorientacja)(\\b)", "(?<!xg>)(oślepienie)(\\b)",
			
			
			
			// Ru Fix
			// Телосложение
			"(?<!xg>)(подготовка)", "(?<!xg>)(крепость)", "(?<!xg>)(непоколебимость)",
			"(?<!xg>)(недомогание)(\\b)", "(?<!xg>)(ослабление)(\\b)", "(?<!xg>)(немощь)(\\b)",
			
			// Решительность
			"(?<!xg>)(устремленность)", "(?<!xg>)(решительность)", "(?<!xg>)(Отвага)",
			"(?<!xg>)(встряска)(\\b)", "(?<!xg>)(испуг)(\\b)", "(?<!xg>)(ужас)(\\b)",
			
			// Ловкость
			"(?<!xg>)(быстрота)", "(?<!xg>)(проворство)", "(?<!xg>)(стремительность)",
			"(?<!xg>)(хромота)(\\b)", "(?<!xg>)(обездвиженность)(\\b)", "(?<!xg>)(паралич)(\\b)",
			
			// Интеллект
			"(?<!xg>)(ум)", "(?<!xg>)(сообразительность)", "(?<!xg>)(мудрость)",
			"(?<!xg>)(путаница)(\\b)", "(?<!xg>)(заворожен)(ие|ые)", "(?<!xg>)(доминирование)(\\b)",

			// Сила
			"(?<!xg>)(сила)", "(?<!xg>)(цепкость)", "(?<!xg>)(энергия)",
			"(?<!xg>)(потрясение)(\\b)", "(?<!xg>)(Ошеломлен)(ные|ие)", "(?<!xg>)(Оглушение)(\\b)",

			// Восприятие
			"(?<!xg>)(проницательность)", "(?<!xg>)(Осведомленность)", "(?<!xg>)(интуиция)",
			"(?<!xg>)(Отвлечение)(\\b)", "(?<!xg>)(Дезориентированные)(\\b)", "(?<!xg>)([Оо]слеплен)(ие|ых|ые)",

			
			// ZH, Gott versteh ich nicht.
			// 体质
			"(?<!xg>)(健康)", "(?<!xg>)(健壮)", "(?<!xg>)(身强体壮)",
			"(?<!xg>)(恶心)(\\b)", "(?<!xg>)(虚弱)(\\b)", "(?<!xg>)(无力)(\\b)",
			
			// 决心
			"(?<!xg>)(坚定)", "(?<!xg>)(坚决)", "(?<!xg>)(勇往直前)",
			"(?<!xg>)(动摇)(\\b)", "(?<!xg>)(惊慌)(\\b)", "(?<!xg>)(恐惧)(\\b)",
			
			// 敏捷
			"(?<!xg>)(快速)", "(?<!xg>)(灵巧)", "(?<!xg>)(身轻如燕)",
			"(?<!xg>)(蹒跚)(\\b)", "(?<!xg>)(定身)(\\b)", "(?<!xg>)(麻痹)(\\b)",
			
			// 智力
			"(?<!xg>)(聪慧)", "(?<!xg>)(聪慧)", "(?<!xg>)(才智过人)",
			"(?<!xg>)(困惑)(\\b)", "(?<!xg>)(魅惑)(\\b)", "(?<!xg>)(受控)(\\b)",
			
			// 力量
			"(?<!xg>)(强力)", "(?<!xg>)(强硬)", "(?<!xg>)(力大无穷)",
			"(?<!xg>)(踉跄)(\\b)", "(?<!xg>)(晕眩)(\\b)", "(?<!xg>)(震慑)(\\b)",
			
			// 感知
			"(?<!xg>)(敏锐)", "(?<!xg>)(机敏)", "(?<!xg>)(直觉超群)",
			"(?<!xg>)(烦乱)(\\b)", "(?<!xg>)(迷离)(\\b)", "(?<!xg>)(目盲)(\\b)"
	}; 
	 
	final static String[] targetWords = new String[] {
	"<link=\"gamedata://2370256c-67fe-4481-bfae-9081b2dc10d3\"><#f7b733>$1 <sprite=\"Inline\" name=\"attribute_constitution\" tint=1>¹</color></link>",
		"<link=\"gamedata://fd4d0c5c-f525-4010-99dc-f30a59b2f729\"><#f7b733>$1 <sprite=\"Inline\" name=\"attribute_constitution\" tint=1>²</color></link>",
		"<link=\"gamedata://86f9c246-c37c-46a6-8838-7dc36c18e08d\"><#f7b733>$1 <sprite=\"Inline\" name=\"attribute_constitution\" tint=1>³</color></link>",
		                                                                                                                                                                                                                                                                                                                           
		"<link=\"gamedata://1e99c141-5288-4bf0-abc2-65cc78638745\"><#f7b733>$1$2 <sprite=\"Inline\" name=\"attribute_constitution\" tint=1>₁</color></link>",
		"<link=\"gamedata://a6368675-47ed-460d-be4f-2593f24ad126\"><#f7b733>$1$2 <sprite=\"Inline\" name=\"attribute_constitution\" tint=1>₂</color></link>",
		"<link=\"gamedata://fe0997e7-422f-4d1d-b9f6-241946fbd1f1\"><#f7b733>$1$2 <sprite=\"Inline\" name=\"attribute_constitution\" tint=1>₃</color></link>",
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
		"<link=\"gamedata://92d1a280-2f8e-4a6d-87a9-6599d41d65e4\"><#30d3d5>$1 <sprite=\"Inline\" name=\"attribute_resolve\" tint=1>¹</color></link>",
		"<link=\"gamedata://51e1cbfc-506b-4e7c-880e-b89312a4b4c3\"><#30d3d5>$1 <sprite=\"Inline\" name=\"attribute_resolve\" tint=1>²</color></link>",     
		"<link=\"gamedata://dc62af47-239f-4ee4-8d91-a5e8b3b97564\"><#30d3d5>$1 <sprite=\"Inline\" name=\"attribute_resolve\" tint=1>³</color></link>",     
		                                                                                                                                                                                                                                                                                                                                      
		"<link=\"gamedata://8f4832b6-49ef-4f40-9d64-20eb50fd9bb6\"><#30d3d5>$1$2 <sprite=\"Inline\" name=\"attribute_resolve\" tint=1>₁</color></link>",   
		"<link=\"gamedata://d9e00789-14d3-46f1-be3e-4c1379148d24\"><#30d3d5>$1$2 <sprite=\"Inline\" name=\"attribute_resolve\" tint=1>₂</color></link>",  
		"<link=\"gamedata://0d89a32d-c883-4a18-9fbd-4ff58f1fd0de\"><#30d3d5>$1$2 <sprite=\"Inline\" name=\"attribute_resolve\" tint=1>₃</color></link>",  
	                                                                                                                                                                    
		"<link=\"gamedata://a0b4a490-2c99-456e-b6fb-131353133fba\"><#72da26>$1 <sprite=\"Inline\" name=\"attribute_dexterity\" tint=1>¹</color></link>",  
		"<link=\"gamedata://0451672b-af45-4b44-96b4-ff2c1b6aa1d3\"><#72da26>$1 <sprite=\"Inline\" name=\"attribute_dexterity\" tint=1>²</color></link>",  
		"<link=\"gamedata://1418c0ae-790d-4aef-9ca6-00238012c84e\"><#72da26>$1 <sprite=\"Inline\" name=\"attribute_dexterity\" tint=1>³</color></link>",  
		                                                                                                                                                                                                                                                                                                                                                
		"<link=\"gamedata://c1d82f88-4f54-4279-8e72-43104bf754a2\"><#72da26>$1$2 <sprite=\"Inline\" name=\"attribute_dexterity\" tint=1>₁</color></link>",
		"<link=\"gamedata://a31fdd6c-5f4f-404d-92f5-5d7d8c166f2c\"><#72da26>$1$2 <sprite=\"Inline\" name=\"attribute_dexterity\" tint=1>₂</color></link>",
		"<link=\"gamedata://72de641d-d2f1-47ca-85f2-6ec3e41140bd\"><#72da26>$1$2 <sprite=\"Inline\" name=\"attribute_dexterity\" tint=1>₃</color></link>",
		                                                                                                                                                                                                                                                                                                                                                                      
		"<link=\"gamedata://0435c12c-8049-4f50-b234-ed3cca340696\"><#00a4ff>$1 <sprite=\"Inline\" name=\"attribute_intellect\" tint=1>¹</color></link>", 
		"<link=\"gamedata://c4ada862-ee17-4c10-8967-734e08daa4aa\"><#00a4ff>$1 <sprite=\"Inline\" name=\"attribute_intellect\" tint=1>²</color></link>",  
		"<link=\"gamedata://dee61b5a-6313-4406-ab83-4fb72b37cea2\"><#00a4ff>$1 <sprite=\"Inline\" name=\"attribute_intellect\" tint=1>³</color></link>",  
		                                                                                                                                                                                                                                                                                                                                                         
		"<link=\"gamedata://917ea7e5-133c-4163-93c6-55c7d55420a4\"><#00a4ff>$1$2 <sprite=\"Inline\" name=\"attribute_intellect\" tint=1>₁</color></link>",
		"<link=\"gamedata://f4a09726-a62d-49a2-8cc6-c3713676c833\"><#00a4ff>$1$2 <sprite=\"Inline\" name=\"attribute_intellect\" tint=1>₂</color></link>",
		"<link=\"gamedata://9e773415-4d95-4be6-ab87-325d9815e0d7\"><#00a4ff>$1$2 <sprite=\"Inline\" name=\"attribute_intellect\" tint=1>₃</color></link>",
	                                                                                                                                                                            
		"<link=\"gamedata://79b41b80-d3c4-4176-a44b-69490efa41a8\"><#ff4800>$1 <sprite=\"Inline\" name=\"attribute_might\" tint=1>¹</color></link>",      
		"<link=\"gamedata://93eb382a-dc8e-469b-a116-4686c73e1eb5\"><#ff4800>$1 <sprite=\"Inline\" name=\"attribute_might\" tint=1>²</color></link>",      
		"<link=\"gamedata://c56f70b5-b611-4dee-b80f-335033b98e2f\"><#ff4800>$1 <sprite=\"Inline\" name=\"attribute_might\" tint=1>³</color></link>",      
	                                                                                                                                                                
		"<link=\"gamedata://67f6a58b-658e-4c97-b0a2-5d53067dcbd8\"><#ff4800>$1$2 <sprite=\"Inline\" name=\"attribute_might\" tint=1>₁</color></link>",    
		"<link=\"gamedata://3a790bc4-40a9-4d9f-8da8-a76f3cb6c5e1\"><#ff4800>$1$2 <sprite=\"Inline\" name=\"attribute_might\" tint=1>₂</color></link>",    
		"<link=\"gamedata://fc2d1039-c512-484a-81e8-a843ba03df4e\"><#ff4800>$1$2 <sprite=\"Inline\" name=\"attribute_might\" tint=1>₃</color></link>",    
		                                                                                                                                                                                                                                                                                                                                                                        
		"<link=\"gamedata://412f8f5e-d257-4c52-ac66-0e49fd603ef2\"><#ca58ff>$1 <sprite=\"Inline\" name=\"attribute_perception\" tint=1>¹</color></link>", 
		"<link=\"gamedata://a64e1484-5867-4112-843c-06da3be5399b\"><#ca58ff>$1 <sprite=\"Inline\" name=\"attribute_perception\" tint=1>²</color></link>", 
		"<link=\"gamedata://c2fb23c5-9907-4654-b62e-77adbc37c9bc\"><#ca58ff>$1 <sprite=\"Inline\" name=\"attribute_perception\" tint=1>³</color></link>", 
		                                                                                                                                                                                                                                                                                                                                                          
		"<link=\"gamedata://919bddc8-1e05-412b-bb5c-8272545b28f0\"><#ca58ff>$1$2 <sprite=\"Inline\" name=\"attribute_perception\" tint=1>₁</color></link>",
		"<link=\"gamedata://1021cac0-2d10-4f0d-93e3-e1b0ce24f9fd\"><#ca58ff>$1$2 <sprite=\"Inline\" name=\"attribute_perception\" tint=1>₂</color></link>",
		"<link=\"gamedata://76a5e15a-e75c-4853-889b-2e9229d46444\"><#ca58ff>$1$2 <sprite=\"Inline\" name=\"attribute_perception\" tint=1>₃</color></link>"
	};
	

	public Entry replaceAfflictionWithColor(String targetLanguage) {
		Entry result = new Entry();
		result.ID = ID;
		result.DefaultText = DefaultText;
		result.FemaleText = FemaleText;
				
		int i = 0;
		switch (targetLanguage) {
			case "en":
				i = 36 * 0;
				break;
			case "de_patch":
				i = 36 * 1;
				break;
			case "fr":
				i = 36 * 2;
				break;
			case "it":
				i = 36 * 3;
				break;
			case "es":
				i = 36 * 4;
				break;
			case "pt":
				i = 36 * 5;
				break;
			case "pl":
				i = 36 * 6;
				break;
			case "ru":
				i = 36 * 7;
				break;
			case "zh":
				i = 36 * 8;
				break;
		}
		
		for(int j = 0; j < 36; j++) {
			result.DefaultText = result.DefaultText.replaceAll(replacementWords[i + j], targetWords[j]);
			result.FemaleText = result.FemaleText.replaceAll(replacementWords[i + j], targetWords[j]);
		}
		if(result.DefaultText.equals(DefaultText) && result.FemaleText.equals(FemaleText)) {
			return null;
		} else {
			return result;
		}
	}

	final static String[] afflictionsReplacementWords = new String[] {
			// EN
			"(Constitution Inspirations?)",
			"(Constitution Afflictions?)",
			"(Resolve Inspirations?)",
			"(Resolve Afflictions?)",
			"(Dexterity Inspirations?)",
			"(Dexterity Afflictions?)",
			"(Intellect Inspirations?)",
			"(Intellect Afflictions?)",
			"(Might Inspirations?)",
			"(Might Afflictions?)",
			"(Perception Inspirations?)",
			"(Perception Afflictions?)",
			
			// de_patch    // Watch out for false positive  Meta: Geisteswirkungen, Körperwirkungen, Geistesinspiration
			"(Verfassungsinspiration(en)?)",
			"(Verfassungs-|Verfassungswirkung(en)?)",
			"(Entschlossenheitsinspiration(en)?)",
			"(Entschlossenheits-|Entschlossenheitswirkung(en)?)",
			"(Gewandtheitsinspiration(en)?)",
			"(Gewandtheits-|Gewandtheitswirkung(en)?)",
			"(Intellektinspiration(en)?)",
			"(Intellekt-|Intellektwirkung(en)?)",
			"(Machtinspiration(en)?)",
			"(Macht-|Machtwirkung(en)?)", 
			"(Wahrnehmungsinspiration(en)?)",
			"(Wahrnehmungs-|Wahrnehmungswirkung(en)?)"
	};
            
            
	public Entry replaceGenericAfflictionsWithIcon(String targetLanguage) {
		Entry result = new Entry();
		result.ID = this.ID;
		result.DefaultText = DefaultText;
		result.FemaleText = FemaleText;
		
		String[] targetWords = new String[] {
				"<link=\"glossary://GlossaryEntry_Inspirations_Constitution\"><#f7b733>$1 <sprite=\"Inline\" name=\"attribute_constitution\" tint=1></color></link>",
	            "<link=\"glossary://GlossaryEntry_Afflictions_Constitution\"><#f7b733>$1 <sprite=\"Inline\" name=\"attribute_constitution\" tint=1></color></link>",
	            "<link=\"glossary://GlossaryEntry_Inspirations_Resolve\"><#30d3d5>$1 <sprite=\"Inline\" name=\"attribute_resolve\" tint=1></color></link>",
	            "<link=\"glossary://GlossaryEntry_Afflictions_Resolve\"><#30d3d5>$1 <sprite=\"Inline\" name=\"attribute_resolve\" tint=1></color></link>",
	            "<link=\"glossary://GlossaryEntry_Inspirations_Dexterity\"><#72da26>$1 <sprite=\"Inline\" name=\"attribute_dexterity\" tint=1></color></link>",            
	            "<link=\"glossary://GlossaryEntry_Afflictions_Dexterity\"><#72da26>$1 <sprite=\"Inline\" name=\"attribute_dexterity\" tint=1></color></link>",
	            "<link=\"glossary://GlossaryEntry_Inspirations_Intellect\"><#00a4ff>$1 <sprite=\"Inline\" name=\"attribute_intellect\" tint=1></color></link>",
	            "<link=\"glossary://GlossaryEntry_Afflictions_Intellect\"><#00a4ff>$1 <sprite=\"Inline\" name=\"attribute_intellect\" tint=1></color></link>",
	            "<link=\"glossary://GlossaryEntry_Inspirations_Might\"><#ff4800>$1 <sprite=\"Inline\" name=\"attribute_might\" tint=1></color></link>",
	            "<link=\"glossary://GlossaryEntry_Afflictions_Might\"><#ff4800>$1 <sprite=\"Inline\" name=\"attribute_might\" tint=1></color></link>",
	            "<link=\"glossary://GlossaryEntry_Inspirations_Perception\"><#ca58ff>$1 <sprite=\"Inline\" name=\"attribute_perception\" tint=1></color></link>",
	            "<link=\"glossary://GlossaryEntry_Afflictions_Perception\"><#ca58ff>$1 <sprite=\"Inline\" name=\"attribute_perception\" tint=1></color></link>"
		};
		
		int i = 0;
		switch (targetLanguage) {
			case "en":
				i = 12 * 0;
				break;
			case "de_patch":
				i = 12 * 1;
				break;
			case "fr":
		//		i = 12 * 2;
				break;
			case "it":
		//		i = 12 * 3;
				break;
			case "es":
		//		i = 12 * 4;
				break;
			case "pt":
		//		i = 12 * 5;
				break;
			case "pl":
		//		i = 12 * 6;
				break;
			case "ru":
		//		i = 12 * 7;
				break;
			case "zh":
		//		i = 12 * 8;
				break;
		}
		
		for(int j = 0; j < 12; j++) {
			result.DefaultText = result.DefaultText.replaceAll(afflictionsReplacementWords[i + j], targetWords[j]);
			result.FemaleText = result.FemaleText.replaceAll(afflictionsReplacementWords[i + j], targetWords[j]);
		}
		if(result.DefaultText.equals(DefaultText) && result.FemaleText.equals(FemaleText)) {
			return null;
		} else {
			return result;
		}
	}       
	
	// Next tagscount und art? vs englisch
	
	final static String[] damageReplacementWords = new String[] {
			// EN 
			"(?<!xg>)([bB]urn)(\\b|ed|ing|s)", 
			"(?<!xg>)([cC]orro)(\\b|des|de|sive)", 
			"(?<!xg>)([cC]rush)(\\b|es|ing)",
			"(?<!xg>)([fF]reez)(\\b|es|ed|e|ing)",
			"(?<!xg>)([pP]ierc)(\\b|es|e|ing)",
			"(?<!xg>)([rR]aw)(\\b)",
			"(?<!xg>)([sS]lash)(\\b|ing)",
			"(?<!xg>)([sS]hock)(\\b|ed|ing|s)",
			
			
			// DE
			"(?<!xg>)(Brand-?|Brandzeit|Brandflächen|[Vv]erbrennt)(sch[aä]den|effekt|\\b)",
			"(?<!xg>)(Zersetzung-?)(ssch[aä]den|\\b)",
			"(?<!xg>)(Wucht-?)(sch[aä]den|\\b)",
			"(?<!xg>)(Frost-?)(sch[aä]den|\\b)",
			"(?<!xg>)(Stich-?)(sch[aä]den|\\b)",
			"(?<!xg>)(Direkt-?|[Dd]irekte[rnm] )((Gift|Blutungs)?[sS]chaden)",
			"(?<!xg>)(Hieb-?)(sch[aä]den|\\b)",
			"(?<!xg>)(Schock-?)(sch[aä]den|\\b)",
			
			//fr
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			//it
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			//es
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			//pt
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			
			
			// PL rodzaje obrazen
            "(?<!xg>)([pP]oparzony|[pP]ali się|ognia)(\\b)",
            "(?<!xg>)(\\b[kK]was|[kK]oro)(\\b|zyjny|zyjna|duje)",
            "(?<!xg>)([mM]iażdż)(y|one)",
            "(?<!xg>)([zZ]imn)(a|o)|([zZ]amr)(aża|ożony|ożenie)",
            "(?<!xg>)([kK]łu[tj])(e|ące)",
            "(?<!xg>)([bB]azowe|kwasu)(\\b)",
            "(?<!xg>)([cC]ięte|[tT]nące)(\\b)",
            "(?<!xg>)([eE]lektryczn)(ość|ości|y|e)",
            
	};
	
	
	public Entry replaceDamageTypeWithIcon(String targetLanguage) {
		Entry result = new Entry();
		result.ID = this.ID;
		result.DefaultText = DefaultText;
		result.FemaleText = FemaleText;
		
		String[] targetWords = new String[] {
			"<sprite=\"Inline\" name=\"cs_burn\" tint=1> $1$2",
			"<sprite=\"Inline\" name=\"cs_corrosive\" tint=1> $1$2",
			"<sprite=\"Inline\" name=\"cs_blunt\" tint=1> $1$2",
			"<sprite=\"Inline\" name=\"cs_freeze\" tint=1> $1$2",
			"<sprite=\"Inline\" name=\"cs_pierce\" tint=1> $1$2",
			"<sprite=\"Inline\" name=\"cs_raw\" tint=1> $1$2",
			"<sprite=\"Inline\" name=\"cs_slash\" tint=1> $1$2",
			"<sprite=\"Inline\" name=\"cs_shock\" tint=1> $1$2",
		};
		
		int i = 0;
		switch (targetLanguage) {
			case "en":
				i = 8 * 0;
				break;
			case "de_patch":
				i = 8 * 1;
				break;
			case "fr":
		//		i = 8 * 2;
				break;
			case "it":
		//		i = 8 * 3;
				break;
			case "es":
		//		i = 8 * 4;
				break;
			case "pt":
		//		i = 8 * 5;
				break;
			case "pl":
				i = 8 * 6;
				break;
			case "ru":
		//		i = 8 * 7;
				break;
			case "zh":
		//		i = 8 * 8;
				break;
		}
		
		for(int j = 0; j < 8; j++) {
			result.DefaultText = result.DefaultText.replaceAll(damageReplacementWords[i + j], targetWords[j]);
			result.FemaleText = result.FemaleText.replaceAll(damageReplacementWords[i + j], targetWords[j]);
		}
		if(result.DefaultText.equals(DefaultText) && result.FemaleText.equals(FemaleText)) {
			return null;
		} else {
			return result;
		}
	}
	
	
	final static String[] defenseReplacementWords = new String[] {
			// EN 
			"(?<!xg>)(Fortitude)", 
			"(?<!xg>)(Deflection)", 
			"(?<!xg>)(Reflex|Reflexes)",
			"(?<!xg>)(Will|Willpower)",
			"(?<!xg>)(Penetrate|Penetration)",
			"(?<!xg>)(AR|Armor Rating)",
			
			// DE 
			"(?<!xg>)(Tapferkeitsverteidigung|Tapferkeit)", 
			"(?<!xg>)(Abwehr)", 
			"(?<!xg>)(Reflexe|Reflexverteidigung)",
			"(?<!xg>)(Willen?\\b|Willensverteidigung)",
			"(?<!xg>)(Durchschlag|Durchschlagskraft)",
			"(?<!xg>)(RW|Rüstungswert)",
			
			//fr
			"",
			"",
			"",
			"",
			"",
			"",
			//it
			"",
			"",
			"",
			"",
			"",
			"",
			//es
			"",
			"",
			"",
			"",
			"",
			"",
			//pt
			"",
			"",
			"",
			"",
			"",
			"",
			
			// PL defensywa
            "(?<!xg>)(Hart|[hH]artu|[hH]artem)",
            "(?<!xg>)([oO]dbici[ae]m)",
            "(?<!xg>)(Refleks|[rR]efleksu|[rR]efleksem)",
            "(?<!xg>)([wW]ol[ai]|[wW]olą|Siła woli)",
            "(?<!xg>)(Penetruje|Penetracja)",
            "(?<!xg>)(SP|Skuteczność pancerza)",
 
	};
	
	public Entry replaceDefenseWithIcon(String targetLanguage) {
		Entry result = new Entry();
		result.ID = this.ID;
		result.DefaultText = DefaultText;
		result.FemaleText = FemaleText;
		
		String[] targetWords = new String[] {
			"<#f9d968><sprite=\"Inline\" name=\"cs_fortitude\" tint=1> $1</color>",
			"<#b0cffd><sprite=\"Inline\" name=\"cs_deflection\" tint=1> $1</color>",
			"<#8bf7c3><sprite=\"Inline\" name=\"cs_reflex\" tint=1> $1</color>",
			"<#d591f8><sprite=\"Inline\" name=\"cs_will\" tint=1> $1</color>",
			"<sprite=\"Inline\" name=\"cs_penetration\" tint=1> $1",
			"<sprite=\"Inline\" name=\"cs_ar\" tint=1> $1",
		};
		
		int i = 0;
		switch (targetLanguage) {
			case "en":
				i = 6 * 0;
				break;
			case "de_patch":
				i = 6 * 1;
				break;
			case "fr":
		//		i = 6 * 2;
				break;
			case "it":
		//		i = 6 * 3;
				break;
			case "es":
		//		i = 6 * 4;
				break;
			case "pt":
		//		i = 6 * 5;
				break;
			case "pl":
				i = 6 * 6;
				break;
			case "ru":
		//		i = 6 * 7;
				break;
			case "zh":
		//		i = 6 * 8;
				break;
		}
		
		for(int j = 0; j < 6; j++) {
			result.DefaultText = result.DefaultText.replaceAll(defenseReplacementWords[i + j], targetWords[j]);
			result.FemaleText = result.FemaleText.replaceAll(defenseReplacementWords[i + j], targetWords[j]);
		}
		if(result.DefaultText.equals(DefaultText) && result.FemaleText.equals(FemaleText)) {
			return null;
		} else {
			return result;
		}
	}

	public Entry stripMarkup() {
		this.DefaultText = this.DefaultText.replaceAll("<size=80%>.*?</size>", "");
		this.DefaultText = this.DefaultText.replaceAll("<(?!\\/?xg).*?>", "");
		this.DefaultText = this.DefaultText.replaceAll(" ", " "); // NB Space gegen normales
		this.DefaultText = this.DefaultText.replaceAll("  ", " "); // 2 space gegen 1
		this.FemaleText = this.FemaleText.replaceAll("<size=80%>[123]</size>", "");
		this.FemaleText = this.FemaleText.replaceAll("<(?!\\/?xg).*?>", "");
		this.FemaleText = this.FemaleText.replaceAll(" ", " ");
		this.FemaleText = this.FemaleText.replaceAll("  ", " ");
		return this;
	}
}

class Pair<A, B> {
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;

	}

	public A a;
	public B b;
}