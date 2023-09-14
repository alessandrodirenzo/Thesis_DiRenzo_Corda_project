public class RejectionAssessmentFlow1{

            @InitiatingFlow
            @StartableByRPC
            public static class RejectionAssessmentFlow1Initiator extends FlowLogic<SignedTransaction> {
            
            private Party initiator;
            private List<Party> receivers;
            
            public RejectionAssessmentFlow1Initiator( List<Party> receivers) {
            this.receivers = receivers;
            }
            
            @Override
            @Suspendable
            public SignedTransaction call() throws FlowException {
            
            this.initiator = getOurIdentity();
            
            final Party notary = getServiceHub().getNetworkMapCache().getNotary(CordaX500Name.parse("O=Notary,L=Milan,C=IT")); 
            
            
             Vault.Page<HospitalTelevisitExternal> results = getServiceHub().getVaultService()
                    .queryBy(HospitalTelevisitExternal.class);

            List<StateAndRef<HospitalTelevisitExternal>> states = results.getStates();

            final StateAndRef inputState = states.get(0);

            final HospitalTelevisitExternal input= (HospitalTelevisitExternal) inputState.getState().getData();

            Party notary = states.get(0).getState().getNotary();
            
         String message = "prova";
          
                 
                HospitalTelevisitExternal output = new HospitalTelevisitExternal( input.getidState(), message, initiator, receivers , false , false , false, false , false , false, false, false, false, false, false, false, false);
            
            
           
                  
            final TransactionBuilder builder = new TransactionBuilder(notary);
            
            builder.addOutputState(output);
            builder.addInputState(inputState);
         builder.addCommand(new HospitalTelevisitExternalContract.Commands.RejectionAssessment1(), Arrays.asList(this.initiator.getOwningKey(), this.receivers.get(0).getOwningKey()));
         
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
            
            @InitiatedBy(RejectionAssessmentFlow1.RejectionAssessmentFlow1Initiator.class)
            public static class RejectionAssessmentFlow1Responder extends FlowLogic<Void>{
                 
            private FlowSession counterpartySession;
    
            //Constructor
            public RejectionAssessmentFlow1Responder(FlowSession counterpartySession) {
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