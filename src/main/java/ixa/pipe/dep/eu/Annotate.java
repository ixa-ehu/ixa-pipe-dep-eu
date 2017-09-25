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
import java.util.Vector;



class Annotate {
    
    private final String baliabideDir;

    
    public Annotate(String baliabideak) {
	this.baliabideDir = baliabideak;
    }
    
    void lortuDependentziakMate(KAFDocument kaf, String conllHelbidea) throws IOException {
	File tartekoaTmpFitx = File.createTempFile("tartekoa.conll", ".tmp");
	tartekoaTmpFitx.deleteOnExit();
	String tartekoaTmpFitxIzena = tartekoaTmpFitx.getAbsolutePath();
	File parsedTmpFitx = File.createTempFile("tartekoa.conll.parsed", ".tmp");
	parsedTmpFitx.deleteOnExit();
	String parsedTmpFitxIzena = parsedTmpFitx.getAbsolutePath();

	KAF2MateAll(kaf, tartekoaTmpFitx);
     
        String modeloa = baliabideDir + "/prs.model";
	String anna = baliabideDir + "/mate-tools/anna-3.3.jar";
        String helbideOsoa = "java -Xmx8G -classpath " + anna + " is2.parser.Parser -model " 
	                     + modeloa + " -test " + tartekoaTmpFitxIzena + " -out " 
	                     + parsedTmpFitxIzena;
        
        try {
	    Process process = Runtime.getRuntime().exec(helbideOsoa);
	    
	    String errorStr = "";
	    BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	    while ((errorStr = errorStream.readLine()) != null) {
		System.err.println("[ERROREA anna]: " + errorStr);
	    }
	    errorStream.close();
	    process.waitFor();
        } catch(Exception errorea) {
            System.err.println("Salbuespena: " + errorea.toString());
        }

        BufferedReader br = new BufferedReader(new FileReader(parsedTmpFitx));
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
                String kategoria = zatiak[4];
                String azpikategoria = zatiak[5];
                String feats = zatiak[6];
                int head = Integer.parseInt(zatiak[9]);
                String deprel = zatiak[11];
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


    public void KAF2MateAll(KAFDocument kaf, File tartekoaTmpFitx) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(tartekoaTmpFitx));
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
		        + "\t" + kategoria + "\t" + azpikategoria + "\t" + feats + "\t" 
		        + feats + "\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\n";
	  } else {
	      emaitza = unekoarenZenbakia + "\t" + hitza + "\t" + lema + "\t" + lema 
		        + "\t" + kategoria + "\t" + azpikategoria + "\t" + feats + "\t" 
		        + feats + "\t_\t_\t_\t_\t_\t_\t_\t_\t_\t_\n";
	  }
	  bw.write(emaitza); 
	}
        bw.close();
    }

    
    private String lortuKategoria(String[] zatiak) {
        String emaitza = "_";
        if (!zatiak[0].contains("@") && !zatiak[0].contains("BIZ") 
	        && !zatiak[0].contains("EZEZAG")) {
	    emaitza = zatiak[0];
	} else if(zatiak[0].contains("EZEZAG") && zatiak[1].contains("IZE")) {
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
	String emaitza = join(denak, "|");
	if (emaitza.contentEquals("")) {
	    emaitza = "_";
        }
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
        for (int i=0; i<zatiak.length; i++){
	    if (zatiak[i].contentEquals("A1") || zatiak[i].contentEquals("A2")
		    || zatiak[i].contentEquals("A3")|| zatiak[i].contentEquals("A4")
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
        for (int i=0; i<zatiak.length; i++){
	    if (zatiak[i].contains("NR_")) {
		String[] zat = zatiak[i].split("_");
		emaitza = "NOR=" + zat[1];
	    }
        }
        
        return emaitza;
    }


    private String lortuNk(String[] zatiak) {
        String emaitza = "";
        for (int i=0; i<zatiak.length; i++){
	    if (zatiak[i].contains("NK_")) {
		String[] zat = zatiak[i].split("_");
		emaitza = "NORK=" + zat[1];              
            }
        }
        
        return emaitza;
    }


    private String lortuNi(String[] zatiak) {
        String emaitza = "";
        for (int i=0; i<zatiak.length; i++){
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


    private void inprimatu(String conllHelbidea, Vector<String> inprimatzekoa) throws IOException {
	String ruta = conllHelbidea;
        File archivo = new File(ruta);
        BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
        
        for (int i=0; i<inprimatzekoa.size(); i++) {
	    bw.write(inprimatzekoa.elementAt(i));
        }
        bw.close();
    }
    
}
