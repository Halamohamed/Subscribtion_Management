package se.lexicon;

import java.util.ArrayList;
import java.util.List;

public class SubscriberProcessor {

    private SubscriberDAO subscriberDAO;

    public SubscriberProcessor(SubscriberDAO subscriberDAO) {
        this.subscriberDAO = subscriberDAO;
    }

    public List<Subscriber> findSubscribers(List<Subscriber> subscribers, SubscriberFilter filter){
        List<Subscriber> result = new java.util.ArrayList<>();
        for (Subscriber subscriber : subscribers) {
            if(filter.matches(subscriber)){
                result.add(subscriber);
            }
        }
        return result;
    }

    public List<Subscriber> applyToMatching(List<Subscriber> subscribers, SubscriberFilter filter, SubscriberAction action){
        List<Subscriber> result = new ArrayList<>();
        for (Subscriber subscriber: subscribers){
            if(filter.matches(subscriber)){
                action.run(subscriber);
                result.add(subscriber);
            }
        }
        return result;
    }
}
