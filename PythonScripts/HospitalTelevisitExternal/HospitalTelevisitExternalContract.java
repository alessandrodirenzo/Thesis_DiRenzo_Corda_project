public class HospitalTelevisitExternalContract implements Contract{

                     public static final String ID = "com.template.contracts.HospitalTelevisitExternalContract";
                     @Override
                     public void verify(LedgerTransaction tx) {

                     final CommandData commandData = tx.getCommands().get(0).getValue();
                     
                       
            if (commandData instanceof Commands.Setappointment) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             

                   requireThat(require -> {
                        require.using("input state not present", tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData1) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.RejectionAssessment1) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.AcceptanceAssessment1) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Askforconsent) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Allowconsent) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData2) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Askforteleconsultation) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Sendavailability) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.DecisionSharingData1) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData3) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.AcceptanceAssessment2) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.RejectionAssessment2) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Sendclinicaldata) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData4) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Sendfinalreport) {
                  HospitalTelevisitExternal output = tx.outputsOfType(HospitalTelevisitExternal.class).get(0);
                  
             HospitalTelevisitExternal input = tx.inputsOfType(HospitalTelevisitExternal.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("message not null", !output.getmessage().equals(""));
                        return null;
                   });
                   
            }
         
                
                    }
                    public interface Commands extends CommandData {               
                    class Setappointment implements Commands {} 
            class PrivitySharingData1 implements Commands {} 
            class RejectionAssessment1 implements Commands {} 
            class AcceptanceAssessment1 implements Commands {} 
            class Askforconsent implements Commands {} 
            class Allowconsent implements Commands {} 
            class PrivitySharingData2 implements Commands {} 
            class Askforteleconsultation implements Commands {} 
            class Sendavailability implements Commands {} 
            class DecisionSharingData1 implements Commands {} 
            class PrivitySharingData3 implements Commands {} 
            class AcceptanceAssessment2 implements Commands {} 
            class RejectionAssessment2 implements Commands {} 
            class Sendclinicaldata implements Commands {} 
            class PrivitySharingData4 implements Commands {} 
            class Sendfinalreport implements Commands {} 
            
                }
            
}
