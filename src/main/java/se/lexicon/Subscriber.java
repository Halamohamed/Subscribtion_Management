package se.lexicon;

public class Subscriber {

    private int id;
    private String email;
    private Plan plan;
    private boolean active;
    private int monthRemaining;

    public Subscriber(int id, String email, Plan plan, boolean active, int monthRemaining) {
        this.id = id;
        this.email = email;
        this.plan = plan;
        this.active = active;
        this.monthRemaining = monthRemaining;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Plan getPlan() {
        return plan;
    }

    public boolean isActive() {
        return active;
    }

    public int getMonthRemaining() {
        return monthRemaining;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMonthRemaining(int monthRemaining) {
        this.monthRemaining = monthRemaining;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", plan=" + plan +
                ", active=" + active +
                ", monthRemaining=" + monthRemaining +
                '}';
    }
}

