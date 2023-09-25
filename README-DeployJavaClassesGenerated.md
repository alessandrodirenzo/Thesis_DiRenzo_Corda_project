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

7. Add the following imports: import net.corda.core.contracts.UniqueIdentifier; import java.util.ArrayList; import 
   net.corda.core.identity.AbstractParty;

8. Open cordapp-template-java > contracts > src.main.java.com.template.contracts > ContractState.java
   and substitute the entire code starting from "public class ContractState ..." with the code in the class 
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

In order to run the first transaction in the other examples, refer to the following lines:

# Hospital Televisit External: 

in the "Pediatric Patient" node -> flow start SetappointmentFlow$SetappointmentFlowInitiator receivers: "O= Hospital Staff,L=Milan,C=IT"

# Hospital Teleconsultation: 

in the "Family" node -> flow start AskfordoctoravailabilityFlow$AskfordoctoravailabilityFlowInitiator receivers: "O= Doctor,L=Milan,C=IT"

# Affiliated VIsit: 

in the "Company Employee" node -> flow start NewrequestofaffiliatedVisitFlow$NewrequestofaffiliatedVisitFlowInitiator receivers: "O= Health Care Fund,L=Milan,C=IT"

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
    
