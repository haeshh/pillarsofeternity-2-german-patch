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

	public static void main(String[] args) throws JAXBException, SAXException, IOException {
		File stringtableSchema = new File("C:\\Repositories\\pillarsofeternity-2-german-patch\\translation_helper\\stringtable.xsd");
		JAXBContext jaxbContext = JAXBContext.newInstance(StringTableFile.class);
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(stringtableSchema); 

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		
	    Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		List<Path> filesInFolder = Files.walk(Paths.get("C:\\Repositories\\pillarsofeternity-2-german-patch\\exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		filesInFolder.addAll(Files.walk(Paths.get("C:\\Repositories\\pillarsofeternity-2-german-patch\\laxa_exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		filesInFolder.addAll(Files.walk(Paths.get("C:\\Repositories\\pillarsofeternity-2-german-patch\\laxb_exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		filesInFolder.addAll(Files.walk(Paths.get("C:\\Repositories\\pillarsofeternity-2-german-patch\\laxc_exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		
		List<Path> compareFilesInFolder = Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\exported\\localized\\de\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		compareFilesInFolder.addAll(Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\laxa_exported\\localized\\de\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		compareFilesInFolder.addAll(Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\laxb_exported\\localized\\de\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		compareFilesInFolder.addAll(Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\laxc_exported\\localized\\de\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		
		
		List<Path> compareFilesENInFolder = Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\exported\\localized\\en\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		compareFilesENInFolder.addAll(Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\laxa_exported\\localized\\en\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		compareFilesENInFolder.addAll(Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\laxb_exported\\localized\\en\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		compareFilesENInFolder.addAll(Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\laxc_exported\\localized\\en\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList()));
		
		
		//cleanupPart(jaxbUnmarshaller, marshaller, filesInFolder, compareFilesInFolder, compareFilesENInFolder);
		
		Cleanup cleanup = new Cleanup();
		
		List<Path> convertingBase = Files.walk(Paths.get("C:\\Repositories\\pillarsofeternity-2-Enhanced-Ui\\PoE2-EnhancedUserInterface\\localized\\en\\text")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		
		
		filesInFolder = Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\exported\\localized\\en\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.colorupdate(convertingBase, filesInFolder, "en2", jaxbUnmarshaller,marshaller);
		
		filesInFolder = Files.walk(Paths.get("C:\\Repositories\\pillarsofeternity-2-german-patch\\exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.colorupdate(convertingBase, filesInFolder, "de_patch", jaxbUnmarshaller,marshaller);
		
		filesInFolder = Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\exported\\localized\\fr\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.colorupdate(convertingBase, filesInFolder, "fr", jaxbUnmarshaller,marshaller);
		
		// It hat da auch viele Fixes mit unter gebracht
		filesInFolder = Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\exported\\localized\\it\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.colorupdate(convertingBase, filesInFolder, "it2", jaxbUnmarshaller,marshaller);
		
		filesInFolder = Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\exported\\localized\\es\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.colorupdate(convertingBase, filesInFolder, "es", jaxbUnmarshaller,marshaller);
		
		filesInFolder = Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\exported\\localized\\pt\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.colorupdate(convertingBase, filesInFolder, "pt", jaxbUnmarshaller,marshaller);
		
		filesInFolder = Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\exported\\localized\\pl\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.colorupdate(convertingBase, filesInFolder, "pl", jaxbUnmarshaller,marshaller);
		
		filesInFolder = Files.walk(Paths.get("C:\\Repositories\\pillarsofeternity-2-Enhanced-Ui\\PoE2-EnhancedUserInterface\\localized\\ru\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.colorupdate(convertingBase, filesInFolder, "rufix", jaxbUnmarshaller,marshaller);
		
		filesInFolder = Files.walk(Paths.get("E:\\GOG Games\\Pillars of Eternity II Deadfire\\PillarsOfEternityII_Data\\exported\\localized\\zh\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		cleanup.colorupdate(convertingBase, filesInFolder, "zh", jaxbUnmarshaller,marshaller);
		
		
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
				entry.compareIsTranslated(enIterator.next());
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
	
	

	private void colorupdate(List<Path> convertingBase, List<Path> orginalLanguage, String language, Unmarshaller jaxbUnmarshaller, Marshaller marshaller) throws JAXBException, IOException {
		for (Path colorful : convertingBase) {
			for (Path plain : orginalLanguage) {
				if(colorful.getFileName().equals(plain.getFileName())) {
					StringTableFile colorfulTexts = (StringTableFile) jaxbUnmarshaller.unmarshal( colorful.toFile() );
					StringTableFile plainTexts = (StringTableFile) jaxbUnmarshaller.unmarshal( plain.toFile() );
					StringTableFile outputFile = new StringTableFile();
					outputFile.setName(plainTexts.getName());
					outputFile.setNextEntryID(plainTexts.getNextEntryID());
					outputFile.setEntryCount(plainTexts.getEntryCount());
					StringTableFile outputFileDamage = new StringTableFile();
					outputFileDamage.setName(plainTexts.getName());
					outputFileDamage.setNextEntryID(plainTexts.getNextEntryID());
					outputFileDamage.setEntryCount(plainTexts.getEntryCount());
					StringTableFile outputFileCombined = new StringTableFile();
					outputFileCombined.setName(plainTexts.getName());
					outputFileCombined.setNextEntryID(plainTexts.getNextEntryID());
					outputFileCombined.setEntryCount(plainTexts.getEntryCount());
					
					for(Entry colorfulEntry : colorfulTexts.getEntries())
					{
						for (Entry plainEntry : plainTexts.getEntries()) {
							if(colorfulEntry.getID() == plainEntry.getID()) {
								Entry coloredEntry = plainEntry.replaceAfflictionWithColor(language);
								if(coloredEntry != null)
									outputFile.getEntries().add(coloredEntry);
								
								Entry damageIconEntry = plainEntry.replaceDamageTypeWithIcon(language);
								Entry damageDefenseIconEntry;
								if(damageIconEntry != null) {
									damageDefenseIconEntry = damageIconEntry.replaceDefenseWithIcon(language);
									if(damageDefenseIconEntry  != null) {
										outputFileDamage.getEntries().add(damageDefenseIconEntry);
									} else {
										outputFileDamage.getEntries().add(damageIconEntry);
									}
								} else {
									damageDefenseIconEntry = plainEntry.replaceDefenseWithIcon(language);
									if(damageDefenseIconEntry != null)
										outputFileDamage.getEntries().add(damageDefenseIconEntry);
								}
								
								if(coloredEntry != null && (damageIconEntry != null || damageDefenseIconEntry != null)) {
									Entry booth = coloredEntry.replaceDamageTypeWithIcon(language);
									if(damageDefenseIconEntry != null) {
										if(booth != null)
											booth = booth.replaceDefenseWithIcon(language);
										else
											booth = coloredEntry.replaceDefenseWithIcon(language);
									}
									outputFileCombined.getEntries().add(booth);
									if(damageIconEntry != null) {
										System.out.println("Clash \n" + coloredEntry.getDefaultText() + "\n" + damageIconEntry.getDefaultText() + "\n"+ booth.getDefaultText());
									}
									if(damageDefenseIconEntry != null) {
										System.out.println("Clash \n" + coloredEntry.getDefaultText() + "\n" + damageDefenseIconEntry.getDefaultText() + "\n"+ booth.getDefaultText());
									}
									if(damageIconEntry != null && damageDefenseIconEntry != null) {
										System.out.println("Clash \n" + coloredEntry.getDefaultText() + "\n" + damageIconEntry.getDefaultText() + "\n" + damageDefenseIconEntry.getDefaultText() + "\n"+ booth.getDefaultText());
									}
								}
							}
						}
					}
					
					// Affliction
					File file = new File("C:\\Repositories\\pillarsofeternity-2-Enhanced-Ui\\PoE2-EnhancedUserInterface_inspiration\\localized\\" + language + "\\text\\game\\" + plain.getFileName());
					file.getParentFile().mkdirs();
					file.createNewFile();
					Path path = Paths.get(file.getPath());
					marshaller.marshal(outputFile, file);
					
					List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8")); 
					lines.set(0, lines.get(0).replaceAll("encoding=\"UTF-8\" standalone=\\\"yes\\\"", "encoding=\"utf-8\""));
					lines.set(1, lines.get(1).replaceAll("xmlns:xs=", "xmlns:xsd="));
					List<String> modified = lines.stream().map(x -> {return x.replaceAll("    ", "  ").replaceFirst("<DefaultText></DefaultText>", "<DefaultText />").replaceFirst("<FemaleText></FemaleText>", "<FemaleText />");}).collect(Collectors.toList());
					Files.write(path, modified);
					
					
					// Damage und Defense
					file = new File("C:\\Repositories\\pillarsofeternity-2-Enhanced-Ui\\PoE2-EnhancedUserInterface_defenses\\localized\\" + language + "\\text\\game\\" + plain.getFileName());
					file.getParentFile().mkdirs();
					file.createNewFile();
					path = Paths.get(file.getPath());
					marshaller.marshal(outputFileDamage, file);
					
					lines = Files.readAllLines(path, Charset.forName("UTF-8")); 
					lines.set(0, lines.get(0).replaceAll("encoding=\"UTF-8\" standalone=\\\"yes\\\"", "encoding=\"utf-8\""));
					lines.set(1, lines.get(1).replaceAll("xmlns:xs=", "xmlns:xsd="));
					modified = lines.stream().map(x -> {return x.replaceAll("    ", "  ").replaceFirst("<DefaultText></DefaultText>", "<DefaultText />").replaceFirst("<FemaleText></FemaleText>", "<FemaleText />");}).collect(Collectors.toList());
					Files.write(path, modified);
					
					
					// Combined, only clashes
					file = new File("C:\\Repositories\\pillarsofeternity-2-Enhanced-Ui\\PoE2-EnhancedUserInterface_zzz_multiple\\localized\\" + language + "\\text\\game\\" + plain.getFileName());
					file.getParentFile().mkdirs();
					file.createNewFile();
					path = Paths.get(file.getPath());
					marshaller.marshal(outputFileCombined, file);
					
					lines = Files.readAllLines(path, Charset.forName("UTF-8")); 
					lines.set(0, lines.get(0).replaceAll("encoding=\"UTF-8\" standalone=\\\"yes\\\"", "encoding=\"utf-8\""));
					lines.set(1, lines.get(1).replaceAll("xmlns:xs=", "xmlns:xsd="));
					modified = lines.stream().map(x -> {return x.replaceAll("    ", "  ").replaceFirst("<DefaultText></DefaultText>", "<DefaultText />").replaceFirst("<FemaleText></FemaleText>", "<FemaleText />");}).collect(Collectors.toList());
					Files.write(path, modified);
				}
			}
		}

		
		
	}

}
