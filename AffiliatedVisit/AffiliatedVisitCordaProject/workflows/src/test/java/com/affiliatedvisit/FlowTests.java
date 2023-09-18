package com.affiliatedvisit;

import com.affiliatedvisit.flows.*;
import com.affiliatedvisit.states.AffiliatedVisit;
import com.google.common.collect.ImmutableList;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.contracts.Attachment;
import net.corda.core.crypto.SecureHash;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.node.NetworkParameters;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.node.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

public class FlowTests {
    private MockNetwork network;
    private StartedMockNode a;
    private StartedMockNode b;
    private StartedMockNode c;

    @Before
    public void setup() {
        MockNetworkParameters mockNetworkParameters = new MockNetworkParameters(
                Arrays.asList(
                        TestCordapp.findCordapp("com.affiliatedvisit.contracts"),
                        TestCordapp.findCordapp("com.affiliatedvisit.flows")
                )).withNetworkParameters(new NetworkParameters(4, Collections.emptyList(),
                10485760, 10485760 * 50, Instant.now(), 1,
                Collections.emptyMap())
        ).withNotarySpecs(Arrays.asList(new MockNetworkNotarySpec(new CordaX500Name("Notary", "Milan", "IT"))));
        network = new MockNetwork(mockNetworkParameters);
        a = network.createPartyNode(new CordaX500Name("Company Employee", "Milan", "IT"));
        b = network.createPartyNode(new CordaX500Name("Health Care Fund", "Milan", "IT"));
        c = network.createPartyNode(new CordaX500Name("Medical Office", "Milan", "IT"));
        network.runNetwork();

    }

    @After
    public void tearDown() {
        network.stopNodes();
    }

    /*
    Test to verify the first Flow and the related transaction:
    Company Employee send to
     */
    @Test
    public void NewRequestOfAffiliatedVisitCorrectFlow() throws Exception {
        NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator flow = new NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator( b.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future=a.startFlow(flow);
        network.runNetwork();
        SignedTransaction ptx= future.get();
        assert (ptx.getTx().getOutputs().get(0).getData() instanceof AffiliatedVisit);
        assert(a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData() instanceof AffiliatedVisit);
        assert(b.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData() instanceof AffiliatedVisit);
        assertEquals(a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),b.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());
        assertEquals(c.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().size(),0);

    }

