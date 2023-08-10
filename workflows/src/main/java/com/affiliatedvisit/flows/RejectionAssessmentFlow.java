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

import static net.corda.core.contracts.ContractsDSL.requireThat;
public class RejectionAssessmentFlow {
    @InitiatingFlow
    @StartableByRPC
    public static class RejectionAssessmentFlowInitiator extends FlowLogic<SignedTransaction>{
    //private variables
    private Party initiator ;
    private Party receiver;
    private UniqueIdentifier idLinState;
    //public constructor
    public RejectionAssessmentFlowInitiator(UniqueIdentifier idLinState, Party receiver) {

        this.idLinState = idLinState;
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

        final AffiliatedVisit output = new AffiliatedVisit(input.getIdState(), initiator,Arrays.asList(receiver), true, false, false, true, true,false,false,false);

        //Step 2. Send personal data to the counterparty
/*
        String rejdecision= "Rejection of the affiliated visit due to the pathology of employee not specified in the list";

        otherPartySession.send(rejdecision);
*/
        // Step 3. Create a new TransactionBuilder object.
        final TransactionBuilder builder = new TransactionBuilder(notary);

        // Step 4. Add the iou as an output state, as well as a command to the transaction builder.
        builder.addInputState(inputState);
        builder.addOutputState(output);
        builder.addCommand(new AffiliatedVisitContract.Commands.RejectionAssessment(), Arrays.asList(output.getParticipants().get(0).getOwningKey(), output.getParticipants().get(1).getOwningKey()));

        // Step 5. Verify and sign it with our KeyPair.
        builder.verify(getServiceHub());
        final SignedTransaction ptx = getServiceHub().signInitialTransaction(builder);

        ArrayList<AbstractParty> parties = new ArrayList<>();
        parties = (ArrayList) output.getParticipants();

        List<FlowSession> signerFlows = parties.stream()
                // We don't need to inform ourselves and we signed already.
                .filter(it -> !it.equals(getOurIdentity()))
                .map(this::initiateFlow)
                .collect(Collectors.toList());
        
        // Step 6. Collect the other party's signature using the SignTransactionFlow.
        final SignedTransaction fullySignedTx = subFlow(
                new CollectSignaturesFlow(ptx, signerFlows, CollectSignaturesFlow.Companion.tracker()));
        //signerFlows.add(extra);
        return subFlow(new FinalityFlow(fullySignedTx, signerFlows));

    }
}

@InitiatedBy(RejectionAssessmentFlow.RejectionAssessmentFlowInitiator.class)
public static class RejectionAssessmentFlowResponder extends FlowLogic<Void>{
    //private variable
    private FlowSession counterpartySession;

    //Constructor
    public RejectionAssessmentFlowResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {

        // Receive the expected message from the initiator
        /*
        String receivedMessage = counterpartySession.receive(String.class).unwrap(data -> data);

        System.out.println("Received message: " + receivedMessage);
        */
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
