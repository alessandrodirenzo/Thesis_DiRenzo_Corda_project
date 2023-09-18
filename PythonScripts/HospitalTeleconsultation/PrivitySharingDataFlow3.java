public class PrivitySharingDataFlow3{

            @InitiatingFlow
            @StartableByRPC
            public static class PrivitySharingDataFlow3Initiator extends FlowLogic<SignedTransaction> {
            
            private Party initiator;
            private List<Party> receivers;
            
            public PrivitySharingDataFlow3Initiator( List<Party> receivers) {
            this.receivers = receivers;
            }
            
            @Override
            @Suspendable
            public SignedTransaction call() throws FlowException {
            
            this.initiator = getOurIdentity();
            
            final Party notary = getServiceHub().getNetworkMapCache().getNotary(CordaX500Name.parse("O=Notary,L=Milan,C=IT")); 
            
            
             Vault.Page<HospitalTeleconsultation> results = getServiceHub().getVaultService()
                    .queryBy(HospitalTeleconsultation.class);

            List<StateAndRef<HospitalTeleconsultation>> states = results.getStates();

            final StateAndRef inputState = states.get(0);

            final HospitalTeleconsultation input= (HospitalTeleconsultation) inputState.getState().getData();

            Party notary = states.get(0).getState().getNotary();
            
         String message ="";
          
                 
                HospitalTeleconsultation output = new HospitalTeleconsultation( input.getidState(), message, initiator, receivers , false , false , false, false, false, false, false);
            
            
           
                 SecureHash attachmentHash = null;
            try {
                attachmentHash = SecureHash.parse(uploadAttachment(
                        pathSource,
                        getServiceHub(),
                        getOurIdentity(),
                        "nomefile")
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            
                  
            final TransactionBuilder builder = new TransactionBuilder(notary);
            
            builder.addOutputState(output);
            builder.addInputState(inputState);
         builder.addCommand(new HospitalTeleconsultationContract.Commands.PrivitySharingData3(), Arrays.asList(this.initiator.getOwningKey(), this.receivers.get(0).getOwningKey()));
         builder.addAttachment(attachmentHash);
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
            
            @InitiatedBy(PrivitySharingDataFlow3.PrivitySharingDataFlow3Initiator.class)
            public static class PrivitySharingDataFlow3Responder extends FlowLogic<Void>{
                 
            private FlowSession counterpartySession;
    
            //Constructor
            public PrivitySharingDataFlow3Responder(FlowSession counterpartySession) {
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