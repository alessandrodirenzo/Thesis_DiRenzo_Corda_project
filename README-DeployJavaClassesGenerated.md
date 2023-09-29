This file allows the deployment of the cordApp composed of classes generated automatically by the script: FromChoreographyToCordaClassGenerator.py

This guide is related to the example of Birth Certificate Issue, but it can be readapted to the other examples by changing files from the folder of the example to deploy and related imports.

# System configuration:

1. Download IntelliJ
2. Java version required: Java 1.8.x  
3. Clone the repository at the link: https://github.com/corda/cordapp-template-java
4. Open the cloned project in IntelliJ environment

# Birth Certificate Issue example 

**Substitute template Java classes with classes automatically generated**

**IntelliJ**

5. Open cordapp-template-java > contracts > .src.main.java.com.template.states >  templateState.java 
  and substitute the code starting after the line "@BelongsToContract(..)" with the code in the class BirthCertificateIssue.java at the 
  path PythonScripts/BirthCertificateIssue/BirthCertificateIssue.java
  
6. Rename the class TemplateState.java in BirthCertificateIssue.java

7. Add the following imports (if they are not already present): import net.corda.core.contracts.UniqueIdentifier; import java.util.ArrayList; import 
   net.corda.core.identity.AbstractParty;

8. Open cordapp-template-java > contracts > src.main.java.com.template.contracts > ContractState.java
   and substitute the code starting from "public class ContractState ..." with the code in the class 
   BirthCertificateIssueContract.java at the path PythonScripts/BirthCertificateIssue/BirthCertificateIssueContract.java

9. Rename the class ContractState.java in BirthCertificateIssueContract.java

10. Change the following import: import com.template.states.TemplateState; > import com.template.states.BirthCertificateIssue;

11. In class BirthCertificateIssue.java, change the following import:
   import com.template.contracts.ContractState; > import com.template.contracts.BirthCertificateIssueContract;

12. In class BirthCertificateIssue.java change the line @BelongsToContract(...) > @BelongsToContract(BirthCertificateIssueContract.class)

13. Open cordapp-template-java > workflows > src.main.java.com.template.flows > TemplateFlow.java
   and substitute the entire code in the class from "public class TemplateFlow ... " with the code in the class 
   RequestofnewbirthcertificateFlow.java at the path PythonScripts/BirthCertificateIssue/RequestofnewbirthcertificateFlow.java

14. Rename the class TemplateFlow.java in RequestofnewbirthcertificateFlow.java

15. In class RequestofnewbirthcertificateFlow.java add the following imports:
    import com.template.contracts.ContractState; > import com.template.contracts.BirthCertificateIssueContract;
    import com.template.states.TemplateState; > import com.template.states.BirthCertificateIssue;
    import net.corda.core.contracts.UniqueIdentifier;
    import java.util.ArrayList;
    import net.corda.core.identity.AbstractParty;
    import org.jetbrains.annotations.NotNull;

16. Open build.gradle in cordapp-template-java > build.gradle, inside "task deployNodes(type: ...)" substitute the existing nodes with the nodes in the file "nodes configuration.txt" at the path PythonScripts/BirthCertificateIssue/nodes configuration.txt

**Terminal**

17. Move to the directory of cordapp-template-java

18. In order to deploy nodes, execute the command: ./gradlew clean deployNodes

It is better to have a number of terminal opened equal to the number of nodes, included Notary (in this case 3).

Terminal 1: 

19. In order to run node, move to the folder of the node: cd build/nodes/Citizen

20. Launch the following command: java -jar corda.jar

Terminal 2: 

21. In order to run node, move to the folder of the node: cd build/nodes/Notary

22. Launch the following command: java -jar corda.jar

Terminal 3: 

23. In order to run node, move to the folder of the node: cd build/nodes/Citizen registry birth certificate

24. Launch the following command: java -jar corda.jar

