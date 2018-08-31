package base;

import java.util.ArrayList;
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
		while (DefaultText.contains("‚ ")) {
			DefaultText = DefaultText.replaceFirst("‚ ", " ‚");
			satzzeichen++;
		}
		while (DefaultText.contains(" ‘")) {
			DefaultText = DefaultText.replaceFirst(" ‘", "‘ ");
			satzzeichen++;
		}
		// TODO false positive chance
		//while (DefaultText.matches("[.!?“][^ ]")) {
		//	DefaultText = DefaultText.replaceFirst("„ ", " „");
		//	satzzeichen++;
		//}

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
		while (FemaleText.contains("‚ ")) {
			FemaleText = FemaleText.replaceFirst("‚ ", " ‚");
			satzzeichen++;
		}
		while (FemaleText.contains(" ‘")) {
			FemaleText = FemaleText.replaceFirst(" ‘", "‘ ");
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
		
		if (DefaultText.split("<.+?>", 99).length > 1) {
			// es gibt öffnenden und schließenden tag
			String pattern = "(<((.+?)=.+?|(.+?))>).*?(<\\/(.+?)>)";

			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = r.matcher(DefaultText);
			if (m.find() && (!m.group(5).startsWith("</") || ((m.group(3) != null && !m.group(3).equals(m.group(6))) || (m.group(4) != null && !m.group(4).equals(m.group(6)))))) {
				System.out.println("Found value: " + m.group(0));
				html++;
			}
		}
		
		if ((DefaultText.split("<.+?>", 99).length - 1) % 2 != 0) {
			System.out.println("Found value, unbalanced Tags: " + DefaultText);
			html++;
		}

		if (FemaleText.chars().filter(ch -> ch == '{').count() != FemaleText.chars().filter(ch -> ch == '}').count()) {
			geschweift++;
		}
		if (FemaleText.split("<", -1).length - 1 != FemaleText.split(">", -1).length - 1) {
			html++;
		}
		if ((FemaleText.split("<", -1).length - 1) % 2 != 0) {
			html++;
		}

		if (FemaleText.split("<.+?>", 99).length > 1) {
			// es gibt öffnenden und schließenden tag
			String pattern = "(<((.+?)=.+?|(.+?))>).*?(<\\/(.+?)>)";
			
			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = r.matcher(FemaleText);
			if (m.find() && (!m.group(5).startsWith("</") || ((m.group(3) != null && !m.group(3).equals(m.group(6))) || (m.group(4) != null && !m.group(4).equals(m.group(6)))))) {
				System.out.println("Found value: " + m.group(0));
				html++;
			}
		}
		
		if ((FemaleText.split("<.+?>", 99).length - 1) % 2 != 0) {
			System.out.println("Found value, unbalanced Tags: " + FemaleText);
			html++;
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
		String[] words = DefaultText.split("[<>\\., !?…\\-–„“‚‘\\n\\t()]");

		for (String word : words) {
			target.merge(word, 1, (x, y) -> x + y);
		}
		words = FemaleText.split("[<>\\., !?…\\-–„“‚‘\\n\\t()]");
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
			"(?<!xg>)(Taumeln)(de|d|\\b)", "(?<!xg>)(Benommen)(e|heit|\\b)", "(?<!xg>)(Betäub)(ung|en|te|t)",
			// Wahrnehmung
			"(?<!xg>)(Einsichtig(e)?)", "(?<!xg>)(Aufmerksam(e)?)", "(?<!xg>)(Intuitiv(e)?)",
			"(?<!xg>)(Abgelenkt|Ablenkenden)(er|e|\\b)", "(?<!xg>)(Desorientier)(te|t|render|\\b)", "(?<!xg>)(G?e?blende|[Ee]rblinde)(te|t|n(?!d))",
			
			
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
			"(?<!xg>)(In [Ff]orma)", "(?<!xg>)(Poderoso)", "(?<!xg>)(Robusto)",
			"(?<!xg>)(Nausea)(ndo|no|to|ti|re|\\b)", "(?<!xg>)(Indebol)(endol[io]|endo|iti|ito|ire|imento|iscono|isce)", "(?<!xg>)(Debilita)(ndo|ndoli|to|ti|\\b)",

			//Risolutezza
			"(?<!xg>)(Nerbo)", "(?<!xg>)(Risolut[io])", "(?<!xg>)(Coraggios[io])",
			"(?<!xg>)(Scoss)(o|i)", "(?<!xg>)(Spaventa)(ndoli|ndo|re|to|\\b)", "(?<!xg>)(Terrorizza)(ndo|no|to|ti)",
			
			// Destrezza
			"(?<!xg>)(Velocizza(ndoli|nte|ndo|to)|Veloc[ei])(?!tà)", "(?<!xg>)(Lest[io])", "(?<!xg>)(Rapid[io])",
			"(?<!xg>)(Azzoppa)(ndoli|ndo|to|re|ti|no|\\b)", "(?<!xg>)(Immobilizza)(ndolo|ndo|no|to|re)", "(?<!xg>)(Parali)(zzando|zzano|zzandoli|zzato|si|zza)",
			 
			// Acume
			"(?<!xg>)(Argut[io])", "(?<!xg>)(Perspicac[ie])", "(?<!xg>)(Brillant[ie])",
			"(?<!xg>)(Conf)(usione|usi|uso|ondendoli|ondendo|onde)", "(?<!xg>)(Charme|Incantat[io]|Incantare)(\\b)", "(?<!xg>)(Domina)(zione|to|\\b)",
			
			// Vigore
			"(?<!xg>)(Fort[ei])(?!tude)", "(?<!xg>)(Tenace)", "(?<!xg>)(Energico)",
			"(?<!xg>)(Sorpre)(si|so|nd[oe]no|ndendo)", "(?<!xg>)(Disorienta)(ndol[oi]|ndo|nte|mento|to|ti|re|\\b)", "(?<!xg>)(Stord)(endoli|endo|isce|ito|iti|imento|ire|iscono|irai)",
			 
			// Percezione
			"(?<!xg>)(Profond[io])", "(?<!xg>)(Consapevol[ie])", "(?<!xg>)(Intuitiv[ioa])",
			"(?<!xg>)(Distra)(ggono|tti|tto|rre|e)", "(?<!xg>)(Spaesa)(to|ti)", "(?<!xg>)(Cecità|Accecan?[dt]?[io]?|Accecare)(\\b)",
			
			
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
            "(?<!xg>)(wytrącen[aei])( z równowagi)", "(?<!xg>)(oszołomieni[ea]?|oszałamia)(jącej|jąc|\\b)", "(?<!xg>)(ogłuszenie)(\\b)",

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
	 
	
	
	
	final static String[] inspirationTargetWords = new String[] {
		"<nobr><link=\"glossary://GlossaryEntry_Fit\"><#f7b733>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_constitution\" tint=1><space=0.3em><voffset=0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Hardy\"><#f7b733>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_constitution\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Robust\"><#f7b733>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_constitution\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
	                                                                                                                                                                                                                                                                                                                           
		"<nobr><link=\"glossary://GlossaryEntry_Sickened\"><#f7b733>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_constitution\" tint=1><space=0.3em><voffset=-0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Weakened\"><#f7b733>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_constitution\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Enfeebled\"><#f7b733>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_constitution\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
		"<nobr><link=\"glossary://GlossaryEntry_Steadfast\"><#30d3d5>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_resolve\" tint=1><space=0.3em><voffset=0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Resolute\"><#30d3d5>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_resolve\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Courageous\"><#30d3d5>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_resolve\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
	                                                                                                                                                                                                                                                                                                                                                         
		"<nobr><link=\"glossary://GlossaryEntry_Shaken\"><#30d3d5>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_resolve\" tint=1><space=0.3em><voffset=-0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Frightened\"><#30d3d5>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_resolve\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Terrified\"><#30d3d5>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_resolve\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
                                                                                                                                                                            
		"<nobr><link=\"glossary://GlossaryEntry_Quick\"><#72da26>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_dexterity\" tint=1><space=0.3em><voffset=0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>", 
		"<nobr><link=\"glossary://GlossaryEntry_Nimble\"><#72da26>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_dexterity\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Swift\"><#72da26>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_dexterity\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
                                                                                                                                                                
		"<nobr><link=\"glossary://GlossaryEntry_Hobbled\"><#72da26>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_dexterity\" tint=1><space=0.3em><voffset=-0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Immobilized\"><#72da26>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_dexterity\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Paralyzed\"><#72da26>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_dexterity\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
	                                 
		"<nobr><link=\"glossary://GlossaryEntry_Smart\"><#00a4ff>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_intellect\" tint=1><space=0.3em><voffset=0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Acute\"><#00a4ff>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_intellect\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Brilliant\"><#00a4ff>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_intellect\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
	
		"<nobr><link=\"glossary://GlossaryEntry_Confused\"><#00a4ff>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_intellect\" tint=1><space=0.3em><voffset=-0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Charmed\"><#00a4ff>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_intellect\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Dominated\"><#00a4ff>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_intellect\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
	
		"<nobr><link=\"glossary://GlossaryEntry_Strong\"><#ff4800>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_might\" tint=1><space=0.3em><voffset=0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Tenacious\"><#ff4800>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_might\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Energized\"><#ff4800>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_might\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
	
		"<nobr><link=\"glossary://GlossaryEntry_Staggered\"><#ff4800>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_might\" tint=1><space=0.3em><voffset=-0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>", 
		"<nobr><link=\"glossary://GlossaryEntry_Dazed\"><#ff4800>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_might\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Stunned\"><#ff4800>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_might\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
	
		"<nobr><link=\"glossary://GlossaryEntry_Insightful\"><#ca58ff>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_perception\" tint=1><space=0.3em><voffset=0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>", 
		"<nobr><link=\"glossary://GlossaryEntry_Aware\"><#ca58ff>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_perception\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Intuitive\"><#ca58ff>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_perception\" tint=1><space=0.3em><voffset=0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>", 
                                                                                                                                                                                                                                                                                                                                                          
		"<nobr><link=\"glossary://GlossaryEntry_Distracted\"><#ca58ff>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_perception\" tint=1><space=0.3em><voffset=-0.35em><size=80%><font=\"EspinosaNova-Regular SDF\"><b>1</b></font></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Disoriented\"><#ca58ff>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_perception\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>2</b></size></voffset></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Blinded\"><#ca58ff>$1$2<space=0.7em><sprite=\"Inline\" name=\"attribute_perception\" tint=1><space=0.3em><voffset=-0.35em><size=80%><b>3</b></size></voffset></color></link></nobr>",
	};
	

	public Entry replaceAfflictionWithColor(String targetLanguage) {
		Entry result = new Entry();
		result.ID = ID;
		result.DefaultText = DefaultText;
		result.FemaleText = FemaleText;
				
		int i = 0;
		switch (targetLanguage) {
			case "en":
				i = inspirationTargetWords.length * 0;
				break;
			case "de_patch":
				i = inspirationTargetWords.length * 1;
				break;
			case "fr":
				i = inspirationTargetWords.length * 2;
				break;
			case "it":
				i = inspirationTargetWords.length * 3;
				break;
			case "es":
				i = inspirationTargetWords.length * 4;
				break;
			case "pt":
				i = inspirationTargetWords.length * 5;
				break;
			case "pl":
				i = inspirationTargetWords.length * 6;
				break;
			case "ru":
				i = inspirationTargetWords.length * 7;
				break;
			case "zh":
				i = inspirationTargetWords.length * 8;
				break;
		}
		
		for(int j = 0; j < inspirationTargetWords.length; j++) {
			result.DefaultText = result.DefaultText.replaceAll(replacementWords[i + j], inspirationTargetWords[j]);
			result.FemaleText = result.FemaleText.replaceAll(replacementWords[i + j], inspirationTargetWords[j]);
		}
		if(result.DefaultText.equals(DefaultText) && result.FemaleText.equals(FemaleText)) {
			return null;
		} else {
			return result;
		}
	}

	// TODO Aufzählungen in anderen Sprachen als de. Da fehlt der -
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
			"(Mind Inspirations?)",
			"(Mind Afflictions?)",
			"(Body Inspirations?)",
			"(Body Afflictions?)",
			
			// de_patch
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
			"(Wahrnehmungs-|Wahrnehmungswirkung(en)?)",
			"(Geistesinspiration(en)?)",
			"(Geisteswirkung(en)?)",
			"(Körperinspiration(en)?)",
			"(Körperwirkung(en)?)",

			// fr
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			
			// it
			"(Ispirazion[ei] della Costituzione)",
            "(Alterazion[ei] della Costituzione)",
            "(Ispirazion[ei] della Risolutezza)",
            "(Alterazion[ei] della Risolutezza)",
            "(Ispirazion[ei] della Destrezza)",
            "(Alterazion[ei] della Destrezza)",
            "(Ispirazion[ei] dell'Acume)",
            "(Alterazion[ei] dell'Acume)",
            "(Ispirazion[ei] d(el|i)? Vigore)",
            "(Alterazion[ei] del Vigore)",
            "(Ispirazion[ei] della Percezione)",
            "(Alterazion[ei] della Percezione)",
            "(Ispirazion[ei] della Mente)",
            "(Alterazion[ei] della Mente)",
            "(Ispirazion[ei] del Corpo)",
            "(Alterazion[ei] del Corpo)",
	};
            
	// Word Icon
	final static String[] genericAfflictionsTargetWordsPostfix = new String[] {
			"<link=\"glossary://GlossaryEntry_Inspirations_Constitution\"><#f7b733>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_constitution\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Afflictions_Constitution\"><#f7b733>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_constitution\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Inspirations_Resolve\"><#30d3d5>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_resolve\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Afflictions_Resolve\"><#30d3d5>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_resolve\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Inspirations_Dexterity\"><#72da26>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_dexterity\" tint=1></color></link>",            
            "<link=\"glossary://GlossaryEntry_Afflictions_Dexterity\"><#72da26>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_dexterity\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Inspirations_Intellect\"><#00a4ff>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_intellect\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Afflictions_Intellect\"><#00a4ff>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_intellect\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Inspirations_Might\"><#ff4800>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_might\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Afflictions_Might\"><#ff4800>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_might\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Inspirations_Perception\"><#ca58ff>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_perception\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Afflictions_Perception\"><#ca58ff>$1<space=0.7em><sprite=\"Inline\" name=\"attribute_perception\" tint=1></color></link>",
            "<link=\"glossary://GlossaryEntry_Mind_Inspiration\">$1</link>",
            "<link=\"glossary://GlossaryEntry_Mind_Affliction\">$1</link>",
            "<link=\"glossary://GlossaryEntry_Body_Inspiration\">$1</link>",
            "<link=\"glossary://GlossaryEntry_Body_Affliction\">$1</link>"
	};
            
	public Entry replaceGenericAfflictionsWithIcon(String targetLanguage) {
		Entry result = new Entry();
		result.ID = this.ID;
		result.DefaultText = DefaultText;
		result.FemaleText = FemaleText;
		
		
		int i = 0;
		switch (targetLanguage) {
			case "en":
				i = genericAfflictionsTargetWordsPostfix.length * 0;
				break;
			case "de_patch":
				i = genericAfflictionsTargetWordsPostfix.length * 1;
				break;
			case "fr":
		//		i = genericAfflictionsTargetWordsPostfix.length * 2;
				break;
			case "it":
				i = genericAfflictionsTargetWordsPostfix.length * 3;
				break;
			case "es":
		//		i = genericAfflictionsTargetWordsPostfix.length * 4;
				break;
			case "pt":
		//		i = genericAfflictionsTargetWordsPostfix.length * 5;
				break;
			case "pl":
		//		i = genericAfflictionsTargetWordsPostfix.length * 6;
				break;
			case "ru":
		//		i = genericAfflictionsTargetWordsPostfix.length * 7;
				break;
			case "zh":
		//		i = genericAfflictionsTargetWordsPostfix.length * 8;
				break;
		}
		
		for(int j = 0; j < genericAfflictionsTargetWordsPostfix.length; j++) {
			result.DefaultText = result.DefaultText.replaceAll(afflictionsReplacementWords[i + j], genericAfflictionsTargetWordsPostfix[j]);
			result.FemaleText = result.FemaleText.replaceAll(afflictionsReplacementWords[i + j], genericAfflictionsTargetWordsPostfix[j]);
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
			"(?<!xg>)([bB]urn)(\\b|ed|ing|s)", // Fire
			"(?<!xg>)([cC]orro)(\\b|des|de|sive)", 
			"(?<!xg>)([cC]rush)(\\b|es|ing)",
			"(?<!xg>)([fF]reez)(\\b|es|ed|e|ing)", // Frost
			"(?<!xg>)([pP]ierc)(\\b|es|e|ing)",
			"(?<!xg>)([rR]aw)(\\b)",
			"(?<!xg>)([sS]lash)(\\b|ing)",
			"(?<!xg>)([sS]hock)(\\b|ed|ing|s)",
			
			
			// DE
			"(?<!xg>)(Brand-?|Brandzeit|Brandflächen|[Vv]erbrennt|Feuer)(sch[aä]den|effekt|angriffen?|\\b)",
			"(?<!xg>)(Zersetzung-?)(ssch[aä]den|\\b)",
			"(?<!xg>)(Wucht-?)(sch[aä]den|\\b)",
			"(?<!xg>)(Frost-?|Eislanzen)(sch[aä]den|angriffen?|\\b)",
			"(?<!xg>)(Stich-?)(sch[aä]den|\\b)",
			"(?<!xg>)(Direkt-?|[Dd]irekte[rnm] )((Gift|Blutungs|Zeit)?[sS]chaden)",
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
            "(?<!xg>)(Ustion[ei])(ant[ei]|\\b)",
            "(?<!xg>)(Corrosi)(on[ei]|v[oi])",
            "(?<!xg>)(Impatt[oi])(\\b)",
            "(?<!xg>)(Congela)(zion[ei]|ment[oi]|nt[ei])",
            "(?<!xg>)(Perforazion[ei])(nt[ei]|\\b)",
            "(?<!xg>)(Puro)(\\b)",
            "(?<!xg>)(Lacera)(zion[ei]|nt[ei])",
            "(?<!xg>)(Folgorazion[ei])(nt[ei]|\\b)",
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
		
	// Word Icon Rest
	final static String[] damageTypeTargetWordsInfix = new String[] {
		"<nobr><link=\"glossary://GlossaryEntry_Burning\"><#e86a1d>$1$2</color><space=0.2em><size=120%><#d84315><voffset=-0.05em><sprite=\"Inline\" name=\"cs_burn\" tint=1></voffset></color></size><space=-4.6em><#ff9800><size=85%><sprite=\"Inline\" name=\"cs_burn\" tint=1></size></color><space=-2.9em><size=45%><#fff59d><sprite=\"Inline\" name=\"cs_burn\" tint=1></color></size></link><space=1em></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Corrode\"><#adc455>$1$2<#9ba967><space=0.3em><sprite=\"Inline\" name=\"cs_corrosive\" tint=1></color><space=-0.3em></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Crushing\">$1$2<space=0.7em><sprite=\"Inline\" name=\"cs_blunt\" tint=1></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Freezing\"><#b3e5fc>$1$2<space=0.7em><sprite=\"Inline\" name=\"cs_freeze\" tint=1></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Piercing\">$1$2<space=0.7em><sprite=\"Inline\" name=\"cs_pierce\" tint=1></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Raw\"><#ef5350>$1$2<space=0.3em><#b71c1c><sprite=\"Inline\" name=\"cs_raw\" tint=1></color><space=-3.8em><voffset=0.25em><size=60%><sprite=\"Inline\" name=\"cs_raw\" tint=1></size></voffset></color><space=0.5em></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Slashing\">$1$2<space=0.7em><sprite=\"Inline\" name=\"cs_slash\" tint=1></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Shock\"><#fff151>$1$2<voffset=-0.1em><sprite=\"Inline\" name=\"cs_shock\" tint=1></voffset></color></link><space=-1em></nobr>",
	};
	
	//  Icon Word Rest
	final static String[] damageTypeTargetWordsPrefix = new String[] {
		"<nobr><link=\"glossary://GlossaryEntry_Burning\"><space=-0.7em><size=120%><#d84315><voffset=-0.05em><sprite=\"Inline\" name=\"cs_burn\" tint=1></voffset></color></size><space=-4.6em><#ff9800><size=85%><sprite=\"Inline\" name=\"cs_burn\" tint=1></size></color><space=-2.9em><size=45%><#fff59d><sprite=\"Inline\" name=\"cs_burn\" tint=1></color></size><#e86a1d><space=1em>$1$2</color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Corrode\"><#9ba967><sprite=\"Inline\" name=\"cs_corrosive\" tint=1></color><#adc455><space=0.3em>$1$2</color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Crushing\"><sprite=\"Inline\" name=\"cs_blunt\" tint=1><space=0.7em>$1$2</link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Freezing\"><#b3e5fc><sprite=\"Inline\" name=\"cs_freeze\" tint=1><space=0.7em>$1$2</color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Piercing\"><sprite=\"Inline\" name=\"cs_pierce\" tint=1><space=0.7em>$1$2</link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Raw\"><space=-0.5em><#b71c1c><sprite=\"Inline\" name=\"cs_raw\" tint=1></color><space=-3.8em><voffset=0.25em><#ef5350><size=60%><sprite=\"Inline\" name=\"cs_raw\" tint=1></size></voffset></color><#b71c1c><space=1em>$1$2</color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Slashing\"><sprite=\"Inline\" name=\"cs_slash\" tint=1><space=0.7em>$1$2</link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Shock\"><space=-0.5em><#fff151><voffset=-0.1em><sprite=\"Inline\" name=\"cs_shock\" tint=1></voffset><space=-0.3em>$1$2</color></link></nobr>",
	};
	
	public Entry replaceDamageTypeWithIcon(String targetLanguage) {
		Entry result = new Entry();
		result.ID = this.ID;
		result.DefaultText = DefaultText;
		result.FemaleText = FemaleText;
		
		
		
		int i = 0;
		switch (targetLanguage) {
			case "en":
				i = damageTypeTargetWordsPrefix.length * 0;
				break;
			case "de_patch":
				i = damageTypeTargetWordsPrefix.length * 1;
				break;
			case "fr":
		//		i = damageTypeTargetWordsPrefix.length * 2;
				break;
			case "it":
				i = damageTypeTargetWordsPrefix.length * 3;
				break;
			case "es":
		//		i = damageTypeTargetWordsPrefix.length * 4;
				break;
			case "pt":
		//		i = damageTypeTargetWordsPrefix.length * 5;
				break;
			case "pl":
				i = damageTypeTargetWordsPrefix.length * 6;
				break;
			case "ru":
		//		i = damageTypeTargetWordsPrefix.length * 7;
				break;
			case "zh":
		//		i = damageTypeTargetWordsPrefix.length * 8;
				break;
		}
		
		for(int j = 0; j < damageTypeTargetWordsPrefix.length; j++) {
			result.DefaultText = result.DefaultText.replaceAll(damageReplacementWords[i + j], damageTypeTargetWordsPrefix[j]);
			result.FemaleText = result.FemaleText.replaceAll(damageReplacementWords[i + j], damageTypeTargetWordsPrefix[j]);
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
			"(?<!xg>)(Accuracy|Accurate)",
			"(?<!xg>)(Health)",
			
			// DE 
			"(?<!xg>)(Tapferkeitsverteidigung|Tapferkeit)", 
			"(?<!xg>)(Abwehr)", 
			"(?<!xg>)(Reflexe|Reflexverteidigung)",
			"(?<!xg>)(Willen?\\b|Willensverteidigung)",
			"(?<!xg>)(Durchschlag|Durchschlagskraft)",
			"(?<!xg>)(RW|Rüstungswert)",
			"(?<!xg>)(Nahkampfgenauigkeit|Fernkampfgenauigkeit|Schussgenauigkeit|Genauigkeit)",
			"(?<!xg>)(Gesundheit|Heilungskreis|Heilung|Heileffekte?)",
			
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
			"(?<!xg>)(Tempra)",
			"(?<!xg>)(Deflessione)",
			"(?<!xg>)(Riflessi)",
			"(?<!xg>)(Volontà)",
			"(?<!xg>)(Penetrazione)",
			"(?<!xg>)(Sogli[ae] di Danno)",
			"(?<!xg>)(Precisione)",
			"(?<!xg>)(Salute)",
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
			
			// PL defensywa
            "(?<!xg>)(Hart|[hH]artu|[hH]artem)",
            "(?<!xg>)([oO]dbici[ae]m)",
            "(?<!xg>)(Refleks|[rR]efleksu|[rR]efleksem)",
            "(?<!xg>)([wW]ol[ai]|[wW]olą|Siła woli)",
            "(?<!xg>)(Penetruje|Penetracja)",
            "(?<!xg>)(SP|Skuteczność pancerza)",
            "(?<!xg>)(Celność)",
			"(?<!xg>)(Zdrowie)",


	};
	
	// Word Icon
	final static String[] defenseTargetWordsPostfix = new String[] {
		"<nobr><link=\"glossary://GlossaryEntry_Fortitude\">$1<space=0.7em><#f9d968><sprite=\"Inline\" name=\"cs_fortitude\" tint=1></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Deflection\">$1<space=0.7em><#b0cffd><sprite=\"Inline\" name=\"cs_deflection\" tint=1></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Reflexes\">$1<space=0.7em><#8bf7c3><sprite=\"Inline\" name=\"cs_reflex\" tint=1></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Will\">$1<space=0.7em><#d591f8><sprite=\"Inline\" name=\"cs_will\" tint=1></color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Penetration\">$1<space=0.7em><sprite=\"Inline\" name=\"cs_penetration\" tint=1></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Armor_Rating\">$1<space=0.7em><sprite=\"Inline\" name=\"cs_ar\" tint=1></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Accuracy\">$1<space=0.7em><sprite=\"Inline\" name=\"cs_accuracy\" tint=1></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Health\">$1<space=0.7em><voffset=0.1em><size=80%><#80e27e><sprite=\"Inline\" name=\"cs_health\" tint=1></color></size></voffset><space=-0.8em><voffset=0.8em><size=45%><#4caf50><sprite=\"Inline\" name=\"cs_health\" tint=1></color></size></voffset></link></nobr>"
	};
		
	// Icon Word
	final static String[] defenseTargetWordsPrefix = new String[] {
		"<nobr><link=\"glossary://GlossaryEntry_Fortitude\"><#f9d968><sprite=\"Inline\" name=\"cs_fortitude\" tint=1><space=0.7em>$1</color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Deflection\"><#b0cffd><sprite=\"Inline\" name=\"cs_deflection\" tint=1><space=0.7em>$1</color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Reflexes\"><#8bf7c3><sprite=\"Inline\" name=\"cs_reflex\" tint=1><space=0.7em>$1</color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Will\"><#d591f8><sprite=\"Inline\" name=\"cs_will\" tint=1><space=0.7em>$1</color></link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Penetration\"><sprite=\"Inline\" name=\"cs_penetration\" tint=1><space=0.7em>$1</link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Armor_Rating\"><sprite=\"Inline\" name=\"cs_ar\" tint=1><space=0.7em>$1</link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Accuracy\"><sprite=\"Inline\" name=\"cs_accuracy\" tint=1><space=0.7em>$1</link></nobr>",
		"<nobr><link=\"glossary://GlossaryEntry_Health\"><voffset=0.1em><size=80%><#80e27e><sprite=\"Inline\" name=\"cs_health\" tint=1></color></size></voffset><space=-0.8em><voffset=0.8em><size=45%><#4caf50><sprite=\"Inline\" name=\"cs_health\" tint=1></size></voffset><space=0.7em>$1</color></link></nobr>"
	};
	
	public Entry replaceDefenseWithIcon(String targetLanguage) {
		Entry result = new Entry();
		result.ID = this.ID;
		result.DefaultText = DefaultText;
		result.FemaleText = FemaleText;
		
		
		
		int i = 0;
		switch (targetLanguage) {
			case "en":
				i = defenseTargetWordsPrefix.length * 0;
				break;
			case "de_patch":
				i = defenseTargetWordsPrefix.length * 1;
				break;
			case "fr":
		//		i = defenseTargetWordsPrefix.length * 2;
				break;
			case "it":
				i = defenseTargetWordsPrefix.length * 3;
				break;
			case "es":
		//		i = defenseTargetWordsPrefix.length * 4;
				break;
			case "pt":
		//		i = defenseTargetWordsPrefix.length * 5;
				break;
			case "pl":
				i = defenseTargetWordsPrefix.length * 6;
				break;
			case "ru":
		//		i = defenseTargetWordsPrefix.length * 7;
				break;
			case "zh":
		//		i = defenseTargetWordsPrefix.length * 8;
				break;
		}
		
		for(int j = 0; j < defenseTargetWordsPrefix.length; j++) {
			result.DefaultText = result.DefaultText.replaceAll(defenseReplacementWords[i + j], defenseTargetWordsPrefix[j]);
			result.FemaleText = result.FemaleText.replaceAll(defenseReplacementWords[i + j], defenseTargetWordsPrefix[j]);
		}
		if(result.DefaultText.equals(DefaultText) && result.FemaleText.equals(FemaleText)) {
			return null;
		} else {
			return result;
		}
	}
	
	
	public Entry addReputation(String fileName) {
		if(fileName.contains("gui")) {
			Entry result = new Entry();
			result.ID = this.ID;
			
			if(this.ID == 391 || this.ID == 4756) {
				result.DefaultText = "<line-height=100%><#157245>" + DefaultText + "</color></line-height>";
				result.FemaleText = !this.FemaleText.equals("") ? "<line-height=100><#157245>" + FemaleText + "</color></line-height>" : FemaleText;
				return result;
			}
			if(this.ID == 392 || this.ID == 4757) {
				result.DefaultText = "<line-height=100%><#bf2a2a>" + DefaultText + "</color></line-height>";
				result.FemaleText = !this.FemaleText.equals("") ? "<line-height=100><#bf2a2a>" + FemaleText + "</color></line-height>" : FemaleText;
				return result;
			}			
		}
		return this;
	}
	
	
	
	final static String[] manualAfflictionsTitleInsertWords = new String[] {
			// EN
			"Constitution Inspiration",
			"Constitution Affliction",
			"Resolve Inspiration",
			"Resolve Affliction",
			"Dexterity Inspiration",
			"Dexterity Affliction",
			"Intellect Inspiration",
			"Intellect Affliction",
			"Might Inspiration",
			"Might Affliction",
			"Perception Inspiration", // 10
			"Perception Affliction",
			"Mind Inspiration",
			"Mind Affliction",
			"Body Inspiration",
			"Body Affliction",
			
			// de_patch
			"Verfassungsinspiration",
			"Verfassungswirkung",
			"Entschlossenheitsinspiration",
			"Entschlossenheitswirkung",
			"Gewandtheitsinspiration",
			"Gewandtheitswirkung",
			"Intellektinspiration",
			"Intellektwirkung",
			"Machtinspiration",
			"Machtwirkung", 
			"Wahrnehmungsinspiration",
			"Wahrnehmungswirkung",
			"Geistesinspiration",
			"Geisteswirkung",
			"Körperinspiration",
			"Körperwirkung",

			// fr
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			
			// it
			"Ispirazioni della Costituzione",
            "Alterazioni della Costituzione",
            "Ispirazioni della Risolutezza",
            "Alterazioni della Risolutezza",
            "Ispirazioni della Destrezza",
            "Alterazioni della Destrezza",
            "Ispirazioni dell'Acume",
            "Alterazioni dell'Acume",
            "Ispirazioni del Vigore",
            "Alterazioni del Vigore",
            "Ispirazioni della Percezione",
            "Alterazioni della Percezione",
            "Ispirazioni della Mente",
            "Alterazioni della Mente",
            "Ispirazioni del Corpo",
            "Alterazioni del Corpo",
	};
	

	
	public Entry addAfflictionsTitle(String fileName, String targetLanguage) {
		if(fileName.contains("cyclopedia")) {
			Entry result = new Entry();
			result.ID = this.ID;
			int language; 
			
			switch (targetLanguage) {
				case "en": language = 16 * 0;
					break;
				case "de_patch": language = 16 * 1;
					break;
				case "fr": language = 16 * 2;
					break;
				case "it": language = 16 * 3;
					break;
				default : language = 16 * 0;
			}
			
			
			if(this.ID == 94 || this.ID == 322 || this.ID == 681) {
				result.DefaultText = "<line-height=100%><voffset=0.5em><size=95%><link=\"glossary://GlossaryEntry_Afflictions_Perception\"><#dac79c>" + manualAfflictionsTitleInsertWords[11 + language] + "</color></link></size></voffset>\n" + DefaultText;
				result.FemaleText = this.FemaleText;
				return result;
			}
			if(this.ID == 96 || this.ID == 98 || this.ID == 102) {
				result.DefaultText = "<line-height=100%><voffset=0.5em><size=95%><link=\"glossary://GlossaryEntry_Afflictions_Intellect\"><#dac79c>" + manualAfflictionsTitleInsertWords[7 + language] + "</color></link></size></voffset>\n" + DefaultText;
				result.FemaleText = this.FemaleText;
				return result;
			}
			if(this.ID == 100 || this.ID == 120 || this.ID == 683) {
				result.DefaultText = "<line-height=100%><voffset=0.5em><size=95%><link=\"glossary://GlossaryEntry_Afflictions_Might\"><#dac79c>" + manualAfflictionsTitleInsertWords[9 + language] + "</color></link></size></voffset>\n" + DefaultText;
				result.FemaleText = this.FemaleText;
				return result;
			}
			if(this.ID == 106 || this.ID == 122 || this.ID == 533) {
				result.DefaultText = "<line-height=100%><voffset=0.5em><size=95%><link=\"glossary://GlossaryEntry_Afflictions_Resolve\"><#dac79c>" + manualAfflictionsTitleInsertWords[3 + language] + "</color></link></size></voffset>\n" + DefaultText;
				result.FemaleText = this.FemaleText;
				return result;
			}
			if(this.ID == 108 || this.ID == 110 || this.ID == 118 || this.ID == 112) { // 112 = Petrified
				result.DefaultText = "<line-height=100%><voffset=0.5em><size=95%><link=\"glossary://GlossaryEntry_Afflictions_Dexterity\"><#dac79c>" + manualAfflictionsTitleInsertWords[5 + language] + "</color></link></size></voffset>\n" + DefaultText;
				result.FemaleText = this.FemaleText;
				return result;
			}
			if(this.ID == 116 || this.ID == 124 || this.ID == 679) {
				result.DefaultText = "<line-height=100%><voffset=0.5em><size=95%><link=\"glossary://GlossaryEntry_Afflictions_Constitution\"><#dac79c>" + manualAfflictionsTitleInsertWords[1 + language] + "</color></link></size></voffset>\n" + DefaultText;
				result.FemaleText = this.FemaleText;
				return result;
			}

		}
		return this;
	}
	
	final static String[] recepiesGroupMarkers = new String[] {
		// All
		"<font=\"EspinosaNova-Ornaments SDF\">a</font>", // https://samples.myfonts.net/a_92/u/b1/4d6eeef366c04e2b5f7cb3b8b975b2.gif
		"<font=\"EspinosaNova-Ornaments SDF\">c</font>", // https://samples.myfonts.net/a_92/u/1a/006d84710296eaee92af57479c4922.gif
	};
	
	public Entry addRecepieGroups(String englishBase) {
		Entry result = new Entry();
		result.ID = this.ID;
		result.DefaultText = DefaultText;
		result.FemaleText = FemaleText;
		
		String[] targetWords = new String[] {
			"<space=0.8em><#FF9800><size=110%><font=\"EspinosaNova-Ornaments SDF\">a</font></size></color></line-height>",
			"<space=0.8em><#00bcd4><size=110%><font=\"EspinosaNova-Ornaments SDF\">c</font></size></color></line-height>"
		};
		
		if(englishBase.contains(recepiesGroupMarkers[0])) {
			result.DefaultText = "<line-height=100>" + result.DefaultText + targetWords[0];
		}
		
		if(englishBase.contains(recepiesGroupMarkers[1])) {
			result.DefaultText = "<line-height=100>" + result.DefaultText + targetWords[1];
		}	
		
		return result;
	}

	public Entry stripMarkup(String fileName) {
		if(fileName.contains("cyclopedia")) {
			if(this.ID == 94 || this.ID == 322 || this.ID == 681 || this.ID == 96 || this.ID == 98 || this.ID == 102 || this.ID == 100 || this.ID == 120 || this.ID == 683 || this.ID == 106 || this.ID == 122 || this.ID == 533 || this.ID == 108 || this.ID == 110 || this.ID == 118 /*|| this.ID == 112 */ || this.ID == 116 || this.ID == 124 || this.ID == 679) {
				if(DefaultText.indexOf("\n") > 0) {
					this.DefaultText = this.DefaultText.substring(DefaultText.indexOf("\n"));
				}
			}
		}
		this.DefaultText = this.DefaultText.replaceAll("<size=1?[18]0%>.*?</size>", "");
		this.DefaultText = this.DefaultText.replaceAll("<(?!\\/?xg).*?>", "");
		this.DefaultText = this.DefaultText.replaceAll("[¹²³₁₂₃]", "");
		this.DefaultText = this.DefaultText.replaceAll(" ", " "); // NB Space gegen normales
		this.DefaultText = this.DefaultText.replaceAll("  ", " "); // 2 space gegen 1
		this.FemaleText = this.FemaleText.replaceAll("<size=1?[18]0%>[123]</size>", "");
		this.FemaleText = this.FemaleText.replaceAll("<(?!\\/?xg).*?>", "");
		this.FemaleText = this.FemaleText.replaceAll("[¹²³₁₂₃]", "");
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