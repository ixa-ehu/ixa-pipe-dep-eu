# ixa-pipe-dep-eu

[**ixa-pipe-dep-eu**](http://ixa2.si.ehu.es/ixakat/ixa-pipe-dep-eu.php)
euskaraz idatzitako testuetarako dependentzia etiketatzailea
da. [ixaKat](http://ixa2.si.ehu.es/ixakat/index.php) kate modularreko
tresna bat da. [Mate](https://code.google.com/archive/p/mate-tools/)
etiketatzailearen grafoetan oinarritutako bertsioan oinarritzen da eta
Java programazio lengoaian inplementatua dago.

Tresna honek [NAF formatu](http://wordpress.let.vupr.nl/naf/)n dagoen
dokumentu bat hartzen du sarrera moduan. Sarrerako dokumentu horrek
lemak, kategoriak eta informazio morfologikoa izan behar
ditu. Sarreran beharreko informazio linguistiko hori duen NAF
dokumentua
[`ixa-pipe-pos-eu`](http://ixa2.si.ehu.es/ixakat/ixa-pipe-pos-eu.php)
tresnaren irteeran lortzen da.


## Instalazioa

Tresna hau lortzeko bi aukera daude: [iturburu-kodea lortu eta konpilatu](iturburu-kodea-erabiliz) edo [aurrekonpilatutako paketea erabili](aurrekonpilatutako-paketea-erabiliz). Bi kasuetan, [baliabide linguistikoak instalatu](baliabide-linguistikoak-instalatu) behar dira.


### Iturburu-kodea erabiliz

**ixa-pipe-dep-eu** instalatzeko ondorengo urratsak jarraitu behar dira:

* JDK 1.7 instalatu

* MAVEN 3 instalatu, adibidez, honako paketea deskargatuta:
  [apache-maven-3.04-bin.tar.gz](http://ftp.udc.es/apache/maven/maven-3/3.0.4/binaries/apache-maven-3.0.4-bin.tar.gz)

* Iturburu-kodea lortu

      > git clone https://github.com/ixa-ehu/ixa-pipe-dep-eu.git

* Konpilatu

      > cd ixa-pipe-dep-eu
      > mvn clean package

Honela, `target` izeneko direktorio bat sortuko da, eta bertan, beste
hainbat fitxategiren artean, exekutagarri hau aurkituko duzu:

    ixa-pipe-dep-eu-v1.0.0-exec.jar

* Instalatu tresna eta baliabide linguistikoak [atal
honetan](baliabide-linguistikoak-instalatu) azaldutako urratsak
jarraituz.


### Aurrekonpilatutako paketea erabiliz

Ituruburu-kodea konpilatu beharrean, exekutagarria duen pakete
aurrekonpilatua ondorengo esteka honetatik deskarga daiteke:
[ixa-pipe-dep-eu-v1.0.0.tgz](http://ixa2.si.ehu.es/ixakat/downloads/ixa-pipe-dep-eu-v1.0.0.tgz)

Paketea deskonprimitu. Exekutagarria erabiltzeko prest egongo da
inongo instalaziorik egin gabe, baina [atal
honetan](baliabide-linguistikoak-instalatu) azaldutako urratsak
jarraitu beharko dituzu beharrezko tresna eta baliabide linguistikoak
instalatzeko.

Tresna hau exekutatzeko Java instalatuta eduki beharko duzu zure makinan.


### Baliabide linguistikoak instalatu

Tresna hau erabiltzen hasi aurretik, ondorengo urratsak jarraitu beharko
dituzu beharrezko tresna eta baliabide linguistikoak erabilgarri
jartzeko:

 - Deskargatu baliabideen paketea hemendik:
   [dep-eu-resources-v1.0.0.tgz](http://ixa2.si.ehu.es/ixakat/downloads/dep-eu-resources-v1.0.0.tgz)

 - Deskonprimitu pakete hori eta eguneratu `run.sh` fitxategi
   exekutagarria `baliabideak` aldagaiean adieraziz lortu berri duzun
   `dep-eu-resources` direktorioaren kokapena.


## Nola erabili

`ixa-pipe-dep-eu-1.0.0-exec.jar` exekutagarria erabili behar da
**ixa-pipe-dep-eu** tresna exekutatzeko. Honen derrigorrezko argumentu
bakarra (`-b`) deskargatu atalean eskuragarri dagoen baliabideen
direktorioaren kokapena da. `ixa-pipe-dep-eu-1.0.0-exec.jar` komandoaren
sintaxi osoa honakoa da:

    > java -jar ixa-pipe-dep-eu-1.0.0-exec.jar [-h] -b BALIABIDEAK_DIR [-c CONLL_FITX]

    argumentuak:
     -h     mezu hau erakutsi eta irten
     -b BALIABIDEAK_DIR [Beharrezkoa] Zehaztu deskargatutako baliabideen direktorioaren kokapena.
     -c CONLL_FITX [Aukerazkoa] Irteera CONLL formatuan ere gorde nahi baduzu, zehaztu irteerako fitxategiaren kokapena.

`run.sh` script exekutagarria eskuragarri jarri da tresna exekutatu
ahal izateko. Erabil dezakezu, baina exekutatu aurretik, eguneratu
`rootDir` eta `baliabideak` aldagaiak [atal
honetan](baliabide-linguistikoak-instalatu) adierazitako moduan.

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

Arantxa Otegi, Nerea Ezeiza, Iakes Goenaga, Gorka Labaka. A Modular
Chain of NLP Tools for Basque. In Proceedings of the 19th
International Conference on Text, Speech and Dialogue - TSD 2016,
Brno, Czech Republic, volume 9924 of Lecture Notes in Artificial
Intelligence, pp. 93-100. 2016. ([*bibtex*](http://ixa2.si.ehu.es/ixakat/bib/otegi2016.bib))


## Lizentzia

**ixa-pipe-dep-eu**-rako sortu den jatorrizko kode guztia [GPL
v3](http://www.gnu.org/licenses/gpl-3.0.en.html) lizentzia librera
atxikiturik dago.

Software honek kanpoko tresna bat erabiltzen du, eta kode eta
baliabideekin batera banatzen dugu. Tresna honek bere lizentzia du:  
 - [mate-tools anna](http://code.google.com/p/mate-tools/): GNU General
   Public License, version 2


## Kontaktua

Arantxa Otegi, arantza.otegi@ehu.eus  
Iakes Goenaga, iakes.goenaga@ehu.eus

