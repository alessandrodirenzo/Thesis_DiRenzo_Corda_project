package com.affiliatedvisit.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.affiliatedvisit.contracts.AffiliatedVisitContract;
import com.affiliatedvisit.states.AffiliatedVisit;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestAcceptedFlow {
    @InitiatingFlow
    @StartableByRPC
    public static class RequestAcceptedFlowInitiator extends FlowLogic<SignedTransaction> {
        //private variables
        private Party initiator;
        private Party receiver;

        //public constructor
        public RequestAcceptedFlowInitiator(Party receiver) {
            this.receiver = receiver;
        }

        @Override
        @Suspendable
        public SignedTransaction call() throws FlowException {


            this.initiator = getOurIdentity();

            Vault.Page<AffiliatedVisit> results = getServiceHub().getVaultService()
                    .queryBy(AffiliatedVisit.class);

            List<StateAndRef<AffiliatedVisit>> states = results.getStates();

            final StateAndRef inputState = states.get(0);

            final AffiliatedVisit input= (AffiliatedVisit) inputState.getState().getData();

            Party notary = states.get(0).getState().getNotary();

            final AffiliatedVisit output = new AffiliatedVisit(input.getIdState(), initiator, Arrays.asList(receiver), true, true, true, false, true,false,false,false, "Your request of affiliated visit has been completed successfully.");

            //Step 2. Send personal data to the counterparty
           // FlowSession otherPartySession = initiateFlow(receiver);

          //  String confirmation= "Your request of affiliated visit has been completed successfully.";

          //  otherPartySession.send(confirmation);

            // Step 3. Create a new TransactionBuilder object.
            final TransactionBuilder builder = new TransactionBuilder(notary);

            // Step 4. Add the iou as an output state, as well as a command to the transaction builder.
            builder.addInputState(inputState);
            builder.addOutputState(output);
            builder.addCommand(new AffiliatedVisitContract.Commands.RequestAccepted(), Arrays.asList(this.initiator.getOwningKey(), this.receiver.getOwningKey()));


            // Step 5. Verify and sign it with our KeyPair.
            builder.verify(getServiceHub());
            final SignedTransaction ptx = getServiceHub().signInitialTransaction(builder);
            ArrayList<AbstractParty> parties = new ArrayList<>();
            parties = (ArrayList) output.getParticipants();
            FlowSession other =null;
            List<FlowSession> signerFlows = parties.stream()
                    // We don't need to inform ourselves and we signed already.
                    .filter(it -> !it.equals(getOurIdentity()))
                    .map(this::initiateFlow)
                    .collect(Collectors.toList());
            for (AbstractParty party: input.getParticipants()){
                if (!output.getParticipants().contains(party)){
                  //  System.out.println(party);
                    other = initiateFlow(party);

                }
            }

            // Step 6. Collect the other party's signature using the SignTransactionFlow.
            final SignedTransaction fullySignedTx = subFlow(
                    new CollectSignaturesFlow(ptx, signerFlows, CollectSignaturesFlow.Companion.tracker()));

            signerFlows.add(other);
            return subFlow(new FinalityFlow(fullySignedTx, signerFlows));

        }
    }

    @InitiatedBy(RequestAcceptedFlow.RequestAcceptedFlowInitiator.class)
    public static class RequestAcceptedFlowResponder extends FlowLogic<Void> {
        //private variable
        private FlowSession counterpartySession;

        //Constructor
        public RequestAcceptedFlowResponder(FlowSession counterpartySession) {
            this.counterpartySession = counterpartySession;
        }

        @Suspendable
        @Override
        public Void call() throws FlowException {

            // Receive the expected message from the initiator
           // String receivedMessage = counterpartySession.receive(String.class).unwrap(data -> data);

           // System.out.println("Received message: " + receivedMessage);

            class SignTxFlow extends SignTransactionFlow {
                private SignTxFlow(FlowSession otherPartyFlow) {
                    super(otherPartyFlow);
                }

                @Override
                protected void checkTransaction(@NotNull SignedTransaction stx) throws FlowException {
                    //In this case there are no further checks to perform on the transaction
                }


            }
            //Stored the transaction into database.
            final SignTxFlow signTxFlow = new SignTxFlow(counterpartySession);
            final SecureHash txId = subFlow(signTxFlow).getId();

            subFlow(new ReceiveFinalityFlow(counterpartySession, txId));
            return null;
        }
    }
}
