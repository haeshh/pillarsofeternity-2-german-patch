rem Ersetze <drive_letter> mit dem Laufwerkbuchstaben, wo die Texte von "Pillars of Eternity: Deadfire" liegen: z. B.: d
rem Ersetze <text_source> mit dem Pfad, wo die Texte von "Pillars of Eternity: Deadfire" liegen: z. B.: poe2_git
rem Ersetze <drive_letter_helper> mit dem Laufwerkbuchstaben, wo die Helper Dateien liegen: z. B.: d
rem Ersetze <helper_source> mit dem Pfad, wo die Helper Dateien liegen: z. B.: test_poe2
rem
rem Beispiel:
rem java -jar saxon9he.jar -s:d:/PoE_2_git/exported/localized/de_patch/language.xml -xsl:d:/test_poe2/stringextract.xsl -threads:4
rem java -jar languagetool-commandline.jar -r -l de-DE -c UTF-8 -d DE_CASE,UPPERCASE_SENTENCE_START d:\poe2_translation_bare > poe2_text_fehler.txt 2>&1
rem
rem In der Datei stringextract.xsl muss text_source noch angepasst werden. Es muss der gleiche Pfad wie hier in dieser Batch Datei sein. Also z. B.: PoE_2_git
rem Die erste Zeile erzeugt ein neues Verzeichnis poe2_translation_bare auf dem Laufwerk <drive_letter_helper>. Also hier im Beispiel dann auf d.



java -jar saxon9he.jar -s:<drive_letter>:/<text_source>/exported/localized/de_patch/language.xml -xsl:<drive_letter_helper>:/<helper_source>/stringextract.xsl -threads:4
java -jar languagetool-commandline.jar -r -l de-DE -c UTF-8 -d DE_CASE,UPPERCASE_SENTENCE_START <drive_letter_helper>:\poe2_translation_bare > poe2_text_fehler.txt 2>&1

pause