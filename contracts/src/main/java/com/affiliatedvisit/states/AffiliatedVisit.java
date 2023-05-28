package com.affiliatedvisit.states;

import com.affiliatedvisit.contracts.AffiliatedVisitContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(AffiliatedVisitContract.class)
public class AffiliatedVisit implements ContractState,LinearState {

    //private variables

    private Party initiator;
    private List<Party> receivers;
    private final UniqueIdentifier idState = new UniqueIdentifier();
    private boolean second_category;
    private boolean first_category;
    private boolean accepted;

    /* Constructor of Affiliated Visit State */
    public AffiliatedVisit( Party initiator, List<Party> receivers, boolean second_category, boolean first_category, boolean accepted, boolean rejected) {
        this.initiator = initiator;
        this.receivers = receivers;
        this.second_category = second_category;
        this.first_category = first_category;
        this.accepted = accepted;
        this.rejected = rejected;
    }

    private boolean rejected;


    //getters
    public Party getInitiator() { return initiator; }
    public  List<Party> getReceivers() { return receivers; }

    /* This method will indicate who are the participants and required signers when
     * this state is used in a transaction. */

    public boolean isSecond_category() {
        return second_category;
    }

    public boolean isFirst_category() {
        return first_category;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isRejected() {
        return rejected;
    }

    @Override
    public List<AbstractParty> getParticipants() {
        List<AbstractParty> entities= new ArrayList<AbstractParty>();
        entities.add(initiator);
        entities.addAll(receivers);
        return entities;
    }
    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        return this.idState;
    }
}