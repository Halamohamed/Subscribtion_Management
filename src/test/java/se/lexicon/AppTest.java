package se.lexicon;

import junit.framework.TestSuite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    private List<Subscriber> subscriberList;
    private SubscriberProcessor processor;
    private SubscriberDAO subscriberDAO;

    @BeforeEach
    void setUp() {
        subscriberDAO = new SubscriberDAO();
        subscriberDAO.save(new Subscriber(1, "alic@email", Plan.BASIC, true, 5));
        subscriberDAO.save(new Subscriber(2, "bobc@email", Plan.FREE, false,2));
        subscriberDAO.save(new Subscriber(3, "carol@email", Plan.PRO, true,1));
        subscriberDAO.save(new Subscriber(4, "dave@email", Plan.FREE, false,0));
        processor = new SubscriberProcessor(subscriberDAO);
    }


    @Test
    public void showActiveSubscriberTest()
    {
        List<Subscriber> activeSubscriber = processor.findSubscribers(subscriberDAO.findAll(),
                subscriber -> subscriber.isActive());
        assertTrue(activeSubscriber.size() == 2);

    }

    @Test
    public void expiringSubscribersTest()
    {
        SubscriberFilter expiringSubscribersFilter = subscriber -> subscriber.getMonthRemaining() <= 1;
        List<Subscriber> expiringSubscribers = processor.findSubscribers(subscriberDAO.findAll(),
                expiringSubscribersFilter);
        assertTrue(expiringSubscribers.size() == 2);
    }

    @Test
    public void showActiveAndExpiringSubscribersTest()
    {
        SubscriberFilter activeSubscriberFilter = subscriber -> subscriber.isActive();
        SubscriberFilter expiringSubscribersFilter = subscriber -> subscriber.getMonthRemaining() <= 1;
        List<Subscriber> activeAndExpiringSubscribers =processor.findSubscribers(subscriberDAO.findAll(),
                subscriber -> (
                        activeSubscriberFilter.matches(subscriber) && expiringSubscribersFilter.matches(subscriber)));
        assertTrue(activeAndExpiringSubscribers.size() == 1);
    }

    @Test
    public void extendingSubscribtionForPayingSubscribersTest()
    {
        SubscriberFilter payingSubscribersFilter = subscriber -> subscriber.getPlan() == Plan.BASIC || subscriber.getPlan() == Plan.PRO;
        SubscriberAction extendingSubscribersAction = subscriber -> subscriber.setMonthRemaining(subscriber.getMonthRemaining() + 2);
        List<Subscriber> payingSubscribers = processor.applyToMatching(subscriberDAO.findAll(),
                payingSubscribersFilter, extendingSubscribersAction);
        assertTrue(payingSubscribers.get(0).getMonthRemaining() == 7);
        assertTrue(payingSubscribers.get(1).getMonthRemaining() == 3);
    }

    @Test
    public void deactivateExpiredFreePlanSubscribersTest()
    {
        SubscriberFilter expiredFreePlanSubscribersFilter = subscriber -> subscriber.getPlan() == Plan.FREE && subscriber.getMonthRemaining() <= 0;
        SubscriberAction markSubscriberInactiveAction = subscriber -> subscriber.setActive(false);
        List<Subscriber> expiredFreePlanSubscribers = processor.applyToMatching(subscriberDAO.findAll(),
                expiredFreePlanSubscribersFilter, markSubscriberInactiveAction);
        assertTrue(!expiredFreePlanSubscribers.get(0).isActive());
    }

    @Test
    public void filterSubscribersByPlanTest()
    {
        SubscriberFilter freePlanFilter = subscriber -> subscriber.getPlan() == Plan.FREE;
        List<Subscriber> freePlanSubscribers = processor.findSubscribers(subscriberDAO.findAll(),
                freePlanFilter);
        assertTrue(freePlanSubscribers.size() == 2);
    }


}
