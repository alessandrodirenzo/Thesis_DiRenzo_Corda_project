public class AffiliatedVisitContract implements Contract{

                     public static final String ID = "com.template.contracts.AffiliatedVisitContract";
                     @Override
                     public void verify(LedgerTransaction tx) {

                     final CommandData commandData = tx.getCommands().get(0).getValue();
                     
                       
            if (commandData instanceof Commands.NewRequestofAffiliatedVisit) {
                  AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
                  
             

                   requireThat(require -> {
                        require.using("input state not present", tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData1) {
                  AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
                  
             AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.AcceptanceAssessment1) {
                  AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
                  
             AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Requestaccepted) {
                  AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
                  
             AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.RejectionAssessment1) {
                  AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
                  
             AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Requestrejected) {
                  AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
                  
             AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Newvisitrequest) {
                  AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
                  
             AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Recapconventionwithavailabledatesforbooking) {
                  AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
                  
             AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData2) {
                  AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
                  
             AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
         
                
                    }
                    public interface Commands extends CommandData {               
                    class NewRequestofAffiliatedVisit implements Commands {} 
            class PrivitySharingData1 implements Commands {} 
            class AcceptanceAssessment1 implements Commands {} 
            class Requestaccepted implements Commands {} 
            class RejectionAssessment1 implements Commands {} 
            class Requestrejected implements Commands {} 
            class Newvisitrequest implements Commands {} 
            class Recapconventionwithavailabledatesforbooking implements Commands {} 
            class PrivitySharingData2 implements Commands {} 
            
                }
            
}