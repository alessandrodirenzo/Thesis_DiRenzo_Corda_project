public class SetappointmentFlow{

            @InitiatingFlow
            @StartableByRPC
            public static class SetappointmentFlowInitiator extends FlowLogic<SignedTransaction> {
            
            private Party initiator;
            private List<Party> receivers;
            
            public SetappointmentFlowInitiator( List<Party> receivers) {
            this.receivers = receivers;
            }
            
            @Override
            @Suspendable
            public SignedTransaction call() throws FlowException {
            
            this.initiator = getOurIdentity();
            
            final Party notary = getServiceHub().getNetworkMapCache().getNotary(CordaX500Name.parse("O=Notary,L=Milan,C=IT")); 
            
            final UniqueIdentifier idState = new UniqueIdentifier();
         String message = "prova";
          
                 
                HospitalTelevisitExternal output = new HospitalTelevisitExternal( idState, message, initiator, receivers , false , false , false, false , false , false, false, false, false, false, false, false, false);
            
            
           
                  
            final TransactionBuilder builder = new TransactionBuilder(notary);
            
            builder.addOutputState(output);
            
         builder.addCommand(new HospitalTelevisitExternalContract.Commands.Setappointment(), Arrays.asList(this.initiator.getOwningKey(), this.receivers.get(0).getOwningKey()));
         
            builder.verify(getServiceHub());
            final SignedTransaction ptx = getServiceHub().signInitialTransaction(builder);
            
            ArrayList<AbstractParty> parties = new ArrayList<>();
            parties = (ArrayList) output.getParticipants();
            
            List<FlowSession> signerFlows = parties.stream()
                    .filter(it -> !it.equals(getOurIdentity()))
                    .map(this::initiateFlow)
                    .collect(Collectors.toList());
        

            final SignedTransaction fullySignedTx = subFlow(
                    new CollectSignaturesFlow(ptx, signerFlows, CollectSignaturesFlow.Companion.tracker()));
                    
            return subFlow(new FinalityFlow(fullySignedTx, signerFlows));
                            
            }
            }
            
            @InitiatedBy(SetappointmentFlow.SetappointmentFlowInitiator.class)
            public static class SetappointmentFlowResponder extends FlowLogic<Void>{
                 
            private FlowSession counterpartySession;
    
            //Constructor
            public SetappointmentFlowResponder(FlowSession counterpartySession) {
                this.counterpartySession = counterpartySession;
            }
    
            @Suspendable
            @Override
            public Void call() throws FlowException {
    
                class SignTxFlow extends SignTransactionFlow {
                    private SignTxFlow(FlowSession otherPartyFlow) {
                        super(otherPartyFlow);
                    }
                    @Override
                    protected void checkTransaction(@NotNull SignedTransaction stx) throws FlowException {
                   
                    }
    
    
                }
             
                final SignTxFlow signTxFlow = new SignTxFlow(counterpartySession);
                final SecureHash txId = subFlow(signTxFlow).getId();
    
                subFlow(new ReceiveFinalityFlow(counterpartySession, txId));
                return null;
                 }
            } 
        
        
}