    @Test
    public void SharingDataCorrect() throws Exception {
        NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator flow = new NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator( b.getInfo().getLegalIdentities().get(0));
        a.startFlow(flow);
        network.runNetwork();

        PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator f= new PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator(Arrays.asList(b.getInfo().getLegalIdentities().get(0),c.getInfo().getLegalIdentities().get(0)));
        CordaFuture<SignedTransaction> future=a.startFlow(f);
        network.runNetwork();
        SignedTransaction ptx= future.get();
        SecureHash hash_attachment = ptx.getTx().getAttachments().get(0);

        assert(ptx.getTx().getOutputs().get(0).getData() instanceof AffiliatedVisit);
        assert(!ptx.getInputs().isEmpty());
        assert(!ptx.getTx().getAttachments().isEmpty());
        assertEquals(a.getServices().getAttachments().openAttachment(hash_attachment).getId(), hash_attachment);
        assertEquals(a.getServices().getAttachments().openAttachment(hash_attachment).getId(), b.getServices().getAttachments().openAttachment(hash_attachment).getId());
        assertEquals(b.getServices().getAttachments().openAttachment(hash_attachment).getId(), c.getServices().getAttachments().openAttachment(hash_attachment).getId());
        assertEquals(a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),b.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());
        assertEquals(a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),c.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());

    }

    @Test
    public void RejectionAssessmentCorrect() throws Exception{
        NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator flow = new NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator( b.getInfo().getLegalIdentities().get(0));
        a.startFlow(flow);
        network.runNetwork();
        PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator flow2= new PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator(Arrays.asList(b.getInfo().getLegalIdentities().get(0),c.getInfo().getLegalIdentities().get(0)));
        a.startFlow(flow2);
        network.runNetwork();

        RejectionAssessmentFlow.RejectionAssessmentFlowInitiator flow3 = new RejectionAssessmentFlow.RejectionAssessmentFlowInitiator(c.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future=b.startFlow(flow3);
        network.runNetwork();
        SignedTransaction ptx= future.get();

        assert(ptx.getTx().getOutputs().get(0).getData() instanceof AffiliatedVisit);
        assert(!ptx.getInputs().isEmpty());
        assertEquals(b.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),c.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());
    }

    @Test
    public void AcceptanceAssessmentCorrect() throws Exception{
        NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator flow = new NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator( b.getInfo().getLegalIdentities().get(0));
        a.startFlow(flow);
        network.runNetwork();
        PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator flow2= new PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator(Arrays.asList(b.getInfo().getLegalIdentities().get(0),c.getInfo().getLegalIdentities().get(0)));
        a.startFlow(flow2);
        network.runNetwork();
        AcceptanceAssessmentFlow.AcceptanceAssessmentFlowInitiator flow3 = new AcceptanceAssessmentFlow.AcceptanceAssessmentFlowInitiator(c.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future=b.startFlow(flow3);
        network.runNetwork();
        SignedTransaction ptx= future.get();

        assert(ptx.getTx().getOutputs().get(0).getData() instanceof AffiliatedVisit);
        assert(!ptx.getInputs().isEmpty());
        assertEquals(b.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),c.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());
    }

    @Test
    public void RequestAcceptedFlowCorrect() throws Exception {
        NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator flow = new NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator( b.getInfo().getLegalIdentities().get(0));
        a.startFlow(flow);
        network.runNetwork();
        PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator flow2= new PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator(Arrays.asList(b.getInfo().getLegalIdentities().get(0),c.getInfo().getLegalIdentities().get(0)));
        a.startFlow(flow2);
        network.runNetwork();
        AcceptanceAssessmentFlow.AcceptanceAssessmentFlowInitiator flow3 = new AcceptanceAssessmentFlow.AcceptanceAssessmentFlowInitiator(c.getInfo().getLegalIdentities().get(0));
        b.startFlow(flow3);
        network.runNetwork();

        RequestAcceptedFlow.RequestAcceptedFlowInitiator f = new RequestAcceptedFlow.RequestAcceptedFlowInitiator(a.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future=b.startFlow(f);
        network.runNetwork();
        SignedTransaction ptx= future.get();

        assert(ptx.getTx().getOutputs().get(0).getData() instanceof AffiliatedVisit);
        assert(!ptx.getInputs().isEmpty());
        assertEquals(b.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());

    }

    @Test
    public void RequestRejectedFlowCorrect() throws Exception {
        NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator flow = new NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator( b.getInfo().getLegalIdentities().get(0));
        a.startFlow(flow);
        network.runNetwork();
        PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator flow2= new PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator(Arrays.asList(b.getInfo().getLegalIdentities().get(0),c.getInfo().getLegalIdentities().get(0)));
        a.startFlow(flow2);
        network.runNetwork();
        RejectionAssessmentFlow.RejectionAssessmentFlowInitiator flow3 = new RejectionAssessmentFlow.RejectionAssessmentFlowInitiator(c.getInfo().getLegalIdentities().get(0));
        b.startFlow(flow3);
        network.runNetwork();

        RequestRejectedFlow.RequestRejectedFlowInitiator f = new RequestRejectedFlow.RequestRejectedFlowInitiator(a.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future=b.startFlow(f);
        network.runNetwork();
        SignedTransaction ptx= future.get();

        assert(ptx.getTx().getOutputs().get(0).getData() instanceof AffiliatedVisit);
        assert(!ptx.getInputs().isEmpty());
        assertEquals(b.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());

    }
    @Test
    public void NewVisitRequestFlowCorrect() throws Exception {
        NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator flow = new NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator( b.getInfo().getLegalIdentities().get(0));
        a.startFlow(flow);
        network.runNetwork();
        PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator flow2= new PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator(Arrays.asList(b.getInfo().getLegalIdentities().get(0),c.getInfo().getLegalIdentities().get(0)));
        a.startFlow(flow2);
        network.runNetwork();
        AcceptanceAssessmentFlow.AcceptanceAssessmentFlowInitiator flow3 = new AcceptanceAssessmentFlow.AcceptanceAssessmentFlowInitiator(c.getInfo().getLegalIdentities().get(0));
        b.startFlow(flow3);
        network.runNetwork();
        RequestAcceptedFlow.RequestAcceptedFlowInitiator flow4 = new RequestAcceptedFlow.RequestAcceptedFlowInitiator(a.getInfo().getLegalIdentities().get(0));
        b.startFlow(flow4);
        network.runNetwork();

        NewVisitRequestFlow.NewVisitRequestFlowInitiator f = new NewVisitRequestFlow.NewVisitRequestFlowInitiator(c.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future=b.startFlow(f);
        network.runNetwork();
        SignedTransaction ptx= future.get();

        assert(ptx.getTx().getOutputs().get(0).getData() instanceof AffiliatedVisit);
        assert(!ptx.getInputs().isEmpty());
        assertEquals(b.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),c.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());

    }

    @Test
    public void RecapConventionWithAvailableDatesForBookingFlowCorrect() throws Exception {
        NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator flow = new NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator( b.getInfo().getLegalIdentities().get(0));
        a.startFlow(flow);
        network.runNetwork();
        PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator flow2= new PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator(Arrays.asList(b.getInfo().getLegalIdentities().get(0),c.getInfo().getLegalIdentities().get(0)));
        a.startFlow(flow2);
        network.runNetwork();
        AcceptanceAssessmentFlow.AcceptanceAssessmentFlowInitiator flow3 = new AcceptanceAssessmentFlow.AcceptanceAssessmentFlowInitiator(c.getInfo().getLegalIdentities().get(0));
        b.startFlow(flow3);
        network.runNetwork();
        RequestAcceptedFlow.RequestAcceptedFlowInitiator flow4 = new RequestAcceptedFlow.RequestAcceptedFlowInitiator(a.getInfo().getLegalIdentities().get(0));
        b.startFlow(flow4);
        network.runNetwork();
        NewVisitRequestFlow.NewVisitRequestFlowInitiator flow5 = new NewVisitRequestFlow.NewVisitRequestFlowInitiator(c.getInfo().getLegalIdentities().get(0));
        b.startFlow(flow5);
        network.runNetwork();

        RecapConventionWithAvailableDatesForBookingFlow.RecapConventionWithAvailableDatesForBookingFlowInitiator f = new RecapConventionWithAvailableDatesForBookingFlow.RecapConventionWithAvailableDatesForBookingFlowInitiator(a.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future=c.startFlow(f);
        network.runNetwork();
        SignedTransaction ptx= future.get();

        assert(ptx.getTx().getOutputs().get(0).getData() instanceof AffiliatedVisit);
        assert(!ptx.getInputs().isEmpty());
        assertEquals(c.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());

    }

    @Test
    public void PrivitySharingDataTwoFlowCorrect() throws Exception {
        NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator flow = new NewRequestOfAffiliatedVisitFlow.NewRequestOfAffiliatedVisitFlowInitiator( b.getInfo().getLegalIdentities().get(0));
        a.startFlow(flow);
        network.runNetwork();
        PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator flow2= new PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator(Arrays.asList(b.getInfo().getLegalIdentities().get(0),c.getInfo().getLegalIdentities().get(0)));
        a.startFlow(flow2);
        network.runNetwork();
        AcceptanceAssessmentFlow.AcceptanceAssessmentFlowInitiator flow3 = new AcceptanceAssessmentFlow.AcceptanceAssessmentFlowInitiator(c.getInfo().getLegalIdentities().get(0));
        b.startFlow(flow3);
        network.runNetwork();
        RequestAcceptedFlow.RequestAcceptedFlowInitiator flow4 = new RequestAcceptedFlow.RequestAcceptedFlowInitiator(a.getInfo().getLegalIdentities().get(0));
        b.startFlow(flow4);
        network.runNetwork();
        NewVisitRequestFlow.NewVisitRequestFlowInitiator flow5 = new NewVisitRequestFlow.NewVisitRequestFlowInitiator(c.getInfo().getLegalIdentities().get(0));
        b.startFlow(flow5);
        network.runNetwork();
        RecapConventionWithAvailableDatesForBookingFlow.RecapConventionWithAvailableDatesForBookingFlowInitiator flow6 = new RecapConventionWithAvailableDatesForBookingFlow.RecapConventionWithAvailableDatesForBookingFlowInitiator(a.getInfo().getLegalIdentities().get(0));
        c.startFlow(flow6);
        network.runNetwork();

        PrivitySharingDataTwoFlow.PrivitySharingDataTwoFlowInitiator f = new PrivitySharingDataTwoFlow.PrivitySharingDataTwoFlowInitiator(a.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future=c.startFlow(f);
        network.runNetwork();
        SignedTransaction ptx= future.get();

        assert(ptx.getTx().getOutputs().get(0).getData() instanceof AffiliatedVisit);
        assert(!ptx.getInputs().isEmpty());
        assertEquals(c.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());

    }

}