An example of nodes deployed can be found in [nodes deployed](https://github.com/alessandrodirenzo/Thesis_DiRenzo_Corda_project/blob/main/Diagrams%20images/CorDapp%20screens/Birth_Certificate_issue_DeployNodes.png).

**Possible problems with the running**
If a node presents a conflict situation, the problem is related to the port of localhost which is equal to another node already in running. In this case, to fix the problem it is necessary to open the file build.gradle and change the port of localhost in rpcSettings and p2pPort ensuring that they are different from the ports already used by other nodes. 
Example: p2pPort 10005 -> p2pPort 10007 
         address("localhost 10008") -> address("localhost 10009")

**After the nodes are running...**

On terminal 1:
**In order to execute a transaction**

 25: flow start RequestofnewbirthcertificateFlow$RequestofnewbirthcertificateFlowInitiator receivers: "O= Citizen registry birth certificate,L=Milan,C=IT"

 An example of transaction is reported in [transaction image](https://github.com/alessandrodirenzo/Thesis_DiRenzo_Corda_project/blob/main/Diagrams%20images/CorDapp%20screens/Birth_Certificate_issue_TransactionCitizen.png).

**In order to check the transaction correctly stored for Citizen**

 26: run vaultQuery contractStateType: com.template.states.BirthCertificateIssue

On terminal 3:
**In order to check the transaction correctly stored for Citizen registry birth certificate**

 27: run vaultQuery contractStateType: com.template.states.BirthCertificateIssue

 An example of query vault is represented in [vault CitizenRegistryBirthCertificate](https://github.com/alessandrodirenzo/Thesis_DiRenzo_Corda_project/blob/main/Diagrams%20images/CorDapp%20screens/Birth_Certificate_Issue_QueryVaultCitizenRegistryBirthCertificate.png) and [vault Citizen](https://github.com/alessandrodirenzo/Thesis_DiRenzo_Corda_project/blob/main/Diagrams%20images/CorDapp%20screens/Birth_Certificate_Issue_QueryVaultCitizen.png).


# Hospital Televisit External 

**Substitute template Java classes with classes automatically generated**

**IntelliJ**

5. Open cordapp-template-java > contracts > .src.main.java.com.template.states >  templateState.java 
  and substitute the code starting after the line "@BelongsToContract(..)" with the code in the class HospitalTelevisitExternal.java at the path PythonScripts/HospitalTelevisitExternal/HospitalTelevisitExternal.java
  
6. Rename the class TemplateState.java in HospitalTelevisitExternal.java

7. Add the following imports (if they are not already present): import net.corda.core.contracts.UniqueIdentifier; import java.util.ArrayList; import net.corda.core.identity.AbstractParty;

8. Open cordapp-template-java > contracts > src.main.java.com.template.contracts > ContractState.java
   and substitute the code starting from "public class ContractState ..." with the code in the class 
   HospitalTelevisitExternalContract.java at the path PythonScripts/HospitalTelevisitExternal/HospitalTelevisitExternalContract.java

9. Rename the class ContractState.java in HospitalTelevisitExternalContract.java

10. Change the following import: import com.template.states.TemplateState; > import com.template.states.HospitalTelevisitExternal;

11. In class HospitalTelevisitExternal.java, change the following import:
   import com.template.contracts.ContractState; > import com.template.contracts.HospitalTelevisitExternalContract;

12. In class HospitalTelevisitExternal.java change the line @BelongsToContract(...) > @BelongsToContract(HospitalTelevisitExternalContract.class)

13. Open cordapp-template-java > workflows > src.main.java.com.template.flows > TemplateFlow.java
   and substitute the entire code in the class from "public class TemplateFlow ... " with the code in the class 
    SetappointmentFlow.java at the path PythonScripts/HospitalTelevisitExternal/SetappointmentFlow.java

14. Rename the class TemplateFlow.java in SetappointmentFlow.java

15. In class SetappointmentFlow.java add the following imports:
    import com.template.contracts.ContractState; > import com.template.contracts.HospitalTelevisitExternalContract;
    import com.template.states.TemplateState; > import com.template.states.HospitalTelevisitExternal;
    import net.corda.core.contracts.UniqueIdentifier;
    import java.util.ArrayList;
    import net.corda.core.identity.AbstractParty;
    import org.jetbrains.annotations.NotNull;

16. Open build.gradle in cordapp-template-java > build.gradle, inside "task deployNodes(type: ...)" substitute the existing nodes with the nodes in the file "nodes configuration.txt" at the path PythonScripts/HospitalTelevisitExternal/nodes configuration.txt

**Terminal**

17. Move to the directory of cordapp-template-java

18. In order to deploy nodes, execute the command: ./gradlew clean deployNodes

It is better to have a number of terminal opened equal to the number of nodes, included Notary (in this case 3).

Terminal 1: 

19. In order to run node, move to the folder of the node: cd build/nodes/Family

20. Launch the following command: java -jar corda.jar

Terminal 2: 

21. In order to run node, move to the folder of the node: cd build/nodes/Notary

22. Launch the following command: java -jar corda.jar

Terminal 3: 

23. In order to run node, move to the folder of the node: cd build/nodes/Pediatric patient

25. Launch the following command: java -jar corda.jar

Terminal 4:

27. In order to run node, move to the folder of the node: cd build/nodes/Specialised group

28. Launch the following command: java -jar corda.jar

Terminal 5:

30. In order to run node, move to the folder of the node: cd build/nodes/Hospital Staff

31. Launch the following command: java -jar corda.jar

**Possible problems with the running**
If a node presents a conflict situation, the problem is related to the port of localhost which is equal to another node already in running. In this case, to fix the problem it is necessary to open the file build.gradle and change the port of localhost in rpcSettings and p2pPort ensuring that they are different from the ports already used by other nodes. 
Example: p2pPort 10005 -> p2pPort 10007 
         address("localhost 10008") -> address("localhost 10009")

**After the nodes are running...**

On terminal 3:
**In order to execute a transaction**

 25: flow start SetappointmentFlow$SetappointmentFlowInitiator receivers: "O= Hospital Staff,L=Milan,C=IT"

**In order to check the transaction correctly stored for Pediatric patient**

 26: run vaultQuery contractStateType: com.template.states.HospitalTelevisitExternal

On terminal 5:
**In order to check the transaction correctly stored for Hospital Staff**

 27: run vaultQuery contractStateType: com.template.states.HospitalTelevisitExternal

# Hospital Teleconsultation 

**Substitute template Java classes with classes automatically generated**

**IntelliJ**

5. Open cordapp-template-java > contracts > .src.main.java.com.template.states >  templateState.java 
  and substitute the code starting after the line "@BelongsToContract(..)" with the code in the class HospitalTeleconsultation.java at the path PythonScripts/HospitalTeleconsultation/HospitalTeleconsultation.java
  
6. Rename the class TemplateState.java in HospitalTeleconsultation.java

7. Add the following imports (if they are not already present): import net.corda.core.contracts.UniqueIdentifier; import java.util.ArrayList; import net.corda.core.identity.AbstractParty;

8. Open cordapp-template-java > contracts > src.main.java.com.template.contracts > ContractState.java
   and substitute the code starting from "public class ContractState ..." with the code in the class 
   HospitalTeleconsultationContract.java at the path PythonScripts/HospitalTeleconsultation/HospitalTeleconsultationContract.java

9. Rename the class ContractState.java in HospitalTeleconsultationContract.java

10. Change the following import: import com.template.states.TemplateState; > import com.template.states.HospitalTeleconsultation;

11. In class HospitalTeleconsultation.java, change the following import:
   import com.template.contracts.ContractState; > import com.template.contracts.HospitalTeleconsultationContract;

12. In class HospitalTeleconsultation.java change the line @BelongsToContract(...) > @BelongsToContract(HospitalTeleconsultationContract.class)

13. Open cordapp-template-java > workflows > src.main.java.com.template.flows > TemplateFlow.java
   and substitute the entire code in the class from "public class TemplateFlow ... " with the code in the class 
    AskfordoctoravailabilityFlow.java at the path PythonScripts/HospitalTeleconsultation/AskfordoctoravailabilityFlow.java

14. Rename the class TemplateFlow.java in AskfordoctoravailabilityFlow.java

15. In class AskfordoctoravailabilityFlow.java add the following imports:
    import com.template.contracts.ContractState; > import com.template.contracts.HospitalTeleconsultationContract;
    import com.template.states.TemplateState; > import com.template.states.HospitalTeleconsultation;
    import net.corda.core.contracts.UniqueIdentifier;
    import java.util.ArrayList;
    import net.corda.core.identity.AbstractParty;
    import org.jetbrains.annotations.NotNull;

16. Open build.gradle in cordapp-template-java > build.gradle, inside "task deployNodes(type: ...)" substitute the existing nodes with the nodes in the file "nodes configuration.txt" at the path PythonScripts/HospitalTeleconsultation/nodes configuration.txt

**Terminal**

17. Move to the directory of cordapp-template-java

18. In order to deploy nodes, execute the command: ./gradlew clean deployNodes

It is better to have a number of terminal opened equal to the number of nodes, included Notary (in this case 3).

Terminal 1: 

19. In order to run node, move to the folder of the node: cd build/nodes/Family

20. Launch the following command: java -jar corda.jar

Terminal 2: 

21. In order to run node, move to the folder of the node: cd build/nodes/Notary

22. Launch the following command: java -jar corda.jar

Terminal 3: 

23. In order to run node, move to the folder of the node: cd build/nodes/Pediatric patient

25. Launch the following command: java -jar corda.jar

Terminal 4:

27. In order to run node, move to the folder of the node: cd build/nodes/Doctor

28. Launch the following command: java -jar corda.jar

**Possible problems with the running**
If a node presents a conflict situation, the problem is related to the port of localhost which is equal to another node already in running. In this case, to fix the problem it is necessary to open the file build.gradle and change the port of localhost in rpcSettings and p2pPort ensuring that they are different from the ports already used by other nodes. 
Example: p2pPort 10005 -> p2pPort 10007 
         address("localhost 10008") -> address("localhost 10009")

**After the nodes are running...**

On terminal 1:
**In order to execute a transaction**

 25: flow start AskfordoctoravailabilityFlow$AskfordoctoravailabilityFlowInitiator receivers: "O= Doctor,L=Milan,C=IT"

**In order to check the transaction correctly stored for Family**

 26: run vaultQuery contractStateType: com.template.states.HospitalTeleconsultation

On terminal 4:
**In order to check the transaction correctly stored for Doctor**

 27: run vaultQuery contractStateType: com.template.states.HospitalTeleconsultation


# Affiliated Visit 

**Substitute template Java classes with classes automatically generated**

**IntelliJ**

5. Open cordapp-template-java > contracts > .src.main.java.com.template.states >  templateState.java 
  and substitute the code starting after the line "@BelongsToContract(..)" with the code in the class AffiliatedVisit.java at the path PythonScripts/AffiliatedVisit/AffiliatedVisit.java
  
6. Rename the class TemplateState.java in AffiliatedVisit.java

7. Add the following imports (if they are not already present): import net.corda.core.contracts.UniqueIdentifier; import java.util.ArrayList; import net.corda.core.identity.AbstractParty;

8. Open cordapp-template-java > contracts > src.main.java.com.template.contracts > ContractState.java
   and substitute the code starting from "public class ContractState ..." with the code in the class 
   AffiliatedVisitContract.java at the path PythonScripts/AffiliatedVisit/AffiliatedVisitContract.java

9. Rename the class ContractState.java in AffiliatedVisitContract.java

10. Change the following import: import com.template.states.TemplateState; > import com.template.states.AffiliatedVisit;

11. In class HospitalTeleconsultation.java, change the following import:
   import com.template.contracts.ContractState; > import com.template.contracts.AffiliatedVisitContract;

12. In class AffiliatedVisit.java change the line @BelongsToContract(...) > @BelongsToContract(AffiliatedVisitContract.class)

13. Open cordapp-template-java > workflows > src.main.java.com.template.flows > TemplateFlow.java
   and substitute the entire code in the class from "public class TemplateFlow ... " with the code in the class 
    NewRequestofAffiliatedVisitFlow.java at the path PythonScripts/AffiliatedVisit/NewRequestofAffiliatedVisitFlow.java

14. Rename the class TemplateFlow.java in NewRequestofAffiliatedVisitFlow.java

15. In class NewRequestofAffiliatedVisitFlow.java add the following imports:
    import com.template.contracts.ContractState; > import com.template.contracts.AffiliatedVisitContract;
    import com.template.states.TemplateState; > import com.template.states.AffiliatedVisit;
    import net.corda.core.contracts.UniqueIdentifier;
    import java.util.ArrayList;
    import net.corda.core.identity.AbstractParty;
    import org.jetbrains.annotations.NotNull;

16. Open build.gradle in cordapp-template-java > build.gradle, inside "task deployNodes(type: ...)" substitute the existing nodes with the nodes in the file "nodes configuration.txt" at the path PythonScripts/AffiliatedVisit/nodes configuration.txt

**Terminal**

17. Move to the directory of cordapp-template-java

18. In order to deploy nodes, execute the command: ./gradlew clean deployNodes

It is better to have a number of terminal opened equal to the number of nodes, included Notary (in this case 3).

Terminal 1: 

19. In order to run node, move to the folder of the node: cd build/nodes/Company Employee

20. Launch the following command: java -jar corda.jar

Terminal 2: 

21. In order to run node, move to the folder of the node: cd build/nodes/Notary

22. Launch the following command: java -jar corda.jar

Terminal 3: 

23. In order to run node, move to the folder of the node: cd build/nodes/Health Care Fund

25. Launch the following command: java -jar corda.jar

Terminal 4:

27. In order to run node, move to the folder of the node: cd build/nodes/Medical Office

28. Launch the following command: java -jar corda.jar

**Possible problems with the running**
If a node presents a conflict situation, the problem is related to the port of localhost which is equal to another node already in running. In this case, to fix the problem it is necessary to open the file build.gradle and change the port of localhost in rpcSettings and p2pPort ensuring that they are different from the ports already used by other nodes. 
Example: p2pPort 10005 -> p2pPort 10007 
         address("localhost 10008") -> address("localhost 10009")

**After the nodes are running...**

On terminal 1:
**In order to execute a transaction**

 25: flow start NewrequestofaffiliatedVisitFlow$NewrequestofaffiliatedVisitFlowInitiator receivers: "O= Health Care Fund,L=Milan,C=IT"

**In order to check the transaction correctly stored for Company Employee**

 26: run vaultQuery contractStateType: com.template.states.AffiliatedVisit

On terminal 3:
**In order to check the transaction correctly stored for Health Care Fund**

 27: run vaultQuery contractStateType: com.template.states.AffiliatedVisit

# Optional: inclusion of all the workflows generated by the scripts

28.  In cordapp-template-java > workflows > src.main.java.com.template.flows >
     create as many java classes as the classes present in the path PythonScripts/BirthcertificateIssue/*.java (in this case there are 
     other 4 workflows to include: PrivitySharingDataFlow1.java, PrivitySharingDataFlow2.java, BirthcertificateregisteredFlow.java, 
     PrivitySharingDataFlow3.java)

29.  Copy the import from the class RequestofnewcertificateFlow.java

30.  Inside the PrivitySharingDataFlow*.java classes, substitute "filename", in the attachmentHash parameters, with the path of a zip 
     file in the local device in order to simulate the sharing of the document through the attachment.

31. Redploy nodes

32. In the Citizen node, run the following commands:
    flow start RequestofnewbirthcertificateFlow$RequestofnewbirthcertificateFlowInitiator receivers: "O= Citizen registry birth 
    certificate,L=Milan,C=IT"

    flow start PrivitySharingDataFlow1$PrivitySharingDataFlow1Initiator receivers: "O= Citizen registry birth 
    certificate,L=Milan,C=IT"

33. Run on both the nodes: run vaultQuery contractStateType: com.template.states.BirthCertificateIssue
    There are both transactions in the vault, with the attachment present in the second one.
    
