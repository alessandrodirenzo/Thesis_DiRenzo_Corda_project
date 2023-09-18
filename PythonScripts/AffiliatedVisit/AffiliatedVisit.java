public class AffiliatedVisit implements ContractState{

        UniqueIdentifier idState;
    boolean accepted1;
      boolean rejected1;
boolean firstcategory1;

        String message;
        Party initiator; 
        List<Party> receivers;
        
          boolean datashared1;
          boolean secondcategory1;
          boolean datashared2;
             
        public AffiliatedVisit(UniqueIdentifier idState, String message, Party initiator, List<Party> receivers, boolean accepted1, boolean rejected1, boolean firstcategory1, boolean datashared1, boolean secondcategory, boolean datashared2){
            this.idState = idState;
            this.message = message;
            this.initiator = initiator;
            this.receivers = receivers;
        this.accepted1 = accepted1;
     this.rejected1 = rejected1;
     this.firstcategory1 = firstcategory1;
     this.datashared1 = datashared1;
        this.secondcategory1 = secondcategory1;
        this.datashared2 = datashared2;
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
                  
                    public boolean getaccepted1(){
                        return accepted1;
                    }
                    
                    public boolean getrejected1(){
                        return rejected1;
                    }
                    
                    public boolean getfirstcategory1(){
                        return firstcategory1;
                    }
             public boolean getdatashared1(){
                        return datashared1;
                    }
                  
                public boolean getsecondcategory1(){
                    return secondcategory1;
                }
                 public boolean getdatashared2(){
                        return datashared2;
                    }
                 
    
        @Override
        public List<AbstractParty> getParticipants() {
            List<AbstractParty> entities= new ArrayList<AbstractParty>();
            entities.add(initiator);
            entities.addAll(receivers);
            return entities;
    }
}