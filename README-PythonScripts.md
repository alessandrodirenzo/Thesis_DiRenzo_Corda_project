This file allows to run the scripts in order to generate the files for each step of the method proposed.
It is possible to generate the file for the running example (AffiliatedVisit) and for the three cases analyzed in the validation test (BirthCertificateIssue, HospitalTeleconsultation, HospitalTelevisitExternal). 

# Steps to execute in order to reproduce the generation of files through Python scripts


**Step 1:**

**From collaboration to choreography tasks**
Possible strings to assign at "FileName" variable in the script CollaborationtoChoreographyTasksGenerator.py:

  Affiliated visit: XMLDocuments/FromCollToChor/Input/AffiliatedVisitCollab.xml
  
  Birth certificate issue: XMLDocuments/FromCollToChor/Input/BirthCertificateIssueCollab.xml
  
  HospitalTeleconsultation: XMLDocuments/FromCollToChor/Input/HospitalTeleconsultationCollab.xml
  
  HospitalTelevisitExternal: XMLDocuments/FromCollToChor/Input/HospitalTelevisitExternalCollab.xml

1. Assign one of the strings

2. Run the script CollaborationtoChoreographyTasksGenerator.py. The output will be generated in 
   XMLDocuments/FromCollToChor/Output/<NameOfExample>ChoreographyDiagram.xml

3. Results are already present to observe them, but the file are overwritten in every run, thus we are sure to generate a new file each 
   time. For an additional check, delete the content of the XMLDocuments/FromCollToChor/Output folder and run the script again.
  
4. In case of error related to the path of file in input, extract the absolute path.
    

**Step 2:**

**Generation of additional tasks from choreography diagram enriched with security notations**

Possible strings to assign at "FileName" variable in the script AdditionalChoreographyTasksGenerator.py:

  Affiliated visit: XMLDocuments/AdditionalTasksGenerator/Input/AffiliatedVisitwithSecNot.xml
  
  Birth certificate issue: XMLDocuments/AdditionalTasksGenerator/Input/BirthCertificateIssuewithSecNot.xml
  
  HospitalTeleconsultation: XMLDocuments/AdditionalTasksGenerator/Input/HospitalTelecosultationwithSecNot.xml
  
  HospitalTelevisitExternal: XMLDocuments/AdditionalTasksGenerator/Input/HospitalTelevisitExternalwithSecNot.xml

1. Assign one of the strings

2. Run the script AdditionalChoreographyTasksGenerator.py. The output will be generated in 
   XMLDocuments/AdditionalTasksGenerator/Output/<NameOfExample>AdditionalTasksComp.xml

3. Results are already present to observe them, but the file are overwritten in every run, thus we are sure to generate a new file each 
   time. For an additional check, delete the content of the XMLDocuments/AdditionalTasksGenerator/Output folder and run the script again.
  
4. In case of error related to the path of file in input, extract the absolute path.


**Step 3:**

**From choreography diagram to Corda Java classes**

Possible strings to assign at "FileName" variable in the script FromChoreographyToCordaClassesGenerator.py:

  Affiliated visit: XMLDocuments/FromChorToCorda/AffiliatedVisitChorDiagram.xml
  
  Birth certificate issue: XMLDocuments/FromChorToCorda/BirthCertificateIssueChorDiagram.xml
  
  HospitalTeleconsultation: XMLDocuments/FromChorToCorda/HospitalTeleconsultationChorDiagram.xml
  
  HospitalTelevisitExternal: XMLDocuments/FromChorToCorda/HospitalTelevisitExternalChorDiagram.xml

1. Assign one of the strings
 
2. Before running the script FromChoreographyToCordaClassesGenerator.py, delete the file <NameOfExample>/nodes configuration.txt because 
   it is written in append mode. After that, run the script FromChoreographyToCordaClassesGenerator.py. The classes will be generated in 
   <NameOfExample>/ in addition with the "nodes configuration.txt". 

3. Results are already present to observe them, but the file are overwritten in every run, thus we are sure to generate a new file each 
   time. For an additional check, delete the content of the <NameOfExample>/ folder and run the script again.
  
4. In case of error related to the path of file in input, extract the absolute path.



**Additional step:**

**Generation of textual Corda template**

Possible strings to assign at "FileName" variable in the script FromChoreographyToCordaClassesGenerator.py:

  Affiliated visit: XMLDocuments/FromChorToCorda/AffiliatedVisitChorDiagram.xml
  
  Birth certificate issue: XMLDocuments/FromChorToCorda/BirthCertificateIssueChorDiagram.xml
  
  HospitalTeleconsultation: XMLDocuments/FromChorToCorda/HospitalTeleconsultationChorDiagram.xml
  
  HospitalTelevisitExternal: XMLDocuments/FromChorToCorda/HospitalTelevisitExternalChorDiagram.xml

1. Assign one of the strings

2. Run the script FromChoreographyToCordaTemplate.py. The output will be generated in 
   CordaTemplateTextualFiles/<NameOfExample>CordaTemplate.txt

3. Results are already present to observe them, but the file are overwritten in every run, thus we are sure to generate a new file each 
   time. For an additional check, delete the content of the CordaTemplateTextualFiles/ folder and run the script again.
  
4. In case of error related to the path of file in input, extract the absolute path.


