package dev.gotiger.gTDonationEvent.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DonationActionRegistry {

    private final Map<String, DonationAction> actions = new HashMap<>();

    public void register(DonationAction action) {
        actions.put(action.getName().toUpperCase(), action);
    }

    public Optional<DonationAction> get(String name) {
        return Optional.ofNullable(actions.get(name.toUpperCase()));
    }
}
