public class RequestofnewbirthcertificateFlow{

            @InitiatingFlow
            @StartableByRPC
            public static class RequestofnewbirthcertificateFlowInitiator extends FlowLogic<SignedTransaction> {
            
            private Party initiator;
            private List<Party> receivers;
            
            public RequestofnewbirthcertificateFlowInitiator( List<Party> receivers) {
            this.receivers = receivers;
            }
            
            @Override
            @Suspendable
            public SignedTransaction call() throws FlowException {
            
            this.initiator = getOurIdentity();
            
            final Party notary = getServiceHub().getNetworkMapCache().getNotary(CordaX500Name.parse("O=Notary,L=Milan,C=IT")); 
            
            final UniqueIdentifier idState = new UniqueIdentifier();
         String message = "prova";
          
                 
                BirthCertificateIssue output = new BirthCertificateIssue( idState, message, initiator, receivers , false, false, false);
            
            
           
                  
            final TransactionBuilder builder = new TransactionBuilder(notary);
            
            builder.addOutputState(output);
            
         builder.addCommand(new BirthCertificateIssueContract.Commands.Requestofnewbirthcertificate(), Arrays.asList(this.initiator.getOwningKey(), this.receivers.get(0).getOwningKey()));
         
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
            
            @InitiatedBy(RequestofnewbirthcertificateFlow.RequestofnewbirthcertificateFlowInitiator.class)
            public static class RequestofnewbirthcertificateFlowResponder extends FlowLogic<Void>{
                 
            private FlowSession counterpartySession;
    
            //Constructor
            public RequestofnewbirthcertificateFlowResponder(FlowSession counterpartySession) {
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