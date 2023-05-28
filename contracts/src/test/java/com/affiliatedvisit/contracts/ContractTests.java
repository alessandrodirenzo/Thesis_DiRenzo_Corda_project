package com.affiliatedvisit.contracts;

import com.affiliatedvisit.states.AffiliatedVisit;
import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import org.junit.Test;

import java.util.Arrays;

import static net.corda.testing.node.NodeTestUtils.ledger;


public class ContractTests {
    private final MockServices ledgerServices = new MockServices(Arrays.asList("com.template"));
    TestIdentity alice = new TestIdentity(new CordaX500Name("Alice",  "TestLand",  "US"));
    TestIdentity bob = new TestIdentity(new CordaX500Name("Alice",  "TestLand",  "US"));

    @Test
    public void issuerAndRecipientCannotHaveSameEmail() {
        AffiliatedVisit state = new AffiliatedVisit(alice.getParty(),bob.getParty());
        ledger(ledgerServices, l -> {
            l.transaction(tx -> {
                tx.input(AffiliatedVisitContract.ID, state);
                tx.output(AffiliatedVisitContract.ID, state);
                tx.command(alice.getPublicKey(), new AffiliatedVisitContract.Commands.Send());
                return tx.fails(); //fails because of having inputs
            });
            l.transaction(tx -> {
                tx.output(AffiliatedVisitContract.ID, state);
                tx.command(alice.getPublicKey(), new AffiliatedVisitContract.Commands.Send());
                return tx.verifies();
            });
            return null;
        });
    }
}