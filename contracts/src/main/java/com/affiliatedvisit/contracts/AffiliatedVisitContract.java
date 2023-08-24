package com.affiliatedvisit.contracts;

import com.affiliatedvisit.states.AffiliatedVisit;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class AffiliatedVisitContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.affiliatedvisit.contracts.AffiliatedVisitContract";

    // A transaction is valid if verify() method of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(LedgerTransaction tx) {

        final CommandData commandData = tx.getCommands().get(0).getValue();

        if (commandData instanceof Commands.NewRequestOfAffiliatedVisit) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("No inputs should be consumed when asking new request of affiliated visit.", tx.getInputStates().isEmpty());
                require.using("No decision made", (!output.isAccepted()) && (!output.isRejected()));
                require.using("No validations performed", (!output.isFirst_category()) && (!output.isSecond_category()));
                require.using("Company Employee have to start the flow", output.getInitiator().getName().getOrganisation().equals("Company Employee"));
                require.using("No sharing of data already executed and no recap requested", !output.isDatashared_one() && !output.isDatashared_two() && !output.isRecap_one() && !output.isRecap_two());
                return null;
            });
        }

        if (commandData instanceof Commands.PrivitySharingDataOne) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("Input state present", !tx.getInputStates().isEmpty());
                require.using("No decision made", (!output.isAccepted()) && (!output.isRejected()));
                require.using("No validations performed", (!output.isFirst_category()) && (!output.isSecond_category()));
                require.using("Company Employee have to start the flow", output.getInitiator().getName().getOrganisation().equals("Company Employee"));
                require.using("First data sharing executed and no recap requested", output.isDatashared_one() && !output.isDatashared_two() && !output.isRecap_one() && !output.isRecap_two());
                return null;
            });
        }
        if (commandData instanceof Commands.RejectionAssessment) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("Input state present", !tx.getInputStates().isEmpty());
                require.using("Rejection decision", (!output.isAccepted()) && output.isRejected());
                require.using("Validation performed by validators of second category", (!output.isFirst_category()) && output.isSecond_category());
                require.using("Health Care Fund have to start the flow", output.getInitiator().getName().getOrganisation().equals("Health Care Fund"));
                require.using("First data sharing executed and no recap requested", output.isDatashared_one() && !output.isDatashared_two() && !output.isRecap_one() && !output.isRecap_two());
                return null;
            });
        }
        if (commandData instanceof Commands.RequestRejected) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("Input state present", !tx.getInputStates().isEmpty());
                require.using("Rejection decision", (!output.isAccepted()) && output.isRejected());
                require.using("Validation performed by validators of both categories", output.isFirst_category() && output.isSecond_category());
                require.using("Health Care Fund have to start the flow", output.getInitiator().getName().getOrganisation().equals("Health Care Fund"));
                require.using("First data sharing executed and no recap requested", output.isDatashared_one() && !output.isDatashared_two() && !output.isRecap_one() && !output.isRecap_two());
                return null;
            });
        }
        if (commandData instanceof Commands.AcceptanceAssessment) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("Input state present", !tx.getInputStates().isEmpty());
                require.using("Acceptance decision", output.isAccepted() && !output.isRejected());
                require.using("Validation performed by validators of second category", (!output.isFirst_category()) && output.isSecond_category());
                require.using("Health Care Fund have to start the flow", output.getInitiator().getName().getOrganisation().equals("Health Care Fund"));
                require.using("First data sharing executed and no recap requested", output.isDatashared_one() && !output.isDatashared_two() && !output.isRecap_one() && !output.isRecap_two());
                return null;
            });
        }
        if (commandData instanceof Commands.RequestAccepted) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("Input state present", !tx.getInputStates().isEmpty());
                require.using("Acceptance decision", output.isAccepted() && !output.isRejected());
                require.using("Validation performed by validators of both categories", output.isFirst_category() && output.isSecond_category());
                require.using("Health Care Fund have to start the flow", output.getInitiator().getName().getOrganisation().equals("Health Care Fund"));
                require.using("First data sharing executed and no recap requested", output.isDatashared_one() && !output.isDatashared_two() && !output.isRecap_one() && !output.isRecap_two());
                return null;
            });
        }
        if (commandData instanceof Commands.NewVisitRequest) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
            AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);
            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("Input state present", !tx.getInputStates().isEmpty());
                require.using("Input state with attributes values", input.isAccepted() && !input.isRejected() && input.isDatashared_one() && !input.isDatashared_two() && input.isFirst_category() && input.isSecond_category() && !input.isRecap_one() && !input.isRecap_two());
                require.using("Acceptance decision", output.isAccepted() && !output.isRejected());
                require.using("Validation performed by validators of both categories", output.isFirst_category() && output.isSecond_category());
                require.using("Health Care Fund have to start the flow", output.getInitiator().getName().getOrganisation().equals("Health Care Fund"));
                require.using("First data sharing executed and first recap required", output.isDatashared_one() && !output.isDatashared_two() && output.isRecap_one() && !output.isRecap_two());
                return null;
            });
        }
        if (commandData instanceof Commands.RecapConventionWithAvailableDatesForBooking) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
            AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);
            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("Input state present", !tx.getInputStates().isEmpty());
                require.using("Input state with attributes values", input.isAccepted() && !input.isRejected() && input.isDatashared_one() && !input.isDatashared_two() && input.isFirst_category() && input.isSecond_category() && input.isRecap_one() && !input.isRecap_two());
                require.using("Acceptance decision", output.isAccepted() && !output.isRejected());
                require.using("Validation performed by validators of both categories", output.isFirst_category() && output.isSecond_category());
                require.using("Medical Office have to start the flow", output.getInitiator().getName().getOrganisation().equals("Medical Office"));
                require.using("First data sharing executed and both recap sent", output.isDatashared_one() && !output.isDatashared_two() && output.isRecap_one() && output.isRecap_two());
                return null;
            });
        }

        if (commandData instanceof Commands.PrivitySharingDataTwo) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);
            AffiliatedVisit input = tx.inputsOfType(AffiliatedVisit.class).get(0);
            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("Input state present", !tx.getInputStates().isEmpty());
                require.using("Input state with attributes values", input.isAccepted() && !input.isRejected() && input.isDatashared_one() && !input.isDatashared_two() && input.isFirst_category() && input.isSecond_category() && input.isRecap_one() && input.isRecap_two()) ;
                require.using("Acceptance decision", output.isAccepted() && !output.isRejected());
                require.using("Validation performed by validators of both categories", output.isFirst_category() && output.isSecond_category());
                require.using("Medical Office have to start the flow", output.getInitiator().getName().getOrganisation().equals("Medical Office"));
                require.using("Both data sharing executed and both recap sent", output.isDatashared_one() && output.isDatashared_two() && output.isRecap_one() && output.isRecap_two());
                return null;
            });
        }
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {

        class NewRequestOfAffiliatedVisit implements Commands {}
        class PrivitySharingDataOne implements Commands {}
        class RejectionAssessment implements Commands {}
        class RequestRejected implements Commands {}
        class AcceptanceAssessment implements Commands {}
        class RequestAccepted implements Commands {}
        class NewVisitRequest implements Commands {}
        class RecapConventionWithAvailableDatesForBooking implements Commands {}
        class PrivitySharingDataTwo implements Commands {}
    }
}