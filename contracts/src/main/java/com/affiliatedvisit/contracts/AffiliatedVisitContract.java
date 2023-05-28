package com.affiliatedvisit.contracts;

import com.affiliatedvisit.states.AffiliatedVisit;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class AffiliatedVisitContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.template.contracts.TemplateContract";

    // A transaction is valid if verify() method of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(LedgerTransaction tx) {

        /* We can use the requireSingleCommand function to extract command data from transaction.
         * However, it is possible to have multiple commands in a single transaction.*/
        //final CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);
        final CommandData commandData = tx.getCommands().get(0).getValue();

        if (commandData instanceof Commands.Send) {
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
        //In our hello-world app, We will only have one command.
        class Send implements Commands {}
    }
}