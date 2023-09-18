public class HospitalTeleconsultationContract implements Contract{

                     public static final String ID = "com.template.contracts.HospitalTeleconsultationContract";
                     @Override
                     public void verify(LedgerTransaction tx) {

                     final CommandData commandData = tx.getCommands().get(0).getValue();
                     
                       
            if (commandData instanceof Commands.Askfordoctoravailability) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             

                   requireThat(require -> {
                        require.using("input state not present", tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Scheduledappointment) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             HospitalTeleconsultation input = tx.inputsOfType(HospitalTeleconsultation.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Allownexttask) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             HospitalTeleconsultation input = tx.inputsOfType(HospitalTeleconsultation.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Consentnotification) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             HospitalTeleconsultation input = tx.inputsOfType(HospitalTeleconsultation.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData1) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             HospitalTeleconsultation input = tx.inputsOfType(HospitalTeleconsultation.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.RejectionAssessment1) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             HospitalTeleconsultation input = tx.inputsOfType(HospitalTeleconsultation.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.AcceptanceAssessment1) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             HospitalTeleconsultation input = tx.inputsOfType(HospitalTeleconsultation.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Interviewpatient) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             HospitalTeleconsultation input = tx.inputsOfType(HospitalTeleconsultation.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.Explainsymptoms) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             HospitalTeleconsultation input = tx.inputsOfType(HospitalTeleconsultation.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             require.using("Message not null", !output.getMessage().equals(""));
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData2) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             HospitalTeleconsultation input = tx.inputsOfType(HospitalTeleconsultation.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
          
            if (commandData instanceof Commands.PrivitySharingData3) {
                  HospitalTeleconsultation output = tx.outputsOfType(HospitalTeleconsultation.class).get(0);
                  
             HospitalTeleconsultation input = tx.inputsOfType(HospitalTeleconsultation.class).get(0);

                   requireThat(require -> {
                        require.using("input state present", !tx.getInputStates().isEmpty());
             
                        return null;
                   });
                   
            }
         
                
                    }
                    public interface Commands extends CommandData {               
                    class Askfordoctoravailability implements Commands {} 
            class Scheduledappointment implements Commands {} 
            class Allownexttask implements Commands {} 
            class Consentnotification implements Commands {} 
            class PrivitySharingData1 implements Commands {} 
            class RejectionAssessment1 implements Commands {} 
            class AcceptanceAssessment1 implements Commands {} 
            class Interviewpatient implements Commands {} 
            class Explainsymptoms implements Commands {} 
            class PrivitySharingData2 implements Commands {} 
            class PrivitySharingData3 implements Commands {} 
            
                }
            
}