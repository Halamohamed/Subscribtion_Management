package se.lexicon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubscriberDAO {

    List<Subscriber> subscribers = new ArrayList<>();

    public void save(Subscriber subscriber){
        subscribers.add(subscriber);
    }

    public List<Subscriber> findAll(){
        return subscribers;
    }

    public Optional<Subscriber> findById(int id){
        for (Subscriber subscriber : subscribers) {
            if(subscriber.getId() == id){
                return Optional.of(subscriber);
            }
        }
        return null;
    }
}
