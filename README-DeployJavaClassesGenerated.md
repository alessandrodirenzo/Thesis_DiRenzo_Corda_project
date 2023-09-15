# System configuration:

1. Download IntelliJ
2. Java version required: Java 1.8.x  
3. Clone the repository at the link: https://github.com/corda/cordapp-template-java
4. Open the cloned project in IntelliJ environment

# Birth Certificate Issue 
**Substitute template Java classes with classes automatically generated**

1.Open in cordapp-template-java > contracts > .src.main.java.com.template.states >  templateState.java 
  and substitute the code starting after the line "@BelongsToContract(..)" with the code in the class BirthCertificateIssue.java at the path 
  /PythonScripts/BirthCertificateIssue/BirthCertificateIssue.java
  
2. Rename the class TemplateState.java in BirthCertificateIssue.java

3. Add the following imports: import net.corda.core.contracts.UniqueIdentifier; import java.util.ArrayList; import 
   net.corda.core.identity.AbstractParty;

4. Open in cordapp-template-java > contracts > src.main.java.com.template.contracts > ContractState.java
   and substitute the entire code starting from "public class ContractState ..." with the code in the class 
   BirthCertificateIssueContract.java at the path /PythonScripts/BirthCertificateIssue/BirthCertificateIssueContract.java

5. Rename the class ContractState.java in BirthCertificateIssueContract.java

6. Change the following import: import com.template.states.TemplateState; > import com.template.states.BirthCertificateIssue;

7. In class BirthCertificateIssue.java, change the following import:
   import com.template.contracts.ContractState; > import com.template.contracts.BirthCertificateIssueContract;

8. In class BirthCertificateIssue.java change the line @BelongsToContract(...) > @BelongsToContract(BirthCertificateIssueContract.class)

9. Open cordapp-template-java > workflows > src.main.java.com.template.flows > TemplateFlow.java
   and substitute the entire code in the class from "public class TemplateFlow ... " with the code in the class 
