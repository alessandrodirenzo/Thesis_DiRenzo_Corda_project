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

    UniqueIdentifier id = new UniqueIdentifier();
    private AffiliatedVisit state1 = new AffiliatedVisit(id, compA.getParty(), Arrays.asList(compB.getParty()), false, false, false, false,false, false, false,false, "message 1");
    private AffiliatedVisit state2 = new AffiliatedVisit(id, compA.getParty(), Arrays.asList(compB.getParty(),compC.getParty()), false, false, false, false,true, false, false,false, "");
    private AffiliatedVisit state3 = new AffiliatedVisit(id, compB.getParty(), Arrays.asList(compC.getParty()), true, false, false,true, true, false,false,false, "message 1");

    private AffiliatedVisit state4 = new AffiliatedVisit(id, compB.getParty(), Arrays.asList(compC.getParty()), true, false, true,false, true, false,false,false, "message 1");

    private AffiliatedVisit state5 = new AffiliatedVisit(id, compB.getParty(), Arrays.asList(compA.getParty()), true, false, true,true, true, false,false,false, "message 1");
    private AffiliatedVisit state6 = new AffiliatedVisit(id, compB.getParty(), Arrays.asList(compA.getParty()), true, true, false,true, true, false,false,false, "message 1");

    private AffiliatedVisit state7 = new AffiliatedVisit(id, compB.getParty(), Arrays.asList(compA.getParty()), true, true, true,false, true, false,false,false, "message 1");

    private AffiliatedVisit state8 = new AffiliatedVisit(id, compB.getParty(), Arrays.asList(compA.getParty()), true, true, false,true, true, true,false,false, "message 1");

    private AffiliatedVisit state9 = new AffiliatedVisit(id, compB.getParty(), Arrays.asList(compC.getParty()), true, true, true,false, true, false,true,false, "message 1");

    private AffiliatedVisit state10 = new AffiliatedVisit(id, compB.getParty(), Arrays.asList(compC.getParty()), true, true, true,false, true, false,false,false, "message 1");

    private AffiliatedVisit state11 = new AffiliatedVisit(id, compC.getParty(), Arrays.asList(compA.getParty()), true, true, true,false, true, false,true,true, "message 1");

    private AffiliatedVisit state12 = new AffiliatedVisit(id, compC.getParty(), Arrays.asList(compA.getParty()), true, true, true,false, true, true,true,true, "");

    private AffiliatedVisit state13 = new AffiliatedVisit(id, compC.getParty(), Arrays.asList(compA.getParty()), true, true, true,false, true, false,true,true, "message 1");

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

    @Test
    public void PrivitySharingDataOneCorrectInputandOutputs(){
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state1);
            tx.output(AffiliatedVisitContract.ID, state3);
            tx.command(Arrays.asList(compA.getPublicKey(), compB.getPublicKey(), compC.getPublicKey()), new AffiliatedVisitContract.Commands.PrivitySharingDataOne());
            tx.fails(); //fails because of decisions taken in state3
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state1);
            tx.output(AffiliatedVisitContract.ID, state2);
            tx.command(Arrays.asList(compA.getPublicKey(), compB.getPublicKey(), compC.getPublicKey()), new AffiliatedVisitContract.Commands.PrivitySharingDataOne());
            tx.verifies();
            return null;
        });
    }

    @Test
    public void RejectionAssessmentCorrectInputandOutputs(){
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state2);
            tx.output(AffiliatedVisitContract.ID, state5);
            tx.command(Arrays.asList(compB.getPublicKey(), compC.getPublicKey()), new AffiliatedVisitContract.Commands.RejectionAssessment());
            tx.fails(); //fails because of both decisions accepted and rejected
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state2);
            tx.output(AffiliatedVisitContract.ID, state4);
            tx.command(Arrays.asList(compB.getPublicKey(), compC.getPublicKey()), new AffiliatedVisitContract.Commands.RejectionAssessment());
            tx.fails(); //fails because of accepted decision instead of rejected
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state2);
            tx.output(AffiliatedVisitContract.ID, state3);
            tx.command(Arrays.asList(compB.getPublicKey(), compC.getPublicKey()), new AffiliatedVisitContract.Commands.RejectionAssessment());
            tx.verifies();
            return null;
        });
    }

    @Test
    public void AcceptanceAssessmentCorrectInputandOutputs(){
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state2);
            tx.output(AffiliatedVisitContract.ID, state5);
            tx.command(Arrays.asList(compB.getPublicKey(), compC.getPublicKey()), new AffiliatedVisitContract.Commands.AcceptanceAssessment());
            tx.fails(); //fails because of both decisions accepted and rejected
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state2);
            tx.output(AffiliatedVisitContract.ID, state4);
            tx.command(Arrays.asList(compB.getPublicKey(), compC.getPublicKey()), new AffiliatedVisitContract.Commands.AcceptanceAssessment());
            tx.verifies();
            return null;
        });
    }

    @Test
    public void RequestRejectedCorrectInputandOutputs(){
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state3);
            tx.output(AffiliatedVisitContract.ID, state8);
            tx.command(Arrays.asList(compB.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.RequestRejected());
            tx.fails(); //fails because of the second data sharing has not been performed
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state4);
            tx.output(AffiliatedVisitContract.ID, state6);
            tx.command(Arrays.asList(compB.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.RequestRejected());
            tx.fails(); //fails because of the attribute accepted is true
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state3);
            tx.output(AffiliatedVisitContract.ID, state6);
            tx.command(Arrays.asList(compB.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.RequestRejected());
            tx.verifies();
            return null;
        });
    }

    @Test
    public void RequestAcceptedCorrectInputandOutputs() {
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state4);
            tx.output(AffiliatedVisitContract.ID, state8);
            tx.command(Arrays.asList(compB.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.RequestAccepted());
            tx.fails(); //fails because of the second data sharing has not been performed
            return null;
        });
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state3);
            tx.output(AffiliatedVisitContract.ID, state7);
            tx.command(Arrays.asList(compB.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.RequestAccepted());
            tx.fails(); //fails because of the attribute rejected is true
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state4);
            tx.output(AffiliatedVisitContract.ID, state7);
            tx.command(Arrays.asList(compB.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.RequestAccepted());
            tx.verifies();
            return null;
        });
    }

    @Test
    public void NewVisitRequestCorrectInputandOutputs() {
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state6);
            tx.output(AffiliatedVisitContract.ID, state9);
            tx.command(Arrays.asList(compB.getPublicKey(), compC.getPublicKey()), new AffiliatedVisitContract.Commands.NewVisitRequest());
            tx.fails(); //fails because of the attribute rejected is true
            return null;
        });
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state7);
            tx.output(AffiliatedVisitContract.ID, state10);
            tx.command(Arrays.asList(compB.getPublicKey(), compC.getPublicKey()), new AffiliatedVisitContract.Commands.NewVisitRequest());
            tx.fails(); //fails because of the attribute recap_one is false
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state7);
            tx.output(AffiliatedVisitContract.ID, state9);
            tx.command(Arrays.asList(compB.getPublicKey(), compC.getPublicKey()), new AffiliatedVisitContract.Commands.NewVisitRequest());
            tx.verifies();
            return null;
        });
    }
    @Test
    public void RecapConventionWithAvailableDatesCorrectInputandOutputs() {
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state8);
            tx.output(AffiliatedVisitContract.ID, state11);
            tx.command(Arrays.asList(compC.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.RecapConventionWithAvailableDatesForBooking());
            tx.fails(); //fails because of the attribute rejected is true
            return null;
        });
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state9);
            tx.output(AffiliatedVisitContract.ID, state10);
            tx.command(Arrays.asList(compC.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.RecapConventionWithAvailableDatesForBooking());
            tx.fails(); //fails because of the attribute recap_one is false
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state9);
            tx.output(AffiliatedVisitContract.ID, state11);
            tx.command(Arrays.asList(compC.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.RecapConventionWithAvailableDatesForBooking());
            tx.verifies();
            return null;
        });
    }

    @Test
    public void PrivitySharingDataTwoCorrectInputandOutputs() {
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state10);
            tx.output(AffiliatedVisitContract.ID, state12);
            tx.command(Arrays.asList(compC.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.PrivitySharingDataTwo());
            tx.fails(); //fails because of the input state has attribute recap_one set to false
            return null;
        });
        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state11);
            tx.output(AffiliatedVisitContract.ID, state13);
            tx.command(Arrays.asList(compC.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.PrivitySharingDataTwo());
            tx.fails(); //fails because of the output state has the attribute datashared_two set ot false
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.input(AffiliatedVisitContract.ID, state11);
            tx.output(AffiliatedVisitContract.ID, state12);
            tx.command(Arrays.asList(compC.getPublicKey(), compA.getPublicKey()), new AffiliatedVisitContract.Commands.PrivitySharingDataTwo());
            tx.verifies();
            return null;
        });
    }
}