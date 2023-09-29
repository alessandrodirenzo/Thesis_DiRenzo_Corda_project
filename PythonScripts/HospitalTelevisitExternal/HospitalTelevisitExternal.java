public class HospitalTelevisitExternal implements ContractState{

        UniqueIdentifier idState;
    boolean accepted1;
      boolean rejected1;
boolean firstcategory1;
boolean accepted2;
      boolean rejected2;
boolean firstcategory2;

        String message;
        Party initiator; 
        List<Party> receivers;
        
          boolean datashared1;
          boolean secondcategory1;
          boolean datashared2;
          boolean decisiondatashared1;
          boolean datashared3;
          boolean secondcategory2;
          boolean datashared4;
             
        public HospitalTelevisitExternal(UniqueIdentifier idState, String message, Party initiator, List<Party> receivers, boolean accepted1, boolean rejected1, boolean firstcategory1, boolean accepted2, boolean rejected2, boolean firstcategory2, boolean datashared1, boolean secondcategory1, boolean datashared2, boolean decisiondatashared1, boolean datashared3, boolean secondcategory2, boolean datashared4){
            this.idState = idState;
            this.message = message;
            this.initiator = initiator;
            this.receivers = receivers;
        this.accepted1 = accepted1;
     this.rejected1 = rejected1;
     this.firstcategory1 = firstcategory1;
     this.accepted2 = accepted2;
     this.rejected2 = rejected2;
     this.firstcategory2 = firstcategory2;
     this.datashared1 = datashared1;
        this.secondcategory1 = secondcategory1;
        this.datashared2 = datashared2;
        this.decisiondatashared1 = decisiondatashared1;
        this.datashared3 = datashared3;
        this.secondcategory2 = secondcategory2;
        this.datashared4 = datashared4;
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
            
                    public boolean getaccepted2(){
                        return accepted2;
                    }
                    
                    public boolean getrejected2(){
                        return rejected2;
                    }
                    
                    public boolean getfirstcategory2(){
                        return firstcategory2;
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
                  
            public boolean getdecisiondatashared1(){
                return decisiondatashared1;
            }
             public boolean getdatashared3(){
                        return datashared3;
                    }
                  
                public boolean getsecondcategory2(){
                    return secondcategory2;
                }
                 public boolean getdatashared4(){
                        return datashared4;
                    }
                 
    
        @Override
        public List<AbstractParty> getParticipants() {
            List<AbstractParty> entities= new ArrayList<AbstractParty>();
            entities.add(initiator);
            entities.addAll(receivers);
            return entities;
    }
}
