package base;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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

	public static void main(String[] args) throws JAXBException, SAXException, IOException {
		File stringtableSchema = new File("C:\\Repositories\\pillarsofeternity-2-german-patch\\translation_helper\\stringtable.xsd");
		JAXBContext jaxbContext = JAXBContext.newInstance(StringTableFile.class);
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(stringtableSchema); 

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		
	    Marshaller m = jaxbContext.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		List<Path> filesInFolder = Files.walk(Paths.get("C:\\Repositories\\pillarsofeternity-2-german-patch\\exported\\localized\\de_patch\\text\\")).filter(Files::isRegularFile).sorted().collect(Collectors.toList());
		Pair<Long, Long> count = new Pair<>(0L,0L);
		Pair<Long, Long> countPunkte = new Pair<>(0L,0L);
		Pair<Long, Long> countLeerzeichen = new Pair<>(0L,0L);
		Pair<Long,Long> countTags = new Pair<>(0L, 0L);
		Pair<Long,Long> balance1 = new Pair<>(0L, 0L);
		Pair<Long,Long> balance2 = new Pair<>(0L, 0L);
		Pair<Long,Long> balance3 = new Pair<>(0L, 0L);
		Pair<Long,Long> apostrophe = new Pair<>(0L, 0L);
		Pair<Long,Long> bindestrich = new Pair<>(0L, 0L);
		for (Path path : filesInFolder) {
			StringTableFile stringtable = (StringTableFile) jaxbUnmarshaller.unmarshal( path.toFile() );
			
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
				
		//		entry.DocuHelper();
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
		System.out.println("< > 10 davon sind wirkliche größer als Zeichen: " + balance2.b); 
		System.out.println("„ “: " + balance3.a);
		System.out.println("‚ ‘: " + balance3.b);
		
		System.out.println("Getauschte ': " + apostrophe.a);
		System.out.println("Getauschte ‚‘ gegen „“: " + apostrophe.b);
		System.out.println("Getauschte -: " + bindestrich.a);
	}
	

}
