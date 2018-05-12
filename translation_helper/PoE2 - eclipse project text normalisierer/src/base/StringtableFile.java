package base;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="StringTableFile")
@XmlAccessorType (XmlAccessType.FIELD)
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
}

@XmlRootElement(name="Entry")
@XmlAccessorType (XmlAccessType.FIELD)
class Entry {
	private int ID;
	private String DefaultText;
	private String FemaleText;
	
	
	public Pair<Long, Long> cleanAnführungszeichen() {
		long unbalanciert = 0, tausch = 0;
		long count = DefaultText.chars().filter(ch -> ch =='"').count();
		if(count % 2 == 1 && !DefaultText.contains("=")) {
			String[] parts= DefaultText.split("\n");
			for (int i = 0; i < parts.length; i++) {
				if(parts[i].startsWith("\"") && parts[i].chars().filter(ch -> ch =='"').count() == 1) {
					parts[i] += "\"";
					unbalanciert++;					
				}
			}
			DefaultText = String.join("\n", parts);
		}
		
		while(DefaultText.contains("\"") && !DefaultText.contains("=")) {
			DefaultText = DefaultText.replaceFirst("\"", "„").replaceFirst("\"", "“");
			tausch++;
		}
		
		
		count = FemaleText.chars().filter(ch -> ch =='"').count();
		if(count % 2 == 1 && !FemaleText.contains("=")) {
			String[] parts= FemaleText.split("\n");
			for (int i = 0; i < parts.length; i++) {
				if(parts[i].startsWith("\"") && parts[i].chars().filter(ch -> ch =='"').count() == 1) {
					parts[i] += "\"";
					unbalanciert++;
				}
			}
			FemaleText = String.join("\n", parts);
		}
		while(FemaleText.contains("\"") && !FemaleText.contains("=")) {
			FemaleText = FemaleText.replaceFirst("\"", "„").replaceFirst("\"", "“");
			tausch++;
		}
		
		return new Pair<Long, Long>(unbalanciert, tausch);
	}
	
	public Pair<Long, Long> repairTags(){
		long tokenFehler = 0, tausch = 0;
		if(DefaultText.contains("=„")) {
			int i = 0;
			i = DefaultText.indexOf("link=", 0);
			if(i>0)DefaultText = DefaultText.substring(0, i+5) + DefaultText.substring(i+5).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = DefaultText.indexOf("color=", 0);
			if(i>0)DefaultText = DefaultText.substring(0, i+6) + DefaultText.substring(i+6).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = DefaultText.indexOf("sprite=", 0);
			if(i>0)DefaultText = DefaultText.substring(0, i+6) + DefaultText.substring(i+6).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = DefaultText.indexOf("name=", 0);
			if(i>0)DefaultText = DefaultText.substring(0, i+5) + DefaultText.substring(i+5).replaceFirst("„", "\"").replaceFirst("“", "\"");

			
			tokenFehler++;
		}
		
		
		if(FemaleText.contains("=")) {
			int i = 0;
			i = FemaleText.indexOf("link=", 0);
			if(i>0)FemaleText = FemaleText.substring(0, i+5) + FemaleText.substring(i+5).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = FemaleText.indexOf("color=", 0);
			if(i>0)FemaleText = FemaleText.substring(0, i+6) + FemaleText.substring(i+6).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = FemaleText.indexOf("sprite=", 0);
			if(i>0)FemaleText = FemaleText.substring(0, i+6) + FemaleText.substring(i+6).replaceFirst("„", "\"").replaceFirst("“", "\"");
			i = FemaleText.indexOf("name=", 0);
			if(i>0)FemaleText = FemaleText.substring(0, i+5) + FemaleText.substring(i+5).replaceFirst("„", "\"").replaceFirst("“", "\"");

			tokenFehler++;
		}
		return new Pair<Long, Long>(tokenFehler, tausch);
	}
	
	
	public Pair<Long, Long> cleanPunkte() {
		long ellipsen = 0, satzzeichen = 0;
		while(DefaultText.contains("...")) {
			DefaultText = DefaultText.replaceFirst("\\.\\.\\.", "…");
			ellipsen++;
		}
		while(DefaultText.contains("..")) {
			DefaultText = DefaultText.replaceFirst("\\.\\.", ".");
			satzzeichen++;
		}
		
		while(FemaleText.contains("...")) {
			FemaleText = FemaleText.replaceFirst("\\.\\.\\.", "…");
			satzzeichen++;
		}
		while(FemaleText.contains("..")) {
			FemaleText = FemaleText.replaceFirst("\\.\\.", ".");
			satzzeichen++;
		}
		
		return new Pair<Long, Long>(ellipsen, satzzeichen);
	}
	
