package com.affiliatedvisit;

import com.affiliatedvisit.flows.NewRequestOfAffiliatedVisitFlow;
import com.affiliatedvisit.flows.PrivitySharingDataOneFlow;
import com.affiliatedvisit.states.AffiliatedVisit;
import com.google.common.collect.ImmutableList;
import net.corda.core.concurrent.CordaFuture;
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

        PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator f= new PrivitySharingDataOneFlow.PrivitySharingDataOneFlowInitiator(a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),Arrays.asList(b.getInfo().getLegalIdentities().get(0),c.getInfo().getLegalIdentities().get(0)));
        CordaFuture<SignedTransaction> future=a.startFlow(f);
        network.runNetwork();
        SignedTransaction ptx= future.get();

        assert(ptx.getTx().getOutputs().get(0).getData() instanceof AffiliatedVisit);
        assert(!ptx.getInputs().isEmpty());
        assert(!ptx.getTx().getAttachments().isEmpty());
        assertEquals(a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),b.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());
        assertEquals(a.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState(),c.getServices().getVaultService().queryBy(AffiliatedVisit.class).getStates().get(0).getState().getData().getIdState());

    }
}
