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
                return null;
            });
        }

        if (commandData instanceof Commands.PrivitySharingDataOne) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {

                return null;
            });
        }
        if (commandData instanceof Commands.RejectionAssessment) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {

                return null;
            });
        }
        if (commandData instanceof Commands.RequestRejected) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {

                return null;
            });
        }
        if (commandData instanceof Commands.AcceptanceAssessment) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {

                return null;
            });
        }
        if (commandData instanceof Commands.RequestAccepted) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {

                return null;
            });
        }
        if (commandData instanceof Commands.NewVisitRequest) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {

                return null;
            });
        }
        if (commandData instanceof Commands.AvailableDatesForBooking) {
            //Retrieve the output state of the transaction
            AffiliatedVisit output = tx.outputsOfType(AffiliatedVisit.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {

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
        class AvailableDatesForBooking implements Commands {}
    }
}