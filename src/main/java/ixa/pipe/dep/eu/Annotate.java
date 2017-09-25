/*
 * Copyright (C) 2017 IXA Taldea, University of the Basque Country UPV/EHU

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

import java.util.List;
import ixa.kaflib.KAFDocument;
import ixa.kaflib.Term;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Hashtable;



class Annotate {
    
    private final String baliabideDir;
    private final File bohnetTmpFitx;
    private final File brownBohnetTmpFitx;
    private final File simBohnetTmpFitx;
    private final File simMaltTmpFitx;
    private final File brownMaltTmpFitx;
    private final File bohnetParsedTmpFitx;
    private final File brownBohnetParsedTmpFitx;
    private final File simBohnetParsedTmpFitx;
    private final File brownMaltParsedTmpFitx;
    private final File simMaltParsedTmpFitx;
    private final File blenderBohnetTmpFitx;
    private final File blenderBrownBohnetTmpFitx;
    private final File blenderSimBohnetTmpFitx;
    private final File blenderEmaitzaTmpFitx;
    private final Hashtable<String, String> brown = new Hashtable<String, String>();
    private final Hashtable<String, String> sim = new Hashtable<String, String>();


    public Annotate(String baliabideak) throws IOException {
	this.baliabideDir = baliabideak;
	this.bohnetTmpFitx = File.createTempFile("Bohnet.conll", ".tmp",
						 new File(baliabideDir + "/Bohnet"));
	bohnetTmpFitx.deleteOnExit();
	this.brownBohnetTmpFitx = File.createTempFile("Brown_Bohnet.conll", ".tmp",
						      new File(baliabideDir + "/BrownBohnet"));
	brownBohnetTmpFitx.deleteOnExit();
	this.simBohnetTmpFitx = File.createTempFile("Sim_Bohnet.conll", ".tmp",
						    new File(baliabideDir + "/SimBohnet"));
	simBohnetTmpFitx.deleteOnExit();
	this.simMaltTmpFitx = File.createTempFile("Sim_Malt.conll", ".tmp",
						  new File(baliabideDir + "/SimMalt"));
	simMaltTmpFitx.deleteOnExit();
	this.brownMaltTmpFitx = File.createTempFile("Brown_Malt.conll", ".tmp",
						    new File(baliabideDir + "/BrownMalt"));
	brownMaltTmpFitx.deleteOnExit();
	this.bohnetParsedTmpFitx = File.createTempFile("Bohnet.conll.parsed", ".tmp",
						       new File(baliabideDir + "/Bohnet"));
	bohnetParsedTmpFitx.deleteOnExit();
	this.brownBohnetParsedTmpFitx = File.createTempFile("Brown_Bohnet.conll.parsed", ".tmp",
							    new File(baliabideDir + "/BrownBohnet"));
	brownBohnetParsedTmpFitx.deleteOnExit();
	this.simBohnetParsedTmpFitx = File.createTempFile("Sim_Bohnet.conll.parsed", ".tmp",
							  new File(baliabideDir + "/SimBohnet"));
	simBohnetParsedTmpFitx.deleteOnExit();
	this.brownMaltParsedTmpFitx = File.createTempFile("BrownMalt.conll.parsed", ".tmp",
							  new File(baliabideDir + "/BrownMalt"));
	brownMaltParsedTmpFitx.deleteOnExit();
	this.simMaltParsedTmpFitx = File.createTempFile("SimMalt.conll.parsed", ".tmp",
							new File(baliabideDir + "/SimMalt"));
	simMaltParsedTmpFitx.deleteOnExit();
	this.blenderBohnetTmpFitx = File.createTempFile("Bohnet.conll.parsed", ".tmp",
							new File(baliabideDir + "/Blender"));
	blenderBohnetTmpFitx.deleteOnExit();
	this.blenderBrownBohnetTmpFitx = File.createTempFile("BrownBohnet.conll.parsed", ".tmp",
							     new File(baliabideDir + "/Blender"));
	blenderBrownBohnetTmpFitx.deleteOnExit();
	this.blenderSimBohnetTmpFitx = File.createTempFile("SimBohnet.conll.parsed", ".tmp",
							   new File(baliabideDir + "/Blender"));
	blenderSimBohnetTmpFitx.deleteOnExit();
	this.blenderEmaitzaTmpFitx = File.createTempFile("Azken_emaitza.conll", ".tmp",
							 new File(baliabideDir + "/Blender"));
	blenderEmaitzaTmpFitx.deleteOnExit();
    }


    void lortuDependentziakMate(KAFDocument kaf, String conllHelbidea) throws IOException,
									      FileNotFoundException {
        kargatuBrown();
        kargatuSim();
        KAF2MateAll(kaf);
        exekutatuBohnet();
        exekutatuBrownBohnet();
        exekutatuSimBohnet();
        exekutatuBrownMalt();
        exekutatuSimMalt();
        exekutatuBlender();

      	BufferedReader br = new BufferedReader(new FileReader(this.blenderEmaitzaTmpFitx));
        String lerroa = br.readLine();
        List <Term> terminoak = kaf.getTerms();
        int zenbatgarrena = 0;
        String Conll07 = "";
        Vector <String> inprimatzekoa = new Vector();
	while (lerroa != null) {
	    if (lerroa.contains("\t")) {
		String[] zatiak = lerroa.split("\t");
                int unekoarenZenb = Integer.parseInt(zatiak[0]);
                String hitza = zatiak[1];
                String lema = zatiak[2];
                String kategoria = zatiak[3];
                String azpikategoria = zatiak[4];
                String feats = zatiak[5];
                int head = Integer.parseInt(zatiak[6]);
                String deprel = zatiak[7];
                String idaztekoaBerri = unekoarenZenb + "\t" + hitza + "\t" + lema + "\t"
		                        + kategoria + "\t" + azpikategoria + "\t" + feats
		                        + "\t" + head + "\t" + deprel + "\t_\t_\n";
                inprimatzekoa.add(idaztekoaBerri);
                int diferentzia = 0;
                int hurrengoa = 0;
                if (head != 0) {
                    Term unekoa = terminoak.get(zenbatgarrena);
                    if (head > unekoarenZenb) {
			diferentzia = head - unekoarenZenb;
                        hurrengoa = zenbatgarrena + diferentzia;
                    } else {
                        diferentzia = unekoarenZenb - head;
                        hurrengoa = zenbatgarrena - diferentzia;                       
                    }                   
                    
		    Term nora = terminoak.get(hurrengoa); 
		    kaf.newDep(nora, unekoa, deprel); 
                }
		zenbatgarrena++;
	    } else {
		inprimatzekoa.add("\n");
            }
	    lerroa = br.readLine();
	}
	br.close();
        if (!conllHelbidea.contentEquals("Hutsa")) {
	    inprimatu(conllHelbidea, inprimatzekoa);
	}
    }


    public void KAF2MateAll(KAFDocument kaf) throws IOException {
	sortuBohnet(kaf);
        sortuBrownBohnet(kaf);
        sortuSimBohnet(kaf);
        sortuBrownMalt(kaf);
        sortuSimMalt(kaf);
                      
    }


    private String lortuKategoria(String[] zatiak) {
        String emaitza = "_";
        if (!zatiak[0].contains("@") && !zatiak[0].contains("BIZ")
	        && !zatiak[0].contains("EZEZAG")) {
	    emaitza = zatiak[0];
            
        } else if (zatiak[0].contains("EZEZAG") && zatiak[1].contains("IZE")) {
	    emaitza = "IZE";
	}
	return emaitza;
   }


    private String lortuAzpikategoria(String[] zatiak, String kategoria) {
         String emaitza = "_";
         if (zatiak[0].contains("IZE") || kategoria.contains("IZE")) {
	     if (zatiak[1].contains("ARR") || zatiak[1].contains("LIB") 
		    || zatiak[1].contains("IZB") || zatiak[1].contains("ZKI")) {
		 emaitza = "IZE_" + zatiak[1];
	     }
	 } else if (zatiak[0].contains("ADL") || zatiak[0].contains("ADT") 
		    || zatiak[0].contains("PRT") || zatiak[0].contains("ADJ")
		    || zatiak[0].contains("BST")) {
	     emaitza = zatiak[0];
	 } else if (zatiak[0].contains("ADB")) {
	     emaitza = "ADB_ADB";
	 } else if (zatiak[0].contains("DET")) {
	     if (zatiak[1].contains("BAN") || zatiak[1].contains("DZG") 
		     || zatiak[1].contains("DZH") || zatiak[1].contains("ERKARR")
		     || zatiak[1].contains("ERKIND") || zatiak[1].contains("NOLARR")
		     || zatiak[1].contains("NOLGAL") || zatiak[1].contains("ORD") 
		     || zatiak[1].contains("ORO")) {
		 emaitza = "DET_" + zatiak[1];
	     }
	 } else if (zatiak[0].contains("IOR")) {
	     if (zatiak[1].contains("ELK") || zatiak[1].contains("IZGGAL") 
		     || zatiak[1].contains("IZGMGB") || zatiak[1].contains("PERARR")
		     || zatiak[1].contains("PERIND")) {
		 emaitza = "IOR_" + zatiak[1];
	     }
	 } else if (zatiak[0].contains("LOT")) {
	     if (zatiak[1].contains("JNT") || zatiak[1].contains("LOK")) {
		 emaitza = "LOT_" + zatiak[1];
	     }
	 } else if (zatiak[0].contains("ADI")) {
	     if (zatiak[1].contains("SIN") || zatiak[1].contains("ADK") 
		     || zatiak[1].contains("ADP") || zatiak[1].contains("FAK")) {
		 emaitza = "ADI_" + zatiak[1];
	     }
	 }
	 if (kategoria.contains("IZEELI")) {
	     emaitza = kategoria;
	 }
	 return emaitza;
    }


    private String lortuFeats(String[] zatiak) {
        Vector<String> denak = new Vector();
        String kasua = lortuKasua(zatiak);
        if (!kasua.contentEquals("")) {
	    denak.add(kasua);
        }

        String erl = lortuErl(zatiak);
        if (!erl.contentEquals("")) {
	    denak.add(erl);
        }

        String adm = lortuAdm(zatiak);
        if (!adm.contentEquals("")) {
	    denak.add(adm);
        }

        String asp = lortuAsp(zatiak);
        if (!asp.contentEquals("")) {
	    denak.add(asp);
        }

        String mdn = lortuMdn(zatiak);
        if (!mdn.contentEquals("")) {
            
            denak.add(mdn);
        }

        String dadudio = lortuDadudio(zatiak);
        if (!dadudio.contentEquals("")) {
	    denak.add(dadudio);
        }

        String nr = lortuNr(zatiak);
        if (!nr.contentEquals("")) {
	    denak.add(nr);
        }

        String nk = lortuNk(zatiak);
        if (!nk.contentEquals("")) {
	    denak.add(nk);
        }

        String ni = lortuNi(zatiak);
        if (!ni.contentEquals("")) {
	    denak.add(ni);
        }

        String num = lortuNum(zatiak);
        if (!num.contentEquals("")) {
	    denak.add(num);
        }
       
	String emaitza = join(denak,"|");
	if (emaitza.contentEquals("")) {
	    emaitza = "_";
        }
        return emaitza;
    }

    
    private String lortuHiruFeatsBrown(String[] zatiak, String hitza) {
        Vector<String> denak = new Vector();
        String brown = lortuBrown(hitza);
        String kasua = lortuKasua(zatiak);
        if (kasua.contentEquals("")) {
	    kasua = "null";
	}
	denak.add(kasua);
        
	String num = lortuNum(zatiak);
        if (num.contentEquals("")) {
	    num = "null";
        }
        denak.add(num);

        String erl = lortuErl(zatiak);
        if (erl.contentEquals("")) {
	    erl = "null";
        } 
        denak.add(erl);
	denak.add(brown);

	String emaitza = join(denak, "|");
        return emaitza;
    }


    private String lortuAzpikategoriKonkretua(String ezaugarriak, String hitza) {
        String emaitza = "";
        if (ezaugarriak.contains("BEREIZ")) {
	    emaitza = "PUNT_BEREIZ";
	} else if (hitza.contains(":")) {
	    emaitza = "PUNT_BI_PUNT";
	} else if (hitza.contains("...")) {
	    emaitza = "PUNT_HIRU";
	} else if (hitza.contains(";")) {
	    emaitza = "PUNT_PUNT_KOMA";
	} else if (hitza.contains("?")) {
	    emaitza = "PUNT_GALD";
	} else if (hitza.contains("!")) {
	    emaitza = "PUNT_ESKL";
	} else if (hitza.contains(",")) {
	    emaitza = "PUNT_KOMA";
	} else if (hitza.contains(".")) {
	    emaitza = "PUNT_PUNT";
	}
	return emaitza;
    }


    private String lortuKasua(String[] zatiak) {
        String emaitza = "";
        for (int i=0; i<zatiak.length; i++) {
	    if (zatiak[i].contentEquals("ABL") || zatiak[i].contentEquals("ABS")
		    || zatiak[i].contentEquals("ABU")|| zatiak[i].contentEquals("ALA")
		    || zatiak[i].contentEquals("BNK") || zatiak[i].contentEquals("DAT")
		    || zatiak[i].contentEquals("DES") || zatiak[i].contentEquals("DESK")
		    || zatiak[i].contentEquals("ERG") || zatiak[i].contentEquals("GEL")
		    || zatiak[i].contentEquals("GEN") || zatiak[i].contentEquals("INE")
		    || zatiak[i].contentEquals("INS") || zatiak[i].contentEquals("MOT")
   		    || zatiak[i].contentEquals("PAR") || zatiak[i].contentEquals("PRO")
		    || zatiak[i].contentEquals("SOZ") || zatiak[i].contentEquals("ZERO")) {
		emaitza = "KAS=" + zatiak[i];
	    }
	}
	return emaitza;
    }


    private String lortuErl(String[] zatiak) {
        String emaitza = "";
        for (int i=0; i<zatiak.length; i++) {
	    if (zatiak[i].contentEquals("AURK") || zatiak[i].contentEquals("BALD")
		    || zatiak[i].contentEquals("DENB")|| zatiak[i].contentEquals("ERLT")
		    || zatiak[i].contentEquals("ESPL") || zatiak[i].contentEquals("HAUT")
		    || zatiak[i].contentEquals("HELB") || zatiak[i].contentEquals("KAUS") 
		    || zatiak[i].contentEquals("KONPL") || zatiak[i].contentEquals("KONT")
		    || zatiak[i].contentEquals("MOD") || zatiak[i].contentEquals("MOS")
		    || zatiak[i].contentEquals("ONDO") || zatiak[i].contentEquals("ZHG") 
		    || zatiak[i].contentEquals("MOD\\/DENB")) {
                if (zatiak[i].contentEquals("MOD\\/DENB")) {
		    zatiak[i] = "MODDENB";
                }
                emaitza = "ERL=" + zatiak[i];
	    }
	}
	return emaitza;
    }


    private String lortuAdm(String[] zatiak) {
        String emaitza = "";
        for (int i=0; i<zatiak.length; i++) {
	    if (zatiak[i].contentEquals("ADIZE") || zatiak[i].contentEquals("PART")) {
		emaitza = "ADM=" + zatiak[i];
	    }
	}
	return emaitza;
    }


    private String lortuAsp(String[] zatiak) {
        String emaitza = "";
        for (int i=0; i<zatiak.length; i++) {
	    if (zatiak[i].contentEquals("BURU") || zatiak[i].contentEquals("EZBU")
		   || zatiak[i].contentEquals("GERO") || zatiak[i].contentEquals("PNT")) {
		emaitza = "ASP=" + zatiak[i];
	    }
	}
	return emaitza;
    }


    private String lortuMdn(String[] zatiak) {
        String emaitza = "";
        for (int i=0; i<zatiak.length; i++) {
	    if (zatiak[i].contentEquals("A1") || zatiak[i].contentEquals("A2") 
		    || zatiak[i].contentEquals("A3") || zatiak[i].contentEquals("A4")
		    || zatiak[i].contentEquals("B1") || zatiak[i].contentEquals("B2")
		    || zatiak[i].contentEquals("B3") || zatiak[i].contentEquals("B4")
		    || zatiak[i].contentEquals("B5") || zatiak[i].contentEquals("B6")
		    || zatiak[i].contentEquals("B7") || zatiak[i].contentEquals("B8")
		    || zatiak[i].contentEquals("C") || zatiak[i].contentEquals("B5A")
		    || zatiak[i].contentEquals("B5B")) {
		emaitza = "MDN=" + zatiak[i];
	    }
	}
	return emaitza;
    }


    private String lortuDadudio(String[] zatiak) {
	String emaitza = "";
        for (int i=0; i<zatiak.length; i++) {
	    if (zatiak[i].contentEquals("NOR") || zatiak[i].contentEquals("NOR_NORK")
		    || zatiak[i].contentEquals("NOR_NORI") 
       	            || zatiak[i].contentEquals("NOR_NORI_NORK")) {
		emaitza = "DADUDIO=" + zatiak[i];
	    }
	}
	return emaitza;
    }


    private String lortuNr(String[] zatiak) {
        String emaitza = "";
        for (int i=0; i<zatiak.length; i++) {
	    if (zatiak[i].contains("NR_")) {
		String[] zat = zatiak[i].split("_");
		emaitza = "NOR=" + zat[1];
	    }
	}
	return emaitza;
    }


    private String lortuNk(String[] zatiak) {
        String emaitza = "";
        for (int i=0; i<zatiak.length; i++) {
	    if (zatiak[i].contains("NK_")) {
		String[] zat = zatiak[i].split("_");
		emaitza = "NORK=" + zat[1];
	    }
	}
	return emaitza;
    }


    private String lortuNi(String[] zatiak) {
        String emaitza = "";
        for (int i=0; i<zatiak.length; i++) {
	    if (zatiak[i].contains("NI_")) {
		String[] zat = zatiak[i].split("_");
		emaitza = "NORI=" + zat[1];
	    }
	}
	return emaitza;
    }


    private String lortuNum(String[] zatiak) {
	String emaitza = "";
        for (int i=0; i<zatiak.length; i++) {
	    if (zatiak[i].contentEquals("NUMP")) {
		emaitza = "NUM=P";
	    } else if (zatiak[i].contentEquals("NUMS")) {
		emaitza = "NUM=S";
	    }
	}
	return emaitza;
    }

    
    public static String join(Vector <String> parts, String delim) {
        StringBuilder result = new StringBuilder();
	for (int i = 0; i < parts.size(); i++) {
            String part = parts.elementAt(i);
            result.append(part);
            if (delim != null && i < parts.size()-1) {
                result.append(delim);
            }        
        }
        return result.toString();
    }


    private void inprimatu(String helbidea, Vector<String> inprimatzekoa) throws IOException {
	String ruta = helbidea;
        File archivo = new File(ruta);
        BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
	for (int i=0; i<inprimatzekoa.size(); i++) {
	    bw.write(inprimatzekoa.elementAt(i));
	}
	bw.close();
    }


    private String lortuBrown(String hitza) {
        String bueltatzekoa = "";
        if (brown.containsKey(hitza)){
	    bueltatzekoa = brown.get(hitza);
	} else if (brown.containsKey(hitza.toLowerCase())) {
	    bueltatzekoa = brown.get(hitza.toLowerCase());
	} else if (brown.containsKey(hitza.toUpperCase())) {
	    bueltatzekoa = brown.get(hitza.toUpperCase());
        }           
        return bueltatzekoa;
    }


    private void kargatuBrown() throws FileNotFoundException, IOException {
	String ruta = baliabideDir + "/Brown.txt";
	BufferedReader br = new BufferedReader(new FileReader (new File (ruta)));
	String linea = "";
	while((linea = br.readLine()) != null) {
	    String[] zatiak = linea.split("\t");
	    String osoa = zatiak[0];
	    String hitza = zatiak[1];
	    String laukoa = "";
	    String osobi = "";
	    if (osoa.length() > 4) {
		laukoa = osoa.substring(0, 4);
		osobi = "CLUSTER=" + osoa + "|CLUSTERM=" + laukoa;
	    } else {
		laukoa = osoa;
		osobi = "CLUSTER=" + osoa + "|CLUSTERM=" + laukoa;
	    }
	    
	    brown.put(hitza, osobi);
	    linea = br.readLine();          
	}
	br.close();
    }


    private void kargatuSim() throws FileNotFoundException, IOException {
	String ruta = baliabideDir + "/Sim.txt";
	BufferedReader br = new BufferedReader(new FileReader (new File (ruta)));
	String linea = "";
	while((linea = br.readLine()) != null) {
	    String[] zatiak = linea.split(" ");
	    String hitza = zatiak[0];
	    String clus = "CLUSTERS=" + zatiak[1];
	    sim.put(hitza, clus);
	    linea = br.readLine();
	}
	br.close();
    }


    private void sortuBohnet(KAFDocument kaf) throws IOException {
	BufferedWriter bw = new BufferedWriter(new FileWriter(this.bohnetTmpFitx));
        List <Term> terminoak = kaf.getTerms();
        String hitza = "";
        String lema = "";
        String ezaugarriak = "";
        String kategoria = "";
        String azpikategoria = "";
        String feats = "";
        String emaitza = "";
        int aurrekoarenSent = -1;
        int unekoarenSent = 0;
        int unekoarenZenbakia = 0;
        for (Term terminoa : terminoak) {
	    unekoarenSent = terminoa.getSent();
	    if (unekoarenSent != aurrekoarenSent) {
		unekoarenZenbakia = 1;
		aurrekoarenSent = unekoarenSent;
	    } else {
		unekoarenZenbakia++;
	    }
	    
	    hitza = terminoa.getForm();
	    lema = terminoa.getLemma();
	    ezaugarriak = terminoa.getCase();
	    
	    if (ezaugarriak.contains(" ")) {
		String[] zatiak = ezaugarriak.split(" ");
		kategoria = lortuKategoria(zatiak);
		azpikategoria = lortuAzpikategoria(zatiak, kategoria);
		feats = lortuFeats(zatiak); 
	    } else {
		kategoria = "PUNT";
		azpikategoria = lortuAzpikategoriKonkretua(ezaugarriak, hitza);
		feats = "_";
	    }
	    if (unekoarenZenbakia == 1 && unekoarenSent != 1) {
		emaitza = "\n" + unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t" + lema 
		    + "\t" + kategoria + "\t" + azpikategoria + "\t" + feats + "\t" + feats 
		    + "\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\n";
	    } else {
		emaitza = unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t" + lema + "\t"
		    + kategoria + "\t" + azpikategoria + "\t" + feats + "\t" + feats 
		    + "\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\n";
	    }
	    bw.write(emaitza);
	}
        bw.close();
    }


    private void sortuBrownBohnet(KAFDocument kaf) throws IOException {
	BufferedWriter bw = new BufferedWriter(new FileWriter(this.brownBohnetTmpFitx));
        List <Term> terminoak = kaf.getTerms();
        String hitza = "";
        String lema = "";
        String ezaugarriak = "";
        String kategoria = "";
        String azpikategoria = "";
        String feats = "";
        String tartekofeats = "";
        String emaitza = "";
        String brown = "";
        String atzizkia = "Null";
        int aurrekoarenSent = -1;
        int unekoarenSent = 0;
        int unekoarenZenbakia = 0;
        for (Term terminoa : terminoak) {
	    unekoarenSent = terminoa.getSent();
	    if (unekoarenSent != aurrekoarenSent) {
		unekoarenZenbakia = 1;
		aurrekoarenSent = unekoarenSent;
	    } else {
		unekoarenZenbakia++;
	    }

	    hitza = terminoa.getForm();
	    lema = terminoa.getLemma();
	    ezaugarriak = terminoa.getCase();
	    
	    if (ezaugarriak.contains(" ")) {
		String[] zatiak = ezaugarriak.split(" ");
		kategoria = lortuKategoria(zatiak);
		azpikategoria = lortuAzpikategoria(zatiak, kategoria);
		tartekofeats=lortuFeats(zatiak);
		brown = lortuBrown(hitza);
		atzizkia = lortuAtzizkia(hitza, lema);
		
		if (brown.equalsIgnoreCase("")) {
		    feats = tartekofeats;
		} else {
		    if (!tartekofeats.contains("_")) {
			feats = tartekofeats + "|" + brown + "|" + atzizkia;
		    } else {
			feats = brown + "|" + atzizkia;
		    }
		}
	    } else {
		kategoria = "PUNT";
		azpikategoria = lortuAzpikategoriKonkretua(ezaugarriak, hitza);
		feats = "_";
	    }
	    if (unekoarenZenbakia == 1 && unekoarenSent != 1) {
		emaitza = "\n" + unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t" + lema 
		    + "\t" + kategoria + "\t" + azpikategoria + "\t" + feats + "\t" + feats 
		    + "\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\n";
	    } else {
		emaitza = unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t" + lema + "\t" 
		    + kategoria + "\t" + azpikategoria + "\t" + feats + "\t" + feats 
		    + "\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\n";
	    }
	    bw.write(emaitza);
	}
        bw.close();
    }


    private void sortuSimBohnet(KAFDocument kaf) throws IOException {
	BufferedWriter bw = new BufferedWriter(new FileWriter(this.simBohnetTmpFitx));
        List <Term> terminoak = kaf.getTerms();
        String hitza = "";
        String lema = "";
        String ezaugarriak = "";
        String kategoria = "";
        String azpikategoria = "";
        String tartekofeats = "";
        String atzizkia = "";
        String feats = "";
        String sim = "";
        String emaitza = "";
        int aurrekoarenSent = -1;
        int unekoarenSent = 0;
        int unekoarenZenbakia = 0;
        for (Term terminoa : terminoak) {
	    unekoarenSent = terminoa.getSent();
	    if (unekoarenSent != aurrekoarenSent) {
		unekoarenZenbakia = 1;
		aurrekoarenSent = unekoarenSent;
	    } else {
		unekoarenZenbakia++;
	    }
	    hitza = terminoa.getForm();
	    lema = terminoa.getLemma();
	    ezaugarriak = terminoa.getCase();
           
	    if (ezaugarriak.contains(" ")) {
		String[] zatiak = ezaugarriak.split(" ");
		kategoria = lortuKategoria(zatiak);
		azpikategoria = lortuAzpikategoria(zatiak, kategoria);
		tartekofeats = lortuFeats(zatiak); 
		atzizkia = lortuAtzizkia(hitza, lema);
		sim = lortuSim(hitza);
		if (sim.equalsIgnoreCase("")) {
		    feats = tartekofeats;
		} else {
		    if (!tartekofeats.contains("_")) {
			feats = tartekofeats + "|" + sim + "|" + atzizkia;
		    } else {
			feats = sim + "|" + atzizkia;
		    }
		}
	    } else {
		kategoria = "PUNT";
		azpikategoria = lortuAzpikategoriKonkretua(ezaugarriak, hitza);
		feats = "_";
	    }
	    if (unekoarenZenbakia == 1 && unekoarenSent != 1) {
		emaitza = "\n" + unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t" + lema 
		    + "\t" + kategoria + "\t" + azpikategoria + "\t" + feats + "\t" + feats 
		    + "\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\n";
	    } else {
		emaitza = unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t" + lema + "\t" 
		    + kategoria + "\t" + azpikategoria + "\t" + feats + "\t" + feats 
		    + "\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\n";
	    }
	    bw.write(emaitza);
	}
        bw.close();
    }


    private void sortuBrownMalt(KAFDocument kaf) throws IOException {
	BufferedWriter bw = new BufferedWriter(new FileWriter(this.brownMaltTmpFitx));
        List <Term> terminoak = kaf.getTerms();
        String hitza = "";
        String lema = "";
        String ezaugarriak = "";
        String kategoria = "";
        String azpikategoria = "";
        String tartekofeats = "";
        String atzizkia = "";
        String feats = "";
        String brown = "";
        String emaitza = "";
        int aurrekoarenSent = -1;
        int unekoarenSent = 0;
        int unekoarenZenbakia = 0;
        for (Term terminoa : terminoak) {
	    unekoarenSent = terminoa.getSent();
	    if (unekoarenSent != aurrekoarenSent) {
		unekoarenZenbakia = 1;
		aurrekoarenSent = unekoarenSent;
	    } else {
		unekoarenZenbakia++;
	    }
	    
	    hitza = terminoa.getForm();
	    lema = terminoa.getLemma();
	    ezaugarriak = terminoa.getCase();
           
	    if (ezaugarriak.contains(" ")) {
		String[] zatiak = ezaugarriak.split(" ");
		kategoria = lortuKategoria(zatiak);
		azpikategoria = lortuAzpikategoria(zatiak, kategoria);
		tartekofeats = lortuFeats(zatiak);
		brown = lortuBrown(hitza);
		atzizkia = lortuAtzizkia(hitza, lema);
		
		if (brown.equalsIgnoreCase("")) {
		    feats = tartekofeats;
		} else {
		    if (!tartekofeats.contains("_")) {
			feats = tartekofeats + "|" + brown + "|" + atzizkia;
		    } else {
			feats = brown + "|" + atzizkia;
		    }
		}
	    } else {
		kategoria = "PUNT";
		azpikategoria = lortuAzpikategoriKonkretua(ezaugarriak, hitza);
		feats = "_";
	    }
	    if (unekoarenZenbakia == 1 && unekoarenSent != 1) {
		emaitza = "\n" + unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t"
		    + kategoria + "\t" + azpikategoria + "\t" + feats + "\t_\t_\t_\t_\n";
           } else {
		emaitza = unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t" + kategoria
		    + "\t" + azpikategoria + "\t" + feats + "\t_\t_\t_\t_\n";
	    }
	    bw.write(emaitza);
	}
        bw.close();
    }


    private void sortuSimMalt(KAFDocument kaf) throws IOException {
	BufferedWriter bw = new BufferedWriter(new FileWriter(this.simMaltTmpFitx));
        List <Term> terminoak = kaf.getTerms();
        String hitza = "";
        String lema = "";
        String ezaugarriak = "";
        String kategoria = "";
        String azpikategoria = "";
        String tartekofeats = "";
        String atzizkia = "";
        String sim = "";
        String feats = "";
        String emaitza = "";
        int aurrekoarenSent = -1;
        int unekoarenSent = 0;
        int unekoarenZenbakia = 0;
        for (Term terminoa : terminoak) {
	    unekoarenSent = terminoa.getSent();
	    if (unekoarenSent != aurrekoarenSent) {
		unekoarenZenbakia = 1;
		aurrekoarenSent = unekoarenSent;
	    } else {
		unekoarenZenbakia++;
	    }

	    hitza = terminoa.getForm();
	    lema = terminoa.getLemma();
	    ezaugarriak = terminoa.getCase();
           
	    if (ezaugarriak.contains(" ")) {
		String[] zatiak = ezaugarriak.split(" ");
		kategoria = lortuKategoria(zatiak);
		azpikategoria = lortuAzpikategoria(zatiak, kategoria);
		tartekofeats = lortuFeats(zatiak); 
		atzizkia = lortuAtzizkia(hitza, lema);
		sim = lortuSim(hitza);
		if (sim.equalsIgnoreCase("")) {
		    feats = tartekofeats;
		} else {
		    if (!tartekofeats.contains("_")) {
			feats = tartekofeats + "|" + sim + "|" + atzizkia;
		    } else {
			feats = sim + "|" + atzizkia;
		    }                  
		}
	    } else {
		kategoria = "PUNT";
		azpikategoria = lortuAzpikategoriKonkretua(ezaugarriak, hitza);
		feats = "_";
	    }
	    if (unekoarenZenbakia == 1 && unekoarenSent != 1) {
		emaitza = "\n" + unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t" 
		          + kategoria + "\t" + azpikategoria + "\t" + feats + "\t_\t_\t_\t_\n";
           } else {
		emaitza = unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t"
		          + kategoria + "\t" + azpikategoria + "\t" + feats + "\t_\t_\t_\t_\n";
	    }
	    bw.write(emaitza);
	}
        bw.close();
    }


    private String lortuAtzizkia(String hitza, String lema) {
	String emaitza = "ATZIZKIA=Null";
	if (!lema.contains("*") && !lema.contains("?") && !lema.contains(".") 
	    && !lema.contains(")") && !lema.contains("(") && !lema.contains("[")
	    && !lema.contains("]") && !lema.contains("+")) {
            
            Pattern p = Pattern.compile(lema);
            Matcher m = p.matcher(hitza);
            int luzerahitza = hitza.length();
            int luzeralema = lema.length();
            int tartea = luzerahitza - luzeralema;
	    if (m.find() && tartea != 0) {
		emaitza = "ATZIZKIA=" + hitza.substring(luzeralema);
	    }
	}
	return emaitza;
    }


    private String lortuSim(String hitza) {
	String bueltatzekoa = "";
        if (sim.containsKey(hitza)) {
	    bueltatzekoa = sim.get(hitza);
	} else if (sim.containsKey(hitza.toLowerCase())) {
	    bueltatzekoa = sim.get(hitza.toLowerCase());
	} else if (sim.containsKey(hitza.toUpperCase())) {
	    bueltatzekoa = sim.get(hitza.toUpperCase());
        }        
        return bueltatzekoa;
    }
   

    private void exekutatuBrownBohnet() throws FileNotFoundException, IOException {
	String modeloa = baliabideDir + "/BrownBohnet/prs.model";
        String anna = baliabideDir + "/mate-tools/anna-3.3.jar";
	String tartekoaTmpFitxIzena = this.brownBohnetTmpFitx.getAbsolutePath();
	String parsedTmpFitxIzena = this.brownBohnetParsedTmpFitx.getAbsolutePath();
        String helbideOsoa = "java -Xmx1G -classpath " + anna + " is2.parser.Parser -model " 
	                     + modeloa + " -test " + tartekoaTmpFitxIzena + " -out " 
	                     + parsedTmpFitxIzena;
        
        try {
	    Process process = Runtime.getRuntime().exec(helbideOsoa);
	    String errorStr = "";
	    BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	    while ((errorStr = errorStream.readLine()) != null) {
		System.err.println("[ERROREA anna-BrownBohnet]: " + errorStr);
	    }
	    errorStream.close();
	    process.waitFor();
	} catch(Exception errorea) {
            System.out.println("Salbuespena: " + errorea.toString());
	}
        
        BufferedReader br = new BufferedReader(new FileReader(this.brownBohnetParsedTmpFitx));
        String lerroa = br.readLine();
        Vector <String> inprimatzekoa = new Vector();
        while (lerroa != null){
	    if (lerroa.contains("\t")) {
		String [] zatiak = lerroa.split("\t");
                int unekoarenZenb = Integer.parseInt(zatiak[0]);
                String hitza = zatiak[1];
                String lema = zatiak[2];
                String kategoria = zatiak[4];
                String azpikategoria = zatiak[5];
                String feats = zatiak[6];
                int head = Integer.parseInt(zatiak[9]);
                String deprel = zatiak[11];
                String idaztekoaBerri = unekoarenZenb + "\t" + hitza + "\t" + lema + "\t" 
		                        + kategoria + "\t" + azpikategoria + "\t" + feats 
		                        + "\t" + head + "\t" + deprel + "\t_\t_\n";
                inprimatzekoa.add(idaztekoaBerri);
	    } else {
		inprimatzekoa.add("\n");
            }
            lerroa = br.readLine();
	}
	br.close();

	String blenderBrownBohnetTmpFitxIzena = this.blenderBrownBohnetTmpFitx.getAbsolutePath();
        inprimatuBrownBohnet(blenderBrownBohnetTmpFitxIzena,inprimatzekoa);
    }
    

    private void exekutatuBohnet() throws FileNotFoundException, IOException {
	String modeloa = baliabideDir + "/Bohnet/prs.model";
        String anna = baliabideDir + "/mate-tools/anna-3.3.jar";
	String tartekoaTmpFitxIzena = this.bohnetTmpFitx.getAbsolutePath();
	String parsedTmpFitxIzena = this.bohnetParsedTmpFitx.getAbsolutePath();

        String helbideOsoa = "java -Xmx1G -classpath " + anna + " is2.parser.Parser -model "
	                     + modeloa + " -test " + tartekoaTmpFitxIzena + " -out "
	                     + parsedTmpFitxIzena;
     
	try {
	    Process process = Runtime.getRuntime().exec(helbideOsoa);
	    String errorStr = "";
	    BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	    while ((errorStr = errorStream.readLine()) != null) {
		System.err.println("[ERROREA anna-Bohnet]: " + errorStr);
	    }
	    errorStream.close();
	    process.waitFor();
        } catch(Exception errorea) {
            System.out.println("Salbuespena: " + errorea.toString());
	}
        
        BufferedReader br = new BufferedReader(new FileReader(this.bohnetParsedTmpFitx));
        String lerroa = br.readLine();
        Vector <String> inprimatzekoa = new Vector();
        while (lerroa != null) {
	    if (lerroa.contains("\t")) {
		String [] zatiak = lerroa.split("\t");
                int unekoarenZenb = Integer.parseInt(zatiak[0]);
                String hitza = zatiak[1];
                String lema = zatiak[2];
                String kategoria = zatiak[4];
                String azpikategoria = zatiak[5];
                String feats = zatiak[6];
                int head = Integer.parseInt(zatiak[9]);
                String deprel = zatiak[11];
                String idaztekoaBerri = unekoarenZenb + "\t" + hitza + "\t" + lema + "\t" 
		                        + kategoria + "\t" + azpikategoria + "\t" + feats
		                        + "\t" + head + "\t" + deprel + "\t_\t_\n";
                inprimatzekoa.add(idaztekoaBerri);
	    } else {
		inprimatzekoa.add("\n");
            }
	    lerroa = br.readLine();
	}
	br.close();
        
	String blenderBohnetTmpFitxIzena = this.blenderBohnetTmpFitx.getAbsolutePath();
        inprimatuBohnet(blenderBohnetTmpFitxIzena,inprimatzekoa);
    }


    private void exekutatuSimBohnet() throws FileNotFoundException, IOException {
	String modeloa = baliabideDir + "/SimBohnet/prs.model";
        String anna = baliabideDir + "/mate-tools/anna-3.3.jar";
	String tartekoaTmpFitxIzena = this.simBohnetTmpFitx.getAbsolutePath();
	String parsedTmpFitxIzena = this.simBohnetParsedTmpFitx.getAbsolutePath();
        String helbideOsoa = "java -Xmx1G -classpath " + anna + " is2.parser.Parser -model "
	                     + modeloa + " -test " + tartekoaTmpFitxIzena + " -out "
	                     + parsedTmpFitxIzena;
       
        try {
	    Process process = Runtime.getRuntime().exec(helbideOsoa);
	    String errorStr = "";
	    BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	    while ((errorStr = errorStream.readLine()) != null) {
		System.err.println("[ERROREA anna-SimBohnet]: " + errorStr);
	    }
	    errorStream.close();
	    process.waitFor();
        } catch(Exception errorea) {
            System.out.println("Salbuespena: " + errorea.toString());
	}
        
        BufferedReader br = new BufferedReader(new FileReader(this.simBohnetParsedTmpFitx));
        String lerroa = br.readLine();
        Vector <String> inprimatzekoa = new Vector();
        while (lerroa != null) {
	    if (lerroa.contains("\t")) {
		String [] zatiak = lerroa.split("\t");
                int unekoarenZenb = Integer.parseInt(zatiak[0]);
                String hitza = zatiak[1];
                String lema = zatiak[2];
                String kategoria = zatiak[4];
                String azpikategoria = zatiak[5];
                String feats = zatiak[6];
                int head = Integer.parseInt(zatiak[9]);
                String deprel = zatiak[11];
                String idaztekoaBerri = unekoarenZenb + "\t" + hitza + "\t" + lema + "\t"
		                        + kategoria + "\t" + azpikategoria + "\t" + feats
		                        + "\t" + head + "\t" + deprel + "\t_\t_\n";
                inprimatzekoa.add(idaztekoaBerri);
	    } else {
		inprimatzekoa.add("\n");
            }
            lerroa = br.readLine();
	}
	br.close();

	String blenderSimBohnetTmpFitxIzena = this.blenderSimBohnetTmpFitx.getAbsolutePath();
        inprimatuSimBohnet(blenderSimBohnetTmpFitxIzena,inprimatzekoa);
     }


    private void exekutatuBrownMalt() {
        String anna = baliabideDir + "/maltparser/maltparser-1.7.jar";
	String tartekoaTmpFitxIzena = this.brownMaltTmpFitx.getAbsolutePath();
	String parsedTmpFitxIzena = this.brownMaltParsedTmpFitx.getAbsolutePath();
        String helbideOsoa = "java -jar -Xmx1g " + anna + " -w " + baliabideDir 
	                     + "/BrownMalt/ -c langModel -i " + tartekoaTmpFitxIzena + " -o " 
	                     + parsedTmpFitxIzena + " -m parse ";
        String blender = baliabideDir + "/Blender/";
        
        try {
	    Process process = Runtime.getRuntime().exec(helbideOsoa);
	    //String errorStr = "";
	    //BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	    //while ((errorStr = errorStream.readLine()) != null) {
	    //	System.err.println("[ERROREA maltparser-BrownMalt]: " + errorStr);
	    //}
	    //errorStream.close();
	    process.waitFor();
	    Process process2 = Runtime.getRuntime().exec("mv " + parsedTmpFitxIzena + " " + blender);
	    process2.waitFor();
	    process2.destroy();
        } catch(Exception errorea) {
            System.out.println("Salbuespena: " + errorea.toString());
	}
    }


    private void exekutatuSimMalt() {
        String anna = baliabideDir + "/maltparser/maltparser-1.7.jar";
	String tartekoaTmpFitxIzena = this.simMaltTmpFitx.getAbsolutePath();
	String parsedTmpFitxIzena = this.simMaltParsedTmpFitx.getAbsolutePath();
        String helbideOsoa = "java -jar -Xmx1g " + anna + " -w " + baliabideDir 
	                     + "/SimMalt/ -c langModel -i " + tartekoaTmpFitxIzena + " -o " 
	                     + parsedTmpFitxIzena + " -m parse ";
        String blender = baliabideDir + "/Blender/";
        
        try {
	    Process process = Runtime.getRuntime().exec(helbideOsoa);
	    //String errorStr = "";
	    //BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	    //while ((errorStr = errorStream.readLine()) != null) {
	    //System.err.println("[ERROREA maltparser-SimMalt]: " + errorStr);
	    //}
	    //errorStream.close();
	    process.waitFor();
	    Process process2 = Runtime.getRuntime().exec("mv " + parsedTmpFitxIzena + " " + blender);
	    process2.waitFor();
	    process2.destroy();
        } catch(Exception errorea) {
            System.out.println("Salbuespena: " + errorea.toString());
        }
    }


    private void exekutatuBlender() {
        String jar = baliabideDir + "/maltblender/MaltBlender_0.0.1.jar";
        String ebaluatzailea = baliabideDir + "/Blender/eval07.pl";
        String emaitza = this.blenderEmaitzaTmpFitx.getAbsolutePath();
        String gold = baliabideDir + "/Blender/dev.Basque.pred.conll";
        String bohnetEstimazioa = baliabideDir + "/Blender/dev.Basque.pred.parsed.bohnet.conll.oinarria";
	String bohnetTesta = this.blenderBohnetTmpFitx.getAbsolutePath();
        String brownBohnetEstimazioa = baliabideDir + "/Blender/dev.Basque.pred.parsed.bohnet.brown.conll";
        String brownBohnetTesta = this.blenderBrownBohnetTmpFitx.getAbsolutePath();
        String simBohnetEstimazioa = baliabideDir + "/Blender/dev.Basque.pred.parsed.bohnet.sim.conll";
        String simBohnetTesta = this.blenderSimBohnetTmpFitx.getAbsolutePath();
        String brownMaltEstimazioa = baliabideDir + "/Blender/dev.Basque.pred.parsed.brown.malt";
	File brownMaltTestFitx = new File(baliabideDir + "/Blender/" + this.brownMaltParsedTmpFitx.getName());
	String brownMaltTesta = brownMaltTestFitx.getAbsolutePath();
	brownMaltTestFitx.deleteOnExit();
        String simMaltEstimazioa = baliabideDir + "/Blender/dev.Basque.pred.parsed.sim.malt";
	File simMaltTestFitx = new File(baliabideDir + "/Blender/" + this.simMaltParsedTmpFitx.getName());
	String simMaltTesta = simMaltTestFitx.getAbsolutePath();
	simMaltTestFitx.deleteOnExit();

        String agindua = "java -DPERL=/usr/bin/perl -DEVALUATOR=" + ebaluatzailea + " -jar " 
	                 + jar + " -o " + emaitza + " -h " + gold + " -w 3 -l 3 -H " 
	                 + brownBohnetEstimazioa + " " + bohnetEstimazioa + " " 
	                 + simBohnetEstimazioa + " " + brownMaltEstimazioa + " " 
	                 + simMaltEstimazioa + " -F " + brownBohnetTesta + " " + bohnetTesta
	                 + " " + simBohnetTesta + " " + brownMaltTesta + " " + simMaltTesta;
                
        try {
	    Process process = Runtime.getRuntime().exec(agindua);
	    String errorStr = "";
	    BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	    while ((errorStr = errorStream.readLine()) != null) {
		System.err.println("[ERROREA MaltBlender-Blender]: " + errorStr);
	    }
	    errorStream.close();
	    process.waitFor();
        } catch(Exception errorea) {
            System.out.println("Salbuespena: " + errorea.toString());
	}
    }


    private void inprimatuBohnet(String conllHelbidea, Vector<String> inprimatzekoa) throws IOException {
	String ruta = conllHelbidea;
        File archivo = new File(ruta);
        BufferedWriter bohnet = new BufferedWriter(new FileWriter(archivo));
	for (int i=0; i<inprimatzekoa.size(); i++) {
	    bohnet.write(inprimatzekoa.elementAt(i));
	}
	bohnet.close();
    }


    private void inprimatuBrownBohnet(String conllHelbidea, Vector<String> inprimatzekoa) throws IOException {
        String ruta = conllHelbidea;
        File archivo = new File(ruta);
        BufferedWriter brownBohnet = new BufferedWriter(new FileWriter(archivo));
	for (int i=0; i<inprimatzekoa.size(); i++) {
	    brownBohnet.write(inprimatzekoa.elementAt(i));
	}
        brownBohnet.close();
    }


    private void inprimatuSimBohnet(String conllHelbidea, Vector<String> inprimatzekoa) throws IOException {
	String ruta = conllHelbidea;
        File archivo = new File(ruta);
        BufferedWriter simBohnet = new BufferedWriter(new FileWriter(archivo));
	for (int i=0; i<inprimatzekoa.size(); i++) {
	    simBohnet.write(inprimatzekoa.elementAt(i));                
        }
	simBohnet.close();
    }

}

