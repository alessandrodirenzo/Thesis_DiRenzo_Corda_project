public class BirthCertificateIssue implements ContractState{

        UniqueIdentifier idState;
    
        String message;
        Party initiator; 
        List<Party> receivers;
        
          boolean datashared1;
          boolean datashared2;
          boolean datashared3;
             
        public BirthCertificateIssue(UniqueIdentifier idState, String message, Party initiator, List<Party> receivers, boolean datashared1, boolean datashared2, boolean datashared3){
            this.idState = idState;
            this.message = message;
            this.initiator = initiator;
            this.receivers = receivers;
        this.datashared1 = datashared1;
        this.datashared2 = datashared2;
        this.datashared3 = datashared3;
        }
                
                public UniqueIdentifier getidState(){
                    return idState; 
                }
                
                public String getmessage(){
                    return message;
                }
                public Party getinitiator(){
                    return initiator;
                }
                public List<Party> getreceivers(){
                    return receivers;
                }
                   public boolean getdatashared1(){
                        return datashared1;
                    }
                  public boolean getdatashared2(){
                        return datashared2;
                    }
                  public boolean getdatashared3(){
                        return datashared3;
                    }
                 
    
        @Override
        public List<AbstractParty> getParticipants() {
            List<AbstractParty> entities= new ArrayList<AbstractParty>();
            entities.add(initiator);
            entities.addAll(receivers);
            return entities;
    }
}