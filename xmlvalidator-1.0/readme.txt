#############################################################
# Copyright (C) 2014 VeriSign, Inc.
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
#############################################################

This directory contains an XML Validator that can pre-parse 
and cache XML schemas used to validate passed XML files.

Run "xmlvalidator" on UNIX or "xmlvalidator.bat" on Windows 
to display the help for the XML Validator, that is included below:

$ xmlvalidator -help
usage: xmlvalidator[.bat] [-options] xmlFiles...
where options include:
 -help                Print help
 -schemasDir <arg>    XML schemas directory will prefix the file
                      references in schemasFile or specify the directory
                      to load *.xsd with the default directory of "./"
 -schemasFile <arg>   XML schemas file containing ordered list of XML
                      schema files to load, with the default of loading
                      *.xsd in the schemasDir
 -xmlDir <arg>        XML file directory automatically added as prefix to
                      xmlFiles
Examples:
xmlvalidator -schemasDir sample sample/good-domain-create.xml
xmlvalidator -schemasDir sample -xmlDir sample good-domain-create.xml
xmlvalidator -schemasDir sample -schemasFile sample/schemasfile.txt
sample/good-domain-create.xml
