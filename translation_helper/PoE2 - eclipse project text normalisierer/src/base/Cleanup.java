package base;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Einfacher Textprüfer für Pillars of Eternity Stringtable Dateien. Könnte man teilweise in einen Commit Hook wandeln.
 * 
 * @author xar
 *
 */



public class Cleanup {
	
	JFrame cleanupWindow;
	
	/** The repo of the german patch, needed because it contains the xsd and the texts for de_patch */
	final static String baseFolderDeFiles = "C:\\Repositories\\pillarsofeternity-2-german-patch";
	
	/** The repo of enhanced user interfaces, contains all other languages which we have changed files for */
	final static String baseFolderEnhanchedUiRepo = "C:\\Repositories\\pillarsofeternity-2-Enhanced-Ui\\PoE2-EnhancedUserInterface";
	
	/** The game directory, for any language whe have nothing handcrafted */
	final static String baseGameFolder = "E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data";

	public static void main(String[] args) throws JAXBException, SAXException, IOException {
		File stringtableSchema = new File(baseFolderDeFiles + "\\translation_helper\\stringtable.xsd");
		JAXBContext jaxbContext = JAXBContext.newInstance(StringTableFile.class);
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(stringtableSchema); 

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		
	    Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		List<Path> filesInFolder = Files.walk(Paths.get(baseFolderDeFiles + "\\exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		filesInFolder.addAll(Files.walk(Paths.get(baseFolderDeFiles + "\\laxa_exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		filesInFolder.addAll(Files.walk(Paths.get(baseFolderDeFiles + "\\laxb_exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		filesInFolder.addAll(Files.walk(Paths.get(baseFolderDeFiles + "\\laxc_exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		filesInFolder.addAll(Files.walk(Paths.get(baseFolderDeFiles + "\\laxd_exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		
		List<Path> compareFilesInFolder = Files.walk(Paths.get(baseGameFolder + "\\exported\\localized\\de\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		compareFilesInFolder.addAll(Files.walk(Paths.get(baseGameFolder + "\\laxa_exported\\localized\\de\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		compareFilesInFolder.addAll(Files.walk(Paths.get(baseGameFolder + "\\laxb_exported\\localized\\de\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		compareFilesInFolder.addAll(Files.walk(Paths.get(baseGameFolder + "\\laxc_exported\\localized\\de\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		compareFilesInFolder.addAll(Files.walk(Paths.get(baseGameFolder + "\\laxd_exported\\localized\\de\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		
		
		List<Path> compareFilesENInFolder = Files.walk(Paths.get(baseGameFolder + "\\exported\\localized\\en\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		compareFilesENInFolder.addAll(Files.walk(Paths.get(baseGameFolder + "\\laxa_exported\\localized\\en\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		compareFilesENInFolder.addAll(Files.walk(Paths.get(baseGameFolder + "\\laxb_exported\\localized\\en\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		compareFilesENInFolder.addAll(Files.walk(Paths.get(baseGameFolder + "\\laxc_exported\\localized\\en\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		compareFilesENInFolder.addAll(Files.walk(Paths.get(baseGameFolder + "\\laxd_exported\\localized\\en\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		
		
		cleanupPart(jaxbUnmarshaller, marshaller, filesInFolder, compareFilesInFolder, compareFilesENInFolder);
		
		Cleanup cleanup = new Cleanup();
		
		// Base files, the handpicked stuff from Spherikal
		List<Path> convertingBase = Files.walk(Paths.get(baseFolderEnhanchedUiRepo + "\\localized\\en\\text")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		
		cleanup.stripMarkup(convertingBase, jaxbUnmarshaller,marshaller, "en_fixed");
		
		// Trying to get the same via the regex from the english base
		filesInFolder = Files.walk(Paths.get(baseFolderEnhanchedUiRepo + "\\localized\\en_fixed\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.formatText(convertingBase, filesInFolder, "en", jaxbUnmarshaller, marshaller);
		
		filesInFolder = Files.walk(Paths.get(baseFolderDeFiles + "\\exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.formatText(convertingBase, filesInFolder, "de_patch", jaxbUnmarshaller, marshaller);
		//cleanup.splitupText(convertingBase, filesInFolder, "de_patch", jaxbUnmarshaller, marshaller);
		
		filesInFolder = Files.walk(Paths.get(baseGameFolder + "\\exported\\localized\\fr\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		//cleanup.colorupdate(convertingBase, filesInFolder, "fr", jaxbUnmarshaller,marshaller);
		
		// It fix von Kilay
		filesInFolder = Files.walk(Paths.get(baseFolderEnhanchedUiRepo + "\\localized\\it\\text\\game")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		//cleanup.stripMarkup(filesInFolder, jaxbUnmarshaller,marshaller, "it_fixed");
		
		filesInFolder = Files.walk(Paths.get(baseFolderEnhanchedUiRepo + "\\localized\\it_fixed\\text\\game")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.formatText(convertingBase, filesInFolder, "it", jaxbUnmarshaller, marshaller);
		
		filesInFolder = Files.walk(Paths.get(baseGameFolder + "\\exported\\localized\\es\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		//cleanup.colorupdate(convertingBase, filesInFolder, "es", jaxbUnmarshaller,marshaller);
		
		filesInFolder = Files.walk(Paths.get(baseGameFolder + "\\exported\\localized\\pt\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		//cleanup.colorupdate(convertingBase, filesInFolder, "pt", jaxbUnmarshaller,marshaller);
		
		filesInFolder = Files.walk(Paths.get(baseGameFolder + "\\exported\\localized\\pl\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
 		//cleanup.colorupdate(convertingBase, filesInFolder, "pl", jaxbUnmarshaller,marshaller);
		
		// Ru fix von Phenomenum
		filesInFolder = Files.walk(Paths.get(baseFolderEnhanchedUiRepo + "\\localized\\ru\\text\\game")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		//cleanup.colorupdate(convertingBase, filesInFolder, "ru2", jaxbUnmarshaller,marshaller);
		
		filesInFolder = Files.walk(Paths.get(baseGameFolder + "\\exported\\localized\\zh\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		//cleanup.colorupdate(convertingBase, filesInFolder, "zh", jaxbUnmarshaller,marshaller);
		
		
		//cleanup.cleanupWindow = new JFrame();
	}
	






	private static void cleanupPart(Unmarshaller jaxbUnmarshaller, Marshaller m, List<Path> filesInFolder,
			List<Path> compareFilesInFolder, List<Path> compareFilesENInFolder) throws JAXBException, IOException {
		Pair<Long, Long> count = new Pair<>(0L,0L);
		Pair<Long, Long> countPunkte = new Pair<>(0L,0L);
		Pair<Long, Long> countLeerzeichen = new Pair<>(0L,0L);
		Pair<Long,Long> countTags = new Pair<>(0L, 0L);
		Pair<Long,Long> balance1 = new Pair<>(0L, 0L);
		Pair<Long,Long> balance2 = new Pair<>(0L, 0L);
		Pair<Long,Long> balance3 = new Pair<>(0L, 0L);
		Pair<Long,Long> apostrophe = new Pair<>(0L, 0L);
		Pair<Long,Long> bindestrich = new Pair<>(0L, 0L);
		Pair<Long,Long> unwanted = new Pair<>(0L, 0L);
		
		Set<String> allTags = new TreeSet<>();
		Set<Character> counter = new TreeSet<>();
		Map<String, Integer> wordCount = new TreeMap<>();
		ListIterator<Path> compareFilesIterator = compareFilesInFolder.listIterator();
		ListIterator<Path> compareFilesENIterator = compareFilesENInFolder.listIterator();
		for (Path path : filesInFolder) {
			Path compareFile = compareFilesIterator.next();
			Path compareENFile = compareFilesENIterator.next();
			
			StringTableFile stringtable = (StringTableFile) jaxbUnmarshaller.unmarshal( path.toFile() );
	//		StringTableFile compareStringtable = (StringTableFile) jaxbUnmarshaller.unmarshal( compareFile.toFile() );
			StringTableFile compareENStringtable = (StringTableFile) jaxbUnmarshaller.unmarshal( compareENFile.toFile() );
	//		if(stringtable.getEntries().size() != compareStringtable.getEntries().size() || stringtable.getEntries().size() != compareENStringtable.getEntries().size()) {
	//			System.out.println(compareFile.toString());
	//			System.out.println(stringtable.getEntries().size() + " <-> " + compareStringtable.getEntries().size() + " <-> " + compareENStringtable.getEntries().size());
	//		}
			
			
			
			
			for(Entry entry : stringtable.getEntries())
			{
				Pair<Long, Long> count2 = entry.cleanAnführungszeichen();
				count.a += count2.a;
				count.b += count2.b;
				
				count2 = entry.cleanPunkte();
				countPunkte.a += count2.a;
				countPunkte.b += count2.b;
				
				count2 = entry.cleanLeerzeichen();
				countLeerzeichen.a += count2.a;
				countLeerzeichen.b += count2.b;
				
				count2 = entry.repairTags();
				countTags.a += count2.a;
				
				count2 = entry.tagBalanceCheck1();
				balance1.a += count2.a;
				balance1.b += count2.b;
				
				count2 = entry.tagBalanceCheck2();
				balance2.a += count2.a;
				balance2.b += count2.b;
				
				count2 = entry.tagBalanceCheck3();
				balance3.a += count2.a;
				balance3.b += count2.b;
				
				// erst teil a, dann teil b
				count2 = entry.cleanApostroph();
				apostrophe.a += count2.a;
				apostrophe.b += count2.b;
				
				count2 = entry.cleanBindestrich();
				bindestrich.a += count2.a;
				
				count2 = entry.unwanted();
				unwanted.a += count2.a;
				//		entry.DocuHelper();
				
		//		entry.compareTags2();
		//		allTags.addAll(entry.tagViewHelper());
				
		//		counter.addAll(entry.counter());
				
		//		for (java.util.Map.Entry<String, Integer> elem : entry.wordCount().entrySet()) {
				//	wordCount.merge(elem.getKey(), elem.getValue(), (x, y) -> x + y); 
		//		}
				
				
			}
			
			ListIterator<Entry> enIterator = compareENStringtable.getEntries().listIterator();
			for(Entry entry : stringtable.getEntries())
			{
//				entry.compareTags(enIterator.next());
				if(entry.getID() > 80000) continue; // die für den Enzyclopädie fix
				if(enIterator.hasNext()) { // Uneven Patch levels
					entry.compareIsTranslated(enIterator.next());
				}
			}
		    m.marshal(stringtable, path.toFile());
			
		    List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8")); 
			lines.set(0, lines.get(0).replaceAll("encoding=\"UTF-8\" standalone=\\\"yes\\\"", "encoding=\"utf-8\""));
			lines.set(1, lines.get(1).replaceAll("xmlns:xs=", "xmlns:xsd="));
			List<String> modified = lines.stream().map(x -> {return x.replaceAll("    ", "  ").replaceFirst("<DefaultText></DefaultText>", "<DefaultText />").replaceFirst("<FemaleText></FemaleText>", "<FemaleText />");}).collect(Collectors.toList());
			Files.write(path, modified);
		}	
		System.out.println("done");
		System.out.println("Unbalancierte \": " + count.a);
		System.out.println("Getauschte \": " + count.b);
		System.out.println("Ellipsen …: " + countPunkte.a);
		System.out.println("Doppelte Punkte ..: " + countPunkte.b);
		System.out.println("Doppelte  : " + countLeerzeichen.a);
		System.out.println("Leerzeichen vor Satzzeichen : " + countLeerzeichen.b);
		System.out.println("Tags wieder hergestellt: " + countTags.a);
		
		System.out.println("Balancierungsprobleme");
		System.out.println("( ): " + balance1.a);
		System.out.println("[ ]: " + balance1.b);
		System.out.println("{ }: " + balance2.a);
		System.out.println("< > 10 davon sind wirkliche größer als Zeichen, 38 Sprites, die keinen Endtag haben: " + balance2.b); 
		System.out.println("„ “: " + balance3.a);
		System.out.println("‚ ‘: " + balance3.b);
		System.out.println("Übrige ': " + unwanted.a);
		System.out.println("Getauschte ': " + apostrophe.a);
		System.out.println("Getauschte ‚‘ gegen „“: " + apostrophe.b);
		System.out.println("Getauschte -: " + bindestrich.a);
		
		
		System.out.println("Tags");
		System.out.println(String.join("\n", allTags));
		for (Character integer : counter) {
			System.out.println(String.valueOf(integer));
		}
		
		System.out.println("Wordcount ");
		for (java.util.Map.Entry<String, Integer> word : wordCount.entrySet()) {
			System.out.println(String.format("%05d %s", word.getValue(), word.getKey()));
		}
	}
	
	

	private void stripMarkup(List<Path> convertingBase, Unmarshaller jaxbUnmarshaller, Marshaller marshaller, String languageFolder) throws JAXBException, IOException {
		for (Path colorful : convertingBase) {
			StringTableFile colorfulTexts = (StringTableFile) jaxbUnmarshaller.unmarshal( colorful.toFile() );
			StringTableFile outputFile = new StringTableFile();
			outputFile.setName(colorfulTexts.getName());
			outputFile.setNextEntryID(colorfulTexts.getNextEntryID());
			outputFile.setEntryCount(colorfulTexts.getEntryCount());
			
			for(Entry colorfulEntry : colorfulTexts.getEntries())
			{
				Entry cleanEntry = colorfulEntry.stripMarkup();
				outputFile.getEntries().add(cleanEntry);
			}
			
			// Clean
			File file = new File(baseFolderEnhanchedUiRepo + "\\localized\\" + languageFolder + "\\text\\game\\" + colorful.getFileName());
			Path path;
			List<String> lines;
			List<String> modified;
			saveFile(marshaller, outputFile, file);
		}
	}

	
	public void formatText(List<Path> convertingBase, List<Path> orginalLanguage, String language, Unmarshaller jaxbUnmarshaller, Marshaller marshaller) throws JAXBException, IOException {
		for(int i = 1; i <= 4; i++) {
			for (Path colorful : convertingBase) {
				for (Path plain : orginalLanguage) {
					if(colorful.getFileName().equals(plain.getFileName())) {
						StringTableFile colorfulTexts = (StringTableFile) jaxbUnmarshaller.unmarshal( colorful.toFile() );
						StringTableFile plainTexts = (StringTableFile) jaxbUnmarshaller.unmarshal( plain.toFile() );
						StringTableFile changedStrings = new StringTableFile();
						changedStrings.setName(plainTexts.getName());
						changedStrings.setNextEntryID(plainTexts.getNextEntryID());
						changedStrings.setEntryCount(plainTexts.getEntryCount());
						for(Entry colorfulEntry : colorfulTexts.getEntries()) {
							for (Entry plainEntry : plainTexts.getEntries()) {
								if(colorfulEntry.getID() == plainEntry.getID()) {
									Entry formattedEntry = null;
									if(i == 1 || i == 2 || i == 3) {
										formattedEntry = formatLevel(language, plainEntry, (x) -> plainEntry.replaceAfflictionWithColor(x), (x,y) -> x.replaceGenericAfflictionsWithIcon(y)); // 1, 2, 3 
									}
									if(i == 1 || i == 2 || i == 4) {
										formattedEntry = formatLevel(language, plainEntry, (x) -> plainEntry.replaceDamageTypeWithIcon(x), (x,y) -> x.replaceDefenseWithIcon(y)); // 1, 2, 4
									}
									final Entry formattedEntry2 = formattedEntry;	
									if(i == 1 || i == 2 || i == 3) {
										formattedEntry = formatLevel(language, formattedEntry2, (x) -> formattedEntry2.replaceAfflictionWithColor(x), (x,y) -> x.replaceGenericAfflictionsWithIcon(y)); // 1, 2, 3 
									}
									if(i == 1) {
										formattedEntry = formattedEntry.addRecepieGroups(colorfulEntry.getDefaultText()); // 1
										formattedEntry = formattedEntry.addReputation(plain.getFileName().toString()); // 1
									}
									
									if(formattedEntry != null && !formattedEntry.getDefaultText().equals(plainEntry.getDefaultText())) {
										changedStrings.getEntries().add(formattedEntry);
									} else if(i == 1) {
										System.out.println("Unchanged ID: " + plainEntry.getID() + "\n" + plainEntry.getDefaultText());
									}
									
								}
							}
						}
						File file = new File(baseFolderEnhanchedUiRepo + "_" + i + "\\localized\\" + language + "\\text\\game\\" + plain.getFileName());
						saveFile(marshaller, changedStrings, file);				
					}
				}
			}
		}
	}
	
	public void splitupText(List<Path> convertingBase, List<Path> orginalLanguage, String language, Unmarshaller jaxbUnmarshaller, Marshaller marshaller) throws JAXBException, IOException {
		for(int i = 1; i <= 4; i++) {
			for (Path colorful : convertingBase) {
				for (Path plain : orginalLanguage) {
					if(colorful.getFileName().equals(plain.getFileName())) {
						StringTableFile colorfulTexts = (StringTableFile) jaxbUnmarshaller.unmarshal( colorful.toFile() );
						StringTableFile plainTexts = (StringTableFile) jaxbUnmarshaller.unmarshal( plain.toFile() );
						StringTableFile changedStrings = new StringTableFile();
						changedStrings.setName(plainTexts.getName());
						changedStrings.setNextEntryID(plainTexts.getNextEntryID());
						changedStrings.setEntryCount(plainTexts.getEntryCount());
						for(Entry colorfulEntry : colorfulTexts.getEntries()) {
							for (Entry plainEntry : plainTexts.getEntries()) {
								if(colorfulEntry.getID() == plainEntry.getID()) {
									Entry formattedEntry = null;
									if(i == 1 || i == 2 || i == 3) {
										formattedEntry = plainEntry; // 1, 2, 3 
									}
									if(i == 1 || i == 2 || i == 4) {
										formattedEntry = plainEntry; // 1, 2, 4
									}
									final Entry formattedEntry2 = formattedEntry;	
									if(i == 1 || i == 2 || i == 3) {
										formattedEntry = plainEntry; // 1, 2, 3 
									}
									if(i == 1) {
										formattedEntry = plainEntry; // 1
										//formattedEntry = plainEntry // 1
									}
									
									if(formattedEntry != null) {
										changedStrings.getEntries().add(formattedEntry);
									}
									
								}
							}
						}
						File file = new File(baseFolderEnhanchedUiRepo + "_" + i + "\\localized\\" + language + "\\text\\game\\" + plain.getFileName());
						saveFile(marshaller, changedStrings, file);				
					}
				}
			}
		}
	}


	private Entry formatLevel(String language, Entry plainEntry, Function<String, Entry> firstFunction, BiFunction<Entry, String, Entry> secondFunction) {
		Entry firstChange = firstFunction.apply(language);
		Entry secondChange;
		if(firstChange != null) {
			secondChange = secondFunction.apply(firstChange, language);
			return secondChange  != null ? secondChange : firstChange;
		} else {
			secondChange = secondFunction.apply(plainEntry, language);
			if(secondChange != null)
				return secondChange;
		}
		return plainEntry;
	}
	


	private void saveFile(Marshaller marshaller, StringTableFile outputFile, File file)
			throws IOException, JAXBException {
		file.getParentFile().mkdirs();
		file.createNewFile();
		Path path = Paths.get(file.getPath());
		marshaller.marshal(outputFile, file);
		
		List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8")); 
		lines.set(0, lines.get(0).replaceAll("encoding=\"UTF-8\" standalone=\\\"yes\\\"", "encoding=\"utf-8\""));
		lines.set(1, lines.get(1).replaceAll("xmlns:xs=", "xmlns:xsd="));
		List<String> modified = lines.stream().map(x -> {return x.replaceAll("    ", "  ").replaceFirst("<DefaultText></DefaultText>", "<DefaultText />").replaceFirst("<FemaleText></FemaleText>", "<FemaleText />");}).collect(Collectors.toList());
		Files.write(path, modified);
	}

}
