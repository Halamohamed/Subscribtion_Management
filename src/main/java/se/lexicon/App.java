package se.lexicon;

import java.util.List;

/**
 * Starting point of the application
 *  Subscriber, SubscriberDAO, SubscriberFilter
 *  SubscriberAction, SubscriberProcessor
 */
public class App 
{
    private static SubscriberFilter activeSubscriberFilter =subscriber -> subscriber.isActive();
    private static SubscriberFilter expiringSubscribersFilter = subscriber -> subscriber.getMonthRemaining() <= 1;
    private static SubscriberAction extendingSubscribersAction = subscriber -> subscriber.setMonthRemaining(subscriber.getMonthRemaining() + 2);
    private static SubscriberAction markSubscriberInactiveAction = subscriber -> subscriber.setActive(false);

    public static void main( String[] args )
    {
        SubscriberDAO subscriberDAO = new SubscriberDAO();
        SubscriberProcessor processor = new SubscriberProcessor(subscriberDAO);
        subscriberDAO.save(new Subscriber(1, "alic@email", Plan.BASIC, true, 5));
        subscriberDAO.save(new Subscriber(2, "bobc@email", Plan.FREE, false,2));
        subscriberDAO.save(new Subscriber(3, "carol@email", Plan.PRO, true,1));
        subscriberDAO.save(new Subscriber(4, "dave@email", Plan.FREE, false,0));
        List<Subscriber> activeSubscriber = processor.findSubscribers(subscriberDAO.findAll(), activeSubscriberFilter);

        // Print results of active subscribers
        System.out.println("1- Active Subscribers:");
        for (Subscriber subscriber: activeSubscriber){
            System.out.println(subscriber);
        }

        // Print results of expiring subscribers
        List<Subscriber> expiringSubscribers = processor.findSubscribers(subscriberDAO.findAll(), expiringSubscribersFilter);
        System.out.println("2- Expiring Subscribers:");
        for (Subscriber subscriber: expiringSubscribers){
            System.out.println(subscriber);
        }

        // Print results of active and expiring subscribers
        List<Subscriber> activeAndExpiringSubscribers =processor.findSubscribers(subscriberDAO.findAll(),
                subscriber -> (
            activeSubscriberFilter.matches(subscriber) && expiringSubscribersFilter.matches(subscriber)));

        System.out.println("3- Active and Expiring Subscribers:");
        for (Subscriber subscriber: activeAndExpiringSubscribers){
            System.out.println(subscriber);
        }

        // Print results of subscribers with FREE plan
        List<Subscriber> subscriberMatchesByPlan = processor.findSubscribers(subscriberDAO.findAll(),
                subscriber -> subscriber.getPlan() == Plan.FREE);

        System.out.println("4- Subscribers with FREE plan:");
        for (Subscriber subscriber: subscriberMatchesByPlan){
            System.out.println(subscriber);
        }

        // Print results of paying subscribers with BASIC or PRO plan
        List<Subscriber> payingSubscribers = processor.findSubscribers(subscriberDAO.findAll(),
                subscriber -> subscriber.getPlan() == Plan.BASIC || subscriber.getPlan() == Plan.PRO);

        System.out.println("5- Paying Subscribers (BASIC or PRO):");
        for (Subscriber subscriber: payingSubscribers){
            System.out.println(subscriber);
        }

        // Extending expiring subscribers by 2 months
        List<Subscriber> extendingSubscribers = processor.applyToMatching(subscriberDAO.findAll(), expiringSubscribersFilter,
                extendingSubscribersAction);

        // Print results of extending subscribers
        System.out.println("6- Extending Expiring Subscribers by 2 months:");
        for (Subscriber subscriber: extendingSubscribers){
            System.out.println(subscriber);
        }



        List<Subscriber> markSubscriberInactive = processor.applyToMatching(subscriberDAO.findAll(), activeSubscriberFilter,
                markSubscriberInactiveAction);

        System.out.println("7- Marking Active Subscribers as Inactive:");
        for (Subscriber subscriber: markSubscriberInactive){
            System.out.println(subscriber);
        }

        System.out.println("___________________");
        for (Subscriber subscriber: subscriberDAO.findAll()){
            System.out.println("Final state of all subscribers:");
            System.out.println(subscriber);
        }
        System.out.println("___________________");


    }
}
