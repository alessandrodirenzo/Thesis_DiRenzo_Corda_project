public class BirthCertificateIssueContract implements Contract{

                     public static final String ID = "com.template.contracts.BirthCertificateIssueContract";
                     @Override
                     public void verify(LedgerTransaction tx) {

                     final CommandData commandData = tx.getCommands().get(0).getValue();
                     
                       
            if (commandData instanceof Commands.Requestofnewbirthcertificate) {
                  BirthCertificateIssue output = tx.outputsOfType(BirthCertificateIssue.class).get(0);
                  
             

                   requireThat(require -> {
                        require.using("input state not present", tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData1) {
                  BirthCertificateIssue output = tx.outputsOfType(BirthCertificateIssue.class).get(0);
                  
             BirthCertificateIssue input = tx.inputsOfType(BirthCertificateIssue.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData2) {
                  BirthCertificateIssue output = tx.outputsOfType(BirthCertificateIssue.class).get(0);
                  
             BirthCertificateIssue input = tx.inputsOfType(BirthCertificateIssue.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Birthcertificateregistered) {
                  BirthCertificateIssue output = tx.outputsOfType(BirthCertificateIssue.class).get(0);
                  
             BirthCertificateIssue input = tx.inputsOfType(BirthCertificateIssue.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData3) {
                  BirthCertificateIssue output = tx.outputsOfType(BirthCertificateIssue.class).get(0);
                  
             BirthCertificateIssue input = tx.inputsOfType(BirthCertificateIssue.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
         
                
                    }
                    public interface Commands extends CommandData {               
                    class Requestofnewbirthcertificate implements Commands {} 
            class PrivitySharingData1 implements Commands {} 
            class PrivitySharingData2 implements Commands {} 
            class Birthcertificateregistered implements Commands {} 
            class PrivitySharingData3 implements Commands {} 
            
                }
            
}
