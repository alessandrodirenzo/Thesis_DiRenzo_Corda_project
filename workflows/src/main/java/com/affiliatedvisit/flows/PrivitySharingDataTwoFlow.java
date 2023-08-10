package com.affiliatedvisit.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.affiliatedvisit.contracts.AffiliatedVisitContract;
import com.affiliatedvisit.states.AffiliatedVisit;
import net.corda.core.contracts.Attachment;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.Vault;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PrivitySharingDataTwoFlow {

    private static String pathSource= "C:\\Users\\Alessandro\\Desktop\\Prova.zip";

    @InitiatingFlow
    @StartableByRPC
    public static class PrivitySharingDataTwoFlowInitiator extends FlowLogic<SignedTransaction> {
        private Party initiator ;
        private Party receiver;
        private UniqueIdentifier idLinState;

        public PrivitySharingDataTwoFlowInitiator(UniqueIdentifier idLinState, Party receiver) {
            this.idLinState = idLinState;
            this.receiver = receiver;
        }

        // Step 1. Get a reference to the notary service on our network and our key pair.

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

            final AffiliatedVisit output = new AffiliatedVisit(input.getIdState(), initiator, Arrays.asList(receiver), true, true, true, false, true,true,true,true);

            SecureHash attachmentHash = null;
            try {
                attachmentHash = SecureHash.parse(uploadAttachment(
                        pathSource,
                        getServiceHub(),
                        getOurIdentity(),
                        "Provazipfile")
                );
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Step 3. Create a new TransactionBuilder object.
            final TransactionBuilder builder = new TransactionBuilder(notary);

            // Step 4. Add the iou as an output state, as well as a command to the transaction builder.
            builder.addInputState(inputState);
            builder.addOutputState(output);
            builder.addCommand(new AffiliatedVisitContract.Commands.PrivitySharingDataOne(), Arrays.asList(this.initiator.getOwningKey(), this.receiver.getOwningKey()));

            FlowSession otherPartySession = initiateFlow(receiver);

            builder.verify(getServiceHub());
            final SignedTransaction ptx = getServiceHub().signInitialTransaction(builder);

            // Step 6. Collect the other party's signature using the SignTransactionFlow.

            final SignedTransaction fullySignedTx = subFlow(
                    new CollectSignaturesFlow(ptx, Arrays.asList(otherPartySession), CollectSignaturesFlow.Companion.tracker()));
            return subFlow(new FinalityFlow(fullySignedTx, Arrays.asList(otherPartySession)));


        }
    }

    @InitiatedBy(PrivitySharingDataTwoFlow.PrivitySharingDataTwoFlowInitiator.class)
    public static class PrivitySharingDataTwoFlowResponder extends FlowLogic<Void>{
        //private variable
        private FlowSession counterpartySession;

        //Constructor
        public PrivitySharingDataTwoFlowResponder(FlowSession counterpartySession) {
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
                    /*String hash = "";
                    String pathDestination="";
                    try {
                        hash= stx.toLedgerTransaction(getServiceHub(), false).getAttachments().get(0).getId().toString();
                    } catch (SignatureException e) {
                        throw new RuntimeException(e);
                    }
                    Attachment content = getServiceHub().getAttachments().openAttachment(SecureHash.parse(hash));
                    try {
                        assert content != null;
                        //content.extractFile(path, new FileOutputStream(new File(path)));
                        InputStream inStream = content.open();
                        byte[] buffer = new byte[inStream.available()];
                        inStream.read(buffer);
                        File targetFile = new File(pathDestination);
                        new FileOutputStream(targetFile).write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }


            }
            final SignTxFlow signTxFlow = new SignTxFlow(counterpartySession);
            final SecureHash txId = subFlow(signTxFlow).getId();

            subFlow(new ReceiveFinalityFlow(counterpartySession, txId));
            return null;
        }

    }

    private static String uploadAttachment(String path, ServiceHub service, Party whoami, String filename) throws IOException {
        SecureHash attachmentHash = service.getAttachments().importAttachment(
                new FileInputStream(new File(path)),
                whoami.toString(),
                filename
        );

        return attachmentHash.toString();
    }
}
