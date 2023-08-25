package com.affiliatedvisit.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.affiliatedvisit.contracts.AffiliatedVisitContract;
import com.affiliatedvisit.states.AffiliatedVisit;
import net.corda.core.contracts.*;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.node.services.vault.QueryCriteriaUtils;
import net.corda.core.transactions.LedgerTransaction;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.*;

import javax.sound.sampled.Line;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PrivitySharingDataOneFlow {

    private static String pathSource= "C:\\Users\\Alessandro\\Desktop\\Prova.zip";

    @InitiatingFlow
    @StartableByRPC
    public static class PrivitySharingDataOneFlowInitiator extends FlowLogic<SignedTransaction> {
        private Party initiator;
        private List<Party> receivers;
        private UniqueIdentifier idLinState;

        public PrivitySharingDataOneFlowInitiator(UniqueIdentifier idLinState, List<Party> receivers) {
            this.idLinState = idLinState;
            this.receivers = receivers;
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

            final AffiliatedVisit output = new AffiliatedVisit( input.getIdState(),initiator, receivers, false, false, false, false, true,false,false,false, "");

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
            builder.addCommand(new AffiliatedVisitContract.Commands.PrivitySharingDataOne(), Arrays.asList(this.initiator.getOwningKey(), this.receivers.get(0).getOwningKey(), this.receivers.get(1).getOwningKey()));
            builder.addAttachment(attachmentHash);

            // Step 5. Verify and sign it with our KeyPair.
            builder.verify(getServiceHub());
            final SignedTransaction ptx = getServiceHub().signInitialTransaction(builder);

            // Step 6. Collect the other party's signature using the SignTransactionFlow.
            ArrayList<AbstractParty> parties = new ArrayList<>();
            parties = (ArrayList) output.getParticipants();



            List<FlowSession> signerFlows = parties.stream()
                    // We don't need to inform ourselves and we signed already.
                    .filter(it -> !it.equals(getOurIdentity()))
                    .map(this::initiateFlow)
                    .collect(Collectors.toList());
            //Party secondParty = output.getReceivers().get(1);
            //FlowSession extra = initiateFlow(secondParty);

            final SignedTransaction fullySignedTx = subFlow(
                    new CollectSignaturesFlow(ptx, signerFlows, CollectSignaturesFlow.Companion.tracker()));
            //signerFlows.add(extra);
            return subFlow(new FinalityFlow(fullySignedTx, signerFlows));

        }
    }

        @InitiatedBy(PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator.class)
        public static class PrivitySharingDataOneFlowResponder extends FlowLogic<Void>{
            //private variable
            private FlowSession counterpartySession;

            //Constructor
            public PrivitySharingDataOneFlowResponder(FlowSession counterpartySession) {
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
                        String pathDestination="C:\\Users\\Alessandro\\Downloads\\";
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
                new FileInputStream(path),
                whoami.toString(),
                filename
        );

        return attachmentHash.toString();
    }
}
