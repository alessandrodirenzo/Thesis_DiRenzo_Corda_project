package com.affiliatedvisit.contracts;

import com.affiliatedvisit.states.AffiliatedVisit;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;
import org.junit.Test;

import java.util.List;

public class StateTests {

    //Mock State test check for if the state has correct parameters type
    @Test
    public void hasFieldsOfCorrectType() throws NoSuchFieldException {
        AffiliatedVisit.class.getDeclaredField("idState");
        AffiliatedVisit.class.getDeclaredField("initiator");
        AffiliatedVisit.class.getDeclaredField("receivers");
        AffiliatedVisit.class.getDeclaredField("first_category");
        AffiliatedVisit.class.getDeclaredField("second_category");
        AffiliatedVisit.class.getDeclaredField("accepted");
        AffiliatedVisit.class.getDeclaredField("rejected");
        assert(AffiliatedVisit.class.getDeclaredField("idState").getType().equals(UniqueIdentifier.class));
        assert(AffiliatedVisit.class.getDeclaredField("initiator").getType().equals(Party.class));
        assert(AffiliatedVisit.class.getDeclaredField("receivers").getType().equals(List.class));
        assert(AffiliatedVisit.class.getDeclaredField("first_category").getType().equals(boolean.class));
        assert(AffiliatedVisit.class.getDeclaredField("second_category").getType().equals(boolean.class));
        assert(AffiliatedVisit.class.getDeclaredField("accepted").getType().equals(boolean.class));
        assert(AffiliatedVisit.class.getDeclaredField("rejected").getType().equals(boolean.class));
    }
}