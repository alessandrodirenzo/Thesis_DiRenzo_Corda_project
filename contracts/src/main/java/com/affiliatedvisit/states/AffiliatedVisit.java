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
public class AffiliatedVisit implements ContractState{

    //private variables

    private Party initiator;
    private List<Party> receivers;
    private UniqueIdentifier idState;
    private boolean second_category;
    private boolean first_category;
    private boolean accepted;
    private boolean rejected;
    private boolean datashared_one;
    private boolean datashared_two;
    private boolean recap_one;
    private boolean recap_two;

    /* Constructor of Affiliated Visit State */
    public AffiliatedVisit(UniqueIdentifier idState, Party initiator, List<Party> receivers, boolean second_category, boolean first_category, boolean accepted, boolean rejected, boolean datashared_one, boolean datashared_two, boolean recap_one, boolean recap_two) {
        this.initiator = initiator;
        this.receivers = receivers;
        this.second_category = second_category;
        this.first_category = first_category;
        this.accepted = accepted;
        this.rejected = rejected;
        this.datashared_one = datashared_one;
        this.datashared_two = datashared_two;
        this.recap_one = recap_one;
        this.recap_two = recap_two;
        this.idState = idState;
    }


    public boolean isDatashared_one() {
        return datashared_one;
    }

    public boolean isDatashared_two() {
        return datashared_two;
    }

    public boolean isRecap_one() {
        return recap_one;
    }

    public boolean isRecap_two() {
        return recap_two;
    }

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

    public UniqueIdentifier getIdState() {
        return idState;
    }
}