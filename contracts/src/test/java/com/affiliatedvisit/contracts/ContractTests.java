package com.affiliatedvisit.contracts;

import com.affiliatedvisit.states.AffiliatedVisit;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import org.junit.Test;

import java.util.Arrays;

import static net.corda.testing.node.NodeTestUtils.ledger;
import static net.corda.testing.node.NodeTestUtils.transaction;


public class ContractTests {
    private MockServices ledgerServices = new MockServices(new TestIdentity(new CordaX500Name("TestId", "Milan", "IT")));;
    private final TestIdentity compA= new TestIdentity(new CordaX500Name("Company Employee",  "Milan",  "IT"));
    private final TestIdentity compB = new TestIdentity(new CordaX500Name("Health Care Fund",  "Milan",  "IT"));
    private final TestIdentity compC = new TestIdentity(new CordaX500Name("Medical Office",  "Milan",  "IT"));
    private AffiliatedVisit state1 = new AffiliatedVisit( compA.getParty(), Arrays.asList(compB.getParty()), false, false, false, false);
    private AffiliatedVisit state2 = new AffiliatedVisit( compA.getParty(), Arrays.asList(compB.getParty()), true, true, true, false);

    private AffiliatedVisit state3 = new AffiliatedVisit( compA.getParty(), Arrays.asList(compB.getParty()), true, true, true, true);

    @Test
    public void NewRequestOfAffiliatedVisitCorrectInputsandOutputs() {

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state1);
            tx.output(AffiliatedVisitContract.ID, state1);
            tx.command(Arrays.asList(compA.getPublicKey(), compB.getPublicKey()), new AffiliatedVisitContract.Commands.NewRequestOfAffiliatedVisit());
            tx.fails(); //fails because of having inputs
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.output(AffiliatedVisitContract.ID, state1);
            tx.command(Arrays.asList(compA.getPublicKey(), compB.getPublicKey()), new AffiliatedVisitContract.Commands.NewRequestOfAffiliatedVisit());
            tx.verifies();
            return null;
        });

    }
}