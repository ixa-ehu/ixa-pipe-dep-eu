# ixa-pipe-dep-eu

[**ixa-pipe-dep-eu**](http://ixa2.si.ehu.es/ixakat/ixa-pipe-dep-eu.php)
euskaraz idatzitako testuetarako dependentzia etiketatzailea
da. [ixaKat](http://ixa2.si.ehu.es/ixakat/index.php) kate modularreko
tresna bat da. Analizatzaile desberdinek sortzen dituzten analisien
konbinazioan oinarritzen da. Hain zuzen ere, [Mate]
(https://code.google.com/archive/p/mate-tools/) eta
[MaltParser](http://maltparser.org/) analizatzaileak erabiltzen ditu
analisiak sortzeko, eta MaltBlender tresna hauen analisien
konbinaziorik onenak aukeratzeko. Java programazio lengoaian
inplementatua dago.

Tresna honek [NAF formatu](http://wordpress.let.vupr.nl/naf/)n dagoen
dokumentu bat hartzen du sarrera moduan. Sarrerako dokumentu horrek
lemak, kategoriak eta informazio morfologikoa izan behar
ditu. Sarreran beharreko informazio linguistiko hori duen NAF
dokumentua
[`ixa-pipe-pos-eu`](http://ixa2.si.ehu.es/ixakat/ixa-pipe-pos-eu.php)
tresnaren irteeran lortzen da.


## Instalazioa

Tresna hau lortzeko bi aukera daude: [iturburu-kodea lortu eta
konpilatu](#iturburu-kodea-erabiliz) edo [aurrekonpilatutako paketea
erabili](#aurrekonpilatutako-paketea-erabiliz). Bi kasuetan,
[baliabide linguistikoak
instalatu](#baliabide-linguistikoak-instalatu) behar dira.


### Iturburu-kodea erabiliz

**ixa-pipe-dep-eu** instalatzeko ondorengo urratsak jarraitu behar dira:

* Java instalatu (JDK 1.7+)

* [Maven](https://maven.apache.org/download.cgi) instalatu

* Iturburu-kodea lortu

      > git clone https://github.com/ixa-ehu/ixa-pipe-dep-eu.git

* Konpilatu

      > cd ixa-pipe-dep-eu
      > mvn clean package

Honela, `target` izeneko direktorio bat sortuko da, eta bertan, beste
hainbat fitxategiren artean, exekutagarri hau aurkituko duzu:

    ixa-pipe-dep-eu-2.0.0-exec.jar

* Instalatu tresna eta baliabide linguistikoak [atal
honetan](#baliabide-linguistikoak-instalatu) azaldutako urratsak
jarraituz.


### Aurrekonpilatutako paketea erabiliz

Ituruburu-kodea konpilatu beharrean, exekutagarria duen pakete
aurrekonpilatua ondorengo esteka honetatik deskarga daiteke:
[ixa-pipe-dep-eu-v2.0.0.tgz](http://ixa2.si.ehu.es/ixakat/downloads/ixa-pipe-dep-eu-v2.0.0.tgz)

Paketea deskonprimitu eta exekutagarria erabiltzeko prest egongo da
inongo instalaziorik egin gabe, baina [atal
honetan](#baliabide-linguistikoak-instalatu) azaldutako urratsak
jarraitu beharko dituzu beharrezko tresna eta baliabide linguistikoak
instalatzeko.

Tresna hau exekutatzeko Java instalatuta eduki beharko duzu zure makinan.


### Baliabide linguistikoak instalatu

Tresna hau erabiltzen hasi aurretik, ondorengo urratsak jarraitu beharko
dituzu beharrezko tresna eta baliabide linguistikoak erabilgarri
jartzeko:

 - Deskargatu baliabideen paketea hemendik:
   [dep-eu-resources-v2.0.0.tgz](http://ixa2.si.ehu.es/ixakat/downloads/dep-eu-resources-v2.0.0.tgz)

 - Deskonprimitu pakete hori eta eguneratu `run.sh` fitxategi
   exekutagarria `baliabideak` aldagaiean adieraziz lortu berri duzun
   `dep-eu-resources` direktorioaren kokapena.


## Nola erabili

`ixa-pipe-dep-eu-2.0.0-exec.jar` exekutagarria erabili behar da
**ixa-pipe-dep-eu** tresna exekutatzeko. Honen derrigorrezko argumentu
bakarra (`-b`) deskargatu atalean eskuragarri dagoen baliabideen
direktorioaren kokapena da. `ixa-pipe-dep-eu-2.0.0-exec.jar` komandoaren
sintaxi osoa honakoa da:

    > java -jar ixa-pipe-dep-eu-2.0.0-exec.jar [-h] -b BALIABIDEAK_DIR [-c CONLL_FITX]

    argumentuak:
     -h                  mezu hau erakutsi eta irten
     -b BALIABIDEAK_DIR [Beharrezkoa] Zehaztu deskargatutako baliabideen direktorioaren kokapena.
     -c CONLL_FITX      [Aukerazkoa] Irteera CONLL formatuan ere gorde nahi baduzu, zehaztu irteerako fitxategiaren kokapena.

`run.sh` script exekutagarria eskuragarri jarri da **ixa-pipe-dep-eu**
tresna exekutatu ahal izateko. Erabil dezakezu, baina exekutatu
aurretik, eguneratu `rootDir` eta `baliabideak` aldagaiak [atal
honetan](#baliabide-linguistikoak-instalatu) adierazitako moduan.

Tresna honek sarrera estandarretik irakurtzen du, eta sarrera horrek
UTF-8an kodetutako NAF formatuan dagoen dokumentua izan behar du,
lemak, kategoriak eta informazio morfologikoa dituena (NAF-eko `text`
eta `terms` elementuak). Sarreran beharreko informazio linguistiko
hori duen NAF dokumentua
[`ixa-pipe-pos-eu`](http://ixa2.si.ehu.es/ixakat/ixa-pipe-pos-eu.php)
tresnaren irteeran irteeran lortzen dena da.

Hortaz, testu gordina duen fitxategi bateko dependentzia sintaktikoak
lortzeko, honako komando hau erabil dezakezu:

    > cat test.txt | sh ixa-pipe-pos-eu/ixa-pipe-pos-eu.sh | sh ixa-pipe-dep-eu/run.sh

Tresnak irteera estandarrean idatziko du, UTF-8an kodetutatako NAF
formatuan. Irteerako NAF dokumentuan `deps` elementuen bidez
dependentzia sintaktikoak markatuta ageriko dira beheko adibide
honetan ikus daitekeen moduan (adibideko sarrerako esaldia honakoa da:
"*Donostiako Zinemaldiko sail ofizialean lehiatuko da Handia filma.*"):

    <deps>
      <!--ncsubj(da, Zinemaldiko)-->
      <dep from="t6" to="t2" rfunc="ncsubj" />
      <!--ncmod(lehiatuko, sail)-->
      <dep from="t5" to="t3" rfunc="ncmod" />
      <!--ncmod(sail, ofizialean)-->
      <dep from="t3" to="t4" rfunc="ncmod" />
      <!--xpred(da, lehiatuko)-->
      <dep from="t6" to="t5" rfunc="xpred" />
    </deps>


## Nola egin aipamena

**ixa-pipe-dep-eu** tresna erabiltzen baduzu, ondorengo lan honen
aipamena egin zure lan akademikoan mesedez:

 Iakes Goenaga, Koldo Gojenola, Nerea Ezeiza. Combining Clustering
  Approaches for Semi-Supervised Parsing: the BASQUE TEAM system in
  the SPRML 2014 Shared Task. Workshop on Statistical Parsing of
  Morphologically Rich Languages SPRML 2014 Shared Task, Dublin,
  COLING
  Workshop. 2014. ([*bibtex*](http://ixa2.si.ehu.es/ixakat/bib/goenaga2014.bib))


## Lizentzia

**ixa-pipe-dep-eu**-rako sortu den jatorrizko kode guztia [GPL
v3](http://www.gnu.org/licenses/gpl-3.0.en.html) lizentzia librera
atxikiturik dago.

Software honek kanpoko hainbat tresna erabiltzen ditu, eta kode eta
baliabideekin batera banatzen ditugu. Tresna hauek beren lizentzia eta
copyright jabeak dituzte:

 - [mate-tools anna](http://code.google.com/p/mate-tools/): GNU
General Public License, version 2

 - [MaltParser](http://code.google.com/p/mate-tools/): Copyright (C)
   2007-2017, Johan Hall, Jens Nilsson and Joakin
   Nivre. Redistribution and use in source and binary forms, with or
   without modification, are permitted.

 - [MaltOptimizer](http://nil.fdi.ucm.es/maltoptimizer/index.html):
   Copyright (C) 2011, Miguel Ballesteros and Joakin
   Nivre. Redistribution and use in source and binary forms, with or
   without modification, are permitted.


## Kontaktua

Arantxa Otegi, arantza.otegi@ehu.eus  
Iakes Goenaga, iakes.goenaga@ehu.eus