	public Pair<Long, Long> cleanLeerzeichen() {
		long leerzeichen = 0, satzzeichen = 0;
		while(DefaultText.contains("  ")) {
			DefaultText = DefaultText.replaceFirst("  ", " ");
			leerzeichen++;
		}
		while(DefaultText.contains(" .")) {
			DefaultText = DefaultText.replaceFirst(" \\.", ". ");
			satzzeichen++;
		}
		while(DefaultText.contains(" !")) {
			DefaultText = DefaultText.replaceFirst(" !", "! ");
			satzzeichen++;
		}
		while(DefaultText.contains(" ?")) {
			DefaultText = DefaultText.replaceFirst(" \\?", "? ");
			satzzeichen++;
		}
		while(DefaultText.contains(" ,")) {
			DefaultText = DefaultText.replaceFirst(" ,", ", ");
			satzzeichen++;
		}
		while(DefaultText.contains(" :")) {
			DefaultText = DefaultText.replaceFirst(" :", ": ");
			satzzeichen++;
		}
		while(DefaultText.contains(" “")) {
			DefaultText = DefaultText.replaceFirst(" “", "“ ");
			satzzeichen++;
		}
		
		while(FemaleText.contains("  ")) {
			FemaleText = FemaleText.replaceFirst("  ", " ");
			leerzeichen++;
		}
		while(FemaleText.contains(" .")) {
			FemaleText = FemaleText.replaceFirst(" \\.", ". ");
			satzzeichen++;
		}
		while(FemaleText.contains(" !")) {
			FemaleText = FemaleText.replaceFirst(" !", "! ");
			satzzeichen++;
		}
		while(FemaleText.contains(" ?")) {
			FemaleText = FemaleText.replaceFirst(" \\?", "? ");
			satzzeichen++;
		}
		while(FemaleText.contains(" ,")) {
			FemaleText = FemaleText.replaceFirst(" ,", ", ");
			satzzeichen++;
		}
		while(FemaleText.contains(" :")) {
			FemaleText = FemaleText.replaceFirst(" :", ": ");
			satzzeichen++;
		}
		while(FemaleText.contains(" “")) {
			FemaleText = FemaleText.replaceFirst(" “", "“ ");
			satzzeichen++;
		}
		
		return new Pair<Long, Long>(leerzeichen, satzzeichen);
	}
	
	
	// mind. 2 heißt es ist keine Auslassung, meistens jedenfalls
	public Pair<Long,Long> cleanApostroph() {
		long apostroph = 0, keineSchachtel = 0;
/*		while(DefaultText.chars().filter(ch -> ch == '\'').count() >= 2) {
			DefaultText = DefaultText.replaceFirst("\'", "‚").replaceFirst("\'", "‘");
			apostroph++;
		}
		while(FemaleText.chars().filter(ch -> ch == '\'').count() >= 2) {
			FemaleText = FemaleText.replaceFirst("\'", "‚").replaceFirst("\'", "‘");
			apostroph++;
		}
*/		
		while(DefaultText.contains("‚") && DefaultText.contains("‘") && !DefaultText.contains("„") && !DefaultText.contains("“")) {
			DefaultText = DefaultText.replaceFirst("‚", "„").replaceFirst("‘", "“");
			keineSchachtel++;
		}
		
		while(FemaleText.contains("‚") && FemaleText.contains("‘") && !FemaleText.contains("„") && !FemaleText.contains("“")) {
			FemaleText = FemaleText.replaceFirst("‚", "„").replaceFirst("‘", "“");
			keineSchachtel++;
		}
		
		return new Pair<Long, Long>(apostroph, keineSchachtel);
	}

	public Pair<Long,Long> cleanBindestrich() {
		long bindestrich = 0;
		while(DefaultText.contains(" - ")) {
			DefaultText = DefaultText.replaceFirst("-", "–");
			bindestrich++;
		}
		while(FemaleText.contains(" - ")) {
			FemaleText = FemaleText.replaceFirst("-", "–");
			bindestrich++;
		}
		
		return new Pair<Long, Long>(bindestrich, 0L);
	}
	
	
	public Pair<Long, Long> tagBalanceCheck1() {
		long eckige = 0, runde = 0;
		
		if(DefaultText.chars().filter(ch -> ch =='(').count()  != DefaultText.chars().filter(ch -> ch ==')').count()) {
			runde++;
		}
		if(DefaultText.chars().filter(ch -> ch =='[').count()  != DefaultText.chars().filter(ch -> ch ==']').count()) {
			eckige++;
		}
		
		if(FemaleText.chars().filter(ch -> ch =='(').count()  != FemaleText.chars().filter(ch -> ch ==')').count()) {
			runde++;
		}
		if(FemaleText.chars().filter(ch -> ch =='[').count()  != FemaleText.chars().filter(ch -> ch ==']').count()) {
			eckige++;
		}
		
		return new Pair<Long, Long>(eckige, runde);
	}
	
	public Pair<Long, Long> tagBalanceCheck2() {
		long geschweift = 0, html = 0;
		
		if(DefaultText.chars().filter(ch -> ch =='{').count()  != DefaultText.chars().filter(ch -> ch =='}').count()) {
			geschweift++;
		}
		if(DefaultText.split("&lt;", -1).length-1 != DefaultText.split("&gt;", -1).length-1) {
			html++;
		}
		
		if(FemaleText.chars().filter(ch -> ch =='{').count()  != FemaleText.chars().filter(ch -> ch =='}').count()) {
			geschweift++;
		}
		if(FemaleText.split("&lt;", -1).length-1 != FemaleText.split("&gt;", -1).length-1) {
			html++;
		}
		
		return new Pair<Long, Long>(geschweift, html);
	}
	
	public Pair<Long, Long> tagBalanceCheck3() {
		long eckige = 0, runde = 0;
		
		if(DefaultText.chars().filter(ch -> ch =='„').count()  != DefaultText.chars().filter(ch -> ch =='“').count()) {
			runde++;
		}
		if(DefaultText.chars().filter(ch -> ch =='‚').count()  != DefaultText.chars().filter(ch -> ch =='‘').count()) {
			eckige++;
		}
		
		if(FemaleText.chars().filter(ch -> ch =='„').count()  != FemaleText.chars().filter(ch -> ch =='“').count()) {
			runde++;
		}
		if(FemaleText.chars().filter(ch -> ch =='‚').count()  != FemaleText.chars().filter(ch -> ch =='‘').count()) {
			eckige++;
		}
		
		return new Pair<Long, Long>(eckige, runde);
	}
}

class Pair<A,B>{
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
		
	}
	public A a;
	public B b;
}