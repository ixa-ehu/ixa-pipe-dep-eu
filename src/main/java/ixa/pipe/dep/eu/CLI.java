/*
 * Copyright (C) 2016 IXA Taldea, University of the Basque Country UPV/EHU

   This file is part of ixa-pipe-dep-eu.

   ixa-pipe-dep-eu is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   ixa-pipe-dep-eu is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with ixa-pipe-dep-eu.  If not, see <http://www.gnu.org/licenses/>.

*/


package ixa.pipe.dep.eu;


import ixa.kaflib.KAFDocument;
import ixa.kaflib.WF;
import ixa.kaflib.Term;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;


public class CLI {


    private final String version = CLI.class.getPackage().getImplementationVersion();
    private final String commit = CLI.class.getPackage().getSpecificationVersion();


    public static void main(String[] args) throws Exception {
        CLI cmdLine = new CLI();
        cmdLine.parseCLI(args);
    }


    public final void parseCLI(final String[] args) throws Exception {
	Namespace parsedArguments = null;

	ArgumentParser parser = ArgumentParsers.newArgumentParser(
            "ixa-pipe-dep-eu-" + version + ".jar").description(
            "ixa-pipe-dep-eu-" + version + " euskaraz idatzitako testuetarako"
	    + " dependentzia etiketatzailea da, Mate  etiketatzailean"
	    + " oinarritua. ixaKat tresna bat da (http://ixa2.si.ehu.es/ixakat/).\n");

	parser
	    .addArgument("-b", "--baliabideak")
	    .required(true)
	    .help("Beharrezkoa da zehaztea deskargatutako baliabideen direktorioaren kokapena.");

	parser
            .addArgument("-c", "--conll")
            .required(false)
	    .setDefault("Hutsa")
            .help("Erabili aukera hau irteera CONLL formatuan ere gorde nahi bada. " +
		  "Zehaztu irteerako fitxategiaren kokapena.");

        try {
            parsedArguments = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.out.println("Exekutatu java -jar ixa-pipe-dep-eu-" + version 
			       + ".jar -help laguntza jasotzeko");
            System.exit(1);
        }

	String baliabideDir = parsedArguments.getString("baliabideak");
	String conllFitx = parsedArguments.getString("conll");

	BufferedReader stdInReader = null;
	BufferedWriter w = null;

	stdInReader = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
	w = new BufferedWriter(new OutputStreamWriter(System.out, "UTF-8"));
	KAFDocument kaf = KAFDocument.createFromStream(stdInReader);

	String lang = kaf.getLang();

	KAFDocument.LinguisticProcessor lp = kaf.addLinguisticProcessor("deps", "ixa-pipe-dep-eu-" 
									+ lang, version + "-" + commit);
        lp.setBeginTimestamp();
        
        Annotate annotator = new Annotate(baliabideDir);
          
	try{
	    List<WF> wordForms = kaf.getWFs();
            List<Term> terms = kaf.getTerms();
            if (!wordForms.isEmpty() && !terms.isEmpty()) {
		annotator.lortuDependentziakMate(kaf,conllFitx);
	    }
	} catch(Exception e) {
	    System.err.println("Dependentzi analisiak huts egin du: ");
	    e.printStackTrace();
	} finally{
	    lp.setEndTimestamp();
	    w.write(kaf.toString());
	    w.close();
	}
    }
}