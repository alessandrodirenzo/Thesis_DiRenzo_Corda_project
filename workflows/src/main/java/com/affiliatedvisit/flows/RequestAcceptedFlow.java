package com.affiliatedvisit.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.affiliatedvisit.contracts.AffiliatedVisitContract;
import com.affiliatedvisit.states.AffiliatedVisit;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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

            // Step 1. Get a reference to the notary service on our network and our key pair.
            /** Explicit selection of notary by CordaX500Nam*/
            final Party notary = getServiceHub().getNetworkMapCache().getNotary(CordaX500Name.parse("O=Notary,L=Milan,C=IT"));


            final AffiliatedVisit output = new AffiliatedVisit(null, initiator, Arrays.asList(receiver), true, true, true, false, true,false,false,false);

            //Step 2. Send personal data to the counterparty
            FlowSession otherPartySession = initiateFlow(receiver);

            String confirmation= "Your request of affiliated visit has been completed successfully.";

            otherPartySession.send(confirmation);

            // Step 3. Create a new TransactionBuilder object.
            final TransactionBuilder builder = new TransactionBuilder(notary);

            // Step 4. Add the iou as an output state, as well as a command to the transaction builder.
            builder.addOutputState(output);
            builder.addCommand(new AffiliatedVisitContract.Commands.NewRequestOfAffiliatedVisit(), Arrays.asList(this.initiator.getOwningKey(), this.receiver.getOwningKey()));


            // Step 5. Verify and sign it with our KeyPair.
            builder.verify(getServiceHub());
            final SignedTransaction ptx = getServiceHub().signInitialTransaction(builder);

            // Step 6. Collect the other party's signature using the SignTransactionFlow.

            final SignedTransaction fullySignedTx = subFlow(
                    new CollectSignaturesFlow(ptx, Arrays.asList(otherPartySession), CollectSignaturesFlow.Companion.tracker()));
            return subFlow(new FinalityFlow(fullySignedTx, Arrays.asList(otherPartySession)));

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
            String receivedMessage = counterpartySession.receive(String.class).unwrap(data -> data);

            System.out.println("Received message: " + receivedMessage);

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