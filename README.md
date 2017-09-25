
[[*euskaraz*](IRAKURRI.md)]

# ixa-pipe-dep-eu

[**ixa-pipe-dep-eu**](http://ixa2.si.ehu.es/ixakat/ixa-pipe-dep-eu.php?lang=en)
is a dependency parser for Basque written documents. It is a tool of
the [ixaKat](http://ixa2.si.ehu.es/ixakat/index.php?lang=en) modular
chain. It is based on the combination of the analyses obtained by
different parsers. More precisely,
[Mate](https://code.google.com/archive/p/mate-tools/) and
[MaltParser](http://maltparser.org/) parsers are used to obtain the
analyses, and MaltBlender tool is used to choose the best combination
of those analyses.

The tool takes a document in [NAF
format](http://wordpress.let.vupr.nl/naf/). This input document should
contain lemmas, PoS tags and morphological information. The input NAF
document containing the necessary linguistic information could be
obtained from the output of
[`ixa-pipe-pos-eu`](http://ixa2.si.ehu.es/ixakat/ixa-pipe-pos-eu.php?lang=en).


## Installation

There are two options in order to get this tool: [get the source code
and compile it](#from-source) or [use the pre-compiled
package](#using-pre-compiled-package). Anyway, some [linguistic
resources must be installed](#installing-the-linguistic-resources).

### From source

Installing the **ixa-pipe-dep-eu** requires the following steps:

* Install Java (JDK 1.7+)

* Install [Maven](https://maven.apache.org/download.cgi)

* Get module source code

      > git clone https://github.com/ixa-ehu/ixa-pipe-dep-eu.git

* Compile

      > cd ixa-pipe-dep-eu
      > mvn clean package

This step will create a directory called `target`  which contains
various directories and files. Most importantly, there you will find
the module executable:

    ixa-pipe-dep-eu-2.0.0-exec.jar

* Install the linguistic tools and resources as specified in [this
section](#installing-the-linguistic-resources).


### Using the pre-compiled package

Instead of compiling from source, you can download the pre-compiled
package that contains the executable file from the following link:
[ixa-pipe-dep-eu-v2.0.0.tgz](http://ixa2.si.ehu.es/ixakat/downloads/ixa-pipe-dep-eu-v2.0.0.tgz)

Decompress the package. The executable will be ready to use, without
any installation, but you have to follow the steps in [this
section](#installing-the-linguistic-resources) in order to install the
linguistic tools and resources needed.

To run the tool, Java should be installed in your computer.


### Installing the linguistic resources

Before starting using the tool, you have to follow the next steps in
order to install the necessary resources and dependencies.

 - Download the package of the resources from the following link:
   [dep-eu-resources-v2.0.0.tgz](http://ixa2.si.ehu.es/ixakat/downloads/dep-eu-resources-v2.0.0.tgz)

 - Decompress the package and update the `run.sh` executable file
   changing the `baliabideak` variable to specify the path of the
   `dep-eu-resources` directory you just obtained.


## How to use

The `ixa-pipe-dep-eu-2.0.0-exec.jar` executable is used to run the
**ixa-pipe-dep-eu** tool. The only required argument (`-b`) is the
path of the linguistic resources directory obtained in [this
section](#installing-the-linguistic-resources). The full command syntax
of `ixa-pipe-dep-eu-2.0.0-exec.jar` is

    > java -jar ixa-pipe-dep-eu-2.0.0-exec.jar [-h] -b RESOURCES_DIR [-c CONLL_FILE]

    arguments:
     -h                show this help message and exit
     -b RESOURCES_DIR [Required] Specify the path of the downloaded resource directory
     -c CONLL_FILE    [Optional] If you want to save the output also in CONLL format, specify the path of the output file


A executable script `run.sh` is provided to run the
**ixa-pipe-dep-eu** tool. You can use it, but before running it,
update the `rootDir` and `baliabideak` variables on this script as
specified in [this section](#installing-the-linguistic-resources).

This tool reads from standard input. It should be UTF-8 encoded NAF
format, containing lemmas, PoS tags and morphological annotations
(`text` and `terms` elements of NAF). The input NAF document
containing the necessary linguistic information could be obtained from
the output of
[`ixa-pipe-pos-eu`](http://ixa2.si.ehu.es/ixakat/ixa-pipe-pos-eu.php?lang=en).

Therefore, we can obtain syntactic dependencies of a plain text file
using the following comand:

    > cat test.txt | sh ixa-pipe-pos-eu/ixa-pipe-pos-eu.sh | sh ixa-pipe-dep-eu/run.sh

The output is written to standard output and it is in UTF-8 encoding
and NAF format. In the NAF output document the syntactic dependencies
will be marked by `deps` elements as it is shown in the example below:

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


## How to cite

If you use **ixa-pipe-dep-eu** tool, please cite the following paper in
your academic work:

Iakes Goenaga, Koldo Gojenola, Nerea Ezeiza. Combining Clustering
Approaches for Semi-Supervised Parsing: the BASQUE TEAM system in
the SPRML 2014 Shared Task. Workshop on Statistical Parsing of
Morphologically Rich Languages SPRML 2014 Shared Task, Dublin,
COLING
Workshop. 2014. ([*bibtex*](http://ixa2.si.ehu.es/ixakat/bib/goenaga2014.bib))


## License

All the original code produced for **ixa-pipe-dep-eu** is licensed under
[GPL v3](http://www.gnu.org/licenses/gpl-3.0.en.html) free license.

This software uses a external tool, and it is distributed with the
source code and the resources. This tool has its own license:

 - [mate-tools anna](http://code.google.com/p/mate-tools/): GNU General
   Public License, version 2

 - [MaltParser](http://code.google.com/p/mate-tools/): Copyright (C)
   2007-2017, Johan Hall, Jens Nilsson and Joakin
   Nivre. Redistribution and use in source and binary forms, with or
   without modification, are permitted.

 - [MaltOptimizer](http://nil.fdi.ucm.es/maltoptimizer/index.html):
   Copyright (C) 2011, Miguel Ballesteros and Joakin
   Nivre. Redistribution and use in source and binary forms, with or
   without modification, are permitted.


## Contact

Arantxa Otegi, arantza.otegi@ehu.eus  
Iakes Goenaga, iakes.goenaga@ehu.eus
 
